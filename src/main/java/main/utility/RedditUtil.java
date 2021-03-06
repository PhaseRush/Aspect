package main.utility;

import com.google.gson.reflect.TypeToken;
import main.utility.metautil.BotUtils;
import main.utility.structures.Pair;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class RedditUtil {

    public static RedditClient reddit;

    // Prishe: "uwu"
    private static Type InputJsonMapType = new TypeToken<Map<String, SubData>>() {}.getType();
    private static Map<String, SubData> initDataMap = BotUtils.gson.fromJson(
            BotUtils.readFromFileToString(System.getProperty("user.dir") + "/data/subreddit_init_data.json"),
            InputJsonMapType);


    static {
        UserAgent userAgent = new UserAgent("Aspect", "com.github.PhaseRush.Aspect", "v2.0", BotUtils.REDDIT_IGN);
        Credentials creds = Credentials.script(BotUtils.REDDIT_IGN, BotUtils.REDDIT_PW, BotUtils.REDDIT_CLIENT_ID, BotUtils.REDDIT_SECRET);
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        reddit = OAuthHelper.automatic(adapter, creds);
    }

    public static DefaultPaginator<Submission> retrieveListing(String subredditName, int limit, SubredditSort sort, TimePeriod time) {
        return RedditUtil.reddit.subreddit(subredditName).posts()
                .limit(limit)
                .sorting(sort)
                .timePeriod(time)
                .build();
    }

    public static Pair<Submission, Integer> getNextImage(DefaultPaginator paginator, int currIdx) {
        Listing<Submission> submissionListing = paginator.getCurrent();
        if (submissionListing == null) submissionListing = paginator.next(); // needs this because init as null

        Pair<Submission, Integer> responsePair = checkCurrList(submissionListing, currIdx);

        while (responsePair == null) {
            responsePair = checkCurrList(paginator.next(), 0); // reset currIdx to 0 if calling next for new Listing<Submission>
        }
        // if reach this point, then pair is no longer null, and we create new pair which adds current idx
        return responsePair; // new Pair<>(responsePair.getKey(), responsePair.getValue() + currIdx);
    }

    // return first image found along with index, else return null
    private static Pair<Submission, Integer> checkCurrList(List<Submission> submissions, int currIdx) {
        for (int i = currIdx; i < submissions.size(); i++) {
            Submission post = submissions.get(i);
            if (post.getDomain().contains("imgur.com") || post.getDomain().contains("i.redd.it")) {
                return new Pair<>(post, i);
            }
        }
        return null; // return this if nothing in this List has images
    }

    public static class SubData {
        public long[] subscribedChannels;
        public int defaultFrequency;
        public boolean isNsfw;
        public boolean isCute;

        // String[] note; // json debugging use
    }

    public static Map<String, SubData> getDataMap() {
        return initDataMap;
    }
}
