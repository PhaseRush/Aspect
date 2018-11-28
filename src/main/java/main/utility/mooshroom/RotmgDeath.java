package main.utility.mooshroom;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class RotmgDeath {
    private String playerName;
    private String playerIGN;
    private String playerClass;
    private int playerOutOfEight;
    private Date timeOfDeath;

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, MM/dd"); // might get rid of YEAR later


    public RotmgDeath(String playerName, String playerIGN, String playerClass, int playerOutOfEight, Date timeOfDeath) {
        this.playerName = playerName;
        this.playerIGN = playerIGN;
        this.playerClass = playerClass;
        this.playerOutOfEight = playerOutOfEight;
        this.timeOfDeath = timeOfDeath;
    }

    public RotmgDeath(String playerName, String playerIGN, String playerClass, int playerOutOfEight) {
        this.playerName = playerName;
        this.playerIGN = playerIGN;
        this.playerClass = playerClass;
        this.playerOutOfEight = playerOutOfEight;
        this.timeOfDeath = Date.from(Instant.now());
    }

    private long elapsedHours(Date date) {
        long elapsedCurrentParameterTime = date.getTime();
        long elapsedDeathTime = timeOfDeath.getTime();

        return elapsedCurrentParameterTime - elapsedDeathTime;
    }

    public long elapsedTimeHours(Date date) {
        return elapsedHours(date)/(1000*60*60);
    }

    @Override
    public String toString() {
        return playerName + " died as a " + playerOutOfEight + "/8 " + playerClass + " on " + formatter.format(timeOfDeath) + ". It has been " + elapsedHours(timeOfDeath) + " hours since the death";
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerIGN() {
        return playerIGN;
    }

    public int getPlayerOutOfEight() {
        return playerOutOfEight;
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public Date getTimeOfDeath() {
        return timeOfDeath;
    }
}
