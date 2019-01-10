package main.commands.dontopendeadinside.imaging.ascii;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Refactored from: Eric Mikulin, 2015
 * https://gist.github.com/ErisMik/f497e041a955f41f7a6a
 */
public class AsciifyOld implements Command {

    static Map<Integer, Character> asciiMap = new HashMap<>();

    static {
        //Map an integer darkness value to an ASCII character
        //The value of darkness for the chars I determined from some other random Internet source
        asciiMap.put(0, '@');
        asciiMap.put(1, '@');
        asciiMap.put(2, '@');
        asciiMap.put(3, '@');
        asciiMap.put(4, '@');

        asciiMap.put(5, '@');
        asciiMap.put(6, 'N');
        asciiMap.put(7, '%');
        asciiMap.put(8, 'W');
        asciiMap.put(9, '$');

        asciiMap.put(10, 'D');
        asciiMap.put(11, 'd');
        asciiMap.put(12, 'x');
        asciiMap.put(13, '6');
        asciiMap.put(14, 'E');

        asciiMap.put(15, '5');
        asciiMap.put(16, '{');
        asciiMap.put(17, 's');
        asciiMap.put(18, '?');
        asciiMap.put(19, '!');

        asciiMap.put(20, ';');
        asciiMap.put(21, '"');
        asciiMap.put(22, ':');
        asciiMap.put(23, '_');
        asciiMap.put(24, '\'');

        asciiMap.put(25, '`');
        asciiMap.put(26, ' ');
    }

    /**
     * @param args url,
     *             "Line Skip Coefficient"
     *             Basically, this is the amount the x and y values of the coordinate increase each time. Larger = smaller image.
     *             Anything above 2 usually doesn't work too well
     */
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        final BufferedImage image;
        try {
            image = ImageIO.read(new URL(args.get(0)));
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            int skipC = 1;
            if (args.size() == 2) {
                skipC = Integer.valueOf(args.get(1));
            }

            StringBuilder ascii = new StringBuilder("```");
            for (int x = 0; x < imgWidth; x += 2 * skipC) {
                for (int y = 0; y < imgHeight; y += skipC) {
                    ascii.append(convert(image.getRGB(x, y)));
                }
                ascii.append("\n");
            }

            if (ascii.length() > 1997)
                BotUtils.send(event.getChannel(), "Ascii art is > 2000 characters, and discord will shit on me. Use a higher skip count.\n" +
                        "$ascii [url], [increase this number]");
            else
                BotUtils.send(event.getChannel(), ascii.append("```").toString());

        } catch (IOException e) {
            BotUtils.send(event.getChannel(), "Invalid URL. Make sure it is directly linking a picture. Make sure the url ends in .png, .jpg, or any image file extension.");
        }
    }

    /**
     * Converts the RGB int value to a char, based on the amount of color in the pixel
     */
    private char convert(int value) {
        //Grab the three values for each red, green and blue (and alpha)
        int alpha = (value >> 24) & 0xFF;
        int red = (value >> 16) & 0xFF;
        int green = (value >> 8) & 0xFF;
        int blue = (value) & 0xFF;

        //Covert to a unified integer value between 0 and 26.
        //This is done by averaging, then dividing by 10 (RGB values range from 0 to 255)
        int darkness = ((int) ((0.21 * red) + (0.72 * green) + (0.07 * blue) / 3) / 10);

        //If alpha is completely clear, assume white
        if (alpha == 0)
            darkness = 26;

        //Output the respective char, grabbing it from the hashmap
        return asciiMap.get(darkness);
    }

    @Override
    public String getDesc() {
        return "AsciifyOld an art piece.";
    }
}
