package main.utility.state_json.json_container.reload_playlists;

//this belongs to an EnhancedIUser
public class MusicStats {

    private long totalMillisQueued;
    private long totalMillisPlayed;
    private int totalSongsQueued;

    public long getTotalMillisQueued() {
        return totalMillisQueued;
    }

    public void setTotalMillisQueued(long totalMillisQueued) {
        this.totalMillisQueued = totalMillisQueued;
    }

    public long getTotalMillisPlayed() {
        return totalMillisPlayed;
    }

    public void setTotalMillisPlayed(long totalMillisPlayed) {
        this.totalMillisPlayed = totalMillisPlayed;
    }

    public int getTotalSongsQueued() {
        return totalSongsQueued;
    }

    public void setTotalSongsQueued(int totalSongsQueued) {
        this.totalSongsQueued = totalSongsQueued;
    }
}
