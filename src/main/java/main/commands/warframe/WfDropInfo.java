package main.commands.warframe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.warframe.wfstatus.WarframeDropInfoEntity;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class WfDropInfo implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String parsedUserInput = args.get(0).replaceAll("\\s", "%20");
        String url = "https://api.warframestat.us/drops/search/" + parsedUserInput;
        String json = BotUtils.getStringFromUrl(url);

        Type listOfDropInfo = new TypeToken<List<WarframeDropInfoEntity>>() {
        }.getType();
        List<WarframeDropInfoEntity> drops = new Gson().fromJson(json, listOfDropInfo);
        drops.sort((o1, o2) -> Float.compare(o2.getChance(), o1.getChance()));


        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe | " + args.get(0))
                .withUrl("http://warframe.wikia.com/wiki/" + args.get(0).replaceAll("\\s", "_"))
                .withColor(Visuals.getVibrantColor());

        if (drops.size() > 10) { //clear extra large lists
            eb.withDesc("Showing first 10 entries out of " + drops.size());
            drops.subList(10, drops.size()).clear();
        }
        for (WarframeDropInfoEntity e : drops) {
            eb.appendField(e.getPlace(), e.getChance() + "%", false);
        }

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "info about material drops";
    }
}
