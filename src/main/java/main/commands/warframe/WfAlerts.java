package main.commands.warframe;

import main.Command;
import main.utility.BotUtils;
import main.utility.WarframeUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfAlerts implements Command {
    //private final String nodesJSON = "https://raw.githubusercontent.com/WFCD/warframe-worldstate-data/master/data/solNodes.json";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), WarframeUtil.generateAlertsEmbed());
    }


    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Displays all alerts";
    }
}
