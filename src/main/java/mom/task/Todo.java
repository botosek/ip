package mom.task;

/**
 * Todo class where each todo object contains its description and status.
 */
public class Todo extends Task {

    /**
     * Create new Todo object.
     *
     * @param description Todo task description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Create new Todo object.
     *
     * @param description Todo task description.
     * @param status      Completion status of task.
     */
    public Todo(String description, String status) {
        super(description, status);
    }

    /**
     * Generate string of task to be displayed to user.
     *
     * @return  Task string to be displayed to user.
     */
    @Override
    public String toString() {
        return "[T]" + this.getStatusIcon() + " " + this.getDescription();
    }

    /**
     * Generate string of task to be saved in hard disk.
     *
     * @return  Task string to be saved to hard disk.
     */
    @Override
    public String toSaveString() {
        return "T"
            + " | " + this.getStatus()
            + " | " + this.getDescription();
    }
}
