package main.commands.utilitycommands.guild;

import main.Command;
import main.Main;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.List;

public class DumpEmotes implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        List<IEmoji> allEmojis = new ArrayList<>();

        if (!args.isEmpty() && args.get(0).equals("all")) {
            Main.client.getGuilds().stream()
                    .map(IGuild::getEmojis)
                    .forEach(allEmojis::addAll);
        } else {
            allEmojis.addAll(event.getGuild().getEmojis());
        }

        StringBuilder sb = new StringBuilder();

        for (IEmoji e : allEmojis) {
            sb.append("<")
                    .append((e.isAnimated()? "a:" : ":"))
                    .append("a:")
                    .append(e.getStringID())
                    .append("> ");
        }
        BotUtils.send(event.getChannel(), sb.toString());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event);
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "dumps all emotes which this bot can access";
    }
}
