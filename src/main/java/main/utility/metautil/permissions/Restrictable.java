package main.utility.metautil.permissions;

import sx.blah.discord.handle.obj.IUser;

public interface Restrictable {

    default boolean hasPassivePerms(IUser user) {
        return true;
    }

    default boolean hasCommandPerms(IUser user) {
        return true;
    }
}
