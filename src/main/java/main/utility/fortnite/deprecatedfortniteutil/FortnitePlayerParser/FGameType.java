package main.utility.fortnite.deprecatedfortniteutil.FortnitePlayerParser;

//'solo', 'duo', 'squad', 'all'
public class FGameType {
    private int kills;
    private int matchesPlayed;
    private String lastMatch;
    private int minutesPlayed;
    private int wins;
    private int top10;
    private int top25;
    private int deaths;
    private double kpd;
    private double kpm;
    private double tpm;
    private int score;
    private double winRate;

    public int getKills() {
        return kills;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public String getLastMatch() {
        return lastMatch;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getTop10() {
        return top10;
    }

    public int getTop25() {
        return top25;
    }

    public int getDeaths() {
        return deaths;
    }

    public double getKpd() {
        return kpd;
    }

    public double getKpm() {
        return kpm;
    }

    public double getTpm() {
        return tpm;
    }

    public int getScore() {
        return score;
    }

    public double getWinrate() {
        return winRate;
    }
}
