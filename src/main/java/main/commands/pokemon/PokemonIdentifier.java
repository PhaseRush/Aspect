package main.commands.pokemon;

import main.Aspect;
import main.Command;
import main.utility.PokemonUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PokemonIdentifier implements Command {


    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), PokemonUtil.mostRecentEmbed);

        if (PokemonUtil.shouldSendDiff){
            RequestBuffer.request(() -> {
                try {
                    File diffImgFile = new File(PokemonUtil.mostRecentDiffImgPath);
                    return event.getChannel().sendFile("Difference Image: ", diffImgFile);
                } catch (FileNotFoundException e) {
                    BotUtils.send(event.getChannel(), "Error sending difference image");
                    Aspect.LOG.info("Could not find difference image");
                }
                return null;
            }).get();
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return PokemonUtil.mostRecentEmbed != null;
    }

    @Override
    public String getDesc() {
        return "refactored from automatic identification to a command - requested by Vowed 4 nov 2018";
    }
}
