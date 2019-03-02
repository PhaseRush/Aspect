package main.utility.apexlegends;

import java.util.List;

public class ALPlayerInfo {

    private ALPlayerData data;

    public ALPlayerData getData() {
        return data;
    }

    public class ALPlayerData {
        private String id;
        private String type;
        private List<ALChild> children;
        private ALMetadata metadata;
        private List<ALStat> stats;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public List<ALChild> getChildren() {
            return children;
        }

        public ALMetadata getMetadata() {
            return metadata;
        }

        public List<ALStat> getStats() {
            return stats;
        }
    }

}
