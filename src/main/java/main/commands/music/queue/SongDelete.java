package main.commands.music.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongDelete implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        List<AudioTrack> audioTracks;

        synchronized (audioTracks = guildMusicManager.getScheduler().getQueue()) { //added this synchronized block
            try {
                int i = Integer.valueOf(args.get(0));
                if (Integer.valueOf(args.get(0)) > audioTracks.size()) {
                    BotUtils.sendMessage(event.getChannel(), "There are only " + audioTracks.size() + " tracks!");
                    return;
                }
                guildMusicManager.getScheduler().getQueue().remove(i-1);
                BotUtils.reactWithCheckMark(event.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                BotUtils.sendMessage(event.getChannel(), "Need a number for the index");
            }
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
