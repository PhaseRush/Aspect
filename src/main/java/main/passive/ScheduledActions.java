package main.passive;

import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledActions {
    private static ScheduledFuture<?> morningGreeter = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final long kaitGeneralChatID = 197158565004312576L;
    private static IChannel kaitGenChannel;

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final Runnable quoter= () -> BotUtils.send(kaitGenChannel, BotUtils.getQuoteEmbed());

        // initialize channel
        kaitGenChannel = Main.client.getChannelByID(kaitGeneralChatID);

        morningGreeter = scheduler.scheduleAtFixedRate(quoter, BotUtils.millisToNextHour(7), 1000*60*60*24, TimeUnit.MILLISECONDS);
        System.out.println("Kait morning greeter scheduled for " + Instant.now().plusMillis(BotUtils.millisToNextHour(7)).atZone(ZoneId.of("America/Los_Angeles")).toString());
    }


}
