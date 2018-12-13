package main.passive;

import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledActions {
    public static ScheduledFuture<?> morningGreeter = null;

    private static final String greetMsg = "Have a good day, everyone :)";
    private static final long kaitGeneralChatID = 197158565004312576L;
    private static IChannel kaitGenChannel;

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable greeter= () -> BotUtils.send(kaitGenChannel, greetMsg);

        // initialize channel
        Main.client.getChannelByID(kaitGeneralChatID);

        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
        ZonedDateTime zonedNow = zonedDateTime.toLocalDate().atStartOfDay(zoneId);

        Instant tmr7amInstant = zonedNow.plusDays(1).plusHours(7).toInstant(); // 7:00 tomorrow (Los Angeles)

        long diffMillis = tmr7amInstant.toEpochMilli() - now.toEpochMilli();

        morningGreeter = scheduler.scheduleAtFixedRate(greeter, diffMillis, 1000*60*60*24, TimeUnit.MILLISECONDS);
        System.out.println("Kait morning greeter scheduled for " + tmr7amInstant.toString());
    }


}
