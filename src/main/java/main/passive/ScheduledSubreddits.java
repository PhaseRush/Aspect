package main.passive;

import javafx.util.Pair;
import main.Main;
import main.utility.metautil.BotUtils;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static main.utility.RedditUtil.*;

public class ScheduledSubreddits {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);


//    @EventSubscriber
//    public void redditCuties(ReadyEvent event) {
//        DefaultPaginator<Submission> cutePaginator = retreiveListing("aww", Paginator.RECOMMENDED_MAX_LIMIT, SubredditSort.HOT, TimePeriod.DAY);
//        final int[] currIdx = {0}; // need this to be final for lambda
//
//        // define the function that will actually do the work
//        final Runnable cuteRunner = () -> cuteSubscribers.forEach(ch ->BotUtils.send(ch,embedInit(cutePaginator, currIdx)));
//
//        scheduledFuture = scheduler.scheduleAtFixedRate(cuteRunner,3, 3, TimeUnit.SECONDS);
//        System.out.println("Reddit scheduled");
//    }

    @EventSubscriber
    public void oneMinute(ReadyEvent event) {
        Map<String, SubscriptionFrequency[]> map = getMap();
        for (Map.Entry<String, SubscriptionFrequency[]> entry : map.entrySet()) {
            for (SubscriptionFrequency freq : entry.getValue()) {
                if (freq.frequency_seconds != 5) {
                    map.remove(entry.getKey());
                }
            }
        }

        for (Map.Entry<String, SubscriptionFrequency[]> entry : map.entrySet()) {
            DefaultPaginator<Submission> defaultPaginator = retreiveListing(entry.getKey(), Paginator.RECOMMENDED_MAX_LIMIT, SubredditSort.HOT, TimePeriod.DAY);
            final int[] currIdx = {0}; // need this to be final for lambda

            // define the function that will actually do the work
            final Runnable runner = () -> {
                EmbedBuilder eb = embedInit(defaultPaginator, currIdx);

                Arrays.stream(entry.getValue())
                        .map(subFreq -> Main.client.getChannelByID(subFreq.channel_id))
                        .forEach(ch -> BotUtils.send(ch, eb));

            };

            scheduledFuture = scheduler.scheduleAtFixedRate(runner, 1, 300, TimeUnit.SECONDS);
            System.out.println("reddit sch");
        }
    }

    public EmbedBuilder embedInit(DefaultPaginator<Submission> defaultPaginator, int[] currIdx) {
        Pair<Submission, Integer> sub = getNextImage(defaultPaginator, currIdx[0]);
        Submission picPost = sub.getKey();
        currIdx[0] = sub.getValue() + 1; // increment everytime

        return new EmbedBuilder()
                .withTitle(picPost.getTitle())
                .withImage(picPost.getUrl())
                .withFooterText("r/" + picPost.getSubreddit() + ", " + picPost.getScore() + " points");
    }
}
