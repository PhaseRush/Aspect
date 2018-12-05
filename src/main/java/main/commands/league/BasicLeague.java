package main.commands.league;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class BasicLeague implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        System.out.println(args.get(0));
        Summoner summoner = Orianna.summonerNamed(args.get(0)).get();

        BotUtils.send(event.getChannel(), summoner.getName() + " is level " + summoner.getLevel() + " on the " + summoner.getRegion() + " server");
    }



    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Shows summoner level";
    }

    private void testing() {
        Summoner summoner = Orianna.summonerNamed("Danman96").get();

    }

}
