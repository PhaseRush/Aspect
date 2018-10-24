package main.commands.fortnite.deprecatedFortnite;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser.FGameType;
import main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser.FPlayer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class FortniteStats implements Command {
    private String[] questions = {"Likes sushi", "Thinks chairs are for sitting", "Actually stays awake in class", "Has petted Kat before", "\"nerf singed\"", "Eats olives for a living", "Drops loot lake when kat wants to"};

    //!fn druddigod, all
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getJson("https://fortnite.y3n.co/v2/player/" + args.get(0).replaceAll(",", ""), true);
        handleFortniteStatsEmbed(json, args.get(1), event.getChannel());
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Temporarily Deprecated - Displays Fortnite stats for a player. ```$fn AmperianLoop, squad```";
    }

    private void handleFortniteStatsEmbed(String json, String gameType, IChannel channel) {
        //'solo', 'duo', 'squad', 'all'
        FPlayer fp = new Gson().fromJson(json, FPlayer.class);
        EmbedBuilder eb = new EmbedBuilder();
        FGameType fGameType = null;
        try {
            fGameType = fp.getBattleRoyal().getStats().getPc().getSolo(); //defaults to solo
        } catch (Exception e) {
            channel.sendMessage("Something went very wrong. If you typed your name correctly, tell kat to fix her shit");
            return; //dont do anything else if this failed.
        }

        switch (gameType) {
            case "duo":
                fGameType = fp.getBattleRoyal().getStats().getPc().getDuo();
                break;
            case "squad":
                fGameType = fp.getBattleRoyal().getStats().getPc().getSquad();
                break;
            case "all":
                fGameType = fp.getBattleRoyal().getStats().getPc().getAll();
                break;
        }

        int minutesPlayed = (fGameType.getMinutesPlayed() == 0? (int) fGameType.getKpm() * fGameType.getKills(): fGameType.getMinutesPlayed());

        eb.withAuthorName(fp.getDisplayName() + ", " + gameType/* + ", level " + fp.getBattleRoyal().getProfile().getLevel()*/)
                .withColor(Visuals.getVibrantColor())
                .withDesc("*FortniteStats Statistical Analysis*")
                .appendField("Kills", String.valueOf(fGameType.getKills()), true)
                .appendField("Kpm", String.valueOf(fGameType.getKpm()), true)
                .appendField("Kpd", String.valueOf(fGameType.getKpd()), true)
                //.appendField("-", "-", false)
                .appendField("Matches Played", String.valueOf(fGameType.getMatchesPlayed()), true)
                .appendField("Minutes Played", String.valueOf(minutesPlayed), true)
                //.appendField("-", "-", false)
                .appendField("Wins", String.valueOf(fGameType.getWins()), true)
                .appendField("Winrate %", String.valueOf(fGameType.getWinrate()), true)
                .appendField("Score", String.valueOf(fGameType.getScore()), true)
                .appendField(BotUtils.getRandomFromStringArray(questions), (new Random().nextBoolean() ? "yes" : "no"), true)
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("This information was last updated " + fp.getLastUpdate());

        OffsetDateTime updateTime = OffsetDateTime.parse(fp.getLastUpdate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        RequestBuffer.request(() -> {
            channel.sendMessage(eb.withFooterText("Updated " + updateTime.format(formatter) + " :3").build());
        });
    }
}
