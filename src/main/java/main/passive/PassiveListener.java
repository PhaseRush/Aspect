package main.passive;

import com.google.api.services.youtube.model.VideoStatistics;
import com.google.common.math.BigIntegerMath;
import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import main.Aspect;
import main.commands.dontopendeadinside.games.CoinFlip;
import main.utility.GoogleUtil;
import main.utility.RedditUtil;
import main.utility.TableUtil;
import main.utility.Visuals;
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

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PassiveListener {
    // private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Pattern unexpFactRegex = Pattern.compile("\\b[0-9]+!");

    // matches youtube urls, videoId in group 2
    private static Pattern youtubeIdRegex = Pattern.compile("^.*(youtu\\.be/|v/|u/\\w/|embed/|watch\\?v=|&v=)([^#&?]*).*");

    private static Map<Long, Long> lastThanksgivingMap = new LinkedHashMap<>();
    private static List<Long> reactionsBlacklist = Arrays.asList(402728027223490572L, 208023865127862272L); //for Ohra's private server

    private static Set<Long> videoAnalyticsOptIn = Collections.emptySet();


    @EventSubscriber
    public void reactToEmojiMessage(MessageReceivedEvent event) {
        try {
            if (reactionsBlacklist.contains(event.getGuild().getLongID())) return;
            if (event.getChannel().isPrivate()) return;
            if (event.getAuthor().isBot()) return;
            getEmojiFromMsg(event, event.getMessage().getFormattedContent())
                    .forEach(emoji -> RequestBuffer.request(() -> event.getMessage().addReaction(emoji)));
        } catch (NullPointerException | RateLimitException ignored) {
        } // dont care
    }

    private List<IEmoji> getEmojiFromMsg(ChannelEvent event, String msgContent) {
        return event.getGuild().getEmojis().stream()
                .filter(emoji -> msgContent.matches("\\b:?" + emoji.getName() + ":?\\b"))
                .collect(Collectors.toList());
    }


    @EventSubscriber
    public void subredditLinker(MessageReceivedEvent event) {
        for (String s : event.getMessage().getFormattedContent().split(" ")) {
            if (s.matches("(.*\\s)*/?(r/).*")) {
                String afterR = s.substring(s.indexOf("r/"));
                String subR;
                if (afterR.contains(" "))
                    subR = afterR.substring(0, afterR.indexOf(" "));
                else subR = afterR.replaceAll("[.,#!$%^&*;:{}=\\-_`~()]", ""); //dont replace '/'

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
        if (event.getAuthor().isBot()) return;
        Matcher matcher = unexpFactRegex.matcher(event.getMessage().getFormattedContent());

        String group;
        if (matcher.find()) {
            group = matcher.group();
        } else return;

        int num = Integer.valueOf(group.substring(0, group.length() - 1));
        if (num > 797) return;
        BigInteger fact = BigIntegerMath.factorial(num);

        BotUtils.send(event.getChannel(), "Did you know that `" + num + "! = " + fact + "`");
    }


    @EventSubscriber
    public void alexaPlayTheD(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        String alexa = "alexa play despacito";

        //BotUtils.send(event.getChannel(), String.valueOf(levenshtein.distance(msg,alexa)));
        if (BotUtils.stringSimilarity(msg, alexa) < 3 || msg.contains(alexa)) {
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

        if (similarity < 3)
            BotUtils.send(event.getChannel(), new EmbedBuilder().withImage("https://i.imgur.com/sohWhy9.png"));
    }

    @EventSubscriber
    public void coinFlip(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent().toLowerCase().trim();
        if (msg.length() < 6 || msg.length() > 20) return;

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
        } catch (Exception e) {
            Aspect.LOG.warn(e.getLocalizedMessage());
        }
    }

    /**
     * Vivi stop sleeping!
     * enjoy this lullaby
     */
    @EventSubscriber
    public void viviStopSleeping(UserVoiceChannelEvent event) {
        try {
            if (event.getUser().getStringID().equals("167418444067766273"))
                event.getGuild().getAFKChannel().getConnectedUsers().stream()
                        .filter(u -> u.getStringID().equals("167418444067766273"))
                        .findFirst()
                        .ifPresent(u -> event.getClient().getConnectedVoiceChannels().stream()
                                .filter(ch -> event.getGuild().getVoiceChannels().contains(ch))
                                .findFirst()
                                .ifPresent(u::moveToVoiceChannel)
                        );
        } catch (NullPointerException e) { // ignored
        }
    }

    /**
     * Sends an embed with when link with YouTube video detected
     * -View count
     * -Likes
     * -Dislikes
     * -Total Ratings
     * -Comments
     *
     * @param event message event which may or may not contain a youtube link
     */
    @EventSubscriber
    public void youtubeStats(MessageReceivedEvent event) {
        if (event.getChannel().isPrivate()) return;
        // check for valid guild
        if (!videoAnalyticsOptIn.contains(event.getGuild().getLongID())) return; // opt out

        Matcher matcher = youtubeIdRegex.matcher(event.getMessage().getFormattedContent());
        String videoId = matcher.matches() ? matcher.group(2) : null;
        if (videoId == null) return; // weird edge case so return

        VideoStatistics stats = GoogleUtil.getYTRatings(videoId);

        BigDecimal likes = new BigDecimal(stats.getLikeCount());
        BigDecimal dislikes = new BigDecimal(stats.getDislikeCount());
        BigDecimal totalRatings = likes.add(dislikes);

        GridTable table = GridTable.of(5, 2)
                .put(0, 0, Cell.of("Views"))
                .put(0, 1, Cell.of(stats.getViewCount().toString()))
                .put(1, 0, Cell.of("Likes"))
                .put(1, 1, Cell.of(stats.getLikeCount() + " = " + likes.multiply(new BigDecimal(100)).divide(totalRatings, RoundingMode.HALF_DOWN) + "%"))
                .put(2, 0, Cell.of("Dislikes"))
                .put(2, 1, Cell.of(stats.getDislikeCount() + " = " + dislikes.multiply(new BigDecimal(100)).divide(totalRatings, RoundingMode.HALF_DOWN) + "%"))
                .put(3, 0, Cell.of("Total Ratings"))
                .put(3, 1, Cell.of(totalRatings.toString()))
                .put(4, 0, Cell.of("Comments"))
                .put(4, 1, Cell.of(stats.getCommentCount().toString()));

        table = Border.DOUBLE_LINE.apply(table);


        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("YouTube Analytics")
                .withDesc(TableUtil.renderInCodeBlock(table).toString());

        try {
            eb.withColor(Visuals.analyzeImageColor(Visuals.urlToBufferedImage("https://i1.ytimg.com/vi/" + videoId + "/hqdefault.jpg"))); // analyze thumbnail
        } catch (Exception e) {// happens when cant retrieve image
            eb.withColor(Color.BLACK);
        }

        BotUtils.send(event.getChannel(), eb);
    }


//    @EventSubscriber
//    public void testing(MessageReceivedEvent event) {
//        if (event.getChannel().getStringID().equals("481734359343300609")) {
//            Aspect.LOG.info("hi");
//        }
//    }
}
