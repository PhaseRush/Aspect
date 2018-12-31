package main.commands.utilitycommands.guild;

import main.Command;
import main.utility.BotUtils;
import main.utility.gist.GistUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class UsersToGist implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.gson.toJson(event.getGuild().getUsers());
        BotUtils.send(event.getChannel(), GistUtils.makeGistGetUrl("Users Snapshot for " + event.getGuild().getName(), BotUtils.getTodayYYYYMMDD(), json));
    }


    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return event.getAuthor().getStringID().equals(BotUtils.DEV_DISCORD_STRING_ID);
    }
}
