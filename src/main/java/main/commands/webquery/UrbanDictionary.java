package main.commands.webquery;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.urbandictionary.DefinitionContainer;
import main.utility.urbandictionary.UDDefinition;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class UrbanDictionary implements Command {
    private Gson gson = new Gson();
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String url = "http://api.urbandictionary.com/v0/define?term={"+ args.get(0).trim() + "}";
        String json = BotUtils.getStringFromUrl(url);

        DefinitionContainer definition = gson.fromJson(json, DefinitionContainer.class);

        if (definition.getList().size() == 0) {
            BotUtils.sendMessage(event.getChannel(), "No definitions found :(");
            return;
        }
        int numDefsToUse = (5 > definition.getList().size()? definition.getList().size() : 5);
        UDDefinition topDef = definition.getList().get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Aspect :: Urban Dictionary Query")
                .withUrl(topDef.getPermalink())
                .withDesc(generateDesc(topDef));

        for (int i = 1; i < numDefsToUse + 1; i++) {
            UDDefinition def = definition.getList().get(i);
            try {
                eb.appendField(removeBrackets(def.getDefinition()), "by: " + def.getAuthor(), false);
            } catch (IllegalArgumentException ignored) {}
        }

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String generateDesc(UDDefinition topDef) {
        return "Top definition by: " + topDef.getAuthor() + "\n" +
                removeBrackets(topDef.getDefinition()) +
                "\nExample: " +
                removeBrackets(topDef.getExample());
    }

    private String removeBrackets(String s) {
        return s.replaceAll("\\[", "")
                .replaceAll("]", "");
    }


    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
