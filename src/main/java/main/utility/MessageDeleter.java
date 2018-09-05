package main.utility;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageHistory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MessageDeleter {
    //todo - make some stuff static someday

    private String[] params;
    private String intendedAuthorID;
    private int minutesToDelete;

    boolean deletionRunning = false;
    boolean authorCallingSelf = false;

    private IUser msgDeleter;
    private IChannel channel;

    private List<IMessage> messagesToBulkDelete = new ArrayList<>();

    public MessageDeleter(String input, IUser author, IChannel channel) {
        params = input.split("\\s*,\\s*");

        handleIntendedAuthorID(author);

        minutesToDelete = Integer.parseInt(params[1]);
        msgDeleter = author;
        this.channel = channel;
    }

    public MessageDeleter(String id, int minutesToDelete, IUser author, IChannel iChannel) {
        if (id.replaceAll(",","").equals("me"))
            intendedAuthorID = author.getStringID();
        else
            intendedAuthorID = id;

        this.minutesToDelete = minutesToDelete;
        msgDeleter = author;
        this.channel = iChannel;
    }

    public MessageDeleter(int minutesToDelete, IChannel channel, IUser author) {
        this.minutesToDelete = minutesToDelete;
        this.channel = channel;
        msgDeleter = author;
    }

    private void handleIntendedAuthorID(IUser author) {
        String potentialAuthorID = params[0].substring(1); //could be "me"  = call to self
        if (potentialAuthorID.equals("me"))
            intendedAuthorID = author.getStringID();
        else
            intendedAuthorID = potentialAuthorID;
    }


    public void runDelete(IGuild iGuild) {
        IMessage deletingMsg = channel.sendMessage("```Deleting...```");
        Instant instantToDeleteFrom = Instant.now().minus(minutesToDelete, ChronoUnit.MINUTES);

        MessageHistory msgHist = channel.getMessageHistoryTo(instantToDeleteFrom);

        //find all msg written by the intended author
        for (IMessage msg :msgHist) {
            if (msg.getAuthor().getStringID().equals(intendedAuthorID)) { //figure out how to do a rolecheck @todo
                messagesToBulkDelete.add(msg);
            }
        }

        deletingMsg.delete();

        channel.bulkDelete(messagesToBulkDelete);
        BotUtils.sendMessage(channel, "<@" + msgDeleter.getStringID() + "> deleted " + messagesToBulkDelete.size() + ((messagesToBulkDelete.size() == 1) ? " message" : " messages") + " sent by " + ((intendedAuthorID.equals("417925383762214912")) ? "me :( " : getDeletee().getNicknameForGuild(iGuild)) + " in the last " + ((minutesToDelete == 1) ? "minute." : minutesToDelete + " minutes."));
    }

    public void runBulkDelete() {
        IMessage deletingMsg = channel.sendMessage("```Bulk Deleting...```");
        Instant instantToDeleteFrom = Instant.now().minus(minutesToDelete, ChronoUnit.MINUTES);

        messagesToBulkDelete.addAll(channel.getMessageHistoryTo(instantToDeleteFrom));
        messagesToBulkDelete.add(deletingMsg);
        channel.bulkDelete(messagesToBulkDelete);
        BotUtils.sendMessage(channel, "<@" + msgDeleter.getStringID() + "> deleted " + messagesToBulkDelete.size() + " messages in " + channel.getName() + " in the last " + (minutesToDelete == 1 ? "minute" : minutesToDelete + " minutes"));
    }

    @Override
    public String toString() {
        return "<@" + msgDeleter.getStringID() + "> is deleting " + intendedAuthorID + "'s messages from " + minutesToDelete + " minutes ago.";
    }

    //as in interviewer/interviewee :)
    private IUser getDeletee() {
        IGuild iGuild = channel.getGuild();
        return iGuild.getUserByID(Long.parseLong(intendedAuthorID));
    }

}