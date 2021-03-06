package main.commands.utilitycommands;

import main.Command;
import main.utility.metautil.BotUtils;
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

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Vancouver"));
        int minute = calendar.get(Calendar.MINUTE);
        BotUtils.send(event.getChannel(),
                "In Vancouver, it is currently " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        (minute < 10 ? "0" + minute : minute));
    }

    @Override
    public String getDesc() {
        return "Shows shows current time in Vancouver, Canada";
    }
}
