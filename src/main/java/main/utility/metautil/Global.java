package main.utility.metautil;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

/**
 * class that has all public static variables
 */
public class Global {
    // System statistics
    private OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    // Read this file line by line
    private final List<String> arguments = BotUtils.readFromFile(System.getProperty("user.dir") + "/data/launch.txt");

    // global variables from arguments
    private String BOT_PREFIX = arguments.get(0);
    private String DISCORD_TOKEN = arguments.get(1);
    private String DARKSKY_KEY = arguments.get(2);
    private String RIOT_GAMES_KEY = arguments.get(3);
    private String NASA_KEY = arguments.get(4);
    private String CLOUDSIGHT_KEY = arguments.get(5);
    private String SMMRY_KEY = arguments.get(6);
    private String FORTNITE_STATS_KEY = arguments.get(7);
    private String WOLFRAM_KEY = arguments.get(8);
    private String YOUTUBE_V3_KEY = arguments.get(9);
    private String IBM_WATSON_KEY = arguments.get(10);
    private String BOTTOM_TEXT_CH_STR_ID = arguments.get(11);
    private String DEEP_AI_KEY = arguments.get(12);
    private String REDDIT_IGN = arguments.get(13);
    private String REDDIT_PW = arguments.get(14);
    private String REDDIT_CLIENT_ID = arguments.get(15);
    private String REDDIT_CLIENT_SECRET = arguments.get(16);

    // other global vars derived from args
    // IBM watson
    IamOptions options = new IamOptions.Builder().apiKey(IBM_WATSON_KEY).build();
    private LanguageTranslator translator = new LanguageTranslator("2018-05-01", options);

    private IDiscordClient client = BotUtils.getBuiltDiscordClient(DISCORD_TOKEN);
    private IChannel BOTTOM_TEXT_CH = client.getChannelByID(Long.valueOf(BOTTOM_TEXT_CH_STR_ID));

    // Other util Objs

    public Global() {
        // League
        Orianna.setRiotAPIKey(RIOT_GAMES_KEY);
        Orianna.setDefaultRegion(Region.NORTH_AMERICA);
    }


    // getters
    public List<String> getArguments() {
        return arguments;
    }

    public String getBOT_PREFIX() {
        return BOT_PREFIX;
    }

    public String getDISCORD_TOKEN() {
        return DISCORD_TOKEN;
    }

    public String getDARKSKY_KEY() {
        return DARKSKY_KEY;
    }

    public String getRIOT_GAMES_KEY() {
        return RIOT_GAMES_KEY;
    }

    public String getNASA_KEY() {
        return NASA_KEY;
    }

    public String getCLOUDSIGHT_KEY() {
        return CLOUDSIGHT_KEY;
    }

    public String getSMMRY_KEY() {
        return SMMRY_KEY;
    }

    public String getFORTNITE_STATS_KEY() {
        return FORTNITE_STATS_KEY;
    }

    public String getWOLFRAM_KEY() {
        return WOLFRAM_KEY;
    }

    public String getYOUTUBE_V3_KEY() {
        return YOUTUBE_V3_KEY;
    }

    public String getIBM_WATSON_KEY() {
        return IBM_WATSON_KEY;
    }

    public String getBOTTOM_TEXT_CH_STR_ID() {
        return BOTTOM_TEXT_CH_STR_ID;
    }

    public String getDEEP_AI_KEY() {
        return DEEP_AI_KEY;
    }

    public String getREDDIT_IGN() {
        return REDDIT_IGN;
    }

    public String getREDDIT_PW() {
        return REDDIT_PW;
    }

    public String getREDDIT_CLIENT_ID() {
        return REDDIT_CLIENT_ID;
    }

    public String getREDDIT_CLIENT_SECRET() {
        return REDDIT_CLIENT_SECRET;
    }

    public LanguageTranslator getTranslator() {
        return translator;
    }

    public IDiscordClient getClient() {
        return client;
    }

    public IChannel getBOTTOM_TEXT_CH() {
        return BOTTOM_TEXT_CH;
    }

    public OperatingSystemMXBean getOsBean() {
        return osBean;
    }
}
