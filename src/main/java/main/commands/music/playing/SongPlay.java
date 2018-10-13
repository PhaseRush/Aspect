package main.commands.music.playing;

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

        String searchStr = String.join(" ", args);
        MasterManager.loadAndPlay(event.getChannel(), searchStr, event);
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
