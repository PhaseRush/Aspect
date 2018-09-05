package main.utility.warframe.wfstatus.alerts;

public class WarframeMission {
    String node;
    String type;
    String faction;
    WarframeReward reward;
    int minEnemyLevel;
    int maxEnemyLevel;
    int maxWaveNum;
    boolean nightmare;
    boolean archwingRequired;

    public String getNode() {
        return node;
    }

    public String getType() {
        return type;
    }

    public String getFaction() {
        return faction;
    }

    public WarframeReward getReward() {
        return reward;
    }

    public int getMinEnemyLevel() {
        return minEnemyLevel;
    }

    public int getMaxEnemyLevel() {
        return maxEnemyLevel;
    }

    public int getMaxWaveNum() {
        return maxWaveNum;
    }

    public boolean isNightmare() {
        return nightmare;
    }

    public boolean getArchwingRequired() {
        return archwingRequired;
    }
}
