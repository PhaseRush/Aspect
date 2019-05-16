package main.commands.league;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class RandomQuote implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        //BotUtils.send(event.getChannel(), BotUtils.getLeagueQuoteEmbed());
        BotUtils.send(event.getChannel(), "Paused.");

        StringBuilder sb = new StringBuilder("Dumps for #white_bags\n");

        event.getChannel().getFullMessageHistory().stream()
                .filter(message -> BotUtils.numPictures(message) == 0)
                .forEach(toDel -> RequestBuffer.request(toDel::delete).get());

        System.out.println("paused");
    }

    @Override
    public String getDesc() {
        return "Displays a random quote from League of Legends";
    }
}
