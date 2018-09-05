package main.utility.user;

import sx.blah.discord.handle.obj.IUser;

public class SimpleIUser {
    IUser fullIUserObject; //usually null, otherwise we don't need this object
    String stringID;
    Long longID;
    String displayName;
    String actualName;
    String discriminator;

    public SimpleIUser(IUser iUser, String stringID, Long longID, String displayName, String actualName, String discriminator) {
        fullIUserObject = iUser;
        this.stringID = stringID;
        this.longID = longID;
        this.displayName = displayName;
        this.actualName = actualName;
        this.discriminator = discriminator;
    }

    public SimpleIUser() {

    }

    public IUser getFullIUserObject() {
        return fullIUserObject;
    }

    public void setFullIUserObject(IUser fullIUserObject) {
        this.fullIUserObject = fullIUserObject;
    }

    public String getStringID() {
        return stringID;
    }

    public void setStringID(String stringID) {
        this.stringID = stringID;
    }

    public Long getLongID() {
        return longID;
    }

    public void setLongID(Long longID) {
        this.longID = longID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
