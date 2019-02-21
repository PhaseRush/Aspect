package main.commands.kaitlyn;

import com.google.gson.Gson;
import main.Command;
import main.utility.ReadWrite;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * idea that has been floating around to screw with nicknames in Kaitlyn's server
 *
 * first save all current nicks
 * then change everyones nickname
 * finally, change them back when everyone panics
 */
public class ReNickName implements Command {
    private Gson gson = new Gson();
    private String winBasedir = "C:\\Users\\leozh\\Desktop\\nick.txt";
    private String basedir; //some linux shit here

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IGuild iGuild = event.getGuild();
        Map<String, String> nickMap = new LinkedHashMap<>();
        //save all current nicknames

        //ty @xaanit
        //List<String> currentNicks = iGuild.getUsers().stream().initDataMap( u -> u.getNicknameForGuild(iGuild)).collect(Collectors.toList());

        for (IUser u : iGuild.getUsers())
            nickMap.put(u.getStringID(), u.getNicknameForGuild(iGuild));

        String serialized = gson.toJson(nickMap);
        ReadWrite.writeToFile(winBasedir, serialized, false, false);


    }

    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return false;
    }

    @Override
    public String getDesc() {
        return null;
    }
}