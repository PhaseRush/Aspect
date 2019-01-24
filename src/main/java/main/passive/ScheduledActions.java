package main.passive;

import main.Main;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledActions {
    private static ScheduledFuture<?> morningGreeter = null;
    private static final String quotesURL = "http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1";

    private static final String greetMsg = "Have a good day, everyone :)";
    private static final long kaitGeneralChatID = 197158565004312576L;
    private static final long blackBoxID = 417926480501407745L;
    private static IChannel kaitGenChannel;
    private static IChannel blackBoxChannel;

    @EventSubscriber
    public void morningGreeter(ReadyEvent event) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable greeter= () -> BotUtils.send(kaitGenChannel, greetMsg);
        final Runnable tester= () -> BotUtils.send(blackBoxChannel, generate());
        // RUN THIS FOR TESTING
        // scheduler.scheduleAtFixedRate(tester, 100, 5000, TimeUnit.MILLISECONDS);


        // initialize channel
        kaitGenChannel = Main.client.getChannelByID(kaitGeneralChatID);
        blackBoxChannel = Main.client.getChannelByID(blackBoxID);

        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
        ZonedDateTime zonedNow = zonedDateTime.toLocalDate().atStartOfDay(zoneId);

        Instant tmr7amInstant = zonedNow.plusDays(1).plusHours(7).toInstant(); // 7:00 tomorrow (Los Angeles)

        long diffMillis = tmr7amInstant.toEpochMilli() - now.toEpochMilli();

        morningGreeter = scheduler.scheduleAtFixedRate(greeter, diffMillis, 1000*60*60*24, TimeUnit.MILLISECONDS);
        System.out.println("Kait morning greeter scheduled for " + tmr7amInstant.toString());
    }

    private EmbedBuilder generate() {
        String jsonArray = BotUtils.getStringFromUrl(quotesURL);
        String json = jsonArray.substring(1, jsonArray.length()-1);
        QuoteContainer quote = BotUtils.gson.fromJson(json, QuoteContainer.class);

        return new EmbedBuilder()
                .withTitle("Random quote of the day")
                .withColor(Visuals.getVibrantColor())
                .withDesc("\"" + quote.content.substring(3, quote.content.length()-5) + "\n\t- " + quote.title);
    }


    private class QuoteContainer {
        int id;
        String title;
        String content;
        String link;
    }


}
