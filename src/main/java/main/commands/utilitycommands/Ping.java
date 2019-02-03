package main.commands.utilitycommands;

import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public class Ping implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long gatewayPing = System.currentTimeMillis() - event.getMessage().getTimestamp().toEpochMilli();

        long startSend = System.currentTimeMillis();
        IMessage msg = BotUtils.sendGet(event.getChannel(), "Meow!");
        long restPing = msg.getTimestamp().toEpochMilli() - startSend;

        msg.edit("My ping is " + (restPing + gatewayPing) + " ms." + "\n```js\n" +
                "Gateway: " + gatewayPing +
                "ms\nRest: " + restPing + "ms```");
    }

    @Override
    public String getDesc() {
        return "a meme";
    }
}
