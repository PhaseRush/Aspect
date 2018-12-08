package main.commands.utilitycommands;

import main.Command;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;

public class SystemLoad implements Command {
    OperatingSystemMXBean osBean;
    Runtime r;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        osBean = Main.osBean;
        r = Runtime.getRuntime();
        double maxM = r.maxMemory() / 1E6;

        String msg = "```" +
                "System Load:  \t\t" + osBean.getSystemLoadAverage() +
                "\nRam (MB): \t\t\t" + (int) (maxM - r.freeMemory() / 1E6) + " / " + (int) maxM +
                "\nArch: \t\t\t\t" + osBean.getArch() +
                "\nName: \t\t\t\t" + osBean.getName() +
                "\nVersion:  \t\t\t" + osBean.getVersion() +
                "\nAvail Cores:  \t\t" + osBean.getAvailableProcessors()
                + "```";

        BotUtils.send(event.getChannel(), msg);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return "How hard I'm working.";
    }
}