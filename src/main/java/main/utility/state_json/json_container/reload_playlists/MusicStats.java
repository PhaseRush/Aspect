package main.utility.state_json.json_container.reload_playlists;

//this belongs to an EnhancedIUser
public class MusicStats {

    private long numMillisQueued;
    private long numMillisPlayed;
    private int numSongsQueued;
    private int numSongsSkipped;

    public int getNumSongsSkipped() {
        return numSongsSkipped;
    }

    public void setNumSongsSkipped(int numSongsSkipped) {
        this.numSongsSkipped = numSongsSkipped;
    }

    public void incrNumSongsSkipped() {
        numSongsSkipped++;
    }

    public long getNumMillisQueued() {
        return numMillisQueued;
    }

    public void setNumMillisQueued(long numMillisQueued) {
        this.numMillisQueued = numMillisQueued;
    }

    public long getNumMillisPlayed() {
        return numMillisPlayed;
    }

    public void setNumMillisPlayed(long numMillisPlayed) {
        this.numMillisPlayed = numMillisPlayed;
    }

    public int getNumSongsQueued() {
        return numSongsQueued;
    }

    public void setNumSongsQueued(int numSongsQueued) {
        this.numSongsQueued = numSongsQueued;
    }

    public void incrNumSongsQueued() {
        numSongsQueued++;
    }
}
