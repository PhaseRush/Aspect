package main.commands.utilitycommands.metautil;

import main.Command;
import main.Main;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Uptime implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), genUptime());
    }

    public static String genUptime() {
        long millis = System.currentTimeMillis() - Main.startTime;
        int sec = (int) (millis / 1000) % 60;
        int min = (int) ((millis / (1000 * 60)) % 60);
        int hr = (int) ((millis / (1000 * 60 * 60)) % 24);

        int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
        return "```Uptime:   " + days + " d  " + hr + ":" + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec) +
                "  =  " + millis + " ms" +
                "\nStart instant:\t" + Main.startInstant.toString() + "```";
    }

    @Override
    public String getDesc() {
        return "Displays Aspect's uptime (time since last launch)";
    }
}
