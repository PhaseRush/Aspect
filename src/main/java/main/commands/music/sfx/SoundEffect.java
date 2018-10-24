package main.commands.music.sfx;

import main.Command;
import main.utility.BotUtils;
import main.utility.music.MasterManager;
import main.utility.music.SfxManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;

public class SoundEffect implements Command {

    private Map<String, String> sfxMap = SfxManager.sfxMap;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (args.size()>0 && args.get(0).equalsIgnoreCase("list")) {
            BotUtils.sendMessage(event.getChannel(), "Did you mean `$listsfx`");
            return;
        }

        if (event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel() == null) {
            BotUtils.joinVC(event);
        }

        if (args.get(0).matches("re+")) {
            MasterManager.loadAndPlay(event.getChannel(), sfxMap.get("ree"), event, true, "");
            return;
        }

        if (sfxMap.keySet().contains(args.get(0)))
            MasterManager.loadAndPlay(event.getChannel(), sfxMap.get(args.get(0)), event, true, "");
        else
            BotUtils.sendMessage(event.getChannel(), "This sound effect does not exist. Check your spelling or go bother kat to add it");
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public String getDescription() {
        return "plays a specified sound effect. use $listsfx to get list of available sounds";
    }
}
