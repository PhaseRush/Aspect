package main.commands.dontopendeadinside.imaging.deepAI;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

/**
 * something wrong with api
 */
public class DeepDream extends DeepAI implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String targetUrl = getTargetUrl(event, args);
        String json = fetchJson("https://api.deepai.org/api/deepdream", targetUrl);

        String deepenedUrl = BotUtils.gson.fromJson(json, Container.class).output_url;
        BotUtils.send(event.getChannel(), new EmbedBuilder().withTitle(this.getClass().getSimpleName()).withImage(deepenedUrl));
    }

    @Override
    public String getDesc() {
        return "Exaggerates feature attributes or textures using information that the bvlc_goolenet model learned during training.";
    }
    private class Container {
        private String output_url;
    }
}
