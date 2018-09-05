package main.commands.warframe;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.warframe.wfstatus.WarframeCetusTimeObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeCetusCycle implements Command {
    String[] dayActivities = {
            "Go mining",
            "GO do bounties",
            "Go catch some Tralok.",
            "Go catch some Mawfish."};
    String[] nightActivities = {
            "Go hunt some eidolons.",
            "Go catch some Cuthol.",
            "Go catch some Glappid."
    };

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/cetusCycle");
        WarframeCetusTimeObject cetus = new Gson().fromJson(json, WarframeCetusTimeObject.class);
        boolean isDay = cetus.isDay();
        BotUtils.sendMessage(event.getChannel(), "It is currently " +
                (isDay ? "day. It will be night in " + cetus.getTimeLeft() + "\n" + BotUtils.getRandomFromStringArray(dayActivities) :
                        "night. It will be day in " + cetus.getTimeLeft() + "\n" + BotUtils.getRandomFromStringArray(nightActivities)));
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
