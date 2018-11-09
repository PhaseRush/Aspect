package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.List;

public class SongStop implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IVoiceChannel voiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (voiceChannel == null) {
            BotUtils.sendMessage(event.getChannel(), "Aspect is currently not in any voice channels." +
                    " If you believe this is a mistake, please pm the mods or bot owner @ Requiem#8148");
            return;
        }

        voiceChannel.leave();

        TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();
        AudioTrack currentTrack = scheduler.getCurrentTrack();
        try {
            currentTrack.setPosition(currentTrack.getDuration()); //sets it to the end of this song
        } catch (Exception e) {
            System.out.println("SongStop - currentTrack.setPos(end of track) error");
        }
        scheduler.getQueue().clear();

        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Stops current playing songs. Clears queue.";
    }
}
