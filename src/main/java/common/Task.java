package common;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public Task(String description, String status) {
        this.description = description;
        this.isDone = status.equals("1");
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]"); // mark done task with X
    }

    public String getStatus() { return (isDone? "1" : "0"); }

    public String getDescription() {
        return this.description;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String toString() {
        return this.getStatusIcon() + " " + this.getDescription();
    }

    public abstract String toSaveString();
}

