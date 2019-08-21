package main.passive;

import main.Aspect;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import main.utility.warframe.wfstatus.WarframeCetusTimeObject;
import main.utility.warframe.wfstatus.WarframeOrbVallisCycle;
import main.utility.warframe.wfstatus.alerts.WarframeAlert;
import main.utility.warframe.wfstatus.alerts.WarframeMission;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WfPassive {
    private static ScheduledFuture<?> cetusStatusUpdater = null, alertFilterUpdater = null;


    public static void warframeOpenWorldPresence() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final AtomicLong toggle = new AtomicLong(0L); // emulate atomic boolean


        final Runnable updater = () -> {
            if (toggle.getAndIncrement() % 2 == 0) {
                WarframeCetusTimeObject cetus = WarframeUtil.getCetus();
                Aspect.client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, (cetus.isDay() ? " Sol " : " Lua ") + " :: " + cetus.getShortString());
            } else {
                WarframeOrbVallisCycle orbVallis = WarframeUtil.getOrbVallis();
                Aspect.client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, " the thermometer. :: " + orbVallis.getTimeLeft() + "m until" + (orbVallis.isWarm() ? " cold" : " warm"));
            }
        };

        cetusStatusUpdater = scheduler.scheduleAtFixedRate(updater, 0/*elapseMillis/1000*/, 60/*150*60*/, SECONDS);
    }

    //todo make this more robust
    public static boolean killCetusUpdater() {
        try {
            return cetusStatusUpdater.cancel(true);
        } catch (NullPointerException ignored) {
        } // throws if not running in the first place

        return false;
    }


    @EventSubscriber
    public void alertFilter(ReadyEvent event) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable alertFilter = () -> {
            List<WarframeAlert> alerts = WarframeUtil.getCurrentAlerts();
            alerts.removeIf(warframeAlert -> {
                boolean containsKeyword = false;
                for (String s : WarframeUtil.alertFilters)
                    if (warframeAlert.getMission().getReward().getAsString().contains(s)) {
                        containsKeyword = true;
                        break;
                    }

                return !containsKeyword;
            });

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Warframe :: Filtered Alerts")
                    .withColor(Visuals.getRandVibrantColour());

            if (alerts.isEmpty()) return;

            for (WarframeAlert alert : alerts) {
                WarframeMission mission = alert.getMission();
                eb.appendField(mission.getNode() + " | " + mission.getType() + " | " + alert.getEta() + " remaining", mission.getReward().getAsString(), false);
            }

            if (BotUtils.BOTTOM_TEXT != null)
                BotUtils.send(BotUtils.BOTTOM_TEXT, eb); //bottom text is null on startup, throwing NPE.
        };

        alertFilterUpdater = scheduler.scheduleAtFixedRate(alertFilter, 0, 15, MINUTES);
    }
}
