package main.commands.dontopendeadinside.games;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinFlip implements Command {
    private static Random r = ThreadLocalRandom.current();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        flip(event);
    }

    public static void flip(MessageReceivedEvent event) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withImage(
                        (r.nextBoolean()?
                                "https://upload.wikimedia.org/wikipedia/commons/1/18/2017-W-100-American-Liberty-225th-Anniversary-Gold-Coin-Obverse.jpg" :
                                "https://upload.wikimedia.org/wikipedia/commons/c/ca/2017-W-100-American-Liberty-225th-Anniversary-Gold-Coin-Reverse.jpg")
                ));
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }

    @Override
    public Status mayRun(MessageReceivedEvent event, List<String> args) {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
