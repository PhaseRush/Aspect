package main.commands.music;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.Map;

import static main.utility.music.MusicUtils.customUrls;

public class CustomQueues implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder("\n");

        for (Map.Entry e : customUrls.entrySet()) {
            sb.append("["+e.getKey() + "](" + e.getValue()).append(")\n");
        }

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Preconfigured Playlists")
                .withDesc(sb.toString())
                .withColor(Visuals.getRandVibrandColour());

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "Returns a list of custom queues that are supported by the bot. Try `$play [queue name]`";
    }
}
