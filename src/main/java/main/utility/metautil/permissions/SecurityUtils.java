package main.utility.metautil.permissions;


import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility Class for various things
 *
 * Includes white/black lists for general bot things
 */
public class SecurityUtils {
    private static Map<Long, ServerRestriction> restrictionMap = new HashMap<>();

    static {
    }

    public static ServerRestriction getRestriction(IGuild guild) {
        return restrictionMap.get(guild.getLongID());
    }

    public boolean canPassive(MessageReceivedEvent event) {
        return restrictionMap.get(event.getGuild().getLongID())
                .hasPassivePerms(event.getAuthor());
    }

    public boolean canCommand(MessageReceivedEvent event) {
        return restrictionMap.get(event.getGuild().getLongID())
                .hasCommandPerms(event.getAuthor());
    }
}
