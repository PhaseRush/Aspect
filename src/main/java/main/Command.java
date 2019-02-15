package main;

import main.utility.metautil.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    void runCommand(MessageReceivedEvent event, List<String> args);

    default boolean canRun(MessageReceivedEvent event, List<String> args) {
        return true;
    }

    default Status mayRun(MessageReceivedEvent event, List<String> args) {
        return Status.OK;
    }

    default boolean requireSynchronous(){
        return false;
    }

    default String getDesc() {
        return BotUtils.GITHUB_URL;
    }

    default String getSyntax() {
        return BotUtils.GITHUB_URL;
    }

    default boolean correctable() {
        return true;
    }

    enum Status {
        OK("OK"),
        MISSING_PERMS("Missing permissions"),
        WRONG_ARGS("Command parameters are wrong"),
        STATE_PRECONDITION_ERROR("There are unsatisfied preconditions"),
        RUN_STATE_ERROR("Error encountered while running (not user problem)"),
        DEFAULT("Unknown error");

        private final String desc;
        Status(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}