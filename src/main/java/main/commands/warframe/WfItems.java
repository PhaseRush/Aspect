package main.commands.warframe;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.warframe.market.item.WarframeItem;
import main.utility.warframe.market.item.WarframeItemPayloadContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WfItems implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframe.market/v1/items");

        WarframeItemPayloadContainer obj = BotUtils.gson.fromJson(json, WarframeItemPayloadContainer.class);

        for (WarframeItem i : obj.getPayload().getItems().getEn()) {
            System.out.println(i.getItem_name());
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }

    @Override
    public String getDesc() {
        return "dev use";
    }
}
