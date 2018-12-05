package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getAuthor().getOrCreatePMChannel(), "Sorry, due to the dev being lazy, I haven't had a chance to write up a proper help message.\n" +
                "Please visit this for more details: " + BotUtils.GITHUB_URL);
        BotUtils.send(event.getChannel(), "Details have been PM'd");
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Details at: " + BotUtils.GITHUB_URL;
    }
}