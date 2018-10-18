package main.commands.music.playing;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongPlay implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel()
                != event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()) {

            BotUtils.joinVC(event);
        }
        AudioPlayer player = MasterManager.getGuildAudioPlayer(event.getGuild()).player;
        if (player.isPaused()) {
            player.setPaused(false);
            BotUtils.sendMessage(event.getChannel(), "Player is now unpaused.");
        }
        String searchStr = String.join(" ", args);
        MasterManager.loadAndPlay(event.getChannel(), searchStr, event, false, "");
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "plays song url, or searches youtube if url is not valid.";
    }
}
