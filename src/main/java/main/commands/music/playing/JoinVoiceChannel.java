package main.commands.music.playing;

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
                    .map(String::toLowerCase)
                    .sorted(Comparator.comparingDouble(ch -> BotUtils.stringSimilarity(ch, args.get(0))))
                    .filter(ch -> BotUtils.stringSimilarity(ch, args.get(0)) < Math.max(2, ch.length()/5))
                    .findFirst()
                    .ifPresent(ch -> event.getGuild().getVoiceChannelsByName(ch).get(0).join());
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
