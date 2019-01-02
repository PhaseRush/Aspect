package main.commands.utilitycommands.metautil;

import main.Command;
import main.passive.WfPassive;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

import java.util.List;

public class UpdatePresence implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).equals("warframe")) WfPassive.cetusTimePresense();
        else {
            ActivityType activity = null;
            switch (args.get(0)) {
                case "P":
                    activity = ActivityType.PLAYING;
                    break;
                case "S":
                    activity = ActivityType.STREAMING;
                    break;
                case "L":
                    activity = ActivityType.LISTENING;
                    break;
                case "W":
                    activity = ActivityType.WATCHING;
                    break;
            }

            event.getClient().changePresence(StatusType.ONLINE, activity, args.get(1));
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return event.getAuthor().getStringID().equals(BotUtils.DEV_DISCORD_STRING_ID);
    }

}
