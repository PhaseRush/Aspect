package main.commands.humor;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OofCounter implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long startTime = System.currentTimeMillis();
        Map<IUser, Integer> oofMap = new HashMap<>();
        IChannel channel = event.getChannel();

        if (args.size() < 1) {
            BotUtils.sendMessage(event.getChannel(), "This command needs a word to search for. It also takes an optional parameter for server wide scans. " +
                    "\nWARNING USING ALL CAN *DRASTICALLY* INCREASE RUN TIME" +
                    "\nEx: $count hello" +
                    "\nEx: $count oof, all");
            return;
        }

        boolean scanAllChannels = args.size() == 2;

        List<IChannel> textChannels = new ArrayList<>();
        if (scanAllChannels)
            textChannels.addAll(event.getGuild().getChannels());
        else
            textChannels.add(event.getChannel());


        int messageCounter = 0;
        for (IChannel textChannel : textChannels) {
            try {
                for (IMessage m : textChannel.getFullMessageHistory()) {
                    if (m.getContent().contains(args.get(0))) {
                        IUser author = m.getAuthor();
                        try {
                            oofMap.put(author, oofMap.get(author) + 1);
                        } catch (NullPointerException e) {
                            oofMap.putIfAbsent(author, 1); //initialize with 1.
                        }
                    }
                    messageCounter++;
                }
            } catch (MissingPermissionsException e) {
                BotUtils.sendMessage(channel, "Aspect is missing READ_MESSAGES permission in " + textChannel.getName() + ". Trying to forcibly continue execution.");
            }
        }

        oofMap = BotUtils.sortMapByValue(oofMap, false);
        Entry<IUser, Integer> mostGoodPerson = oofMap.entrySet().iterator().next();
        String nick = mostGoodPerson.getKey().getNicknameForGuild(event.getGuild());


        long timeElapsed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timeElapsed/60000);
        int seconds = (int) (timeElapsed % 60000)/1000;
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("very high tech counter")
                //.withColor(Visuals.analyizeImageColor(Visuals.urlToBufferedImage(mostGoodPerson.getKey().getAvatarURL()))) //has problems.
                .withColor(Visuals.getVibrantColor())
                .withThumbnail(mostGoodPerson.getKey().getAvatarURL())
                .withDesc("Winner: " + (nick == null ? mostGoodPerson.getKey().getName() : nick))
                .withFooterText("It took me " + minutes +":"+ (seconds <10? "0" + seconds : seconds) + " to scan through " +
                        messageCounter + " messages in " + (scanAllChannels? textChannels.size() + " channels" : channel.getName()));

        int rankCounter = 1;
        for (Entry<IUser, Integer> entry : oofMap.entrySet()) {
            try {
                String eachNick = entry.getKey().getNicknameForGuild(event.getGuild());
                eb.appendField(rankCounter + ": " + (eachNick == null ? entry.getKey().getName() : eachNick), args.get(0) + " count: " + entry.getValue().toString(), false);
                rankCounter++;
            } catch (IllegalArgumentException e) {
                break; //already at 25 fields. Since map is sorted, just break loop
            }
        }

        BotUtils.sendMessage(channel, eb);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
