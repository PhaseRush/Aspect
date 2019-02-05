package main.commands.dontopendeadinside.games;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Minesweeper implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

    }

    @Override
    public String getDesc() {
        return "Generates a game of minesweeper to play";
    }
}
