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
     * Instantiate new task list if there is no existing task list data.
     */
    public TaskList() {
        this.tasks = new ArrayList<>(100);
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
     * @param offset Offset number of input string where task description starts.
     */
    public Object[] markTask(String input, int offset) {
        for (Task task : this.tasks) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.mark();

                return new Object[]{true, task};
            }
        }
        return new Object[]{false};
    }

    /**
     * Unmark task in list.
     *
     * @param input  Raw user input string.
     * @param offset Offset number of input string where task description starts.
     */
    public Object[] unmarkTask(String input, int offset) {
        for (Task task : this.tasks) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.unmark();

                return new Object[]{true, task};
            }
        }
        return new Object[]{false};
    }

    /**
     * Handle all commands of the user except "bye".
     *
     * @param ui        The chatbots ui object.
     * @param taskList  The task list of the user.
     * @param command   The parse command of the user input.
     * @param input     The raw input string of the user input.
     * @param inputList The parsed String[] array of the user input.
     * @param offset    The offset number where the description starts in the raw user input string.
     */
    public void handleTaskCommand(Ui ui, TaskList taskList, Command command, String input, String[] inputList,
                                  int offset) {
        switch (command) {
        case list: {
            ui.displayTaskList(taskList);
            break;
        }
        case mark: {
            Object[] result = taskList.markTask(input, offset);
            if ((boolean) result[0]) {
                ui.displayMark((Task) result[1]);
            }
            break;
        }
        case unmark: {
            Object[] result = taskList.unmarkTask(input, offset);
            if ((boolean) result[0]) {
                ui.displayUnmark((Task) result[1]);
            }
            break;
        }
        case delete: {
            int rank = Integer.parseInt(inputList[1]);
            ui.displayDelete(rank, taskList.getTask(rank), taskList.getSize() - 1);
            taskList.deleteTask(rank);
            break;
        }
        default: {
            try {
                handleTask(ui, taskList, command, input, offset);
            } catch (InvalidInputException e) {
                Ui.errorDisplay(e.toString());
            }
            break;
        }
        }
    }

    /**
     * Add task that user has input into task list.
     *
     * @param ui       The chatbots ui object.
     * @param taskList The task list of the user.
     * @param command  The parse command of the user input.
     * @param input    The raw input string of the user input.
     * @param offset   The offset number where the description starts in the raw user input string.
     * @throws InvalidInputException If the raw user input is not a valid command.
     */
    public static void handleTask(Ui ui, TaskList taskList, Command command, String input, int offset)
            throws InvalidInputException {
        try {
            switch (command) {
            case todo: {
                String description = Parser.parseEntryTodo(input, offset);
                taskList.addTask(new Todo(description));
                break;
            }
            case deadline: {
                Object[] result = Parser.parseEntryDeadline(input, offset);
                String description = (String) result[0];
                LocalDateTime byDateTime = (LocalDateTime) result[1];
                taskList.addTask(new Deadline(description, byDateTime));
                break;
            }
            case event: {
                Object[] results = Parser.parseEntryEvent(input, offset);
                String description = (String) results[0];
                LocalDateTime fromDateTime = (LocalDateTime) results[1];
                LocalDateTime toDateTime = (LocalDateTime) results[2];
                taskList.addTask(new Event(description, fromDateTime, toDateTime));
                break;
            }
            default: {
                throw new InvalidInputException("Please enter a valid command.");

            }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidInputException("Invalid Command: " + input);
        }

        ui.displayAdd(taskList.getTask(taskList.getSize()), taskList.getSize());
    }
}
