package main;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import main.utility.BotUtils;
import sx.blah.discord.api.IDiscordClient;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Aspect -- Discord bot built with love
 *
 * @URL github.com/PhaseRush/Aspect
 * 2018/11/24
 */
public class Main {
    public static IDiscordClient client;

    public static long startTime;
    public static Instant startInstant;

    public static OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    public static LanguageTranslator translator;

    // Keep a public list of all listeners
    public static List<Object> dispatchListeners = new ArrayList<>();

    public static void main(String[] args){

        // ------------------------------------------------------------ //

        startTime = System.currentTimeMillis();
        startInstant = Instant.now();

        if (args.length != 15) {
            System.out.println("You screwed up the runtime config params!");
            System.exit(9001);
            return;
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

        // ------------------------------------------------------------ //

        // Create all dispatch listeners
        dispatchListeners = BotUtils.createListeners();

        // Self Client Initialization
        client = BotUtils.getBuiltDiscordClient(args[1]);

        // Register all listeners via the EventSubscriber annotation which allows for organisation and delegation of events
        client.getDispatcher().registerListeners(dispatchListeners);

        // Self Client login - finalize setup
        // Only login after all events are registered otherwise some may be missed.
        client.login();

        // ------------------------------------------------------------ //

        System.out.println("Initialization time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
