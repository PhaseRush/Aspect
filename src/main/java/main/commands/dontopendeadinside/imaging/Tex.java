package main.commands.dontopendeadinside.imaging;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Tex implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withImage("attachment://tex.png"),
                new ByteArrayInputStream(Visuals.renderTex(sanitize(args)).toByteArray()),
                "tex.png"
        );
    }

    private String sanitize(List<String> args) {
        StringBuilder all = new StringBuilder(args.get(0));

        for (int i = 1; i < args.size(); i++) {
            all.append(",").append(args.get(i)); // need comma
        }

        return all.toString().replace("\\\\", "\\\\\\\\"); // make single backslash -> double backslash
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !args.isEmpty();
    }

    @Override
    public String getDesc() {
        return "Renders TeX as an image";
    }

    @Override
    public String getSyntax() {
        return BotUtils.DEFAULT_BOT_PREFIX + "tex `expression`";
    }
}
