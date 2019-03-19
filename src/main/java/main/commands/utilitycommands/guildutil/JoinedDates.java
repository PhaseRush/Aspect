package main.commands.utilitycommands.guildutil;

import javafx.util.Pair;
import main.Command;
import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class JoinedDates implements Command {
    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withTitle("User stats")
                        .withDesc(generateDesc(event))
        );
    }

    private String generateDesc(MessageReceivedEvent event) {
        return creationDates(event) + "\n---\n" + joinDates(event);
    }

    private String creationDates(MessageReceivedEvent event) {
        return
                new StringBuilder("\nAverage account age: ")
                        .append(
                                Duration.of(
                                        event.getGuild().getUsers().stream()
                                                .mapToLong(u -> u.getCreationDate().toEpochMilli())
                                                .sum()  // total time for all users
                                                / //divided by
                                                event.getGuild().getUsers().size(), // number of users
                                        ChronoUnit.MILLIS
                                ).toDays()
                        )
                        .append("\nOldest member: ")
                        .append(
                                BotUtils.getNickOrDefault(
                                        event.getGuild().getUsers().stream()
                                                .map(u -> new Pair<>(u.getLongID(), u.getCreationDate().toEpochMilli()))
                                                .min((o1, o2) -> {
                                                    if (o1.getValue().equals(o2.getValue())) return 0;
                                                    if (o1.getValue() < o2.getValue()) return -1;
                                                    else return 1;
                                                })
                                                .map(pair -> event.getClient().getUserByID(pair.getKey()))
                                                .orElseGet(() -> event.getClient().getOurUser()),
                                        event.getGuild()))
                        .append("\nYoungest user: ")
                        .append(
                                BotUtils.getNickOrDefault(
                                        event.getGuild().getUsers().stream()
                                                .map(u -> new Pair<>(u.getLongID(), u.getCreationDate().toEpochMilli()))
                                                .max((o1, o2) -> {
                                                    if (o1.getValue().equals(o2.getValue())) return 0;
                                                    if (o1.getValue() < o2.getValue()) return -1;
                                                    else return 1;
                                                })
                                                .map(pair -> event.getClient().getUserByID(pair.getKey()))
                                                .orElseGet(() -> event.getClient().getOurUser()),
                                        event.getGuild()))
                        .toString();
    }

    private String joinDates(MessageReceivedEvent event) {
        return
                new StringBuilder("\nAverage join date: ")
                        .append(
                                Instant.ofEpochMilli(
                                        event.getGuild().getUsers().stream()
                                                .mapToLong(u -> event.getGuild().getJoinTimeForUser(u).toEpochMilli())
                                                .sum()  // total time for all users
                                                / //divided by
                                                event.getGuild().getUsers().size() // number of users
                                ).toString()
                        )
                        .append("\nOldest member still present: ")
                        .append(
                                BotUtils.getNickOrDefault(
                                        event.getGuild().getUsers().stream()
                                                .map(u -> new Pair<>(u.getLongID(), event.getGuild().getJoinTimeForUser(u).toEpochMilli()))
                                                .min((o1, o2) -> {
                                                    if (o1.getValue().equals(o2.getValue())) return 0;
                                                    if (o1.getValue() < o2.getValue()) return -1;
                                                    else return 1;
                                                })
                                                .map(pair -> event.getClient().getUserByID(pair.getKey()))
                                                .orElseGet(() -> event.getClient().getOurUser()),
                                        event.getGuild()))
                        .append("\nNewest recruit: ")
                        .append(
                                BotUtils.getNickOrDefault(
                                        event.getGuild().getUsers().stream()
                                                .map(u -> new Pair<>(u.getLongID(), event.getGuild().getJoinTimeForUser(u).toEpochMilli()))
                                                .max((o1, o2) -> {
                                                    if (o1.getValue().equals(o2.getValue())) return 0;
                                                    if (o1.getValue() < o2.getValue()) return -1;
                                                    else return 1;
                                                })
                                                .map(pair -> event.getClient().getUserByID(pair.getKey()))
                                                .orElseGet(() -> event.getClient().getOurUser()),
                                        event.getGuild()))
                        .toString();
    }
}
