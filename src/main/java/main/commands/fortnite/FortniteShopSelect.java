package main.commands.fortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.fortnite.shop.FortniteShopJson;
import main.utility.fortnite.shop.Storefront;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class FortniteShopSelect implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        String jsonOut = BotUtils.getJson("https://fortnite.y3n.co/v2/shop", true);
        FortniteShopJson shop = new Gson().fromJson(jsonOut, FortniteShopJson.class);

        StringBuilder sb = new StringBuilder()
                .append("***Fortnite Shop Picker***\n\n Select from one of the following by using\n```" + BotUtils.DEFAULT_BOT_PREFIX + "fnselect [category]```\n```\n");

        Storefront[] storefronts = shop.getStorefronts();
        for (int i = 0; i < storefronts.length; i++) {
            Storefront storefront = storefronts[i];
            sb.append((i + 1) + ((i + 1) < 10 ? ".\t " : ".\t") + storefront.getName() + "\n");
        }

        BotUtils.sendMessage(event.getChannel(), sb.append("```").toString());
    }


    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "DEPRECATED - Fortnite - lists shops";
    }
}
