package main.commands.warframe;

import main.Command;
import main.utility.BotUtils;
import main.utility.warframe.WarframeUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeAlerts implements Command {
    //private final String nodesJSON = "https://raw.githubusercontent.com/WFCD/warframe-worldstate-data/master/data/solNodes.json";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.sendMessage(event.getChannel(), WarframeUtil.generateAlertsEmbed());
    }


    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Displays all alerts";
    }
}
