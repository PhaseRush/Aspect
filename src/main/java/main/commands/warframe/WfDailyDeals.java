package main.commands.warframe;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import main.utility.warframe.dynamic.DailyDeal;
import main.utility.warframe.dynamic.WorldState;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class WfDailyDeals implements Command {


    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String fullWorldState = BotUtils.getStringFromUrl("http://content.warframe.com/dynamic/worldState.php");
        WorldState worldState = BotUtils.gson.fromJson(fullWorldState, WorldState.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withDesc("Warframe Daily Deals")
                .withColor(Visuals.getVibrantColor());

        DailyDeal[] dd = worldState.getDailyDeals();

        for (DailyDeal deal : dd) {
            String[] splitName = deal.getStoreItem().split("/");
            eb.appendField(splitName[splitName.length - 1], deal.getOriginalPrice() + " :fast_forward: " + deal.getSalePrice() + ", Discount: " + deal.getDiscount() + "%", false);
        }

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "warframe daily deals";
    }
}
