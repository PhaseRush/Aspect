package main.utility.mooshroom.RealmEye.PlayerUtil;

import main.utility.Visuals;
import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class RealmEyePlayer {
    private String jsonURL;
    String userJson = "";

    private String name = "";
    private int characterCount = 0;
    private int skins = 0;
    private int fame = 0;
    private int xp = 0;
    private int rank = 0;
    private int account_fame = 0;
    private String guild = "";
    private String guild_rank = "";
    private String created = "";
    private String last_seen = "";
    private String[] description = {};

    private List<Character> characters = new ArrayList<>();

    public RealmEyePlayer(String jsonUrl) { // hard part
        this.jsonURL = jsonUrl;
    }

    public String[] getDescription() {
        return description;
    }

    public int getTotalOutOf8() {
        int total = 0;
        for (Character character: characters){
            // in the form: "1/8"
            total += Integer.parseInt(character.getStats_maxed().substring(0,1));
        }
        return total;
    }

    public int getNumChars() {
        return characters.size();
    }

    public String getName() {
        return name;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public double getTotalCharScore() {
        double runningTotal = 0;
        for (Character c: characters) {
            runningTotal += c.katScore();
        }
        return  runningTotal;
    }

    public EmbedBuilder charsToEmbed(String authorURL) {
        Visuals visuals = new Visuals();
        EmbedBuilder eb = visuals.getEmbedBuilderNoField("Scoring V1.1", "https://cdn.discordapp.com/embed/avatars/0.png", "description",
                visuals.getRandVibrandColour(), System.currentTimeMillis(), "http://i.imgur.com/QRUPiHQ.gif",
                name, "https://cdn.discordapp.com/embed/avatars/0.png", authorURL);


        int runningTotalFame = 0;

        for (Character c : characters) {
            if(!c.classHasBeenDetermined)
                c.determineClass();

            runningTotalFame += c.getFame();
            eb.appendField(new EmbedField(":arrow_forward:" + c.get_class() + ", " +c.getStats_maxed(), String.valueOf((long)(c.katScore() * 1e3) / 1e3), false));
        }
        eb.appendField("Total Fame", String.valueOf(runningTotalFame), false);


        double totalScore = (long)(getTotalCharScore() * 1e3) / 1e3;
        eb.appendField("Totally Objectively Undisputably Perfect Kat Score", String.valueOf(totalScore), false);
        eb.appendField("Average Score per Character", String.valueOf((long) ((totalScore/characters.size())* 1e3) / 1e3), false);
        return eb;
    }
}
