package main.commands.rotmg;

import main.Command;
import main.utility.ReadWrite;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class KatInv implements Command {
    private final String KAT_INV_FILE_PATH = "C:\\Users\\Positron\\IdeaProjects\\Aspect\\txtfiles\\Mooshroom\\katinvurl.txt";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), ReadWrite.readFromFile(KAT_INV_FILE_PATH));
    }

    @Override
    public String getDesc() {
        return "ROTMG";
    }
}
