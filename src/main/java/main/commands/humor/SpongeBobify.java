package main.commands.humor;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpongeBobify implements Command {
    private Random r = ThreadLocalRandom.current();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        StringBuilder sb = new StringBuilder(BotUtils.getNickOrDefault(event) + ": ");

        args.forEach(str -> sb.append(str.toLowerCase()).append(" "));

        for (int i = 0; i < sb.length(); i++) {
            if (isValid(sb.charAt(i))) {
                if (r.nextBoolean()) {
                    sb.setCharAt(i, (char) (sb.charAt(i) - 32));
                }
            }
        }

        try { // try to edit the original message
            event.getMessage().edit(sb.toString());
        } catch (MissingPermissionsException e) { // else default to sending new message
            event.getMessage().delete();
            BotUtils.send(event.getChannel(), sb.toString());
        }
    }

    private boolean isValid(char c) {
        return c > 96 && c < 123;
    }

    @Override
    public String getDesc() {
        return "maximizes sarcasm.";
    }
}
