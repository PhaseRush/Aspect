package main.commands.humor.cute;

import main.Command;
import main.utility.BotUtils;
import main.utility.humorUtil.CuteUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class CuteImg implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (CuteUtil.banned.contains(event.getAuthor().getStringID())) return; // put this guy on a watchlist
        try {
            String url = CuteUtil.cuteUrls.get("~"+args.get(0)); // fixed for new string formatting
            if (url.startsWith("~"))
                BotUtils.send(event.getChannel(), new EmbedBuilder().withImage(url));
            else BotUtils.send(event.getChannel(), url);
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "Cuteness doesn't exist!");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Displays a cute Image. Use `listcute` to get a list of valid cuteness";
    }


    public String helpMsg() {
        return null;
    }
}
