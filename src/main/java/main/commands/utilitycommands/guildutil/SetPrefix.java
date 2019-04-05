package main.commands.utilitycommands.guildutil;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SetPrefix implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String targetPrefix = args.get(0);
        // ask for guild or channel
        EmbedBuilder confirmEb = new EmbedBuilder()
                .withTitle(String.format("Would you like to set the prefix for the entire server (%s), or just this channel (%s) ?", event.getGuild().getName(), event.getChannel().getName()))
                .withColor(Color.BLACK)
                .withDesc(":regional_indicator_A: \tserver wide change" +
                        ":regional_indicator_B:\tspecific text channel override" +
                        "\nNew prefix : `" + targetPrefix + "`" +
                        "\nNote: next time, remember to use `" + targetPrefix + "prefix $" + "`" +
                        "\nUse `[current prefix]help setPrefix` if you need help");

        IMessage confirmMsg = BotUtils.sendGet(event.getChannel(), confirmEb);

        IListener reactionListener = (IListener<ReactionAddEvent>) reactionEvent -> {
            if (reactionEvent.getUser().equals(event.getAuthor()) && reactionEvent.getMessage().getStringID().equals(confirmMsg.getStringID())) {
                String emojiName = reactionEvent.getReaction().getEmoji().getName();
                switch (emojiName) {
                    case "\uD83C\uDDE6":
                        BotUtils.setPrefix(event.getGuild(), args.get(0));
                        BotUtils.reactWithCheckMark(event.getMessage());
                        break;
                    case "\uD83C\uDDE7":
                        BotUtils.setPrefix(event.getChannel(), args.get(0));
                        BotUtils.reactWithCheckMark(event.getMessage());
                        break;
                    default:
                        BotUtils.send(event.getChannel(), "Not a valid reaction, exiting. Use help if needed");
                        break;
                }
                if (!confirmMsg.isDeleted()) //just in case
                    confirmMsg.delete();
            }
        };

        // react with emojis
        BotUtils.reactAllEmojis(confirmMsg, Arrays.asList(ReactionEmoji.of("\uD83C\uDDE6"), ReactionEmoji.of("\uD83C\uDDE7")));
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return BotUtils.isDev(event) || // is dev
                event.getGuild().getOwner().equals(event.getAuthor()) || // is owner
                event.getAuthor().getRolesForGuild(event.getGuild()).stream() // or has admin OR kick permissions
                        .anyMatch(r -> r.getPermissions().contains(Permissions.ADMINISTRATOR) || r.getPermissions().contains(Permissions.BAN));
    }

    @Override
    public String getDesc() {
        return "Sets the prefix for either a guild or a server, depending on the ID\n" +
                "Example scenario: Current server called S has three text channels called C1, C2, and C3, and all prefixes are default $\n" +
                "We want to change the server prefix to %, and we do that by using `$setprefix %`, and reacting to \"A\". Now all text channels in this server need to use %\n" +
                "However, lets say that we now want ONLY C2 to have a special prefix of *, but have all other channels use the the server default of %. We do this by using `%setprefix *`, and reacting to \"B\"" +
                "Now, only C2 will use *, while C1 and C3 use %";
    }
}
