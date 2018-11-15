package main.commands.music;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;

import static main.utility.music.MusicUtils.customUrls;

public class CustomQueues implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder("```\n");

        for (Map.Entry e : customUrls.entrySet())
            sb.append(e.getKey() + " : " + e.getValue()).append("\n");

        BotUtils.sendMessage(event.getChannel(), sb.toString());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
