package main;

import main.utility.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.util.concurrent.ThreadLocalRandom;

public class PassiveListener {



    @EventSubscriber
    public void kaitlynsHangOut(MessageReceivedEvent event) {
        //please, no one ask. please please please please please
        if (event.getGuild().getStringID().equals("197158565004312576")) {
            String message = event.getMessage().getFormattedContent().toLowerCase();
            if (message.contains("penis")) {
                BotUtils.sendMessage(event.getChannel(), "penis.");
            }
            //not exclusive
            if (message.contains("turtle")) {
                BotUtils.sendMessage(event.getChannel(), new EmbedBuilder().withImage("https://assets3.thrillist.com/v1/image/2551479/size/tmg-article_tall.jpg"));
            }
        }
    }

    @EventSubscriber
    public void owo(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        if (msg.contains("owo")) {
            if (ThreadLocalRandom.current().nextInt(100) == 1)
                BotUtils.sendMessage(event.getChannel(), "degenerate");
            else
                BotUtils.sendMessage(event.getChannel(), "");
        }
    }

    @EventSubscriber
    public void reactToEmojiMessage(MessageReceivedEvent event) {
        try {
            for (IEmoji e : event.getGuild().getEmojis()) {
                if (event.getMessage().getFormattedContent().contains(e.getName())) {
                    try {
                        event.getMessage().addReaction(e);
                        break;
                    } catch (RateLimitException exception) {
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            //caught if in server with no custom emojis. (ie. pms)
        }
    }

//    @EventSubscriber
//    public void warframeAlert(ReadyEvent event) {
//        //String path = "C:\\Users\\Positron\\IdeaProjects\\Aspect\\txtfiles\\Warframe\\WarframeAlertSubscribers.txt";
//        String path = "~/AspectTextFiles/subscribedServers.txt"; //no idea how linux works :)
//        List<String> subscribedChannels = ReadWrite.readFromFileToStringList(path);
//
//        String json = BotUtils.getStringFromUrl("https://api.warframestat.us/pc/alerts");
//
//        Type alertListType = new TypeToken<LinkedList<WarframeAlert>>() {
//        }.getType();
//        LinkedList<WarframeAlert> alerts = new Gson().fromJson(json, alertListType);
//
//        //scheduler code;
//        final ScheduledExecutorService scheduler =
//                Executors.newScheduledThreadPool(2);
//
//        final Runnable alertEmbedSender = new Runnable() {
//            public void run() {
//                for (String s : subscribedChannels)
//                    new MessageBuilder(event.getClient()).withEmbed(WarframeUtil.generateAlertsEmbed().withTitle("Warframe | Alerts - (automated message)").build()).withChannel(Long.valueOf(s)).send();
//
//                int minute = LocalDateTime.now().getMinute();
//                System.out.println("Automated Warframe Alerts " + LocalDateTime.now().getHour() + ":" + (minute < 10 ? "0" + minute : minute) + "\nSubscribed servers list: " + path);
//            }
//        };
//        final ScheduledFuture<?> alertHandler = scheduler.scheduleAtFixedRate(alertEmbedSender, 0, 30, MINUTES);
//        scheduler.schedule(new Runnable() {
//            public void run() {
//                alertHandler.cancel(false);
//            }
//        }, 24, HOURS);
//    }

    @EventSubscriber
    public void userJoin(UserJoinEvent event) {
        BotUtils.sendMessage(event.getGuild().getDefaultChannel(), "Welcome " + event.getUser().getName() + " to " + event.getGuild().getName() + "!");
    }
}
