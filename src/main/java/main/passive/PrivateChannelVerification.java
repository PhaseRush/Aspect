package main.passive;

import com.google.gson.Gson;
import main.Main;
import main.utility.BotUtils;
import main.utility.privatechannel.IdKeyInfo;
import main.utility.privatechannel.PrivateIdKeyPairContainer;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import java.util.List;

public class PrivateChannelVerification {
    public static List<IdKeyInfo> verifications;

    static {
        String json = BotUtils.getStringFromUrl(BotUtils.PRIVATE_CHANNEL_INFO_URL);
        verifications = new Gson().fromJson(json, PrivateIdKeyPairContainer.class).getPairs();
    }

    @EventSubscriber
    public void verifyPassword(MessageReceivedEvent event) {
        if (!event.getChannel().isPrivate()) return;
        if (!event.getMessage().getFormattedContent().startsWith("::")) return; //special start case

        String hashedInput = BotUtils.SHA256(event.getMessage().getFormattedContent().substring(2));

        for (IdKeyInfo p : verifications) {
            IChannel targetChannel = Main.client.getChannelByID(p.getLongID());
            IGuild thisGuild = targetChannel.getGuild();
            //this user is in the server that this channel is in.
            if (thisGuild.getUsers().contains(event.getAuthor())) {
                //check hashed password equality
                if (hashedInput.equals(BotUtils.SHA256(p.getPassword()))) {
                    IRole role = Main.client.getRoleByID(p.getLongRoleID());
                    if (event.getAuthor().hasRole(role)) {
                        BotUtils.sendMessage(event.getChannel(), "You already have the role to access `" + targetChannel.getName() + "` in `" + thisGuild.getName() + "`");
                    } else {
                        event.getAuthor().addRole(role);
                        BotUtils.sendMessage(event.getChannel(), "Given `" + role.getName() + "` role to access `" + targetChannel.getName() + "` in `" + thisGuild.getName() + "`");
                    }
                    break;
                } else {
                    //could warn of wrong password
                }
            }
        }


    }

}

