package main.utility;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import main.utility.warframe.dynamic.WarframeSolNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BotUtils {

    //meta util
    private static Random tlr = ThreadLocalRandom.current();
    private static Gson gson = new Gson();
    private static OkHttpClient client = new OkHttpClient();

    // Constants for use throughout the bot
    public final static String GITHUB_URL = "https://github.com/PhaseRush/Aspect";

    //guildID, commandPrefix
    private static final String mapFilePath = "";//get rid of this
    private static Map<Long, String> PREFIX_MAP = new HashMap();

    //API keys
    public static String DEFAULT_BOT_PREFIX = "!";
    public static String NASA_API;
    public static String DARK_SKY_API;
    public static String CLOUDSIGHT_API_KEY;
    public static String SMMRY_API_KEY;
    public static String FORTNITE_API_KEY; //service "temp" deprecated?
    public static String WOLFRAM_API_KEY;
    public static String YOUTUBE_API_KEY;

    //todo this thing please help
    public String[] timeEvents =
            {"It's midnight!", //0
                    "Time I should be headed to bed", //1
                    "I'm tired stap", //2
                    "",//3
                    "",//4
                    "",//5
                    "",//6
                    "Time to grab covfefe",//7
                    "",//8
                    "",//9
                    "",//10
                    "",//11
                    "",//12
                    "",//13
                    "",//14
                    "",//15
                    "",//16
                    "",//17
                    "",//18
                    "",//19
                    "",//20
                    "",//21
                    "",//22
                    "",};//23

    //warframe
    public static Map<String, WarframeSolNode> solNodeMap = new LinkedHashMap<>();

    static {
        String solNodeUrl = "https://raw.githubusercontent.com/WFCD/warframe-worldstate-data/master/data/solNodes.json";
        String solNodeJson = getStringFromUrl(solNodeUrl);

        Type type = new TypeToken<LinkedHashMap<String, WarframeSolNode>>() {
        }.getType();
        solNodeMap = gson.fromJson(solNodeJson, type);
    }


    public static String getPrefix(IGuild iGuild) {
        try {
            return PREFIX_MAP.get(Long.valueOf(iGuild.getStringID()));
        } catch (NullPointerException e) {
            return "This key doesn't exist";
        }
    }

    public static void setPrefix(IGuild iGuild, String prefix) {
        PREFIX_MAP.put(Long.valueOf(iGuild.getStringID()), prefix);
    }

    public static void savePrefixMap() {
        try {
            gson.toJson(PREFIX_MAP, new FileWriter(mapFilePath));
        } catch (IOException e) {
            System.out.println("savePrefixMap - Read prefixMap from file error: IOException");
        }
    }

    public static List<String> insults = populateInsults();

    /**
     * (slightly nsfw) Insults
     *
     * @return
     */
    private static List<String> populateInsults() {
        List<String> list = new ArrayList<>();

        list.add("You better pick a God and start praying.");
        list.add("Your family tree is a 2 x 4");
        list.add("If you were any more inbred you'd be a sandwich");
        list.add("You're so ugly, you couldn't even arouse suspicion");
        list.add("You continue to meet my expectations");
        list.add("You're as useful as a Janna main playing [literally any champion that isn't Janna]");
        list.add("Your mother is so old she has a separate entrance for black men.");
        list.add("May you search for your children with a Geiger Counter.");
        list.add("Your mom should've swallowed");
        list.add("The only thing your mother wants for Christmas is a folded up flag");
        list.add("You're not the dumbest person on planet Earth, but you better hope they don't die.");
        list.add("If you were any stupider we'd have to water you");
        list.add("I was pro-life before I met you");

        return list;
    }

    // Handles the creation and getting of a IDiscordClient object for a token
    public static IDiscordClient getBuiltDiscordClient(String token) {

        // The ClientBuilder object is where you will attach your params for configuring the instance of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withToken(token)
                .withRecommendedShardCount()
                .build();

    }

    // Helper functions to make certain aspects of the bot easier to use.
    public static void sendMessage(IChannel channel, String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (DiscordException e) {
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            } catch (MissingPermissionsException e) {
                System.out.println("Missing Permissions: " + channel.getName() + " Msg: " + message);
            }
        });
    }

    public static void sendMessage(IChannel channel, EmbedBuilder embed) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(embed.build());
            } catch (DiscordException e) {
                System.err.println("Embed could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }

    //join voice channel
    public static void handleJoinVoice(MessageReceivedEvent event) {
        IVoiceChannel userVoiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (userVoiceChannel == null)
            return;

        userVoiceChannel.join();
    }

    //leave voice channel
    public static void handleLeaveVoice(MessageReceivedEvent event) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (botVoiceChannel == null)
            return;

        botVoiceChannel.leave();
    }

    public static String getJson(String jsonURL) {
        try {
            String userJson;
            URL url = new URL(jsonURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); //parameter?
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET"); //might make parameter later

            // read the response
            InputStream in = new BufferedInputStream(connection.getInputStream());
            userJson = IOUtils.toString(in, "UTF-8");
            in.close();
            connection.disconnect();

            return userJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getJson(String jsonURL, boolean isFortnite) {
        try {
            String userJson;
            URL url = new URL(jsonURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); //parameter?
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET"); //might make parameter later
            connection.setRequestProperty("X-Key", FORTNITE_API_KEY);
            connection.setRequestProperty("User-Agent", "java request");

            // read the response
            InputStream in = new BufferedInputStream(connection.getInputStream());
            userJson = IOUtils.toString(in, "UTF-8");
            in.close();
            connection.disconnect();

            return userJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRandomFromStringArray(String[] stringArray) {
        return stringArray[new Random().nextInt(stringArray.length)];
    }

    public static String printStringList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(", ");
        }
        return sb.toString();
    }

    public static List<ReactionEmoji> initializeRegionals() {
        List<ReactionEmoji> emojis = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            char[] charArray = {0xD83C, (char) (0xDDE6 + i)};
            emojis.add(ReactionEmoji.of(String.valueOf(charArray)));
        }

        return emojis;
    }

    public static List<ReactionEmoji> initializeNumberEmojis() {
        List<ReactionEmoji> emojis = new ArrayList<>();
        emojis.add(ReactionEmoji.of("0️⃣"));
        emojis.add(ReactionEmoji.of("1️⃣"));
        emojis.add(ReactionEmoji.of("2️⃣"));
        emojis.add(ReactionEmoji.of("3️⃣"));
        emojis.add(ReactionEmoji.of("4️⃣"));
        emojis.add(ReactionEmoji.of("5️⃣⃣"));
        emojis.add(ReactionEmoji.of("6️⃣"));
        emojis.add(ReactionEmoji.of("7️⃣"));
        emojis.add(ReactionEmoji.of("8️⃣"));
        emojis.add(ReactionEmoji.of("9️⃣"));
        emojis.add(ReactionEmoji.of("🔟"));
        return emojis;
    }

    public static boolean isAPicture(IMessage iMessage) {
        if (iMessage.getFormattedContent().contains("gyazo.com")) {
            return true;
        } else {
            for (Attachment a : iMessage.getAttachments()) {
                if (a.getUrl().endsWith(".png") || a.getUrl().endsWith(".jpeg") || a.getUrl().equals(".jpg")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void reactAllEmojis(IMessage iMessage, List<ReactionEmoji> emojis) {
        for (ReactionEmoji e : emojis)
            RequestBuffer.request(() -> iMessage.addReaction(e)).get(); //.get() is literally magic and fixes the entire universe
    }

    public static void reactiWithEmoji(IMessage iMessage, ReactionEmoji e) {
        RequestBuffer.request(() -> iMessage.addReaction(e)).get();
    }

    public static String getRandomFromListString(List<String> listString) {
        return listString.get(tlr.nextInt(listString.size()));
    }

    public static void joinVC(MessageReceivedEvent event) {
        IVoiceChannel voiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (voiceChannel == null) {
            BotUtils.sendMessage(event.getChannel(), "Join a voice chanel first, then use this command again");
            return;
        }
        voiceChannel.join();
    }

    public static String capitalizeFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String getStringFromUrl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();//Response response = client.newCall(request).execute()
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //kys
            return "error - url status request";
        }
    }

    //copied from poll generation command
    public static String buildOptions(List<String> intendedItemNames, int numOptions) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numOptions; i++) {
            sb.append(":regional_indicator_" + getCharFromInt(i) + ":\t\t" + intendedItemNames.get(i) + "\n");
        }
        sb.append("\n:x: cancel this request");
        return sb.toString();
    }

    private static String getCharFromInt(int i) {
        return i > -1 && i < 26 ? String.valueOf((char) (i + 'a')) : null; //super fucking janky
    }

    @Override
    public String toString() {
        return "Baka don't touch me!";
    }

}
