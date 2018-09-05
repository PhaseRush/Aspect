package main.utility.warframe.dynamic;

public class AlertMissionInfo {
    String missionType;
    String faction;
    String location;
    String levelOverride;
    String enemySpec;
    int minEnemyLevel;
    int maxEnemyLevel;
    double difficulty;
    int seed;
    int maxWaveNum;
    boolean archwingRequired;
    WarframeMissionReward missionReward;

    public String getMissionType() {
        return missionType;
    }

    public String getFaction() {
        return faction;
    }

    public String getLocation() {
        return location;
    }

    public String getLevelOverride() {
        return levelOverride;
    }

    public String getEnemySpec() {
        return enemySpec;
    }

    public int getMinEnemyLevel() {
        return minEnemyLevel;
    }

    public int getMaxEnemyLevel() {
        return maxEnemyLevel;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public int getSeed() {
        return seed;
    }

    public int getMaxWaveNum() {
        return maxWaveNum;
    }

    public boolean isArchwingRequired() {
        return archwingRequired;
    }

    public WarframeMissionReward getMissionReward() {
        return missionReward;
    }
}
