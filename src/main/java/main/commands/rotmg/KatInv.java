package main.commands.rotmg;

import main.Command;
import main.utility.BotUtils;
import main.utility.ReadWrite;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class KatInv implements Command {
    private final String KAT_INV_FILE_PATH = "C:\\Users\\Positron\\IdeaProjects\\Aspect\\txtfiles\\Mooshroom\\katinvurl.txt";
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), ReadWrite.readFromFile(KAT_INV_FILE_PATH));
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "ROTMG";
    }
}
