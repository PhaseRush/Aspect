package main.commands.dontopendeadinside.imaging;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * might get around to using some Tesseract for this one day.
 */
public class Ocr implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }


    @Override
    public String getDesc() {
        return null;
    }
}
