package main.commands.electronicArts;

import com.google.gson.JsonSyntaxException;
import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import main.Aspect;
import main.Command;
import main.utility.TableUtil;
import main.utility.apexlegends.ALChild;
import main.utility.apexlegends.ALChildStat;
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
                BotUtils.APEX_LEGENDS_API_KEY);
        //Aspect.LOG.info(json1);

        ALPlayerInfo.ALPlayerData player;
        try {
            player = BotUtils.gson.fromJson(json1, ALPlayerInfo.class).getData();
        } catch (IllegalStateException | JsonSyntaxException e) {
            e.printStackTrace();
            Aspect.LOG.info(json1);
            return;
        }



        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Apex Legends :: " + player.getMetadata().getPlatformUserHandle() + " lv. " + player.getMetadata().getLevel())
                .withUrl("https://apex.tracker.gg/profile/"+getTrackerPlatform(args)+"/"+ign)
                .withDesc(generateDesc(player))
                .withFooterText("footer placeholder");

        Aspect.LOG.info(generateDesc(player));



        try {
            String json2 = BotUtils.getStringFromUrl("https://apextab.com/api/search.php?platform="+getIconUrlPlatform(args) +"&search=" + ign);
            IconEncap.IconResult iconResult = BotUtils.gson.fromJson(json2, IconEncap.class).results[0];
            eb.withThumbnail(iconResult.avatar);
        } catch (NullPointerException e) { // this means ign is not valid, since there is no icon
            //BotUtils.send(event.getChannel(), "Make sure your username is valid. If it is, contact the dev @Requiem#8148 to report this error");
        }

//        BotUtils.send(event.getChannel(),
//                eb.withImage("attachment://stats.png"),
//                new ByteArrayInputStream(Visuals.genTextImage(generateDesc(player)).toByteArray()),
//                "stats.png");

        BotUtils.send(event.getChannel(), eb);
    }

    private String generateDesc(ALPlayerInfo.ALPlayerData player) {
        GridTable table = GridTable.of(player.getChildren().size()*6 + 1, 4)
                .put(0,0, Cell.of("--"))
                .put(0, 1, Cell.of("value"))
                .put(0, 2, Cell.of("Top %"))
                .put(0,3, Cell.of("Rank"));

        // iterate over legends
        for (int i = 0; i < player.getChildren().size(); i++) {
            ALChild legend = player.getChildren().get(i);
            int currRow = 6*i + 1;

            // legend name and stats placeholders
            table.put(currRow, 0, Cell.of(legend.getMetadata().getLegend_name()))
                    .put(currRow, 1, Cell.of("-"))
                    .put(currRow, 2, Cell.of("-"))
                    .put(currRow, 3, Cell.of("-"));

            // iterate over kills, k/m, dmg/m, dmg, etc
            for (int j = 0; j < legend.getStats().size(); j++) {
                ALChildStat stat = legend.getStats().get(j);
                table.put(currRow+j+1, 0, Cell.of("  -" + sanitizeName(stat.getMetadata().getName())))
                        .put(currRow+j+1, 1, Cell.of(val(stat.getValue())))
                        .put(currRow+j+1, 2, Cell.of(val(stat.getPercentile())))
                        .put(currRow+j+1, 3, Cell.of(val(stat.getRank())));
            }

            if (("```js\n                                               \n" +TableUtil.render(table).toString() + "```").length() >= 1500) break;
        }

        Aspect.LOG.info("length before border: " + ("```js\n                                     \n" +TableUtil.render(table).toString() + "```").length());

        //table = Border.DOUBLE_LINE.apply(table); //

        Aspect.LOG.info("length after border: " + ("```js\n                                     \n" +TableUtil.render(table).toString() + "```").length());

        return "```js\n                                            " +TableUtil.render(table).toString() + "```"; // surround in js code block (number colouring)
    }

    private String sanitizeName(String name) {
        if (name.equals("Kills")) return "Kills";
        if (name.equals("Kills Per Match")) return "Kills/Match";
        if (name.equals("Damage Per Match")) return "Dmg/Match";
        if (name.equals("Damage")) return "Dmg";
        if (name.equals("Matches Played")) return "Matches";
        if (name.equals("Care Package Kills")) return "CarePack Ks";
        if (name.startsWith("Legend")) return "Legend Spec";
        return name;
    }

    private String val(Number n) {
        return String.valueOf(n);
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
