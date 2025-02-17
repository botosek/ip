package mom.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Event class where each event object contains its description, status, and its start and end time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATETIME_PRINT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter DATETIME_SAVE = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Create new Event object.
     *
     * @param description Deadline task description.
     * @param from        Start date and time.
     * @param to          End date and time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public Event(Event other) {
        super(other);
        this.from = other.getFrom();
        this.to = other.getTo();
    }

    @Override
    public Task copy() {
        return new Event(this); // Calls the Event copy constructor
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Create new Event object.
     *
     * @param description Deadline task description.
     * @param status      Completion status of task.
     * @param from        Start date and time.
     * @param to          End date and time.
     */
    public Event(String description, String status, LocalDateTime from, LocalDateTime to) {
        super(description, status);
        this.from = from;
        this.to = to;
    }

    /**
     * Generate string of task to be displayed to user.
     *
     * @return  Task string to be displayed to user.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from:" + from.format(DATETIME_PRINT) + " to:" + to.format(DATETIME_PRINT)
                + ")";
    }

    /**
     * Generate string of task to be saved in hard disk.
     *
     * @return  Task string to be saved to hard disk.
     */
    public String toSaveString() {
        return "E" + " | " + this.getStatus() + " | " + this.getDescription() + " | "
                + this.from.format(DATETIME_SAVE) + "-" + this.to.format(DATETIME_SAVE);
    }
}

