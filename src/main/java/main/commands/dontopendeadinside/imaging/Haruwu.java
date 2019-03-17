package main.commands.dontopendeadinside.imaging;

import main.Command;
import main.commands.dontopendeadinside.imaging.deepAI.DeepAI;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

public class Haruwu implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String url = DeepAI.fetchWaifu2x(event, args);
        BufferedImage img = Visuals.urlToBufferedImage(url);

        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withImage("attachment://overlay_pfp.png"),
                new ByteArrayInputStream(
                        Objects.requireNonNull(Visuals.buffImgToOutputStream(
                                overlay(img, "uwu"),
                                "png"
                        )).toByteArray()
                ),
                "overlay_pfp.png"
        );
    }


    private BufferedImage overlay(BufferedImage old, String text) {
        BufferedImage img = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0,0, null);
        g2d.setFont(new Font("Roboto", Font.PLAIN, 20));

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.setColor(Color.BLACK);

        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textHeight = g2d.getFont().getSize();
        int currX = 0, currY = 0;

        while (currY < img.getHeight()) {
            while (currX < img.getWidth()) {
                g2d.drawString(text, currX, currY);
                currX += textWidth * 1.5;
            }
            currX = 0; // reset by align left
            currY += textHeight * 1.5;
        }

        g2d.dispose();
        return img;
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
