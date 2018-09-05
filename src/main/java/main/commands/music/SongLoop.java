package main.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongLoop implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        AudioTrack currentTrack = guildMusicManager.getScheduler().getCurrentTrack();

        BotUtils.sendMessage(event.getChannel(), "Now playing: " + currentTrack.getInfo().title);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
