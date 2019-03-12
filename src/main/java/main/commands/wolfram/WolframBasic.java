package main.commands.wolfram;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.wolfram.WolframUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class WolframBasic implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        boolean formatVerticalBar = args.size() == 2;

        EmbedBuilder eb;
        try {
            eb = WolframUtil.runQuery(BotUtils.concatArgs(args), formatVerticalBar, false);
        } catch (Exception e) {
            eb = WolframUtil.runQuery(BotUtils.concatArgs(args), false, true);
            eb.appendDesc("Defaulting to advanced query");
        }

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "Wolfram Alpha - Basic query with to-the-point replies.";
    }
}
