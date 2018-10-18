package main.utility.fortnite.fornitetracker;

import main.utility.fortnite.fornitetracker.stats.FortniteTrackerStats;

import java.util.List;

public class FortniteTrackerJsonObj {
    private String accountId;
    private int platformId; // pc = 3
    private String platformName;
    private String platformNameLong;
    private String epicUserHandle;
    private FortniteTrackerStats stats;
    //private Map<String, String> lifeTimeStats;
    private List<FortniteTrackerLifetimeMapEntry> lifeTimeStats;
    private List<FortniteTrackerRecentMatch> recentMatches;

    public String getAccountId() {
        return accountId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformNameLong() {
        return platformNameLong;
    }

    public String getEpicUserHandle() {
        return epicUserHandle;
    }

    public FortniteTrackerStats getStats() {
        return stats;
    }

    public List<FortniteTrackerLifetimeMapEntry> getLifeTimeStats() {
        return lifeTimeStats;
    }

    public List<FortniteTrackerRecentMatch> getRecentMatches() {
        return recentMatches;
    }
}
