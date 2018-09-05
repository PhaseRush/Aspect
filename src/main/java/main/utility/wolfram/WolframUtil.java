package main.utility.wolfram;

import com.wolfram.alpha.WAEngine;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.obj.Embed.EmbedField;

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

    public static Iterable<? extends EmbedField> handleRepeatedFields(List<EmbedField> embedFields) {
        Map<String, StringBuilder> map = new LinkedHashMap<>();

        //populate field names
        for (EmbedField ef : embedFields) {
            if (!map.keySet().contains(ef.getName()))
                map.put(ef.getName(), new StringBuilder());
        }

        //append values as necessary
        for (EmbedField ef : embedFields) {
            map.get(ef.getName()).append(ef.getValue() + '\n');
        }

        //format as table only if the it contains a vertical bar "|"
        for (String s : map.keySet()) {
            if (map.get(s).toString().contains("|"))
                handleFieldFormatting(map.get(s));
        }

        //turn map into list of embedfields
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
