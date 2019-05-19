package main.commands.mooshroom;

import main.Aspect;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RPoints implements Command {
    private static PointsContainer container = BotUtils.gson.fromJson(BotUtils.readFromFileToString("data/mooshroom.json"), PointsContainer.class);

    /**
     *
     * @param event
     * @param args
     */
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IUser targetUser = event.getMessage().getMentions().get(0);
        if (args.size() == 1) { // want to see commands
            BotUtils.send(
                    event.getChannel(),
                    new EmbedBuilder()
                            .withTitle(getCurrPoints(targetUser, event.getGuild()))
                            .withColor(getColour(targetUser))
                            .withDesc("Top contributors: " + genContribDesc(targetUser, event.getGuild()) + " points")
            );
        } else if (args.size() == 2) {
            long points;
            try {
                points = Long.parseLong(args.get(1));
            } catch (NumberFormatException e) {
                BotUtils.send(event.getChannel(), "Invalid number!");
                return;
            }

            // update total points for target
            container.point_map.put(targetUser.getStringID(),
                    points + container.point_map.getOrDefault(targetUser.getStringID(), 0L));
            // update giver's status
            if (container.gifts.containsKey(event.getAuthor().getStringID())) { // has given before
                List<InnerPair> pairs = container.gifts.get(event.getAuthor().getStringID());
                Optional<InnerPair> currGifts = pairs.stream().filter(
                        pair -> pair.gift_user_id.equals(targetUser.getStringID())
                ).findFirst();

                if (currGifts.isPresent()) { // has given to target before
                    currGifts.get().points_given += points;
                } else { // first time giving to target
                    pairs.add(new InnerPair(targetUser.getStringID(), points));
                }
            } else { // first time giver
                container.gifts.put(event.getAuthor().getStringID(),
                        Collections.singletonList(new InnerPair(targetUser.getStringID(), points)));
            }
            BotUtils.send(event.getChannel(), getCurrPoints(targetUser, event.getGuild()));
            write();
        } else {
            BotUtils.send(event.getChannel(), "Too many parameters!");
            return;
        }

        BotUtils.reactWithCheckMark(event.getMessage());
    }

    private String genContribDesc(IUser targetUser, IGuild guild) {
        StringBuilder sb = new StringBuilder();

        container.gifts.entrySet().stream()
                .filter(entry -> entry.getValue().stream() // filter entries that dont contribute to target
                        .anyMatch(pair -> pair.gift_user_id.equals(targetUser.getStringID())))
                .limit(5) // just take top 5
                .forEach(entry ->
                        sb.append(BotUtils.getNickOrDefault(Aspect.client.getUserByID(Long.valueOf(entry.getKey())), guild))
                                .append("\t")
                                .append(entry.getValue().stream()
                                        .filter(pair -> pair.gift_user_id.equals(targetUser.getStringID()))
                                        .findFirst().get().points_given)
                                .append(" points"));

        return sb.toString();
    }

    private String getCurrPoints(IUser targetUser, IGuild guild) {
        return BotUtils.getNickOrDefault(targetUser, guild) + " has " +
                container.point_map.get(targetUser.getStringID()) + " retard points";
    }

    private Color getColour(IUser targetUser) {
        BufferedImage buff = Visuals.urlToBufferedImage(targetUser.getAvatarURL());
        if (buff != null) return Visuals.analyzeImageColor(buff);
        else return Visuals.getRandVibrandColour();
    }

    private synchronized void write() {
        BotUtils.writeToFile(System.getProperty("user.dir") + "/data/mooshroom.json",
                BotUtils.gson.toJson(container),
                false);
    }


    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        //if (event.getGuild().getLongID() != 287811950216478732L) return false; // only run in mooshroom
        if (event.getAuthor().getRolesForGuild(event.getGuild()).stream()
                .map(IRole::getName)
                .anyMatch(name -> name.contains("Head") || name.contains("Body"))
        ) { // if is a head or body
            return !args.isEmpty(); // make sure at least one argument
        }
        return BotUtils.isDev(event) && !args.isEmpty(); // else just check if dev
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "If you dont know what this does, dont use it";
    }

    @Override
    public String getSyntax() {
        return "To give points use `$rpoint x`" +
                "To see his points use `$rpoint`";
    }


    private class PointsContainer {
        private Map<String, Long> point_map;
        private Map<String, List<InnerPair>> gifts;
    }
    private class InnerPair {
        private String gift_user_id;
        private long points_given;

        public InnerPair(String gift_user_id, long points_given) {
            this.gift_user_id = gift_user_id;
            this.points_given = points_given;
        }
    }
}
