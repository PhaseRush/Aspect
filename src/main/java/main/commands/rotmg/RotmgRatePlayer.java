package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class RotmgRatePlayer implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String json = BotUtils.getJson("http://www.tiffit.net/RealmInfo/api/user?u="+ args.get(0) + "&f=");
        handleRealmEyeRanking(json, event.getChannel(), event.getAuthor());
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    private void handleRealmEyeRanking(String userJson, IChannel iChannel, IUser author) {
        RealmEyePlayer player = new Gson().fromJson(userJson, RealmEyePlayer.class);
        iChannel.sendMessage(player.getName() + " has a total of " + player.getTotalOutOf8() + " maxed stats over " + player.getNumChars() + " characters.");
    }
    @Override
    public String getDescription() {
        return "ROTMG";
    }
}
