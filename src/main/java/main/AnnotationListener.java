package main;

import com.google.gson.Gson;
import main.utility.MessageDeleter;
import main.utility.ReadWrite;
import main.utility.Visuals;
import main.utility.chat.ChatRestriction;
import main.utility.chat.ChatRestrictionManager;
import main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser.FGameType;
import main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser.FPlayer;
import main.utility.maths.UnitConverter;
import main.utility.metautil.BotUtils;
import main.utility.mooshroom.RealmEye.GuildUtil.RealmEyeGuild;
import main.utility.mooshroom.RealmEye.GuildUtil.RealmEyeGuildMember;
import main.utility.mooshroom.RealmEye.PetUtil.Pet;
import main.utility.mooshroom.RealmEye.PetUtil.RealmEyePets;
import main.utility.mooshroom.RealmEye.PlayerUtil.Character;
import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import main.utility.mooshroom.RotmgDeath;
import org.apache.commons.io.IOUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ---------------------------------------------------------------
 * THIS IS FROM THE OLD BOT. NONE OF THIS CLASS IS IN USE ANYMORE.
 * ---------------------------------------------------------------
 */
@SuppressWarnings("all") // yikes dont look too hard
public class AnnotationListener {
    private IDiscordClient client;
    private List<String> bannedUsers = new ArrayList<>();
    private List<IUser> allUsers = new ArrayList<>();
    private List<IUser> friendlyUsers = new ArrayList<>();
    private Visuals vis = new Visuals();
    private Random r = new Random();

    //music
    private List<String> musicURLs = new ArrayList<>();
    private List<String> musicPlaylistURLs = new ArrayList<>();

    //RotMG
    private String katInvImgur = "https://http.cat/404";
    private RotmgDeath latestDeath;
    private final String katInvFilePath = "C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\katinvurl.txt";
    private final String mooshroomHistory = "C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\mooshroomMembers.txt"; //todo-- haha this has been a todo for like a month now :)
    private final String mooshroomNukeHistory = "C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\nukehistory.txt";
    private final String mooshroomPunishRecord = "C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\punishmentRecord.txt";
    private final String flowerDancePath = "C:\\Users\\Positron\\Music\\FlowerDance.mp3";

    private List<IUser> mooshies = new ArrayList<>();
    private List<String> meowable = Arrays.asList("295009127267041284");
    private List<String> hasMooshroomPerms = new ArrayList<>();
    private ChatRestrictionManager masterChatRestrictionManager = new ChatRestrictionManager();

    //random things that shouldnt be here but are anyways :)
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String[] hurricanes = {"Maria", "Irma", "Matthew", "Katrina"};
    private String[] kittyQuotes = {"*Yawn*", "*Yawn* Who woke me up?", "Im telling Kat!", ":3", "Hiiiiii", "Im tired", "Meow :3", "mew mew"};
    private String[] questions = {"Likes sushi", "Thinks chairs are for sitting", "Actually stays awake in class", "Has petted Kat before", "\"nerf singed\"", "Eats olives for a living", "Drops loot lake when kat wants to"};

    UnitConverter unitConverter = new UnitConverter();
    private List<String> massUnits = new ArrayList<>();
    private List<String> distanceUnits = new ArrayList<>();
    private List<String> volumeUnits = new ArrayList<>();

    private List<String> elevatedUserStringIDs = new ArrayList<>();
    private List<ReactionEmoji> regionalIndicators = BotUtils.getRegionals();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private boolean shellRestricted = false;
    private boolean chatRestricted = false;

    //utility initialization
    ReadWrite readerWriter = new ReadWrite();

    public AnnotationListener(List<String> musicPlaylistUrls, List<String> musicURLs, IDiscordClient client) {
        this.musicURLs = musicURLs;
        this.musicPlaylistURLs = musicPlaylistUrls;
        this.client = client;
    }
    private void initMusicURLs() {
        musicURLs.add("https://www.youtube.com/watch?v=nOLqvFOqXtw");
        musicURLs.add("https://www.youtube.com/watch?v=7PYe57MwxPI");
        musicURLs.add("https://www.youtube.com/watch?v=7xFe0vkUJXU");
        musicURLs.add("https://www.youtube.com/watch?v=Rila7A6Ut_4");
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        //do stuff when the bot is first ready to do stuffs :)
        //@annotations tell which methods get triggered.
        //initMusicURLs();
        client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, "butterflies out the window");

        elevatedUserStringIDs.add("264213620026638336");
        elevatedUserStringIDs.add("145338465783906304");

        massUnits = unitConverter.allWeightUnits();
        volumeUnits = unitConverter.allVolumeUnits();
        distanceUnits = unitConverter.allDistanceUnits();


        int localMinute = LocalTime.now().getMinute();

