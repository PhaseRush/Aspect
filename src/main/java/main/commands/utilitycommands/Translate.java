package main.commands.utilitycommands;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * see TranslateBot for impl.
 * CUZ IM NOT DOING THIS AGAIN REEEEE
 */
public class Translate implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "WIP - translate input";
    }
}
