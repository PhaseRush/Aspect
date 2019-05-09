package main.commands.pokemon.setup;

import main.Aspect;
import main.Command;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TransparentTrimmer implements Command {
    String fileDir ="/home/positron/pokemons";
    String winFileDir = "C:\\Users\\leozh\\Desktop\\pokemons\\";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!event.getAuthor().getStringID().equals("264213620026638336")) return;

        File folder = new File(winFileDir);
        Arrays.stream(folder.listFiles()).forEach(imageFile -> {
            Aspect.LOG.info(imageFile.getName() + "\n");
            if (imageFile.getName().startsWith("Naganadel") || imageFile.getName().startsWith("Tynamo")) return;
            try {
                BufferedImage img = ImageIO.read(imageFile);
                BufferedImage cropped = Visuals.cropTransparent(img);
                ImageIO.write(cropped, "png", imageFile);
            } catch (IOException e) {
                Aspect.LOG.info("screwed up");
            }
        });
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
