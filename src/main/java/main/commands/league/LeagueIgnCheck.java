package main.commands.league;

import main.Command;
import main.utility.metautil.BotUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.Optional;

public class LeagueIgnCheck implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String html = BotUtils.getStringFromUrl("https://lolnames.gg/en/na/" + BotUtils.concatArgs(args) + "/");
        Optional<Element> element = Jsoup.parse(html).getAllElements().stream()
                .filter(e -> e.hasClass("text-center"))
                .filter(e -> e.tag().getName().equals("h4"))
                .findFirst();

        element.ifPresent(element1 -> BotUtils.send(event.getChannel(),
                new EmbedBuilder()
                        .withTitle(element1.text())
                        .withDesc("[Add to Google Calendar](" + getCalUrl(html)+ ")")));

    }

    private String getCalUrl(String html) {
        try {
            int startIdx = html.indexOf("http://www.google.com/calendar/render?action=TEMPLATE&text=Summoner+name+");
            html = html.substring(startIdx);
            int endIdx = html.indexOf("xml") + 3;
            return html.substring(0, endIdx);
        } catch (Exception e) {
            e.printStackTrace();
            return "not available";
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    public String getDesc() {
        return "Gives time until username is available";
    }
}
