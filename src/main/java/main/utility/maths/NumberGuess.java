package main.utility.maths;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.Random;
import java.util.UUID;

public class NumberGuess {
    private final UUID id = UUID.randomUUID(); //id

    private int number;
    private IChannel iChannel;
    private IUser author;

    public NumberGuess(IChannel iChannel, IUser author) {
        number = new Random().nextInt(99)+1; //[1 , 100]
        this.iChannel = iChannel;
        this.author = author;
    }

    public NumberGuess(int highestValue, IChannel iChannel, IUser author) {
        number = new Random().nextInt(highestValue-1) +1;//[1 , highestValue]
        this.iChannel = iChannel;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void sayNumber() {
        iChannel.sendMessage("<@"+author.getStringID()+">, your number is " + number);
    }
}