package main.passive;

import main.utility.metautil.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CutePassive {

    private long lastMeow, lastPat;
    private static List<Long> cuteWhiteList = Arrays.asList(417926479813279754L, 402728027223490572L); //singleton list for now
    private Random r = ThreadLocalRandom.current();

    private Pattern owo = Pattern.compile("(?i)\\b([o0U*]+[\\s]*[wv3]+[\\s]*[*U0o]+)+\\b"); // added '3' for vivi


    @EventSubscriber
    public void owo(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // bots get no owo :3

        Matcher match = owo.matcher(event.getMessage().getFormattedContent()); // get a matcher
        if (match.find()) { // use grouping
            if (r.nextInt() % 4 == 0) BotUtils.send(event.getChannel(), match.group());
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
