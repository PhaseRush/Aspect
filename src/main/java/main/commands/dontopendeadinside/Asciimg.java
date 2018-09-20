package main.commands.dontopendeadinside;

import main.Command;
import main.utility.Visuals;
import main.utility.asciimg.image.AsciiImgCache;
import main.utility.asciimg.image.character_fit_strategy.ColorSquareErrorFitStrategy;
import main.utility.asciimg.image.character_fit_strategy.StructuralSimilarityFitStrategy;
import main.utility.asciimg.image.converter.AsciiToImageConverter;
import main.utility.asciimg.image.converter.AsciiToStringConverter;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Asciimg implements Command {
    AsciiToStringConverter stringConverter;
    AsciiToImageConverter imageConverter;
    AsciiImgCache cache;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String imgUrl = args.get(0);
        BufferedImage userImage = Visuals.urlToBufferedImage(imgUrl);
        String nick = event.getAuthor().getNicknameForGuild(event.getGuild());


        if (args.size() == 2) {
            cache = AsciiImgCache.create(new Font("Courier", Font.BOLD, Integer.valueOf(args.get(1))));
        } else {
            cache = AsciiImgCache.create(new Font("Courier", Font.BOLD, 6));
        }

        if (args.size() == 3) {
            if (args.get(2).equals("-o txt"))
                AsciiToText(event, userImage, cache);
        } else
            AsciiToImage(event, userImage, cache);

    }

    private void AsciiToImage(MessageReceivedEvent event, BufferedImage userImage, AsciiImgCache cache) {
        imageConverter = new AsciiToImageConverter(cache, new ColorSquareErrorFitStrategy());
        BufferedImage asciimg = imageConverter.convertImage(userImage);

        File asciimgFile = new File("asciimgFile.png");
        try {
            ImageIO.write(asciimg, "png", asciimgFile);
            event.getChannel().sendFile(asciimgFile);
            event.getMessage().delete();
            asciimgFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://www.genuitec.com/dump-a-stringbuilder-to-file/
    //https://stackoverflow.com/questions/1677194/dumping-a-java-stringbuilder-to-file
    private void AsciiToText(MessageReceivedEvent event, BufferedImage userImage, AsciiImgCache cache) {
        stringConverter = new AsciiToStringConverter(cache, new StructuralSimilarityFitStrategy());
        StringBuffer asciiString = stringConverter.convertImage(userImage);
        File outputAsciiTxt = new File("Temp Ascii txt file by " + event.getAuthor().getName() + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputAsciiTxt))){
            bw.write(asciiString.toString());

            event.getChannel().sendFile(outputAsciiTxt);
            event.getMessage().delete();
            outputAsciiTxt.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Ascii-fied";
    }
}
