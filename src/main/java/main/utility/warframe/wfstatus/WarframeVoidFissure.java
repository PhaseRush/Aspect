package main.utility.warframe.wfstatus;

public class WarframeVoidFissure {
    String id;
    String node;
    String missionType;
    String enemy;
    String tier;
    int tierNum;
    String activation;
    String expiry;
    boolean expired;
    String eta;


    public String getId() {
        return id;
    }

    public String getNode() {
        return node;
    }

    public String getMissionType() {
        return missionType;
    }

    public String getEnemy() {
        return enemy;
    }

    public String getTier() {
        return tier;
    }

    public int getTierNum() {
        return tierNum;
    }

    public String getActivation() {
        return activation;
    }

    public String getExpiry() {
        return expiry;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getEta() {
        return eta;
    }
}
