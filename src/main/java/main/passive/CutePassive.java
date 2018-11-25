package main.passive;

import main.utility.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CutePassive {

    private long lastMeow, lastPat;

    @EventSubscriber
    public void meow(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; //bot get no meowPattern
        if (System.currentTimeMillis() - lastMeow < 1000) return; //meowPattern no more than 1/sec
        if (!event.getMessage().getFormattedContent().matches("\\b(me+o+w)\\b")) return; //actually check the pattern

        BotUtils.sendMessage(event.getChannel(), "*meow meow :3*");
        lastMeow = System.currentTimeMillis();
    }

    //working
    @EventSubscriber
    public void pat(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; //bot get no pat
        if (System.currentTimeMillis() - lastPat < 1000) return; //pat no more than 1/sec
        if (!event.getMessage().getFormattedContent().matches("\\b([pP][aAeE][tT])\\b")) return; //if no pat, NO PAT FOR U

        BotUtils.sendMessage(event.getChannel(), "*pet pet :3*");
        lastPat = System.currentTimeMillis();
    }
}