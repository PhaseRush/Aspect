package main.commands.utilitycommands.metautil;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

public class Reboot implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        try {
            Runtime.getRuntime().exec(new String[]{"bash", "-c", System.getenv("HOME") + "/reboot_aspect.sh " + pid});
        } catch (IOException e) {
            BotUtils.send(event.getChannel(), "Error rebooting:\n" + e.getMessage());
            e.printStackTrace();
        }

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
