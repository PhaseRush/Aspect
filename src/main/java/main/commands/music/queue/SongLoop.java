package main.commands.music.queue;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SongLoop implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.getScheduler();

        if (scheduler.getCurrentTrack() == null) {
            BotUtils.sendMessage(event.getChannel(), "There is no track playing.");
            return;
        }

        int maxLoop = 0;
        try {
            if (args.size() == 1)
                maxLoop = Integer.valueOf(args.get(0));
        } catch (NumberFormatException e) {
            BotUtils.sendMessage(event.getChannel(), "Invalid integer");
            return;
        }

        //toggle looping
        if (scheduler.isLooping()) {
            scheduler.setLooping(false, 0);
            BotUtils.sendMessage(event.getChannel(), "Stopped loop");
        } else {
            scheduler.setLooping(true, maxLoop);
            BotUtils.reactWithCheckMark(event.getMessage());
        }

    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "loops current song infinitely, or x times";
    }
}
