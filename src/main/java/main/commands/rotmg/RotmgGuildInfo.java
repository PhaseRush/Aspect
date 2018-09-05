package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.mooshroom.RealmEye.GuildUtil.RealmEyeGuild;
import main.utility.mooshroom.RealmEye.GuildUtil.RealmEyeGuildMember;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class RotmgGuildInfo  implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long startTime = System.currentTimeMillis();
        String realmEyeUrl = "http://www.tiffit.net/RealmInfo/api/guild?g=" + args.get(0).replaceAll(" ", "%20") + "&f=";
        String json = BotUtils.getJson(realmEyeUrl);
        handleRealmEyeGuildHighlight(json, event.getChannel(), startTime);
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    private void handleRealmEyeGuildHighlight(String json, IChannel channel, long startTime) {
        RealmEyeGuild realmGuild = new Gson().fromJson(json, RealmEyeGuild.class);

        String founder = "";
        try {
            founder = realmGuild.getMembers().get(0).getName();
        } catch (NullPointerException e) {
            channel.sendMessage("Error: there is no founder in " + realmGuild.getName());
            return;
        }

        EmbedBuilder eb = Visuals.getEmbedBuilderNoField(founder + ", Founder","https://realmeye.com/player/"+ founder,"desc", Visuals.getVibrantColor(),System.currentTimeMillis(),
                "", realmGuild.getName(), "https://www.realmeye.com/s/c7/img/eye-big.png",
                "https://realmeye.com/guild/"+ (realmGuild.getName().contains(" ") ? realmGuild.getName().replaceAll(" ", "%20") : realmGuild.getName()));

        for (RealmEyeGuildMember member: realmGuild.getMembers()) {
            if (!member.getName().equals("Private"))
                if (member.getGuild_rank().equals("Leader")) {
                    eb.appendField(member.getName() + ", *Leader*", "content ph", false);
                }
        }
        channel.sendMessage(eb.withFooterText("This operation took me " + String.valueOf((System.currentTimeMillis() - startTime)) + "ms to compute :3").build());
        System.out.println("triggered guildinfo highlight");
    }
}
