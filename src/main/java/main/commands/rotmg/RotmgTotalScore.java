package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class RotmgTotalScore implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long startTime = System.currentTimeMillis();
        String realmEyeUrl = "http://www.tiffit.net/RealmInfo/api/user?u="+ args.get(0) + "&f=";
        String json = BotUtils.getJson(realmEyeUrl);
        handleRealmEyeTotalCharScore(json, event.getChannel(), event.getAuthor(), startTime);

    }

    @Override
    public boolean requiresElevation() {
        return false;
    }
    private void handleRealmEyeTotalCharScore(String json, IChannel channel, IUser author, double startTime) {
        RealmEyePlayer player = new Gson().fromJson(json, RealmEyePlayer.class);
        EmbedBuilder embed = player.charsToEmbed(author.getAvatarURL());

        RequestBuffer.request(() -> channel.sendMessage(embed.withFooterText("This operation took me " + (System.currentTimeMillis() - startTime) + "ms to compute :3").build()));
    }
}
