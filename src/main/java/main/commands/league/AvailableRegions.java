package main.commands.league;

import com.merakianalytics.orianna.types.common.Region;
import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableRegions implements Command {
    public static Map<String, Region> regionMap = new HashMap<>();

    public AvailableRegions() {
        regionMap.put("na ", Region.NORTH_AMERICA);
        regionMap.put("euw", Region.EUROPE_WEST);
        regionMap.put("kr ", Region.KOREA);
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        sb.append("All available regions:\n```");

        for (String s : regionMap.keySet()) {
            sb.append(s + " : " + regionMap.get(s).name() + "\n");
        }
        sb.append("```");

        BotUtils.send(event.getChannel(), sb.toString());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Lists supported regions";
    }
}
