package main.commands.warframe;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import main.utility.warframe.wfstatus.voidTrader.WarframeVoidTrader;
import main.utility.warframe.wfstatus.voidTrader.WarframeVoidTraderInventoryObj;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class WfBaro implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/voidTrader");
        WarframeVoidTrader trader = BotUtils.gson.fromJson(json, WarframeVoidTrader.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe | Void Trader " + trader.getCharacter())
                .withColor(Visuals.getRandVibrantColour());

        if (!trader.isActive()) {
            eb.appendField(trader.getCharacter() + " is currently not active.", "Arrival: " + trader.getStartString() + " at " + trader.getLocation(), false);
        } else {
            for (WarframeVoidTraderInventoryObj o : trader.getInventory()) {
                eb.appendField(o.getItem(), "Ducats: " + o.getDucats() + " | Credits: " + o.getCredits(), false);
            }
        }
        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "displays info about void trader";
    }
}
