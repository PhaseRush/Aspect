package main.commands.fortnite.fortnitev3;

import com.google.gson.JsonObject;
import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import main.Command;
import main.utility.TableUtil;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class FortniteStats implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String uuid = BotUtils.gson.fromJson(BotUtils.getStringFromUrl(
                "https://fortnite-api.theapinetwork.com/users/id?username=" + args.get(0),
                "Authorization",
                BotUtils.FORTNITE_V2
        ), JsonObject.class)
                .get("data")
                .getAsJsonObject()
                .get("uid")
                .getAsString();


        Wrapper obj = BotUtils.gson.fromJson(
                BotUtils.getStringFromUrl("https://fortnite-api.theapinetwork.com/prod09/users/public/br_stats?user_id=" + uuid + "&platform=pc",
                        "Authorization",
                        BotUtils.FORTNITE_V2
                ), Wrapper.class
        );

        GameModeStats s = new GameModeStats(obj.stats, args.size() == 2 ? args.get(1) : "all");

        GridTable table = GridTable.of(1, 2)
                .put(0, 0, Cell.of("Kills", "Wins", "Top 10", "Top 25", "Matches", "KDR", "WR", "Score", "Minutes"))
                .put(0, 1, Cell.of(s(s.kills), s(s.placetop1), s(s.placetop10), s(s.placetop25), s(s.matchesplayed), s(s.kd), s(s.winrate), s(s.score), s(s.minutesplayed)));

        table = Border.DOUBLE_LINE.apply(table);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Fortnite Stats v2 [experimental]")
                .withColor(Visuals.getRandVibrantColour())
                .withDesc("```js\n" + TableUtil.render(table).toString() + "```")
                .withFooterText("Last updated " + Date.from(Instant.ofEpochSecond(s.lastmodified)));

        BotUtils.send(event.getChannel(), eb);
    }

    public String s(Number n) {
        return String.valueOf(n);
    }

    class GameModeStats {
        int kills;
        int placetop1;
        int placetop10;
        int placetop25;
        int matchesplayed;
        float kd;
        float winrate;
        float score;
        int minutesplayed;
        long lastmodified;

        GameModeStats(Wrapper.Stats s, String mode) {
            switch (mode) {
                case "all":  // merge everything
                    this.kills = s.kills_solo + s.kills_duo + s.kills_squad;
                    this.placetop1 = s.placetop1_solo + s.placetop1_duo + s.placetop1_squad;
                    this.placetop10 = s.placetop10_solo + s.placetop10_duo + s.placetop10_squad;
                    this.placetop25 = s.placetop25_solo + s.placetop25_duo + s.placetop25_squad;
                    this.matchesplayed = s.matchesplayed_solo + s.matchesplayed_duo + s.matchesplayed_squad;

                    float soloWeight = ((float) s.kills_solo / kills);
                    float duoWeight = ((float) s.kills_duo / kills);
                    float squadWeight = ((float) s.kills_squad / kills);
                    this.kd = s.kd_solo * soloWeight +
                            s.kd_duo * duoWeight +
                            s.kd_squad * squadWeight;
                    this.winrate = s.winrate_solo * soloWeight +
                            s.winrate_duo * duoWeight +
                            s.winrate_squad * squadWeight;
                    this.score = s.score_solo + s.score_duo + s.score_squad;
                    this.minutesplayed = s.minutesplayed_solo + s.minutesplayed_duo + s.minutesplayed_squad;
                    this.lastmodified = Math.max(Math.max(s.lastmodified_solo, s.lastmodified_duo), s.lastmodified_squad);
                    break;
                case "solo":
                    System.out.println(s.kills_solo);
                    this.kills = s.kills_solo;
                    this.placetop1 = s.placetop1_solo;
                    this.placetop10 = s.placetop10_solo;
                    this.placetop25 = s.placetop25_solo;
                    this.matchesplayed = s.matchesplayed_solo;
                    this.kd = s.kd_solo;
                    this.winrate = s.winrate_solo;
                    this.score = s.score_solo;
                    this.minutesplayed = s.minutesplayed_solo;
                    this.lastmodified = s.lastmodified_solo;
                    break;
                case "duo":
                    this.kills = s.kills_duo;
                    this.placetop1 = s.placetop1_duo;
                    this.placetop10 = s.placetop10_duo;
                    this.placetop25 = s.placetop25_duo;
                    this.matchesplayed = s.matchesplayed_duo;
                    this.kd = s.kd_duo;
                    this.winrate = s.winrate_duo;
                    this.score = s.score_duo;
                    this.minutesplayed = s.minutesplayed_duo;
                    this.lastmodified = s.lastmodified_duo;
                    break;
                case "squad":
                    this.kills = s.kills_squad;
                    this.placetop1 = s.placetop1_squad;
                    this.placetop10 = s.placetop10_squad;
                    this.placetop25 = s.placetop25_squad;
                    this.matchesplayed = s.matchesplayed_squad;
                    this.kd = s.kd_squad;
                    this.winrate = s.winrate_squad;
                    this.score = s.score_squad;
                    this.minutesplayed = s.minutesplayed_squad;
                    this.lastmodified = s.lastmodified_squad;
                    break;
            }
        }
    }

    class Wrapper {
        int response;
        Stats stats;
        Totals totals;

        class Stats {
            int kills_solo;
            int placetop1_solo;
            int placetop10_solo;
            int placetop25_solo;
            int matchesplayed_solo;
            float kd_solo;
            float winrate_solo;
            float score_solo;
            int minutesplayed_solo;
            long lastmodified_solo;

            int kills_duo;
            int placetop1_duo;
            int placetop10_duo;
            int placetop25_duo;
            int matchesplayed_duo;
            float kd_duo;
            float winrate_duo;
            float score_duo;
            int minutesplayed_duo;
            long lastmodified_duo;

            int kills_squad;
            int placetop1_squad;
            int placetop10_squad;
            int placetop25_squad;
            int matchesplayed_squad;
            float kd_squad;
            float winrate_squad;
            float score_squad;
            int minutesplayed_squad;
            long lastmodified_squad;

        }

        class Totals {
            int kills;
            int wins;
            int matchesplayed;
            int minutesplayed;
            int hoursplayed;
            int score;
            float winrate;
            float kd;
            long lastupdate;
        }
    }
}
