package main.commands.utilitycommands;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryFilter implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        System.out.println(args.get(0));
        String condensed = BotUtils.dictionaryList.stream()
                .filter(args.get(0).startsWith("\\")?
                        word -> word.matches(args.get(0).substring(1, args.get(0).length()-1)) :
                        word -> word.contains(args.get(0)))
                .collect(Collectors.joining(", "));


        System.out.println(condensed.length() + " : " + condensed);
        EmbedBuilder eb = new EmbedBuilder().withTitle("English :: pattern match (not exhaustive due to hastebin breaking)");
//        try {
            BotUtils.send(event.getChannel(),
                            //eb.withDesc( condensed.length() > 2048 ? BotUtils.makeHasteGetUrl(condensed)) : condensed)
                    eb.withDesc(condensed.substring(0, 2048))
            );
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (!args.isEmpty() && !args.get(0).startsWith("\\")) { // if trying to use regex, it better compile
            try {
                Pattern.compile(args.get(0));
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
