package main.commands.utilitycommands;

import main.Command;
import main.utility.TranslateUtils;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * see TranslateBot for impl.
 * CUZ IM NOT DOING THIS AGAIN REEEEE
 */
public class Translate implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder inputString = new StringBuilder();
        for (String s : args)
            inputString.append(s + ", ");

        BotUtils.send(event.getChannel(), TranslateUtils.transEnFr(inputString.toString().substring(0, inputString.lastIndexOf(","))));

    }

    @Override
    public String getDesc() {
        return "WIP - translate input";
    }
}
