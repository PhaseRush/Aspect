package main.utility;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

// xannit is a meanie and made me do this
public class Global {
    private IChannel WF_BOTTOM_TEXT;


    public void setWF_BOTTOM_TEXT(IDiscordClient client, long id) {
        if (WF_BOTTOM_TEXT == null) {
            WF_BOTTOM_TEXT = client.getChannelByID(id);
        }
    }

    public IChannel WF_BOTTOM_TEXT() {
        return WF_BOTTOM_TEXT;
    }
}
