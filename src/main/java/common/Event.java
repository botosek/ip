package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private static final DateTimeFormatter DATETIME_PRINT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter DATETIME_SAVE= DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public Event(String description, String status, LocalDateTime from, LocalDateTime to) {
        super(description, status);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from:" + from.format(DATETIME_PRINT)
                + " to:" + to.format(DATETIME_PRINT) + ")";
    }

    public String toSaveString() {
        return "E"
                + " | " + this.getStatus()
                + " | " + this.getDescription()
                + " | " + this.from.format(DATETIME_SAVE)
                + "-" + this.to.format(DATETIME_SAVE);
    }
}

