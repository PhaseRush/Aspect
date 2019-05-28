package main.commands.humor.cute;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class CatPics implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withImage(Visuals.getCatMedia()));
    }

    @Override
    public String getDesc() {
        return "shows you a cute cat picture";
    }

    @Override
    public String getSyntax() {
        return "$cat";
    }
}
