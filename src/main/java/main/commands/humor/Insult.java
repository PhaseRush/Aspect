package main.commands.humor;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Insult implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), BotUtils.getRandomFromListString(BotUtils.insults));
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }


    public String helpMsg() {
        return null;
    }
}
