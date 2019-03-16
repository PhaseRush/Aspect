package main.commands.music.playing;

import javafx.util.Pair;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.Comparator;
import java.util.List;

public class JoinVoiceChannel implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).matches("\\d.*")) {
            event.getClient().getVoiceChannelByID(Long.valueOf(args.get(0))).join();
        } else {
            event.getGuild().getVoiceChannels().stream()
                    .map(IChannel::getName)
                    .map(name -> new Pair<>(name, name.toLowerCase()))
                    .sorted(Comparator.comparingDouble(o -> Math.min(
                            BotUtils.stringSimilarity(o.getKey(), args.get(0)),
                            BotUtils.stringSimilarity(o.getValue(), args.get(0).toLowerCase()))))
                    .filter(pair ->
                            BotUtils.stringSimilarity(pair.getKey(), args.get(0)) < Math.max(2, pair.getKey().length()/5) ||
                            BotUtils.stringSimilarity(pair.getValue(), args.get(0).toLowerCase()) < Math.max(2, pair.getValue().length()/5))
                    .findFirst()
                    .ifPresent(pair -> event.getGuild().getVoiceChannelsByName(pair.getKey()).get(0).join());
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).matches("\\d.*")) {
            try {
                event.getClient().getVoiceChannelByID(Long.valueOf(args.get(0)));
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "Forces Aspect into a voice channel without additionally playing anything";
    }
}
