package common;

public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public Event(String description, String status, String from, String to) {
        super(description, status);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from
                + " to: " + to + ")";
    }

    public String toSaveString() {
        return "E"
                + " | " + this.getStatus()
                + " | " + this.getDescription()
                + " | " + this.from
                + "-" + this.to;
    }
}

