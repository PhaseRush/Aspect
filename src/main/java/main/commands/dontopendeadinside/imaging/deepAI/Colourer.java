package main.commands.dontopendeadinside.imaging.deepAI;

import com.google.gson.JsonSyntaxException;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class Colourer extends DeepAI {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String targetUrl = getTargetUrl(event, args);
        String json = fetchJson("https://api.deepai.org/api/colorizer", targetUrl);

        try {
            String colouredUrl = BotUtils.gson.fromJson(json, Container.class).output_url;
            BotUtils.send(event.getChannel(), new EmbedBuilder().withTitle("Colourized").withImage(colouredUrl));
        }catch (JsonSyntaxException e) {
            BotUtils.send(event.getChannel(), "Failed to parse url; try another image");
        }
    }

    @Override
    public String getDesc() {
        return "Adds colour to a black and white image";
    }

    private class Container {
        private String output_url;
    }
}
