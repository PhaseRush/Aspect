package main.commands.music.playing;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongSkip implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.size() == 0)
            MasterManager.skipTrack(event);
        else {
            try {
                MasterManager.skipNumTracks(event, Integer.valueOf(args.get(0)));
            } catch (NumberFormatException e) {
                BotUtils.sendMessage(event.getChannel(), "Use: ```\n" + BotUtils.DEFAULT_BOT_PREFIX + "help skip``` for proper formatting");
            }
        }
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "skips to next song, or next x songs if specified";
    }
}
