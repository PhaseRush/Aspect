package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Ping implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), "Pong!");
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "a meme";
    }
}
