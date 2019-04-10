package main.commands.dontopendeadinside.imaging;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Render implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(
                event.getChannel(),
                new EmbedBuilder().withImage("attachment://render.png"),
                new ByteArrayInputStream(Visuals.genTextImage(BotUtils.concatArgs(args)).toByteArray()),
                "render.png");
    }

    @Override
    public String getDesc() {
        return "Generates a nice image for your loving message";
    }
}
