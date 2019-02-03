package main.commands.utilitycommands.guildutil;

import main.Command;
import main.Main;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Sarah is the best <3
 */
public class GetUserPfp implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        List<IUser> users = new ArrayList<>();

        if (!event.getMessage().getMentions().isEmpty()) {
            users.addAll(event.getMessage().getMentions());
        } else {
            try {
                users.add(Main.client.getUserByID(Long.valueOf(args.get(0))));
            } catch (Exception e) {
                BotUtils.send(event.getChannel(), "Invalid parameter. Use the person's ID (@User not supported yet)");
                return;
            }
            if (users.isEmpty()) {
                BotUtils.send(event.getChannel(), "Cannot parse userID. Is the person from this server, or in a server that Aspect is in?");
                return;
            }
        }


        // at this point, we have all our users. we need to get their profiles now

        StringBuilder sb = new StringBuilder();
        users.forEach(u -> sb.append(BotUtils.getNickOrDefault(u, event.getGuild())).append("\t").append(u.getAvatarURL()).append("\n"));

        BotUtils.send(event.getChannel(), sb.toString());
    }

    @Override
    public String getDesc() {
        return null;
    }
}
