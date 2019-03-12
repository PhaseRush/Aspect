package main.commands.utilitycommands;

import main.Command;
import main.passive.ScheduledActions;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ResetCpuHawk implements Command
{
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        ScheduledActions.sentMessage.set(false);
        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }
}
