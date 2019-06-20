package main.commands.league;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Skin;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Comparator;
import java.util.List;

public class SkinDetail implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String champName = BotUtils.autoCorrectChampName(BotUtils.capitalizeFirstLowerRest(args.get(0)));

        if (args.get(1).equals("all")) {
            showAllSkins(Champion.named(champName).get(), event.getChannel());
        } else {
            showSkin(Champion.named(champName).get(), args, event.getChannel());
        }

    }

    private void showAllSkins(Champion champion, IChannel channel) {
        champion.getSkins().stream()
                .sorted(Comparator.comparingInt((Skin::getNumber)))
                .forEachOrdered(skin -> BotUtils.sendGet(channel,
                        new EmbedBuilder()
                                .withTitle(skin.getNumber() + " : " + skin.getName())
                                .withImage(skin.getSplashImageURL())
                                .withColor(Visuals.analyzeImageColor(Visuals.urlToBufferedImage(skin.getSplashImageURL())))));
    }

    private void showSkin(Champion champion, List<String> args, IChannel channel) {
        Skin skin;

        if (StringUtils.isNumeric(args.get(1))) {
            skin = champion.getSkins().get(Integer.valueOf(args.get(1)));
        } else
            skin = champion.getSkins().find(x -> x.getName().equals(args.get(1)));

        if ((skin == null)) {
            BotUtils.send(channel, "Error getting skin information. Find a number with `$allskins " + champion.getName() + "` or check your spelling");
            return;
        }

        String skinUrl = skin.getSplashImageURL();

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(skin.getName())
                .withColor(Visuals.analyzeWeightedImageColor(Visuals.urlToBufferedImage(skinUrl), 4))
                .withImage(skinUrl);

        BotUtils.send(channel, eb);
    }

    @Override
    public String getDesc() {
        return "League - Displays image of skin";
    }
}
