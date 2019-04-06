package main.utility.miscJsonObj.leaguequotes;

import java.util.Map;

public class LeagueQuoteContainer {
    private int champ_id;
    private Map<String, Map<String,String>> quotes;

    public int getChamp_id() {
        return champ_id;
    }

    public Map<String, Map<String, String>> getQuotes() {
        return quotes;
    }

    //    class LeagueQuotes {
//        @SerializedName("Champion Select")
//        HashMap<String,String> ChampionSelect;
//        HashMap<String,String> Attacking;
//        HashMap<String,String> Movement;
//        HashMap<String,String> Joke;
//        HashMap<String,String> Taunt;
//        HashMap<String,String> Laugh;
//        @SerializedName("Upon.*")
//        HashMap<String,String> UponDeath;
//    }
}
