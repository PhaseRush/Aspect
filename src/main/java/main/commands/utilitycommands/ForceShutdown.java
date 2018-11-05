package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import main.utility.state_json.MasterJsonUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ForceShutdown implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!event.getAuthor().getStringID().equals("264213620026638336")) {
            BotUtils.sendMessage(event.getChannel(), "no u.");
            return;
        }

       // dump MasterState json
        boolean force = args.size() > 0 & args.get(0).contains("-f");
        try {
            MasterJsonUtil.writeState();
        } catch (Exception e) {
            BotUtils.sendMessage(event.getChannel(), "Error writing MasterState json.\n" +
                    (force? "Forcing shutdown" : "Use `-f` to force."));
        } finally {
            if (force) {
                BotUtils.sendMessage(event.getChannel(), "");
                System.gc();
                System.exit(9002);
            }
        }
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "plz dont do this to me";
    }
}
