package main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser;

public class FPlayer {
    private String _id;
    private FBattleRoyal br;
    private FSTW stw;
    private String lastUpdate;
    private String userID;
    private String displayName;
    private String displayNameLowerCase;

    public String getId() {
        return _id;
    }

    public FBattleRoyal getBattleRoyal() {
        return br;
    }

    public FSTW getStw() {
        return stw;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameLowerCase() {
        return displayNameLowerCase;
    }
}
