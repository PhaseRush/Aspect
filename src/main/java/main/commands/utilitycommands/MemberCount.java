package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class MemberCount implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        int memberCount = event.getGuild().getUsers().size();

        BotUtils.sendMessage(event.getChannel(), "There are currently " +
                memberCount + " members in " +
                event.getGuild().getName());
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Counts total members in this server";
    }
}
