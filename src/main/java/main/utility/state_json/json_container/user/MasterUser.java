package main.utility.state_json.json_container.user;

import main.utility.state_json.json_container.Reminder;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class MasterUser {

    IUser iUser; //base user obj

    String name;
    List<IGuild> guilds; //servers this person is in (that are visible to aspect)
    //nicknames?

    String stringId;
    long longId;

    List<Reminder> reminders;

    //stats
    int numCommandsCalled; //might want to make a initDataMap for each command
    int songsQueued; //might change this to played;
    long totalMusicTime;

}
