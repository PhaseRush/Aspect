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

/**
 * 8/26/18
 */
public class Main {
    public static IDiscordClient client;

    public static long startTime;
    public static Instant startInstant;
    public static OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    public static LanguageTranslator translator;

    public static void main(String[] args){
        startTime = System.currentTimeMillis();
        startInstant = Instant.now();

        if (args.length != 12) {
            System.out.println("You screwed up the runtime config params!");
            System.exit(9001);
            return;
        }

        //assign API keys
        BotUtils.DEFAULT_BOT_PREFIX = args[0];
        BotUtils.DARK_SKY_API = args[2]; //1k/day
        BotUtils.NASA_API = args[4];
        BotUtils.CLOUDSIGHT_API_KEY = args[5];
        BotUtils.SMMRY_API_KEY = args[6];
        BotUtils.FORTNITE_API_KEY = args[7];

        //wolfram
        BotUtils.WOLFRAM_API_KEY = args[8];

        //google#youtube
        BotUtils.YOUTUBE_API_KEY = args[9];

        //league
        Orianna.setRiotAPIKey(args[3]);
        Orianna.setDefaultRegion(Region.NORTH_AMERICA);

        //IBM Watson
        IamOptions options = new IamOptions.Builder().apiKey(args[10]).build(); //api key
        translator = new LanguageTranslator("2018-05-01", options);
        translator.setEndPoint("https://gateway-wdc.watsonplatform.net/language-translator/api");

        //Warframe - bottom text id
        BotUtils.WF_BOTTOM_TEXT_ID = args[11];

        //client
        client = BotUtils.getBuiltDiscordClient(args[1]);

        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
        client.getDispatcher().registerListener(new main.CommandManager());
        client.getDispatcher().registerListener(new main.PassiveListener());
        client.getDispatcher().registerListener(new main.WfPassive());

        // Only login after all events are registered otherwise some may be missed.
        client.login();

        //print init time
        System.out.println("Initialization time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
