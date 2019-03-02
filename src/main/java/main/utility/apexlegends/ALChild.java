package main.utility.apexlegends;

import java.util.List;

public class ALChild {
    private String id;
    private String type;
    private ALChildMetadata metadata;
    private List<ALChildStat> stats;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ALChildMetadata getMetadata() {
        return metadata;
    }

    public List<ALChildStat> getStats() {
        return stats;
    }
}
