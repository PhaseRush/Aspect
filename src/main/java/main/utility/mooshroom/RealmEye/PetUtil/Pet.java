package main.utility.mooshroom.RealmEye.PetUtil;

import main.utility.Visuals;
import main.utility.mooshroom.RealmEye.PlayerUtil.Ability;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;


public class Pet {
    private String skin = "";
    private String name = "";
    private String rarity = "";
    private String family = "";
    private String place = "";

    private Ability ability1 = new Ability();
    private Ability ability2 = new Ability();
    private Ability ability3 = new Ability();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ability 1: " + ability1.getType());
        sb.append("Ability 2: " + ability2.getType());
        sb.append("Ability 3: " + ability3.getType());

        return sb.toString();
    }


    public void toEmbed(IChannel iChannel, IUser author, String playerIGN, String realmEyeJsonUrl) {
        EmbedBuilder temp = new EmbedBuilder();

        temp.withTitle(playerIGN.toUpperCase().substring(0,1) + playerIGN.toLowerCase().substring(1))
                .withUrl("https://www.realmeye.com/pets-of/"+ playerIGN)
                .withDesc("```[raw](" + realmEyeJsonUrl + ")```")
                .withColor(new Visuals().getRandVibrandColour())
                .withTimestamp(System.currentTimeMillis())
                .withThumbnail("https://cdn.discordapp.com/embed/avatars/0.png")
                .withAuthorName(name + ", " + skin + ": " + rarity + " " + family)
                .withAuthorIcon("https://cdn.discordapp.com/embed/avatars/0.png")
                .withAuthorUrl("https://discordapp.com")
                .appendField(ability1.getType(), ((ability1.getUnlocked()) ? String.valueOf(ability1.getLevel()): "*Locked*"), false)
                .appendField(ability2.getType(), ((ability2.getUnlocked()) ? String.valueOf(ability2.getLevel()): "*Locked*"), false)
                .appendField(ability3.getType(), ((ability3.getUnlocked()) ? String.valueOf(ability3.getLevel()): "*Locked*"), false)
                .build();

        RequestBuffer.request(() -> iChannel.sendMessage(temp.build()));
    }

    public String getSkin() {
        return skin;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public String getFamily() {
        return family;
    }

    public String getPlace() {
        return place;
    }

    public Ability getAbility1() {
        return ability1;
    }

    public Ability getAbility2() {
        return ability2;
    }

    public Ability getAbility3() {
        return ability3;
    }

}
