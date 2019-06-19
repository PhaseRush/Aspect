package main.commands.utilitycommands.guildutil;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class ScreenShare implements Command {
    private static Set<String> special = new HashSet<>(Collections.singletonList("all"));

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        EmbedBuilder eb = new EmbedBuilder();

        if (!args.isEmpty() && args.get(0).equals("all")) {
            eb.withTitle("Screenshare links for all voice channels");
            event.getGuild().getVoiceChannels()
                    .forEach(vc -> eb.appendDesc("\n[" + vc.getName() + "](https://discordapp.com/channels/" + event.getGuild().getStringID() + "/" + vc.getStringID() + "/)"));
            BotUtils.send(event.getChannel(), eb);
            return; // short circuit
        }
        // else not share all
        IVoiceChannel targetVC;
        if (event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null) {
            targetVC = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();
        } else { // used arg
            try { // try name first
                targetVC = event.getGuild().getVoiceChannelsByName(
                        BotUtils.autoCorrect(args.get(0),
                                event.getGuild().getVoiceChannels().stream()
                                        .map(IChannel::getName).collect(Collectors.toList())))
                        .get(0);
            } catch (NoSuchElementException e) { // used id
                targetVC = event.getGuild().getVoiceChannelByID(Long.valueOf(args.get(0)));
            }
        }
        eb.withDesc("[Click to join screen share for " + targetVC + "](https://discordapp.com/channels/" + event.getGuild().getStringID() + "/" + targetVC.getStringID() + "/)");

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null) return true;
        if (!args.isEmpty() && special.contains(args.get(0))) return true;

        try {
            return (!args.isEmpty() &&
                    event.getClient().getVoiceChannelByID(
                            Long.valueOf(args.get(0))) != null);
        } catch (NumberFormatException ignored) { // dont care, means that didnt use id but instead used name
            try {
                return BotUtils.fuzzyVoiceMatch(event, args.get(0)) != null;
            } catch (Exception e) { // no matching name either
                return false;
            }
        }
    }

    @Override
    public String getDesc() {
        return "provides a link to screenshare in a voice channel";
    }
}
