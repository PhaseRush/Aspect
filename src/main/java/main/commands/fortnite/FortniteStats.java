package main.commands.fortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.StatsFactory;
import main.utility.fortnite.fornitetracker.FortniteTrackerJsonObj;
import main.utility.fortnite.fornitetracker.stats.FortniteTrackerPx;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class FortniteStats implements Command {
    private static Gson gson = new Gson();
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String platform = "pc";
        String ign = args.get(0);

        if (args.size() == 2)
            platform = args.get(1);


        String json = BotUtils.getStringFromUrl("https://api.fortnitetracker.com/v1/profile/" + platform  + "/" + ign,
                "TRN-Api-Key", BotUtils.FORTNITE_API_KEY);
        FortniteTrackerJsonObj stats = gson.fromJson(json, FortniteTrackerJsonObj.class);


        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Fortnite Stats: " + BotUtils.capitalizeFirst(ign) + " : " + platform)
                .withUrl("https://fortnitetracker.com/profile/" + platform + "/" + ign)
                //.withThumbnail("https://vignette.wikia.nocookie.net/fortnite/images/5/54/Icon_Monthly_VIP_Badge.png/revision/latest?cb=20170806011009")
                .withDesc(generateDesc(stats))
                .withFooterText("(*) indicates an estimated value; click title for more details");

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String generateDesc(FortniteTrackerJsonObj stats) {
        int width = 53;
        StringBuilder sb = initHeader(new StringBuilder("```\n"), width);

        FortniteTrackerPx solo = stats.getStats().getCurr_p2();
        FortniteTrackerPx duo = stats.getStats().getCurr_p10();
        FortniteTrackerPx squad = stats.getStats().getCurr_p9();

        //rating
        sb.append(generateRow("Rating", solo.getTrnRating().getDisplayValue(), duo.getTrnRating().getDisplayValue(), squad.getTrnRating().getDisplayValue())).append("\n");

        //percentile
        String duoPercentile = String.valueOf(duo.getTrnRating().getPercentile());
        if (duo.getTrnRating().getPercentile() == 0) {
            double totalPlayers = calcTotalPlayers(stats);
            duoPercentile = "*" + String.valueOf(100 * duo.getTrnRating().getRank() / totalPlayers);
        }
        sb.append(generateRow("Top %", String.valueOf(solo.getTrnRating().getPercentile()), (duoPercentile.length()>5? duoPercentile.substring(0,5): duoPercentile), String.valueOf(squad.getTrnRating().getPercentile()))).append("\n");

        sb.append("\n");
        //win count
        sb.append(StatsFactory.generateRow("Wins", solo.getTop1().getValue(), duo.getTop1().getValue(), squad.getTop1().getValue(),width)).append("\n");

        //win %StatsFactory.
        sb.append(StatsFactory.generateRow("Win %", solo.getWinRatio().getValue(), duo.getWinRatio().getValue(), squad.getWinRatio().getValue(),width)).append("\n");

        //StatsFactory.
        sb.append(StatsFactory.generateRow("Kills",solo.getKills().getValue(),  duo.getKills().getValue(), squad.getKills().getValue(),width)).append("\n");

        //kills/StatsFactory.tch
        sb.append(StatsFactory.generateRow("Kills/Game", solo.getKpg().getValue(), duo.getKpg().getValue(), squad.getKpg().getValue(),width)).append("\n");

        //kd StatsFactory.
        sb.append(StatsFactory.generateRow("K/D ratio", solo.getKd().getValue(), duo.getKd().getValue(), squad.getKd().getValue(),width)).append("\n");
        //Score/StatsFactory.tch
        sb.append(StatsFactory.generateRow("Score/Game", solo.getScorePerMatch().getValue(), duo.getScorePerMatch().getValue(), squad.getScorePerMatch().getValue(),width)).append("\n");

        return sb.append("```").toString();
    }

    private double calcTotalPlayers(FortniteTrackerJsonObj stats) {
        int rank = stats.getStats().getCurr_p10().getScore().getRank();
        double percentile = stats.getStats().getCurr_p10().getScore().getPercentile()/100;
        return rank/percentile;
    }

    /**
     * Actually format the f-ing rows correctly
     */
    private StringBuilder generateRow(String category, String soloVal, String duoVal, String squadVal) {
        int totalWidth = 45;
        int catLen = category.length();
        int firstE = 19;
        int secondE = 31;
        int thirdE = 45;

        StringBuilder sb = new StringBuilder(category);

        //fill with blanks
        for (int i = catLen; i < totalWidth; i++) sb.append(" ");

        //replace each
        sb.replace(firstE-soloVal.length(),firstE, soloVal);
        sb.replace(secondE-duoVal.length(),secondE,duoVal);
        sb.replace(thirdE-squadVal.length(),thirdE, squadVal);

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
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Fortnite stats. specify platform on 2nd parameter.";
    }
}
