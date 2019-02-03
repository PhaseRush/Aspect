package main.commands.utilitycommands;

import main.Command;
import main.CommandManager;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!args.isEmpty()) {
            if (CommandManager.commandMap.keySet().contains(args.get(0))) {
                Command cmd = CommandManager.commandMap.get(args.get(0));
                if (!cmd.getDesc().equals(BotUtils.GITHUB_URL)){
                    BotUtils.send(event.getAuthor().getOrCreatePMChannel(), "Info for `" + args.get(0) + "`\n" + cmd.getDesc());
                }
            }
        }


        BotUtils.send(event.getAuthor().getOrCreatePMChannel(), "Please visit this for more details: " + BotUtils.GITHUB_URL);
        BotUtils.send(event.getChannel(), "Details have been PM'd");
    }

    @Override
    public String getDesc() {
        return "Details at: " + BotUtils.GITHUB_URL;
    }
}