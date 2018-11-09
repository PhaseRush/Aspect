package main.commands.league;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Skin;
import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ListSkins implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        String champName = args.get(0);

        if(Character.isLowerCase(champName.charAt(0))) {
            champName = champName.substring(0,1).toUpperCase() + champName.substring(1,champName.length());
        }

        Champion champion = Champion.named(champName).get();
        if ((champion == null)){
            BotUtils.sendMessage(event.getChannel(), "Failed to get Champion");
            return;
        }

        sb.append("*"+champion.getName() + " has the following skins:*\n");

        List<Skin> skins = champion.getSkins();
        for (int i = 0; i < champion.getSkins().size(); i++) {
            sb.append(i + ": " + skins.get(i).getName() + "\n");
        }

        BotUtils.sendMessage(event.getChannel(), sb.toString());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Lists skins for a champion";
    }
}
