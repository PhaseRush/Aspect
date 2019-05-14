package main.commands.league;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

public class RandomQuote implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        //BotUtils.send(event.getChannel(), BotUtils.getLeagueQuoteEmbed());
        BotUtils.send(event.getChannel(), "Paused.");

        StringBuilder sb = new StringBuilder("Dumps for #white_bags\n");

        final int[] count = new int[1];
        event.getChannel().getFullMessageHistory().stream()
                .filter(message -> BotUtils.numPictures(message) == 0)
                .forEach(toDel -> {
                    count[0] ++;
                    sb.append(String.format("[%s] : [%s] : [%s] : %s",
                            BotUtils.getNickOrDefault(toDel.getAuthor(), event.getGuild()),
                            toDel.getTimestamp().atZone(ZoneId.of("America/Los_Angeles")).toString(),
                            toDel.getStringID(),
                            toDel.getContent()))
                            .append("\n");
                });

        try {
            BotUtils.send(event.getChannel(),
                    BotUtils.makeHasteGetUrl(sb.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            BotUtils.send(event.getChannel(), "number of non images: " + count[0]);
        }

        System.out.println("paused");
    }

    @Override
    public String getDesc() {
        return "Displays a random quote from League of Legends";
    }
}
