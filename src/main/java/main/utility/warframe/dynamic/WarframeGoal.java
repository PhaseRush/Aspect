package main.utility.warframe.dynamic;

public class WarframeGoal {
    EventID _id;
    EventDate Activation;
    EventDate Expiry;

    double HealthPct;
    String VictimNode;
    int RegionIdx;
    int success;
    String Desc;
    String ToolTip;
    String Icon;
    String Tag;
    String JobAffiliationTag;

    WarframeJob[] Jobs;


    public EventID get_id() {
        return _id;
    }

    public EventDate getActivation() {
        return Activation;
    }

    public EventDate getExpiry() {
        return Expiry;
    }

    public double getHealthPct() {
        return HealthPct;
    }

    public String getVictimNode() {
        return VictimNode;
    }

    public int getRegionIdx() {
        return RegionIdx;
    }

    public int getSuccess() {
        return success;
    }

    public String getDesc() {
        return Desc;
    }

    public String getToolTip() {
        return ToolTip;
    }

    public String getIcon() {
        return Icon;
    }

    public String getTag() {
        return Tag;
    }

    public String getJobAffiliationTag() {
        return JobAffiliationTag;
    }

    public WarframeJob[] getJobs() {
        return Jobs;
    }
}
