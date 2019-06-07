package main.commands.warframe;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import main.utility.warframe.WarframeLoadout;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class WfRandomLoadout implements Command {

    private static String[] warframes = Arrays.stream(
            BotUtils.gson.fromJson(
                    BotUtils.getStringFromUrl("https://raw.githubusercontent.com/South-Paw/warframe-item-list/master/data/json/Warframe.json"),
                    WarframeLoadout[].class))
            .map(WarframeLoadout::getName)
            .toArray(String[]::new);

    private static String[] primaries = Arrays.stream(
            BotUtils.gson.fromJson(
                    BotUtils.getStringFromUrl("https://raw.githubusercontent.com/South-Paw/warframe-item-list/master/data/json/Primary.json"),
                    WarframeLoadout[].class))
            .map(WarframeLoadout::getName)
            .toArray(String[]::new);

    private static String[] secondaries = Arrays.stream(
            BotUtils.gson.fromJson(
                    BotUtils.getStringFromUrl("https://raw.githubusercontent.com/South-Paw/warframe-item-list/master/data/json/Secondary.json"),
                    WarframeLoadout[].class))
            .map(WarframeLoadout::getName)
            .toArray(String[]::new);

    private static String[] melees = Arrays.stream(
            BotUtils.gson.fromJson(
                    BotUtils.getStringFromUrl("https://raw.githubusercontent.com/South-Paw/warframe-item-list/master/data/json/Melee.json"),
                    WarframeLoadout[].class))
            .map(WarframeLoadout::getName)
            .toArray(String[]::new);


    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(
                event.getChannel(),
                new EmbedBuilder()
                        .withTitle("Warframe :: Random Loadout")
                        .withColor(Visuals.getRandVibrantColour())
                        .withDesc("```" +
                                "\nW : " + BotUtils.getRandomFromArrayString(warframes) +
                                "\nP : " + BotUtils.getRandomFromArrayString(primaries) +
                                "\nS : " + BotUtils.getRandomFromArrayString(secondaries) +
                                "\nM : " + BotUtils.getRandomFromArrayString(melees) + "```"
                        )
        );
    }

    @Override
    public String getDesc() {
        return "Generates a random loadout";
    }
}
