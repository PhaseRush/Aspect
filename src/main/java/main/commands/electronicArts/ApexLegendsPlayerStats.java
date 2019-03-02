package main.commands.electronicArts;

import main.Command;
import main.utility.Visuals;
import main.utility.apexlegends.ALPlayerInfo;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class ApexLegendsPlayerStats implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String ign = args.get(0);
        String json1 = BotUtils.getStringFromUrl("https://public-api.tracker.gg/apex/v1/standard/profile/"+getTrackerPlatform(args)+"/"+ign,
                "TRN-Api-Key",
                "af122ede-81ca-43a0-bb5c-ccf4b0fd8c1b");

        ALPlayerInfo.ALPlayerData player = BotUtils.gson.fromJson(json1, ALPlayerInfo.class).getData();

        String json2 = BotUtils.getStringFromUrl("https://apextab.com/api/search.php?platform="+getIconUrlPlatform(args) +"&search=" + ign);
        IconEncap.IconResult iconResult = BotUtils.gson.fromJson(json2, IconEncap.class).results[0];

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Apex Legends")

                .withThumbnail(iconResult.avatar)
                .withColor(Visuals.analyizeImageColor(Visuals.urlToBufferedImage(iconResult.avatar)))

                .withDesc(generateDesc(player))
                .withFooterText("");


        BotUtils.send(event.getChannel(), eb);
    }

    private String generateDesc(ALPlayerInfo.ALPlayerData player) {
        return null;
    }

    private class IconEncap {
        int totalresults;
        IconResult[] results;

        private class IconResult {
            String avatar;
        }
    }

    private String getIconUrlPlatform(List<String> args) {
        String platform = "pc";
        if (args.size() >= 2) {
            String userPlat = args.get(1);
            if (userPlat.toLowerCase().trim().equals("xbox")) platform = "xbl";
            else if (userPlat.toLowerCase().trim().startsWith("ps")) platform = "psn";
        }
        return platform;
    }

    private String getTrackerPlatform(List<String> args) {
        String platform = "5";
        if (args.size() >= 2) {
            String userPlat = args.get(1);
            if (userPlat.toLowerCase().trim().equals("xbox")) platform = "1";
            else if (userPlat.toLowerCase().trim().startsWith("ps")) platform = "2";
        }
        return platform;
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    public String getDesc() {
        return "Apex Legends : shows player stats";
    }
}
