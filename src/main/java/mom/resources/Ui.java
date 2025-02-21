package mom.resources;

import java.util.ArrayList;

import mom.task.Task;

/**
 * Ui class to handle easy printing of messages to user.
 */
public class Ui {
    private static final String botName = "Mom";

    public Ui() {
    }

    /**
     * Display default intro message with chatbot name.
     */
    public static String displayIntro() {
        return "Hi, I'm " + botName + "!" + "\n" + "What can I do for you?";
    }

    /**
     * Displays default outro message.
     */
    public static String displayOutro() {
        return "Bye. See you soon!";
    }

    /**
     * Displays task list.
     *
     * @param taskList The list of tasks of type TaskList
     * @return         The string formatted list of tasks
     */
    public static String displayTaskList(TaskList taskList) {
        ArrayList<Task> tasks = taskList.getTaskList();
        StringBuilder output = new StringBuilder();
        output.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            int listIndex = i + 1;
            output.append(listIndex).append(". ").append(tasks.get(i).toString());
            if (listIndex < tasks.size()) {
                output.append("\n");
            }
        }
        return output.toString();

    }

    /**
     * Displays marked task UI.
     *
     * @param task  The task to be marked.
     * @return      String formatted task indicated as marked.
     */
    public static String displayMark(Task task) {
        return "Nice! I've marked this task as done." + "\n    " + task.toString();
    }

    /**
     * Displays unmarked task UI.
     *
     * @param task  The task to be unmarked.
     * @return      String formatted task indicated as unmarked.
     */
    public static String displayUnmark(Task task) {
        return "Okay, I've unmarked this task as incomplete." + "\n    " + task.toString();
    }

    /**
     * Displays deleted task UI.
     *
     * @param rank The rank position of the task in the tasklist.
     * @param task The task.
     * @param number The number of tasks left in the task list.
     * @return       The formatted string output of the deleted task.
     */
    public static String displayDelete(int rank, Task task, int number) {
        return "Understood, removing the task below:" + "\n    " + rank + "." + task.toString() + "\nNow you have "
                + number + " tasks in the list.";
    }

    /**
     * Displays add task UI.
     *
     * @param task The task to add.
     * @param number The final number of tasks in the list.
     * @return       The formatted string output of the task added.
     */
    public static String displayAdd(Task task, int number) {
        return "Got it. I've added this task:" + "\n    " + task.toString() + "\nNow you have " + number
                + " tasks in the list.";
    }

    /**
     * Displays tasks with matching keywords.
     *
     * @param taskList The arraylist of tasks.
     * @param keyword  The keyword string that will be used to identify the tasks.
     * @return        The formatted String output of the tasks with matching keywords.
     */
    public static String displayFind(TaskList taskList, String keyword) {
        ArrayList<Task> tasks = taskList.getTaskList();
        StringBuilder output = new StringBuilder();
        output.append("Here are your tasks with the matching keyword provided:\n");
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).toString().contains(keyword)) {
                int listIndex = i + 1;
                output.append(listIndex).append(". ").append(tasks.get(i).toString()).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Displays undo message
     */
    public static String displayUndo() {
        return "Undoing latest command. Use the command 'list' to view your current tasklist state!";
    }
}
