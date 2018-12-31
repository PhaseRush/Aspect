package main.commands.music.queue;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public class SongPastQueue implements Command {
    private IMessage previousQMsg;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();
        StringBuilder sb = scheduler.getPastQueueStrB(event);
        System.out.println("Song pq: sb: " + sb.toString());

        // delete previous message if not null
        // if (previousQMsg != null) previousQMsg.delete();
        previousQMsg = BotUtils.sendGet(event.getChannel(), sb.toString());


        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public String getDesc() {
        return "Displays the past music queue (songs that were already played). Will only show first 15 tracks if the queue is longer than 15.";
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

}