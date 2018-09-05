package main.utility.warframe.market.itemdetail;

public class ItemDetailPayload {
    WarframeDetailedItem[] items_in_set;
    String id;

    public String getId() {
        return id;
    }

    public WarframeDetailedItem[] getItems_in_set() {
        return items_in_set;
    }
}
