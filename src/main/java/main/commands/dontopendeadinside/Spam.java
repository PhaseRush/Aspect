package main.commands.dontopendeadinside;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.miscJsonObj.leaguequotes.LeagueQuoteContainer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Spam implements Command {
    private static ScheduledFuture<?> scheduledFuture = null;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // quotes
    private final List<LeagueQuoteContainer> containers = new ArrayList<>(BotUtils.leagueQuoteMap.values());
    private final List<String> allQuotes = containers.stream()
            .flatMap(container -> container.getQuotes().values().stream())
            .flatMap(condensed -> condensed.values().stream())
            .collect(Collectors.toList());

    private Map<String, VolatileRunnable> runMap = new HashMap<>();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (runMap.containsKey(event.getChannel().getStringID())) { // if contains we want to toggle
            runMap.get(event.getChannel().getStringID()).toggle(); // change state
        } else {
            VolatileRunnable volatileRunnable = new VolatileRunnable(event.getChannel());
            runMap.put(event.getChannel().getStringID(), volatileRunnable);

            scheduledFuture = scheduler.scheduleAtFixedRate(volatileRunnable,
                    0,
                    3,
                    TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return event.getChannel().getName().contains("spam");
    }


    private class VolatileRunnable implements Runnable {
        private volatile AtomicBoolean shutdown = new AtomicBoolean(false);
        private final IChannel targetCh;
        private final Random r = new Random(System.currentTimeMillis());

        VolatileRunnable(IChannel targetCh) {
            this.targetCh = targetCh;
        }

        @Override
        public void run() {
            while (!shutdown.get()) {
                BotUtils.send(targetCh, allQuotes.get(r.nextInt(allQuotes.size())));
            }
        }
        public void shutdown() {
            shutdown.set(true);
        }

        /**
         * toggle current state
         * @return new state
         */
        public boolean toggle() {
            boolean curr = shutdown.get();
            shutdown.set(curr);
            return !curr;
        }
    }
}
