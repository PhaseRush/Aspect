package main.utility.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;

public class GuildMusicManager {
    private final AudioPlayer player;
    private final AudioProvider provider;
    private final TrackScheduler scheduler;
    private long thisStartTime;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        provider = new AudioProvider(player);
        scheduler = new TrackScheduler(player);
    }

    /**
     * Adds a listener to be registered for audio events.
     */
    public void addAudioListener(AudioEventListener listener) {
        player.addListener(listener);
    }

    /**
     * Removes a listener that was registered for audio events.
     */
    public void removeAudioListener(AudioEventListener listener) {
        player.removeListener(listener);
    }

    /**
     * @return The scheduler for AudioTracks.
     */
    public TrackScheduler getScheduler() {
        return this.scheduler;
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioProvider getAudioProvider() {
        return provider;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public void setThisStartTime(long thisStartTime) {
        this.thisStartTime = thisStartTime;
    }
}
