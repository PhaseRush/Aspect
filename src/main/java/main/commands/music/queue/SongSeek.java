package main.commands.music.queue;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.TrackScheduler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongSeek implements Command {
    private static Pattern mmss = Pattern.compile("[0-9][0-9]:[0-9][0-9]");
    private static Pattern hhmmssmili = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]:[0-9][0-9][0-9][0-9]");

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            TrackScheduler scheduler = MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler();
            scheduler.getCurrentTrack().setPosition(
                    parseDuration(args.get(0)));

            BotUtils.reactWithCheckMark(event.getMessage());
        } catch (Exception e) {
            BotUtils.reactWithX(event.getMessage());
            BotUtils.send(event.getChannel(), "Please check the formatting: " + getDesc() +
                    "\nOr your duration is past the length of this song.");
        }
    }

    private long parseDuration(String input) {
        int hours = 0, mins = 0, secs = 0, millis = 0;

        Matcher mmssMatcher = mmss.matcher(input);
        if (mmssMatcher.find()) {
            int firstIdx = input.indexOf(':');
            mins = Integer.valueOf(input.substring(0, firstIdx));
            secs = Integer.valueOf(input.substring(firstIdx+1));
        } else {
            Matcher otherMatcher = hhmmssmili.matcher(input);
            if (otherMatcher.find()) {
                int firstIdx = input.indexOf(':');
                int secondIdx = input.substring(firstIdx).indexOf(':');
                int thirdIdx = input.substring(secondIdx).indexOf(':');

                hours = Integer.valueOf(input.substring(0, firstIdx));
                mins = Integer.valueOf(input.substring(firstIdx+1, secondIdx));
                secs = Integer.valueOf(input.substring(secondIdx+1, thirdIdx));
                millis = Integer.valueOf(input.substring(thirdIdx+1));
            } else { // no matches
                return -1;
            }
        }

        // calculate duration
        return hours * 60*60*1000 +
                mins *    60*1000 +
                secs *       1000 +
                millis;
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler().getQueue().isEmpty();
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "Set the current song to start playing at a certain time.\nFormat your input as: mm:ss or hh:mm:ss:mili" +
                "ex. skipping to 3 mins and 14 seconds -> 3:14, 4 hrs 18 sec -> 4:00:18, 1 min 1 sec 1 millisecond, 1:01:0001";
    }
}
