package main.commands.warframe;

import main.Command;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfAlerts implements Command {
    //private final String nodesJSON = "https://raw.githubusercontent.com/WFCD/warframe-worldstate-data/master/data/solNodes.json";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), WarframeUtil.generateAlertsEmbed());
    }

    @Override
    public String getDesc() {
        return "Displays all alerts";
    }
}
