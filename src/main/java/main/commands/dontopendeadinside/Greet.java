package main.commands.dontopendeadinside;

import main.Command;
import main.utility.metautil.BotUtils;
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
        // me.exe
        customMap.put(264213620026638336L, "Empress Kat");

        // pKamen
        customMap.put(107377006747848704L, "Orha"); // haha take that!
        customMap.put(127943220335214592L, "Siamese cat");
        customMap.put(167418444067766273L, "Vivi");

        // d4j
        customMap.put(149031328132628480L, "Your Majesty");

    }


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

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // get author voice channel
        List<IVoiceChannel> vChannels = new ArrayList<>(Collections.singletonList(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()));

        // add all users
        if (!args.isEmpty() && args.get(0).equals("all")) vChannels.addAll(event.getGuild().getVoiceChannels());

        if (vChannels.isEmpty()) {
            BotUtils.send(event.getChannel(), "There is no one to greet :(\n Either join a voice channel, or try `$greet all`");
            return;
        }

        StringBuilder sb = new StringBuilder();
        vChannels.forEach(
                vc -> vc.getConnectedUsers().stream()
                        .filter(u -> !u.isBot())
                        .forEach( u -> sb
                                .append(BotUtils.getRandomFromArrayString(greetings) + ", ")
                                .append((customMap.containsKey(u.getLongID())?
                                        customMap.get(u.getLongID()) :
                                        BotUtils.getNickOrDefault(u, event.getGuild()))
                                        + "!\n"))
        );

        BotUtils.send(event.getChannel(), sb.toString());

        // update map
        idTimeMap.put(event.getAuthor().getLongID(), System.currentTimeMillis());
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
        return "Aspect greets friends :)";
    }
}
