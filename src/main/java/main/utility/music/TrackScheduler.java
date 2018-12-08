package main.utility.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

//import org.jetbrains.annotations.NotNull;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 *
 * ----
 * HEAVILY MODIFIED -- queue shouldnt jump anymore.
 * ----
 */
public class TrackScheduler {
    private volatile List<AudioTrack> queue; //dannie said to make volatile
    private final AudioPlayer player;

    private Random r = ThreadLocalRandom.current();

    private volatile boolean looping = false; //might need to make AtomicBoolean --//added volatile
    private int loopCount;
    private int maxLoop = -1;

    private IMessage currentSongEmbed;
    private IMessage lastSongEmbed; //added as temp to delete previous embed
    private AudioTrack previousTrack; //track that just ended -- keep track for looping -- debug
    private AudioTrack currentTrack;
    public IChannel lastEmbedChannel; //public so accessible from masterManager


    //fancy shmancy stuff
    private static ThreadGroup floatingPlayer = new ThreadGroup("Floating Music Player");
    private final ScheduledExecutorService floatingPlayerScheduler = Executors.newScheduledThreadPool(1);//not sure @todo
    private ScheduledFuture<?> trackEmbedUpdater;

    //private volatile IChannel currentCallChannel;

    public TrackScheduler(AudioPlayer player) {
        // Because we will be removing from the "head" of the queue frequently, a LinkedList is a better implementation
        // since all elements won't have to be shifted after removing. Additionally, choosing to add in between the queue
        // will similarly not cause many elements to shift and wil only require a couple of node changes.
        queue = Collections.synchronizedList(new LinkedList<>());
        this.player = player;

        // For encapsulation, keep the listener anonymous.
        player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                //always update lastTrack before anything else, so will never be null
                previousTrack = track;
                // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
                if(endReason.mayStartNext) {
                    //check looping condition
                    if (looping) {
                        System.out.println("Looping, count = " + loopCount);
                        if (loopCount == maxLoop) { //end loop, behave as if else below (WORKING)
                            //System.out.println("called nextTrack (is looping, but reached max loop)");
                            loopCount = 0;
                            maxLoop = -1;
                            looping = false;
                            currentSongEmbed.delete(); //@TODO MIGHT BE ISSUE WITH MULTITHREADING  or update later on @todo poop tonight at 8:00 (thanks luis)
                            nextTrack();
                        } else { //(NOT WORKING)
                            //player.startTrack(previousTrack.makeClone(), false); //< -- could be this?
                            currentSongEmbed.delete();
                            queue.add(0, previousTrack.makeClone());
                            //player.startTrack(currentTrack.makeClone(), false); //FAIL - deletes entire queue
                            nextTrack();

                            //System.out.println("called player.startTrack is looping");
                            loopCount++;
                        }
                    } else { //not looping (WORKING)
                        //System.out.println("called nextTrack (not looping)");
                        loopCount = 0;
                        maxLoop = -1;
                        currentSongEmbed.delete();
                        nextTrack();
                    }
                }
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

        if (playing)
            handleFloatingPlayer(track);

        if (!playing){
            queue.add(track);
        }

        return playing;
    }

    public synchronized boolean queueFront(AudioTrack track) {
        boolean playing = player.startTrack(track, true);

        if(playing)
            handleFloatingPlayer(track);

        if(!playing) {
            queue.add(0,track);
        }

        return playing;
    }

    /**
     * Starts the next track, stopping the current one if it is playing.
     * @return The track that was stopped, null if there wasn't anything playing
     */
    public synchronized AudioTrack nextTrack() { //TEMP: trace 1
        AudioTrack currentTrack = player.getPlayingTrack();
        AudioTrack nextTrack = queue.isEmpty() ? null : queue.remove(0);

        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(nextTrack, false);
        currentTrack = nextTrack;
        handleFloatingPlayer(nextTrack); //newly added <- TRACE to 2
        return currentTrack;
    }

    /**
     * overriden with event for currentSong embed
     *
     * @param channel text channel that called $play or skip etc.
     * @return
     */
    public synchronized AudioTrack nextTrack(IChannel channel) { //called from skip - FLOATING PLAYER WORKS CORRECTLY - 2018-10-25
        this.lastEmbedChannel = channel;
        AudioTrack currentTrack = player.getPlayingTrack(); //redundant assignment?
        AudioTrack nextTrack = queue.isEmpty() ? null : queue.remove(0);

        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        if (currentSongEmbed !=null) //debug purposes
            currentSongEmbed.delete(); //delete the embed before starting next song.
        player.startTrack(nextTrack, false);
        currentTrack = nextTrack; //for looping

        // TODO: 2018-07-22 <- immortalize this
        //handle songinfo for next tracks
        handleFloatingPlayer(nextTrack);

        return currentTrack;
    }

