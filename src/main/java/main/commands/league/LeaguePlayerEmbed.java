package main.commands.league;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.common.Division;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMasteries;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.league.LeagueUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class LeaguePlayerEmbed implements Command {
    private List<String> suggestions = new ArrayList<>();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args){
        boolean notNA = args.size() == 2;
        String ign = args.get(0);
        Summoner summoner;

        if (!notNA) { //from NA -- double negative op for sure
            summoner = Orianna.summonerNamed(ign).get();
        } else { //not from NA
            summoner = Orianna.summonerNamed(ign).withRegion(LeagueUtil.parseRegion(args.get(1))).get();
        }

        boolean validProfileIcon = true;
        try {
            summoner.getProfileIcon().getImage().get();
        } catch (ForbiddenException e) {
            BotUtils.sendMessage(event.getChannel(), "Kat's api key is outdated. Spam her until she decides to mute you all");
        } catch (NullPointerException e) {
            //thrown when Profile icon id = 1?
            validProfileIcon = false;
        }
        ChampionMasteries cms = summoner.getChampionMasteries();
        ChampionMastery cm = cms.get(0);

        //cant do getLeague.getTier -> might give NullPE
        boolean isRanked = summoner.getLeague(Queue.RANKED_SOLO_5x5) != null;


        String rank = summoner.getLeague(Queue.RANKED_SOLO_5x5).getTier().name().toLowerCase();
        Division division = summoner.getLeaguePosition(Queue.RANKED_SOLO_5x5).getDivision();
        // String rankTier = summoner.getLeague(Queue.RANKED_SOLO_5x5).getTier();

        EmbedBuilder eb = new EmbedBuilder()
                .withAuthorName(summoner.getName())
                .withTitle("League of Legends analytics - 8.11")
                .withUrl("https://github.com/PhaseRush/Aspect")
                .withThumbnail("http://opgg-static.akamaized.net/images/medals/" + BotUtils.capitalizeFirst(rank) + "_" + "" + ".png")
                .withDesc(ign + " is a " + (isRanked ? rank + " " + division.name() : "") + " " + cm.getChampion().getName()
                        + " main "  + (isRanked? "in " + summoner.getLeague(Queue.RANKED_SOLO_5x5).getName() : ""))
                .withColor(validProfileIcon ? (Visuals.analyizeImageColor(summoner.getProfileIcon().getImage().get())) : Visuals.getVibrantColor())
                //.withColor(Visuals.analyizeWeightedImageColor(summoner.getProfileIcon().getImage().get(), 4))
                .withTimestamp(System.currentTimeMillis())
                .withAuthorUrl(getOpggUrl(ign))
                .withAuthorIcon((validProfileIcon ? summoner.getProfileIcon().getImage().getURL() : null))
                .withImage((validProfileIcon ? summoner.getProfileIcon().getImage().getURL() : null));

        try {
            //final SearchableList<ChampionMastery> pro1 = cms.filter((final ChampionMastery mastery1) -> mastery1.getPoints() >= 250000);
            final SearchableList<ChampionMastery> pro = cms.filter((final ChampionMastery mastery) -> mastery.getLevel() == 7);
            for (final ChampionMastery mastery : pro)
                eb.appendField(mastery.getChampion().getName(), mastery.getPoints() + " mastery points", false);
        } catch (NullPointerException npe) {
            //uhh. Mo idea just don't execute then I guess
        }

        IMessage iMessage = event.getChannel().sendMessage(eb.build());
        iMessage.addReaction(ReactionEmoji.of("blobfish", 362999190453747712L)); //why....

        System.out.println("HSB Converted: " + (validProfileIcon ? Visuals.analyizeImageColor(summoner.getProfileIcon().getImage().get()) : "No valid profile icon"));
    }

    private String getOpggUrl(String ign) { //todo hard coded NA value
        return "http://na.op.gg/summoner/userName=" + ign.replaceAll(" ", "+");
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Generates visual for a player";
    }
}
