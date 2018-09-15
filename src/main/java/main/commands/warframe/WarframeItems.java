package main.commands.warframe;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.warframe.market.item.WarframeItem;
import main.utility.warframe.market.item.WarframeItemPayloadContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeItems implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframe.market/v1/items");

        WarframeItemPayloadContainer obj = new Gson().fromJson(json, WarframeItemPayloadContainer.class);

        for (WarframeItem i : obj.getPayload().getItems().getEn()) {
            System.out.println(i.getItem_name());
        }

    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "dev use";
    }
}
