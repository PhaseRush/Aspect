package main.utility.warframe.market.marketlistings;

public class WarframeListingUser {
    String status;
    int reputation;
    String last_seen;
    String avatar;//no idea what this is, probably string
    String ingame_name;
    int reputation_bonus;
    String id;
    String region; //en

    public String getStatus() {
        return status;
    }

    public int getReputation() {
        return reputation;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIngame_nmae() {
        return ingame_name;
    }

    public int getReputation_bonus() {
        return reputation_bonus;
    }

    public String getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }
}
