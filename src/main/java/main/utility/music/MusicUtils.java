package main.utility.music;

import sx.blah.discord.handle.impl.obj.ReactionEmoji;

import java.util.LinkedList;
import java.util.List;

public class MusicUtils {

    public static List<ReactionEmoji> floatingReactions;
    public static char[] nextTrackUnicodeArray = {0x23ED, 0xFE0F};

    static {

        floatingReactions = new LinkedList<>();
        floatingReactions.add(ReactionEmoji.of("\u25b6")); //play -- might also need to attack \ufe0f
        floatingReactions.add(ReactionEmoji.of("\u23f8")); //pause -- might also need to attack \ufe0f
        floatingReactions.add(ReactionEmoji.of(String.valueOf(nextTrackUnicodeArray)));
        floatingReactions.add(ReactionEmoji.of("\uD83C\uDDF6")); //regional Q

    }
}
