package main.commands.warframe;

import main.Command;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfCetusCycle implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), WarframeUtil.cetusCycleString());
    }

    @Override
    public String getDesc() {
        return "Warframe - Current Cetus cycle. (PC only)";
    }
}
