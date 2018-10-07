package main.utility.warframe.wfcd.relics;

import java.util.List;

public class RelicObject {
    String tier;
    String state;
    List<RelicReward> rewards;
    String _id;

    public String getTier() {
        return tier;
    }

    public String getState() {
        return state;
    }

    public List<RelicReward> getRewards() {
        return rewards;
    }

    public String get_id() {
        return _id;
    }
}
