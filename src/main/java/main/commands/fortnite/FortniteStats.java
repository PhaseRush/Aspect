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
                .withDesc(generateRanking(stats));

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String generateRanking(FortniteTrackerJsonObj stats) {
        int width = 53;
        StringBuilder sb = initHeader(new StringBuilder("```\n"), width);

        FortniteTrackerPx solo = stats.getStats().getCurr_p2();
        FortniteTrackerPx duo = stats.getStats().getCurr_p10();
        FortniteTrackerPx squad = stats.getStats().getCurr_p9();

        //win count
        //sb.append("Wins :\t\t" + solo.getTop1().getValue() + "\t\t" + duo.getTop1().getValue() + "\t\t" + squad.getTop1().getValue() +"\n");
        sb.append(generateRow("Wins", solo.getTop1().getValue(), duo.getTop1().getValue(), squad.getTop1().getValue())).append("\n");

        //win %
        sb.append("Win %:\t\t" + solo.getWinRatio().getValue() + "\t\t" + duo.getWinRatio().getValue() + "\t\t" + squad.getWinRatio().getValue() +"\n");

        //kills
        sb.append("Kills:\t\t" + solo.getKills().getValue() + "\t\t" + duo.getKills().getValue() + "\t\t" + squad.getKills().getValue() +"\n");

        //kills/match
        sb.append("Kills/Game:\t" + solo.getKpg().getValue() + "\t\t" + duo.getKpg().getValue() + "\t\t" + squad.getKpg().getValue() +"\n");

        //kd ratio
        sb.append("K/D ratio:\t" + solo.getKd().getValue() + "\t\t" + duo.getKd().getValue() + "\t\t" + squad.getKd().getValue() +"\n");

        //Score/match
        sb.append("Score/Game:\t" + solo.getScorePerMatch().getValue() + "\t" + duo.getScorePerMatch().getValue() + "\t\t" + squad.getScorePerMatch().getValue() +"\n");





        return sb.append("```").toString();
    }

    /**
     * Actually format the f-ing rows correctly
     */
    private StringBuilder generateRow(String category, String soloVal, String duoVal, String squadVal) {
        int totalLength = 45;
        int firstS = 13;
        int firstE = 19;
        int secondS = 25;
        int secondE = 31;
        int thirdS = 39;
        int thirdE = 45;


        StringBuilder sb = new StringBuilder(category);
        for (int i = category.length(); i < totalLength; i++) sb.append(" ");


        return null;
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
