package main.passive;

import javafx.util.Pair;
import main.utility.RedditUtil;
import main.utility.metautil.BotUtils;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledSubreddits {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);


    @EventSubscriber
    public void redditCuties(ReadyEvent event) {
        DefaultPaginator<Submission> cutePaginator = RedditUtil.retreiveListing("aww", Paginator.RECOMMENDED_MAX_LIMIT, SubredditSort.HOT, TimePeriod.DAY);
        final int[] currIdx = {0}; // need this to be final for lambda

        // define the function that will actually do the work
        final Runnable cuteRunner = () -> {
            Pair<Submission, Integer> sub = RedditUtil.getNextImage(cutePaginator, currIdx[0]);
            Submission picPost = sub.getKey();
            currIdx[0] = sub.getValue()+1; // increment everytime

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle(picPost.getTitle())
                    .withImage(picPost.getUrl())
                    .withFooterText(picPost.getSubreddit() + ", " + picPost.getScore() + " points");
            //.withFooterIcon(RedditUtil.reddit.subreddit(picPost.getSubreddit()).) //wanted to get subreddit icon

            RedditUtil.cuteSubscribers.forEach(ch -> BotUtils.send(ch, eb));
        };

        scheduledFuture = scheduler.scheduleAtFixedRate(cuteRunner,3, 3, TimeUnit.SECONDS);
        System.out.println("Reddit scheduled");
    }


}
