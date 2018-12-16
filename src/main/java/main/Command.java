package main;

import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    void runCommand(MessageReceivedEvent event, List<String> args);

    default boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    default boolean requireSynchronous(){
        return false;
    }

    default String getDescription() {
        return BotUtils.GITHUB_URL;
    }
    //String getSyntax();
}