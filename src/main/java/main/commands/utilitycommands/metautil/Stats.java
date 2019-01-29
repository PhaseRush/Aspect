package main.commands.utilitycommands.metautil;

import main.Command;
import main.CommandManager;
import main.Main;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Stats implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(), generateStats());
    }

    private EmbedBuilder generateStats() {
        return new EmbedBuilder()
                .withTitle("Aspect v2.0")
                .withColor(Color.BLACK)
                .withThumbnail(Main.client.getApplicationIconURL())
                .withDesc(generateDesc())
                .withFooterIcon(Main.client.getUserByID(BotUtils.DEV_DISCORD_LONG_ID).getAvatarURL())
                .withFooterText("Built with love. " + BotUtils.GITHUB_URL_SHORT)

                // fields
                .appendField("Guilds", String.valueOf(Main.client.getGuilds().size()), true)
                .appendField("Users", "Total: " + Main.client.getGuilds().stream().mapToInt(u -> u.getUsers().size()).sum() + "\n" +
                                "Unique: " + Main.client.getGuilds().stream().flatMap(g -> g.getUsers().stream()).collect(Collectors.toSet()).size(), true)

                .appendField("Commands available", "Unique: "+ new HashSet<>(CommandManager.commandMap.values()).size() +
                        "\nAliases: " + CommandManager.commandMap.keySet().size(), true)
                ;
    }

    private String generateDesc() {
        StringBuilder sb = new StringBuilder()
                .append(Uptime.genUptime()).append("\n")         // uptime
                .append(CpuStats.genMessage()).append('\n')
                .append(CpuStats.genSyncMap().orElse(""));    // general stats

        CpuStats.genSyncMap().ifPresent(sb::append); // syncmap

        return sb.toString();
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }
}
