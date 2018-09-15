package main.commands.league;

import com.merakianalytics.orianna.types.core.staticdata.Item;
import main.Command;
import main.utility.Visuals;
import main.utility.league.LeagueUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class RandomItem implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        Item primaryItem = LeagueUtil.getRandomItem();

        EmbedBuilder eb = new EmbedBuilder();
        eb.withTitle("League of Legends analytics - 8.11")
                .withUrl("https://github.com/PhaseRush/Aspect")
                .withDesc("Here's your chosen item. Alternatives are provided upon request\n```" + primaryItem.getName()+ "```\n" + primaryItem.getPlaintext())
                .withColor(Visuals.analyizeImageColor(primaryItem.getImage().get()))
                .withTimestamp(System.currentTimeMillis())
                .withImage(primaryItem.getImage().getURL());
        
        if(args.size() == 1) {
            for (int i = 0; i < Integer.valueOf(args.get(0)); i++) {
                Item item = LeagueUtil.getRandomItem();
                eb.appendField(item.getName(), item.getPlaintext(), false);
            }
        }

        IMessage iEmbed = event.getChannel().sendMessage(eb.build());
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Shows one (or more, if specified) random items";
    }
}
