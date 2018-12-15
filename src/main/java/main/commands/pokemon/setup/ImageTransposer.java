package main.commands.pokemon.setup;

import main.Command;
import main.Main;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageTransposer implements Command {
    private String windowsPath = "C:\\Users\\leozh\\Desktop\\pokemons\\";
    private String linuxPath = "/home/positron/pokemons/";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!event.getAuthor().getStringID().equals("264213620026638336")) return;
        IChannel channel = Main.client.getChannelByID(500523363102359552L);
        List<IMessage> allMsg = channel.getFullMessageHistory();
        for (IMessage msg : allMsg) {
            String withoutDomain = msg.getFormattedContent().substring(40);
            int indexOpenBracket = withoutDomain.indexOf("(");
            String name = withoutDomain.substring(0, indexOpenBracket-1);
            String url = msg.getEmbeds().get(0).getThumbnail().getUrl();

            try /*(FileOutputStream outputStream = new FileOutputStream(windowsPath + name + ".png"))*/{
                //byte[] image = Visuals.urlToImageByteArray(embed.getThumbnail().getUrl());
                BufferedImage image = Visuals.urlToBufferedImageWithAgentHeader(url);
                File file = new File(windowsPath + name + ".png");
                ImageIO.write(image, "png", file);
                //outputStream.write(image);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
