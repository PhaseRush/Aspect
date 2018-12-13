package main.commands.nasa;

import main.Command;
import main.utility.BotUtils;
import main.utility.nasa.APOD;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class NasaApod implements Command {
    private final String nasaLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/NASA_logo.svg/928px-NASA_logo.svg.png";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getJson("https://api.nasa.gov/planetary/apod?api_key=" + BotUtils.NASA_API);
        APOD apod = BotUtils.gson.fromJson(json, APOD.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withAuthorName("Nasa :: Astronomy Picture of the Day")
                .withAuthorIcon(nasaLogoUrl)
                .withAuthorUrl("https://apod.nasa.gov/apod/astropix.html")
                .withTitle(apod.getTitle())
                .withDesc(apod.getExplanation())
                .withImage(apod.getHdurl())
                .withFooterText((apod.getCopyright()==null? "NASA" : apod.getCopyright()) + ", " + apod.getDate());

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDescription() {
        return "NASA - shows Astronomy Picture of the Day";
    }
}
