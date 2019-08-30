package main.commands.music.playing;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class JoinVoiceChannel implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).matches("\\d.*")) {
            event.getClient().getVoiceChannelByID(Long.parseLong(args.get(0))).join();
        } else {
            BotUtils.fuzzyVoiceMatch(event, args.get(0)).join();
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).matches("\\d.*")) {
            try {
                event.getClient().getVoiceChannelByID(Long.parseLong(args.get(0)));
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
