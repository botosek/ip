package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private static final DateTimeFormatter DATETIME_PRINT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter DATETIME_SAVE= DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public Deadline(String description, String status, LocalDateTime by) {
        super(description, status);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by:" + by.format(DATETIME_PRINT) + ")";
    }

    public String toSaveString() {
        return "D"
            + " | " + this.getStatus()
            + " | " + this.getDescription()
            + " | " + this.by.format(DATETIME_SAVE);
    }
}
