package main.commands.utilitycommands.guildutil;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.NoSuchElementException;

public class ScreenShare implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        EmbedBuilder eb = new EmbedBuilder()
                .withDesc("[Click to join screen share](https://discordapp.com/channels/" + event.getGuild().getStringID()+"/");

        if (event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null) {
            eb.appendDesc(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().getStringID()+"/)");
        } else { // used arg
            try { // try name first
                eb.appendDesc(BotUtils.fuzzyVoiceMatch(event, args.get(0)).getStringID() + "/)");
            } catch (NoSuchElementException e) { // used id
                eb.appendDesc(args.get(0)).appendDesc("/)");
            }
        }

        BotUtils.send(event.getChannel(), eb);
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        if (event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null) return true;

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
