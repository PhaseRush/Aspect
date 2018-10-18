package main.commands.fortnite.deprecatedFortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.fortnite.deprecatedfortniteutil.shop.CatalogEntry;
import main.utility.fortnite.deprecatedfortniteutil.shop.FortniteShopJson;
import main.utility.fortnite.deprecatedfortniteutil.shop.Prices;
import main.utility.fortnite.deprecatedfortniteutil.shop.Storefront;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class FortniteShopDetail implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String jsonOut = BotUtils.getJson("https://fortnite.y3n.co/v2/shop", true);
        FortniteShopJson shop = new Gson().fromJson(jsonOut, FortniteShopJson.class);
        Storefront selected = shop.getStorefronts()[Integer.valueOf(args.get(0)) - 1];
        BotUtils.sendMessage(event.getChannel(), "Selected Storefront: " + selected.getName());


        EmbedBuilder eb = new EmbedBuilder()
                .withDesc("***Fortnite Item Shop***")
                .withColor(Visuals.getVibrantColor())
                .withFooterText("Expires: " + shop.getExpiration().substring(10));

        for (CatalogEntry entry : selected.getCatalogEntries()) {
            Prices prices = entry.getPrices()[0];
            eb.appendField((entry.getTitle() == null ? "placeholder title" : entry.getTitle()), entry.getDescription() + "\n" + prices.getRegularPrice() + " -> " + prices.getFinalPrice() + "(" + prices.getCurrencyType() + ")", false);
        }

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "DEPRECATED - returns detailed infomation about a fortnite shop category";
    }
}
