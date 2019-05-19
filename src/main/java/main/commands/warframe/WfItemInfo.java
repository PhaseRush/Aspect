package main.commands.warframe;

import main.Aspect;
import main.Command;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import main.utility.warframe.market.itemdetail.WarframeDetailedDrop;
import main.utility.warframe.market.itemdetail.WarframeDetailedItem;
import main.utility.warframe.market.itemdetail.WarframeItemDetailPayloadContainer;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WfItemInfo implements Command {
    MessageReceivedEvent e;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        e = event;
        List<String> intendedItemNames = WarframeUtil.getIntendedStrings(args.get(0));

        //if its a perfect match, just use the first String - WHICH IS IN INDEX 1
        if (intendedItemNames.get(0).equals("Index 1 is a perfect match")) {
            finishCommand(intendedItemNames.get(1)); //changed this
            Aspect.LOG.info("perfect match trigger");
        } else { //ask user first before getting url name
            handleUserReactionWait(intendedItemNames);
        }
        //put end of this function in helper: "finishCommand"
    }

    private void finishCommand(String itemName) {
        String jsonURL = "https://api.warframe.market/v1/items/" + WarframeUtil.getItemUrlName(itemName);
        WarframeItemDetailPayloadContainer payloadContainer = BotUtils.gson.fromJson(BotUtils.getStringFromUrl(jsonURL), WarframeItemDetailPayloadContainer.class);
        //WarframeDetailedItem item = payloadContainer.getPayload().getItem().getItems_in_set()[0]; //@todo problem here. assuming is first item.

        WarframeDetailedItem[] items = payloadContainer.getPayload().getItem().getItems_in_set();
        WarframeDetailedItem item = items[0]; //assume default


        //try to find exact name match
        for (WarframeDetailedItem i : items) {
            if (i.getEn().getItem_name().equalsIgnoreCase(itemName)) {
                item = i;
            }
        }

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe | " + itemName)
                .withUrl(item.getEn().getWiki_link())
                .withThumbnail(WarframeUtil.getItemImageUrl(itemName))
                .withColor(Visuals.getRandVibrandColour());

        StringBuilder drops = new StringBuilder();
        for (WarframeDetailedDrop d : item.getEn().getDrop()) {
            drops.append(d.getName() + ", ");
        }
        eb.appendField("Drops", (drops.toString().equals("") ? "not found in drop tables" : drops.toString()), false);

        BotUtils.send(e.getChannel(), eb);
    }

    private void handleUserReactionWait(List<String> intendedItemNames) {
        IUser userToWaitFor = e.getAuthor();
        IChannel channelToWaitFor = e.getChannel();
        final int numOptions = 5;
        final int secondsTimeout = 10;


        EmbedBuilder itemOptionEmbed = new EmbedBuilder()
                .withDesc("Your query did not exactly match any item.\n React with the corresponding letter to continue\n\n" + BotUtils.buildOptions(intendedItemNames, numOptions))
                .withColor(Visuals.getRandVibrandColour());

        IMessage embedMessage = RequestBuffer.request(() -> e.getChannel().sendMessage(itemOptionEmbed.build())).get();
        BotUtils.reactAllEmojis(embedMessage, BotUtils.getRegionals().subList(0, numOptions));
        //might need to sleep thread to guarantee this comes last.
        RequestBuffer.request(() -> {
            embedMessage.addReaction(ReactionEmoji.of("❌"));
        }).get(); // the age old solution to "optimization" :tm:


        IListener reactionListener = new IListener<ReactionAddEvent>() {
            /**
             * Invoked when the {@link EventDispatcher} this listener is registered with fires an event of type {@link T}.
             *
             * @param reactionEvent The event object.
             */
            @Override
            public void handle(ReactionAddEvent reactionEvent) {
                if (reactionEvent.getUser().equals(userToWaitFor) && reactionEvent.getChannel().equals(channelToWaitFor)) {
                    String emojiName = reactionEvent.getReaction().getEmoji().getName();
                    switch (emojiName) {
                        case "\uD83C\uDDE6":
                            finishCommand(intendedItemNames.get(0));
                            break;
                        case "\uD83C\uDDE7":
                            finishCommand(intendedItemNames.get(1));
                            break;
                        case "\uD83C\uDDE8":
                            finishCommand(intendedItemNames.get(2));
                            break;
                        case "\uD83C\uDDE9":
                            finishCommand(intendedItemNames.get(3));
                            break;
                        case "\uD83C\uDDEA":
                            finishCommand(intendedItemNames.get(4));
                            break;
                        case "\u274C": //changed from red cross literal
                            send("Command terminated.");
                            break;
                        default:
                            send("Not a valid reaction; command will be terminated.");
                            break;
                    }
                    if (!embedMessage.isDeleted()) //just in case
                        embedMessage.delete();
                }
            }
        };

        //register this listener
        e.getClient().getDispatcher().registerListener(reactionListener);

        ExecutorService executorService = Executors.newFixedThreadPool(1);//no idea how many i need. seems like a relatively simple task?
        Runnable removeListener = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(secondsTimeout * 1000);
                } catch (InterruptedException e1) {
                    Aspect.LOG.info("guess this is fucked");
                } finally { //please just execute this no matter what
                    e.getClient().getDispatcher().unregisterListener(reactionListener);
                    Aspect.LOG.info("Listener Deleted");
                }
            }
        };
        executorService.execute(removeListener);
    }

    private void send(String message) {
        BotUtils.send(e.getChannel(), message);
    }

    @Override
    public String getDesc() {
        return "info about item drop rates.";
    }
}