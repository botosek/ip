import java.util.Scanner;

public class Mom {
    static String chatbotName = "Mom";
    static String divider = "--------------------------------------------------";
    static Task[] taskList = new Task[100];
    static int taskIndex = 0;

    public static void main(String[] args) {

        System.out.println("Hi, I'm " + chatbotName);
        System.out.println("What can I do for you?");
        System.out.println(divider + "\n");

        Scanner scan = new Scanner(System.in);
        String input = "";

        while(true) {
            input = scan.nextLine();
            String[] inputList = input.split(" ");
            int offset = inputList[0].length() + 1;

            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                Mom.listTasks();

            } else if (inputList[0].equals("mark")) {
                Mom.markTask(input, offset);

            } else if (inputList[0].equals("unmark")) {
                Mom.unmarkTask(input, offset);

            } else {
                addTask(input, inputList);

            }
        }
        scan.close();

        System.out.println("Bye. See you soon!");
    }

    public static void listTasks() {
        System.out.println(divider);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskIndex; i++) {
            int listIndex = i + 1;
            System.out.println(listIndex + "."
                    + taskList[i].toString());
        }
        System.out.println(divider + "\n");
    }

    public static void addTask(String input, String[] inputList) {
        int offset = inputList[0].length() + 1;
        if (inputList[0].equals("todo")) {
            taskList[taskIndex] = new Todo(input.substring(offset));

        } else if (inputList[0].equals("deadline")) {
            String[] params = input.split(" /by ");
            String by = params[1];
            taskList[taskIndex] = new Deadline(params[0].substring(offset), by);
        } else {
            String[] params = input.split(" /from ");
            String[] startEnd = params[1].split("/to");
            String from = startEnd[0];
            String to = startEnd[1];
            taskList[taskIndex] = new Event(params[0].substring(offset), from, to);

        }

        System.out.println(divider);
        System.out.println("Got it. I've added this task:");
        System.out.println("    " + taskList[taskIndex].toString());
        System.out.println("Now you have " + (taskIndex + 1) + " tasks in the list.");
        System.out.println(divider + "\n");
        taskIndex++;
    }

    public static void markTask(String input, int offset) {
        for (int i = 0; i < taskIndex; i++) {
            if (taskList[i].getDescription().equals(input.substring(offset))) {
                taskList[i].mark();

                System.out.println(divider);
                System.out.println("Nice! I've marked this task as done.");
                System.out.println("    " + taskList[i].toString());
                System.out.println(divider + "\n");
                break;
            }
        }
    }

    public static void unmarkTask(String input, int offset) {
        for (int i = 0; i < taskIndex; i++) {
            if (taskList[i].getDescription().equals(input.substring(offset))) {
                taskList[i].unmark();

                System.out.println(divider);
                System.out.println("Okay, I've unmarked this task as incomplete.");
                System.out.println("    " + taskList[i].toString());
                System.out.println(divider + "\n");
                break;
            }
        }
    }
}
