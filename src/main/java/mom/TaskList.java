package mom;

import mom.command.Command;
import mom.exceptions.InvalidInputException;
import mom.task.*;

import java.time.LocalDateTime;

import java.util.ArrayList;

public class TaskList implements Parser {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void deleteTask(int rank) {
        this.tasks.remove(rank - 1);
    }

    public Task getTask(int rank) {
        return this.tasks.get(rank - 1);
    }

    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    public int getSize() {
        return this.tasks.size();
    }

    public Object[] markTask(String input, int offset) {
        for (Task task : this.tasks) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.mark();

                return new Object[]{true, task};
            }
        }
        return new Object[]{false};
    }

    public Object[] unmarkTask(String input, int offset) {
        for (Task task : this.tasks) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.unmark();

                return new Object[]{true, task};
            }
        }
        return new Object[]{false};
    }

    public void handleTaskCommand(Ui ui, TaskList taskList, Command command,
                                  String input, String[] inputList, int offset) {
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
                ui.displayDelete(rank, taskList.getTask(rank), taskList.getSize() - 1 );
                taskList.deleteTask(rank);
                break;
            }
            default: {
                try {
                    addTask(ui, taskList, command, input, offset);
                } catch (InvalidInputException e) {
                    Ui.errorDisplay(e.toString());
                }
                break;
            }
        }
    }

    public static void addTask(Ui ui, TaskList taskList, Command command,
                               String input, int offset) throws InvalidInputException {
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
