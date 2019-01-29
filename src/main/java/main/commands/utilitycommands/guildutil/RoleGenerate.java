package main.commands.utilitycommands.guildutil;

import main.Command;
import main.utility.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.RoleBuilder;

import java.awt.*;
import java.util.List;

public class RoleGenerate implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        IGuild guild = event.getGuild();
        String desiredName = args.get(0);

        try {

            if (guild.getRolesByName(desiredName).size() >= 1) {
                BotUtils.send(event.getChannel(), "One or more roles with this name already exists.");
                return;
            }

            int r = Integer.valueOf(args.get(1));
            int g = Integer.valueOf(args.get(2));
            int b = Integer.valueOf(args.get(3));
            Color color = new Color(r, g, b);

            IRole newRole = new RoleBuilder(event.getGuild())
                    .setHoist(true)
                    .setMentionable(true)
                    .withColor(color)
                    .withName(args.get(0))
                    .withPermissions(event.getAuthor().getPermissionsForGuild(guild))
                    .build();

            event.getAuthor().addRole(newRole);
        } catch (NullPointerException | IllegalArgumentException e) {
            BotUtils.send(event.getChannel(), "4 parameters needed: role name, r[0-255], g[0-255], b[0-255]\n" +
                    "Example: $makerole Canadian, 255, 0, 0");
        }

    }

    @Override
    public String getDesc() {
        return "Generates a role with user's permission hierarchy ```$makerole [name], r, g, b";
    }
}
