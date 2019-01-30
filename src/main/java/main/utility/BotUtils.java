package main.utility;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import info.debatty.java.stringsimilarity.Levenshtein;
import javafx.util.Pair;
import main.CommandManager;
import main.Main;
import main.passive.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BotUtils {

    //meta util -- Gson now public
    private static Random tlr = ThreadLocalRandom.current();
    public static Gson gson = new Gson();
    private static JsonParser jsonParser = new JsonParser();
    private static OkHttpClient client = new OkHttpClient();

    //leven
    private static Levenshtein leven = new Levenshtein();

    //encryption
    private static MessageDigest messageDigest;

    // Constants for use throughout the bot
    public final static String GITHUB_URL = "https://github.com/PhaseRush/Aspect";
    public final static String GITHUB_URL_SHORT = "github.com/PhaseRush/Aspect";
    public final static String DEV_DISCORD_STRING_ID = "264213620026638336";
    public final static long DEV_DISCORD_LONG_ID = 264213620026638336L;

    //guildID, commandPrefix
    private static final String mapFilePath = ""; //get rid of this
    private static Map<Long, String> PREFIX_MAP = new HashMap<>(); //adding angle brackets surpress warnings :)

    //API keys
    public static String DEFAULT_BOT_PREFIX = "$";
    public static String NASA_API;
    public static String DARK_SKY_API;
    public static String CLOUDSIGHT_API_KEY;
    public static String SMMRY_API_KEY;
    public static String FORTNITE_API_KEY; //updated key with fortnitetracker.com/site-api
    public static String WOLFRAM_API_KEY;
    public static String YOUTUBE_API_KEY;
    public static String WF_BOTTOM_TEXT_ID;
    public static String TICKET_MASTER_API_KEY;
    public static IChannel BOTTOM_TEXT;
    public static String PRIVATE_CHANNEL_INFO_URL;


    //dev meta -- USE FOR GIST GENERATION
    public static String DEV_GITHUB_NAME;
    public static String DEV_GITHUB_PASSWORD;


    //lock Util
    public static Set<IUser> bannedUsers = new LinkedHashSet<>();

    // weird flex but ok
    private static String[] wfboWeird = {"eccentric", "eerie", "peculiar", "unnatural"};
    private static String[] wfboFlex = {"boast", "display", "exhibit", "swagger"};
    private static String[] wfboBut = {"alas", "although", "however", "nevertheless", "though"};
    private static String[] wfboOk = {"adequate", "common", "decent", "sufficient", "tolerable", "admissible", "copacetic"};

    // dictionary
    public static Set<String> dictionary;

    // Utility objects
    private static ExecutorService listenerExecuter = Executors.newCachedThreadPool();

    // --- Static initializer --
    static {
        // encrypter
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignored) {}

        // dictionary
        String allString = BotUtils.getStringFromUrl("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt");
        dictionary = Arrays.stream(allString.split("\n")).map(String::toLowerCase).collect(Collectors.toCollection(TreeSet::new));
    }


    public static String getPrefix(IGuild iGuild) {
        try {
            return PREFIX_MAP.get(Long.valueOf(iGuild.getStringID()));
        } catch (NullPointerException e) {
            System.out.println("This key doesn't exist");
            return null;
        }
    }

    public static void setPrefix(IGuild iGuild, String prefix) {
        PREFIX_MAP.put(Long.valueOf(iGuild.getStringID()), prefix);
    }

    public static void setBottomText() {
        BOTTOM_TEXT = Main.client.getChannelByID(Long.valueOf(WF_BOTTOM_TEXT_ID));
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

    public static boolean writeToFile(String absolutePath, String text, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, append))) {
            writer.append("\n").append(text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
                        System.out.println("Missing Permissions: " + channel.getName() + " Msg: " + message);
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
                        System.out.println("Missing Permissions: " + channel.getName() + " EmbedBuilder: " + embedBuilder.toString());
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
                return  null;
            } catch (MissingPermissionsException e) {
                send(channel, "Missing Permissions: " + channel.getName());
                return null;
            }
        }).get();
    }

    public static void send(IChannel channel, List<String> messages) {
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
            System.out.println("Missing Permissions for message list: " + channel.getName());
        }
    }

    public static void send(IChannel channel, EmbedBuilder embed) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(embed.build());
            } catch (DiscordException e) {
                System.err.println("Embed could not be sent with error: ");
                e.printStackTrace();
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
                System.out.println("error in botutils#requestGet");
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
//                Main.client.getDispatcher().unregisterListener(reactionListener);
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
            char[] charArray = {0xD83C, (char) (0xDDE6 + i)};
            emojis.add(ReactionEmoji.of(String.valueOf(charArray)));
        }

        return emojis;
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
     * @param iMessage
     * @return
     */
    public static int numPictures(IMessage iMessage) {
        int imageCount = 0;
        if (iMessage.getFormattedContent().contains("gyazo.com")) {
            imageCount++;
        } else {
            for (Attachment a : iMessage.getAttachments()) {
                if (a.getUrl().endsWith(".png") || a.getUrl().endsWith(".jpeg") || a.getUrl().equals(".jpg")) {
                    imageCount++;
                }
            }
        }
        return imageCount;
    }

    public synchronized static void reactAllEmojis(IMessage iMessage, List<ReactionEmoji> emojis) {
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

    public static String getRandomFromListString(List<String> listString) {
        return listString.get(tlr.nextInt(listString.size()));
    }
    public static String getRandomFromArrayString(String[] strings) {
        return strings[ThreadLocalRandom.current().nextInt(strings.length)];
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
                .build();//Response response = client.newCall(request).execute()
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
                .build();//Response response = client.newCall(request).execute()
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
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
     * @param i
     * @return ith char of alphabet, null if input not valid
     */
    public static String ithChar(int i) {
        return i > -1 && i < 26 ? String.valueOf((char) (i + 'a')) : null; //super fucking janky
    }

    /**
     * Utility for sorting a map
     * needs work for sorting by key, since Entry#compareingByKey does not return correct type
     * @param map to sort
     * @param smallestToLargest sort from smallest to largest?
     * @param sortByValue sort by value?
     * @param <K>
     * @param <V>
     * @return sorted map
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

    public static String generateWeirdFlex(){
        StringBuilder sb = new StringBuilder();

        sb.append(getRandStrArr(wfboWeird)).append(" ");
        sb.append(getRandStrArr(wfboFlex)).append(" ");
        sb.append(getRandStrArr(wfboBut)).append(" ");
        sb.append(getRandStrArr(wfboOk));

        return sb.toString();
    }

    /**
     * generates sha-256 hash for an input string
     * @param input any arbitrary input String
     * @return sha256 hashed String output
     */
    public static String SHA256(String input) {
        try{
            byte[] hash = messageDigest.digest(input.getBytes(UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex) {
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

        return dispatchListeners;
    }

    public static String getNickOrDefault(MessageReceivedEvent event) {
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

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isLinux() {
        String os =  System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }

    /**
     *  credit to https://github.com/Lmperatoreq/HastebinAPI
     */
    public static String makeHasteGetUrl(String contents) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)new URL("https://hastebin.com/documents").openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("user-agent", "Java/Aspect-DiscordBot");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(contents);

        writer.flush();
        writer.close();

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());

        String key = jsonParser.parse(reader).getAsJsonObject().get("key").getAsString();

        reader.close();

        return key;
    }

    public static EmbedBuilder getQuoteEmbed() {
        String jsonArray = BotUtils.getStringFromUrl("http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1");
        String json = jsonArray.substring(1, jsonArray.length()-1);
        QuoteContainer quote = gson.fromJson(json, QuoteContainer.class);

        return new EmbedBuilder()
                .withTitle("Have a good day, everyone!")
                .withColor(Visuals.getVibrantColor())
                .withDesc(Jsoup.parse(quote.content).text() + "\n\t- " + quote.title);
    }
    private static class QuoteContainer {
        private int id;
        private String title;
        private String content;
        private String link;
    }

    public static long millisToNextHour24(int hour24) {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
        ZonedDateTime zonedNow = zonedDateTime.toLocalDate().atStartOfDay(zoneId);

        Instant today7amInstant = zonedNow.plusHours(hour24).toInstant();
        //System.out.println("today: " + (today7amInstant.toEpochMilli() - System.currentTimeMillis()));

        Instant tmr7amInstant = zonedNow.plusDays(1).plusHours(hour24).toInstant();
        //System.out.println("tmr: " + (tmr7amInstant.toEpochMilli() - System.currentTimeMillis()));

        Instant next7am;
        if (today7amInstant.isAfter(now)) next7am = today7amInstant;  // 7:00 today
        else next7am = tmr7amInstant; // 7:00 tmr

        return next7am.toEpochMilli() - now.toEpochMilli();
    }

    public static String cmdSpellCorrect(String inputStr) {
        return CommandManager.commandMap.entrySet().stream()
                .filter(e -> e.getValue().correctable()) // thanks phanta
                .filter(e -> Math.abs(e.getKey().length() - inputStr.length()) < 2)
                .map(e -> new Pair<>(e.getKey(), BotUtils.stringSimilarity(e.getKey(), inputStr)))
                .min(Comparator.comparingDouble(Pair::getValue))
                .filter(p -> p.getValue() < 2) // the order of this filter and min has caused fat debates in #programming-help
                .map(Pair::getKey)
                .orElse(null);
    }


    @Override
    public String toString() {
        return "Baka don't touch me!";
    }

}