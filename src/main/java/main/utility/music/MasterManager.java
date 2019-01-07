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

public class MasterManager {
    private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    //thanks to decc the hecc
    static {
        playerManager.setFrameBufferDuration(5000); //5 second audio buffer MIGHT NEED TO CHANGE FOR EQUALIZER
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

    public synchronized static void loadAndPlay(final IChannel channel, final String trackUrl, MessageReceivedEvent event, boolean insertFront, String confirmMsg) {
        //MasterState json;

        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.getScheduler().lastEmbedChannel = channel; //set embed channel for floating player

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                BotUtils.send(channel, (confirmMsg.equals("") ? "Playing: " + track.getInfo().title : confirmMsg));
                play(musicManager, track, insertFront);
//                try {
//                    MasterJsonUtil.jsonObj.getUserMap().get(event.getAuthor().getStringID()).getMusicStats().incrNumSongsQueued();
//                } catch (Exception ignored) {}
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                int counter = 0;
                long duration = 0;
                List<AudioTrack> audioTrackList = playlist.getTracks();
                if (audioTrackList != null) {
                    for (AudioTrack track : audioTrackList) {
                        play(musicManager, track, insertFront);
                        counter++;
                        duration += track.getDuration();
                    }
                }
                BotUtils.send(channel, "Adding " + counter + " songs to queue from " + playlist.getName() + " (first song: " + playlist.getTracks().get(0).getInfo().title + ")");

                //update MasterState json
//                try {
//                    MusicStats stats = MasterJsonUtil.jsonObj.getUserMap().get(event.getAuthor().getStringID()).getMusicStats();
//                    stats.setNumSongsQueued(stats.getNumSongsQueued() + counter);
//                    stats.setNumMillisQueued(stats.getNumMillisQueued() + duration);
//                } catch (Exception ignored) {}
            }

            @Override
            public void noMatches() {
                //check if it is a preinit playlist

                //not needed anymore
//                switch (trackUrl) {
//                    case "music":
//                        loadAndPlay(channel, "https://www.youtube.com/playlist?list=PLN2wnTVWJMHdufDvt6HyYzeuhN2DFe8cE", event, insertFront, "");
//                        return;
//                    case "tier2":
//                        loadAndPlay(channel, "https://www.youtube.com/playlist?list=PLN2wnTVWJMHcoslyAE8aY53IBDXK2N9-X", event, insertFront, "");
//                        return;
//                    case "nb3all":
//                        loadAndPlay(channel, "https://www.youtube.com/watch?v=BwEZaariQQ4&list=PLEgNqLmZpLuI9ajUy3Hg97NrpssG4repu", event, insertFront, "");
//                        return;
//                    case "nb3":
//                        loadAndPlay(channel, "https://www.youtube.com/watch?v=yLxsJpgvkfo&list=PLwMEL7UNT4o9iMzrvNBXZqXbNPFfT6rVD", event, insertFront, "");
//                        return;
//                    case "dank":
//                        loadAndPlay(channel, "https://www.youtube.com/playlist?list=PLSbBQFh_CUqufnCqVuAfjw7pwJzuu2UNe", event, insertFront, "");
//                        return;
//                }

                //use customUrl map --was broken, now fixed
                if (MusicUtils.customUrls.containsKey(trackUrl)) {
                    loadAndPlay(channel, MusicUtils.customUrls.get(trackUrl), event, insertFront, "");
                    return; //Don't need to search Youtube
                }


                //Search Youtube and listen for return result

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
                        BotUtils.send(channel, "Nothing found by: " + trackUrl);
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

                    IListener reactionListener = (IListener<ReactionAddEvent>) reactionEvent -> {
                        if (reactionEvent.getUser().equals(event.getAuthor()) && reactionEvent.getMessage().getStringID().equals(embedMessage.getStringID())) {
                            String emojiName = reactionEvent.getReaction().getEmoji().getName();
                            switch (emojiName) {
                                case "\uD83C\uDDE6":
                                    loadAndPlay(channel, searchResults.get(0).getId().getVideoId(), event, insertFront, "");
                                    break;
                                case "\uD83C\uDDE7":
                                    loadAndPlay(channel, searchResults.get(1).getId().getVideoId(), event, insertFront, "");
                                    break;
                                case "\uD83C\uDDE8":
                                    loadAndPlay(channel, searchResults.get(2).getId().getVideoId(), event, insertFront, "");
                                    break;
                                case "\uD83C\uDDE9":
                                    loadAndPlay(channel, searchResults.get(3).getId().getVideoId(), event, insertFront, "");
                                    break;
                                case "\uD83C\uDDEA":
                                    loadAndPlay(channel, searchResults.get(4).getId().getVideoId(), event, insertFront, "");
                                    break;
                                case "\u274C": //changed from red cross literal
                                    BotUtils.send(channel, "Search terminated");
                                    break;
                                default:
                                    BotUtils.send(channel, "Not a valid reaction; search will be terminated");
                                    break;
                            }
                            if (!embedMessage.isDeleted()) //just in case
                                embedMessage.delete();
                        }
                    };

                    //react with emojis before registering listener
                    List<ReactionEmoji> reactionEmojis = BotUtils.getRegionals().subList(0, 5);
                    reactionEmojis.add(ReactionEmoji.of("\u274C"));
                    BotUtils.reactAllEmojis(embedMessage, reactionEmojis);

                    // register listener
                    event.getClient().getDispatcher().registerListener(reactionListener);

                    // unregister listener after x ms
                    BotUtils.unregisterListener(reactionListener, 10000, embedMessage);

                } catch (IOException e) {
                    System.out.println("Audio - MasterManager.loadAndPlay.noMatches - IOException thrown");
                    e.printStackTrace();
                }

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                BotUtils.send(channel, "Could not play: " + exception.getMessage());
            }

        });
    }



    //updated with boolean insertFront //got rid of syncrh. (dannie)
    private static void play(GuildMusicManager musicManager, AudioTrack track, boolean insertFront) {
        if (!insertFront) musicManager.getScheduler().queue(track);
        else musicManager.getScheduler().queueFront(track);
    }

    // NPE
    public synchronized static void skipTrack(MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        musicManager.getScheduler().nextTrack(event.getChannel());
        BotUtils.reactWithCheckMark(event.getMessage());
        musicManager.getScheduler().setLooping(false, -1); //end looping

        //update state json
//        try {
//            MasterJsonUtil.jsonObj.getUserMap().get(event.getAuthor().getStringID()).getMusicStats().incrNumSongsSkipped();
//        } catch (Exception ignored) {}
    }

    public synchronized static void skipNumTracks(MessageReceivedEvent event, int numToSkip) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        //
        List<AudioTrack> tracklist = musicManager.getScheduler().getQueue();
        musicManager.getScheduler().setQueue(tracklist.subList(numToSkip, tracklist.size())); //removed size - 1

        BotUtils.reactWithCheckMark(event.getMessage());
        //BotUtils.send(channel, "Skipped " + numToSkip + (numToSkip == 1 ? " track" : " tracks"));

        //state update
//        try {
//            MusicStats stats = MasterJsonUtil.jsonObj.getUserMap().get(event.getAuthor().getStringID()).getMusicStats();
//            stats.setNumSongsSkipped(stats.getNumSongsSkipped() + numToSkip);
//        } catch (Exception ignored) {}
    }


}
