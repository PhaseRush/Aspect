package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class BulkDelete implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        int timeToDeleteFrom = Integer.valueOf(args.get(0));
        //new MessageDeleter(timeToDeleteFrom, event.getChannel(), event.getAuthor()).runBulkDelete();
        BotUtils.sendMessage(event.getChannel(), "Command disabled.");
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "bulk deletes message from a text channel. \nCURRENTLY DISABLED FOR OBVIOUS REASONS";
    }
}
