package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.mooshroom.RealmEye.PlayerUtil.Character;
import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class RotmgRecentChar implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long startTime = System.currentTimeMillis();
        String realmEyeUrl = "http://www.tiffit.net/RealmInfo/api/user?u="+ args.get(0) + "&f=";
        String json = BotUtils.getJson(realmEyeUrl);
        handleRealmEyeRecentChar(json, event.getChannel(), event.getAuthor(), args.get(0), realmEyeUrl, startTime);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    private void handleRealmEyeRecentChar(String userJson, IChannel iChannel, IUser author, String playerIGN, String realmEyeUrl, long startTime) {
        RealmEyePlayer player = new Gson().fromJson(userJson, RealmEyePlayer.class);
        Character mostRecentChar = player.getCharacters().get(0); //gets first (most recent character)
        mostRecentChar.toEmbed(iChannel, author, playerIGN, realmEyeUrl, startTime);
    }

    @Override
    public String getDescription() {
        return "ROTMG";
    }
}
