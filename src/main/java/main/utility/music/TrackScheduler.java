package main.utility.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.utility.Visuals;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler {
    private volatile List<AudioTrack> queue; //dannie said to make volatile
    private final AudioPlayer player;

    private IMessage currentSongEmbed;
    private boolean looping = false; //might need to make AtomicBoolean

    public TrackScheduler(AudioPlayer player) {
        // Because we will be removing from the "head" of the queue frequently, a LinkedList is a better implementation
        // since all elements won't have to be shifted after removing. Additionally, choosing to add in between the queue
        // will similarly not cause many elements to shift and wil only require a couple of node changes.
        queue = Collections.synchronizedList(new LinkedList<>());
        this.player = player;

        // For encapsulation, keep the listener anonymous.
        player.addListener(new AudioEventAdapter() {
            @Override //@todo make looping here possibly.... and update info currently playing embed.
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
                if(endReason.mayStartNext) {
                    nextTrack();
                }


                currentSongEmbed.delete();

            }
        });
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public synchronized boolean queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        boolean playing = player.startTrack(track, true);

        if(!playing) {
            queue.add(track);
        }

        return playing;
    }

    /**
     * Starts the next track, stopping the current one if it is playing.
     * @return The track that was stopped, null if there wasn't anything playing
     */
    public synchronized AudioTrack nextTrack() {
        AudioTrack currentTrack = player.getPlayingTrack();
        AudioTrack nextTrack = queue.isEmpty() ? null : queue.remove(0);

        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(nextTrack, false);
        return currentTrack;
    }

    /**
     * overriden with event for currentSong embed
     * @param channel text channel that called $play or skip etc.
     * @return
     */
    public synchronized AudioTrack nextTrack(IChannel channel) {
        AudioTrack currentTrack = player.getPlayingTrack();
        AudioTrack nextTrack = queue.isEmpty() ? null : queue.remove(0);

        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(nextTrack, false);

        // TODO: 2018-07-22
        //handle songinfo for next track
        handleFloatingPlayer(nextTrack, channel);

        return currentTrack;
    }

    private void handleFloatingPlayer(AudioTrack nextTrack, IChannel channel) {
        if (nextTrack == null) return;
        AudioTrackInfo info = nextTrack.getInfo();
        String desc = info.title + "\nby:\t" +info.author + "\nlength:\t" + info.length;

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Aspect :: Floating Music Player")
                .withUrl(info.uri)
                .withDesc(desc)
                .withColor(Visuals.getVibrantColor());

        //channel.getGuild().getDefaultChannel() //or something
        try {
            currentSongEmbed = channel.sendMessage(eb.build());
        } catch (RateLimitException e) {
            //person skipping too much, triggered rate limitation
        }
        currentSongEmbed.edit(eb.withDesc(desc).build());
    }


    private String trackProgress(long startTime) {
        long duration = getCurrentTrack().getDuration();
        long position = getCurrentTrack().getPosition();

        StringBuilder sb = new StringBuilder("```");


        sb.append("```");
        return null;
    }

    /**
     * Returns the queue for this scheduler. Adding to the head of the queue (index 0) does not automatically
     * cause it to start playing immediately. The returned collection is thread-safe and can be modified.
     *
     * @apiNote To iterate over this queue, use a synchronized block. For example:
     * {@code synchronize (getQueue()) { // iteration code } }
     */
    public List<AudioTrack> getQueue() {
        return this.queue;
    }

    public void setQueue(List<AudioTrack> newQueue) {
        queue = newQueue;
    }

    /**
     * Gets the current Track for the current song info command
     *
     * @return
     */
    public AudioTrack getCurrentTrack() {
        return player.getPlayingTrack();
    }
}
