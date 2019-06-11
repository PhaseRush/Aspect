package main.commands.league;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class LeagueQuote implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), BotUtils.getLeagueQuoteEmbed());
    }

    @Override
    public String getDesc() {
        return "sends a quote from a random champion";
    }

    @Override
    public String getSyntax() {
        return "$lolquotemap";
    }
}
