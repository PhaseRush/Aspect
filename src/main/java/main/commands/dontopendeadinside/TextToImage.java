package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class TextToImage implements Command {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String path = "text_image_" + UUID.randomUUID().toString();
        Visuals.saveImg(Visuals.genTextImage(args.get(0)), path);

        path = System.getProperty("user.dir") + "\\" + path + ".png";
        BotUtils.sendGet(event.getChannel(), path, true);

        new File(path).delete();
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    public String getDesc() {
        return "Generates a nice image for your loving message";
    }
}
