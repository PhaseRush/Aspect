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
            BotUtils.send(event.getChannel(), "There is no track playing.");
            return;
        }

        int maxLoop = Integer.MAX_VALUE; //todo : debate whether default (no params) should loop once or infinite times
        try {
            if (args.size() == 1) {
                int loopCount = Integer.valueOf(args.get(0));
                if (loopCount < 1) throw new NumberFormatException();
                maxLoop = loopCount;
            }
        } catch (NumberFormatException e) {
            BotUtils.send(event.getChannel(),
                    "Please use a number that is within the range [1 to " + Integer.MAX_VALUE + "], inclusive." +
                    "\n If you want to loop indefinetly, use `" + BotUtils.getPrefix(event.getGuild()) + "loop`");
            return;
        }

        //toggle looping
        if (scheduler.isLooping()) {
            scheduler.setLooping(false, 0);
            BotUtils.send(event.getChannel(), "Stopped loop");
        } else {
            scheduler.setLooping(true, maxLoop);
            BotUtils.reactWithCheckMark(event.getMessage());
        }

    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "loops current song infinitely, or x times";
    }
}
