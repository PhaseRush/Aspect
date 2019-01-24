package main.commands.utilitycommands;

import main.Command;
import main.CommandManager;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.Executors;

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
        Runnable runCommand = () -> {
            if (cmd.canRun(event, targetParams)) {
                cmd.runCommand(event, targetParams);
            } else {
                BotUtils.send(event.getChannel(), "Error running target command");
            }
        };

        long start;

        if (cmd.requireSynchronous()) {
            String id = event.getGuild().getStringID();
            CommandManager.syncExecuteMap.putIfAbsent(id, Executors.newFixedThreadPool(1));
            start = System.nanoTime();
            CommandManager.syncExecuteMap.get(id).execute(runCommand);
        } else { // use default executor
            start = System.nanoTime();
            CommandManager.commandExecutors.execute(runCommand);
        }

        long end = System.nanoTime();

        BotUtils.send(event.getChannel(), "Elapsed time : " + (end - start)/1E6 + " ms");
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    public boolean requireSynchronous() {
        return false;
    }

    @Override
    public String getDesc() {
        return "times a command. syntax unix like";
    }
}
