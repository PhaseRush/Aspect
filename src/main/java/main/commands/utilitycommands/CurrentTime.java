package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Now works correctly with Vancouver time zone
 * Might expand to work for any city in the future :)
 */
public class CurrentTime implements Command {

    private final Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Vancouver"));

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(),
                "In Vancouver, it is currently " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE));
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
