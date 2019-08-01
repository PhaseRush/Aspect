package main.utility.metautil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import info.debatty.java.stringsimilarity.Levenshtein;
import main.Aspect;
import main.CommandManager;
import main.passive.*;
import main.utility.Visuals;
import main.utility.miscJsonObj.leaguequotes.LeagueQuoteContainer;
import main.utility.structures.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BotUtils {

    //meta util -- Gson now public
    private static final Random random = new Random();
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();
    private static final OkHttpClient client = new OkHttpClient();

    //leven
    private static final Levenshtein leven = new Levenshtein();

    //encryption
    private static MessageDigest messageDigest;

    // Constants for use throughout the bot
    public final static String GITHUB_URL = "https://github.com/PhaseRush/Aspect";
    public final static String GITHUB_URL_SHORT = "github.com/PhaseRush/Aspect";
    public final static String DEV_DISCORD_STRING_ID = "264213620026638336";
    public final static long DEV_DISCORD_LONG_ID = 264213620026638336L;

    //guildID, commandPrefix
    private static final String mapFilePath = ""; //get rid of this

    //API keys
    public static String DEFAULT_BOT_PREFIX = "$";
    public static String NASA_API;
    public static String DARK_SKY_API;
    public static String CLOUDSIGHT_API_KEY;
    public static String SMMRY_API_KEY;
    public static String FORTNITE_API_KEY; //updated key with fortnitetracker.com/site-api
    public static String APEX_LEGENDS_API_KEY; // using same service as fortnite tracker
    public static String WOLFRAM_API_KEY;
    public static String YOUTUBE_API_KEY;
    public static String WF_BOTTOM_TEXT_ID;
    public static String TICKET_MASTER_API_KEY;
    public static IChannel BOTTOM_TEXT;
    public static String PRIVATE_CHANNEL_INFO_URL;
    public static String DEEP_AI_API_KEY;

    public static String REDDIT_IGN;
    public static String REDDIT_PW;
    public static String REDDIT_CLIENT_ID;
    public static String REDDIT_SECRET;


    //dev meta -- USE FOR GIST GENERATION
    public static String DEV_GITHUB_NAME;
    public static String DEV_GITHUB_PASSWORD;

    // spotify api
    public static String SPOTIFY_CLIENT_ID = "";
    public static String SPOTIFY_CLIENT_SECRET = "";


    public static String FORTNITE_V2;

    //lock Util
    public static Set<IUser> bannedUsers = new LinkedHashSet<>();

    // weird flex but ok
    private static String[] wfboWeird = {"eccentric", "eerie", "peculiar", "unnatural"};
    private static String[] wfboFlex = {"boast", "display", "exhibit", "swagger"};
    private static String[] wfboBut = {"alas", "although", "however", "nevertheless", "though"};
    private static String[] wfboOk = {"adequate", "common", "decent", "sufficient", "tolerable", "admissible", "copacetic"};

    // dictionarySet
    public static Set<String> dictionarySet;
    public static ArrayList<String> dictionaryList;

    // Utility objects
    private static ExecutorService listenerExecuter = Executors.newSingleThreadExecutor();

    // prefix map util
    private static Type prefixMapType = new TypeToken<HashMap<String, String>>() {
    }.getType();
    private static HashMap<String, String> prefixMap = BotUtils.gson.fromJson(
            BotUtils.readFromFileToString(System.getProperty("user.dir") + "/data/prefix_map.json"),
            prefixMapType);
    // league quote map
    private static Type leagueQuoteMapType = new TypeToken<HashMap<String, LeagueQuoteContainer>>() {
    }.getType();
    public static HashMap<String, LeagueQuoteContainer> leagueQuoteMap = gson.fromJson(
            readFromFileToString(System.getProperty("user.dir") + "/data/league_quotes.json"),
            leagueQuoteMapType);

    public static String getPrefix(MessageReceivedEvent event) { // cant use IID b/c need to preserve channel > guild
        if (event.getChannel().isPrivate()) return BotUtils.DEFAULT_BOT_PREFIX;
        return prefixMap.getOrDefault(event.getChannel().getStringID(),
                prefixMap.getOrDefault(event.getGuild().getStringID(),
                        BotUtils.DEFAULT_BOT_PREFIX));
    }

    public static synchronized void setPrefix(IIDLinkedObject idObject, String prefix) {
        prefixMap.put(idObject.getStringID(), prefix);

        writeToFile(System.getProperty("user.dir") + "/data/prefix_map.json",
                gson.toJson(prefixMap),
                false);
    }

    // --- Static initializer --
    static {
        // encrypter
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignored) {
        }

        // dictionarySet
        String allString = BotUtils.getStringFromUrl("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt");
        dictionarySet = Arrays.stream(allString.split("\n")).map(String::toLowerCase).collect(Collectors.toCollection(TreeSet::new));
        dictionaryList = Arrays.stream(allString.split("\n")).map(String::toLowerCase).collect(Collectors.toCollection(ArrayList::new));
    }


    public static void setBottomText() {
        BOTTOM_TEXT = Aspect.client.getChannelByID(Long.valueOf(WF_BOTTOM_TEXT_ID));
    }

    public static List<String> insults = Arrays.asList(
            "You better pick a God and start praying.",
            "Your family tree is a 2 x 4",
            "If you were any more inbred you'd be a sandwich",
            "You're so ugly, you couldn't even arouse suspicion",
            "You continue to meet my expectations",
            "You're as useful as a Janna main playing [literally any champion that isn't Janna]",
            "Your mother is so old she has a separate entrance for black men.",
            "May you search for your children with a Geiger Counter.",
            "Your mom should've swallowed",
            "The only thing your mother wants for Christmas is a folded up flag",
            "You're not the dumbest person on planet Earth, but you better hope they don't die.",
            "If you were any stupider we'd have to water you",
            "I was pro-life before I met you"
    );

    // Handles the creation and getting of a IDiscordClient object for a token
    public static IDiscordClient getBuiltDiscordClient(String token) {

        // The ClientBuilder object is where you will attach your params for configuring the INSTANCE_ID of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withToken(token)
                .withRecommendedShardCount()
                .build();

    }

    public static boolean writeToFile(String absolutePath, String text, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, append))) {
            writer.append("\n").append(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> readFromFileToStringList(String path) {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
        }
        return null;
    }

    public static String readFromFileToString(String path) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }

    public static void msgDev(String msg) {
        send(Aspect.client.getUserByID(DEV_DISCORD_LONG_ID).getOrCreatePMChannel(), msg);
    }

    public static IMessage msgDevGet(String msg) {
        return sendGet(Aspect.client.getUserByID(DEV_DISCORD_LONG_ID).getOrCreatePMChannel(), msg);
    }

    // Helper functions to make certain aspects of the bot easier to use.
    public static void send(IChannel channel, String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (DiscordException e) {
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            } catch (MissingPermissionsException e) {
                send(channel, "Missing Permissions: " + channel.getName() + " Msg: " + message);
            }
        });
    }

    // redundant var just for distinction
    // cannot change other because will make the call bulky
    public static void send(IChannel channel, String path, boolean isFilePath) {
        File file = new File(path);
        RequestBuffer.request(() -> {
            try {
                channel.sendFile(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MissingPermissionsException e) {
                send(channel, "Missing Permissions: " + channel.getName());
            }
        });
    }

    public static IMessage sendGet(IChannel channel, String message) {
        return
                RequestBuffer.request(() -> {
                    try {
                        return channel.sendMessage(message);
                    } catch (DiscordException e) {
                        System.err.println("Message could not be sent with error: ");
                        e.printStackTrace();
                        return null;
                    } catch (MissingPermissionsException e) {
                        Aspect.LOG.info("Missing Permissions: " + channel.getName() + " Msg: " + message);
                        return null;
                    }
                }).get();
    }

    public static IMessage sendGet(IChannel channel, EmbedBuilder embedBuilder) {
        return
                RequestBuffer.request(() -> {
                    try {
                        return channel.sendMessage(embedBuilder.build());
                    } catch (DiscordException e) {
                        System.err.println("Message could not be sent with error: ");
                        e.printStackTrace();
                        return null;
                    } catch (MissingPermissionsException e) {
                        Aspect.LOG.info("Missing Permissions: " + channel.getName() + " EmbedBuilder: " + embedBuilder.toString());
                        return null;
                    }
                }).get();
    }

    public static IMessage sendGet(IChannel channel, EmbedObject embedObject) {
        return
                RequestBuffer.request(() -> {
                    try {
                        return channel.sendMessage(embedObject);
                    } catch (DiscordException e) {
                        System.err.println("Message could not be sent with error: ");
                        e.printStackTrace();
                        return null;
                    } catch (MissingPermissionsException e) {
                        Aspect.LOG.info("Missing Permissions: " + channel.getName() + " EmbedObject: " + embedObject.toString());
                        return null;
                    }
                }).get();
    }

    public static IMessage sendGet(IChannel channel, String path, boolean isFilePath) {
        File file = new File(path);
        return RequestBuffer.request(() -> {
            try {
                return channel.sendFile(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (MissingPermissionsException e) {
                send(channel, "Missing Permissions: " + channel.getName());
                return null;
            }
        }).get();
    }

    public static void send(IChannel channel, Collection<String> messages) {
        try {
            for (String s : messages) {
                RequestBuffer.request(() -> {
                    channel.sendMessage(s);
                });
            }
        } catch (DiscordException e) {
            System.err.println("Message could not be sent with error: ");
            e.printStackTrace();
        } catch (MissingPermissionsException e) {
            Aspect.LOG.info("Missing Permissions for message list: " + channel.getName());
        }
    }

    public static void send(IChannel channel, EmbedBuilder embed) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(embed.build());
            } catch (DiscordException e) {
                System.err.println("Embed could not be sent with error: ");
                e.printStackTrace();
            } catch (NullPointerException npe) { // subreddit scheduler, likely due to the client not having access to the channel
                // ignored
            }
        });
    }

    public static void send(IChannel channel, EmbedBuilder embed, File file) {
        RequestBuffer.request(() -> {
            try {
                channel.sendFile(embed.build(), file);
            } catch (DiscordException e) {
                System.err.println("Embed could not be sent with error: ");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("File could not be found with error: ");
                e.printStackTrace();
            }
        });
    }

    public static void send(IChannel channel, EmbedBuilder embed, InputStream inStream, String fileName) {
        RequestBuffer.request(() -> {
            try {
                channel.sendFile(embed.build(), inStream, fileName);
            } catch (DiscordException e) {
                System.err.println("Embed could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }

    public static void reactGet(IMessage message, ReactionEmoji emoji) {
        RequestBuffer.request(() -> {
            try {
                message.addReaction(emoji);
            } catch (Exception e) {
                Aspect.LOG.info("error in botutils#requestGet");
            }
        }).get();
    }

    // temp listener
    // 100% cpu?
//    public static synchronized void unregisterListener(IListener reactionListener, int timeoutMillis, IMessage embedMsg) {
//        listenerExecuter.execute(() -> {
//            try {
//                Thread.sleep(timeoutMillis);
//            } catch (InterruptedException ignored) {
//            } finally { //please just execute this no matter what
//                Aspect.client.getDispatcher().unregisterListener(reactionListener);
//                if (!embedMsg.isDeleted()) embedMsg.delete();
//            }
//        });
//    }

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

    public static String getJson(String jsonURL, int timeoutMillis) {
        try {
            String userJson;
            URL url = new URL(jsonURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeoutMillis);
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

    public static String getRandStrArr(String[] stringArray) {
        return stringArray[new Random().nextInt(stringArray.length)];
    }

    public static String buildString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(s -> sb.append(s).append(","));
        return sb.toString();
    }

    public static List<ReactionEmoji> getRegionals() {
        List<ReactionEmoji> emojis = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            emojis.add(getRegionalChar((char) ('a' + i)));
        }

        return emojis;
    }

    public static ReactionEmoji getRegionalChar(char c) {
        return ReactionEmoji.of(
                String.valueOf(
                        new char[]{0xD83C, (char) (0xDDE6 + c - 97)}
                )
        );
    }

    // need to use unicode
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

    /**
     * was originally BotUtils#isAPicture
     * now returns num of pictures (untested)
     *
     * @param iMessage
     * @return
     */
    public static long numPictures(IMessage iMessage) {
        return iMessage.getAttachments().stream()
                .map(attachment -> attachment.getUrl().replaceAll("[^A-Za-z0-9]", ""))
                .filter(BotUtils::anyContain)
                .count()
                + (anyContain(iMessage.getContent()) ? 1L : 0L);
    }

    private static boolean anyContain(String s) {
        return s.contains("png") || s.contains("jpeg") || s.contains("jpg") || s.contains("steamuserimage") ||
                s.contains("gyazo") || s.contains("prntscr");
    }

    public static boolean isPicture(String url) {
        return url.contains("gyazo.com") || url.endsWith(".png") || url.endsWith(".jpeg") || url.equals(".jpg");
    }


    // dont need sync b/c could be diff servers
    public static void reactAllEmojis(IMessage iMessage, Collection<ReactionEmoji> emojis) {
        for (ReactionEmoji e : emojis)
            RequestBuffer.request(() -> iMessage.addReaction(e)).get(); //.get() is literally magic and fixes the entire universe -- because it blocks thread until last emoji finished
    }

    public static void reactiWithEmoji(IMessage iMessage, ReactionEmoji e) {
        RequestBuffer.request(() -> iMessage.addReaction(e)).get();
    }

    public static void reactWithCheckMark(IMessage iMessage) {
        if (iMessage == null) return;
        RequestBuffer.request(() -> iMessage.addReaction(ReactionEmoji.of("\u2705")));
    }

    public static void reactWithX(IMessage iMessage) {
        if (iMessage == null) return;
        RequestBuffer.request(() -> iMessage.addReaction(ReactionEmoji.of("\u274C")));
    }

    public static <T> T getRandFromCollection(Collection<T> collection) {
        List<T> tempList = new ArrayList<>(collection);
        return tempList.get(random.nextInt(tempList.size()));
    }

    public static <T> T getRandFromArray(T[] array) {
        return array[random.nextInt(array.length)];
    }

    public static void joinVC(MessageReceivedEvent event) {
        IVoiceChannel voiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (voiceChannel == null) {
            BotUtils.send(event.getChannel(), "Join a voice chanel first, then use this command again");
            return;
        }
        voiceChannel.join();
    }

    public static String capitalizeFirstLowerRest(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String capitalizeFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String getStringFromUrl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //kys
            return "error - url status request - getstringfromurl";
        }
    }

    public static String getStringFromUrl(String url, String headerName, String headerValue) {
        Request request = new Request.Builder()
                .url(url)
                .header(headerName, headerValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //kys
            return "error - url status request - getstringfromurl - with header";
        }
    }

    public static String getStringFromUrl(String url, String headerName1, String headerValue1, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .header(headerName1, headerValue1)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //kys
            return "error - url status request - getstringfromurl - with header";
        }
    }

    public static String getStringFromURL(String url, List<String> headerNames, List<String> headerValues) {
        Request.Builder request = new Request.Builder()
                .url(url);
        for (int i = 0; i < headerNames.size(); i++) {
            request.header(headerNames.get(i), headerValues.get(i));
        }
        try (Response response = client.newCall(request.build()).execute()) {
            return response.body().string();
        } catch (IOException | NullPointerException e) {
            //kys
            return "error - url status request - getstringfromurl - with header";
        }
    }

    //copied from poll generation command
    public static String buildOptions(List<String> intendedItemNames, int numOptions) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numOptions; i++)
            sb.append(":regional_indicator_" + ithChar(i) + ":\t\t" + intendedItemNames.get(i) + "\n");

        return sb.append("\n:x: cancel this request").toString();
    }

    /**
     * returns the ith char of the alphabet.
     * NOTE: 0-based index, ie: a is the 0th char
     *
     * @param i
     * @return ith char of alphabet, null if input not valid
     */
    public static String ithChar(int i) {
        return i > -1 && i < 26 ? String.valueOf((char) (i + 'a')) : null; //super fucking janky
    }

    /**
     * Utility for sorting a initDataMap
     * needs work for sorting by key, since Entry#compareingByKey does not return correct type
     *
     * @param map               to sort
     * @param smallestToLargest sort from smallest to largest?
     * @param sortByValue       sort by value?
     * @param <K>
     * @param <V>
     * @return sorted initDataMap
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMap(Map<K, V> map, boolean smallestToLargest, boolean sortByValue) {
        List<Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
        if (sortByValue) entryList.sort(Entry.comparingByValue());
//        else entryList.sort(Entry.comparingByKey());

        if (!smallestToLargest) Collections.reverse(entryList);

        Map<K, V> result = new LinkedHashMap<>();

        entryList.forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    public static String millisToHMS(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) (millis / (1000 * 60 * 60));

        return (hours == 0 ? "" : hours + ":") +
                (hours != 0 && minutes < 10 ? "0" + minutes : minutes) + ":" +
                (seconds < 10 ? "0" + seconds : seconds);
    }

    public static String millisToMS(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);

        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Limits the length of a String
     *
     * @param s            input string
     * @param length       target length
     * @param useDotDotDot end with "..."? length includes ...
     * @param cutAtChar    end the string at the following character?
     * @param cutChar      character to use if cutAtChar is true
     * @return string within target length
     */
    public static String limitStrLen(String s, int length, boolean useDotDotDot, boolean cutAtChar, char cutChar) {
        // if str is shorter just return
        if (s.length() <= length) return s;

        // if don't care just return substr
        if (!useDotDotDot && !cutAtChar) return s.substring(0, length);

        int cutIndex = useDotDotDot ? length - 4 : length; // 4 = 3 dots + 1 space

        // only update cutIndex if we want to cut at space
        if (cutAtChar) {
            // iter down until we hit a space
            for (int i = cutIndex; i > 0; i--) {
                if (s.charAt(i) == cutChar) {
                    cutIndex = i; // set cutIndex to current iter and break
                    break;
                }
            }
        }

        return s.substring(0, cutIndex) + (useDotDotDot ? " ...\n" : "\n");
    }

    public static double stringSimilarity(String s1, String s2) {
        return leven.distance(s1, s2);
    }

    public static int stringSimilarityInt(String s1, String s2) {
        return (int) leven.distance(s1, s2);
    }

    public static String generateWeirdFlex() {
        return new StringBuilder()
                .append(getRandStrArr(wfboWeird)).append(" ")
                .append(getRandStrArr(wfboFlex)).append(" ")
                .append(getRandStrArr(wfboBut)).append(" ")
                .append(getRandStrArr(wfboOk))
                .toString();
    }

    /**
     * generates sha-256 hash for an input string
     *
     * @param input any arbitrary input String
     * @return sha256 hashed String output
     */
    public static String SHA256(String input) {
        try {
            byte[] hash = messageDigest.digest(input.getBytes(UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }

//    public static <T extends Number> getMin(T first, T second) {
//        return (first > second? second : first);
//    }

    public static List<Object> createListeners() {
        List<Object> dispatchListeners = new ArrayList<>();

        dispatchListeners.add(new CommandManager());
        dispatchListeners.add(new PassiveListener());
        dispatchListeners.add(new WfPassive());
        dispatchListeners.add(new PokemonIdentifier());
        dispatchListeners.add(new PrivateChannelVerification()); // Thanks Resuna!
        dispatchListeners.add(new CutePassive());
        dispatchListeners.add(new ScheduledActions());
        dispatchListeners.add(new ScheduledSubreddits());

        return dispatchListeners;
    }

    public static String getNickOrDefault(MessageReceivedEvent event) {
        if (event.getChannel().isPrivate()) return event.getAuthor().getName();
        String nick = event.getAuthor().getNicknameForGuild(event.getGuild());
        if (nick == null) return event.getAuthor().getName();
        else return nick;
    }

    public static String getNickOrDefault(IUser user, IGuild guild) {
        String nick = user.getNicknameForGuild(guild);
        if (nick == null) return user.getName();
        else return nick;
    }

    public static String getTodayYYYYMMDD() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static boolean isDev(MessageReceivedEvent event) {
        return event.getAuthor().getStringID().equals(DEV_DISCORD_STRING_ID);
    }

    // this java app doesnt run on mac hopefully
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }

    /**
     * credit to https://github.com/Lmperatoreq/HastebinAPI
     */
    public static String makeHasteGetUrl(String contents) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://hasteb.in").openConnection(); // might want to try hasteb.in

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("user-agent", "Aspect");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(contents);

        writer.flush();
        writer.close();

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());

        String key = jsonParser.parse(reader).getAsJsonObject().get("key").getAsString();

        reader.close();

        return "https://hastebin.com/" + key;
    }

    public static EmbedBuilder getQuoteEmbed() {
        String jsonArray = getStringFromUrl("http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1");
        String json = jsonArray.substring(1, jsonArray.length() - 1);
        QuoteContainer quote = gson.fromJson(json, QuoteContainer.class);

        return new EmbedBuilder()
                .withTitle("Have a good day, everyone!")
                .withColor(Visuals.getRandVibrantColour())
                .withDesc(Jsoup.parse(quote.content).text() + "\n\t- " + quote.title);
    }

    private static class QuoteContainer {
        private int id;
        private String title;
        private String content;
        private String link;
    }

    /**
     * generates an embed which contains a league quote, the champion, and the status/state in which they say it
     *
     * @return EmbedBuilder with quote, champ, and stauts information
     */
    public static EmbedBuilder getRandLeagueQuoteEmbed() {
        final String champName = getRandFromCollection(leagueQuoteMap.keySet());// champs.get(ThreadLocalRandom.current().nextInt(champs.size()));

        return getLeagueQuoteForChamp(champName);
    }

    public static EmbedBuilder getLeagueQuoteForChamp(String champName) {
        final LeagueQuoteContainer quoteObj = leagueQuoteMap.get(champName);
        final List<Map.Entry<String, Map<String, String>>> statusListMap = new ArrayList<>(quoteObj.getQuotes().entrySet());
        Map.Entry<String, Map<String, String>> randStatusQuote = getRandFromCollection(statusListMap);

        while (randStatusQuote.getValue().values().isEmpty()) { // skip null/empty quotes
            randStatusQuote = getRandFromCollection(statusListMap);
        }

        final String statusQuote = getRandFromCollection(randStatusQuote.getValue().values());

        return new EmbedBuilder()
                .withColor(Visuals.getRandVibrantColour())
                .withDesc(statusQuote + "\n\t-" + champName + ",\t" + randStatusQuote.getKey());
    }

    /**
     * returns milliseconds to next Xth hour
     * <p>
     * note: Timezone is Los Angeles / Pacific
     *
     * @param hour24 24 hour format for desired target time
     * @return milliseconds until hour24
     */
    public static long millisToNextHour24(int hour24, String timeZoneName) {
        return millisToNextHHMMSSMMMM(hour24, 0, 0, 0, timeZoneName);
    }

    /**
     * used to determine milliseconds until next specified time.
     * <p>
     * example: 18:40 in Vancouver (local)
     *
     * @param hours24      18
     * @param minutes      40
     * @param seconds      0
     * @param millis       0
     * @param timeZoneName "America/Los_Angeles"
     * @return millis until specified time  (18:40 in vancouver for the example above)
     */
    public static long millisToNextHHMMSSMMMM(int hours24, int minutes, int seconds, int millis, String timeZoneName) {
        // validate timezone
        if (!ZoneId.getAvailableZoneIds().contains(timeZoneName))
            throw new ZoneRulesException("Invalid zone name:\t" + timeZoneName);

        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of(timeZoneName);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
        ZonedDateTime zonedNow = zonedDateTime.toLocalDate().atStartOfDay(zoneId);

        // today at input time
        Instant todayInst = zonedNow.plusHours(hours24)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusNanos((long) (millis * 10E6))
                .toInstant();

        // tmr at input time
        Instant tmrInst = zonedNow
                .plusDays(1) // tmr
                .plusHours(hours24)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusNanos((long) (millis * 10E6))
                .toInstant();

        // take the one that is right after now
        Instant nextInst = (todayInst.isAfter(now) ? todayInst : tmrInst);

        return nextInst.toEpochMilli() - now.toEpochMilli();
    }

    /**
     * unregisteres a event listener after 10 seconds
     *
     * @param embedMessage
     * @param reactionListener
     * @param event
     */
    public static void unregListenerAfter10sec(IMessage embedMessage, IListener reactionListener, MessageReceivedEvent event) {
        Runnable removeListener = () -> {                        // unregister listener after x ms
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            } finally { //please just execute this no matter what
                event.getClient().getDispatcher().unregisterListener(reactionListener);
                if (!embedMessage.isDeleted()) embedMessage.delete();

            }
        };
        listenerExecuter.execute(removeListener);
    }


    /**
     * @param inputStr input string to perform spell correction on
     * @return name of command that mostly closely resembles the input string, or null if closest string has
     * > 2 levenshtein distance, or if the command has blacklisted this particular spellcheck
     * <p>
     * note: cannot use autocorrect function because we have special blacklist requirements
     */
    public static String cmdSpellCorrect(String inputStr) {
        return CommandManager.commandMap.entrySet().stream()
                .filter(e -> e.getValue().correctable()) // thanks phanta
                .filter(e -> Math.abs(e.getKey().length() - inputStr.length()) < 2)
                .map(e -> new Pair<>(e.getKey(), BotUtils.stringSimilarityInt(e.getKey(), inputStr)))
                .min(Comparator.comparingDouble(Pair::getValue))
                .filter(p -> p.getValue() < 2) // the order of this filter and min has caused fat debates in #programming-help
                .map(Pair::getKey)

                // this part is for checking the blacklist
                .map(name -> new Pair<>(name, CommandManager.commandMap.get(name)))
                .filter(pair -> !pair.getValue().getAutocorrectBlackList().contains(inputStr))
                .map(Pair::getKey)
                .orElse(null);
    }

    /**
     * dont use empty list plz
     *
     * @param input string to spell check
     * @param pool  pool of strings to check from
     * @return closest string from the pool by levenshtein distance
     */
    public static String autoCorrect(String input, Collection<String> pool) {
        return pool.stream()
                .min(Comparator.comparingDouble(str -> leven.distance(str, input)))
                .get();
    }

    public static String autoCorrectChampName(String input) {
        return autoCorrect(input, leagueQuoteMap.keySet());
    }

    public static IVoiceChannel fuzzyVoiceMatch(MessageReceivedEvent event, String arg) {
        return
                event.getGuild().getVoiceChannelsByName(
                        event.getGuild().getVoiceChannels().stream()
                                .map(IChannel::getName)
                                .map(name -> new Pair<>(name, name.toLowerCase().replaceAll("^[ -~]", "").trim()))
                                .sorted(Comparator.comparingDouble(o -> Math.min(
                                        stringSimilarity(o.getKey(), arg),
                                        stringSimilarity(o.getValue(), arg.toLowerCase()))))
                                .filter(pair ->
                                        stringSimilarity(pair.getKey(), arg) < Math.max(2, pair.getKey().length() / 2) ||
                                                stringSimilarity(pair.getValue(), arg.toLowerCase()) < Math.max(2, pair.getValue().length() / 2))
                                .findFirst()
                                .get().getKey()
                ).get(0);
    }

    public static boolean containsIgnoreCase(List<String> list, String s) {
        return list.stream()
                .map(String::toLowerCase)
                .anyMatch(str -> str.equals(s.toLowerCase()));
    }

    public static String concatArgs(List<String> args) {
        return String.join(" ", args);
    }

    public static String concatArgs(List<String> args, String delim) {
        return String.join(delim, args);
    }

    public static String concatArgs(String[] args) {
        return concatArgs(Arrays.stream(args).collect(Collectors.toList()));
    }

    /**
     * https://stackoverflow.com/a/611117/10583298
     * Encodes the passed String as UTF-8 using an algorithm that's compatible
     * with JavaScript's <code>encodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param s The String to be encoded
     * @return the encoded String
     */
    public static String encodeURIComponent(String s) {
        String result = null;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("%21", "!")
                    .replaceAll("%27", "'")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%7E", "~");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }

    public static String concatArgs(String[] args, String delim) {
        return concatArgs(Arrays.stream(args).collect(Collectors.toList()), delim);
    }

    public static <T> T jsonUrlToPojo(String url, Class<T> klass) {
        return gson.fromJson(getStringFromUrl(url), klass);
    }

    @Override
    public String toString() {
        return "Baka don't touch me!";
    }

}