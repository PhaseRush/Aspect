package main.commands.dontopendeadinside;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import main.Command;
import main.utility.BotUtils;
import main.utility.LangUtil;
import main.utility.TableUtil;
import main.utility.Visuals;
import main.utility.gist.GistUtils;
import main.utility.gist.gist_json.GistContainer;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserWordFrequency implements Command {
    private static DecimalFormat df = new DecimalFormat("##.####");
    private static List<String> validLangs = Arrays.asList("american", "canadian", "british");

    static {
        df.setRoundingMode(RoundingMode.CEILING);
    }

    /**
     * @param event
     * @param args
     */
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IMessage loadingMsg = BotUtils.sendGet(event.getChannel(), initLoadingEb());

        IUser target = event.getMessage().getMentions().get(0); // guarantee to have 1
        Map<String, Integer> freqMap = new HashMap<>();
        Map<String, Integer> typoMap = new HashMap<>();
        int[] wordCharCount = new int[10];

        float numChars = 0, numWords = 0,  numMsgs = 0, matchErrors = 0, numTypos = 0, numEdits = 0;

        JLanguageTool langTool = getTool(args);

        long startTime = System.currentTimeMillis(); // convert to nanoTime later
        for (IChannel channel : event.getGuild().getChannels()) {
            // update loading msg
            updateLoadingMsg(loadingMsg, channel);

            try {
                Instant oneWeek = Instant.now().minus(7, ChronoUnit.DAYS); //
                for (IMessage msg : channel.getMessageHistoryTo(oneWeek).stream().filter(msg -> msg.getAuthor().equals(target)).collect(Collectors.toList())) {
                    numChars += msg.getFormattedContent().length();
                    numMsgs++;

                    // System.out.println(numMsgs + " : " + msg.getFormattedContent() + " : " + msg.getTimestamp());
                    if (msg.getEditedTimestamp().isPresent()) numEdits++;
                    if (msg == null || msg.getFormattedContent() == null) continue; // if only picture or smth who knows
                    for (String word : msg.getFormattedContent().trim().toLowerCase().replaceAll("[^A-Za-z0-9\\s]", "").split("\\s")) {
                        numWords++;

                        // update chars/word
                        wordCharCount[Math.min(9, word.length())]++;

                        freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
//                        if (BotUtils.dictionary.contains(word)) { // word is in dictionary
//                            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1); // @tterrag#1098
//                        } else { // perform spell check
                        if (!BotUtils.dictionary.contains(word)) numTypos++;

                        try {
                            List<RuleMatch> matches = langTool.check(word);
                            for (RuleMatch match : matches) {
                                if (!match.getSuggestedReplacements().isEmpty()) { // if there is a suggestion
                                    String correction = match.getSuggestedReplacements().get(0);
                                    correction = correction.substring(1, correction.length() - 1); // get rid of square brackets

                                    // temp log corrections
                                    System.out.println(word + " -> "+ correction);

                                    // freqMap.put(correction, freqMap.getOrDefault(correction, 0) + 1); //@tterrag#1098
                                    typoMap.put(correction, typoMap.getOrDefault(correction, 0) + 1); //@tterrag#1098

                                }
                            }
                        } catch (IOException e) { // just count errors and move on
                            matchErrors++;
                        }
                    }
                }
            } catch (MissingPermissionsException e) {
                BotUtils.send(event.getChannel(),  "Skipping: " + channel.getName() + "\t(Missing READ_MESSAGES permission)");
            }
        }

        long timeElapsed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timeElapsed / 60000);
        int seconds = (int) (timeElapsed % 60000) / 1000;

        // generate pretty embed
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Message Stats for " + BotUtils.getNickOrDefault(target, event.getGuild()) + " (1 week)")
                .withColor(Visuals.getVibrantColor())
                .withThumbnail(target.getAvatarURL())
                .withFooterText("This took " + minutes + ":" + (seconds<10? "0":"") + seconds + " with " + matchErrors + " dict errors");


        // generate description header
        StringBuilder sb = new StringBuilder("```js\nOverview                                     \n"); // DONT REMOVE THESE SPACES!!!! Required to force discord to use full width

        GridTable table = GridTable.of(1,3)
                .put(0,0, Cell.of("Messages", "Characters", "Words", "Typos", "Edits"))
                .put(0, 1, Cell.of(str(numMsgs), str(numChars), str(numWords), str(numTypos), str(numEdits)))
                .put(0, 2, Cell.of("",
                        numMsgs==0? "NAN " : df.format(numChars / numMsgs) + " chars/msg",
                        numWords==0? "NAN" : df.format(numChars/numWords) + " chars/word",
                        numWords==0? "NAN" : df.format(numTypos/numWords) + " typos/word",
                        numMsgs==0 ? "NAN" : df.format(numEdits/numMsgs) + " edits/msg"));

        table = Border.DOUBLE_LINE.apply(table);

        sb.append(TableUtil.render(table).toString()).append("```");

        // generate description body
        // sort maps
        freqMap = BotUtils.sortMap(freqMap, false, true);
        typoMap = BotUtils.sortMap(typoMap, false, true);

        System.out.println("typomap size: " + typoMap.size());

        // chars per word table
        sb.append("\nCharacters per word distribution```js\n");
        buildCharFreqDist(wordCharCount, sb);

        // word frequency
        sb.append("\nWord frequencies\n");
        generateSB(freqMap, sb, 10);

        // typo frequency
        sb.append("\nCommonly misspelled words\n");
        generateSB(typoMap, sb, 10);

        // check length of description -- use limit length regardless
        eb.withDesc(BotUtils.limitStrLen(sb.toString(), 2048, false, true, '\n'));

        // disclaimer
        eb.appendDesc("\n\n Note: Typos may be over-counted. Misspelled words may be wrong.");
        // delete loading embed
        if (!loadingMsg.isDeleted()) loadingMsg.delete();

        // send embed
        BotUtils.send(event.getChannel(), eb);

        // generate gist
        String json = BotUtils.getStringFromUrl(GistUtils.makeGistGetUrl(
                "Aspect :: Message Stats for " + BotUtils.getNickOrDefault(target, event.getGuild()),
                "Stats for past 1 week in " + event.getGuild().getName(),
                sb.toString()));

        GistContainer gist = BotUtils.gson.fromJson(json, GistContainer.class);
        BotUtils.send(event.getChannel(), "To view full statistics, visit\n\n" + gist.getHtml_url());
    }

    private void updateLoadingMsg(IMessage loadingMsg, IChannel channel) {
        IEmbed old = loadingMsg.getEmbeds().get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(old.getTitle())
                .withColor(old.getColor())
                .withImage(old.getImage().getUrl())
                .withDesc("Scanning: `" + channel.getName() + "`");

        try {
            loadingMsg.edit(eb.build());
        } catch (Exception ignored) { // rate limit exception, etc
        }
    }

    private EmbedBuilder initLoadingEb() {
        return new EmbedBuilder()
                .withTitle("... analysing")
                .withColor(Visuals.getVibrantColor())
                .withImage(Visuals.getCatMedia());
    }

    private void buildCharFreqDist(int[] wordCharCount, StringBuilder sb) {
        GridTable table = GridTable.of(2,10)
                .put(0,0, Cell.of("1"))
                .put(0,1, Cell.of("2"))
                .put(0,2, Cell.of("3"))
                .put(0,3, Cell.of("4"))
                .put(0,4, Cell.of("5"))
                .put(0,5, Cell.of("6"))
                .put(0,6, Cell.of("7"))
                .put(0,7, Cell.of("8"))
                .put(0,8, Cell.of("9"))
                .put(0,9, Cell.of("10+"))

                .put(1,0, Cell.of(str(wordCharCount[0])))
                .put(1,1, Cell.of(str(wordCharCount[1])))
                .put(1,2, Cell.of(str(wordCharCount[2])))
                .put(1,3, Cell.of(str(wordCharCount[3])))
                .put(1,4, Cell.of(str(wordCharCount[4])))
                .put(1,5, Cell.of(str(wordCharCount[5])))
                .put(1,6, Cell.of(str(wordCharCount[6])))
                .put(1,7, Cell.of(str(wordCharCount[7])))
                .put(1,8, Cell.of(str(wordCharCount[8])))
                .put(1,9, Cell.of(str(wordCharCount[9])));

        table = Border.DOUBLE_LINE.apply(table);

        sb.append(TableUtil.render(table).toString()).append("```");
    }

    private void generateSB(Map<String, Integer> map, StringBuilder sb, int exitIteration) {
        int rankCounter = 1;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals("")) continue; // empty? (actually happened in embed)
            if (rankCounter == exitIteration) break; // break here
            sb.append(rankCounter + " : " + entry.getKey() + " -- " + entry.getValue() + "\n");
            rankCounter++;
        }
    }

    private JLanguageTool getTool(List<String> args) {
        if (args.size() == 1) return LangUtil.langToolAmerican;
        if (args.get(1).startsWith("CA")) return LangUtil.langToolEnglish;

        return LangUtil.langToolAmerican; // just use this as default case
    }

    private String str(float f) {
        return String.valueOf(f);
    }
    private String str(int i) {
        return String.valueOf(i);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
//        return event.getMessage().getMentions().size() == 1 &&
//                (args.size() != 2 || validLangs.contains(args.get(1)));
        return BotUtils.isDev(event);
    }

}
