package main.commands.utilitycommands;

import main.Command;
import main.CommandManager;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.isEmpty()) {
            BotUtils.send(event.getAuthor().getOrCreatePMChannel(), "Here's a link with a bunch of info: " + BotUtils.GITHUB_URL);
            BotUtils.send(event.getChannel(), "Details have been PM'd");
            return;
        }

        String targetCmd = BotUtils.cmdSpellCorrect(args.get(0));
        if (targetCmd == null) {
            BotUtils.send(event.getChannel(), "Your target command doesnt match any alias - DM'd a list of all commands");
            BotUtils.send(event.getAuthor().getOrCreatePMChannel(), String.join("\n", CommandManager.commandMap.keySet()));
            return;
        }
        BotUtils.send(event.getChannel(),
                CommandManager.commandMap.get(targetCmd).getDesc());
    }

    @Override
    public String getDesc() {
        return "Gives a list of all commands, or the description of a single command";
    }
}