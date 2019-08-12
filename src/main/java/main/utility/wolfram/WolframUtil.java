package main.utility.wolfram;

import com.wolfram.alpha.*;
import main.Aspect;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WolframUtil {
    public static WAEngine engine;

    static {
        engine = new WAEngine();
        engine.setAppID(BotUtils.WOLFRAM_API_KEY);
        //plaintext is the only format available
        engine.addFormat("plaintext");
    }

    public static EmbedBuilder runQuery(String inputQuery, boolean formatVerticalBar, boolean advanced) {
        WAQuery query = engine.createQuery();
        query.setInput(inputQuery);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Wolfram Alpha Query")
                .withColor(Visuals.getWolframColour());

        List<EmbedField> embedFields;
        try {
            // For educational purposes, print out the URL we are about to send:
            Aspect.LOG.info("Query URL:\n" + engine.toURL(query));

            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);
            queryResult.acquireImages();

            if (queryResult.isError()) {
                Aspect.LOG.info("Query error");
                Aspect.LOG.info("  error code: " + queryResult.getErrorCode());
                Aspect.LOG.info("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                Aspect.LOG.info("Query was not understood; no results available.");
                eb.withDesc("Query was not understood; no results available.");
            } else {
                // Have result, check if advanced
                embedFields = addFields(queryResult, formatVerticalBar, advanced);

                for (EmbedField ef : handleRepeatedFields(embedFields)) {
                    try {
                        eb.appendField(ef);
                    } catch (IllegalArgumentException e) {
                        eb.appendField(new EmbedField(ef.getName(), ef.getValue().substring(0, 1023), false));
                    }
                }

                eb.withFooterText("This operation took " + Math.round(queryResult.getTiming() * 1000 * 1000) / 1000 + " ms");
            }
        } catch (WAException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            return null;
        }

        return eb;
    }

    private static List<EmbedField> addFields(WAQueryResult queryResult, boolean formatVerticalBar, boolean advanced) {
        List<EmbedField> embedFields = new ArrayList<>();


        WAPod[] pods = queryResult.getPods();
        for (int i = 0; i < (advanced ? pods.length : Math.min(pods.length, 2)); i++) {
            WAPod pod = pods[i];
            if (!pod.isError()) {
                for (WASubpod subpod : pod.getSubpods()) {
                    for (Object element : subpod.getContents()) {
                        if (element instanceof WAPlainText) {
                            String content = ((WAPlainText) element).getText();
                            if (!(content.equals("") || pod.getTitle().equals(""))) {
                                embedFields.add(new EmbedField(pod.getTitle(), (formatVerticalBar ? content.replaceAll("[|]", ":\t") : content), false));
                            }
                        }
                    }
                }
            }
        }

        return embedFields;
    }

    private static Iterable<? extends EmbedField> handleRepeatedFields(List<EmbedField> embedFields) {
        Map<String, StringBuilder> map = new LinkedHashMap<>();

        //populate field names
        for (EmbedField ef : embedFields) {
            if (!map.keySet().contains(ef.getName()))
                map.put(ef.getName(), new StringBuilder());
        }

        //append values as necessary
        for (EmbedField ef : embedFields) {
            map.get(ef.getName()).append(ef.getValue()).append('\n');
        }

        //format as table only if the it contains a vertical bar "|"
        for (String s : map.keySet()) {
            if (map.get(s).toString().contains("|"))
                handleFieldFormatting(map.get(s));
        }

        //turn initDataMap into list of embedfields
        List<EmbedField> formattedFields = new ArrayList<>();
        for (String fieldName : map.keySet()) {
            formattedFields.add(new EmbedField(fieldName, map.get(fieldName).toString(), false));
        }
        return formattedFields;
    }

    private static void handleFieldFormatting(StringBuilder fieldValueString) {
        int longestRow = longestRowLength(fieldValueString);
        int numRows = fieldValueString.toString().split("\n").length;
        int numCols = fieldValueString.toString().split("[|]").length;
    }


    private static int longestRowLength(StringBuilder stringBuilder) {
        int longestRow = 0;
        String[] rows = stringBuilder.toString().split("\n");
        for (String s : rows) {
            if (s.length() > longestRow)
                longestRow = s.length();
        }
        return longestRow;
    }

}
