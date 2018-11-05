package main.utility.state_json.json_container;

import main.utility.state_json.json_container.reload_playlists.MusicStats;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class EnhancedIUser {
    private IUser iUser;
    private String stringId;
    private long longId;

    private List<Reminder> reminders;

    private MusicStats musicStats;
    private CommandStats commandStats;

    public IUser getiUser() {
        return iUser;
    }

    public void setiUser(IUser iUser) {
        this.iUser = iUser;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public long getLongId() {
        return longId;
    }

    public void setLongId(long longId) {
        this.longId = longId;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public MusicStats getMusicStats() {
        return musicStats;
    }

    public void setMusicStats(MusicStats musicStats) {
        this.musicStats = musicStats;
    }

    public CommandStats getCommandStats() {
        return commandStats;
    }

    public void setCommandStats(CommandStats commandStats) {
        this.commandStats = commandStats;
    }
}
