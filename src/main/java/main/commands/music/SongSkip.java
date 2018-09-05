package main.commands.music;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongSkip implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.size() == 0)
            MasterManager.skipTrack(event.getChannel());
        else {
            try {
                MasterManager.skipNumTracks(event.getChannel(), Integer.valueOf(args.get(0)));
            } catch (NumberFormatException e) {
                BotUtils.sendMessage(event.getChannel(), "Use: ```\n" + BotUtils.DEFAULT_BOT_PREFIX + "help skip``` for proper formatting");
            }
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
}
