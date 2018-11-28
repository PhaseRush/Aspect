package main.utility.warframe.dynamic;

public class WorldState {
    String WorldSeed;
    int Version;
    String MobileVersion;
    String BuildLabel;

    int Time;
    int Date;

    WarframeEvent[] Events;

    WarframeGoal[] Goals; // empty

    WarframeAlert[] Alerts;

    // lots of missing stuffs

    DailyDeal[] DailyDeals;


    // missing more stuffs


    public String getWorldSeed() {
        return WorldSeed;
    }

    public int getVersion() {
        return Version;
    }

    public String getMobileVersion() {
        return MobileVersion;
    }

    public String getBuildLabel() {
        return BuildLabel;
    }

    public int getTime() {
        return Time;
    }

    public int getDate() {
        return Date;
    }

    public WarframeEvent[] getEvents() {
        return Events;
    }

    public WarframeGoal[] getGoals() {
        return Goals;
    }

    public WarframeAlert[] getAlerts() {
        return Alerts;
    }

    public DailyDeal[] getDailyDeals() {
        return DailyDeals;
    }
}
