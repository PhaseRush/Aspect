package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.*;

public class Greet implements Command {
    private Map<Long, Long> idTimeMap = new HashMap<>();
    private static Map<Long, String> customMap = new HashMap<>();
    private static List<Long> blacklistIDs = new ArrayList<>();
    private static String[] greetings = {
            // standard
            "Hi",
            "Hello",
            "Heyo",
            "Hiya",
            "Sup",

            // these are cringy
            "What's up",
            "What's good",
            "Peek-a-boo",

            // other (human) languages
            "Salut",
            "Ciao",
            "Hola",
            "Aloha",
    };

    static {
        customMap.put(107377006747848704L, "Orha"); // haha take that!
        customMap.put(127943220335214592L, "Siamese cat");
    }


    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // get voice channel
        Optional<IVoiceChannel> vChannel = Optional.ofNullable(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel());

        // reactor cosplay
//        BotUtils.send(event.getChannel(),
//                event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()
//                        .ifNotNullDo(c -> c.getConnectedUsers())
//                        .map(u->(customMap.containsKey(u.getLongID())?
//                                    customMap.get(u.getLongID()) :
//                                    BotUtils.getNickOrDefault(u, event.getGuild())))
//                                .collect(some StringBuilder)
//                                .elseIfNull("you're not connected")
//                        );

        if (vChannel.isPresent()) { // generate greetings
            StringBuilder sb = new StringBuilder();
            vChannel.get().getConnectedUsers().stream()
                    .filter(u -> !u.isBot())
                    .forEach((u) -> sb
                            .append(BotUtils.getRandomFromArrayString(greetings) + ", ")
                            .append((customMap.containsKey(u.getLongID())?
                                    customMap.get(u.getLongID()) :
                                    BotUtils.getNickOrDefault(u, event.getGuild()))
                                    + "!\n"));

            BotUtils.send(event.getChannel(), sb.toString());
            // update map
            idTimeMap.put(event.getAuthor().getLongID(), System.currentTimeMillis());
        } else { // get the fuck in voice
            BotUtils.send(event.getChannel(), "You're not in a voice channel! Join a voice channel and try again :)");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
//        if (idTimeMap.containsKey(event.getAuthor().getLongID())) { // if already in map, see if > 60 seconds
//            return (System.currentTimeMillis() - idTimeMap.get(event.getAuthor().getLongID())) > 1000 * 60;
//        } else {
//            return true;
//        }
        return true; // just do this for now
    }

    @Override
    public String getDesc() {
        return "Aspect greets everyone in the voice channel :)";
    }
}
