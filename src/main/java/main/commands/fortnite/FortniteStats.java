package main.commands.fortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.fortnite.fornitetracker.FortniteTrackerJsonObj;
import main.utility.fortnite.fornitetracker.stats.FortniteTrackerPx;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

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
        FortniteTrackerJsonObj stats = new Gson().fromJson(json, FortniteTrackerJsonObj.class);


        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Fortnite Stats: " + ign + " : " + platform)
                .withUrl("https://fortnitetracker.com/profile/" + platform + "/" + ign)
                //.withThumbnail("https://vignette.wikia.nocookie.net/fortnite/images/5/54/Icon_Monthly_VIP_Badge.png/revision/latest?cb=20170806011009")
                .withDesc(generateRanking(stats));

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String generateRanking(FortniteTrackerJsonObj stats) {
        int width = 53;
        StringBuilder sb = initHeader(new StringBuilder("```\n"), width);

        FortniteTrackerPx solo = stats.getStats().getCurr_p2();
        FortniteTrackerPx duo = stats.getStats().getCurr_p10();
        FortniteTrackerPx squad = stats.getStats().getCurr_p9();

        //rating
        sb.append(generateRow("Rating", solo.getTrnRating().getDisplayValue(), duo.getTrnRating().getDisplayValue(), squad.getTrnRating().getDisplayValue())).append("\n");

        //percentile
        sb.append(generateRow("Top %", String.valueOf(solo.getTrnRating().getPercentile()), String.valueOf(duo.getTrnRating().getPercentile()), String.valueOf(squad.getTrnRating().getPercentile()))).append("\n");

        sb.append("\n");
        //win count
        sb.append(generateRow("Wins", solo.getTop1().getValue(), duo.getTop1().getValue(), squad.getTop1().getValue())).append("\n");

        //win %
        sb.append(generateRow("Win %", solo.getWinRatio().getValue(), duo.getWinRatio().getValue(), squad.getWinRatio().getValue())).append("\n");

        //kills
        sb.append(generateRow("Kills",solo.getKills().getValue(),  duo.getKills().getValue(), squad.getKills().getValue())).append("\n");

        //kills/match
        sb.append(generateRow("Kills/Game", solo.getKpg().getValue(), duo.getKpg().getValue(), squad.getKpg().getValue())).append("\n");

        //kd ratio
        sb.append(generateRow("K/D ratio", solo.getKd().getValue(), duo.getKd().getValue(), squad.getKd().getValue())).append("\n");

        //Score/match
        sb.append(generateRow("Score/Game", solo.getScorePerMatch().getValue(), duo.getScorePerMatch().getValue(), squad.getScorePerMatch().getValue())).append("\n");

        return sb.append("```").toString();
    }

    /**
     * Actually format the f-ing rows correctly
     */
    private StringBuilder generateRow(String category, String soloVal, String duoVal, String squadVal) {
        int totalLength = 45;
        int catLen = category.length();
        int firstS = 13;
        int firstE = 19;
        int secondS = 25;
        int secondE = 31;
        int thirdS = 39;
        int thirdE = 45;

        StringBuilder sb = new StringBuilder(category);

        //fill with blanks
        for (int i = catLen; i < totalLength; i++) sb.append(" ");

        //replace each
        sb.replace(firstE-soloVal.length(),firstE, soloVal);
        sb.replace(secondE-duoVal.length(),secondE,duoVal);
        sb.replace(thirdE-squadVal.length(),thirdE,squadVal);

        return sb;
    }

    private StringBuilder initHeader(StringBuilder sb, int width) {
        for (int i = 0; i < width; i++) //ehh we'll see
            sb.append(" ");
        sb.append("\n");
        for (int i = 0; i < 14; i++)
            sb.append(" ");
        sb.append("SOLOS");
        for (int i = 0; i < 8; i++)
            sb.append(" ");
        sb.append("DUOS");
        for (int i = 0; i < 8; i++)
            sb.append(" ");
        sb.append("SQUADS");
        return sb.append("\n");
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Fortnite stats. specify platform on 2nd parameter.";
    }
}
