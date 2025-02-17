package mom.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deadline class where each deadline object contains its description, status, and its deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATETIME_PRINT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter DATETIME_SAVE = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected LocalDateTime by;

    /**
     * Create new Deadline object.
     *
     * @param description Deadline task description.
     * @param by          Date and time of deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public Deadline(Deadline other) {
        super(other);
        this.by = other.getBy();
    }

    @Override
    public Task copy() {
        return new Deadline(this); // Calls the Deadline copy constructor
    }

    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Create new Deadline object.
     *
     * @param description Deadline task description.
     * @param status      Completion status of task.
     * @param by          Date and time of deadline.
     */
    public Deadline(String description, String status, LocalDateTime by) {
        super(description, status);
        this.by = by;
    }

    /**
     * Generate string of task to be displayed to user.
     *
     * @return  Task string to be displayed to user.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by:" + by.format(DATETIME_PRINT) + ")";
    }

    /**
     * Generate string of task to be saved in hard disk.
     *
     * @return  Task string to be saved to hard disk.
     */
    public String toSaveString() {
        return "D" + " | " + this.getStatus() + " | " + this.getDescription() + " | " + this.by.format(DATETIME_SAVE);
    }
}
