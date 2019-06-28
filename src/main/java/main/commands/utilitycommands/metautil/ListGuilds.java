package main.commands.utilitycommands.metautil;

import main.Aspect;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

import java.util.List;
import java.util.stream.Collectors;

public class ListGuilds implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                Aspect.client.getGuilds().stream()
                        .map(IGuild::getName)
                        .sorted()
                        .collect(Collectors.joining("\n"))
        );
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }
}
