package main.utility.mooshroom.RealmEye.PlayerUtil;

public class Ability {
    private String type = "";
    private boolean unlocked = false;
    private int level = 0;

    public int getLevel() {
        return level;
    }
    public String getType() {
        return type;
    }

    public boolean getUnlocked() {
        return unlocked;
    }
}
