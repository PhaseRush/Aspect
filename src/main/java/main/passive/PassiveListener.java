package main.passive;

import com.google.common.math.BigIntegerMath;
import main.commands.dontopendeadinside.games.CoinFlip;
import main.utility.RedditUtil;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import net.dean.jraw.ApiException;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PassiveListener {
    // private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Pattern unexpFactRegex = Pattern.compile("\\b[0-9]+!");

    private static Map<Long, Long> lastThanksgivingMap = new LinkedHashMap<>();
    private static List<Long> reactionsBlacklist = Arrays.asList(402728027223490572L, 208023865127862272L); //for Ohra's private server


    @EventSubscriber
    public void reactToEmojiMessage(MessageReceivedEvent event) {
        try {
            if (reactionsBlacklist.contains(event.getGuild().getLongID())) return;
            if (event.getChannel().isPrivate()) return;
            if (event.getAuthor().isBot()) return;
            getEmojiFromMsg(event, event.getMessage().getFormattedContent())
                    .forEach(emoji -> RequestBuffer.request(() -> event.getMessage().addReaction(emoji)));
        } catch (NullPointerException | RateLimitException ignored) {} // dont care
    }

    private List<IEmoji> getEmojiFromMsg(ChannelEvent event, String msgContent) {
        return event.getGuild().getEmojis().stream()
                .filter(emoji -> msgContent.matches("\\b" + emoji.getName() + "\\b"))
                .collect(Collectors.toList());
    }


    @EventSubscriber
    public void subredditLinker(MessageReceivedEvent event) {
        String[] msgSplit = event.getMessage().getFormattedContent().split(" ");
        for (String s : msgSplit) {
            if (s.matches("(.*\\s)*/?(r/).*")) {
                String afterR = s.substring(s.indexOf("r/"));
                String subR;
                if (afterR.contains(" "))
                    subR = afterR.substring(0, afterR.indexOf(" "));
                else subR = afterR.replaceAll("[.,#!$%^&*;:{}=\\-_`~()]",""); //dont replace '/'

                // check name length limit (20)
                if (subR.length() > 20) return;
                try { // expensive check so do last
                    RedditUtil.reddit.subreddit(subR.substring(2)).about();
                } catch (NullPointerException | StringIndexOutOfBoundsException | ApiException e) {
                    return; // doesnt exist
                } // string error


                BotUtils.send(event.getChannel(), "https://www.reddit.com/" + subR);
            }
        }
    }

    @EventSubscriber
    public void weirdFlexButOk(MessageReceivedEvent event) {
        final String reference = "weird flex but ok";
        double levenScore = BotUtils.stringSimilarity(reference, event.getMessage().getFormattedContent().toLowerCase().trim());
        if (levenScore < 4) {
            BotUtils.send(event.getChannel(), BotUtils.generateWeirdFlex());
        }
    }


    @EventSubscriber
    public void unexpectedFactorial(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        Matcher matcher = unexpFactRegex.matcher(msg);

        String group;
        if (matcher.find()) {
            group = matcher.group();
        } else return;

        int num = Integer.valueOf(group.substring(0, group.length()-1));
        if (num > 797) return;
        BigInteger fact = BigIntegerMath.factorial(num);

        BotUtils.send(event.getChannel(), "Did you know that `" + num + "! = " + fact + "`");
    }


    @EventSubscriber
    public void alexaPlayTheD(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        String alexa = "alexa play despacito";

        //BotUtils.send(event.getChannel(), String.valueOf(levenshtein.distance(msg,alexa)));
        if (BotUtils.stringSimilarity(msg, alexa) <3 || msg.contains(alexa)) {
            if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel()
                    != event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()) {

                BotUtils.joinVC(event);
            }
            MasterManager.loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=kJQP7kiw5Fk", event, true, "I'm not Alexa, but I can play despacito.");
        }
    }

    //@EventSubscriber
