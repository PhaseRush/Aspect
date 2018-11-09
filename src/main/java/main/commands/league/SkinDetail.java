package main.commands.league;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Skin;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class SkinDetail implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String champName = args.get(0);

        if(Character.isLowerCase(champName.charAt(0))) {
            champName = champName.substring(0,1).toUpperCase() + champName.substring(1,champName.length());
        }
        Champion champion = Champion.named(champName).get();
        Skin skin = null;

        if(StringUtils.isNumeric(args.get(1))) {
            skin = champion.getSkins().get(Integer.valueOf(args.get(1)));
        }else
            skin = champion.getSkins().find(x -> x.getName().equals(args.get(1)));

        if ((skin == null)){
            BotUtils.sendMessage(event.getChannel(), "Error getting skin information");
            return;
        }

        String skinUrl = skin.getSplashImageURL();

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(skin.getName())
                .withColor(Visuals.analyizeWeightedImageColor(Visuals.urlToBufferedImage(skinUrl), 4))
                .withImage(skinUrl);

        event.getChannel().sendMessage(eb.build());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "League - Displays image of skin";
    }
}
