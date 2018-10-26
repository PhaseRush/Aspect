package main.passive;

import com.google.common.math.BigIntegerMath;
import info.debatty.java.stringsimilarity.Levenshtein;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassiveListener {
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Pattern unexpFactRegex = Pattern.compile("[0-9]+!");
    private static Levenshtein levenshtein = new Levenshtein();


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
    public void owo(MessageReceivedEvent event) throws Exception{
        if(event.getAuthor().isBot()) return; //added for auto music updater (seems to trigger this every time or something
        try {
            if (event.getMessage().getContent().equalsIgnoreCase("owo")){
                if (ThreadLocalRandom.current().nextInt(100) == 1)
                    BotUtils.sendMessage(event.getChannel(), "degenerate");
            }else {
                throw new Exception();
            }
        }catch(Exception e){
            System.out.println("no message");
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


    @EventSubscriber
    public void subredditLinker(MessageReceivedEvent event) {
        String[] msgSplit = event.getMessage().getFormattedContent().split(" ");
        for (String s : msgSplit) {
            if (s.matches("(.*\\s)*(r/).*")) {
                String afterR = s.substring(s.indexOf("r/"));
                String subR = "";
                if (afterR.contains(" "))
                    subR = afterR.substring(0, afterR.indexOf(" "));
                else subR = afterR;

                subR = subR.replaceAll("[.,/#!$%^&*;:{}=\\-_`~()]","");

                BotUtils.sendMessage(event.getChannel(), "https://www.reddit.com/" + subR);
            }
        }
    }


    @EventSubscriber
    public void unexpectedFactorial(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        Matcher matcher = unexpFactRegex.matcher(msg);

        String group = "";
        if (matcher.find()) {
            group = matcher.group();
        } else return;

        int num = Integer.valueOf(group.substring(0, group.length()-1));
        if (num > 797) return;
        BigInteger fact = BigIntegerMath.factorial(num);

        BotUtils.sendMessage(event.getChannel(), "Did you know that `" + num + "! = " + fact + "`");
    }


    @EventSubscriber
    public void alexaPlayTheD(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        String alexa = "alexa play despacito";

        //BotUtils.sendMessage(event.getChannel(), String.valueOf(levenshtein.distance(msg,alexa)));
        if (levenshtein.distance(msg, alexa) < 3 || msg.contains(alexa)) {
            if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel()
                    != event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()) {

                BotUtils.joinVC(event);
            }
            MasterManager.loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=kJQP7kiw5Fk", event, true, "I'm not Alexa, but I can play despacito.");
        }
    }



    @EventSubscriber
    public void userJoin(UserJoinEvent event) {
        BotUtils.sendMessage(event.getGuild().getDefaultChannel(), "Welcome " + event.getUser().getName() + " to " + event.getGuild().getName() + "!");
    }

    @Override
    public String toString() {
        return "This is janky asf";
    }

    //THIS IS JUST A JOKE. If youre a recruiter plz understand that i have a good sense of humour;
    @Override public boolean equals(Object o) {return true;}
}
