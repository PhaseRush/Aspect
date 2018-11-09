package main.commands.wolfram;

import com.wolfram.alpha.*;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.wolfram.WolframUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class WolframGeneral implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        boolean formatVerticalBar = false;
        if (args.size() == 2)
            formatVerticalBar = true;

        WAEngine engine = WolframUtil.engine;
        WAQuery query = engine.createQuery();
        query.setInput(args.get(0));

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Advanced Wolfram Query")
                .withColor(Visuals.getVibrantColor());

        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:\n" + engine.toURL(query));

            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);
            queryResult.acquireImages();

            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            } else {
                // Got a result.
                List<EmbedField> embedFields = new ArrayList<>();
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    String content = ((WAPlainText) element).getText();
                                    if (!(content.equals("") || pod.getTitle().equals(""))) {
                                        embedFields.add(new EmbedField(pod.getTitle(), (formatVerticalBar ? content.replaceAll("[|]", ":\t") : content), false));
                                    }
                                } else if (element instanceof WAAssumption) {
                                    eb.withDesc(((WAAssumption) element).getDescription());
                                } else if (element instanceof WAImage) {
                                    eb.withImage(((WAImage) element).getURL());
                                    System.out.println("Image url: " + ((WAImage) element).getURL());
                                } else
                                    System.out.println("Element was not an instance of any WAObject");
                            }
                        }
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.

                for (EmbedField ef : WolframUtil.handleRepeatedFields(embedFields)) {
                    try {
                        eb.appendField(ef);
                    } catch (IllegalArgumentException e) {
                        eb.appendField(new EmbedField(ef.getName(), ef.getValue().substring(0, 1023), false));
                    }
                }
                eb.withFooterText("This operation took " + queryResult.getTiming() * 1000 + " ms");
            }
        } catch (WAException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            BotUtils.sendMessage(event.getChannel(), "The parsed result is longer than 2000 characters and cannot be displayed\nor there is a different problem and i should just kms");
        }

        BotUtils.sendMessage(event.getChannel(), eb);
    }



    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Wolfram Alpha - Deep query.";
    }
}
