package main.commands.humor;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.maths.MathUtil;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class Ship implements Command {
    private List<String> advice = new ArrayList<>();

    public Ship() {
        initAdvice();
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IGuild iGuild = event.getGuild();
        String cupidId = "";
        try {
            if (args.size() == 1) {
                cupidId = event.getAuthor().getStringID();
                handleLoveMatch(event.getAuthor(),
                        iGuild.getUserByID(Long.valueOf(args.get(0).replaceAll("[^0-9]", ""))), event);
            } else {
                cupidId = args.get(0).replaceAll("[^0-9]", "");
                String psycheId = args.get(1).replaceAll("[^0-9]", "");
                handleLoveMatch(
                        iGuild.getUserByID(Long.valueOf(cupidId)),
                        iGuild.getUserByID(Long.valueOf(psycheId)), event);
            }
        } catch (Exception e) {
            BotUtils.send(event.getChannel(), "Something went terribly wrong. Either you're retarded and can't type or Kat's retarded and should delete this entire repo");
        }
    }

    private void handleLoveMatch(IUser cupid, IUser psyche, MessageReceivedEvent event) {
        List<ReactionEmoji> hearts = new ArrayList<>();
        hearts.add(ReactionEmoji.of("❤"));
        hearts.add(ReactionEmoji.of("💔"));

        double loveScore = MathUtil.randomGaussian(50, 15);
        IGuild guild = event.getGuild();

        String cupidDisplayName = initNickname(cupid, event);
        String psycheDisplayName = initNickname(psyche, event);

        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("**Rejection Simulator**")
                .withColor(Visuals.getVibrantColor())
                .appendField("Cupid", cupidDisplayName, true)
                .appendField("Psyche", psycheDisplayName, true)//runs
                .appendField("% Divorce after 2 years", String.valueOf(loveScore), true)
                .appendField("Exciting Visual", renderScore(loveScore), true)
                .appendField("/r/advice", handleAdvice(cupid, psyche), false);

        IChannel iChannel = event.getChannel();
        EmbedObject embed = eb.build();

        IMessage embedMessage = iChannel.sendMessage(embed);
        BotUtils.reactAllEmojis(embedMessage, hearts);
    }

    private String handleAdvice(IUser cupid, IUser psyche) {
        if (cupid.getStringID().equals(psyche.getStringID()))
            return "You're gay";
        else
            return BotUtils.getRandomFromListString(advice);
    }

    private String initNickname(IUser user, MessageReceivedEvent event) {
        String cupidNick = user.getNicknameForGuild(event.getGuild());

        if (cupidNick == null)
            cupidNick = user.getName();

        return cupidNick;
    }

    //if max resolution is 10 squares, i have 1/20 precision
    //since loveScore *should be* (0,100)
    //round to nearest 5
    private String renderScore(double loveScore) {
        StringBuilder sb = new StringBuilder();
        int nearest10 = (int) loveScore/10;
        boolean hasHalf = ((int) (loveScore%10)) >= 5;

        for (int i = 0; i < nearest10; i++)
            sb.append("⬜");

        if(hasHalf)
            sb.append("◽");

        for (int i = 0; i < (hasHalf ? 10 - nearest10 - 1 : 1 - nearest10); i++)
            sb.append("⬛");


        return sb.toString();
    }

    @Override
    public String getDescription() {
        return "Ships 2 people. Might be slightly broken atm bother the dev if you want this fixed.";
    }

    //fill this with really helpful advice FOR SURE
    private void initAdvice() {
        advice.add("Take a shot every time someone picks Janna but instead of alcohol use bleach");
        //advice.add("The median lethal dose of Botox is 1ng/kg.");
    }
}
