package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import main.utility.gist.GistUtils;
import main.utility.gist.gist_json.GistContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

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
            GistContainer gist = GistUtils.makeGistGetObj(
                    numDice + "d" + numSides + "roll for " + BotUtils.getNickOrDefault(event),
                    "Total: " + total,
                    desc.toString());
            BotUtils.send(event.getChannel(), "To see all the rolls, visit\n" + gist.getHtml_url());
        }
    }

    @Override
    public String getDesc() {
        return "```\n$roll `x`d`y````\n where x is the number of dice to be rolled, and y is the number of faces per die.\n Rolling 2 6 sided die (ex. for a game of monopoly): ```\n$roll 2d6";
    }
}
