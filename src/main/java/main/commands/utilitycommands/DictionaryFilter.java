package main.commands.utilitycommands;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryFilter implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String input = args.get(0);
        String condensed = BotUtils.dictionaryList.stream()
                .filter(input.startsWith(" \\") ?
                        word -> word.matches(input.substring(input.indexOf("\\")+1, input.length()-1)) :
                        word -> word.contains(args.get(0)))
                .collect(Collectors.joining(", "));

        condensed = condensed.length() == 0 ? "No words match the query." : condensed;

        EmbedBuilder eb = new EmbedBuilder().withTitle("English :: pattern match");
        try {
            BotUtils.send(event.getChannel(),
                    eb.withDesc( condensed.length() > 2048 ? BotUtils.makeHasteGetUrl(condensed.replaceAll(", ", "\n")) : condensed));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (args.get(0).startsWith(" \\")) { // if trying to use regex, it better compile
            try {
                Pattern.compile(args.get(0).substring(1, args.get(0).length()-1));
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
