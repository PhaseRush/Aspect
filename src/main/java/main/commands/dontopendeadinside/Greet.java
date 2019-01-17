package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Greet implements Command {
    private Map<Long, Long> idTimeMap = new HashMap<>();

    @Override
    @SuppressWarnings("all")
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // update map
        idTimeMap.put(event.getAuthor().getLongID(), System.currentTimeMillis());

        // get voice channel
        Optional<IVoiceChannel> vChannel = Optional.ofNullable(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel());

        if (vChannel.isPresent()) { // generate greetings
            StringBuilder sb = new StringBuilder();
            vChannel.get().getConnectedUsers().forEach((u) -> sb.append("Hello " + BotUtils.getNickOrDefault(u, event.getGuild()) + "!\n"));
            BotUtils.send(event.getChannel(), sb.toString());
        } else { // get the fuck in voice
            BotUtils.send(event.getChannel(), "You're not in a voice channel! Join a voice channel and try again :)");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (idTimeMap.containsKey(event.getAuthor().getLongID())) { // if already in map, see if > 60 seconds
            return (System.currentTimeMillis() - idTimeMap.get(event.getAuthor().getLongID())) > 1000 * 60;
        } else {
            return true;
        }
    }

    @Override
    public String getDesc() {
        return "Aspect greets everyone in the voice channel :)";
    }
}
