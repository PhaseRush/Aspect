package main.utility.chat;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class ChatRestriction {
    IChannel restrictedChannel;
    boolean isMuted;
    boolean hasWarnedChat = false;
    IUser creator;

    public ChatRestriction(IChannel channel, boolean toMute, IUser creator) {
        isMuted = toMute;
        restrictedChannel = channel;
        this.creator = creator;
    }

    public void setNotMuted() {
        isMuted = false;
    }
    public void setIsMuted() {
        isMuted = true;
    }

    public boolean getHasWarnedChat() {
        return hasWarnedChat;
    }

    public void setHasWarnedChatTrue() {
        hasWarnedChat = true;
    }

    public void setHasWarnedChatFalse() {
        hasWarnedChat = false;
    }

    public IUser getCreator() {
        return creator;
    }

    public IChannel getRestrictedChannel() {
        return restrictedChannel;
    }

    public boolean isMuted() {
        return isMuted;
    }

    @Override
    public String toString() {
        return restrictedChannel.getName() + " is now " + ((isMuted) ? "" : "not") + " muted";
    }
}
