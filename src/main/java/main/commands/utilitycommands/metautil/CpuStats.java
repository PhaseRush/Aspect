package main.commands.utilitycommands.metautil;

import main.Aspect;
import main.Command;
import main.CommandManager;
import main.utility.metautil.BotUtils;
import org.jetbrains.annotations.NotNull;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CpuStats implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        // check special request
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("syncmap")){
            BotUtils.send(event.getChannel(), genSyncMap().orElse("No servers have synchronous maps"));
            return;
        }

        BotUtils.send(event.getChannel(), genMessage());
    }

    @NotNull
    public static String genMessage() {
        OperatingSystemMXBean osBean = Aspect.OS_BEAN;
        Runtime r = Runtime.getRuntime();
        double maxM = r.maxMemory() / 1E6;

        return "```                                                     \n" +
                "Instance ID: \t\t" + Aspect.INSTANCE_ID.toString() +
                "\nSystem Load:  \t\t" + osBean.getSystemLoadAverage()*100 + " %" +
                "\nRam (MB): \t\t\t" + (int) (maxM - r.freeMemory() / 1E6) + " / " + (int) maxM +
                "\nArch: \t\t\t\t" + osBean.getArch() +
                "\nName: \t\t\t\t" + osBean.getName() +
                "\nVersion:  \t\t\t" + osBean.getVersion() +
                "\nAvail Cores:  \t\t" + osBean.getAvailableProcessors()
                + "```";
    }

    static Optional<String> genSyncMap() {
        if (CommandManager.syncExecuteMap.entrySet().isEmpty()) {
            return Optional.empty();
        }

        StringBuilder sb = new StringBuilder("The following servers have synchronous maps:```js\n                                                     ");

        for (Map.Entry e : CommandManager.syncExecuteMap.entrySet()) {
            IGuild guild = Aspect.client.getGuildByID(Long.valueOf((String) e.getKey()));
            sb.append(guild.getName().replaceAll("'", "")).append(" @ ")
                    .append(guild.getStringID())
                    .append("\n");
        }

        sb.append("```");

        return Optional.of(sb.toString());
    }

    @Override
    public String getDesc() {
        return "How hard I'm working.";
    }
}