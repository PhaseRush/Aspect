package main.utility.apexlegends;

public class ALChildStat {
    private ALChildStatMetadata metadata;
    private float value;
    private float percentile;
    private int rank;
    private String displayValule;
    private String displayRank;

    public ALChildStatMetadata getMetadata() {
        return metadata;
    }

    public float getValue() {
        return value;
    }

    public float getPercentile() {
        return percentile;
    }

    public int getRank() {
        return rank;
    }

    public String getDisplayValule() {
        return displayValule;
    }

    public String getDisplayRank() {
        return displayRank;
    }
}
