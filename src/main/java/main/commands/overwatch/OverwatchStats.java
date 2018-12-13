package main.commands.overwatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Command;
import main.utility.BotUtils;
import main.utility.StatsFactory;
import main.utility.gsonModifiers.IntTypeAdaptor;
import main.utility.overwatch.json.OWGames;
import main.utility.overwatch.json.OWStats;
import main.utility.overwatch.json.OverwatchJsonObj;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class OverwatchStats implements Command {
    //need custom Gson parser
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(int.class, new IntTypeAdaptor())
            .registerTypeAdapter(Integer.class, new IntTypeAdaptor()).create();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String ign, identifier;
        try {
            ign = args.get(0);
            identifier = args.get(1);
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "Usage:```\n$ow [ign],[identifier]```");
            return;
        }

        String json = BotUtils.getStringFromUrl("https://ow-api.com/v1/stats/pc/us/" + ign + "-" + identifier + "/profile");
        OverwatchJsonObj stats = gson.fromJson(json, OverwatchJsonObj.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Aspect - Basic Overwatch Profile")
                //.withThumbnail(stats.getIcon()) @todo
                .withUrl("https://www.overbuff.com/players/pc/"+ ign + "-" + identifier)
                .withDesc(generateDesc(stats));

        BotUtils.send(event.getChannel(), eb);
    }

    private String generateDesc(OverwatchJsonObj stats) {
        int width = 45;
        StringBuilder sb = new StringBuilder(stats.getName() + " is level " + stats.getLevel() + " with " + stats.getGamesWon() + " wins.");
        initHeader(sb.append("```\n"), width);

        OWStats comp = stats.getCompetitiveStats();
        OWStats quick = stats.getQuickPlayStats();


        //wins
        sb.append(StatsFactory.generateRow("Wins", comp.getGames().getWon(), quick.getGames().getWon(), width)).append("\n");

        //win rate
        double compWR, quickWR;
        OWGames compGame = comp.getGames(), quickGame = quick.getGames();
        if (compGame.getPlayed() == 0) compWR = -1;
        else compWR = (double)compGame.getWon()/(double)compGame.getPlayed();
        if (quick.getGames().getPlayed() == 0) quickWR = -1;
        else quickWR = (double)quickGame.getWon()/(double)quickGame.getPlayed();

        sb.append(StatsFactory.generateRow("Winrate", compWR, quickWR, width)).append("\n");

        //avg elims
        sb.append(StatsFactory.generateRow("Avg elims", comp.getEliminationsAvg(), quick.getEliminationsAvg(), width)).append("\n");

        //avg damage
        sb.append(StatsFactory.generateRow("Avg dmg", comp.getDamageDoneAvg(), quick.getDamageDoneAvg(), width)).append("\n");

        //avg deaths
        sb.append(StatsFactory.generateRow("Avg deaths", comp.getDeathsAvg(), quick.getDeathsAvg(), width)).append("\n");

        //avg healing
        sb.append(StatsFactory.generateRow("Avg healing", comp.getHealingDoneAvg(), quick.getHealingDoneAvg(), width)).append("\n");

        //avg obj kills
        sb.append(StatsFactory.generateRow("Avg obj kills", comp.getObjectiveKillsAvg(), quick.getObjectiveKillsAvg(), width)).append("\n");

        //avg obj time
        sb.append(StatsFactory.generateRow("Avg obj time", comp.getObjectiveTimeAvg(), quick.getObjectiveTimeAvg(), width)).append("\n");

        return sb.append("```").toString();
    }

    private StringBuilder initHeader(StringBuilder sb, int width) {
        for (int i = 0; i < width; i++) //ehh we'll see
            sb.append(" ");
        sb.append("\n");
        for (int i = 0; i < 14; i++)
            sb.append(" ");
        sb.append("COMPETITIVE");
        for (int i = 0; i < 8; i++)
            sb.append(" ");
        sb.append("CASUAL");
        return sb.append("\n");
    }

    @Override
    public String getDescription() {
        return "Ships as-is. All public apis are non-usable (extremely outdated)";
    }
}
