package main.utility.state_json.json_container;

import java.time.Instant;
import java.time.LocalDateTime;

public class Reminder {
    private Instant instant;
    private LocalDateTime time;
    private long epochTime;

    //for redundancy
    private String userStringId;
    private long userLongId;

    private String message;

    public Instant getInstant() {
        return instant;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public String getUserStringId() {
        return userStringId;
    }

    public long getUserLongId() {
        return userLongId;
    }

    public String getMessage() {
        return message;
    }
}
