package main.commands.dontopendeadinside.imaging;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.awt.image.BufferedImage;
import java.util.List;

public class Haruwu implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BufferedImage img = Visuals.urlToBufferedImage(event.getAuthor().getAvatarURL());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event) || event.getAuthor().getStringID().equals("292350757691457537");
    }

    @Override
    public String getDesc() {
        return null;
    }
}
