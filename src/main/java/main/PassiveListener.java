package main;

import main.utility.BotUtils;
import main.utility.PokemonUtil;
import main.utility.Visuals;
import main.utility.WarframeUtil;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.MINUTES;

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
        long startTime = System.currentTimeMillis();
        if (!event.getAuthor().getStringID().equals("365975655608745985") ||
                !event.getAuthor().getStringID().equals("264213620026638336")) return;

        IEmbed embed = event.getMessage().getEmbeds().get(0);
        if (embed == null) return;

        String targetUrl = embed.getImage().getUrl();
        BufferedImage target = Visuals.urlToBufferedImage(targetUrl);

        HashMap<String, Double> similarityMap = new HashMap<>();
        BufferedImage testImg = null;
        for (String s : PokemonUtil.pokemonArray) {
            try {
                testImg = ImageIO.read(new File(PokemonUtil.baseDir + s + ".png"));
            } catch (IOException e) {
                System.out.println(s + " was not found"); //just one of the files not in the 775 / 807, can fine tune
            }
            similarityMap.put(s, calcSim(target, testImg));
        }
        Map<String, Double> sortedSimilarity = new LinkedHashMap<>();
        sortedSimilarity = BotUtils.sortMapByValue(similarityMap, true);

        Entry answer = sortedSimilarity.entrySet().iterator().next();
        BufferedImage differenceImage = calcDifference(target, (String) answer.getKey());
        File diffImgFile = new File("diffImgFile.png");
        try {
            ImageIO.write(differenceImage, "png", diffImgFile);
        } catch (IOException e) {
            System.out.println("problem in saving difference image");
            e.printStackTrace();
        }

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Aspect | Pokémon Predictor")
                .withColor(Visuals.analyizeImageColor(target))
                .withDesc("I am ```" + answer.getValue() + "%``` confident that this Pokémon is: ```" + answer.getKey() + "```\nDifference image:")
                .withFooterText("This operation took " + (System.currentTimeMillis() - startTime) + " ms.");

        RequestBuffer.request(() -> {
            try {
                return event.getChannel().sendFile(eb.build(), diffImgFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });

        diffImgFile.delete();


    }

    private BufferedImage calcDifference(BufferedImage target, String answer) {
        BufferedImage answerImg = null;
        BufferedImage diffImg = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        try {
            answerImg = ImageIO.read(new File(PokemonUtil.baseDir + answer + ".png"));
        } catch (IOException ignored) { return null;}

        for (int x = 0; x < target.getWidth(); x++) {
            for (int y = 0; y < target.getHeight(); y++) {
                int argb0 = target.getRGB(x, y);
                int argb1 = answerImg.getRGB(x, y);

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

    private Double calcSim(BufferedImage target, BufferedImage testImg) {
        if (testImg == null) return 0d; //probs wont reach here, but just in case
        double targetHeight = target.getHeight();
        double targetWidth = target.getWidth();
        double testHeight = testImg.getHeight();
        double testWidth = testImg.getWidth();
        BufferedImage scaledTest = testImg;

        //resize
        if (testHeight != targetHeight || testWidth != targetWidth) {
            scaledTest = new BufferedImage((int)testWidth, (int)testHeight, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(targetWidth/testWidth, targetHeight/testHeight);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); //want pixel perfect scaling
            scaledTest = scaleOp.filter(testImg, scaledTest);
        }

        //now the comparison
        long difference = 0;
        for (int x = 0; x < targetWidth; x++) {
            for (int y = 0; y < targetHeight; y++) {
                //check transparency first
                if (target.getTransparency() == scaledTest.getTransparency()) continue;

                int rgbA = target.getRGB(x, y);
                int rgbB = scaledTest.getRGB(x, y);
                int redA = (rgbA >> 16) & 0xff; //bunch of masking
                int greenA = (rgbA >> 8) & 0xff;
                int blueA = (rgbA) & 0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB) & 0xff;
                difference += Math.abs(redA - redB);
                difference += Math.abs(greenA - greenB);
                difference += Math.abs(blueA - blueB);
            }
        } //exit outer for

        double totalPixels = targetWidth * targetHeight * 3;
        double avgDiff = difference/totalPixels;

        return avgDiff * 100 / 255;
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
