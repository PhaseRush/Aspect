package main;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    void runCommand(MessageReceivedEvent event, List<String> args);

    boolean requiresElevation();

    String getDescription();
    //String getSyntax();

}
