package main.utility.maths;

import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.List;

public class NumberGuessManager {
    private List<NumberGuess> numberGuesses = new ArrayList<>();
    private IGuild iGuild;

    public NumberGuessManager(IGuild iGuild) {
        this.iGuild = iGuild;
    }
    public NumberGuessManager() {

    }

    public void addNumberGuess(NumberGuess numberGuess) {
        numberGuesses.add(numberGuess);
    }
    public void removeNumberGuess(NumberGuess numberGuess) {
        numberGuesses.remove(numberGuess);
    }
}
