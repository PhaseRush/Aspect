package main.commands.humor.cute;

import main.Command;
import main.utility.BotUtils;
import main.utility.humorUtil.CuteUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ListCuties implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder("```\n");
        for (String cuteKey : CuteUtil.cuteUrls.keySet())
            sb.append(cuteKey).append("\n");

        BotUtils.send(event.getChannel(), sb.append("```").toString());
    }

    @Override
    public String getDescription() {
        return null;
    }
}
