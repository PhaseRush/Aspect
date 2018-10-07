package main.commands.warframe;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.warframe.wfcd.relics.RelicWrapper;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class WarframeRelicInfo implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String parsedUserInput = args.get(0);

        if (!checkValid(parsedUserInput)) {
            BotUtils.sendMessage(event.getChannel(), "Invalid relic");
            return;
        }
        String url = "https://raw.githubusercontent.com/WFCD/warframe-drop-data/gh-pages/data/relics.json";
        String json = BotUtils.getStringFromUrl(url);

        RelicWrapper relics = new Gson().fromJson(json, RelicWrapper.class);
        relics.getRelics().get(0).get_id();

    }

    private boolean checkValid(String parsedUserInput) {
        return true;
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
