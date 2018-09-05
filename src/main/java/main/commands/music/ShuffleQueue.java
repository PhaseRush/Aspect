package main.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class ShuffleQueue implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        List<AudioTrack> audioTracks = guildMusicManager.getScheduler().getQueue();
        Collections.shuffle(audioTracks);

        BotUtils.sendMessage(event.getChannel(), "Queue has been shuffled");

    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
