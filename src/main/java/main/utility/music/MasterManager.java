package main.utility.music;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.utility.BotUtils;
import main.utility.GoogleUtil;
import main.utility.Visuals;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MasterManager {
    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    //thanks to decc the hecc
    static {
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = guild.getLongID();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    public synchronized static void loadAndPlay(final IChannel channel, final String trackUrl, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                BotUtils.sendMessage(channel, "Playing: " + track.getInfo().title);

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                int counter = 0;
                List<AudioTrack> audioTrackList = playlist.getTracks();
                if (audioTrackList != null) {
                    for (AudioTrack track : audioTrackList) {
                        play(musicManager, track);
                        counter++;
                    }
                }
                BotUtils.sendMessage(channel, "Adding " + counter + " songs to queue from " + playlist.getName() + " (first song: " + playlist.getTracks().get(0).getInfo().title + ")");
            }

            @Override
            public void noMatches() {
                //check if it is a preinit playlist
                if (trackUrl.equals("music2")) {
                    loadAndPlay(channel, "https://www.youtube.com/playlist?list=PLN2wnTVWJMHdufDvt6HyYzeuhN2DFe8cE", event);
                    return;
                } else if (trackUrl.equals("tier2")) {
                    loadAndPlay(channel, "https://www.youtube.com/playlist?list=PLN2wnTVWJMHcoslyAE8aY53IBDXK2N9-X", event);
                    return;
                }

                YouTube youtube = GoogleUtil.getYoutube();
                YouTube.Search.List search;
                SearchListResponse searchResponse;

                try {
                    search = youtube.search()
                            .list("id,snippet")
                            .setKey(BotUtils.YOUTUBE_API_KEY)
                            .setQ(trackUrl)//trackUrl isnt an actual url, so it must be a search query
                            .setType("video")
                            //.search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"); //@todo could be useful
                            .setMaxResults(5L); //could abstract this
                    searchResponse = search.execute();
                    List<SearchResult> searchResults = searchResponse.getItems();

                    if (searchResults.size() == 0) {
                        BotUtils.sendMessage(channel, "Nothing found by: " + trackUrl);
                        return;
                    }

                    //reaction Embed
                    String firstVideoThumbnailUrl = searchResults.get(0).getSnippet().getThumbnails().getDefault().getUrl();
                    EmbedBuilder itemOptionEmbed = new EmbedBuilder()
                            .withThumbnail(firstVideoThumbnailUrl)
                            .withColor(Visuals.analyizeImageColor(Visuals.urlToBufferedImage(firstVideoThumbnailUrl)));

                    List<String> optionsForEmbed = new ArrayList<>();
                    for (SearchResult singleVideo : searchResults) {
                        optionsForEmbed.add(singleVideo.getSnippet().getTitle());
                    }

                    itemOptionEmbed.withDesc("Youtube search results.\n React with the corresponding letter to add song to queue\n\n" + BotUtils.buildOptions(optionsForEmbed, 5));

                    IMessage embedMessage = RequestBuffer.request(() -> channel.sendMessage(itemOptionEmbed.build())).get();
                    List<ReactionEmoji> reactionEmojis = BotUtils.initializeRegionals().subList(0, 5);
                    reactionEmojis.add(ReactionEmoji.of("\u274C"));
                    BotUtils.reactAllEmojis(embedMessage, reactionEmojis);

                    IListener reactionListener = (IListener<ReactionAddEvent>) reactionEvent -> {
                        if (reactionEvent.getUser().equals(event.getAuthor()) && reactionEvent.getChannel().equals(event.getChannel())) {
                            String emojiName = reactionEvent.getReaction().getEmoji().getName();
                            switch (emojiName) {
                                case "\uD83C\uDDE6":
                                    loadAndPlay(channel, searchResults.get(0).getId().getVideoId(), event);
                                    break;
                                case "\uD83C\uDDE7":
                                    loadAndPlay(channel, searchResults.get(1).getId().getVideoId(), event);
                                    break;
                                case "\uD83C\uDDE8":
                                    loadAndPlay(channel, searchResults.get(2).getId().getVideoId(), event);
                                    break;
                                case "\uD83C\uDDE9":
                                    loadAndPlay(channel, searchResults.get(3).getId().getVideoId(), event);
                                    break;
                                case "\uD83C\uDDEA":
                                    loadAndPlay(channel, searchResults.get(4).getId().getVideoId(), event);
                                    break;
                                case "\u274C": //changed from red cross literal
                                    BotUtils.sendMessage(channel, "Search terminated");
                                    break;
                                default:
                                    BotUtils.sendMessage(channel, "Not a valid reaction; search will be terminated");
                                    break;
                            }
                            if (!embedMessage.isDeleted()) //just in case
                                embedMessage.delete();
                        }
                    };

                    //register this listener
                    event.getClient().getDispatcher().registerListener(reactionListener);

                    ExecutorService executorService = Executors.newFixedThreadPool(1);//no idea how many i need. seems like a relatively simple task?
                    Runnable removeListener = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e1) {
                                System.out.println("guess this is fucked");
                            } finally { //please just execute this no matter what
                                event.getClient().getDispatcher().unregisterListener(reactionListener);
                                System.out.println("Listener Deleted");
                            }
                        }
                    };
                    executorService.execute(removeListener);


                } catch (IOException e) {
                    System.out.println("Audio - MasterManager.loadAndPlay.noMatches - IOException thrown");
                    e.printStackTrace();
                }

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                BotUtils.sendMessage(channel, "Could not play: " + exception.getMessage());
            }

        });
    }

    private synchronized static void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.getScheduler().queue(track);
    }

    public synchronized static void skipTrack(IChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.getScheduler().nextTrack();

        BotUtils.sendMessage(channel, "Skipped to next track.");
    }

    public synchronized static void skipNumTracks(IChannel channel, int num) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        for (int i = 0; i < num; i++) {
            musicManager.getScheduler().nextTrack();
        }

        BotUtils.sendMessage(channel, "Skipped " + num + (num == 1 ? " track" : " tracks"));
    }


}
