package main.commands.utilitycommands.metautil;

import main.Command;
import main.passive.WfPassive;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

import java.util.List;

public class UpdatePresence implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).equals("warframe")) WfPassive.warframeOpenWorldPresence();
        else {
            ActivityType activity = null;
            switch (args.get(0).toLowerCase()) {
                case "p":
                    activity = ActivityType.PLAYING;
                    break;
                case "s":
                    activity = ActivityType.STREAMING;
                    break;
                case "l":
                    activity = ActivityType.LISTENING;
                    break;
                case "w":
                    activity = ActivityType.WATCHING;
                    break;
            }

            WfPassive.killCetusUpdater();

            event.getClient().changePresence(StatusType.ONLINE, activity, args.get(1));
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }

}
