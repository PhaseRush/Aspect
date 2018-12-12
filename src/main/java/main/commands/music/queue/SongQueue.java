package main.commands.music.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public class SongQueue implements Command {
    private IMessage previousQMsg;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();
        List<AudioTrack> audioTracks = scheduler.getQueue();

        boolean moreThan15 = audioTracks.size() > 15;

        // generate message head
        StringBuilder sb = new StringBuilder()
                .append("Queue for " + event.getGuild().getName() + ": " + (moreThan15 ? "(listing first 15 of) " + audioTracks.size() + " songs" : "")
                        + "\nTotal duration: " + scheduler.getQueueHMS() + "```\n");

        // generate embed desc
        for (int i = 0; i < (moreThan15 ? 15 : audioTracks.size()); i++) {
            sb.append((i + 1) + ". \t" + (i < 9 ? " " : "") +
                    "[" + BotUtils.millisToMS(audioTracks.get(i).getInfo().length) + "]   " +
                    BotUtils.limitStrLen(audioTracks.get(i).getInfo().title + "\n", 75, true, true)); // +1 for index 1
        }

        sb.append("```");

        // delete previous message if not null
        if (previousQMsg != null) previousQMsg.delete();
        previousQMsg = BotUtils.sendGet(event.getChannel(), sb.toString());
    }

    @Override
    public String getDescription() {
        return "Displays the music queue. Will only show first 15 tracks if the queue is longer than 15.";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
