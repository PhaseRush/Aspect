package main.utility.chat;

import sx.blah.discord.handle.obj.IChannel;

import java.util.ArrayList;
import java.util.List;

public class ChatRestrictionManager {
    List<ChatRestriction> chatRestrictionList = new ArrayList<>();

    //default constructor

    public void addChatRestriction(ChatRestriction cr) {
        chatRestrictionList.add(cr);
    }

    public void removeChatRestriction(ChatRestriction cr) {
        chatRestrictionList.remove(cr);
    }

    public List<ChatRestriction> getChatRestrictionList() {
        return chatRestrictionList;
    }

    public boolean contains(IChannel c) {
        return chatRestrictionList.contains(c);
    }
}
