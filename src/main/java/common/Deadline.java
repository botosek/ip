package common;

public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public Deadline(String description, String status, String by) {
        super(description, status);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by + ")";
    }

    public String toSaveString() {
        return "D"
            + " | " + this.getStatus()
            + " | " + this.getDescription()
            + " | " + this.by;
    }
}
