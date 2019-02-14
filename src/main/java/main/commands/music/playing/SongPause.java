package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.metautil.BotUtils;
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
        return "Toggles pause/unpause on current music player";
    }


    @Override
    public Status mayRun(MessageReceivedEvent event, List<String> args) {
        if (MasterManager.getGuildAudioPlayer(event.getGuild()).getPlayer() == null) {
            return Status.STATE_PRECONDITION_ERROR;
        } else if (event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null) {
            return Status.RUN_STATE_ERROR;
        }

        return Status.OK;
    }


    @Override
    public boolean requireSynchronous() {
        return true;
    }


}
