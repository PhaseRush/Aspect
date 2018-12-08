package main.commands.music.queue;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongRestart implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.getScheduler();

        try {
            scheduler.getPlayer().startTrack(scheduler.getCurrentTrack().makeClone(), false);
            BotUtils.reactWithCheckMark(event.getMessage());
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "There was an error restarting this track.");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
