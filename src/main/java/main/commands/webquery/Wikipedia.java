package main.commands.webquery;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.wikipedia.WikiContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class Wikipedia implements Command {
    private Gson gson = new Gson();
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String subject = args.get(0);
        String url = "https://en.wikipedia.org/api/rest_v1/page/summary/" + subject.replaceAll(" ", "_");
        String json = BotUtils.getStringFromUrl(url);

        WikiContainer wiki = gson.fromJson(json, WikiContainer.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(wiki.getDisplaytitle())
                .withColor(Visuals.getVibrantColor())
                .withDesc(generateDesc(wiki));

        if (wiki.getOriginalimage() != null)
            eb.withImage(wiki.getOriginalimage().getSource());

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String generateDesc(WikiContainer wiki) {
        return wiki.getDescription() + "\n\n" +
                "\t" + wiki.getExtract();
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
