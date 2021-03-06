package main.commands.league;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import main.Aspect;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class BasicLeague implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        Aspect.LOG.info(args.get(0));
        Summoner summoner = Orianna.summonerNamed(args.get(0)).get();

        BotUtils.send(event.getChannel(), summoner.getName() + " is level " + summoner.getLevel() + " on the " + summoner.getRegion() + " server");
    }

    @Override
    public String getDesc() {
        return "Shows summoner level";
    }
}
