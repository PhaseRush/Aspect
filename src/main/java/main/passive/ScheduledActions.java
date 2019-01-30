package main.passive;

import com.sun.management.OperatingSystemMXBean;
import main.Main;
import main.utility.BotUtils;
import main.utility.structures.DoubleRingBuffer;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduledActions {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // kaitlyn quoter
    private static final long kaitGeneralChatID = 197158565004312576L;
    private static IChannel kaitGenChannel;

    // cpu 100% watcher
    AtomicBoolean sentMessage = new AtomicBoolean(false);

    // graph profiler
    static public DoubleRingBuffer cpuQueue = new DoubleRingBuffer(30);
    static public DoubleRingBuffer memQueue = new DoubleRingBuffer(30);

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final Runnable quoter= () -> BotUtils.send(kaitGenChannel, BotUtils.getQuoteEmbed());

        // initialize channel
        kaitGenChannel = Main.client.getChannelByID(kaitGeneralChatID);

        scheduledFuture = scheduler.scheduleAtFixedRate(quoter, BotUtils.millisToNextHour24(7), 1000*60*60*24, TimeUnit.MILLISECONDS);
        System.out.println("Kait morning greeter scheduled for " + Instant.now().plusMillis(BotUtils.millisToNextHour24(7)).atZone(ZoneId.of("America/Los_Angeles")).toString());
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

        scheduledFuture = scheduler.scheduleAtFixedRate(cpuHawk, 30, 60, TimeUnit.SECONDS); // start with more offset so doesnt trigger off reboot
    }

    @EventSubscriber
    public void cpuUsageProfiler(ReadyEvent event) {
        
        final Runnable systemProber = () -> {
            cpuQueue.push(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad()*100); // convert to %
            memQueue.push((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1E6);
        };

        scheduledFuture = scheduler.scheduleAtFixedRate(systemProber, 3, 30, TimeUnit.SECONDS); // start with more offset so doesnt trigger off reboot

    }


}
