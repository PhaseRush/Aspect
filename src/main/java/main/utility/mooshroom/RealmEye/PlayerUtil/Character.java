package main.utility.mooshroom.RealmEye.PlayerUtil;

import main.utility.Visuals;
import main.utility.mooshroom.ItemUtil.ItemUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.Random;

public class Character {

    private String _class = "";
    private int level = 0;
    private String class_quests_completed = "";
    private int fame = 0;
    private int xp = 0;
    private int place = 0;
    private String[] equipment = {};
    private String stats_maxed = "";
    private Stat stats = new Stat();

    double noise = new Random().nextGaussian();
    boolean classHasBeenDetermined = false;
    boolean eqNamesHasBeenParsed = false;
    ItemUtil itemUtil = new ItemUtil();
    private String[] eqNames;

    public void parseEqNames() {
         eqNames = new String[equipment.length];
        for (int i = 0; i < equipment.length - 1; i++) {
            int indexLastSpace = equipment[i].lastIndexOf(" ");
            eqNames[i] = equipment[i].substring(0,indexLastSpace);
        }
        eqNamesHasBeenParsed = true;
    }

    public void determineClass() {
        String ability = equipment[1].toLowerCase();

        if (ability.contains("cloak") || ability.contains("drape")) {
            _class = "Rogue";
        } else if (ability.contains("quiver")) {
            _class = "Archer";
        } else if (ability.contains("spell") || ability.contains("vitamine") || ability.contains("tablet")) {
            _class = "Wizard";
        } else if (ability.contains("tome") || ability.contains("book of geb")) {
            _class = "Priest";
        } else if (ability.contains("helm")) {
            _class = "Warrior";
        } else if (ability.contains("shield") || ability.contains("spiteful")) {
            _class = "Knight";
        } else if (ability.contains("seal")) {
            _class = "Paladin";
        }else if (ability.contains("skull") || ability.contains("momento")) {
            _class = "Necromancer";
        } else if (ability.contains("trap") || ability.contains("snare")) {
            _class = "Huntress";
        } else if (ability.contains("poison") || ability.contains("venom") || ability.contains("toxin")) {
            _class = "Assassin";
        } else if (ability.contains("orb") || ability.contains("bearer")) {
            _class = "Mystic";
        } else if (ability.contains("prism") || ability.contains("golem")) {
            _class = "Trickster";
        } else if (ability.contains("scepter")) {
            _class = "Sorcerer";
        } else if (ability.contains("star") || ability.contains("circle") || ability.contains("shuriken") || ability.contains("kageboshi")) {
            _class = "Ninja";
        } else _class = "undetermined";

        classHasBeenDetermined = true;
    }

    public String get_class() {
        if (!classHasBeenDetermined)
            determineClass();
        return _class;
    }


    public void toEmbed(IChannel iChannel, IUser author, String playerIGN, String realmEyeJsonUrl, long startTime) {
        if (!eqNamesHasBeenParsed)
            parseEqNames();

        EmbedBuilder temp = new EmbedBuilder();


        temp.withTitle(playerIGN.substring(0,1).toUpperCase() + playerIGN.substring(1)) //might format later
                .withUrl("https://realmeye.com/players/"+playerIGN)
                .withDesc("```raw[" + realmEyeJsonUrl +"]```")
                .withColor(new Visuals().getRandVibrantColour())
                .withTimestamp(System.currentTimeMillis())
                .withThumbnail("https://www.realmeye.com/s/c5/img/eye-big.png")
                //.withAuthorName("Knight, 4/8")
                .withAuthorName(get_class() + ", " + stats_maxed)
                .withAuthorIcon("https://www.realmeye.com/s/c5/img/eye-big.png")
                .withAuthorUrl("https://realmeye.com/players"+playerIGN)
                .appendField("Weapon", equipment[0] + "  ", false)
                .appendField("Ability", equipment[1] +", " + itemUtil.getItemFameBonus(eqNames[1]) + "%", false)
                .appendField("Armor", equipment[2] + ", Feedpower: " +itemUtil.getItemFeedPower(eqNames[2]), false)
                .appendField("Ring", equipment[3] + ", " + (itemUtil.getItemSoulbound(eqNames[3])? "Soulbound": ""), false)
                .appendField("Alive Fame", String.valueOf(fame), true)
                .appendField("Objectively Correct Kat Ranking Score", String.valueOf((katScore()*1e2)/1e2), true)
                .build();

        RequestBuffer.request(() ->
                iChannel.sendMessage(temp.withFooterText("This operation took me " + (System.currentTimeMillis() - startTime) + "ms.").build()));
    }

    public double katScore() {

        if (!classHasBeenDetermined)
            determineClass();

        double score = 100;
        double classScore = getClassScore();
        double fameScore = getFameScore();
        double equipScore = getEquipScore();
        double outOf8Weight = getOutOf8Score();

        return score * classScore * fameScore * equipScore * outOf8Weight; //no noise for now
    }

    private double getClassScore() {
        if (!classHasBeenDetermined)
            determineClass();

        double classScore = 1;

        if (_class.equals("Rogue")) {
            return 1;
        } else if (_class.equals("Archer")) {
            return 1;
        } else if (_class.equals("Wizard")) {
            return 1;
        } else if (_class.equals("Priest")) {
            return 1;
        } else if (_class.equals("Warrior")) {
            return 1;
        } else if (_class.equals("Knight")) {
            return 1;
        } else if (_class.equals("Paladin")) {
            return 1;
        } else if (_class.equals("Assassin")) {
            return 1;
        } else if (_class.equals("Necromancer")) {
            return 1;
        } else if (_class.equals("Huntress")) {
            return 1;
        } else if (_class.equals("Mystic")) {
            return 1;
        } else if (_class.equals("Trickster")) {
            return 1;
        } else if (_class.equals("Sorcerer")) {
            return 1;
        } else if (_class.equals("Ninja")) {
            return 1;
        } else if (_class.contains("undetermined")) {
            return 1;
        }
        return classScore;
    }

    private double getOutOf8Score() {
        return Math.sqrt(Double.parseDouble(stats_maxed.substring(0,1))+ 1)/7;// 0/8 -> 1/9, weighted 100% @ 6/8
    }

    private double getFameScore() {
        return Math.sqrt(fame+1); //for 0 fame chars
        //return maths.log10(fame + 1); //<- probably too mean
    }

    private double getEquipScore() {
        double itemScore = 0;

        for (String item : equipment) {
            itemScore += itemTierScore(item);
        }
        return itemScore/30 + 1/30;
    }

    private double itemTierScore(String itemName){
        int tier = itemUtil.getItemTier(itemName);
        return ((tier == -1) ? 15 : tier);
    }

    private double itemFeedPowerScore(String itemName) {
        return itemUtil.getItemFameBonus(itemName);
    }



    public int getLevel() {
        return level;
    }

    public String getClass_quests_completed() {
        return class_quests_completed;
    }

    public int getFame() {
        return fame;
    }

    public int getXp() {
        return xp;
    }

    public int getPlace() {
        return place;
    }

    public String[] getEquipment() {
        return equipment;
    }

    public String getStats_maxed() {
        return stats_maxed;
    }

    public Stat getStats() {
        return stats;
    }

    public double getNoise() {
        return noise;
    }

    public boolean getClassHasBeenDetermined() {
        return classHasBeenDetermined;
    }
}
