package main.passive;

import com.sun.management.OperatingSystemMXBean;
import main.Main;
import main.utility.metautil.BotUtils;
import main.utility.structures.DoubleRingBuffer;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduledActions {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // kaitlyn quoter
    private static final long kaitGeneralChatID = 562375333807128576L; // Vowed's#questions-or-ranting
    private static IChannel kaitGenChannel;

    // streak btw (btw (btw))
    private final long RESUNA_ID = 1L;
    private final long KAIT_ID = 1L;
    private static IUser resuna;
    private static IUser kait;

    // cpu 100% watcher
    public static AtomicBoolean sentMessage = new AtomicBoolean(false);

    // graph profiler
    static public DoubleRingBuffer cpuQueue = new DoubleRingBuffer(30);
    static public DoubleRingBuffer memQueue = new DoubleRingBuffer(30);

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final Runnable quoter = () -> BotUtils.send(kaitGenChannel, BotUtils.getQuoteEmbed());

        // initialize channel
        kaitGenChannel = Main.client.getChannelByID(kaitGeneralChatID);

        scheduledFuture = scheduler.scheduleAtFixedRate(quoter, BotUtils.millisToNextHour24(7, "America/Los_Angeles"), 1000*60*60*24, TimeUnit.MILLISECONDS);
        System.out.println("Kait morning greeter scheduled for " + Instant.now().plusMillis(BotUtils.millisToNextHour24(7, "America/Los_Angeles")).toString());
    }

    @EventSubscriber
    public void leagueQuotes(ReadyEvent event) {
        final Runnable quoter = () -> {
        };
    }

    @EventSubscriber
    public void streak(ReadyEvent event) {
        resuna = Main.client.getUserByID(RESUNA_ID);
        kait = Main.client.getUserByID(KAIT_ID);

        final Runnable streaker = () -> {
            BotUtils.send(resuna.getOrCreatePMChannel(), "Streak");
            BotUtils.send(kait.getOrCreatePMChannel(), "Streak");
        };

        scheduledFuture = scheduler.scheduleAtFixedRate(streaker,
                BotUtils.millisToNextHHMMSSMMMM(18, 40, 0, 0, "CST6CDT"),
                24*60*60*1000,
                TimeUnit.MILLISECONDS);
    }

    @EventSubscriber
    public void cpuLoadWatcher(ReadyEvent event) {
        final Runnable cpuHawk = () -> {
            if ( !sentMessage.get() && Main.osBean.getSystemLoadAverage() > 1) {
                BotUtils.send(
                        Main.client.getUserByID(Long.valueOf(BotUtils.DEV_DISCORD_STRING_ID)).getOrCreatePMChannel(),
                        "cpu sad :(");

                sentMessage.set(true);
            }
        };

        scheduledFuture = scheduler.scheduleAtFixedRate(cpuHawk, 60, 60, TimeUnit.SECONDS); // start with more offset so doesnt trigger off reboot
    }

    @EventSubscriber
    public void cpuUsageProfiler(ReadyEvent event) {

        final Runnable systemProber = () -> {
            cpuQueue.push(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad()*100); // convert to %
            memQueue.push((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1E6);
        };

        scheduledFuture = scheduler.scheduleAtFixedRate(systemProber, 3, 30, TimeUnit.SECONDS); // start with more offset to not collide with other process
    }


}
