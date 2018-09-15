package main.commands.utilitycommands;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.util.List;

public class Dev implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IChannel iChannel = event.getChannel();
        BotUtils.sendMessage(iChannel, "Here's <@264213620026638336>'s info card:");
        RequestBuffer.request(() -> iChannel.sendMessage(createKatEmbed().build()));
    }

    @Override
    public boolean requiresElevation() {
        return false;
    }

    @Override
    public String getDescription() {
        return "me.";
    }

    private EmbedBuilder createKatEmbed() {
        EmbedBuilder temp = new EmbedBuilder();

        //copied from the github thing
        temp.withTitle("Hi, I'm Kat!")
                .withUrl("https://discordapp.com")
                .withDesc("18 earth year old ambitious lump of organic matter that goes around\n trying to not be useless :)")
                .withColor(new Color(13458387))
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("Look Onwards")
                .withFooterIcon("https://pbs.twimg.com/profile_images/664158849536921600/KZAnmaIr_400x400.jpg")
                .withThumbnail("https://vignette.wikia.nocookie.net/leagueoflegends/images/1/12/PROJECT_Katarina_profileicon.png/revision/latest?cb=20170505005706")
                .withImage("https://i.imgur.com/8kGgNGX.jpg")
                .withAuthorName("KVTVRINV")
                .withAuthorIcon("https://i.imgur.com/echE5EF.jpg")
                .withAuthorUrl("https://google.com")
                .appendField(":cat:", "placeholder 1", false)
                .appendField("comments, suggestions, critique", "contact@email.com", false)
                .appendField("ph", "ph", false)
                .appendField("Schrodinger's", "dead", true)
                .appendField("Cat", "alive", true)
                .build();

        return temp;
    }
}
