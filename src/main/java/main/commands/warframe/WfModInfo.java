package main.commands.warframe;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfModInfo implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String modName = args.get(0);
        String json = BotUtils.getStringFromUrl("https://api.warframestats.us/mods/search/" + modName);

        System.out.println(json);

    }

    @Override
    public String getDesc() {
        return null;
    }
}
