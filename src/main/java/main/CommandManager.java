package main;

import com.google.gson.reflect.TypeToken;
import main.commands.dontopendeadinside.Greet;
import main.commands.dontopendeadinside.Summarize;
import main.commands.dontopendeadinside.UserWordFrequency;
import main.commands.dontopendeadinside.WordCounter;
import main.commands.dontopendeadinside.games.CoinFlip;
import main.commands.dontopendeadinside.games.Minesweeper;
import main.commands.dontopendeadinside.games.RollDice;
import main.commands.dontopendeadinside.games.WouldYourRather;
import main.commands.dontopendeadinside.imaging.Haruwu;
import main.commands.dontopendeadinside.imaging.Imaging;
import main.commands.dontopendeadinside.imaging.Render;
import main.commands.dontopendeadinside.imaging.Tex;
import main.commands.dontopendeadinside.imaging.ascii.AsciifyOld;
import main.commands.dontopendeadinside.imaging.ascii.Asciimg;
import main.commands.dontopendeadinside.imaging.deepAI.Colourer;
import main.commands.dontopendeadinside.imaging.deepAI.DeepDream;
import main.commands.dontopendeadinside.imaging.deepAI.DemographicRecog;
import main.commands.dontopendeadinside.imaging.deepAI.Waifu2x;
import main.commands.electronicArts.ApexLegendsPlayerStats;
import main.commands.fortnite.FortniteStats;
import main.commands.humor.AnalyzeUser;
import main.commands.humor.Insult;
import main.commands.humor.Ship;
import main.commands.humor.SpongeBobify;
import main.commands.humor.cute.CatPics;
import main.commands.humor.cute.CuteImg;
import main.commands.humor.cute.ListCuties;
import main.commands.league.*;
import main.commands.mooshroom.RPoints;
import main.commands.music.CustomQueues;
import main.commands.music.MusicEqualizer;
import main.commands.music.playing.*;
import main.commands.music.queue.*;
import main.commands.music.sfx.SoundEffect;
import main.commands.music.sfx.SoundEffectList;
import main.commands.nasa.BlueMarble;
import main.commands.nasa.NasaApod;
import main.commands.outdoors.EventsNearLocation;
import main.commands.overwatch.OverwatchStats;
import main.commands.pokemon.PokemonIdentifier;
import main.commands.rotmg.*;
import main.commands.utilitycommands.*;
import main.commands.utilitycommands.channel.BulkDelete;
import main.commands.utilitycommands.channel.DeleteWaifus;
import main.commands.utilitycommands.channel.WordReaction;
import main.commands.utilitycommands.guild.DumpEmotes;
import main.commands.utilitycommands.guild.UsersToGist;
import main.commands.utilitycommands.guildutil.*;
import main.commands.utilitycommands.metautil.*;
import main.commands.warframe.*;
import main.commands.webquery.UrbanDictionary;
import main.commands.webquery.Wikipedia;
import main.commands.wolfram.WolframAdvanced;
import main.commands.wolfram.WolframBasic;
import main.utility.metautil.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandManager {
    private long readyTime = System.currentTimeMillis();

    public static ExecutorService commandExecutors = Executors.newCachedThreadPool();

    public static Map<String, Command> commandMap = new LinkedHashMap<>();

    public static Map<String, ExecutorService> syncExecuteMap = new HashMap<>();

    public static Map<String, String> prefixMap = BotUtils.gson.fromJson(
            BotUtils.readFromFileToString(System.getProperty("user.dir") + "/data/prefix_map.json"),
            new TypeToken<Map<String, String>>() {
            }.getType());

    private static Map<String, Integer> cmdFreqMap = BotUtils.gson.fromJson(
            BotUtils.readFromFileToString(System.getProperty("user.dir") + "/data/cmd_freq_map.json"),
            new TypeToken<Map<String, Integer>>() {
            }.getType());


    //talked to hec about using a static initializer but constructor is fine
    public CommandManager() {

        //Actual Testing
        //commandMap.put("bulba", new BulbapediaScraper());
        //commandMap.put("transpose", new ImageTransposer());
        //commandMap.put("trim", new TransparentTrimmer());
        //commandMap.put("nick", new ReNickName()); //for kaitlyn

        //webquery
        UrbanDictionary urbanDictionary = new UrbanDictionary();
        commandMap.put("dic", urbanDictionary);
        commandMap.put("def", urbanDictionary);
        commandMap.put("wiki", new Wikipedia()); // :)
        commandMap.put("dictfilter", new DictionaryFilter());


        //dontdeadopeninside
        commandMap.put("ascii2", new AsciifyOld());
        commandMap.put("ascii", new Asciimg());
        commandMap.put("render", new Render());
        commandMap.put("tex", new Tex());
        commandMap.put("help", new Help());
        commandMap.put("summarize", new Summarize());
        commandMap.put("img", new Imaging()); // showcase?
        commandMap.put("identify", new PokemonIdentifier()); // showcase?
        commandMap.put("roll", new RollDice());
        commandMap.put("flip", new CoinFlip());
        commandMap.put("wyr", new WouldYourRather());

        // Deep AI
        Colourer color = new Colourer();
        commandMap.put("upscale", new Waifu2x());
        commandMap.put("face", new DemographicRecog());
        commandMap.put("color", color);
        commandMap.put("colour", color);
        commandMap.put("deep", new DeepDream());

        // misc features
        commandMap.put("events", new EventsNearLocation());
        commandMap.put("greet", new Greet());
        commandMap.put("sweep", new Minesweeper());

        //humor
        commandMap.put("cute", new CuteImg());
        commandMap.put("listcute", new ListCuties());
        commandMap.put("ship", new Ship());
        commandMap.put("insult", new Insult());
        commandMap.put("count", new WordCounter());
        commandMap.put("freq", new UserWordFrequency());
        commandMap.put("lyze", new AnalyzeUser());
        commandMap.put("bob", new SpongeBobify());
        commandMap.put("uwu", new Haruwu());

        //music -- Showcase
        SongInfo songInfo = new SongInfo();
        SongStop songStop = new SongStop();
        SongDelete songDelete = new SongDelete();
        SongQueue songQueue = new SongQueue();
        SongSkip songSkip = new SongSkip();
        SongPlayPrev songPlayPrev = new SongPlayPrev();
        SongPause songPause = new SongPause();
        commandMap.put("lvoice", songStop);
        commandMap.put("stop", songStop);
        commandMap.put("play", new SongPlay());
        commandMap.put("join", new JoinVoiceChannel());
        commandMap.put("nowplaying", songInfo);
        commandMap.put("np", songInfo);
        commandMap.put("current", songInfo);
        commandMap.put("skip", songSkip);
        commandMap.put(">", songSkip);
        commandMap.put("restart", songPlayPrev);
        commandMap.put("<", songPlayPrev);
        commandMap.put("queue", songQueue);
        commandMap.put("q", songQueue);
        commandMap.put("pq", new SongPastQueue());
        commandMap.put("fq", new FilterQueue());
        commandMap.put("loop", new SongLoop());
        commandMap.put("shuffle", new ShuffleQueue());
        commandMap.put("purge", new PurgeQueue());
        commandMap.put("insert", new SongInsert());
        commandMap.put("pause", songPause);
        commandMap.put("||", songPause);
        commandMap.put("resume", new SongResume());
        commandMap.put("qdel", songDelete);
        commandMap.put("songdel", songDelete);
        commandMap.put("listmusic", new CustomQueues());
        commandMap.put("seek", new SongSeek());

        commandMap.put("sfx", new SoundEffect());
        commandMap.put("listsfx", new SoundEffectList());

        commandMap.put("eq", new MusicEqualizer());

        //Realm of the Mad God
        commandMap.put("katinv", new KatInv());
        commandMap.put("rguild", new RotmgGuildInfo());
        commandMap.put("rpet", new RotmgPets());
        commandMap.put("rrate", new RotmgRatePlayer());
        commandMap.put("rdesc", new RotmgReadDescription());
        commandMap.put("rrecentchar", new RotmgRecentChar());
        commandMap.put("rscore", new RotmgTotalScore());
        commandMap.put("setinv", new SetKatInv());

        //Utility
        commandMap.put("bulkdelete", new BulkDelete());
        commandMap.put("timev", new CurrentTime());
        commandMap.put("deletemsg", new DeleteMsg());
        commandMap.put("trans", new Translate());
        commandMap.put("pfp", new GetUserPfp());
        commandMap.put("serverpfp", new GetGuildPfp());
        commandMap.put("presence", new UpdatePresence());
        commandMap.put("cleanup", new DeleteWaifus());
        commandMap.put("time", new CommandTimer());
        commandMap.put("stats", new Stats());
        commandMap.put("users", new UserDates());
        commandMap.put("share", new ScreenShare());
        commandMap.put("rx", new WordReaction());

        //Fortnite -- Showcase
        commandMap.put("fn", new FortniteStats());
        commandMap.put("fnstats", new main.commands.fortnite.fortnitev3.FortniteStats());
        //commandMap.put("fnshop", new FortniteShopSelect());
        //commandMap.put("fnselect", new FortniteShopDetail());

        // Apex Legends
        commandMap.put("apex", new ApexLegendsPlayerStats());

        //Overwatch
        commandMap.put("ow", new OverwatchStats());

        //meta
        CpuStats cpu = new CpuStats();
        commandMap.put("setprefix", new SetPrefix());
        commandMap.put("ban", new BanUser());
        commandMap.put("ping", new Ping());
        commandMap.put("info", new PersonalEmbed());
        commandMap.put("membercount", new MemberCount());
        commandMap.put("poll", new Poll());
        commandMap.put("cpuload", cpu);
        commandMap.put("cpu", cpu);
        commandMap.put("makerole", new RoleGenerate());
        commandMap.put("shutdown", new ForceShutdown());
        commandMap.put("uptime", new Uptime());
        commandMap.put("write", new Testing());
        commandMap.put("reboot", new Reboot());
        commandMap.put("guilds", new ListGuilds());

        //League of Legends -- Showcase
        commandMap.put("lollevel", new BasicLeague());
        commandMap.put("lolsum", new LeaguePlayerEmbed());
        commandMap.put("lolregions", new AvailableRegions());
        commandMap.put("lolitem", new RandomItem());
        commandMap.put("allskins", new ListSkins());
        commandMap.put("skin", new SkinDetail());
        commandMap.put("lolrecent", new PreviousMatch());
        commandMap.put("lolign", new LeagueIgnCheck());
        commandMap.put("lolquote", new LeagueQuote());
        commandMap.put("loltft", new TftItems());

        //Warframe
        commandMap.put("wfdaily", new WfDailyDeals());
        commandMap.put("wfcetus", new WfCetusCycle());
        commandMap.put("wforb", new WfOrbVallisCycle());
        commandMap.put("wfalerts", new WfAlerts());
        commandMap.put("wfvoid", new WfVoidFissures());
        //commandMap.put("wfitems", new WfItems()); //dev use
        commandMap.put("wfinfo", new WfItemInfo());
        commandMap.put("wfaco", new WfAcolyteTracker());
        commandMap.put("wfbaro", new WfBaro());
        commandMap.put("wfmarket", new WfMarketListing());
        commandMap.put("wfdropinfo", new WfDropInfo());
        commandMap.put("wfrelic", new WfRelicInfo());
        commandMap.put("wfrand", new WfRandomLoadout());
        commandMap.put("wfmod", new WfModInfo());

        // Nasa // Showcase
        commandMap.put("apod", new NasaApod());
        commandMap.put("bluemarble", new BlueMarble());

        // Mooshroom meta
        commandMap.put("rpoints", new RPoints());

        // Wolfram Alpha -- Showcase
        WolframBasic wolframBasic = new WolframBasic();
        commandMap.put("wolfram", wolframBasic);
        commandMap.put("answer", wolframBasic);
        commandMap.put("solve", wolframBasic);
        commandMap.put("_", wolframBasic);

        commandMap.put("__", new WolframAdvanced());

        // Other meta util
        commandMap.put("dumpusers", new UsersToGist());
        commandMap.put("emotes", new DumpEmotes());
        commandMap.put("resetcpu", new ResetCpuHawk());
        commandMap.put("haste", new FileToHaste());

        // other misc
        commandMap.put("cat", new CatPics());
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        event.getClient().changePresence(StatusType.ONLINE, ActivityType.LISTENING, " your commands");
        readyTime = System.currentTimeMillis();
        Aspect.LOG.info("Aspect is online and ready at " + LocalTime.now().toString());
    }

    /**
     * Message parser and command dispatcher
     *
     * @param event evennnnntttttttt
     */
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argArray;
        String commandStr;
        List<String> argsList = new ArrayList<>();

        // first check channel prefix, then guild prefix, then lastly use default
        String prefix = BotUtils.getPrefix(event);
        // If message doesn't start with BOT_PREFIX, check if it tags us
        if (!event.getMessage().getFormattedContent().startsWith(prefix)) {
            if (!event.getMessage().getContent().matches("<@(!)?" + event.getClient().getOurUser().getStringID() + ">.*")) {
                return; // does not start with prefix AND does not start by tagging our user
            } else { // tagged
                argArray = event.getMessage().getContent().substring(21).trim().split(" ", 2);
                if (event.getMessage().getContent().replaceFirst("!", "").substring(21).trim().equals("")) { // if no command args at all, use help command
                    commandStr = "help";
                    BotUtils.send(event.getChannel(), "My prefix here is : " + BotUtils.getPrefix(event));
                } else {
                    commandStr = argArray[0].toLowerCase(); // enforce lowercase in case shift held down after $
                }
            }
        } else { // start with bot prefix
            // Given a message "/test arg1, arg2", argArray will contain ["$test", "arg1, arg2, ...."]
            argArray = event.getMessage().getContent().split(" ", 2);
            // Extract the "command" part of the first arg out by ditching the amount of characters present in the prefix
            commandStr = argArray[0].substring(prefix.length());
        }

        // turn arg array into list
        if (argArray.length != 1) argsList.addAll(Arrays.asList(argArray[1].split(",[ ]?")));


        // Return if command is not inside of commandMap
        // Instead of delegating the work to a switch, automatically do it via calling the mapping if it exists
        // include command spell cmdSpellCorrect
        if (!commandMap.containsKey(commandStr)) {
            if (commandStr.length() < 2) return; // dont cmdSpellCorrect on length 1 commands

            String corrected = BotUtils.cmdSpellCorrect(commandStr);
            if (corrected != null) {
                commandStr = corrected; // set correction
                BotUtils.send(event.getChannel(), "Command not found, instead executing : `" + commandStr + "`");
            } else return; // no correction
        }

        // Get the command
        Command cmd = commandMap.get(commandStr);
        // Define a runnable for the command
        String finalCommandStr = commandStr;  // need this or final in lambda gets mad
        Runnable runCommand = () -> {
            if (cmd.canRun(event, argsList)) {
                cmd.runCommand(event, argsList);

                if (cmd.loggable()) {
                    cmdPrintLog(event, finalCommandStr, argsList);
                    cmdFreqMap.put(finalCommandStr, cmdFreqMap.getOrDefault(finalCommandStr, 0) + 1);
                    writeFreqMap();
                }
            } else {
                BotUtils.send(event.getChannel(),
                        "Cannot run command due to either incorrect arguments or user's status too low. " +
                                "Here is the command's description:\n" + cmd.getSyntax());
            }

            // Handle state json
//            try {
//                CommandStats cmdStats = MasterJsonUtil.jsonObj.getUserMap().get(event.getAuthor().getStringID()).getCommandStats();
//                cmdStats.setCallCount(cmdStats.getCallCount() + 1);
//            } catch (Exception e) {
//                Aspect.LOG.info("CommandManager - error updating CommandStats json");
//            }
        };

//        Runnable runCmd = () -> {
//            Command.Status status = cmd.mayRun(event, argsList);
//            if (status == Command.Status.OK) {
//                cmd.runCommand(event, argsList);
//                cmdPrintLog(event, finalCommandStr, argsList);
//            } else {
//                BotUtils.send(event.getChannel(), status.getDesc());
//            }
//        };

        // Execute the command on the server specific Executor if synchrony is required
        if (cmd.requireSynchronous()) {
            String id = event.getGuild().getStringID();
            syncExecuteMap.putIfAbsent(id, Executors.newFixedThreadPool(1));
            syncExecuteMap.get(id).execute(runCommand);
        } else { // use default executor
            commandExecutors.execute(runCommand);
        }
    }


    private void cmdPrintLog(MessageReceivedEvent event, String commandStr, List<String> argsList) {
        StringBuilder commandArgs = new StringBuilder();
        for (String s : argsList) commandArgs.append(s).append("\t");

        StringBuilder commandPrint = new StringBuilder()
                .append(LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles")).toLocalDateTime().toString()).append("\t")
                .append(event.getAuthor().getName()).append("\t")
                .append(event.getAuthor().getStringID()).append("\t")
                .append("cmd: " + commandStr).append("\t")
                .append((argsList.size() != 0 ? " args:  " + commandArgs.toString() : ""));

        Aspect.LOG.info(commandPrint.toString());
    }


    private synchronized void writeFreqMap() {
        BotUtils.writeToFile(System.getProperty("user.dir") + "/data/cmd_freq_map.json",
                BotUtils.gson.toJson(cmdFreqMap), false);
    }

    @Override
    public String toString() {
        return "Segmentation fault (core dumped)";
    }

    @Override
    public int hashCode() {
        return Integer.parseInt("no u");
    }

}