package main.passive;

import com.sun.management.OperatingSystemMXBean;
import main.Aspect;
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
    private static final long kaitQuoteChannelId = 562375333807128576L; // Vowed's#questions-or-ranting
    private static IChannel kaitQuoteChannel;

    private static final long kaitLeagueChannelId = 1L;
    private static IChannel kaitLeagueChannel;
    private static final long pantsuGenId = 402728027680931841L;
    private static IChannel pantsuGenChannel;

    // cpu 100% watcher
    public static AtomicBoolean sentMessage = new AtomicBoolean(false);

    // graph profiler
    static public DoubleRingBuffer cpuQueue = new DoubleRingBuffer(30);
    static public DoubleRingBuffer memQueue = new DoubleRingBuffer(30);

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final Runnable quoter = () -> BotUtils.send(kaitQuoteChannel, BotUtils.getQuoteEmbed());

        // initialize channel
        kaitQuoteChannel = Aspect.client.getChannelByID(kaitQuoteChannelId);

        scheduledFuture = scheduler.scheduleAtFixedRate(quoter, BotUtils.millisToNextHour24(7, "America/Los_Angeles"), 1000*60*60*24, TimeUnit.MILLISECONDS);
        Aspect.LOG.info("Kait morning greeter scheduled for " + Instant.now().plusMillis(BotUtils.millisToNextHour24(7, "America/Los_Angeles")).toString());
    }

    @EventSubscriber
    public void leagueQuotes(ReadyEvent event) {
        final Runnable quoter = () -> {
            BotUtils.send(kaitLeagueChannel, BotUtils.getLeagueQuoteEmbed());
            BotUtils.send(kaitLeagueChannel, BotUtils.getLeagueQuoteEmbed());
        };

        kaitLeagueChannel = event.getClient().getChannelByID(kaitLeagueChannelId);
        pantsuGenChannel = event.getClient().getChannelByID(pantsuGenId);

        scheduledFuture = scheduler.scheduleAtFixedRate(quoter, BotUtils.millisToNextHour24(7, "America/Los_Angeles"), 24, TimeUnit.HOURS);
        Aspect.LOG.info("League quoter scheduled");
    }

    @EventSubscriber
    public void streak(ReadyEvent event) {
        IUser resuna = Aspect.client.getUserByID(105688694219886592L);
        IUser kait = Aspect.client.getUserByID(187328584698953728L);

        final Runnable streaker = () -> {
            BotUtils.send(resuna.getOrCreatePMChannel(), "Streak");
            BotUtils.send(kait.getOrCreatePMChannel(), "Streak");
        };

        //long initDelay = BotUtils.millisToNextHHMMSSMMMM(18, 40, 0, 0, "CST6CDT");
        long initDelay2 = BotUtils.millisToNextHHMMSSMMMM(16, 40,0, 0, "America/Los_Angeles");
        scheduledFuture = scheduler.scheduleAtFixedRate(streaker,
                initDelay2,
                24*60*60*1000,
                TimeUnit.MILLISECONDS);

        Aspect.LOG.info("Streak Scheduler has been init to " + initDelay2 + " ms");
    }

    @EventSubscriber
    public void cpuLoadWatcher(ReadyEvent event) {
        final Runnable cpuHawk = () -> {
            if ( !sentMessage.get() && Aspect.osBean.getSystemLoadAverage() > 1) {
                BotUtils.send(
                        Aspect.client.getUserByID(BotUtils.DEV_DISCORD_LONG_ID).getOrCreatePMChannel(),
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
