package main.commands.warframe;

import main.Command;
import main.utility.Visuals;
import main.utility.WarframeUtil;
import main.utility.metautil.BotUtils;
import main.utility.warframe.market.marketlistings.WarframeListingUser;
import main.utility.warframe.market.marketlistings.WarframeListingsPayloadContainer;
import main.utility.warframe.market.marketlistings.WarframeTradeListing;
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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class WfMarketListing implements Command {
    //might declare a field MessageReceivedEvent e;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        List<String> intendedItemNames = WarframeUtil.getIntendedStrings(args.get(0));
        String filterOnline = (args.size() == 2 ? args.get(1) : "");

        if (intendedItemNames.get(0).equals("Index 1 is a perfect match"))
            finishCommand(intendedItemNames.get(1), event, filterOnline);
        else {
            handleUserReactionWait(intendedItemNames, event, filterOnline);
        }
    }

    private void finishCommand(String intendedItemName, MessageReceivedEvent event, String filterParam) { //@todo change args to something more reasonable later
        String urlName = WarframeUtil.getItemUrlName(intendedItemName);

        String json = BotUtils.getStringFromUrl("https://api.warframe.market/v1/items/" + urlName + "/orders");
        WarframeListingsPayloadContainer payload = BotUtils.gson.fromJson(json, WarframeListingsPayloadContainer.class);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Warframe Market | Price check for " + intendedItemName)
                .withColor(Visuals.getVibrantColor())
                .withThumbnail(WarframeUtil.getItemImageUrl(intendedItemName));
        List<WarframeTradeListing> listings = payload.getPayload().getOrders();


        // default : filter out users that are not ingame
        // if user provides second argument, do not filter.
        if (filterParam.equals("ingame"))
            listings.removeIf(warframeTradeListing -> {
                WarframeListingUser user = warframeTradeListing.getUser();
                return !user.getStatus().equals("ingame");
            });

        //filter out the wtb
        listings.removeIf(new Predicate<WarframeTradeListing>() {
            @Override
            public boolean test(WarframeTradeListing warframeTradeListing) {
                return warframeTradeListing.getOrder_type().equals("buy");
            }
        });

        listings.sort(new Comparator<WarframeTradeListing>() {
            @Override
            public int compare(WarframeTradeListing o1, WarframeTradeListing o2) {
                return Integer.compare(o1.getPlatinum(), o2.getPlatinum()); //i actaully have no idea if this is what youre supposed to do. I will now assume it is correct and move on
            }
        });

        for (int i = 0; i < (listings.size() < 11 ? listings.size() : 10); i++) { //display 10 cheapest listings
            WarframeTradeListing listing = listings.get(i);
            WarframeListingUser seller = listing.getUser();
            eb.appendField(seller.getIngame_nmae() + " | " + seller.getStatus() + " | plat: " + listing.getPlatinum(),
                    ///w HyperfluousKat Hi! I want to buy: Nano-Applicator for 100 platinum. (warframe.market)
                    "/w " + seller.getIngame_nmae() + " Hi! I want to buy your " + intendedItemName + " for " + listing.getPlatinum() + " platinum.", false);
        }

        BotUtils.send(event.getChannel(), eb);
    }

    private void handleUserReactionWait(List<String> intendedItemNames, MessageReceivedEvent e, String filterParam) {
        IUser userToWaitFor = e.getAuthor();
        IChannel channelToWaitFor = e.getChannel();
        final int numOptions = 5;
        final int secondsTimeout = 10;


        EmbedBuilder itemOptionEmbed = new EmbedBuilder()
                .withDesc("Your query did not exactly match any item.\n React with the corresponding letter to continue\n\n" + BotUtils.buildOptions(intendedItemNames, numOptions))
                .withColor(Visuals.getVibrantColor());

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
                            finishCommand(intendedItemNames.get(0), e, filterParam);
                            break;
                        case "\uD83C\uDDE7":
                            finishCommand(intendedItemNames.get(1), e, filterParam);
                            break;
                        case "\uD83C\uDDE8":
                            finishCommand(intendedItemNames.get(2), e, filterParam);
                            break;
                        case "\uD83C\uDDE9":
                            finishCommand(intendedItemNames.get(3), e, filterParam);
                            break;
                        case "\uD83C\uDDEA":
                            finishCommand(intendedItemNames.get(4), e, filterParam);
                            break;
                        case "❌":
                            BotUtils.send(e.getChannel(), "Command terminated.");
                            break;
                        default:
                            BotUtils.send(e.getChannel(), "Not a valid reaction; command will be terminated.");
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

    @Override
    public String getDesc() {
        return "retrieves market listings for item. can filter based on online ingame and offline";
    }
}
