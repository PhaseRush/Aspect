package main;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    void runCommand(MessageReceivedEvent event, List<String> args);

    default boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    String getDescription();
    //String getSyntax();
}