package main.commands.wolfram;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.wolfram.WolframUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class WolframAdvanced implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        boolean formatVerticalBar = args.size() == 2;

        EmbedBuilder eb = WolframUtil.runQuery(args.get(0), formatVerticalBar, true);

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "Wolfram Alpha - Advanced query with full results.";
    }
}
