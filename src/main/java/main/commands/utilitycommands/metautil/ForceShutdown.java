package main.commands.utilitycommands.metautil;

import main.Command;
import main.CommandManager;
import main.utility.BotUtils;
import main.utility.state_json.MasterJsonUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ForceShutdown implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
       // dump MasterState json
        boolean force = args.size() > 0 & args.get(0).contains("-f");
        try {
            MasterJsonUtil.writeState();
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "Error writing MasterState json.\n" +
                    (force? "Forcing shutdown" : "Use `-f` to force."));
        } finally {
            if (force) {
                //BotUtils.send(event.getChannel(), "");
                CommandManager.commandExecutors.shutdown(); //not sure if needed?
                System.gc();
                System.exit(9002);
            }
        }
    }

    /**
     * only run for me.
     * @param event
     * @param args
     * @return
     */
    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return event.getAuthor().getStringID().equals("264213620026638336");
    }

    @Override
    public String getDesc() {
        return "plz dont do this to me";
    }
}
