package main;

import main.commands.dontopendeadinside.*;
import main.commands.fortnite.FortniteShopDetail;
import main.commands.fortnite.FortniteShopSelect;
import main.commands.fortnite.FortniteStats;
import main.commands.humor.AnalyzeUser;
import main.commands.humor.CuteImg;
import main.commands.humor.Insult;
import main.commands.humor.Ship;
import main.commands.league.*;
import main.commands.music.*;
import main.commands.music.queue.ShuffleQueue;
import main.commands.music.queue.SongQueue;
import main.commands.nasa.BlueMarble;
import main.commands.nasa.NasaApod;
import main.commands.rotmg.*;
import main.commands.utilitycommands.*;
import main.commands.warframe.*;
import main.commands.wolfram.WolframGeneral;
import main.utility.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

import java.time.LocalTime;
import java.util.*;

public class CommandManager {
    private long readyTime = System.currentTimeMillis();
    public Map<String, Command> commandMap = new LinkedHashMap<>();
    //talked to hec about using a static initializer but constructor is fine

    public CommandManager() {
        //dontdeadopeninside
        commandMap.put("ascii2", new AsciifyOld());
        commandMap.put("ascii", new Asciimg());
        commandMap.put("help", new Help());
        commandMap.put("summarize", new Summarize());
        commandMap.put("img", new Imaging());

        //humor
        commandMap.put("cute", new CuteImg());
        commandMap.put("ship", new Ship());
        commandMap.put("insult", new Insult());
        commandMap.put("count", new WordCounter());
        commandMap.put("lyze", new AnalyzeUser());

        //music
        SongInfo songInfo = new SongInfo();
        SongStop songStop = new SongStop();
        commandMap.put("lvoice", songStop);
        commandMap.put("stop", songStop);
        commandMap.put("play", new SongPlay());
        commandMap.put("nowplaying", songInfo);
        commandMap.put("currentsong", songInfo);
        commandMap.put("skip", new    SongSkip());
        commandMap.put("queue", new SongQueue());
        commandMap.put("loop", new SongLoop());
        commandMap.put("shuffle", new ShuffleQueue());

        commandMap.put("sfx", new SoundEffect());
        commandMap.put("listsfx", new SoundEffectList());

        //Realm of the Mad God
        commandMap.put("katinv", new KatInv());
        commandMap.put("rguild", new RotmgGuildInfo());
        commandMap.put("rpet", new RotmgPets());
        commandMap.put("rrate", new RotmgRatePlayer());
        commandMap.put("rdesc", new RotmgReadDescription());
        commandMap.put("rrecentchar", new RotmgRecentChar());
        commandMap.put("rscore", new RotmgTotalScore());
        commandMap.put("setinv", new SetKatInv());

        //general commands
        commandMap.put("bulkdelete", new BulkDelete());
        commandMap.put("time", new CurrentTime());
        commandMap.put("deletemsg", new DeleteMsg());

        //Fortnite
        commandMap.put("fn", new FortniteStats());
        commandMap.put("fnshop", new FortniteShopSelect());
        commandMap.put("fnselect", new FortniteShopDetail());

        //meta
        commandMap.put("setprefix", new SetPrefix());
        commandMap.put("ban", new BanUser());
        commandMap.put("ping", new Ping());
        commandMap.put("dev", new Dev());
        commandMap.put("membercount", new MemberCount());
        commandMap.put("poll", new Poll());
        commandMap.put("cpuload", new SystemLoad());
        commandMap.put("makerole", new RoleGenerate());

        //League of Legends
        commandMap.put("lollevel", new BasicLeague());
        commandMap.put("lolsum", new LeaguePlayerEmbed());
        commandMap.put("lolregions", new AvailableRegions());
        commandMap.put("lolitem", new RandomItem());
        commandMap.put("allskins", new ListSkins());
        commandMap.put("skin", new SkinDetail());
        commandMap.put("lolrecent", new PreviousMatch());

        //Warframe
        commandMap.put("wfdaily", new WarframeDailyDeals());
        commandMap.put("wfcetus", new WarframeCetusCycle());
        commandMap.put("wfalerts", new WarframeAlerts());
        commandMap.put("wfvoid", new WarframeVoidFissures());
        //commandMap.put("wfitems", new WarframeItems()); //dev use
        commandMap.put("wfinfo", new WarframeItemInfo());
        commandMap.put("wfaco", new WarframeAcolyteTracker());
        commandMap.put("wfvoidtrader", new WarframeVoidTraderTracker());
        commandMap.put("wfmarket", new WarframeMarketListings());
        commandMap.put("wfdropinfo", new WarframeDropInfo());

        //Nasa
        commandMap.put("apod", new NasaApod());
        commandMap.put("bluemarble", new BlueMarble());

        //Wolfram Alpha
        WolframGeneral wolframGeneral = new WolframGeneral();
        commandMap.put("wolfram", wolframGeneral);
        commandMap.put("answer", wolframGeneral);
        commandMap.put("solve", wolframGeneral);
        commandMap.put("_", wolframGeneral);
        commandMap.put(" ", wolframGeneral);
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        event.getClient().changePresence(StatusType.ONLINE, ActivityType.LISTENING, " your commands");
        readyTime = System.currentTimeMillis();
        System.out.println("Aspect is online and ready at " + LocalTime.now().toString());
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {

        // Given a message "/test arg1, arg2", argArray will contain ["!test", "arg1, arg2, ...."]
        String[] argArray = event.getMessage().getContent().split(" ", 2);

        // Check if the first arg (the command) starts with the prefix defined in the utils class
        if (!argArray[0].startsWith(BotUtils.DEFAULT_BOT_PREFIX))
            return;

        //if(BotUtils.PREFIX_MAP.get(argArray[0]).equals())

        //String mappedPrefix = BotUtils.getPrefix(event.getGuild());
        //if(!argArray[0].startsWith(mappedPrefix)) {
        //    return;
        //}


        // Extract the "command" part of the first arg out by ditching the amount of characters present in the prefix
        String commandStr = argArray[0].substring(BotUtils.DEFAULT_BOT_PREFIX.length());

        // Load the rest of the args in the array into a List for safer access
        // CHANGED IMPLEMENTATION TO BETTER SEPARATE COMMAS AND SPACES
        List<String> argsList = new ArrayList<>();
        if (argArray.length != 1) {
            argsList.addAll(Arrays.asList(argArray[1].split(", ")));
        }

        // Instead of delegating the work to a switch, automatically do it via calling the mapping if it exists, or call wolfram general if it doesn't
        if (commandMap.containsKey(commandStr)) {
            commandMap.get(commandStr).runCommand(event, argsList);

            StringBuilder sb = new StringBuilder();
            for (String s : argsList) {
                sb.append(s + "\t");

            }
            System.out.println("run: " + commandStr + (argsList.size() != 0 ? " args: " + sb.toString() : ""));
//        } else { //else just assume wolfram
//            String query = event.getMessage().getFormattedContent().substring(BotUtils.DEFAULT_BOT_PREFIX.length());
//            //BotUtils.sendMessage(event.getChannel(), "Interpreting: " + query);
//            commandMap.get("wolfram").runCommand(event, Collections.singletonList(query));
//            System.out.println("Triggered: Wolfram. Query: " + query);
        }
    }

    @Override
    public String toString() {
        return "Segmentation fault (core dumped)";
    }

}
