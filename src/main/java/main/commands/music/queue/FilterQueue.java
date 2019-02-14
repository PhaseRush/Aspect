package main.commands.music.queue;

import main.Command;
import main.utility.metautil.BotUtils;
import main.utility.music.GuildMusicManager;
import main.utility.music.MasterManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class FilterQueue implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GuildMusicManager guildMusicManager = MasterManager.getGuildAudioPlayer(event.getGuild());
        guildMusicManager.getScheduler().setQueue(
                guildMusicManager.getScheduler().getQueue().stream()
                        .filter(track -> track.getInfo().title.toLowerCase().contains(args.get(0).toLowerCase()))
                        .collect(Collectors.toList())
        );

        BotUtils.reactWithCheckMark(event.getMessage());
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !MasterManager.getGuildAudioPlayer(event.getGuild()).getScheduler().getQueue().isEmpty() &&
                !args.isEmpty();
    }

    @Override
    public boolean requireSynchronous() {
        return true;
    }

    @Override
    public String getDesc() {
        return "Filters the current music queue by keywords in title";
    }
}
