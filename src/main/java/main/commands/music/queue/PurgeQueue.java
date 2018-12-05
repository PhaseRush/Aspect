package main.commands.music.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class PurgeQueue implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        List<AudioTrack> audioTracks;

        synchronized (audioTracks = guildMusicManager.getScheduler().getQueue()) { //added this synchronized block
            try {
                if (Integer.valueOf(args.get(0)) > audioTracks.size()) {
                    BotUtils.send(event.getChannel(), "There are only " + audioTracks.size() + " tracks!");
                    return;
                }

                guildMusicManager.getScheduler().setQueue(audioTracks.subList(0, Integer.valueOf(args.get(0))));
                BotUtils.send(event.getChannel(), "Queue has been purged from " + args.get(0) + " onwards.");
            } catch (NumberFormatException e) {
                BotUtils.send(event.getChannel(), "Need a number for the index number");
            }
        }

    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
