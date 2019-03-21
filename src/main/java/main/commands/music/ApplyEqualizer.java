package main.commands.music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.Visuals;
import main.utility.grapher.LineChart;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyEqualizer implements Command {
    private static final float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,-0.1f, -0.1f, -0.1f, -0.1f };
    private static final float[] BANDS = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

    private static final Map<IGuild, float[]> eqMap = new HashMap<>();

    private final EqualizerFactory equalizer = new EqualizerFactory();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        AudioPlayer player = MasterManager.getGuildAudioPlayer(event.getGuild()).getPlayer();

        float diff = getDiff(args);

        try {
            if (args.get(0).equals("stop")) {
                player.setFilterFactory(null);
                eqMap.remove(event.getGuild()); // remove eq
            } else if (args.get(0).equals("bass")) {
                player.setFilterFactory(setHighBass(diff, event.getGuild()));
            } else if (args.get(0).equals("treb")) {
                player.setFilterFactory(setLowBass(diff, event.getGuild()));
            } else if (args.get(0).equals("show")) {
                BotUtils.send(event.getChannel(),
                        new EmbedBuilder(),
                        generateEqChart(event.getGuild()),
                        "equalizer.png"
                );
            }

            BotUtils.reactWithCheckMark(event.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            BotUtils.reactWithX(event.getMessage());
        }

    }

    private ByteArrayInputStream generateEqChart(IGuild guild) {
        return new ByteArrayInputStream(
                Visuals.buffImgToOutputStream(
                        LineChart.generateOnePlot(
                                "Equalizer Configuration",
                                "Band",
                                BANDS,
                                eqMap.getOrDefault(guild, new float[BASS_BOOST.length])),
                        "png").toByteArray()
        );
    }

    private float getDiff(List<String> args) {
        try {
            return Float.valueOf(args.get(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private EqualizerFactory setHighBass(float diff, IGuild guild) {
        float[] currFilter = eqMap.getOrDefault(guild, new float[BASS_BOOST.length]);
        float[] newFilter = new float[BASS_BOOST.length];

        // init newFilter with the new weights
        for (int i = 0; i < currFilter.length; i++) {
            float eqVi = currFilter[i] + BASS_BOOST[i] + diff;
            newFilter[i] = eqVi;
            currFilter[i] = eqVi;
        }

        eqMap.put(guild, newFilter); // update map with newFilter

        for (int i = 0; i < currFilter.length; i++) {
            equalizer.setGain(i, newFilter[i]); // add the BASS values to current filter
        }

        return equalizer;
    }

    private EqualizerFactory setLowBass(float diff, IGuild guild) {
        float[] currFilter = eqMap.getOrDefault(guild, new float[BASS_BOOST.length]);
        float[] newFilter = new float[BASS_BOOST.length];

        // init newFilter with the new weights
        for (int i = 0; i < currFilter.length; i++) {
            float eqVi = currFilter[i] - BASS_BOOST[i] + diff;
            newFilter[i] = eqVi;
            currFilter[i] = eqVi;
        }

        eqMap.put(guild, newFilter); // update map with newFilter

        for (int i = 0; i < currFilter.length; i++) {
            equalizer.setGain(i, newFilter[i]); // add the BASS values to current filter
        }

        return equalizer;
    }


    // Predefined EQ configs
    private enum Filters {
        STOP(), // remove eq
        BASS_BOOST(), // bass boost
        BASS_REMOVE(), // negative bass boost
        EAR_RPE(); // no
    }


    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "Applies an equalizer to the music music";
    }
}
