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
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserWordFrequency implements Command {
    private static List<String> validLangs = Arrays.asList("american", "canadian", "british");
    /**
     * @param event
     * @param args
     */
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IUser target = event.getMessage().getMentions().get(0); // guarantee to have 1
        Map<String, Integer> freqMap = new HashMap<>();
        Map<String, Integer> typoMap = new HashMap<>();

        float numChars = 1, numWords = 1,  numMsgs = 1, matchErrors = 1, numTypos = 1, numEdits = 1;

        JLanguageTool langTool = getTool(args);

        long startTime = System.currentTimeMillis(); // convert to nanoTime later
        for (IChannel channel : event.getGuild().getChannels()) {
            try {
                Instant oneHour = Instant.now().minus(1, ChronoUnit.HOURS); //
                for (IMessage msg : channel.getMessageHistoryTo(oneHour).stream().filter(msg -> msg.getAuthor().equals(target)).collect(Collectors.toList())) {
                    numChars += msg.getContent().length();
                    numMsgs++;
                    // System.out.println(numMsgs + " : " + msg.getFormattedContent() + " : " + msg.getTimestamp());
                    if (msg.getEditedTimestamp().isPresent()) numEdits++;
                    if (msg == null || msg.getFormattedContent() == null) continue; // if only picture or smth who knows
                    for (String word : msg.getFormattedContent().trim().toLowerCase().replaceAll("[^A-Za-z0-9]", "").split("\\s")) {
                        numWords++;
                        if (BotUtils.dictionary.contains(word)) { // word is in dictionary
                            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1); // @tterrag#1098
                        } else { // perform spell check
                            try {
                                List<RuleMatch> matches = langTool.check(word);
                                for (RuleMatch match : matches) {
                                    if (!match.getSuggestedReplacements().isEmpty()) { // if there is a suggestion
                                        String correction = match.getSuggestedReplacements().get(0);
                                        correction = correction.substring(1, correction.length() - 1); // get rid of square brackets
                                        freqMap.put(correction, freqMap.getOrDefault(correction, 0) + 1); //@tterrag#1098
                                    }
                                }
                            } catch (IOException e) { // just count errors and moveon
                                matchErrors++;
                            }
                            numTypos++;
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
                .withTitle("Message Stats for " + BotUtils.getNickOrDefault(target, event.getGuild()))
                .withColor(Visuals.getVibrantColor())
                .withThumbnail(target.getAvatarURL())
                .withFooterText("This took " + minutes + ":" + (seconds<10? "0":"") + seconds + " with " + matchErrors + " dict errors");

//        StringBuilder sb = new StringBuilder(
//                "Stats for past 1 week" +
//                        "```js" +
//                        "\nMessages:\t" + numMsgs +
//                        "\nCharacters:\t" + numChars + "\t~" + numChars/numMsgs + " chars/msg" +
//                        "\nWords:\t" + numWords + "\t~" + numChars/numWords + " chars/word" + "\t~" + numWords/numMsgs + " words/msg" +
//                        "\nTypos:\t" + numTypos + "\t~" + numTypos/numWords + " typos/word" +
//                        "\nEdits:\t" + numEdits + "\t~" + numEdits/numMsgs + " edits/msg" +
//                        "```\n"
//        );

        StringBuilder sb = new StringBuilder();

        GridTable table = GridTable.of(1,3)
                .put(0,0, Cell.of("Messages", "Characters", "Words", "Typos", "Edits"))
                .put(0, 1, Cell.of(str(numMsgs), str(numChars), str(numWords), str(numTypos), str(numEdits)))
                .put(0, 2, Cell.of("",
                        str(numChars/numMsgs) + " chars/msg",
                        str(numChars/numWords) + " chars/word",
                        str(numTypos/numWords) + " typos/word",
                        str(numEdits/numMsgs) + " edits/msg"));

        table = Border.DOUBLE_LINE.apply(table);

        sb.append("```js\n" + TableUtil.render(table).toString() + "```");

        // word frequency
        sb.append("\nWord frequencies\n");
        generateSB(freqMap, sb, 10);

        // typo frequency
        sb.append("\nCommonly misspelled words\n");
        generateSB(typoMap, sb, 10);

        // check length of description -- use limit length regardless
        eb.withDesc(BotUtils.limitStrLen(sb.toString(), 2048, false, true, '\n'));

        BotUtils.send(event.getChannel(), eb);

        // generate gist
        String json = BotUtils.getStringFromUrl(GistUtils.makeGistGetUrl(
                "Aspect :: Message Stats for " + BotUtils.getNickOrDefault(target, event.getGuild()),
                "Stats for past 1 week in" + event.getGuild().getName(),
                sb.toString()));

        GistContainer gist = BotUtils.gson.fromJson(json, GistContainer.class);
        BotUtils.send(event.getChannel(), "To view full statistics, visit\n\n" + gist.getHtml_url());
    }

    private void generateSB(Map<String, Integer> typoMap, StringBuilder sb, int exitIteration) {
        int rankCounter = 1;
        for (Map.Entry<String, Integer> entry : typoMap.entrySet()) {
            if (rankCounter == exitIteration) break; // break here
            sb.append(rankCounter + " : " + entry.getKey() + " -- " + entry.getValue() + "\n");
        }
    }

    private JLanguageTool getTool(List<String> args) {
        if (args.size() == 1) return LangUtil.langToolAmerican;
        if (args.get(1).startsWith("can")) return LangUtil.langToolEnglish;

        return LangUtil.langToolAmerican; // just use this as default case
    }

    private String str(float f) {
        return String.valueOf(f);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return event.getMessage().getMentions().size() == 1 &&
                (args.size() != 2 || validLangs.contains(args.get(1)));
    }

}
