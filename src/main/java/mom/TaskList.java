
package mom;

import java.time.LocalDateTime;
import java.util.ArrayList;

import mom.command.Command;
import mom.exceptions.InvalidInputException;
import mom.task.Deadline;
import mom.task.Event;
import mom.task.Task;
import mom.task.Todo;

/**
 * TaskList class that contains the list of tasks
 */
public class TaskList implements Parser {
    private final ArrayList<Task> tasks;

    /**
     * Instantiate task list with old task list data loaded in.
     *
     * @param tasks ArrayList of tasks to be loaded in.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Create copy.
     *
     * @param other ArrayList of tasks to be loaded in.
     */
    public TaskList(TaskList other) {
        this.tasks = new ArrayList<>();
        for (Task task : other.tasks) {
            this.tasks.add(task.copy()); // Ensure Task has a copy constructor
        }
    }

    /**
     * Instantiate new task list if there is no existing task list data.
     */
    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    /**
     * Add task that user has input into task list.
     *
     * @param taskList The task list of the user.
     * @param command  The parse command of the user input.
     * @param input    The raw input string of the user input.
     * @param offset   The offset number where the description starts in the raw user input string.
     * @throws InvalidInputException If the raw user input is not a valid command.
     */
    public static String handleTask(TaskList taskList, Command command, String input, int offset)
            throws InvalidInputException {
        try {
            switch (command) {
            case todo: {
                String description = Parser.parseEntryTodo(input, offset);
                taskList.addTask(new Todo(description));
                StateList.addState(taskList);
                break;
            }
            case deadline: {
                Object[] result = Parser.parseEntryDeadline(input, offset);
                String description = (String) result[0];
                LocalDateTime byDateTime = (LocalDateTime) result[1];
                taskList.addTask(new Deadline(description, byDateTime));
                StateList.addState(taskList);
                break;
            }
            case event: {
                Object[] results = Parser.parseEntryEvent(input, offset);
                String description = (String) results[0];
                LocalDateTime fromDateTime = (LocalDateTime) results[1];
                LocalDateTime toDateTime = (LocalDateTime) results[2];
                taskList.addTask(new Event(description, fromDateTime, toDateTime));
                StateList.addState(taskList);
                break;
            }
            default: {
                throw new InvalidInputException("Please enter a valid command.");

            }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidInputException("Invalid Command: " + input);
        }
        int size = taskList.getSize();
        Task task = taskList.getTask(size);
        assert size > -1;
        assert task != null;

        return Ui.displayAdd(task, size);
    }

    /**
     * Add task into task list.
     *
     * @param task Task to be added into task list.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Delete task in task list.
     *
     * @param rank Task rank to be deleted.
     */
    public void deleteTask(int rank) {
        this.tasks.remove(rank - 1);
    }

    /**
     * Get specific task from task list.
     *
     * @param rank Rank of task to be returned.
     * @return Specified task to be returned.
     */
    public Task getTask(int rank) {
        return this.tasks.get(rank - 1);
    }

    /**
     * Get task list as an ArrayList.
     *
     * @return Task list as an ArrayList.
     */
    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    /**
     * Get size of task list.
     *
     * @return Size of task list.
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Mark task in list.
     *
     * @param input  Raw user input string.
     * @param rank   Rank of task to be marked.
     */
    public Object[] markTask(String input, int rank) {
        if (rank <= this.tasks.size()) {
            this.getTask(rank).mark();
            Task task = this.getTask(rank);
            return new Object[]{true, task};
        }
        return new Object[]{false};
    }

    /**
     * Unmark task in list.
     *
     * @param input  Raw user input string.
     * @param rank   Rank of task to be unmarked.
     */
    public Object[] unmarkTask(String input, int rank) {
        if (rank <= this.tasks.size()) {
            this.getTask(rank).unmark();
            Task task = this.getTask(rank);
            return new Object[]{true, task};
        }
        return new Object[]{false};
    }

    /**
     * Handle all commands of the user when the bot is in GUI format.
     *
     * @param taskList  The task list of the user.
     * @param command   The parse command of the user input.
     * @param input     The raw input string of the user input.
     * @param inputList The parsed String[] array of the user input.
     * @param offset    The offset number where the description starts in the raw user input string.
     * @return The user response.
     */
    public String handleTaskCommandGui(TaskList taskList, Command command, String input, String[] inputList,
                                       int offset) {
        StateList.printStates();
        try {
            switch (command) {
            case list: {
                taskList = StateList.getCurrentState();
                return Ui.displayTaskList(taskList);
            }
            case find: {
                StateList.addState(taskList);
                return Ui.displayFind(taskList, inputList[1]);
            }
            case mark: {
                Object[] result = taskList.markTask(input, Integer.parseInt(inputList[1]));
                StateList.addState(taskList);
                if ((boolean) result[0]) {
                    return Ui.displayMark((Task) result[1]);
                }
                break;
            }
            case unmark: {
                Object[] result = taskList.unmarkTask(input, Integer.parseInt(inputList[1]));
                StateList.addState(taskList);
                if ((boolean) result[0]) {
                    return Ui.displayUnmark((Task) result[1]);
                }
                break;
            }
            case delete: {
                int rank = Integer.parseInt(inputList[1]);
                String reply = Ui.displayDelete(rank, taskList.getTask(rank), taskList.getSize() - 1);
                taskList.deleteTask(rank);
                StateList.addState(taskList);
                return reply;
            }
            default: {
                return handleTask(taskList, command, input, offset);
            }
            }
            throw new InvalidInputException("Invalid Command: " + input);
        } catch (InvalidInputException e) {
            return e.toString();
        }

    }
}
