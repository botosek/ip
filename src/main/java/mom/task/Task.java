package mom.task;

/**
 * General Task class
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Create new Task object that has not been completed.
     *
     * @param description Deadline task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Create new Task object with confirmed completion status.
     *
     * @param description Deadline task description.
     * @param status      Completion status of task.
     */
    public Task(String description, String status) {
        this.description = description;
        this.isDone = status.equals("1");
    }

    public String getStatusIcon() {
        return isDone ? "[X]"
                      : "[ ]";
    }

    public String getStatus() {
        return isDone ? "1"
                      : "0";
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Mark task as complete.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Unmark task as incomplete.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Generate string of task to be displayed to user.
     *
     * @return  Task string to be displayed to user.
     */
    public String toString() {
        return this.getStatusIcon() + " " + this.getDescription();
    }

    /**
     * Generate string of task to be saved in hard disk.
     *
     * @return  Task string to be saved to hard disk.
     */
    public String toSaveString() {
        return this.getStatusIcon() + " " + this.getDescription();
    }

    public boolean isEmpty() {
        return this.getDescription().isEmpty();
    }

}

