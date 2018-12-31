package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

public class RotmgReadDescription implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String realmEyeUrl = "http://www.tiffit.net/RealmInfo/api/user?u="+ args.get(0)+ "&f=";
        String json = BotUtils.getJson(realmEyeUrl);
        handleRealmEyeDesc(json, event.getChannel(), args.get(0));
    }

    private void handleRealmEyeDesc(String json, IChannel channel, String playerIGN) {
        RealmEyePlayer player = new Gson().fromJson(json, RealmEyePlayer.class);

        StringBuilder sb = new StringBuilder();
        sb.append(playerIGN.substring(0,1).toUpperCase()+playerIGN.substring(1).toLowerCase() + "'s RealmEye Description:\n");
        sb.append("```");
        for (String s : player.getDescription()) {
            sb.append(s).append("\n");
        }
        sb.append("```");
        channel.sendMessage(sb.toString());
    }

    @Override
    public String getDesc() {
        return "ROTMG";
    }
}
