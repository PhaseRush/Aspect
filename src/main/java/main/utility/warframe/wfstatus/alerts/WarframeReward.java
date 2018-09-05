package main.utility.warframe.wfstatus.alerts;

public class WarframeReward {
    String[] items; //might just be Object[] items;
    WarframeCountedItem[] countedItems;
    int credits;
    String asString;
    String itemString;
    String thumbnail;
    double color; //might be something else

    public String[] getItems() {
        return items;
    }

    public WarframeCountedItem[] getCountedItems() {
        return countedItems;
    }

    public int getCredits() {
        return credits;
    }

    public String getAsString() {
        return asString;
    }

    public String getItemString() {
        return itemString;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public double getColor() {
        return color;
    }
}
