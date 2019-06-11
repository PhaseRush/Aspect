package main.commands.dontopendeadinside.games;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.Random;

public class CoinFlip implements Command {
    private static Random r = new Random();
    private static final String HEADS_URL = "https://upload.wikimedia.org/wikipedia/commons/1/18/2017-W-100-American-Liberty-225th-Anniversary-Gold-Coin-Obverse.jpg";
    private static final String TAILS_URL = "https://upload.wikimedia.org/wikipedia/commons/c/ca/2017-W-100-American-Liberty-225th-Anniversary-Gold-Coin-Reverse.jpg";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        flip(event);
    }

    public static void flip(MessageReceivedEvent event) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder()
                        .withImage((r.nextBoolean() ?
                                HEADS_URL :
                                TAILS_URL)
                        ));
    }

    @Override
    public String getDesc() {
        return "Flips a coin and shows an image of either heads or tails";
    }
}
