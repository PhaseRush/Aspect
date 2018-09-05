package main.utility.warframe.dynamic;

public class WarframeJob {
    String jobType;
    String rewards;
    int masteryReq;
    int minEnemyLevel;
    int maxEnemyLevel;
    int[] xpAmounts; //count be problematic?

    public String getJobType() {
        return jobType;
    }

    public String getRewards() {
        return rewards;
    }

    public int getMasteryReq() {
        return masteryReq;
    }

    public int getMinEnemyLevel() {
        return minEnemyLevel;
    }

    public int getMaxEnemyLevel() {
        return maxEnemyLevel;
    }

    public int[] getXpAmounts() {
        return xpAmounts;
    }
}
