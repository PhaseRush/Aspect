package main.passive;

import main.utility.PokemonUtil;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.Entry;

public class PokemonIdentifier {
    private static ThreadGroup pokemonIdentifiers = new ThreadGroup("Pokemon Identifiers");

    //2019-1-3 took out subscription. Causing lag, and no one really uses.
    //@EventSubscriber //Not a subscriber anymore - Vowed 4/nov/18
    public void pokemonIdentifier(MessageReceivedEvent event) {
        if (!(event.getAuthor().getStringID().equals("365975655608745985")/* || event.getAuthor().getStringID().equals("264213620026638336")*/)) return;
        if (event.getMessage().getEmbeds().size() == 0) return; //not *that* needed but nice to have

        Runnable identifier = () -> {
            long startTime = System.currentTimeMillis();
            double threshold = 10; //changed from .1 -> 10
            boolean shouldSendDiff = true;
            String targetUrl;

            if(event.getAuthor().getStringID().equals("264213620026638336") && event.getMessage().getFormattedContent().startsWith("ht")) { //for use by dev for testing
                targetUrl = event.getMessage().getFormattedContent();
            }else if (event.getAuthor().getStringID().equals("365975655608745985")){
                IEmbed embed = event.getMessage().getEmbeds().get(0);
                targetUrl = embed.getImage().getUrl(); //event.getMessage().getFormattedContent();
            } else return;

            Aspect.LOG.info("Starting pokemon identification at " + LocalDateTime.now().toString());

            StringBuilder logBuilder = new StringBuilder("Attempting match on: " + targetUrl + "\n");
            BufferedImage target = Visuals.cropTransparent(Visuals.urlToBufferedImageWithAgentHeader(targetUrl)); //important

            HashMap<String, Double> similarityMap = new HashMap<>();
            BufferedImage testImg;
            Entry answer;

            int counter = 1;
            for (String s : PokemonUtil.pokemonArray) {
                try {
                    if (counter%100 == 0)
                        Aspect.LOG.info(counter + " Path:" + PokemonUtil.baseDir + s + ".png");
                    testImg = ImageIO.read(new File(PokemonUtil.baseDir + s + ".png")); //change dir here @todo
                    double sim = PokemonUtil.calcSim(target, testImg);
                    logBuilder.append("Imaging #" + counter + " : " + s + " score: " + sim + "\n");
                    BotUtils.writeToFile("/home/positron/AspectTextFiles/RecentPokemonMatch.txt", logBuilder.toString(), false);//@todo
                    similarityMap.put(s, sim);
                    if (sim < threshold) { //if this is already ~perfect~ close enough match, dont do any more
                        shouldSendDiff = false;
                        Aspect.LOG.info(counter + " Path:" + PokemonUtil.baseDir + s + ".png"); // print out for debug
                        break;
                    }
                } catch (IOException e) {
                    //Aspect.LOG.info(s + " was not found"); //just one of the files not in the 775 / 807, can fine tune
                }
                counter++;
                Thread.yield(); //@todo newly added yield in attempt to spread out cpu usage. Keep eye on this. -- WORKS FINE
            }
            Map<String, Double> sortedSimilarity = BotUtils.sortMap(similarityMap, true, true);
            answer = sortedSimilarity.entrySet().iterator().next(); //first entry

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle("Aspect | Pokédex")
                    .withColor(Visuals.analyzeImageColor(target))
                    .withDesc("I am ```" + (99.99 - (Double)answer.getValue()) + "%``` confident that this Pokémon is: ```" + answer.getKey() + "```")
                    .withFooterText("This operation took " + (System.currentTimeMillis() - startTime) + " ms.");

            PokemonUtil.closestMatches(eb, sortedSimilarity);


            //this sends the message -- DONT SEND
            //RequestBuffer.request(() -> event.getChannel().send(eb.build())).get();
            //instead of sending the embed, set the most recent embed to this instead
            PokemonUtil.mostRecentEmbed = eb;

            //difference image
            if (shouldSendDiff) { //removed 2nd condition:   && (double)answer.getValue() > 0.01
                BufferedImage diffImg = PokemonUtil.calcDifference(target, (String) answer.getKey());
                File diffImgFile = new File("Diff Img.png");
                try {
                    ImageIO.write(diffImg, "png", diffImgFile);
                } catch (IOException e) {
                    Aspect.LOG.info("IOException writing difference file");
                }
                PokemonUtil.shouldSendDiff = true;
//                RequestBuffer.request(() -> {
//                    try {
//                        return event.getChannel().sendFile("Difference Image: ", diffImgFile);
//                    } catch (FileNotFoundException e) {
//                        Aspect.LOG.info("Could not find difference image");
//                    }
//                    return null;
//                }).get();

                //diffImgFile.runDelete();
            } else {
                PokemonUtil.shouldSendDiff = false;
            }
        };

        //@todo using this thread impl. to test out priority setting
        Thread iden = new Thread(pokemonIdentifiers, identifier, "identifier");
        iden.setPriority(3); //5 is default priority
        iden.start();
        //then execute this guy
        //executor.execute(identifier);
    }
}
