package main.utility.mooshroom.RealmEye.GuildUtil;

import java.util.ArrayList;
import java.util.List;

// http://www.tiffit.net/RealmInfo/api/guild?g=Mooshroom&f=
public class RealmEyeGuild {

    private String name = "";
    private int memberCount = 0;
    private int characters = 0;

    private RealmEyeFameRank fame;
    private RealmEyeXpRank xp;
    private RealmEyeMostActiveServer most_active;
    private String[] desc; // might need to change to list, probably not
    List<RealmEyeGuildMember> members;


    public RealmEyeGuild() {}

    public List<String> getNames(){
        List<String> names = new ArrayList<>();

        for (RealmEyeGuildMember m :
                members) {
            names.add(m.getName());
        }
        return names;
    }

    public List<RealmEyeGuildMember> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public int getCharacters() {
        return characters;
    }

    public RealmEyeFameRank getFame() {
        return fame;
    }

    public RealmEyeXpRank getXp() {
        return xp;
    }

    public RealmEyeMostActiveServer getMost_active() {
        return most_active;
    }

    public String[] getDesc() {
        return desc;
    }
}
