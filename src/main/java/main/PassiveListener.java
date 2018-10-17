package main;

import com.google.common.math.BigIntegerMath;
import info.debatty.java.stringsimilarity.Levenshtein;
import main.utility.BotUtils;
import main.utility.PokemonUtil;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.music.MasterManager;
import main.utility.warframe.wfstatus.alerts.WarframeAlert;
import main.utility.warframe.wfstatus.alerts.WarframeMission;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.MINUTES;
import static sx.blah.discord.handle.impl.obj.Embed.EmbedField;

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
    public void owo(MessageReceivedEvent event) {
        if (event.getMessage().getContent().equalsIgnoreCase("owo"))
            if (ThreadLocalRandom.current().nextInt(100) == 1)
                BotUtils.sendMessage(event.getChannel(), "degenerate");
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
    public void alertFilter(ReadyEvent event) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable alertFilter = () -> {
            LinkedList<WarframeAlert> alerts = WarframeUtil.getCurrentAlerts();
            alerts.removeIf(warframeAlert -> {
                boolean containsKeyword = false;
                for (String s : WarframeUtil.alertFilters)
                    if (warframeAlert.getMission().getReward().getAsString().contains(s))
                        containsKeyword = true;

                return !containsKeyword;
            });

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Warframe | Filtered Alerts")
                    .withColor(Visuals.getVibrantColor());

            if (alerts.size() == 0) return;

            for (WarframeAlert alert : alerts) {
                WarframeMission mission = alert.getMission();
                eb.appendField(mission.getNode() + " | " + mission.getType() + " | " + alert.getEta() + " remaining", mission.getReward().getAsString(), false);
            }
            BotUtils.sendMessage(BotUtils.BOTTOM_TEXT, eb);
        };

        final ScheduledFuture<?> alertFilterUpdater = scheduler.scheduleAtFixedRate(alertFilter, 0, 15, MINUTES);
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

                BotUtils.sendMessage(event.getChannel(), "https://www.reddit.com/" + subR);
            }
        }
    }

    @EventSubscriber
    public void pokemonIdentifier(MessageReceivedEvent event) {

        Runnable identifier = () -> {
            long startTime = System.currentTimeMillis();
            double threshold = 0.1;
            if (!(event.getAuthor().getStringID().equals("365975655608745985")/* || event.getAuthor().getStringID().equals("264213620026638336")*/)) return;
            if (event.getMessage().getEmbeds().size() == 0) return; //not *that* needed but nice to have

            boolean shouldSendDiff = true;
            String targetUrl = "";
            if(event.getAuthor().getStringID().equals("264213620026638336") && event.getMessage().getFormattedContent().startsWith("ht")) {
                targetUrl = event.getMessage().getFormattedContent();
            }else if (event.getAuthor().getStringID().equals("365975655608745985")){
                IEmbed embed = event.getMessage().getEmbeds().get(0);
                targetUrl = embed.getImage().getUrl(); //event.getMessage().getFormattedContent();
            } else return;

            System.out.println("Starting pokemon identification");

            StringBuilder logBuilder = new StringBuilder("Attempting match on: " + targetUrl + "\n");
            BufferedImage target = Visuals.cropTransparent(Visuals.urlToBufferedImageWithAgentHeader(targetUrl)); //important

            HashMap<String, Double> similarityMap = new HashMap<>();
            BufferedImage testImg = null;
            Entry answer = null;

            int counter = 1;
            for (String s : PokemonUtil.pokemonArray) {
                try {
                    if (counter%100 == 0)
                        System.out.println(counter + " Path:" + PokemonUtil.baseDir + s + ".png");
                    testImg = ImageIO.read(new File(PokemonUtil.baseDir + s + ".png")); //change dir here @todo
                    double sim = calcSim(target, testImg);
                    logBuilder.append("Imaging #" + counter + " : " + s + " score: " + sim + "\n");
                    BotUtils.writeToFile("/home/positron/AspectTextFiles/RecentPokemonMatch.txt", logBuilder.toString(), false);//@todo
                    similarityMap.put(s, sim);
                    if (sim < threshold) { //if this is already ~perfect~ close enough match, dont do any more
                        shouldSendDiff = false;
                        break;
                    }
                } catch (IOException e) {
                    //System.out.println(s + " was not found"); //just one of the files not in the 775 / 807, can fine tune
                }
                counter++;
            }
            Map<String, Double> sortedSimilarity = BotUtils.sortMapByValue(similarityMap, true);
            answer = sortedSimilarity.entrySet().iterator().next(); //first entry

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Aspect | Pokédex")
                    .withColor(Visuals.analyizeImageColor(target))
                    .withDesc("I am ```" + (99.99 - (Double)answer.getValue()) + "%``` confident that this Pokémon is: ```" + answer.getKey() + "```")
                    .withFooterText("This operation took " + (System.currentTimeMillis() - startTime) + " ms.");

            closestMatches(eb, sortedSimilarity);

            RequestBuffer.request(() -> event.getChannel().sendMessage(eb.build())).get();
            //difference image
            if (shouldSendDiff && (double)answer.getValue() > 0.01) {
                BufferedImage diffImg = calcDifference(target, (String) answer.getKey());
                File diffImgFile = new File("Diff Img.png");
                try {
                    ImageIO.write(diffImg, "png", diffImgFile);
                } catch (IOException e) {
                    System.out.println("IOException writing difference file");
                }
                RequestBuffer.request(() -> {
                    try {
                        return event.getChannel().sendFile("Difference Image: ", diffImgFile);
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not find difference image");
                    }
                    return null;
                }).get();

                diffImgFile.delete();
            }

            //BotUtils.sendMessage(event.getChannel(), eb);

        };
        //then execute this guy
        executor.execute(identifier);
    }

    private void closestMatches(EmbedBuilder eb, Map<String, Double> sortedSimilarity) {
        List<Entry<String, Double>> topSix = new LinkedList<>();

        Iterator<Entry<String, Double>> iter = sortedSimilarity.entrySet().iterator();
        for (int i = 0; i < 6; i++)
            topSix.add(iter.next());

        for (Entry<String, Double> entry : topSix.subList(1, topSix.size())) //ignore first
            eb.appendField(new EmbedField(entry.getKey(), (99.99 - entry.getValue()) + "%", false));
    }

    /**
     * deprecated. Does work.
     * @param target
     * @param answer
     * @return
     */
    private BufferedImage calcDifference(BufferedImage target, String answer) {
        BufferedImage answerImg = null;
        BufferedImage diffImg = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        try {
            answerImg = ImageIO.read(new File(PokemonUtil.baseDir + answer + ".png")); //@todo fix dir
        } catch (IOException ignored) { return null;}

        //scale answerImg if not same size
        //resize
        double targetHeight = target.getHeight();
        double targetWidth = target.getWidth();
        double testHeight = answerImg.getHeight();
        double testWidth = answerImg.getWidth();
        System.out.println("Target W x H: " + targetWidth + " x " + targetHeight);
        System.out.println("Ans before W x H: " + testWidth + " x " + testHeight);
        BufferedImage scaledAnswer = answerImg; //changed this
        scaledAnswer = getScaledWithNearestNeighbour(answerImg, targetHeight, targetWidth, testHeight, testWidth, scaledAnswer);
        System.out.println("Ans after W x H: " + scaledAnswer.getWidth() + " x " + scaledAnswer.getHeight());


        for (int x = 0; x < target.getWidth(); x++) {
            for (int y = 0; y < target.getHeight(); y++) {
                int argb0 = target.getRGB(x, y);
                int argb1 = scaledAnswer.getRGB(x, y);

                int a0 = (argb0 >> 24) & 0xFF;
                int r0 = (argb0 >> 16) & 0xFF;
                int g0 = (argb0 >>  8) & 0xFF;
                int b0 = (argb0      ) & 0xFF;

                int a1 = (argb1 >> 24) & 0xFF;
                int r1 = (argb1 >> 16) & 0xFF;
                int g1 = (argb1 >>  8) & 0xFF;
                int b1 = (argb1      ) & 0xFF;

                int aDiff = Math.abs(a1 - a0);
                int rDiff = Math.abs(r1 - r0);
                int gDiff = Math.abs(g1 - g0);
                int bDiff = Math.abs(b1 - b0);

                int diff = (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
                diffImg.setRGB(x, y, diff);
            }
        }
        return diffImg;
    }

    private BufferedImage getScaledWithNearestNeighbour(BufferedImage answerImg, double targetHeight, double targetWidth, double testHeight, double testWidth, BufferedImage scaledAnswer) {
        if (testHeight != targetHeight || testWidth != targetWidth) {
            scaledAnswer = new BufferedImage((int)targetWidth, (int)targetHeight, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(targetWidth/testWidth, targetHeight/testHeight);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); //want pixel perfect scaling
            scaledAnswer = scaleOp.filter(answerImg, scaledAnswer);
        }
        return scaledAnswer;
    }

    private Double calcSim(BufferedImage target, BufferedImage testImg) {
        if (testImg == null) return 0d; //probs wont reach here, but just in case
        double targetHeight = target.getHeight();
        double targetWidth = target.getWidth();
        double testHeight = testImg.getHeight();
        double testWidth = testImg.getWidth();
        BufferedImage scaledTest = testImg;

        //resize
        scaledTest = getScaledWithNearestNeighbour(testImg, targetHeight, targetWidth, testHeight, testWidth, scaledTest);

        //now the comparison
        long difference = 0;
        for (int x = 0; x < targetWidth; x++) {
            for (int y = 0; y < targetHeight; y++) {
                //check transparency first
                //if (target.getTransparency() == scaledTest.getTransparency()) continue;

                int rgbA = target.getRGB(x, y);
                int rgbB = scaledTest.getRGB(x, y);
                int alpA = (rgbA >> 24) & 0xff;
                int alpB =  (rgbB >> 24) & 0xff;

                int redA = (rgbA >> 16) & 0xff; //bunch of masking
                int greenA = (rgbA >> 8) & 0xff;
                int blueA = (rgbA) & 0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB) & 0xff;

                difference += Math.abs(alpA - alpB);
                difference += Math.abs(redA - redB);
                difference += Math.abs(greenA - greenB);
                difference += Math.abs(blueA - blueB);
            }
        } //exit outer for

        double totalPixels = targetWidth * targetHeight * 4;
        double avgDiff = difference/totalPixels;

        return avgDiff * 100 / 255;
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

        BotUtils.sendMessage(event.getChannel(), "Did you know that `" + num + "!` is `" + fact + "`?");
    }


    @EventSubscriber
    public void alexaPlayD(MessageReceivedEvent event) {
        String msg = event.getMessage().getFormattedContent();
        String alexa = "alexa play despacito";

        //BotUtils.sendMessage(event.getChannel(), String.valueOf(levenshtein.distance(msg,alexa)));
        if (levenshtein.distance(msg, alexa) < 3 || msg.contains(alexa)) {
            if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel()
                    != event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()) {

                BotUtils.joinVC(event);
            }
            MasterManager.loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v=kJQP7kiw5Fk", event, false, "I'm not Alexa, but I can play despacito.");
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
}
