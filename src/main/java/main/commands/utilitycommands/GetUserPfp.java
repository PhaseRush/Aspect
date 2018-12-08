package main.commands.utilitycommands;

import main.Command;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class GetUserPfp implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IUser user;
        try {
            user = Main.client.getUserByID(Long.valueOf(args.get(0)));
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "Invalid parameter. Use the person's ID (@User not supported yet)");
            return;
        }
        if (user == null) {
            BotUtils.send(event.getChannel(), "Cannot parse userID. Is the person from this server, or in a server that Aspect is in?");
            return;
        }

        BotUtils.send(event.getChannel(), user.getAvatarURL());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
