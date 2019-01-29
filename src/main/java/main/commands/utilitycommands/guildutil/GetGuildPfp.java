package main.commands.utilitycommands.guildutil;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class GetGuildPfp implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), event.getGuild().getIconURL());
    }

    @Override
    public String getDesc() {
        return null;
    }
}
