package main.commands.warframe;

import main.Command;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfOrbVallisCycle implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), WarframeUtil.orbVallisCycleString());
    }

    @Override
    public String getDesc() {
        return "Warframe : return the current state of the Orb Vallis";
    }
}