    //gets called once per track
    private void handleFloatingPlayer(AudioTrack nextTrack) { //TRACE 2
        //simplification thanks to Dannie --https://discordapp.com/channels/208023865127862272/208296822156820481/515593523274448899
        if (trackEmbedUpdater != null && currentSongEmbed == null) //will be null on the very first call (no prev. embed)
            trackEmbedUpdater.cancel(true); //clear out the last song's embed updater

        if (nextTrack == null || lastEmbedChannel == null) return;

        EmbedBuilder eb = generateCurrentTrackEmbed(nextTrack);
        try { currentSongEmbed = lastEmbedChannel.sendMessage(eb.build());
        } catch (RateLimitException e) { //person skipping too much, triggered rate limitation
            System.out.println("handlefloatingplayer getting ratelimited");
            //e.printStackTrace();
        }

        handleTimelineUpdate();

        //handle reactions on currentSongEmbed -- get rid of this for now
        //BotUtils.reactAllEmojis(currentSongEmbed, MusicUtils.floatingReactions);
    }

    private void handleTimelineUpdate() {
        //make the timeline updater
        final Runnable trackTimelineUpdater = () -> currentSongEmbed.edit(generateCurrentTrackEmbed(getCurrentTrack()).build());
        long onePercentDuration = getCurrentTrack().getDuration()/100;
        long refreshTime = (onePercentDuration > 1000 ? onePercentDuration : 1000); //use min of 1 second
        //set the current updater to this update runner
        trackEmbedUpdater = floatingPlayerScheduler.scheduleAtFixedRate(trackTimelineUpdater, refreshTime, refreshTime*2, TimeUnit.MILLISECONDS);
    }

    public synchronized EmbedBuilder generateCurrentTrackEmbed(AudioTrack audioTrack) {
        AudioTrackInfo songInfo = audioTrack.getInfo();

        return new EmbedBuilder()
                .withColor(generateBiasedColor())
                .withTitle(songInfo.title)
                .withDesc("By: " + songInfo.author + "\n" + trackProgress())
                .withUrl(songInfo.uri);
    }

    //@NotNull //got rid of this and the import

    /**
     * Generates a "seeded" colour based on the previous colour.
     * For use in conjunction with the floating music player.
     * @return Color which is the next color to be used by the floating music player
     */
    private Color generateBiasedColor() {
        if (currentSongEmbed == null) {
            return Visuals.getVibrantColor();
        }else { //hue could overflow
            Color color = currentSongEmbed.getEmbeds().get(0).getColor();
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            try {
                return Color.getHSBColor(hsb[0] + r.nextFloat() / 25, hsb[1], hsb[2]); //hsb[1] = 0.9f, hsb[2] = 1.0f
            } catch (Exception e) { //catch if hue is > 1 (there is a chance, so just reset it with random color)
                return Visuals.getVibrantColor();
            }
        }
    }

    public StringBuilder trackProgress() {
        int lengthFactor = 2;
        long duration = getCurrentTrack().getDuration();
        long position = getCurrentTrack().getPosition();
        long percent = 100*position / (duration*lengthFactor); //*2
        String marker = ":red_circle:";
        String filler = "-";

        StringBuilder sb = new StringBuilder("\n[0:00][");

        for (double d = 0; d < percent; d++)
            sb.append(filler);

        sb.append(marker);

        for (double d = percent; d < 100/lengthFactor - 1; d++) //-1 for the auto track updater not reaching the end
            sb.append(filler);

        sb.append("]["+ getFormattedSongLength(getCurrentTrack().getInfo()) + "]");
        return sb;
    }

    public void deleteCurrentEmbed() {
        if (currentSongEmbed != null) currentSongEmbed.delete();
    }

    public void setLooping(boolean b, int loopCount) {
        looping = b;
        this.maxLoop = loopCount;
    }

    public boolean isLooping() {
        return looping;
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
        //synchronized (queue = newQueue){} //lol does this work???
        queue = newQueue;
    }

    public void clearQueue() {
        queue = Collections.synchronizedList(new LinkedList<>());
    }

    private String getFormattedSongLength(AudioTrackInfo songInfo) {
        long millis = songInfo.length;
        int mins = (int) (millis / 1000 / 60);
        int secs = (int) ((millis / 1000) % 60);

        return mins + ":" + (secs < 10 ? "0" + secs : secs);
    }
    /**
     * Gets the current Track for the current song info command
     *
     * @return
     */
    public AudioTrack getCurrentTrack() {
        return player.getPlayingTrack();
    }

    public long getQueueDurationMillis() {
        return queue.stream().mapToLong(AudioTrack::getDuration).sum();
//        long l = 0;
//        for (AudioTrack a : queue)
//            l += a.getDuration();
//        return l;
    }

    public String getQueueHMS() {
        return BotUtils.millisToHMS(getQueueDurationMillis());
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
