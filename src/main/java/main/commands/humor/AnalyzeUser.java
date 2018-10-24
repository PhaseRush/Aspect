package main.commands.humor;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeUser implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IUser targetUser = event.getClient().getUserByID(Long.valueOf(args.get(0).substring(2, 20)));

        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        int totalMsgCount = 0;
        int totalMsgWithCapitalization = 0;

        for (IChannel textChannel : event.getGuild().getChannels()) {
            for (IMessage m : textChannel.getFullMessageHistory()) {
                String fullMessage = m.getContent();
                String[] spaceSplit = fullMessage.split(" ");


                if (fullMessage.substring(0, 1).matches("^[^a-z]"))
                    totalMsgWithCapitalization++;
                totalMsgCount++;
            }

        }

    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "WIP - Analyses message history of user";
    }
}
