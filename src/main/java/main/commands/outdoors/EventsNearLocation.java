package main.commands.outdoors;

import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.api.discovery.response.PagedResponse;
import com.ticketmaster.api.discovery.response.RateLimit;
import com.ticketmaster.discovery.model.Events;
import main.Aspect;
import main.Command;
import main.utility.Visuals;
import main.utility.gist.GistUtils;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;

public class EventsNearLocation implements Command {
    private static DiscoveryApi api = new DiscoveryApi(BotUtils.TICKET_MASTER_API_KEY);

    private static boolean isRateLimited = false;
    private static long rateReset = 0;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (isRateLimited) {
            if (System.currentTimeMillis() > rateReset) {
                isRateLimited = false;
            } else {
                BotUtils.send(event.getChannel(), "Aspect is currently rate limited. Please wait a bit and try again :(");
                return;
            }
        }

        String[] query = getQuery(args);

        PagedResponse<Events> page;
        try {
            page = api.searchEvents(
                    new SearchEventsOperation()
                            .city(query[0])
                            .countryCode(query[1])
                            .keyword(query[2]));
        } catch (IOException e) {
            e.printStackTrace();
            BotUtils.send(event.getChannel(), "error");
            return;
        }

        handleRateLimit(page);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Events in " + query[0] + ", " + query[1])
                .withColor(Visuals.getVibrantColor())
                .withFooterText("Information provided by TicketMaster");

        if (page.getContent() == null) BotUtils.send(event.getChannel(), "pages null, u dun fucked");
        page.getContent().getEvents().stream()
                .limit(10)
                .forEach((e) -> eb.appendField(e.getName(), e.getDescription(), false));

        String debug = BotUtils.gson.toJson(page);
        Aspect.LOG.info(GistUtils.makeGistGetHtmlUrl("Events Testing", "PagedResponse<Event>", debug));

        BotUtils.send(event.getChannel(), eb);
    }

    private void handleRateLimit(PagedResponse<Events> page) {
        RateLimit rl = page.getRateLimit();

        isRateLimited = Integer.valueOf(rl.getAvailable()) < 5; // leave 5 requests for buffers

        rateReset = Long.valueOf(rl.getReset());
    }

    /**
     * @param args user defined parameters to command
     * @return String[] = "City Name", "Country Code", "Event Query"
     */
    private String[] getQuery(List<String> args) {
        String[] query = new String[3];

        query[0] = args.get(0);

        if (args.size() == 2) query[1] = "US";
        else query[1] = args.get(1);

        query[2] = args.get(args.size()-1);

        return query;
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return args.size() >= 2;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
