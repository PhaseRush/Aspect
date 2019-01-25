package main.commands.utilitycommands;

import main.Command;
import main.CommandManager;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class CommandTimer implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String targetCmd = BotUtils.cmdSpellCorrect(args.get(0));
        if (targetCmd == null) {
            BotUtils.send(event.getChannel(), "No command found. (No command within a levenshtein distance of 2)");
            return;
        }
        Command cmd = CommandManager.commandMap.get(targetCmd);
        List<String> targetParams = args.subList(1, args.size());

        if (!cmd.canRun(event, targetParams)) {
            BotUtils.send(event.getChannel(), "Error running target command");
            return;
        }

        long start = System.nanoTime();
        cmd.runCommand(event, targetParams);
        long end = System.nanoTime();
        double durationMillis = (end - start)/1E6;

        BotUtils.send(event.getChannel(),
                "Runtime for `" + targetCmd +  "/" +cmd.getClass().getName() + "` : " +
                        (durationMillis < 1000?
                                durationMillis + " ms" :
                                BotUtils.millisToMS((long)durationMillis)
                        ));
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    /*
    do not run more than one timer command at any time
     */
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "times runtime for a command. syntax unix like";
    }
}
