package main.utility.warframe.wfstatus.alerts;

/**
 * kait approved fancy
 */
public class WarframeAlert {
    String id;
    String activation;
    String expiry;
    WarframeMission mission;
    boolean expired;
    String eta;
    Object[] rewardTypes; // not good.


    public String getId() {
        return id;
    }

    public String getActivation() {
        return activation;
    }

    public String getExpiry() {
        return expiry;
    }

    public WarframeMission getMission() {
        return mission;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getEta() {
        return eta;
    }

    public Object[] getRewardTypes() {
        return rewardTypes;
    }
}
