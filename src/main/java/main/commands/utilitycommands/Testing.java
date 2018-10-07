package main.commands.utilitycommands;

import main.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Testing implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        File file = new File("~/test.txt");
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("test text");
            output.close();
        } catch (IOException e){
            System.out.println("rip file writing");
        }
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
