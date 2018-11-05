package main.utility.state_json.json_container;

import java.util.List;

public class Meta {
    private int numPokemonIdentified;
    private long totalRunningTime;
    private int numRestarts;
    private List<String> notes;


    public int getNumPokemonIdentified() {
        return numPokemonIdentified;
    }

    public void setNumPokemonIdentified(int numPokemonIdentified) {
        this.numPokemonIdentified = numPokemonIdentified;
    }

    public long getTotalRunningTime() {
        return totalRunningTime;
    }

    public void setTotalRunningTime(long totalRunningTime) {
        this.totalRunningTime = totalRunningTime;
    }

    public int getNumRestarts() {
        return numRestarts;
    }

    public void setNumRestarts(int numRestarts) {
        this.numRestarts = numRestarts;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
