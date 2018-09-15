package main.commands.dontopendeadinside;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;
import java.util.Random;

public class Love implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        handleLoveCommand(event.getAuthor(), event.getChannel());
    }

    private void handleLoveCommand(IUser author, IChannel channel) { //TODO literally the most fucked up function you've ever seen in your life. Case study on how to not generate random numbers
        int max = 100; //for abstraction
        double gaussian = (new Random().nextGaussian()) *.3  + 1; //https://www.desmos.com/calculator/0x3rpqtgrx
        gaussian *= 50; //center at 50
        double roll = Math.abs(Math.round(gaussian*1e5/1e5)); //round and cut off decimal

        //to make people feel better
        roll += 10;

        while (roll > 100) {
            roll -= roll - 100;
        }

        if (roll > .9*max) {
            channel.sendMessage("There must be an ionic bond between us, " + "<@" + author.getStringID()+ ">" + "! The difference in our electronegativities is ..." + roll/40 + "! (" + roll + "/100)");
        } else if (roll > .7 * max) {
            channel.sendMessage("There must be a polar covalent bond between us, " + "<@" + author.getStringID()+ ">" + "!  (" + roll + "/100)");
        } else if (roll > .5 * max) {
            channel.sendMessage("The chemistry between us is... okay, " + "<@" + author.getStringID()+ ">" + ". Its probably a hydrogen bond. (" + roll + "/100)");
        } else if (roll > .3 * max) {
            channel.sendMessage("Hmmm, " + "<@" + author.getStringID()+ ">" + "; there seems to be a dipole-dipole attraction. (" + roll + "/100)");
        } else { //roll <= .3 * max
            channel.sendMessage("I~I think we should break it off, " + "<@" + author.getStringID()+ ">" +"; Van der Waals forces are very weak ...  :broken_heart: (" + roll + "/100)");
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }


}
