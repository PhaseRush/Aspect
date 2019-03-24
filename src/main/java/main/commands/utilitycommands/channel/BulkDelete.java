package main.commands.utilitycommands.channel;

import main.Command;
import main.utility.MessageDeleter;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class BulkDelete implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        int timeToDeleteFrom = Integer.valueOf(args.get(0));
        new MessageDeleter(timeToDeleteFrom, event.getChannel(), event.getAuthor()).runBulkDelete();
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }

    @Override
    public String getDesc() {
        return "bulk deletes message from a text channel. \nCURRENTLY DISABLED FOR OBVIOUS REASONS";
    }
}
