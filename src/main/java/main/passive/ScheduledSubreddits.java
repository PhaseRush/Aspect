package main.passive;

import main.Aspect;
import main.utility.RedditUtil;
import main.utility.metautil.BotUtils;
import main.utility.structures.Pair;
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
import java.util.stream.Collectors;

import static main.utility.RedditUtil.*;

public class ScheduledSubreddits {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @EventSubscriber
    public void cuteSubs(ReadyEvent event) {
        schedule(RedditUtil.getDataMap().entrySet().stream()
                .filter(entry -> entry.getValue().isCute && !entry.getValue().isNsfw)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue
                )),
                3600
        );
    }

    @EventSubscriber
    public void nsfwSubs(ReadyEvent event) {
        schedule(RedditUtil.getDataMap().entrySet().stream()
                .filter(entry -> entry.getValue().isNsfw)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue
                )),
                3600
        );
    }

    private void schedule(Map<String, SubData> map, int periodSeconds) {
        for (Map.Entry<String, SubData> entry : map.entrySet()) {
            DefaultPaginator<Submission> defaultPaginator = retrieveListing(entry.getKey(), Paginator.RECOMMENDED_MAX_LIMIT, SubredditSort.HOT, TimePeriod.DAY);
            final int[] currIdx = {0}; // need this to be final for lambda

            // define the function that will actually do the work
            final Runnable runner = () -> {
                EmbedBuilder eb = embedInit(defaultPaginator, currIdx);

                Arrays.stream(entry.getValue().subscribedChannels)
                        .mapToObj(longId -> Aspect.client.getChannelByID(longId))
                        .forEach(channel -> BotUtils.send(channel, eb));

            };

            scheduledFuture = scheduler.scheduleAtFixedRate(runner, 3, periodSeconds, TimeUnit.SECONDS);
            System.out.println("Scheduled : r/" + entry.getKey() + " in " + map.entrySet().size() + " channels");
        }
    }

    private EmbedBuilder embedInit(DefaultPaginator<Submission> defaultPaginator, int[] currIdx) {
        Pair<Submission, Integer> sub = getNextImage(defaultPaginator, currIdx[0]);
        Submission picPost = sub.getKey();
        currIdx[0] = sub.getValue() + 1; // increment everytime

        return new EmbedBuilder()
                .withTitle(picPost.getTitle())
                .withImage(picPost.getUrl())
                .withFooterText("r/" + picPost.getSubreddit() + ", " + picPost.getScore() + " points");
    }
}
