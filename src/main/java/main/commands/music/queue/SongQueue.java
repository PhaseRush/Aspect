package main.commands.music.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongQueue implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.getScheduler();
        List<AudioTrack> audioTracks = scheduler.getQueue();

        boolean moreThan15 = audioTracks.size() > 15;

        StringBuilder sb = new StringBuilder().append("Queue for " + event.getGuild().getName() + ": " + (moreThan15 ? "(listing first 15 of) " + audioTracks.size() + " songs" : "") + "\nTotal duration: " + scheduler.getQueueHMS() + "```\n");

        for (int i = 0; i < (moreThan15 ? 15 : audioTracks.size()); i++)
            sb.append((i + 1) + ". \t" + (i < 9 ? " " : "") + audioTracks.get(i).getInfo().title + "\n"); //+1 for index 1


        sb.append("```");

        BotUtils.send(event.getChannel(), sb.toString());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return "lists current queue for this voice channel";
    }
}
