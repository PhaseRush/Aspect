package main.utility.music;

import sx.blah.discord.handle.impl.obj.ReactionEmoji;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MusicUtils {

    private static List<ReactionEmoji> floatingReactions = new LinkedList<>();

    public static Map<String, String> customUrls = new LinkedHashMap<>();

    static {

        
        floatingReactions.add(ReactionEmoji.of("\u25b6")); //play -- might also need to attack \ufe0f
        floatingReactions.add(ReactionEmoji.of("\u23f8")); //pause -- might also need to attack \ufe0f
        //floatingReactions.add(ReactionEmoji.of(String.valueOf(nextTrackUnicodeArray)));
        floatingReactions.add(ReactionEmoji.of(String.valueOf("\u23E9"))); //also next track emoji
        floatingReactions.add(ReactionEmoji.of("\uD83C\uDDF6")); //regional Q



        // my custom music playlists -- DO NOT SHIFT THIS LINE (Linked form github)
        customUrls.put("music", "https://www.youtube.com/playlist?list=PLN2wnTVWJMHdufDvt6HyYzeuhN2DFe8cE");
        customUrls.put("tier2", "https://www.youtube.com/playlist?list=PLN2wnTVWJMHcoslyAE8aY53IBDXK2N9-X");

        // nb3
        customUrls.put("nb3all", "https://www.youtube.com/watch?v=BwEZaariQQ4&list=PLEgNqLmZpLuI9ajUy3Hg97NrpssG4repu");
        customUrls.put("nb3", "https://www.youtube.com/watch?v=yLxsJpgvkfo&list=PLwMEL7UNT4o9iMzrvNBXZqXbNPFfT6rVD");


        // other custom playlists
        customUrls.put("dank", "https://www.youtube.com/playlist?list=PLSbBQFh_CUqufnCqVuAfjw7pwJzuu2UNe");
        customUrls.put("piano", "https://www.youtube.com/playlist?list=PLszZ0xXW9BjpSwpNLmRJ7dtOlf-qLDP0g");
        customUrls.put("star", "https://www.youtube.com/playlist?list=PLszZ0xXW9BjoX6r2ZRqPwF9wAlcxZSvgI");
        customUrls.put("xi", "https://www.youtube.com/playlist?list=PLszZ0xXW9BjqGuovNjdnMsM7xcNxOut23");
        customUrls.put("kpop", "https://www.youtube.com/watch?v=u6unJQownW4&list=RD5HMOkfE3kno");

        //youtube defaults
        customUrls.put("pop", "https://www.youtube.com/playlist?list=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj");
        customUrls.put("trap", "https://www.youtube.com/playlist?list=PLPgtsFyiwUoLAFd6-yNNtrF3l9H1EQ9Nx");
        customUrls.put("fbass", "https://www.youtube.com/playlist?list=PLe8jmEHFkvsbRwwi0ode5c9iMQ2dyJU3N");
        customUrls.put("lofi", "https://www.youtube.com/watch?v=hHW1oY26kxQ");

        // Youtube custom songs
        customUrls.put("kda", "https://www.youtube.com/watch?v=UOxkGD8qRB4");
    }
}
