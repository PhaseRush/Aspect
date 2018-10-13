package main.commands.warframe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.warframe.wfstatus.WarframeVoidFissure;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class WfVoidFissures implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/fissures");
        Type fissureListType = new TypeToken<LinkedList<WarframeVoidFissure>>() {
        }.getType();
        LinkedList<WarframeVoidFissure> fissures = new Gson().fromJson(json, fissureListType);

        fissures.removeIf(fissure -> fissure.isExpired());


        //make embed response
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe Void Fissures")
                .withColor(Visuals.getVibrantColor());

        //filter based on relic type (ie meso)
        if (args.size() == 1) {
            String relicName = BotUtils.capitalizeFirst(args.get(0).toLowerCase()); //tolowercase first to make sure all else lower
            fissures.removeIf(fissure -> !fissure.getTier().equals(relicName));
            eb.withTitle("Warframe | " + relicName + " Void Fissures");
            if (fissures.size() == 0) {
                BotUtils.sendMessage(event.getChannel(), "There are no matching relic types for " + relicName);
                return; //don't run the embed
            }
        }


        for (WarframeVoidFissure fissure : fissures) {
            eb.appendField(fissure.getNode() + " | " + fissure.getMissionType() + " | " + fissure.getEnemy() + " | " + fissure.getEta() + " remaining",
                    fissure.getTier() + " tier " + fissure.getTierNum(), false);
        }

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Lists current void fissures. Able to filter based on input. ```$wfvoid lith```";
    }
}
