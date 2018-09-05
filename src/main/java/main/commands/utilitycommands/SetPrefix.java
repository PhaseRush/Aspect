package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SetPrefix implements Command {
    private final String mapFilePath = "C:\\Users\\Positron\\IdeaProjects\\Aspect\\txtfiles\\GuildCommandPrefixMap.txt";
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String pre = args.get(0);

        if(BotUtils.getPrefix(event.getGuild()).equals(pre)) {
            BotUtils.sendMessage(event.getChannel(), "The prefix is already \"" + pre + "\"");
        } else {
            BotUtils.setPrefix(event.getGuild(), pre);
            BotUtils.savePrefixMap();
            BotUtils.sendMessage(event.getChannel(), "Prefix has been updated from \"" + BotUtils.DEFAULT_BOT_PREFIX + "\" to \"" + pre + "\"");
        }


//
//        if (BotUtils.DEFAULT_BOT_PREFIX.equals(pre)) {
//            BotUtils.sendMessage(event.getChannel(), "The prefix is already \"" + pre + "\"");
//        } else {
//            BotUtils.sendMessage(event.getChannel(), "Prefix has been updated from \"" + BotUtils.DEFAULT_BOT_PREFIX + "\" to \"" + pre + "\"");
//            BotUtils.DEFAULT_BOT_PREFIX = args.get(0);
//        }
    }

    @Override
    public boolean requiresElevation() {
        return true;
    }
}
