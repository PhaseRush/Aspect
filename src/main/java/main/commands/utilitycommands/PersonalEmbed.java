package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import main.utility.CustomEmbeds;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class PersonalEmbed implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            if (args.size() == 0) //self call
                sendEmbed(event, CustomEmbeds.getPersonalEmbedMap().get(event.getAuthor().getStringID()));
            else if (args.size() == 1) {
                if (args.get(0).matches("\\d+")) //pasted an ID
                    sendEmbed(event, CustomEmbeds.getPersonalEmbedMap().get(args.get(0)));
                else { //assume was @User
                    String id = args.get(0).substring(2, args.get(0).length() - 1);
                    sendEmbed(event, CustomEmbeds.getPersonalEmbedMap().get(id));
                }
            }
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "There was an error with the ID");
        }
    }

    private void sendEmbed(MessageReceivedEvent event, EmbedBuilder eb) {
        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return "me.";
    }
}
