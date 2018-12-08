package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class SongInfo implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager musicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        AudioTrackInfo songInfo = musicManager.getScheduler().getCurrentTrack().getInfo();

        EmbedBuilder eb = new EmbedBuilder()
                .withColor(Visuals.getVibrantColor())
                .withTitle(songInfo.title)
                .withDesc("By: " + songInfo.author + "\n" + musicManager.getScheduler().trackProgress())
                .withUrl(songInfo.uri);

        BotUtils.send(event.getChannel(), eb);
    }

    private String getFormattedSongLength(AudioTrackInfo songInfo) {
        long millis = songInfo.length;
        int mins = (int) (millis / 1000 / 60);
        int secs = (int) ((millis / 1000) % 60);

        return mins + ":" + (secs < 10 ? "0" + secs : secs);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Shows info about current song";
    }
}