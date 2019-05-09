package main.commands.utilitycommands.metautil;

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
        String home = System.getProperty("user.home");
        File file = new File(home + "/AspectTextFiles/TestOutput.txt");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
            output.write("test text");
        } catch (IOException e) {
            Aspect.LOG.info("rip file writing");
        }
    }

    @Override
    public String getDesc() {
        return null;
    }
}
