package main.utility.mooshroom.ItemUtil;

public class RotmgItem {

    private String id;
    private int slotType;
    private int tier;
    private int x;
    private int y;
    private int fameBonus;
    private int feedPower;
    private int bagType;
    private boolean soulbound;
    private int utst;


    public RotmgItem(String id, int slotType, int tier, int x, int y, int fameBonus, int feedPower, int bagType, boolean soulbound, int utst) {
        this.id = id;
        this.slotType = slotType;
        this.tier = tier;
        this.x = x;
        this.y = y;
        this.fameBonus = fameBonus;
        this.feedPower = feedPower;
        this.bagType = bagType;
        this.soulbound = soulbound;
        this.utst = utst;
    }

    // todo needs work
    public double getFeedPowerScore() {
        return feedPower/30;
    }



    public String getId() {
        return id;
    }

    public int getSlotType() {
        return slotType;
    }

    public int getTier() {
        return tier;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFameBonus() {
        return fameBonus;
    }

    public int getFeedPower() {
        return feedPower;
    }

    public int getBagType() {
        return bagType;
    }

    public boolean isSoulbound() {
        return soulbound;
    }

    public int getUtst() {
        return utst;
    }
}

