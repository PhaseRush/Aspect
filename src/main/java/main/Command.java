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

    default String getDesc() {
        return BotUtils.GITHUB_URL;
    }

    default String getSyntax() {
        return BotUtils.GITHUB_URL;
    }

    default boolean correctable() {
        return true;
    }

//    default String getFailReason(MessageReceivedEvent event, List<String> args) {
//        return BotUtils.GITHUB_URL;
//    }
}