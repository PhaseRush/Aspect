package main.commands.utilitycommands.channel;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class WordReaction implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.reactAllEmojis(
                getMsg(event.getChannel(), args, event.getMessage()),
                getRxns(args.get(0)));
    }

    private List<ReactionEmoji> getRxns(String word) {
        List<ReactionEmoji> emojis = new ArrayList<>();
        for (char c : word.toCharArray()) {
            emojis.add(BotUtils.getRegionalChar(c)); // iffy
        }

        return emojis;
    }

    private IMessage getMsg(IChannel channel, List<String> args, IMessage caller) {
        if (args.size() == 2) {
            try {// first check if args passed specific id
                long id = Long.valueOf(args.get(1));
                return channel.getClient().getMessageByID(id);
            } catch (NumberFormatException e) {
            } // was not id :(
        }
        // next check if someone was mentioned, ret most recent msg by them in last 10 minutes if so
        if (!caller.getMentions().isEmpty()) {
            // def custom comparator to get most recent
            Optional<IMessage> msg = channel.getMessageHistoryTo(Instant.now().minus(10, ChronoUnit.MINUTES))
                    .stream()
                    .filter(iMsg -> iMsg.getAuthor().equals(caller.getMentions().get(0)))
                    .max(Comparator.comparingLong(o -> o.getTimestamp().toEpochMilli())); // gets maximum time, so most recent
            if (msg.isPresent()) return msg.get();
        }

        // last case, return most recent msg
        IMessage ret = channel.getMessageHistoryTo(caller.getTimestamp(), 2).get(0);
        return ret;
    }

    @Override
    public String getDesc() {
        return "Reacts to target message with your favorite word." +
                "By default reacts to message right before the caller, but can specify id for a specific message" +
                " or tag someone to react to their most recent message";
    }
}
