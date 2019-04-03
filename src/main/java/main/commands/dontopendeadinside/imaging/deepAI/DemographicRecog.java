package main.commands.dontopendeadinside.imaging.deepAI;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import main.Command;
import main.utility.TableUtil;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class DemographicRecog extends DeepAI implements Command {
    private static List<String> AutocorrectBlackList = Arrays.asList("fate");

    public List<String> getAutocorrectBlackList() {
        return AutocorrectBlackList;
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String targetUrl = getTargetUrl(event, args);
        String json = fetchJson("https://api.deepai.org/api/demographic-recognition", targetUrl);

        Face target;
        try {
            target = BotUtils.gson.fromJson(json, Container.class).output.faces[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            BotUtils.send(event.getChannel(), "Sorry, no face found :(");
            return;
        }
        BotUtils.send(event.getChannel(), new EmbedBuilder()
                .withTitle("Demographic Recognition")
                .withDesc(generateDesc(target)));
    }

    private String generateDesc(Face target) {
        GridTable table = GridTable.of(2, 3)
                .put(0, 0, Cell.of("Category"))
                .put(0, 1, Cell.of("Estimate"))
                .put(0, 2, Cell.of("Certainty"))

                .put(1, 0, Cell.of("Age range", "Gender", "Ethnicity"))
                .put(1, 1, Cell.of(generateStats(target)))
                .put(1, 2, Cell.of(generateError(target)));

        table = Border.DOUBLE_LINE.apply(table);

        return TableUtil.renderInCodeBlock(table).toString(); // lol this is so hacky
    }

    private String[] generateStats(Face target) {
        String[] obj = new String[3];
        obj[0] = target.age_range[0] + " - " + target.age_range[1];
        obj[1] = target.gender;
        obj[2] = target.cultural_appearance;
        return obj;
    }

    private String[] generateError(Face target) {
        String[] obj = new String[3];
        obj[0] = target.age_range_confidence*100 + " %";
        obj[1] = target.gender_confidence*100 + " %";
        obj[2] = target.cultural_appearance_confidence*100 + " %";
        return obj;
    }

    @Override
    public String getDesc() {
        return "Guesses your age, gender, and race based on your image";
    }

    private class Container {
        Output output;
    }

    private class Output {
        Face[] faces;
    }

    private class Face {

        int[] age_range;
        String gender;
        String cultural_appearance;

        float gender_confidence;
        float age_range_confidence;
        float cultural_appearance_confidence;

        int[] bounding_box;
    }
}
