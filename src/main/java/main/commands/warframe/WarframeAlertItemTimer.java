package main.commands.warframe;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeAlertItemTimer implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return ":(";
    }
}
