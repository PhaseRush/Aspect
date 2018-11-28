package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.Random;

public class RollDice implements Command {
    Random r = new Random();
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String[] parseInput = args.get(0).split("d", 2);
        int numDice = Integer.valueOf(parseInput[0]);
        int numSides = Integer.valueOf(parseInput[1]);

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
                .withDesc(desc.toString());

        BotUtils.send(event.getChannel(), eb);

    }


    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "```\n$rollSingle x, y```\n where x is the number of sides per die, and y is the number of die. You may omit y, and it will default to one die:```\n$rollSingle x```";
    }
}
