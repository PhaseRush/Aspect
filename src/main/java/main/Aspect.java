package main;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import main.utility.metautil.BotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Aspect -- Discord bot built with love
 *
 * @URL github.com/PhaseRush/Aspect
 * 2018/11/28
 */
public class Aspect {
    // Aspect client
    public static IDiscordClient client;

    // Main thread
    private final Thread MAIN = Thread.currentThread();

    // Timekeeping
    public static Instant startInstant;

    // Unique INSTANCE_ID ID
    public static final UUID INSTANCE_ID = UUID.randomUUID();

    // System statistics
    public static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getOperatingSystemMXBean();

    // IBM Translator
    public static LanguageTranslator translator;

    // SLF4J logger
    public static final Logger LOG = LoggerFactory.getLogger("AspectLog");

    public static void main(String[] args) throws IOException, SpotifyWebApiException {
        startInstant = Instant.now();
        Aspect.LOG.info("Aspect Launching");
        Locale.setDefault(Locale.US);
        Aspect.LOG.info("Millis until 19:00 Van " + BotUtils.millisToNextHHMMSSMMMM(19, 0, 0, 0, "America/Los_Angeles"));
        Aspect.LOG.info("Millis until 18:00 CDT " + BotUtils.millisToNextHHMMSSMMMM(18, 40, 0, 0, "CST6CDT"));


        // ------------------------------------------------------------ //

        if (args.length != 23) { // 25 with spotify
            LOG.error("You screwed up the runtime config params!\targs: {}", args.length);
            System.exit(9001);
            return; // :)
        }

        // ------------------------------------------------------------ //

        // Default bot prefix (should be "$")
        BotUtils.DEFAULT_BOT_PREFIX = args[0];

        // Dark Sky -- limit: 1k/day
        BotUtils.DARK_SKY_API = args[2];

        // NASA
        BotUtils.NASA_API = args[4];

        // Cloudsight image recognition
        BotUtils.CLOUDSIGHT_API_KEY = args[5];

        // SMMRY - Summarization service
        BotUtils.SMMRY_API_KEY = args[6];

        // Fortnite statistics API
        BotUtils.FORTNITE_API_KEY = args[7];

        // Wolfram and Mathematica
        BotUtils.WOLFRAM_API_KEY = args[8];

        // Google and Youtube
        BotUtils.YOUTUBE_API_KEY = args[9];

        // Ticketmaster
        BotUtils.TICKET_MASTER_API_KEY = args[15];

        // Deep AI
        BotUtils.DEEP_AI_API_KEY = args[16];

        // Reddit integration
        BotUtils.REDDIT_IGN = args[17];
        BotUtils.REDDIT_PW = args[18];
        BotUtils.REDDIT_CLIENT_ID = args[19];
        BotUtils.REDDIT_SECRET = args[20];

        // Apex Legends
        BotUtils.APEX_LEGENDS_API_KEY = args[21];

        // League of Legends
        Orianna.setRiotAPIKey(args[3]);
        Orianna.setDefaultRegion(Region.NORTH_AMERICA);

        // IBM Watson
        IamOptions options = new IamOptions.Builder().apiKey(args[10]).build(); //api key
        translator = new LanguageTranslator("2018-05-01", options);
        translator.setEndPoint("https://gateway-wdc.watsonplatform.net/language-translator/api");

        // Warframe - Custom alert channel ID (The Lavender Society # bottom-text)
        BotUtils.WF_BOTTOM_TEXT_ID = args[11];

        // Private server id/passwords
        BotUtils.PRIVATE_CHANNEL_INFO_URL = args[12];

        // Github for Git and Gist
        BotUtils.DEV_GITHUB_NAME = args[13];
        BotUtils.DEV_GITHUB_PASSWORD = args[14];

        // Fortnite Scout API
        // Fortnite V2 API
        BotUtils.FORTNITE_V2 = args[22];

        // Spotify API
        try {
            BotUtils.SPOTIFY_CLIENT_ID = args[22];
            BotUtils.SPOTIFY_CLIENT_SECRET = args[23];
        } catch (Exception ignored) {
        }


        LOG.info("All API keys set");
        // ------------------------------------------------------------ //

        // Self Client Initialization
        client = BotUtils.getBuiltDiscordClient(args[1]);

        // Create all dispatch listeners
        List<Object> dispatchListeners = BotUtils.createListeners();

        // Register all listeners via the @EventSubscriber annotation which allows for organization and delegation of events
        client.getDispatcher().registerListeners(dispatchListeners.toArray());

        // Self Client login - finalize setup
        // Only login after all events are registered - otherwise some may be missed.
        client.login();
    }
}