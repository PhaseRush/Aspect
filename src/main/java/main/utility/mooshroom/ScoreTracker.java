package main.utility.mooshroom;

import main.utility.mooshroom.RealmEye.PlayerUtil.RealmEyePlayer;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

public class ScoreTracker {
    final String filePath = "C:\\Users\\Positron\\IdeaProjects\\Kitty_Kat\\txtfiles\\Mooshroom\\scoreTracker.txt";

    private List<RealmEyePlayer> players = new ArrayList<>();
    private List<IUser> users = new ArrayList<>();

    public ScoreTracker(IGuild guild) {
        users = guild.getUsers();
    }

    public double getTotalScore() {
        double total = 0;
        for (RealmEyePlayer p : players)
            total += p.getTotalCharScore();

        return total;
    }

    public void updatePlayerScore(){


    }


}
