package main.commands.utilitycommands.metautil;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.List;

public class Reboot implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        // Runtime.getRuntime().exec()

    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }

    @Override
    public String getDesc() {
        return "Reboots self";
    }

    @Override
    public boolean correctable() {
        return false;
    }
}
