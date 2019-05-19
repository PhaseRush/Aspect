package main.commands.dontopendeadinside.games;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.util.*;

public class WouldYourRather implements Command {
    private final String API_URL = "https://www.rrrather.com/botapi";
    // use Set because Collections#disjoint is asymptotically faster (although it really doesnt matter since its size 3)
    private final Set<String> blackListCategories = new HashSet<>(Arrays.asList("rape", "gross", "sex"));

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        WYRObj wyr;

        do {
            wyr = BotUtils.gson.fromJson(BotUtils.getStringFromUrl(API_URL), WYRObj.class);
        } while (!Collections.disjoint(
                Arrays.asList(wyr.tags.split(",")),
                blackListCategories) &&
                (event.getChannel().isNSFW() || !wyr.nsfw));

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Would you rather...")
                .withColor(Visuals.getRandVibrandColour())
                .withFooterText(wyr.link)

                .withDesc(wyr.title + "\n\n" +
                        ":regional_indicator_a:\t" + wyr.choicea +
                        "\n:regional_indicator_b:\t" + wyr.choiceb);
        IMessage msg = BotUtils.sendGet(event.getChannel(), eb);
        BotUtils.reactAllEmojis(msg, BotUtils.getRegionals().subList(0,2));
    }


    private class WYRObj {
        private String title;
        private String choicea;
        private String choiceb;
        private String link;
        private String tags;
        private boolean explaination;
        private int votes;
        private boolean nsfw;
    }
}