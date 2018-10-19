package main.passive;

import main.utility.BotUtils;
import main.utility.PokemonUtil;
import main.utility.Visuals;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class PokemonIdentifier {
    private static ThreadGroup pokemonIdentifiers = new ThreadGroup("Pokemon Identifiers");

    @EventSubscriber
    public void pokemonIdentifier(MessageReceivedEvent event) {
        if (!(event.getAuthor().getStringID().equals("365975655608745985")/* || event.getAuthor().getStringID().equals("264213620026638336")*/)) return;
        if (event.getMessage().getEmbeds().size() == 0) return; //not *that* needed but nice to have

        Runnable identifier = () -> {
            long startTime = System.currentTimeMillis();
            double threshold = 10; //changed from .1 -> 10
            boolean shouldSendDiff = true;
            String targetUrl = "";

            if(event.getAuthor().getStringID().equals("264213620026638336") && event.getMessage().getFormattedContent().startsWith("ht")) { //for use by dev for testing
                targetUrl = event.getMessage().getFormattedContent();
            }else if (event.getAuthor().getStringID().equals("365975655608745985")){
                IEmbed embed = event.getMessage().getEmbeds().get(0);
                targetUrl = embed.getImage().getUrl(); //event.getMessage().getFormattedContent();
            } else return;

            System.out.println("Starting pokemon identification at " + LocalDateTime.now().toString());

            StringBuilder logBuilder = new StringBuilder("Attempting match on: " + targetUrl + "\n");
            BufferedImage target = Visuals.cropTransparent(Visuals.urlToBufferedImageWithAgentHeader(targetUrl)); //important

            HashMap<String, Double> similarityMap = new HashMap<>();
            BufferedImage testImg = null;
            Map.Entry answer = null;

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
                Thread.yield(); //@todo newly added yield in attempt to spread out cpu usage. Keep eye on this. -- WORKS FINE
            }
            Map<String, Double> sortedSimilarity = BotUtils.sortMapByValue(similarityMap, true);
            answer = sortedSimilarity.entrySet().iterator().next(); //first entry

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Aspect | Pokédex")
                    .withColor(Visuals.analyizeImageColor(target))
                    .withDesc("I am ```" + (99.99 - (Double)answer.getValue()) + "%``` confident that this Pokémon is: ```" + answer.getKey() + "```")
                    .withFooterText("This operation took " + (System.currentTimeMillis() - startTime) + " ms.");

            closestMatches(eb, sortedSimilarity);

            IMessage outboundMsg = null;
            RequestBuffer.request(() -> event.getChannel().sendMessage(eb.build())).get();
            BotUtils.reactWithCheckMark(outboundMsg);
            BotUtils.reactWithX(outboundMsg);
            //difference image
            if (shouldSendDiff) { //removed 2nd condition:   && (double)answer.getValue() > 0.01
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
        };

        //@todo using this thread impl. to test out priority setting
        Thread iden = new Thread(pokemonIdentifiers, identifier, "identifier");
        iden.setPriority(3); //5 is default priority. might want to make this daemon
        iden.start();
        //then execute this guy
        //executor.execute(identifier);
    }

    private void closestMatches(EmbedBuilder eb, Map<String, Double> sortedSimilarity) {
        List<Map.Entry<String, Double>> topSix = new LinkedList<>();

        Iterator<Map.Entry<String, Double>> iter = sortedSimilarity.entrySet().iterator();
        for (int i = 0; i < 6; i++)
            topSix.add(iter.next());

        for (Map.Entry<String, Double> entry : topSix.subList(1, topSix.size())) //ignore first
            eb.appendField(new Embed.EmbedField(entry.getKey(), (99.99 - entry.getValue()) + "%", false));
    }

    private BufferedImage calcDifference(BufferedImage target, String answer) {
        BufferedImage answerImg;
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

}
