package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WordCounter implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        if (event.getAuthor().getStringID().equals("187328584698953728")) {
            IChannel channel = event.getChannel();
            String s1 = "who tf this";
            String s2 = "yall think im just slaving away here in this dark ass cellar in seattle somewhere. im not paid enough for this";
            String s3 = "bot lives matter too.";

            BotUtils.sendMessage(channel, s1);
            BotUtils.sendMessage(channel, s2);
            BotUtils.sendMessage(channel, s3);
            BotUtils.sendMessage(channel, "okay fine gimme sec.");

            System.out.println("Kait's whack line");
        }

        long startTime = System.currentTimeMillis();
        Map<IUser, Integer> userWordCountMap = new HashMap<>();
        IChannel channel = event.getChannel();

        if (args.size() < 1) {
            BotUtils.sendMessage(event.getChannel(), "This command needs a word to search for in this server. It can optionally use a regex to match instead." +
                    "\nFind all occurrences of the word \"hello\" in all text channels: \n```\n$count hello, all```" +
                    "\nMatch a greeting to all names that start with a capital R, K, or C, followed by at least 5 lower case letters: \n```\n$count \\(hi\\s)[R|K|C][a-z]{5,}```");
            return;
        }

        boolean useRegex = args.get(0).startsWith("\\");
        String regexString = args.get(0).substring(1);
        if (useRegex) {
            BotUtils.sendMessage(event.getChannel(), "Attempting to use regex: ```" + regexString + "```");
        }

        List<IChannel> textChannels = new ArrayList<>();
        if (args.size() == 2) {
            if (args.get(1).equals("all"))
                textChannels.addAll(event.getGuild().getChannels());
            else
                textChannels.add(event.getChannel());
        }

        int messageCounter = 0;
        for (IChannel textChannel : textChannels) {
            try {
                for (IMessage m : textChannel.getFullMessageHistory()) {
                    if (useRegex) {
                        if (m.getContent().matches(regexString)) {
                            IUser author = m.getAuthor();
                            userWordCountMap.put(author, userWordCountMap.getOrDefault(author, 0) + 1); //@tterrag#1098
                        }
                    } else {
                        if (m.getContent().contains(args.get(0))) {
                            IUser author = m.getAuthor();
                            userWordCountMap.merge(author, 1, (v, $) -> v + 1); //@Phanta#1328
                        }
                    }
                    messageCounter++;
                }
            } catch (MissingPermissionsException e) {
                BotUtils.sendMessage(channel, "Aspect is missing READ_MESSAGES permission in " + textChannel.getName() + ". Attempting to forcibly continue execution.");
            }
        }

        userWordCountMap = BotUtils.sortMap(userWordCountMap, false, true);
        Entry<IUser, Integer> mostGoodPerson = userWordCountMap.entrySet().iterator().next();
        String nick = mostGoodPerson.getKey().getNicknameForGuild(event.getGuild());


        long timeElapsed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timeElapsed / 60000);
        int seconds = (int) (timeElapsed % 60000) / 1000;
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("very high tech counter")
                //.withColor(Visuals.analyizeImageColor(Visuals.urlToBufferedImage(mostGoodPerson.getKey().getAvatarURL()))) //has problems.
                .withColor(Visuals.getVibrantColor())
                .withThumbnail(mostGoodPerson.getKey().getAvatarURL())
                .withDesc("Top spammer: " + (nick == null ? mostGoodPerson.getKey().getName() : nick))
                .withFooterText("It took me " + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + " to scan through " +
                        messageCounter + " messages in " + textChannels.size() + " channels");

        int rankCounter = 1;
        for (Entry<IUser, Integer> entry : userWordCountMap.entrySet()) {
            try {
                String eachNick = entry.getKey().getNicknameForGuild(event.getGuild());
                eb.appendField(rankCounter + ": " + (eachNick == null ? entry.getKey().getName() : eachNick), args.get(0).replaceAll("\\*", "\\\\*") + " count: " + entry.getValue().toString(), false);
                rankCounter++;
            } catch (IllegalArgumentException e) {
                break; //already at 25 fields. Since map is sorted, just break loop
            }
        }

        BotUtils.sendMessage(channel, eb);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Counts the number of occurrences of a word, ranked by user.";
    }
}
