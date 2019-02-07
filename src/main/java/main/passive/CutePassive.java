package main.passive;

import main.utility.metautil.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CutePassive {

    private long lastMeow, lastPat;
    private static List<Long> cuteWhiteList = Arrays.asList(417926479813279754L, 402728027223490572L); //singleton list for now
    private Random r = ThreadLocalRandom.current();


    @EventSubscriber
    public void owo(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // bots get no owo :3
        if (event.getMessage().getContent().equalsIgnoreCase("owo")) {
            int rand = r.nextInt(100);

            if (rand == 1) BotUtils.send(event.getChannel(), "degenerate");
            else if (rand % 4 == 0) BotUtils.send(event.getChannel(), "owo");
        }
    }

    @EventSubscriber
    public void meow(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; //bot get no meowPattern
        if (System.currentTimeMillis() - lastMeow < 1000) return; //meowPattern no more than 1/sec
        //if (!cuteWhiteList.contains(event.getGuild().getLongID())) return;
        if (!event.getMessage().getFormattedContent().matches("(?i)\\b(me+o+w)\\b")) return; //actually check the pattern

        BotUtils.send(event.getChannel(), "*meow meow :3*");
        lastMeow = System.currentTimeMillis();
    }

    @EventSubscriber
    public void pat(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; //bot get no pat
        if (System.currentTimeMillis() - lastPat < 1000) return; //pat no more than 1/sec
        //if (!cuteWhiteList.contains(event.getGuild().getLongID())) return;
        if (!event.getMessage().getFormattedContent().matches("(?i)\\b(p(a+|e+)t)\\b")) return; //if no pat, NO PAT FOR U

        BotUtils.send(event.getChannel(), "*pet pet :3*");
        lastPat = System.currentTimeMillis();
    }
}
