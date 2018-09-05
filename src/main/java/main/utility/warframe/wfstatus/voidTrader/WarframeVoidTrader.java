package main.utility.warframe.wfstatus.voidTrader;

public class WarframeVoidTrader {
    String id;
    String activation;
    String expiry;
    String character;
    String location;
    //
    WarframeVoidTraderInventoryObj[] inventory;
    String psId;
    boolean active;
    String startString;
    String endString;

    public String getId() {
        return id;
    }

    public String getActivation() {
        return activation;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCharacter() {
        return character;
    }

    public String getLocation() {
        return location;
    }

    public WarframeVoidTraderInventoryObj[] getInventory() {
        return inventory;
    }

    public String getPsId() {
        return psId;
    }

    public boolean isActive() {
        return active;
    }

    public String getStartString() {
        return startString;
    }

    public String getEndString() {
        return endString;
    }
}
