package main.utility.state_json;

import java.time.Instant;
import java.time.LocalDateTime;

public class Reminder {
    Instant instant;
    LocalDateTime time;
    long epochTime;

    String userStringId;
    long userLongId;

    String message;
}
