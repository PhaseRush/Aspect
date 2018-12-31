package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongPause implements Command {
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
            player.setPaused(true);
            BotUtils.reactWithCheckMark(event.getMessage());
        }
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }


}
