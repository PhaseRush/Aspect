package main.commands.music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class ApplyEqualizer implements Command {
    private static final float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,-0.1f, -0.1f, -0.1f, -0.1f };
    private final EqualizerFactory equalizer = new EqualizerFactory();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        AudioPlayer player = MasterManager.getGuildAudioPlayer(event.getGuild()).getPlayer();

        float diff = getDiff(args);

        try {
            if (args.get(0).equals("stop")) {
                player.setFilterFactory(null);
            } else if (args.get(0).equals("bass")) {
                player.setFilterFactory(setHighBass(diff));
            } else if (args.get(0).equals("treb")) {
                player.setFilterFactory(setLowBass(diff));
            }
        } catch (Exception e) {
            e.printStackTrace();
            BotUtils.reactWithX(event.getMessage());
        }

        BotUtils.reactWithCheckMark(event.getMessage());
    }

    private float getDiff(List<String> args) {
        try {
            return Float.valueOf(args.get(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private EqualizerFactory setHighBass(float diff) {
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizer.setGain(i, BASS_BOOST[i] + diff);
        }
        return equalizer;
    }
    private EqualizerFactory setLowBass(float diff) {
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizer.setGain(i, -BASS_BOOST[i] + diff);
        }
        return equalizer;
    }


    // Predefined EQ configs
    private enum Filters {
        STOP(), // remove eq
        BASS_BOOST(), // bass boost taken from https://github.com/sedmelluq/lavaplayer/blob/master/testbot/src/main/java/com/sedmelluq/discord/lavaplayer/demo/music/MusicController.java
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
