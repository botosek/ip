package common;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public Todo(String description, String status) {
        super(description, status);
    }

    @Override
    public String toString() {
        return "[T]" + this.getStatusIcon() + " " + this.getDescription();
    }

    @Override
    public String toSaveString() {
        return "T"
            + " | " + this.getStatus()
            + " | " + this.getDescription();
    }
}
