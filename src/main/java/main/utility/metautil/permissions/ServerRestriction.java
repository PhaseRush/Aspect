package main.utility.metautil.permissions;

import main.Main;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IIDLinkedObject;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rules for who gets to chat in a server
 */
public class ServerRestriction implements Restrictable {

    private long guildID; // the guild this class applies to
    private long minPassiveRoleId;
    private long minCommandRoleId;

    public ServerRestriction(IGuild guild, IRole passiveRole, IRole commandRole) {
        this.guildID = guild.getLongID();
        this.minPassiveRoleId = passiveRole.getLongID();
        this.minCommandRoleId = commandRole.getLongID();
    }

    public boolean hasPassivePerms(IUser user) {
        return check(user, this.minPassiveRoleId);
    }

    public boolean hasCommandPerms(IUser user) {
        return check(user, this.minCommandRoleId);
    }

    private boolean check(IUser user, long minRoleId) {
        IGuild guild = Main.client.getGuildByID(guildID);

        List<Long> userRoles = user.getRolesForGuild(guild).stream()
                .map(IIDLinkedObject::getLongID)
                .collect(Collectors.toList());

        return userRoles.indexOf(user.getRolesForGuild(guild).get(1).getLongID()) <
                userRoles.indexOf(Main.client.getRoleByID(minRoleId).getLongID());
    }

}
