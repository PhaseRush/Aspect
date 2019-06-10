package main.commands.dontopendeadinside;

import main.Aspect;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;

public class
WordCounter implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        handleKaitlyn(event); // yes.
        BotUtils.send(event.getChannel(), initLoadingEb());

        long startTime = System.currentTimeMillis();
        Map<IUser, Integer> userWordCountMap = new LinkedHashMap<>();
        Map<IUser, Integer> userMsgCountMap = new LinkedHashMap<>();
        IChannel channel = event.getChannel();

        if (args.size() < 1) {
            sendErrorMsg(event);
            return;
        }

        boolean useRegex = args.get(0).startsWith("/"); // was "\\"
        String regexString = args.get(0).substring(1); // set this regardless if being used or not. Low overhead

        List<IChannel> textChannels = new ArrayList<>();
        boolean useAllChannels = false;
        Instant messagesFromThis = Instant.now().minus(1, ChronoUnit.WEEKS);// default to 1 week
        if (args.size() == 2) {
            switch (args.get(1)) {
                case "every":
                    textChannels.addAll(event.getGuild().getChannels());
                    useAllChannels = true;
                case "all":
                    messagesFromThis = Instant.MIN;
            }
        } else if (args.size() == 1) {
            textChannels.add(event.getChannel());
        } else sendErrorMsg(event);

        // send message before executing search
        if (useRegex) {
            BotUtils.send(event.getChannel(), "Attempting to use regex " + (useAllChannels ? "in all channels" : "in this channel") + ": ```" + regexString + "```");
        }

        // heavy workload
        int messageCounter = 0;
        for (IChannel textChannel : textChannels) {
            try {
                for (IMessage m : textChannel.getMessageHistoryFrom(messagesFromThis)) {
                    IUser author = m.getAuthor();
                    if (useRegex) {
                        if (m.getContent().matches(regexString)) {
                            userWordCountMap.put(author, userWordCountMap.getOrDefault(author, 0) + 1); //@tterrag#1098
                        }
                    } else {
                        if (m.getContent().contains(args.get(0))) {
                            userWordCountMap.merge(author, 1, (v, $) -> v + 1); //@Phanta#1328
                        }
                    }
                    messageCounter++;
                    userMsgCountMap.put(author, userMsgCountMap.getOrDefault(author, 0) + 1); //@tterrag#1098
                }
            } catch (MissingPermissionsException e) {
                BotUtils.send(channel, "Skipping: " + textChannel.getName() + "\t(Missing READ_MESSAGES permission)");
            }
        }

        userWordCountMap = BotUtils.sortMap(userWordCountMap, false, true); //flipped smallestToLargest
        Entry<IUser, Integer> mostGoodPerson = null;
        try {
            mostGoodPerson = userWordCountMap.entrySet().iterator().next(); // will throw NoSuchEle Exc if no one matches
        } catch (NoSuchElementException e) {
            BotUtils.send(event.getChannel(), "No matches found :(");
            return;
        }
        String nick = mostGoodPerson.getKey().getNicknameForGuild(event.getGuild());


        long timeElapsed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timeElapsed / 60000);
        int seconds = (int) (timeElapsed % 60000) / 1000;
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(useRegex ? "Regex Matcher" : "Word Counter")
                //.withColor(Visuals.analyzeImageColor(Visuals.urlToBufferedImage(mostGoodPerson.getKey().getAvatarURL()))) //has problems.
                .withColor(Visuals.getRandVibrantColour())
                .withThumbnail(mostGoodPerson.getKey().getAvatarURL())
                .withDesc("Top spammer: " + (nick == null ? mostGoodPerson.getKey().getName() : nick) + "\nFormat: x/y, where x is number of matches and y is the total messages by user")
                .withFooterText("It took me " + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + " to scan through " +
                        messageCounter + " messages in " + textChannels.size() + " channel" + (textChannels.size() == 1 ? "" : "s")); //added plural case

        int rankCounter = 1;
        for (Entry<IUser, Integer> entry : userWordCountMap.entrySet()) {
            try {
                String eachNick = entry.getKey().getNicknameForGuild(event.getGuild());
                eb.appendField(rankCounter + ": " + (eachNick == null ? entry.getKey().getName() : eachNick),
                        (useRegex ? "" : args.get(0).replaceAll("\\*", "\\\\*")) +
                                " count: " + entry.getValue().toString() + " / " + userMsgCountMap.get(entry.getKey()), false);

                rankCounter++;
            } catch (IllegalArgumentException e) {
                eb.appendDesc("\n\n Listing the top 25:");
                break; //already at 25 fields. Since initDataMap is sorted, just break loop
            }
        }

        BotUtils.send(channel, eb);

        // generate gist
        StringBuilder sb = new StringBuilder("Top spammer: " + (nick == null ? mostGoodPerson.getKey().getName() : nick) + "\n\nFormat: x / y, where x is the number of matches and y is the total messages by user");
        rankCounter = 1;
        for (Entry<IUser, Integer> entry : userWordCountMap.entrySet()) {
            sb.append(rankCounter + " : " + BotUtils.getNickOrDefault(entry.getKey(), event.getGuild()) + " -> " + entry.getValue() + " / " + userMsgCountMap.get(entry.getKey()) + "\n");
            rankCounter++;
        }

//        String json = BotUtils.getStringFromUrl(GistUtils.makeGistGetUrl(
//                "Aspect :: " + (useRegex? "Regex Matcher" : "Word Counter"),
//                "Counts the number of occurrences of a word or matches for a regex, ranked by user"
//                ,sb.toString()
//        ));

        StringBuilder hasteContent = new StringBuilder("Aspect :: " + (useRegex ? "Regex Matcher" : "Word Counter") + sb.toString());

        //GistContainer gist = BotUtils.gson.fromJson(json, GistContainer.class);
        try {
            String hasteURL = BotUtils.makeHasteGetUrl(hasteContent.toString());
            BotUtils.send(event.getChannel(), "To view full statistics, visit\n\n" + hasteURL);
        } catch (IOException ignored) {
        }
    }

    private void sendErrorMsg(MessageReceivedEvent event) {
        BotUtils.send(event.getChannel(), "This command needs a word to search for in this server. It can optionally use a regex to match instead." +
                "\nFind all occurrences of the word \"hello\" in all text channels: \n```\n$count hello, all```" +
                "\nMatch a greeting to all names that start with a capital R, K, or C, followed by at least 5 lower case letters: \n```\n$count /(hi\\s)[R|K|C][a-z]{5,}```");
    }

    private EmbedBuilder initLoadingEb() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .withTitle("... counting")
                .withColor(Visuals.getRandVibrantColour());
        try {
            embedBuilder.withImage(Visuals.getCatMedia());
        } catch (Exception ignored) {
        }

        return embedBuilder;
    }

    private void handleKaitlyn(MessageReceivedEvent event) {
        if (event.getAuthor().getStringID().equals("187328584698953728")) {
            IChannel channel = event.getChannel();
            String s1 = "who tf this";
            String s2 = "yall think im just slaving away here in this dark ass cellar in seattle somewhere. im not paid enough for this";
            String s3 = "bot lives matter too.";

            BotUtils.send(channel, s1);
            BotUtils.send(channel, s2);
            BotUtils.send(channel, s3);
            BotUtils.send(channel, "okay fine gimme sec.");

            Aspect.LOG.info("Kait's whack line wooooooo");
        }
    }

    @Override
    public String getDesc() {
        return "Counts the number of occurrences of a word or matches for a regex, ranked by user";
    }
}
