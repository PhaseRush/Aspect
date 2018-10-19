package main.utility.overwatch.json;

public class OverwatchJsonObj {
    private String icon; //icon url
    private String name;
    private int level;
    private String levelIcon; //https://blzgdapipro-a.akamaihd.net/game/playerlevelrewards/0x0250000000000951_Border.png
    private int prestige;
    private String prestigeIcon;
    private int rating;
    private String ratingIcon;
    private int gamesWon;
    private OWStats quickPlayStats;
    private OWStats competitiveStats;

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelIcon() {
        return levelIcon;
    }

    public int getPrestige() {
        return prestige;
    }

    public String getPrestigeIcon() {
        return prestigeIcon;
    }

    public int getRating() {
        return rating;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public OWStats getQuickPlayStats() {
        return quickPlayStats;
    }

    public OWStats getCompetitiveStats() {
        return competitiveStats;
    }
}
