package main.commands.webquery;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.wikipedia.WikiContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;

public class Wikipedia implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String subject = args.get(0);
        String url = "https://en.wikipedia.org/api/rest_v1/page/summary/" + subject.replaceAll(" ", "_");
        String json = BotUtils.getStringFromUrl(url);

        WikiContainer wiki = BotUtils.gson.fromJson(json, WikiContainer.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle(wiki.getDisplaytitle())
                .withUrl(wiki.getContent_urls().getDesktop().getPage())
                .withColor(Visuals.getVibrantColor())
                .withDesc(generateDesc(wiki));


        if (wiki.getOriginalimage() != null)
            eb.withImage(wiki.getOriginalimage().getSource());
        else if (wiki.getThumbnail() != null) // else try the thumbnail
            eb.withImage(wiki.getThumbnail().getSource());

        BotUtils.send(event.getChannel(), eb);
    }

    private String generateDesc(WikiContainer wiki) {
        String wikiDesc = wiki.getDescription() == null? "" : "*" + wiki.getDescription() + "*" + "\n\n";
        return wikiDesc+
                "\t" + wiki.getExtract();
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
