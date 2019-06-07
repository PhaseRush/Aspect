package main.commands.utilitycommands;

import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;

public class FileToHaste implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        try {
            BotUtils.send(event.getChannel(),
                    new EmbedBuilder()
                            .withTitle("File -> Haste")
                            .withColor(Visuals.getRandVibrantColour())
                            .withDesc(
                                    BotUtils.makeHasteGetUrl(
                                            BotUtils.getStringFromUrl(event.getMessage().getAttachments().get(0).getUrl())
                                    )
                            ));
        } catch (IOException e) {
            BotUtils.send(event.getChannel(), "There has been an error. Check that you have attached a text file, and that your text file is reasonably sized (<40k chars)");
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !event.getMessage().getAttachments().isEmpty();
    }

    @Override
    public String getDesc() {
        return "Transfers text in uploaded file to haste";
    }

    @Override
    public String getSyntax() {
        return "`$haste` with attachment";
    }
}
