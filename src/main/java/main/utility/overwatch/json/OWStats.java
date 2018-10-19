package main.utility.overwatch.json;

public class OWStats {
    private double eliminationsAvg;
    private double damageDoneAvg;
    private double deathsAvg;
    private double finalBlowsAvg;
    private double healingDoneAvg;
    private double objectiveKillsAvg;
    private String objectiveTimeAvg;
    private double soloKilsAvg;
    private OWGames games;
    private OWAwards awards;

    public double getEliminationsAvg() {
        return eliminationsAvg;
    }

    public double getDamageDoneAvg() {
        return damageDoneAvg;
    }

    public double getDeathsAvg() {
        return deathsAvg;
    }

    public double getFinalBlowsAvg() {
        return finalBlowsAvg;
    }

    public double getHealingDoneAvg() {
        return healingDoneAvg;
    }

    public double getObjectiveKillsAvg() {
        return objectiveKillsAvg;
    }

    public String getObjectiveTimeAvg() {
        return objectiveTimeAvg;
    }

    public double getSoloKilsAvg() {
        return soloKilsAvg;
    }

    public OWGames getGames() {
        return games;
    }

    public OWAwards getAwards() {
        return awards;
    }
}
