package main.commands.pokemon;

import main.Command;
import main.utility.BotUtils;
import main.utility.PokemonUtil;
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
                    System.out.println("Could not find difference image");
                }
                return null;
            }).get();
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "refactored from automatic identification to a command - requested by Vowed 4 nov 2018";
    }
}
