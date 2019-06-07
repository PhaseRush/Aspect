package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
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
                .withColor(Visuals.getRandVibrantColour())
                .withTitle(songInfo.title)
                .withDesc("By: " + songInfo.author + "\n" + musicManager.getScheduler().trackProgress())
                .withUrl(songInfo.uri);

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public String getDesc() {
        return "Shows info about current song";
    }
}