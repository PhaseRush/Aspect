package main.commands.rotmg;

import com.google.gson.Gson;
import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.mooshroom.RealmEye.PetUtil.Pet;
import main.utility.mooshroom.RealmEye.PetUtil.RealmEyePets;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class RotmgPets implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String realmEyeUrl = "http://www.tiffit.net/RealmInfo/api/pets-of?u=" + args.get(0);
        String json = BotUtils.getJson(realmEyeUrl);
        handleRealmEyePets(json, event.getChannel(), event.getAuthor(), args.get(0), realmEyeUrl);
    }

    private void handleRealmEyePets(String userJson, IChannel iChannel, IUser author, String playerIGN, String realmEyeUrl) {
        RealmEyePets pets = new Gson().fromJson(userJson, RealmEyePets.class);
        Pet highestLevelPet = pets.findHighestLevelPet();
        highestLevelPet.toEmbed(iChannel, author, playerIGN, realmEyeUrl); //can throw NPE
    }

    @Override
    public String getDesc() {
        return "ROTMG";
    }
}
