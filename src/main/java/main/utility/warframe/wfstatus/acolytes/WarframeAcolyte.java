package main.utility.warframe.wfstatus.acolytes;

public class WarframeAcolyte {
    String id;
    String agentType;
    String locationTag;
    int rank;
    double healthPercent;
    int fleeDamage;
    int region;
    String lastDiscoveredTime;
    String lastDiscoveredAt;
    boolean isDiscovered;
    boolean isUsingTicketing;
    String pid;

    public String getId() {
        return id;
    }

    public String getAgentType() {
        return agentType;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public int getRank() {
        return rank;
    }

    public double getHealthPercent() {
        return healthPercent;
    }

    public int getFleeDamage() {
        return fleeDamage;
    }

    public int getRegion() {
        return region;
    }

    public String getLastDiscoveredTime() {
        return lastDiscoveredTime;
    }

    public String getLastDiscoveredAt() {
        return lastDiscoveredAt;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public boolean isUsingTicketing() {
        return isUsingTicketing;
    }

    public String getPid() {
        return pid;
    }
}
