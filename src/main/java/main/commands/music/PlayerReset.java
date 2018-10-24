package main.commands.music;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class PlayerReset implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

//        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
//        TrackScheduler scheduler = guildMusicManager.getScheduler();
//        scheduler.clearQueue();
//        guildMusicManager.player.destroy();

    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
