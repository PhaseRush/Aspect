package main.commands.utilitycommands.channel;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DeleteWaifus implements Command {
    private static final String MUDAE = "527432104204697600"; //527432104204697600
    private static final List<String> filterCommands = Arrays.asList("$w", "$mu", "$h", "$i", "$im", "$wg", "$wishlist", "$wish", "$ima", "mm");

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // ask for confirmation
        EmbedBuilder confirmEb = new EmbedBuilder()
                .withTitle("Confirm Delete?")
                .withColor(Color.BLACK)
                .withDesc("Confirm channel cleanup");

        IMessage confirmMsg = BotUtils.sendGet(event.getChannel(), confirmEb);

        IListener reactionListener = (IListener<ReactionAddEvent>) reactionEvent -> {
            if (reactionEvent.getUser().equals(event.getAuthor()) && reactionEvent.getMessage().getStringID().equals(confirmMsg.getStringID())) {
                String emojiName = reactionEvent.getReaction().getEmoji().getName();
                switch (emojiName) {
                    case "\u2705": // check mark
                        List<IMessage> msgs = event.getChannel().getMessageHistoryTo(Instant.now().minus(1, ChronoUnit.HOURS));
                        runDelete(event, msgs);
                        break;
                    case "\u274c": // x
                        BotUtils.send(event.getChannel(), "Exiting cleanup");
                        break;
                    default:
                        BotUtils.send(event.getChannel(), "Not a valid reaction, exiting cleanup");
                        break;
                }
                if (!confirmMsg.isDeleted()) //just in case
                    confirmMsg.delete();
            }
        };

        // react with emojis
        BotUtils.reactAllEmojis(confirmMsg, Arrays.asList(ReactionEmoji.of("\u2705"), ReactionEmoji.of("\u274c")));

        // register listener
        event.getClient().getDispatcher().registerListener(reactionListener);

        // unregister listener after 10000 ms
        Runnable removeListener = () -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            } finally { //please just execute this no matter what
                event.getClient().getDispatcher().unregisterListener(reactionListener);
                if (!confirmMsg.isDeleted()) confirmMsg.delete();

            }
        };
        executorService.execute(removeListener);
    }

    public void runDelete(MessageReceivedEvent event, List<IMessage> msgs) {
        event.getChannel().bulkDelete(
                msgs.stream()
                        .filter(msg -> msg.getAuthor().getStringID().equals(MUDAE) ||
                                filterCommands.contains(msg.getContent()))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true; // not problem
    }

    @Override
    public String getDesc() {
        return "cleans up a text-chat after someone fucks it up again";
    }
}
