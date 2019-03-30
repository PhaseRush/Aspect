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

public class Haruwu implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(
                event.getChannel(),
                new EmbedBuilder().withImage("attachment://overlay_pfp.png"),
                new ByteArrayInputStream(
                        Visuals.buffImgToOutputStream(
                                overlay(Visuals.urlToBufferedImage(DeepAI.fetchWaifu2x(event, args)),
                                        determineText(args)),
                                "png"
                        ).toByteArray()),
                "overlay_pfp.png"
        );
    }

    private BufferedImage overlay(BufferedImage old, String text) {
        BufferedImage img = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0,0, null);
        g2d.setFont(Visuals.Fonts.MONTSERRAT.getFont().deriveFont(20f));

        Visuals.optimizeGraphics2dForText(g2d);

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

    private String determineText(List<String> args) {
        if (args.size() < 2) return "uwu";
        else return args.get(1);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event) || event.getAuthor().getStringID().equals("292350757691457537");
    }

    @Override
    public String getDesc() {
        return "overlays text over target's upscale pfp, or uses \"uwu\" if no text is provided";
    }
}
