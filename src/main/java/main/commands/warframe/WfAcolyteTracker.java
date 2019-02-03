package main.commands.warframe;

import com.google.gson.reflect.TypeToken;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import main.utility.warframe.wfstatus.acolytes.WarframeAcolyte;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class WfAcolyteTracker implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/persistentEnemies");
        Type listOfAcolyteType = new TypeToken<List<WarframeAcolyte>>() {
        }.getType();
        List<WarframeAcolyte> acolytes = BotUtils.gson.fromJson(json, listOfAcolyteType);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe | Acolytes")
                .withColor(Visuals.getVibrantColor());

        for (WarframeAcolyte a : acolytes) {
            String hp = String.valueOf(a.getHealthPercent() * 100).substring(0, 4) + "%";
            eb.appendField(a.getAgentType() + "\t | Lv. " + a.getRank() + "\t | Hp: " + hp, (a.isDiscovered() ? a.getLastDiscoveredAt() : "Not yet discovered"), false);
        }

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "info about acolytes";
    }
}
