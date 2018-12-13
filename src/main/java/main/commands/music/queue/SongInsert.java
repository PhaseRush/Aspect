package main.commands.music.queue;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * bug: need to insert twice for this to sort of work. @todo
 */
public class SongInsert implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {

        String searchStr = String.join(" ", args);
        MasterManager.loadAndPlay(event.getChannel(), searchStr, event, true, "Inserting track to front of queue");
        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }
}
