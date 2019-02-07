package main.commands.utilitycommands.guild;

import main.Command;
import main.Main;
import main.utility.metautil.BotUtils;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DumpEmotes implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        Set<IEmoji> allEmojis = new HashSet<>();

        if (!args.isEmpty() && args.get(0).equals("all")) {
            Main.client.getGuilds().stream()
                    .map(IGuild::getEmojis)
                    .forEach(allEmojis::addAll);
        } else {
            allEmojis.addAll(event.getGuild().getEmojis());
        }

        StringBuilder message = new StringBuilder();
        StringBuilder log = new StringBuilder("Guild name, Emote name, Animated?, Image URL, String ID\n");

        for (IEmoji e : allEmojis) {
            // add to message
            message.append("<")
                    .append((e.isAnimated()? "a:" : ":"))
                    .append(e.getName()).append(":")
                    .append(e.getStringID())
                    .append("> ");

            // add to log
            log.append("\n")
                    .append(e.getGuild().getName()).append("\t")
                    .append(e.getName()).append("\t")
                    .append((e.isAnimated())).append("\t")
                    .append(e.getImageUrl()).append("\t")
                    .append(e.getStringID());
        }

        // do escape check
        //message = new StringBuilder(message.toString().replaceAll("_", "\\_"));


        try {
            BotUtils.send(event.getChannel(), BotUtils.makeHasteGetUrl(log.toString()));
        } catch (IOException ignored) {
        }

        if (message.length() > 2000) {
            List<EmbedBuilder> splitEmotes = splitEmotes(message);
            for (int i = 0; i < splitEmotes.size(); i++) {
                EmbedObject eb = splitEmotes.get(i).build();
                BotUtils.sendGet(event.getChannel(), eb);
                try {
                    BotUtils.sendGet(event.getChannel(), "Dump: "+ i  + BotUtils.makeHasteGetUrl(eb.description));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<EmbedBuilder> splitEmotes(StringBuilder msg) {
        List<EmbedBuilder> list = new ArrayList<>();

        while (msg.length() > 2000) {
            int cutIdx = getCutIndex(msg);
            String thisString = msg.substring(0, cutIdx);
            list.add(new EmbedBuilder().withDesc(thisString));
            msg.delete(0, cutIdx);
        }

        if (msg.length() > 0) list.add(new EmbedBuilder().withDesc(msg.toString()));

        return list;
    }

    private int getCutIndex(StringBuilder msg) {
        int idx = 2000;

        while(msg.charAt(idx) != ' ') idx--;

        return idx;
    }


    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (!BotUtils.isDev(event)) return args.isEmpty();
        return true;
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
