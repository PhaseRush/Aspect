package main.commands.utilitycommands.metautil;

import main.Aspect;
import main.Command;
import main.passive.ScheduledActions;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Reboot implements Command {
    private static final Runnable runReboot = Reboot::reboot;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!args.isEmpty()) {
            if ("toggle".equals(args.get(0))) { // https://stackoverflow.com/questions/1255617/does-atomicboolean-not-have-a-negate-method
                boolean temp;
                do {
                    temp = ScheduledActions.willRebootOnMax.get();
                } while (!ScheduledActions.willRebootOnMax.compareAndSet(temp, !temp));
                BotUtils.send(event.getChannel(), "Aspect will now " + (ScheduledActions.willRebootOnMax.get() ? "" : "not ") + "reboot on max cpu");
            } else {
                try {
                    int seconds = Integer.parseInt(args.get(0));
                    Executors.newScheduledThreadPool(1).schedule(runReboot, seconds, TimeUnit.SECONDS);
                } catch (NumberFormatException e) {
                    BotUtils.send(event.getChannel(), "Number format exception for " + args.get(0));
                }
            }
        } else { // empty args
            reboot(Collections.singletonList(event.getChannel()));
        }
    }

    public static void reboot() {
        reboot(Aspect.client.getGuilds().stream()
                .filter(MasterManager::hasPlayer)
                .map(g -> MasterManager.getGuildAudioPlayer(g).getScheduler().lastEmbedChannel)
                .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    /**
     * @param channels to alert
     */
    private static void reboot(List<IChannel> channels) {
        long pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        // use sendget to block
        channels.forEach(ch -> BotUtils.sendGet(ch,
                "Aspect is now rebooting due to lag. Feel free to queue up more music in a few moments."));

        try {
            BotUtils.msgDev("Attempting to reboot INSTANCE_ID:\t" + Aspect.INSTANCE_ID.toString());
            Runtime.getRuntime().exec(new String[]{"bash", "-c", System.getenv("HOME") + "/reboot_aspect.sh " + pid});
        } catch (IOException e) {
            BotUtils.msgDev("Error rebooting:\n" + e.getMessage());
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