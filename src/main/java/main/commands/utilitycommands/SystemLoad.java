package main.commands.utilitycommands;

import main.Command;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;

public class SystemLoad implements Command {
    OperatingSystemMXBean osBean = Main.osBean;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String msg = "Arch: " + osBean.getArch() +
                "\nName: " + osBean.getName() +
                "\nSys Avg Load: " + osBean.getSystemLoadAverage() +
                "\nVersion: " + osBean.getVersion() +
                "\n Avail Processors: " + osBean.getAvailableProcessors();

        BotUtils.sendMessage(event.getChannel(), msg);

    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "How hard I'm working.";
    }
}
