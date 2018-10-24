package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Poll implements Command {
    private List<ReactionEmoji> regionalIndicators = BotUtils.initializeRegionals();

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            event.getMessage().delete();
        } catch (MissingPermissionsException e) {

        }
        String authorNickname = event.getAuthor().getNicknameForGuild(event.getGuild());

        List<String> optionsList = args.subList(1, args.size());
        EmbedBuilder eb = new EmbedBuilder()
                .withAuthorName((authorNickname == null ? event.getAuthor().getName() : authorNickname))
                .withAuthorIcon(event.getAuthor().getAvatarURL())
                .withDesc("\t**" + args.get(0) + "**" + "\n" + buildPollOptions(optionsList))
                .withColor(Visuals.getVibrantColor());

        IMessage embedMessage = RequestBuffer.request(() -> event.getChannel().sendMessage(eb.build())).get();
        BotUtils.reactAllEmojis(embedMessage, regionalIndicators.subList(0, optionsList.size()));
    }

    private String buildPollOptions(List<String> questionList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < questionList.size(); i++) {
            sb.append(":regional_indicator_" + getCharFromInt(i) + ":\t" + questionList.get(i) + "\n");
        }
        return sb.toString();
    }

    private String getCharFromInt(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'a')) : null; //super fucking janky
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Constructs a Poll with an arbitrary number of options. ```$poll [question], [option1], [option2],...";
    }
}
