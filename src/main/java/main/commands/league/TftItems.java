package main.commands.league;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class TftItems implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder()
                        .withImage("https://cdn.discordapp.com/attachments/356286606530969602/593487598819999757/unknown.png")
                        .withColor(Visuals.getRandVibrantColour()));
        BotUtils.send(event.getChannel(),
                new EmbedBuilder()
                        .withImage("https://cdn.discordapp.com/attachments/356286606530969602/593509924865310720/unknown.png")
                        .withColor(Visuals.getRandVibrantColour()));
    }
}