//    public void thanksgiving(MessageReceivedEvent event) {
//        long lastTime = lastThanksgivingMap.getOrDefault(event.getGuild().getLongID(), 0L);
//
//        if (System.currentTimeMillis() - lastTime < 1000*60) return;
//
//        String msg = event.getMessage().getFormattedContent();
//        double similarity = BotUtils.stringSimilarity(msg, "Happy Thanksgiving");
//
//        //BotUtils.send(event.getChannel(), String.valueOf(similarity));
//        if (similarity < 3) {
//            BotUtils.send(event.getChannel(), "Happy Thanksgiving!");
//            lastThanksgivingMap.put(event.getGuild().getLongID(), System.currentTimeMillis());
//        }
//    }

    @EventSubscriber
    public void pikachuFace(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent().toLowerCase().trim();

        if (msg.length() < 15) return; // too short to be pikachu

        if (!(msg.startsWith("s") || msg.startsWith("i"))) return;

        double similarity = Math.min(
                BotUtils.stringSimilarity(msg, "insert pikachu face"),
                BotUtils.stringSimilarity(msg, "surprised pikachu face"));

        if (similarity < 3) BotUtils.send(event.getChannel(), new EmbedBuilder().withImage("https://i.imgur.com/sohWhy9.png"));
    }

    @EventSubscriber
    public void coinFlip(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent().toLowerCase().trim();
        if (msg.length() < 6) return;

        if (BotUtils.stringSimilarity(msg, "flip a coin") < 3) CoinFlip.flip(event);
    }

    @EventSubscriber
    public void kaitlynsHangOut(MessageReceivedEvent event) {
        if (event.getChannel().isPrivate()) return;
        //please, no one ask. please please please please please
        if (event.getGuild().getStringID().equals("197158565004312576")) {
            String message = event.getMessage().getFormattedContent().toLowerCase();
            if (message.contains("penis"))
                BotUtils.send(event.getChannel(), "penis.");
            //use embed to hide url in message
            if (message.contains("turtle"))
                BotUtils.send(event.getChannel(), new EmbedBuilder().withImage("https://assets3.thrillist.com/v1/image/2551479/size/tmg-article_tall.jpg"));
        }
    }

    @EventSubscriber
    public void userJoin(UserJoinEvent event) {
        List<IChannel> channels = event.getGuild().getChannels();
        IChannel target = null;

        for (IChannel ch : channels) {
            String name = ch.getName().replaceAll("^[ -~]", "").toLowerCase();
            if (name.contains("bot") || name.contains("spam")) {
                target = ch;
                break;
            }
        }

        BotUtils.send(target == null ? event.getGuild().getDefaultChannel() : target,
                "Welcome " + event.getUser().getName() + " to " + event.getGuild().getName() + "!");
    }

    @EventSubscriber
    public void addedReaction(ReactionAddEvent event) {
        if (reactionsBlacklist.contains(event.getGuild().getLongID())) return;
        if (event.getAuthor().isBot()) return;
        RequestBuffer.request(() -> event.getMessage().addReaction(event.getReaction()));
    }

    @EventSubscriber
    public void removedReaction(ReactionRemoveEvent event) {
        if (reactionsBlacklist.contains(event.getGuild().getLongID())) return;
        if (event.getAuthor().isBot()) return;
        try {
            getEmojiFromMsg(event, event.getMessage().getFormattedContent())
                    .forEach(emoji -> RequestBuffer.request(() -> event.getMessage().removeReaction(event.getClient().getOurUser(), event.getReaction())));
        } catch (Exception ignored) {
        }
    }

    /**
     * Vivi stop sleeping!
     * enjoy this lullaby
     */
    @EventSubscriber
    public void viviStopSleeping (UserVoiceChannelEvent event) {
        if (event.getUser().getStringID().equals("167418444067766273"))
            event.getGuild().getAFKChannel().getConnectedUsers().stream()
                    .filter(u -> u.getStringID().equals("167418444067766273"))
                    .findFirst()
                    .ifPresent(u -> event.getClient().getConnectedVoiceChannels().stream()
                            .filter(ch -> event.getGuild().getVoiceChannels().contains(ch))
                            .findFirst()
                            .ifPresent(u::moveToVoiceChannel)
                    );
    }


//    @EventSubscriber
//    public void testing(MessageReceivedEvent event) {
//        if (event.getChannel().getStringID().equals("481734359343300609")) {
//            System.out.println("hi");
//        }
//    }
}
