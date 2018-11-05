package main.utility.state_json.json_container;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MasterState {
    //TODO all of this

    //meta
    private Meta meta;
    //random seed?

    //on load
    //cute img ban list
    // general ban list (just danman ofc)
    private MasterBan bans;


    //last time stuff
    //saved playlists for each server
    private LastShutdownState lastShutdownState;


    //invariant
    //admin/dev stringId
    private String devStringId;
    private long devLongId;

    //stats
    //total music played per guild or person
    //number of commands a person called

    //on shutdown
    //uptime

    //StringId -> EnhancedIUser
    private Map<String, EnhancedIUser> userMap;

    private List<String> notes;


    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public MasterBan getBans() {
        return bans;
    }

    public void setBans(MasterBan bans) {
        this.bans = bans;
    }

    public LastShutdownState getLastShutdownState() {
        return lastShutdownState;
    }

    public void setLastShutdownState(LastShutdownState lastShutdownState) {
        this.lastShutdownState = lastShutdownState;
    }

    public String getDevStringId() {
        return devStringId;
    }

    public void setDevStringId(String devStringId) {
        this.devStringId = devStringId;
    }

    public long getDevLongId() {
        return devLongId;
    }

    public void setDevLongId(long devLongId) {
        this.devLongId = devLongId;
    }

    public Map<String, EnhancedIUser> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, EnhancedIUser> userMap) {
        this.userMap = userMap;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    //luis put this here and i dont have the heart to get rid of it
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterState that = (MasterState) o;
        return devLongId == that.devLongId &&
                Objects.equals(bans, that.bans) &&
                Objects.equals(lastShutdownState, that.lastShutdownState) &&
                Objects.equals(devStringId, that.devStringId) &&
                Objects.equals(userMap, that.userMap) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bans, lastShutdownState, devStringId, devLongId, userMap, notes);
    }
}
