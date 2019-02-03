package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongResume implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        AudioPlayer player = guildMusicManager.getPlayer();

        if (player.getPlayingTrack() == null) {
            BotUtils.send(event.getChannel(), "Currently not playing any track.");
            return;
        }

        if (player.isPaused()) {
            player.setPaused(false);
            BotUtils.send(event.getChannel(), "Player has been resumed");
        } else {
            BotUtils.send(event.getChannel(), "Player is not paused");
        }
    }

    @Override
    public String getDesc() {
        return "Resumes music player";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
