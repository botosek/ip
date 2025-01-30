package mom;

import mom.task.*;

import java.util.ArrayList;

public class Ui {
    private static final String DIVIDER = "--------------------------------------------------";
    private static final String ERROR_DIVIDER = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
    private final String botName;

    public Ui(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public static void display(String message) {
        System.out.println(message);
    }

    public static void errorDisplay(String message) {
        display("\n");
        display(ERROR_DIVIDER);
        display(message);
        display(ERROR_DIVIDER);
        display("\n");
    }

    public void displayIntro() {
        display(DIVIDER);
        display("Hi, I'm " + this.getBotName() + "!");
        display("What can I do for you?");
        display(DIVIDER + "\n");
    }

    public void displayOutro() {
        display(DIVIDER);
        display("Bye. See you soon!");
        display(DIVIDER + "\n");
    }

    public void displayTaskList(TaskList taskList) {
        ArrayList<Task> tasks = taskList.getTaskList();
        display(DIVIDER);
        StringBuilder output = new StringBuilder();
        output.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            int listIndex = i + 1;
            output.append(listIndex).append(". ").append(tasks.get(i).toString());
            if (listIndex < tasks.size()) {
                output.append("\n");
            }
        }
        display(output.toString());
        display(DIVIDER + "\n");

    }

    public void displayMark(Task task) {
        display(DIVIDER);
        display("Nice! I've marked this task as done.");
        display("    " + task.toString());
        display(DIVIDER + "\n");
    }

    public void displayUnmark(Task task) {
        display(DIVIDER);
        display("Okay, I've unmarked this task as incomplete.");
        display("    " + task.toString());
        display(DIVIDER + "\n");
    }

    public void displayDelete(int rank, Task task, int number) {
        display(DIVIDER);
        display("Understood, removing the task below:");
        display("    " + rank + "." + task.toString());
        display("Now you have " + number + " tasks in the list.");
        display(DIVIDER + "\n");
    }

    public void displayAdd(Task task, int number) {
        display(DIVIDER);
        display("Got it. I've added this task:");
        display("    " + task.toString());
        display("Now you have " + number + " tasks in the list.");
        display(DIVIDER + "\n");
    }

    public void displayFind(TaskList taskList, String keyword) {
        ArrayList<Task> tasks = taskList.getTaskList();
        display(DIVIDER);
        StringBuilder output = new StringBuilder();
        output.append("Here are your tasks with the matching keyword provided:\n");
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).toString().contains(keyword)) {
                int listIndex = i + 1;
                output.append(listIndex).append(". ").append(tasks.get(i).toString()).append("\n");
            }
        }
        display(output.toString());
        display(DIVIDER + "\n");

    }

}
