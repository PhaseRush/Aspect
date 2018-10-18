package main.commands.fortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.fortnite.fornitetracker.FortniteTrackerJsonObj;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class FortniteStats implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String platform = "pc";
        String ign = args.get(0);

        if (args.size() == 2)
            platform = args.get(1);


        String json = BotUtils.getStringFromUrl("https://api.fortnitetracker.com/v1/profile/" + platform  + "/" + ign,
                "TRN-Api-Key", BotUtils.FORTNITE_API_KEY);
        //System.out.println(json);

        FortniteTrackerJsonObj fortniteStats = new Gson().fromJson(json, FortniteTrackerJsonObj.class);

        System.out.println(fortniteStats);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
