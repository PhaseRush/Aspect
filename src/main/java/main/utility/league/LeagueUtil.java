package main.utility.league;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import main.utility.BotUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LeagueUtil {
    //class util
    private static Random r = ThreadLocalRandom.current();


    public static List<String> helpPlastic = new ArrayList<>();
    public static List<String> helpBronze = new ArrayList<>();
    public static List<String> helpSilver = new ArrayList<>();
    public static List<String> helpGold = new ArrayList<>();
    public static List<String> helpPlatinum = new ArrayList<>();
    public static List<String> helpDiamond = new ArrayList<>();
    public static List<String> helpMaster = new ArrayList<>();
    public static List<String> helpChallenger = new ArrayList<>();

    public static Set<String> helpAll = new HashSet<>();


    public static Region parseRegion(String s) {
        if (s.equals("euw")) {
            return Region.EUROPE_WEST;
        } else if (s.equals("kr"))
            return Region.KOREA;

        return Region.NORTH_AMERICA;
    }

    public static String getHelp(String tier) {
        populate();
        tier = tier.toLowerCase();
        switch (tier) {
            case "bronze":
                return BotUtils.getRandomFromListString(helpBronze);
            case "silver":
                return BotUtils.getRandomFromListString(helpSilver);
            case "gold":
                return BotUtils.getRandomFromListString(helpGold);
            case "platinum":
                return BotUtils.getRandomFromListString(helpPlatinum);
            case "diamond": return BotUtils.getRandomFromListString(helpDiamond);
            case "master": return BotUtils.getRandomFromListString(helpMaster);
            case "challenger": return BotUtils.getRandomFromListString(helpChallenger);
            default: return "Kat is really bad at coding and you should never see this";
        }
    }

    public static Item getRandomItem() {
        int size = Orianna.getItems().size();
        return Orianna.getItems().get(r.nextInt(size));
//        ItemList itemList = new ItemList();
//        int itemIndex = r.nextInt(itemList.getData().size());
//
//        int counter = 0;
//        for (String s: itemList.getData().keySet()) {
//
//            if (counter == itemIndex) {
//                return itemList.getData().get(s);
//            } else {
//                counter++;
//            }
//
//        }
//        return null;
    }

    public static void populate() {
        populatePlastic();
        populateBronze();
        populateSilver();
        populateGold();
        populatePlatinum();
        populateDiamond();
        populateMaster();
        populateChallenger();

        helpAll.addAll(helpPlastic);
        helpAll.addAll(helpBronze);
        helpAll.addAll(helpSilver);
        helpAll.addAll(helpGold);
        helpAll.addAll(helpPlatinum);
        helpAll.addAll(helpDiamond);
        helpAll.addAll(helpMaster);
        helpAll.addAll(helpChallenger);
    }

    private static void populateChallenger() {
        helpChallenger.add("Zhonya's Hourglass cannot be activated while dead\n-Jensen");
        helpChallenger.add("Summoner Spells can be activated by using \"d\" and \"f\"\n-DoubleLift");
        helpChallenger.add("When in doubt play Darius bot into Yasuo adc\n-Faker");
    }

    private static void populateMaster() {
    }

    private static void populateDiamond() {
    }

    private static void populatePlatinum() {
    }

    private static void populateGold() {
        helpGold.add("You got your season reward.");
        helpGold.add("If gold is worth 18500 usd per pound, you must be a photon");

    }

    private static void populateSilver() {
        helpSilver.add("Didn't know salt was blood soluble");
        helpSilver.add("Play Annie");
        helpSilver.add("$sudo git good");
    }

    private static void populateBronze() {
        helpBronze.add("Ignorance is bliss. Or at least, complete lack of map awareness");
        helpBronze.add("Play Annie");
        helpBronze.add("Can't demote from silver if you never make silver");
        helpBronze.add("You can purchase items in the shop. Alt-F4 opens said shop");
        helpBronze.add("Focus on killing enemies as they give more gold than minions");
    }

    private static void populatePlastic() {
        helpPlastic.add("Even bots use their summoner spells");
        helpPlastic.add("You can use your keyboard and mouse simultaneously");
        helpPlastic.add("Reduce Reuse Recycle");
        helpPlastic.add("At least you're not ranked");
    }
}
