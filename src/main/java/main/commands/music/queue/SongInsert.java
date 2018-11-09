package main.commands.music.queue;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * bug: need to insert twice for this to sort of work. @todo
 */
public class SongInsert implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        String searchStr = String.join(" ", args);
        MasterManager.loadAndPlay(event.getChannel(), searchStr, event, true, "Inserting track to front of queue");

//        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
//        List<AudioTrack> audioTracks = guildMusicManager.getScheduler().getQueue();
//
//        AudioTrack trackToInsert = audioTracks.get(audioTracks.size() - 1);
//        audioTracks.add(0, trackToInsert);
//        audioTracks.remove(audioTracks.size() - 1);
//        guildMusicManager.getScheduler().setQueue(audioTracks); //newly added

        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
