package main.commands.music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import javafx.util.Pair;
import main.Command;
import main.utility.Visuals;
import main.utility.grapher.LineChart;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

import java.io.ByteArrayInputStream;
import java.util.*;

public class MusicEqualizer implements Command {
    private static final float[] BANDS = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

    // keep track of each guild's equalizer
    private static final Map<IGuild, Pair<EqualizerFactory,float[]>> eqMap = new HashMap<>();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        AudioPlayer player = MasterManager.getGuildAudioPlayer(event.getGuild()).getPlayer();


        // check if user passed in a filter. if so, determine what it is
        Optional<Filters> filter = Arrays.stream(Filters.values())
                .map(f -> new Pair<>(f, f.name().toLowerCase().replaceAll("_","")))
                .min((o1, o2) -> {
                    int first = BotUtils.stringSimilarityInt(o1.getValue(), args.get(0));
                    int second = BotUtils.stringSimilarityInt(o2.getValue(), args.get(0));
                    return Integer.compare(first, second);
                })
                .filter(p -> BotUtils.stringSimilarity(args.get(0), p.getValue()) < 2)
                .map(Pair::getKey);

        if (filter.isPresent()) {
            player.setFilterFactory(
                    applyFilter(event.getGuild(), filter.get().filter, 1)
            );
            BotUtils.send(event.getChannel(), "Applied " + filter.get().name() + " \n " + generateCurrEq(event));
        } else { // user did not use a filter (or spelling is just atrocious) so try other commands
            switch (args.get(0)) {
                case "stop" :
                case "null" :
                    player.setFilterFactory(null);
                    eqMap.remove(event.getGuild()); // remove eq
                    break;
                case "show" :
                    BotUtils.send(event.getChannel(),
                            new EmbedBuilder().withImage("attachment://equalizer.png"),
                            generateEqChart(event.getGuild()),
                            "equalizer.png"
                    );
                    break;
                case "mult" :
                    try {
                        replaceFilter(
                                event.getGuild(),
                                eqMap.getOrDefault(
                                        event.getGuild(),
                                        new Pair<>(new EqualizerFactory(), Filters.ZERO.filter)
                                ).getValue(),
                                Float.valueOf(args.get(1))
                        );
                        BotUtils.reactWithCheckMark(event.getMessage());
                    } catch (NumberFormatException e) {
                        BotUtils.send(event.getChannel(), "Must use valid multiplier");
                    }
                    break;
                case "custom" :
                    try {

                    } catch (Exception e) {
                        BotUtils.send(event.getChannel(), "There was an error processing the input");
                    }
                    break;
                case "dump" :
                    BotUtils.send(event.getChannel(), generateCurrEq(event));
                default:
                    BotUtils.reactWithX(event.getMessage());
                    return;
            }
        }
        BotUtils.reactWithCheckMark(event.getMessage()); // success
    }

    private String generateCurrEq(MessageReceivedEvent event) {
        float[] thisEq = eqMap.getOrDefault(event.getGuild(), new Pair<>(new EqualizerFactory(), Filters.ZERO.filter)).getValue();

        return new StringBuilder("Equalizer settings spread (" + thisEq.length + " bands) : ")
                .append(Arrays.toString(thisEq)).toString();

    }

    // Predefined EQ configs
    public enum Filters {
        // zero'd eq
        ZERO    (new float[] {     0,      0,     0,     0,      0,     0,      0,     0,     0,     0,     0,     0,     0,     0,     0 }),
        // all 0.1
        IDENTITY(new float[] {  0.1f,   0.1f,  0.1f,   0.1f,  0.1f,   0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f}),
        // bass boost
        BASS    (new float[] {  0.2f,  0.15f,  0.1f,  0.05f,  0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f }),
        BASS2   (mul(BASS.getFilter(), 0.5f)),
        // treble boost
        TREB    (new float[] { -0.2f, -0.15f, -0.1f, -0.05f, -0.0f,  0.05f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f,  0.1f }),
        // hill
        HILL    (new float[] { -0.2f, -0.15f, -0.1f, -0.05f,     0,   0.1f,  0.2f,  0.3f,  0.2f,  0.1f,     0,-0.05f, -0.1f, -0.15f,-0.2f }),
        // no
        EAR_RPE (new float[] {     1,      1,     1,      1,     1,      1,     1,     1,     1,     1,     1,     1,     1,     1,     1 });

        private float[] filter;

        Filters(float[] filter) {
            this.filter = filter;
        }

        public float[] getFilter() {
            return filter;
        }
    }

    private ByteArrayInputStream generateEqChart(IGuild guild) {
        return new ByteArrayInputStream(
                Visuals.buffImgToOutputStream(
                        LineChart.generateOnePlot(
                                "Equalizer Configuration",
                                "Band",
                                BANDS,
                                eqMap.getOrDefault(guild, new Pair<>(null, Filters.ZERO.getFilter())).getValue()),
                        "png").toByteArray()
        );
    }


    public static EqualizerFactory applyFilter(IGuild guild, float[] filter, float scalar) {
        Pair<EqualizerFactory, float[]> pair = eqMap.getOrDefault(guild, new Pair<>(new EqualizerFactory(), filter));
        float[] currFilter = pair.getValue();
        float[] newFilter = new float[filter.length];

        // init newFilter with the new weights and vals
        for (int i = 0; i < currFilter.length; i++) {
            float eqVi = currFilter[i] + filter[i]*scalar;
            newFilter[i] = eqVi;
            currFilter[i] = eqVi;
        }

        for (int i = 0; i < currFilter.length; i++) {
            pair.getKey().setGain(i, newFilter[i]); // add the values to current filter
        }

        eqMap.put(guild, new Pair<>(pair.getKey(), newFilter)); // update map with newFilter

        return pair.getKey(); // return the eq fac
    }

    public static EqualizerFactory replaceFilter(IGuild guild, float[] filter, float scalar) {
        Pair<EqualizerFactory, float[]> pair = eqMap.getOrDefault(guild, new Pair<>(new EqualizerFactory(), mul(filter, scalar)));

        for (int i = 0; i < filter.length; i++) {
            pair.getKey().setGain(i, filter[i] * scalar); // add the values to current filter
        }

        eqMap.put(guild, new Pair<>(pair.getKey(), filter)); // update map with newFilter

        return pair.getKey(); // return the eq fac
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "Applies an equalizer to the music music";
    }

    // adds each element of two into one, requires one.len >= two.len
    private static float[] merge(float[] one, float[] two) {
        for (int i = 0; i < one.length; i++) {
            one[i] += two[i];
        }
        return one;
    }

    // adds s to each element in one
    private static float[] add(float[] one, float s) {
        for (int i = 0; i < one.length; i++) {
            one[i] += s;
        }
        return one;
    }

    // multiplies each element in one by scalar
    private static float[] mul(float[] one, float scalar) {
        for (int i = 0; i < one.length; i++) {
            one[i] *= scalar;
        }
        return one;
    }

    private static float[] dupe(float val, int num) {
        float[] f = new float[num];
        for (int i = 0; i < num; i++) {
            f[i] = val;
        }
        return f;
    }

    public static Pair<EqualizerFactory, float[]> getGuildEq (IGuild guild) {
        return eqMap.get(guild);
    }
}
