package main.commands.dontopendeadinside;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * rolls dice
 *
 * added more robust error catching
 */
public class RollDice implements Command {
    private Random r = new Random();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String[] parseInput;
        int numDice, numSides;

        try {
            parseInput = args.get(0).split("d", 2);
            numDice = Integer.valueOf(parseInput[0]);
            numSides = Integer.valueOf(parseInput[1]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            BotUtils.send(event.getChannel(), getDesc());
            return;
        }

        // thanks jejeirs
        if (numDice < 1 || numSides < 1) {
            BotUtils.send(event.getChannel(), getDesc());
            return;
        }

        StringBuilder desc = new StringBuilder("Total: ");

        int total = 0;
        for (int i = 0; i < numDice; i++) {
            int rand = r.nextInt(numSides) + 1;

            if (i == 0) desc.append(rand);
            else desc.append(" + ").append(rand);

            if (i != 0 && i % 10 == 0) desc.append("\n"); //new line every 10 numbers (except first)

            total += rand;
        }
        desc.insert(7, total + "\n"); //offset "Total: "

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(numDice + "d" + numSides)
                .withDesc(BotUtils.limitStrLen(desc.toString(), 1500, true, true, '+'));

        BotUtils.send(event.getChannel(), eb);


        if (desc.length() > 1500) {
            try {
                String hasteUrl = BotUtils.makeHasteGetUrl(numDice + "d" + numSides + " roll for " + BotUtils.getNickOrDefault(event)
                        + "\n\n" + desc.toString());

                BotUtils.send(event.getChannel(), "To see all the rolls, visit\n" + hasteUrl);
            } catch (IOException ignored) {
            }
        }
    }

    // could be 100% cpu problem
    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return args.size() == 1;
    }

    @Override
    public String getDesc() {
        return "```\n$roll `x`d`y````\n where x is the number of dice to be rolled, and y is the number of faces per die.\n Rolling 2 6 sided die (ex. for a game of monopoly): ```\n$roll 2d6";
    }
}
