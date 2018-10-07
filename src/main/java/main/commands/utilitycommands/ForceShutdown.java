package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ForceShutdown implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.size() != 1)
            BotUtils.sendMessage(event.getChannel(), "need to force");

        if (event.getAuthor().getStringID().equals("264213620026638336") && args.get(0).contains("-f")) {
            BotUtils.sendMessage(event.getChannel(), "Obeying force shutdown");
            System.gc();
            System.exit(9002);
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "plz dont do this to me";
    }
}
