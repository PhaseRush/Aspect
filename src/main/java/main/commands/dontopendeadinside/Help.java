package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), "Details at: " + BotUtils.GITHUB_URL);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}