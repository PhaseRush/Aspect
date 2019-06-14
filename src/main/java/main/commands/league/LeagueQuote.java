package main.commands.league;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class LeagueQuote implements Command {
    private final static List<String> allChampNames = new ArrayList<>(BotUtils.leagueQuoteMap.keySet());

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.isEmpty()) {
            BotUtils.send(event.getChannel(), BotUtils.getRandLeagueQuoteEmbed());
        } else {
            BotUtils.send(event.getChannel(),
                    BotUtils.getLeagueQuoteForChamp(BotUtils.autoCorrect(args.get(0), allChampNames)));
        }
    }

    @Override
    public String getDesc() {
        return "sends a quote from a random champion, or the champion specified\nNote: if there is no exact match, will use closest lexigraphically";
    }

    @Override
    public String getSyntax() {
        return "$lolquote\n$lolquote katarina";
    }
}
