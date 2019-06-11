package main.commands.humor;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class Insult implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), BotUtils.getRandFromList(BotUtils.insults));
    }

    @Override
    public String getDesc() {
        return "Dishes an insult. ~~Slightly NSFW~~.";
    }
}
