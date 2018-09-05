package main.utility.warframe.dynamic;

public class WarframeEvent {
    EventID _id;
    EventMessages[] Messages;
    String Prop;
    EventDate date;
    String ImageUrl;

    boolean Priority;
    boolean MobileOnly;

    public EventID get_id() {
        return _id;
    }

    public EventMessages[] getMessages() {
        return Messages;
    }

    public String getProp() {
        return Prop;
    }

    public EventDate getDate() {
        return date;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public boolean isPriority() {
        return Priority;
    }

    public boolean isMobileOnly() {
        return MobileOnly;
    }
}
