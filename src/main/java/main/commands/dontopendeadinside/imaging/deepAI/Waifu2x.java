package main.commands.dontopendeadinside.imaging.deepAI;

import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class Waifu2x extends DeepAI{
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withTitle("Waifu2x").withImage(
                        fetchWaifu2x(event, args)
                ));
    }

    @Override
    public String getDesc() {
        return "Upscales an image using Waifu2x";
    }

    protected class Container {
        protected String output_url;
    }
}