        Aspect.LOG.info("Kitty Kat is ready to go at:  " + "\u001B[37;1m" +LocalDateTime.now().getHour() + " : " + ((numDigits(localMinute) == 1) ? "0" + localMinute : localMinute) + "\u001B[0m");
    }

    //Literally copied from StackOverflow
    //https://stackoverflow.com/questions/1306727/way-to-get-number-of-digits-in-an-int
    private int numDigits(int n) {
        if (n < 100000){
            // 5 or less
            if (n < 100){
                // 1 or 2
                if (n < 10)
                    return 1;
                else
                    return 2;
            }else{
                // 3 or 4 or 5
                if (n < 1000)
                    return 3;
                else{
                    // 4 or 5
                    if (n < 10000)
                        return 4;
                    else
                        return 5;
                }
            }
        } else {
            // 6 or more
            if (n < 10000000) {
                // 6 or 7
                if (n < 1000000)
                    return 6;
                else
                    return 7;
            } else {
                // 8 to 10
                if (n < 100000000)
                    return 8;
                else {
                    // 9 or 10
                    if (n < 1000000000)
                        return 9;
                    else
                        return 10;
                }
            }
        }
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        IUser author = event.getAuthor();
        String message = event.getMessage().getFormattedContent();
        IMessage iMessage = event.getMessage();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();

        //if it is a bot, don't bother doing anything else
        if(author.isBot()) {
            if(new Random().nextDouble()>.8)
                channel.sendMessage("beep boop, im a bot too");
            return;
        }

        //Global
        //add "friends" to the friend zone
        //be sure to toggle this accordingly :)
        /*
        if(author.getStringID().equals("139630617703743488")) { //yours truly, Danman96
            bannedUsers.add(author.getName());
        }*/

        if(message.matches(".*(heart)+.*")) {
            channel.sendMessage(":heart:");
        } else if(message.equals("!music")) {
            //needs work ^tm
            channel.sendMessage("https://www.youtube.com/watch?v=s6LCzGPbBnY");

        } else if(message.equals("!anthem")) {
            channel.sendMessage("https://www.youtube.com/watch?v=BVzlL5z38Fs");
        } else if (message.matches(".*(xkcd)+.*")) {
            handleXKCD(channel);
        } else if (message.matches(".*\\s(prof)\\s+.*")) {
            channel.sendMessage("Question?... speak to my professor.\nhttp://phdcomics.com/comics/archive.php?comicid=1718");
        } else if (message.equals("!help")) {
            RequestBuffer.request(() -> {
                handleHelpMessage(author, channel);
            });
        } else if (message.toLowerCase().matches("!j[a-z]*(voice)")) { //!jvoice or !joinvoice
            handleJoinVoice(event);
        } else if (message.toLowerCase().matches("!l[a-z]*(voice)")) { //!lvoice or !leavevoice
            handleLeaveVoice(event);
        } else if (message.toLowerCase().startsWith("!die")) {
            handleAudioWithMessage(event, "```DIE DIE DIE```", "C:\\Users\\Positron\\Music\\die.mp3");
        } else if (message.toLowerCase().startsWith("!fd")) {
            handleAudioWithMessage(event, "This song is so good it's leaving me speechless \n\t-Danman96", flowerDancePath);
        } else if (message.toLowerCase().startsWith("!deletemsg")) {
            if (elevatedUserStringIDs.contains(author.getStringID())) {
                if(message.contains(",")) {
                    new MessageDeleter(message.substring(10), author, channel).runDelete(guild);
                    Aspect.LOG.info(author.getName() + "triggered MessageDelete at: " + LocalTime.now().toString());
                } else {
                    channel.sendMessage("<@" + author.getStringID() + ">, you forgot to use a comma");
                    return; //do not execute rest
                }
            }
            else
                channel.sendMessage("Only authorized users can use this!");
        } else if (message.toLowerCase().startsWith("!bulkdelete")) { //todo
            if (elevatedUserStringIDs.contains(author.getStringID())) {
                int timeToDeleteFrom = Integer.parseInt(message.substring(12));
                new MessageDeleter(timeToDeleteFrom, channel, author).runBulkDelete();
            }
            else
                channel.sendMessage("Only authorized users can use this!");
        } else if ("!love".equals(message)) {//migrated
        } else if ("!dev".equals(message) || message.equals("!kat")) { //embed for me :)
            channel.sendMessage("Here's <@264213620026638336>'s info card:");
            RequestBuffer.request(() -> channel.sendMessage(createKatEmbed().build()));
        } else if (message.toLowerCase().equals("!kittykat")) { //embed f   or Bot
            RequestBuffer.request(() -> channel.sendMessage(createKittyKatBotEmbed().build()));
        } else if (message.startsWith("!conv")) {
            String parsedMsg = message.split("\\s",2)[1];
            handleUnitConversion(parsedMsg, channel);
        } else if (message.toLowerCase().equals("!units")) {
            channel.sendMessage(
                    "Mass/Weight: ```" + printStringList(massUnits) + "```" +
                            "Distance/Length: ```" + printStringList(distanceUnits) + "```" +
                            "Volume: ```" + printStringList(volumeUnits) + "```");

        } else if (message.toLowerCase().equals("!fixyourmic")) {
            channel.sendMessage("Take your mic out of hurricane " + getRandomFromStringArray(hurricanes) + "!");
        } else if (message.toLowerCase().startsWith("!poll")) {
            String parsedInput = message.substring(6);
            List<String> splitParsed = Arrays.asList(parsedInput.split(", "));
            List<String> questionList = splitParsed.subList(1, splitParsed.size());
            handlePoll(channel, splitParsed, questionList, author);
            iMessage.delete();
        } else if (message.toLowerCase().startsWith("!fn")) {
            long startTime = System.currentTimeMillis();
            String ign = message.substring(4);
            String[] parsedMsg = ign.split(", ");
            String json = getJson("https://fortnite.y3n.co/v2/player/" + parsedMsg[0], true);
            handleFortniteStatsEmbed(json, parsedMsg[1], startTime, channel); //'solo', 'duo', 'squad', 'all'
        }

        //Kitty Kat Test Site
        if(guild.getStringID().equals("417926479813279754")) {
            if (message.equals("!ping")) {
                try {
                    Thread.sleep(r.nextInt(1000));
                } catch (InterruptedException e) {
                    //i dont care dont do it cuz i dont wanna deal with it
                }
                channel.sendMessage("pong!");
            } else if(message.matches(".")) {
                channel.sendMessage("Very nice single character message. :3");
            }
        } else if (guild.getStringID().equals("287811950216478732")) {
            mooshies = guild.getUsers(); //sets the list of Users in Mooshroom
            mooshroomEvent(author, message, channel, guild, event.getMessage(), event); //event param for audio player
        }

        handleCatText(event.getMessage());


        //Druddigon's Den

        if (guild.getStringID().equals("225402166561603585")) { //Druddigon's Den
            druddigonEvent(author, message, channel, guild, event.getMessage(), event);
        }

        //other servers

        //prevent spam
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException ie) {
            Aspect.LOG.info("Thread Sleep Problem. main.AnnotationListener.onMessageReceived");
            ie.printStackTrace();
        }
    }

    private void handleFortniteStatsEmbed(String json, String gameType, long startTime, IChannel channel) { //'solo', 'duo', 'squad', 'all'
        FPlayer fp = new Gson().fromJson(json, FPlayer.class);
        EmbedBuilder eb = new EmbedBuilder();
        FGameType fGameType = null;
        try {
            fGameType = fp.getBattleRoyal().getStats().getPc().getSolo(); //defaults to solo
        } catch (Exception e) {
            channel.sendMessage("Something went very wrong. If you typed your name correctly, tell kat to fix her shit");
            return; //dont do anything else if this failed.
        }

        switch (gameType) {
            case "duo":
                fGameType = fp.getBattleRoyal().getStats().getPc().getDuo();
                break;
            case "squad":
                fGameType = fp.getBattleRoyal().getStats().getPc().getSquad();
                break;
            case "all":
                fGameType = fp.getBattleRoyal().getStats().getPc().getAll();
                break;
        }

        eb.withAuthorName(fp.getDisplayName() + ", " + gameType/* + ", level " + fp.getBattleRoyal().getProfile().getLevel()*/)
                .withColor(vis.getRandVibrantColour())
                .withDesc("*FortniteStats Statistical Analysis*")
                .appendField("Kills", String.valueOf(fGameType.getKills()), true)
                .appendField("Kpm", String.valueOf(fGameType.getKpm()), true)
                .appendField("Kpd", String.valueOf(fGameType.getKpd()), true)
                //.appendField("-", "-", false)
                .appendField("Matches Played", String.valueOf(fGameType.getMatchesPlayed()), true)
                .appendField("Minutes Played", String.valueOf(fGameType.getMinutesPlayed()), true)
                //.appendField("-", "-", false)
                .appendField("Wins", String.valueOf(fGameType.getWins()), true)
                .appendField("Winrate %", String.valueOf(fGameType.getWinrate()), true)
                .appendField("Score", String.valueOf(fGameType.getScore()), true)
                .appendField(getRandomFromStringArray(questions), (new Random().nextBoolean() ? "yes" : "no"), true)
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("This information was last updated " + fp.getLastUpdate());

        OffsetDateTime updateTime = OffsetDateTime.parse(fp.getLastUpdate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        RequestBuffer.request(() -> {
            channel.sendMessage(eb.withFooterText("Updated " + updateTime.format(formatter) +
                    ". This operation took me " + String.valueOf(System.currentTimeMillis()-startTime) + " ms  :3").build());
        });
    }

    private void handlePoll(IChannel channel, List<String> splitParsed, List<String> questionList, IUser author) {
        String question = splitParsed.get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .withDesc(buildPollOptions(questionList))
                .withColor(vis.getRandVibrantColour())
                .withAuthorName(question)
                .withTimestamp(System.currentTimeMillis());


        channel.sendMessage("**" + author.getNicknameForGuild(channel.getGuild()) + " asks:" + "**");
        IMessage embedMessage = RequestBuffer.request(() -> channel.sendMessage(eb.build())).get();

        reactAllEmojis(embedMessage,regionalIndicators.subList(0, questionList.size()));
    }

    private String buildPollOptions(List<String> questionList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < questionList.size(); i++) {
            sb.append("regional_indicator_" + getCharFromInt(i) + ":" + questionList.get(i) + "\n");
        }
        return sb.toString();
    }

    private String getCharFromInt(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'a')) : null; //super fucking janky
    }

    private String printStringList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(", ");
        }
        return sb.toString();
    }

    private String getRandomFromStringArray(String[] stringArray) {
        return stringArray[new Random().nextInt(stringArray.length)];
    }

    private void handleUnitConversion(String parsedMsg, IChannel iChannel) {
        try {
            String[] splitString = parsedMsg.split(", ");
            String unitString = splitString[0].replaceAll("^[\\s.\\d]+", "");
            String valueString = splitString[0].replaceAll("[^0-9]+", "");
            String toUnit = splitString[1];
            double value = Double.valueOf(valueString);

            if(distanceUnits.contains(unitString)) {
                double convertedValue = unitConverter.convertDistance(unitString, toUnit, value);
                iChannel.sendMessage("```" + valueString + unitString + " ➡ " + convertedValue + toUnit + "```");
            } else if (massUnits.contains(unitString)) {
                double convertedValue = unitConverter.convertWeight(unitString, toUnit, value);
                iChannel.sendMessage("```" + valueString + unitString + " ➡ " + convertedValue + toUnit + "```");
            } else if (volumeUnits.contains(unitString)) {
                double convertedValue = unitConverter.convertVolume(unitString, toUnit, value);
                iChannel.sendMessage("```" + valueString + unitString + " ➡ " + convertedValue + toUnit + "```");
            }
        } catch (Exception e) {
            iChannel.sendMessage("Malformed Message. Try again and stop being stupid.");
        }
    }

    //Druddigon's Den
    private void druddigonEvent(IUser author, String message, IChannel channel, IGuild guild, IMessage iMessage, MessageReceivedEvent event) {
        List<IEmoji> emojisFromGuild = guild.getEmojis();


        IEmoji emojiInMessage = messageContainsEmojiName(message, emojisFromGuild);
        if (emojiInMessage != null) {
            iMessage.addReaction(emojiInMessage);
        }

        if (channel.getStringID().equals("363537947452243969")) { //fortnite-win-archives;
            drudHandleWins(channel, iMessage);
        } else if (author.getStringID().equals("147566214678446080")) { //kittenpoof
            if (new Random().nextDouble() > .75) {
                List<ReactionEmoji> emojis = new ArrayList<>();
                emojis.add(ReactionEmoji.of("VoHiYo", 407229687924916224L));
                emojis.add(ReactionEmoji.of("monkaS", 388556783536439298L));
                emojis.add(ReactionEmoji.of("Deuueaugh", 426121157381718048L));
                reactAllEmojis(iMessage, emojis);
            }
        }
    }

    private IEmoji messageContainsEmojiName(String message, List<IEmoji> emojis) {
        for (IEmoji e : emojis) {
            if (message.contains(e.getName()))
                return e;
        }
        return null;
    }

    private void drudHandleWins(IChannel channel, IMessage iMessage) {
        List<ReactionEmoji> emojis = new ArrayList<>();
        emojis.add(ReactionEmoji.of("Deuueaugh", 426121157381718048L));

        if (iMessage.getFormattedContent().contains("gyazo.com")) {
            reactAllEmojis(iMessage, emojis);
        } else {

            Iterator i = iMessage.getAttachments().iterator();

            for (; i.hasNext(); ) {
                Attachment a = (Attachment) i.next();
                if (isAPicture(a, iMessage)) {
                    reactAllEmojis(iMessage, emojis);
                }
            }
        }

        if(new Random().nextFloat() > .95f) { //dont send everytime
            channel.sendMessage("*Yay! :3*");
        }
    }

    private void mooshroomEvent(IUser author, String message, IChannel iChannel, IGuild mooshroomGuild, IMessage iMessage, MessageReceivedEvent event) {
        //mandatory UnNuke check, otherwise will never trigger
        if (message.toLowerCase().equals("!unnuke")) {
            handleUnNuke(author, iChannel);
            return;
        }
        //check if this iChannel is nuked. if it is, runDelete the message immediately
        for (ChatRestriction cr: masterChatRestrictionManager.getChatRestrictionList()) {
            if (cr.getRestrictedChannel().equals(iChannel)) { //find the ChatRestriction Obj
                if (cr.isMuted()) { //check of the ChatRestriction Obj is actually muted
                    iMessage.delete();

                    if(cr.getHasWarnedChat()) {
                        return; //don't bother running anything else
                    }
                    else {
                        RequestBuffer.request(() -> {
                            iChannel.sendMessage("This iChannel is currently nuked. This means:\n```ALL: \nMESSAGES\nIMAGES\nFILES```\nwill be instantly nuked (deleted) Currently, only Kat and Bongoes have permission to unNuke this iChannel.");
                        });
                        cr.setHasWarnedChatTrue();
                    }
                    return;
                }
            }
        }

        //bannedUsers.add("292170798687453204"); //alogic
        if (mooshroomCheckForCuteness(iChannel, message, author, iMessage))
            return;


        if (iChannel.getStringID().equals("293883245244973057")) { //graveyard iChannel
            mooshroomHandleGraveyard(iChannel, iMessage);
            Aspect.LOG.info("triggered graveyard");
            return; //dont bother executing anything else if it was the graveyard iChannel
        }

        if (iChannel.getStringID().equals("293883187141148682")) {
            mooshroomHandleWhitebag(iChannel, iMessage);
            return; //might add more sendMsg later
        }

        //handle specific people
        mooshroomHandlePeople(iChannel, author);

        handleCatText(iMessage);



        //handle commands for mooshroom
        if ("!server".equals(message) || message.equals("!homebase")) {
            //iChannel.send("We're currently playing on: \t\tUS West 1 \nCome join us!");
            iChannel.sendMessage("--------------\n|  US West  |\n--------------");

        } else if ("!katinv".equals(message)) {//export imgur from muledump
            iChannel.sendMessage("Note: not everything is for sale!\nAsk <@264213620026638336> for details!");

            //read from file first, if file is empty/not there, just return default error url
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(katInvFilePath));
                katInvImgur = br.readLine(); //should only take first and only line in txt file.
            } catch (IOException e) {
                Aspect.LOG.info("KatInv Url Read Error 1");
            } finally {
                iChannel.sendMessage(katInvImgur); //send this no matter what
            }

        } else if (message.startsWith("!setinv")) {
            if (author.getStringID().equals("264213620026638336")) {
                String parsedMessage = message.substring(8);
                if (parsedMessage.startsWith("https://i.imgur.com/")) {
                    katInvImgur = parsedMessage;
                } else {
                    katInvImgur = "https://i.imgur.com/" + parsedMessage;
                }
                iChannel.sendMessage("Imgur Updated :)");
            } else { //not me who sets it, start bitching at them
                iChannel.sendMessage("<@" + author.getStringID() + ">, don't go around trying to change Kat's inventory!\n I'm going to tell on you!");
                iChannel.sendMessage("<@264213620026638336>, <@" + author.getStringID() + "> tried to change your inventory! :3");
            }
            readerWriter.writeToFile(katInvFilePath, katInvImgur, false, false);
            Aspect.LOG.info("Inventory Updated");

        } else if ("!updateinv".equals(message)) {
            iChannel.sendMessage("<@264213620026638336> UPDATE YOUR IMGUR YOU BAKA ;-;  !!!");
            Aspect.LOG.info("Inventory successfully updated by Katarina");
        } else if ("!judge".equals(message)) {
            iChannel.sendMessage("get good.");

            //still broke :(
            //MessageHistory messageHistory = iChannel.getMessageHistory();
            //IMessage latestMsg = messageHistory.getLatestMessage();
            //latestMsg.addReaction(ReactionEmoji.of(":cat:", 436361966219558932L));

        } else if ("!time".equals(message)) {
            iChannel.sendMessage("In Vancouver, it is currently " + LocalTime.now().toString().substring(0,5));
        }
        //music stuff
        else if ("!radioplay".equals(message)) {
            for (String s : musicURLs) {
                //iChannel.send(";;play " + s);
                iChannel.sendMessage("Deprecated");
            }
        } else if (message.equals("!radioskip")) {
            iChannel.sendMessage(";;skip <@417925383762214912>");
        } else if (message.equals("!radiolist")) {
            for (String s : musicURLs) {
                iChannel.sendMessage(s);
            }
        } else if (message.equals("!anthem")) {
            iChannel.sendMessage("https://www.youtube.com/watch?v=dQw4w9WgXcQ"); //todo fix this fucking link LOL

            try {
                //sleep for 3 seconds *DRAMATIC EFFECT INTENSIFIES*
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ignore everything else because I don't know how to handle it :3
            }
            iChannel.sendMessage("jk. Here's my anthem \n\n https://www.youtube.com/watch?v=xlSmsKJq3CI " +
                    "\nhttps://www.youtube.com/watch?v=JprsKeAStcw");
        } else if (message.equals("!scram")) { //TODO
            List<IUser> allUsers = new ArrayList<>();
            List<IUser> audioUsers = new ArrayList<>();
        } else if (message.equals("!shell")) {
            if (elevatedUserStringIDs.contains(author.getStringID())) { //is elevated
                shellRestricted = !shellRestricted;
                iChannel.sendMessage("<@145338465783906304> has set user <@145586743972855808>'s mute status to: " + shellRestricted);
            } else { //caller is not bongoes -- might change message later
                iChannel.sendMessage("only Bongoes has access to this command. Go annoy him if you want perms");
            }
        } else if (message.equals("!shellStatus")) {
            iChannel.sendMessage("User Shell </145586743972855808> is currently " + ((shellRestricted) ? "" : "not") + " muted.");
        } else if (message.equals("!nuke")) { //todo
            if (elevatedUserStringIDs.contains(author.getStringID())) { //me or bongoes
                ChatRestriction chatRestriction = new ChatRestriction(iChannel, true, author);
                masterChatRestrictionManager.addChatRestriction(chatRestriction);
                iChannel.sendMessage("```THIS CHANNEL IS CURRENTLY UNDER GENERAL MUTE```");

                //log event in txt file
                readerWriter.writeToFile("C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\nukehistory.txt", author.getName() + " EventID: " + author.getStringID() + " Nuked the " + iChannel.getName() + " iChannel in the " + iChannel.getGuild().getName() + " server on: " + new Date().toString(), true, true);

                Aspect.LOG.info("Logged Nuke");
                return;
            } else { //not me or bongoes
                iChannel.sendMessage("Only elevated users have access.");
                return;
            }
        } else if (message.startsWith("!newDeath")) { //testing
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"); //might get rid of YEAR later

            String playerName;
            String playerClass;
            String playerIGN;
            int playerOutOfEight;

            try {
                String withoutCommandPrefix = message.substring(9); //might need to fix here
                Aspect.LOG.info("withoutCommandPrefix: " + withoutCommandPrefix);
                String[] parsedInput = withoutCommandPrefix.split("\\s*,\\s*");
                playerName = parsedInput[0];
                playerIGN = parsedInput[1];
                playerClass = parsedInput[2];
                playerOutOfEight =  Integer.parseInt(parsedInput[3]);


                //check if current call is overwriting anything
                if(latestDeath == null){
                    iChannel.sendMessage("This is the first death since I rebooted :3");
                    latestDeath = new RotmgDeath(playerName, playerIGN, playerClass, playerOutOfEight, new Date());
                    iChannel.sendMessage("The latest death has been successfully updated!");
                } else {
                    iChannel.sendMessage("You are overwriting the previous death entry, which is:");
                    iChannel.sendMessage(latestDeath.toString());
                    latestDeath = new RotmgDeath(playerName, playerIGN, playerClass, playerOutOfEight, new Date());
                    iChannel.sendMessage("The latest death has been successfully updated!");
                }

            } catch (Exception e){
                iChannel.sendMessage("Please use ```!deathFormat``` to check the required format!");
            }
        } else if (message.equals("!latestDeath")) {
            iChannel.sendMessage(latestDeath.toString());
        }  else if (message.equals("!helpDeath")) {
            iChannel.sendMessage("the message needs to be formatted as follows: ```!newDeath playerName, playerIGN, playerClass, x/8```");
            iChannel.sendMessage("Example of a full message: \n```\"!newDeath Kat, SexySelfie, Archer, 6\"```");
        } else if (message.toLowerCase().startsWith("!readrec")) { //might need to make better //todo
            String pRecord = readerWriter.readFromFile("C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\punishmentRecord.txt");
            iChannel.sendMessage(pRecord);
        } else if (message.toLowerCase().startsWith("!rate")) { //todo
            String cutInput = message.substring(6);
            String url = "http://www.tiffit.net/RealmInfo/api/user?u="+ cutInput + "&f=";
            String json = getJson(url);
            handleRealmEyeRanking(json, iChannel, author);
        } else if (message.toLowerCase().startsWith("!pets")) {
            String cutInput = message.substring(6);
            String url = "http://www.tiffit.net/RealmInfo/api/pets-of?u=" + cutInput;
            String json = getJson(url);
            handleRealmEyePets(json, iChannel, author, cutInput, url);
        } else if (message.toLowerCase().startsWith("!recentchar")) {
            long startTime = System.currentTimeMillis();
            String cutInput = message.substring(12);
            String url = "http://www.tiffit.net/RealmInfo/api/user?u="+ cutInput + "&f=";
            String json = getJson(url);
            handleRealmEyeRecentChar(json, iChannel, author, cutInput, url, startTime);
        } else if (message.toLowerCase().startsWith("!totalscore")) {
            double startTime = System.currentTimeMillis();
            String cutInput = message.substring(12);

            String url = "http://www.tiffit.net/RealmInfo/api/user?u="+ cutInput + "&f=";
            String json = getJson(url);
            handleRealmEyeTotalCharScore(json, iChannel, author, cutInput, url, startTime);
            Aspect.LOG.info(author.getName() + " triggered total score on " + cutInput + " at " + LocalDateTime.now().format(timeFormatter));

        } else if (message.toLowerCase().startsWith("!redesc")) {
            String cutInput = message.substring(8);
            String url = "http://www.tiffit.net/RealmInfo/api/user?u="+ cutInput + "&f=";
            String json = getJson(url);
            handleRealmEyeDesc(iChannel, json, cutInput);
        } else if (message.toLowerCase().startsWith("!guildinfo")) {
            long startTime = System.currentTimeMillis();
            String cutInput = message.substring(11);
            String cutInputWithSpaces = cutInput.replaceAll(" ", "%20");

            String url = "http://www.tiffit.net/RealmInfo/api/guild?g=" + cutInputWithSpaces + "&f=";
            String json = getJson(url);
            handleRealmEyeGuildHighlight(iChannel, json, mooshroomGuild, url, startTime);

        } else if (message.toLowerCase().equals("!match")) {
            String url = "http://www.tiffit.net/RealmInfo/api/guild?g=Mooshroom&f=";
            String json = getJson(url);
            handleRealmEyeGuildMemberMatch(iChannel, json, mooshroomGuild);
        } else if (message.toLowerCase().startsWith("!rpoint")){ //!rpoint #userID, #points
            if (elevatedUserStringIDs.contains(author.getStringID())) {
                String parsedInput = message.split(" ", 2)[1];
                String userIDString = parsedInput.split(", ")[0];
                String numPoints = parsedInput.split(", ")[1];
                String recipientNick = client.getUserByID(Long.valueOf(userIDString)).getNicknameForGuild(mooshroomGuild);
                String messageToWrite = author.getName() + ", " + author.getStringID() + ", " + recipientNick + ", " + userIDString + ", " + numPoints; //todo
                readerWriter.writeToFile(mooshroomPunishRecord, messageToWrite, true, true);
                iChannel.sendMessage(author.getNicknameForGuild(mooshroomGuild) + " has given " + recipientNick + " " + numPoints + " retard points.");
            } else {
                iChannel.sendMessage("You don't have elevated permissions.");
            }
        } else if (message.toLowerCase().startsWith("!rcheck")) {
            String userID = message.split(" ", 2)[1];
            String userNick = client.getUserByID(Long.valueOf(userID)).getNicknameForGuild(mooshroomGuild);
            try {
                String stringPoints = handleMooshroomReadRPoints(userID);
                int numPoints = Integer.valueOf(stringPoints);
                int moddedPoints = Math.floorMod(numPoints, 90);
                iChannel.sendMessage( userNick + " is a Prestige " + Math.floorDiv(numPoints, 90) +
                        " level " + Math.floorDiv(moddedPoints, 5) +
                        " retard with " + /*getRetardLevel(moddedPoints)*/ Math.floorDiv(moddedPoints, 5) + " points.", true); //tts works!
            } catch (Exception e) {
                iChannel.sendMessage(userNick + " has no retard points.");
            }
        }


        // not commands
        else if(message.matches(".*(hello)+.*")) {
            iChannel.sendMessage("Hello! :3");
        } else if (message.matches(".*(kms)+.*")) {
            iChannel.sendMessage("*noose is downstairs*");
        } else if (message.matches(".*(\\skys\\s)+.*")) {
            iChannel.sendMessage("Hey! Be nice to each other.");
        } else if (message.contains(">.<")) {
            author.getOrCreatePMChannel().sendMessage("!!! Are you a cutie too?? >.>");
        } else if (message.matches(".*\\s*(m+e+o+w+)+\\s*.*")) {
            //iChannel.send(":3 h~hello!");
        }


        randomReplyEvent(iChannel, mooshroomGuild, author);
    }

    private String handleMooshroomReadRPoints(String userID) {
        List<String> fullRecord = readerWriter.readFromFileToStringList(mooshroomPunishRecord);
        int totalPoints = 0;

        for (String s : fullRecord) {
            String[] parsed = s.split(", ");
            if (parsed[3].equals(userID)) {
                totalPoints += Integer.valueOf(parsed[4]);
            }
        }

        return String.valueOf(totalPoints);
    }

    private void handleRealmEyeGuildHighlight(IChannel channel, String json, IGuild guild, String url, long startTime) {
        RealmEyeGuild realmGuild = new Gson().fromJson(json, RealmEyeGuild.class);

        String founder = "";
        try {
            founder = realmGuild.getMembers().get(0).getName();
        } catch (NullPointerException e) {
            channel.sendMessage("Error: there is no founder in " + realmGuild.getName());
            return;
        }

        EmbedBuilder eb = vis.getEmbedBuilderNoField(founder + ", Founder","https://realmeye.com/player/"+ founder,"desc",vis.getRandVibrantColour(),System.currentTimeMillis(),
                "", realmGuild.getName(), "https://www.realmeye.com/s/c7/img/eye-big.png",
                "https://realmeye.com/guild/"+ (realmGuild.getName().contains(" ") ? realmGuild.getName().replaceAll(" ", "%20") : realmGuild.getName()));

        for (RealmEyeGuildMember member: realmGuild.getMembers()) {
            if (!member.getName().equals("Private"))
                if (member.getGuild_rank().equals("Leader")) {
                    eb.appendField(member.getName() + ", *Leader*", "content ph", false);
                }
        }
        channel.sendMessage(eb.withFooterText("This operation took me " + String.valueOf((System.currentTimeMillis() - startTime)) + "ms to compute :3").build());
        Aspect.LOG.info("triggered guildinfo highlight");
    }

    private void handleRealmEyeGuildMemberMatch(IChannel channel, String json, IGuild mooshroomDiscord) {
        RealmEyeGuild mooshroomGuild = new Gson().fromJson(json, RealmEyeGuild.class);

        List<String> guildNames = new ArrayList<>(); //setting initial size probably not useful
        for (IUser discMem : mooshroomDiscord.getUsers())
            if(discMem.getNicknameForGuild(mooshroomDiscord) != null)
                guildNames.add(discMem.getNicknameForGuild(mooshroomDiscord).split("[)]")[0].substring(1));


        List<String> rotmgIGNs = mooshroomGuild.getNames();

        rotmgIGNs.removeAll(guildNames);
        Math.random();

        StringBuilder sb = new StringBuilder().append("The following is a list of people in the guild, but not in Discord:\n```");

        for (int i = 0; i < rotmgIGNs.size(); i++)
            sb.append((i+1)+". ").append(rotmgIGNs.get(i)).append(", ");

        sb.append("```");
        channel.sendMessage(sb.toString());
        Aspect.LOG.info("Triggered member matching");
    }

    private void handleRealmEyeTotalCharScore(String json, IChannel channel, IUser author, String cutInput, String url, double startTime) {
        RealmEyePlayer player = new Gson().fromJson(json, RealmEyePlayer.class);
        EmbedBuilder embed = player.charsToEmbed(author.getAvatarURL());

        RequestBuffer.request(() -> channel.sendMessage(embed.withFooterText("This operation took me " + (System.currentTimeMillis() - startTime) + "ms to compute :3").build()));
    }

    private void handleRealmEyeDesc(IChannel channel, String json, String playerIGN) {
        RealmEyePlayer player = new Gson().fromJson(json, RealmEyePlayer.class);

        StringBuilder sb = new StringBuilder();
        sb.append(playerIGN.substring(0,1).toUpperCase()+playerIGN.substring(1).toLowerCase() + "'s RealmEye Description:\n");
        sb.append("```");
        for (String s : player.getDescription()) {
            sb.append(s).append("\n");
        }
        sb.append("```");
        channel.sendMessage(sb.toString());
    }

    /**
     * Just use other getAllItemJson from BotUtils. Too lazy to refactor everything over
     *
     * @param jsonURL
     * @return
     */
    private String getJson(String jsonURL) {
        return BotUtils.getJson(jsonURL);
    }
    private String getJson(String jsonURL, boolean isFortnite) {
        try {
            String userJson;
            URL url = new URL(jsonURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); //parameter?
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET"); //might make parameter later
            connection.setRequestProperty("X-Key", "3s9jyXTejPqotuKRkhAv");
            connection.setRequestProperty("User-Agent", "java request"); //no idea

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

    private void handleRealmEyeRanking(String userJson, IChannel iChannel, IUser author) {
        RealmEyePlayer player = new Gson().fromJson(userJson, RealmEyePlayer.class);
        iChannel.sendMessage(player.getName() + " has a total of " + player.getTotalOutOf8() + " maxed stats over " + player.getNumChars() + " characters.");
    }
    private void handleRealmEyePets(String userJson, IChannel iChannel, IUser author, String playerIGN, String realmEyeUrl) {
        RealmEyePets pets = new Gson().fromJson(userJson, RealmEyePets.class);
        Pet highestLevelPet = pets.findHighestLevelPet();
        highestLevelPet.toEmbed(iChannel, author, playerIGN, realmEyeUrl); //can throw NPE
    }
    private void handleRealmEyeRecentChar(String userJson, IChannel iChannel, IUser author, String playerIGN, String realmEyeUrl, long startTime) {
        RealmEyePlayer player = new Gson().fromJson(userJson, RealmEyePlayer.class);
        Character mostRecentChar = player.getCharacters().get(0); //gets first (most recent character)
        mostRecentChar.toEmbed(iChannel, author, playerIGN, realmEyeUrl, startTime);
    }


    private void handleUnNuke(IUser author, IChannel channel) {
        if (elevatedUserStringIDs.contains(author.getStringID())) { //me or bongoes
            for (ChatRestriction cr: masterChatRestrictionManager.getChatRestrictionList()) {
                if (cr.getRestrictedChannel().equals(channel)) {
                    cr.setNotMuted(); //unmute it
                    RequestBuffer.request(() -> {
                        channel.sendMessage("```THIS CHANNEL HAS BEEN UNMUTED```");
                    });

                    //log to file
                    readerWriter.writeToFile("C:\\Users\\Positron\\IdeaProjects\\KittyKatDiscordBot\\txtfiles\\Mooshroom\\nukehistory.txt", author.getName() + " EventID: " + author.getStringID() + " unNuked the " + channel.getName() + " channel in the " + channel.getGuild().getName() + " server on: " + new Date().toString(), true, true);

                    Aspect.LOG.info("triggered unnuke logging");
                    return;
                }
            }
        } else {
            channel.sendMessage("Only Kat and Bongoes have access.");
        }
    }

    private void handleLeaveVoice(MessageReceivedEvent event) {
        IVoiceChannel botVoiceChannel = client.getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if(botVoiceChannel == null)
            return;

        botVoiceChannel.leave();
    }

    private void handleJoinVoice(MessageReceivedEvent event) {
        IVoiceChannel userVoiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

        if(userVoiceChannel == null)
            return;

        userVoiceChannel.join();
    }

    private void handleAudioWithMessage(MessageReceivedEvent event, String displayMessage, String filePath) {
        IUser user = event.getAuthor();
        IVoiceChannel voiceChannel = user.getVoiceStateForGuild(event.getGuild()).getChannel();
        if (voiceChannel == null)
            handleJoinVoice(event);

        AudioPlayer audioPlayer = AudioPlayer.getAudioPlayerForGuild(event.getGuild());
        File musicFile = new File(filePath);
        IMessage displayIMessage = null;
        try {
            audioPlayer.queue(musicFile);
            displayIMessage = event.getChannel().sendMessage("Now playing: " + displayMessage);
        } catch (IOException e) {
            user.getOrCreatePMChannel().sendMessage("IO exception. Tell Kat!");
        } catch (UnsupportedAudioFileException e) {
            user.getOrCreatePMChannel().sendMessage("UnsupportedAudioFileException. Tell Kat!");
        }
        try {
            Thread.sleep(1000*60*35);
            displayIMessage.delete();
        } catch (Exception e) { //also catches null pointer
            Aspect.LOG.info("Error in HandleAudioWithMessage");
        }
    }

    /**@param author sender of message
     * @param channel channel which message was sent
     * @param message String of the message
     * @param iMessage iMessage object to check
     */
    private boolean mooshroomCheckForCuteness(IChannel channel, String message, IUser author, IMessage iMessage) {
        //whitelist for hardcoding first
        if(message.toLowerCase().contains("you would")) {
            return false;
        }

        if (shellRestricted && author.getStringID().equals("145586743972855808") && (!message.toLowerCase().matches("^[a-zA-Z0-9_\\s]*$"))) { //does not match "only letters, numbers, whtspace, underscore"
            iMessage.delete();
            if (new Random().nextFloat() > .8) {
                author.getOrCreatePMChannel().sendMessage("*tsk tsk tsk*\n*finger wag~*");
            }
            return true;
        }

        Pattern cuteowo = Pattern.compile("(?i).*([o0U*]+[\\s]*[wv]+[\\s]*[*U0o]+)+.*");
        Matcher cuteMatcher = cuteowo.matcher(message);
        if (cuteMatcher.find()) {
            iMessage.delete();
            if(new Random().nextFloat() > .75) {
                author.getOrCreatePMChannel().sendMessage("STOP OWOWOWOWWOWing in chat!!!");
            }
            return true;
        }
        return false;
    }

    private void mooshroomHandleWhitebag(IChannel channel, IMessage iMessage) {
        ReactionEmoji whiteBag = ReactionEmoji.of("white", 433068382871617537L);

        //if it is an image, add white bag reaction
        if (iMessage.getFormattedContent().contains("gyazo.com")) {
            iMessage.addReaction(whiteBag);
            RequestBuffer.request(() -> {
                iMessage.addReaction(whiteBag);
            });
            return; //dont do second check
        }

        Iterator i = iMessage.getAttachments().iterator();

        for (; i.hasNext(); ) {
            Attachment a = (Attachment) i.next();
            if (isAPicture(a, iMessage)) {
                iMessage.addReaction(whiteBag);
                RequestBuffer.request(() -> {
                    iMessage.addReaction(whiteBag);
                });
                break; //prevents multiple reactions for messages with multiple pictures
            }
        }
    }

    private void handleCatText(IMessage iMessage) {
        String lcMsg = iMessage.getFormattedContent().toLowerCase();
        ReactionEmoji catEmoji = ReactionEmoji.of("\u1F431");
        if (lcMsg.contains("kat") || lcMsg.contains(" cat ") || lcMsg.contains("kitty")) {
            RequestBuffer.request(() -> {
                iMessage.addReaction(catEmoji);
            });
        }
    }


    private EmbedBuilder createKittyKatBotEmbed() {
        EmbedBuilder temp = new EmbedBuilder();
        temp.withColor(248, 6, 243);
        temp.withThumbnail("https://cdnb.artstation.com/p/assets/images/images/005/255/059/large/m_-valentinus-222222.jpg?1489667163");

        //copied from github dude
        temp.withTitle("This is Kitty Kat reporting in!")
                .withUrl("https://discordapp.com")
                .withDesc("Insert [named links](https://discordapp.com) here later! ```\nGotta remind Kat to put my !help here...```")
                .withColor(new Color(16254707))
                //.withColor(new Visuals().getRandVibrantColour())
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("Absolutely Kat~")
                .withFooterIcon("https://cdn.discordapp.com/embed/avatars/0.png")
                .withThumbnail("https://cdnb.artstation.com/p/assets/images/images/005/255/059/large/m_-valentinus-222222.jpg?1489667163")
                .withImage("http://www.lol-wallpapers.com/wp-content/uploads/2017/10/Death-Sworn-Katarina-Splash-Art-HD-Wallpaper-Background-Official-Art-Artwork-League-of-Legends-lol-1.jpg")
                .withAuthorName("<@417925383762214912>")
                .withAuthorIcon("https://pbs.twimg.com/media/C_9InC1UwAEywjt.jpg")
                .withAuthorUrl("https://discordapp.com")
                .appendField("Meow", "Im a kitty~", false)
                .appendField("Here are two songs I really like!", "(Don't hate me if you don't like them)", false)
                .appendField("Cutie music :3", "[*dancing*](https://www.youtube.com/watch?v=nOLqvFOqXtw)", true)
                .appendField("Also cutie Music!", "[*still dancing*](https://www.youtube.com/watch?v=6q0nTM3ToeM)", true)
                .build(); //not sure if redundant

        return temp;
    }

    private EmbedBuilder createKatEmbed() {
        EmbedBuilder temp = new EmbedBuilder();

        //copied from the github thing
        temp.withTitle("Hi, I'm Kat!")
                .withUrl("https://discordapp.com")
                .withDesc("18 earth year old ambitious lump of organic matter that goes around\n trying to not be useless :)")
                .withColor(new Color(13458387))
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("Look Onwards")
                .withFooterIcon("https://pbs.twimg.com/profile_images/664158849536921600/KZAnmaIr_400x400.jpg")
                .withThumbnail("https://vignette.wikia.nocookie.net/leagueoflegends/images/1/12/PROJECT_Katarina_profileicon.png/revision/latest?cb=20170505005706")
                .withImage("https://i.imgur.com/8kGgNGX.jpg")
                .withAuthorName("KVTVRINV")
                .withAuthorIcon("https://i.imgur.com/echE5EF.jpg")
                .withAuthorUrl("https://google.com")
                .appendField(":cat:", "placeholder 1", false)
                .appendField("comments, suggestions, critique", "contact@email.com", false)
                .appendField("ph", "ph", false)
                .appendField("Schrodinger's", "dead", true)
                .appendField("Cat", "alive", true)
                .build();

        return temp;
    }

    private void randomReplyEvent(IChannel channel, IGuild guild, IUser author) {
        if ((new Random().nextFloat() <= .002f) && (meowable(channel))) {
            //channel.send("Meow :3 (Bongoes tells me to change this but i like meowing)");
            channel.sendMessage("\"MAke iT Say sOME gANgStA sHIt insteaD of mEOw\" - Bongoes 2018");
        }
    }

    private boolean meowable(IChannel channel) {
        return meowable.contains(channel.getStringID());
    }


    private void mooshroomHandlePeople(IChannel channel, IUser author) {
        if (bannedUsers.contains(author.getName())) {
            channel.sendMessage("I don't listen to plebs, " + author.getName() + ".");
            author.getOrCreatePMChannel().sendMessage("No hard feelings though. ~meow~ :3");
        } else if (author.getStringID().equals("292170798687453204")) {
            //channel.send("Shut up. :3");
            //author.getOrCreatePMChannel().send("They made me do it! No hard feelings, okay? :3");
        } else if (author.getStringID().equals("109008117810888704")) { //Umbracus
            if(new Random().nextFloat() > .9f) { //dont send everytime
                author.getOrCreatePMChannel().sendMessage("Get back to work or I'm telling Kat!");
            }
        }
    }

    private void reactAllEmojis(IMessage iMessage, List<ReactionEmoji> emojis) {
        for (ReactionEmoji e : emojis)
            RequestBuffer.request(() -> iMessage.addReaction(e)).get(); //.get() is literally magic
    }


    private void mooshroomHandleGraveyard(IChannel channel, IMessage iMessage) {
        List<ReactionEmoji> emojis = new ArrayList<>();
        emojis.add(ReactionEmoji.of("88", 433072608729104394L));
        emojis.add(ReactionEmoji.of("👼"));
        emojis.add(ReactionEmoji.of("😿"));
        emojis.add(ReactionEmoji.of("🇫"));

        if(new Random().nextFloat() > .85f) { //dont send everytime
            channel.sendMessage("*meows softly*");
        }

        //if it is an image, add 8/8 grave reaction
        if (iMessage.getFormattedContent().contains("gyazo.com")) {
            reactAllEmojis(iMessage, emojis);
            return; //don't runDelete the 2nd check
        }

        Iterator i = iMessage.getAttachments().iterator();
        for (; i.hasNext(); ) {
            Attachment a = (Attachment) i.next();
            if (isAPicture(a, iMessage)) {
                reactAllEmojis(iMessage, emojis);
            }
        }
    }

    private boolean isAPicture(Attachment a, IMessage iMessage) {
        return a.getUrl().endsWith(".png") || a.getUrl().endsWith(".jpeg") || a.getUrl().equals(".jpg") || iMessage.getFormattedContent().toLowerCase().contains("gyazo");
    }

    private void handleHelpMessage(IUser author, IChannel channel) {
        channel.sendMessage("<@" +author.getStringID() + ">, Kitty Kat documentation is on the way!");
        IChannel pm = author.getOrCreatePMChannel();

        //I actually need to figure this out lol the bot is getting too big
    }

    private void handleHelpCommands(IChannel channel, IChannel pm) { //completely ass
        String s = "\t";
        StringBuilder message = new StringBuilder();
        message.append("All commands start with an \"!\"");
        message.append("!pet");
        message.append("\tpet me! :3");
        message.append("!love");
        message.append(s).append("determines how much love kitty will give you");
        if(channel.getGuild().getName().equals("Mooshroom")) {
            message.append("!server or !homebase");
            message.append(s + "where Mooshroom is located");
        }
        message.append("!dev");
        message.append("\tfetches my mommy..... MOM SOMEONE WANTS TO SPEAK TO YOU!");
        message.append("!katinv");
        message.append(s+"want something from kat? Check if its on one of her mules first *before* spamming the shit outta her :)");
        message.append("!updateinv");
        message.append(s+"yells at my mom to update her muledump imgur...");
        message.append("!judge");
        message.append(s+"reflects my cynical outlook in life.");
        message.append("!friendly");
        message.append(s+"adds you to the friendly list. Cannot be used more than once by the same person. Resets everytime bot restarts.");
        message.append("!listfriendly");
        message.append(s+"lists all the friendly people (people who have used !friendly)");
        message.append("!radioplay");
        message.append(s+"deprecated atm");
        message.append("!anthem");
        message.append(s+"I'll tell you all about my <@264213620026638336>'s favorite song!");
        message.append("!time");
        message.append(s+ "Time here, in my comfy home.");
        pm.sendMessage(message.toString());
    }

    private void handleXKCD(IChannel channel) {
        int max = 2018; //TODO: use date to calcuate what this should be
        int roll = new Random().nextInt(max);
        channel.sendMessage("https://xkcd.com/" + roll + "/");
    }
}