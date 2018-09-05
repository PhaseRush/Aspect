package main.utility.warframe.dynamic;

public class WarframeAlert {
    EventID _id; //re-used
    EventDate Activation;
    EventDate Expiry;

    AlertMissionInfo MissionInfo;

    public EventID get_id() {
        return _id;
    }

    public EventDate getActivation() {
        return Activation;
    }

    public EventDate getExpiry() {
        return Expiry;
    }

    public AlertMissionInfo getMissionInfo() {
        return MissionInfo;
    }
}
