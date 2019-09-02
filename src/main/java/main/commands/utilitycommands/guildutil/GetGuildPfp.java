package main.commands.utilitycommands.guildutil;

import main.Aspect;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class GetGuildPfp implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            if (!args.isEmpty()) {
                BotUtils.send(event.getChannel(), Aspect.client.getGuildByID(Long.parseLong(args.get(0))).getIconURL());
            }
            BotUtils.send(event.getChannel(), event.getGuild().getIconURL());
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), e.getLocalizedMessage());
        }
    }

    @Override
    public String getDesc() {
        return "returns link to current guild's profile picture";
    }
}
