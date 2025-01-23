import java.util.ArrayList;
import java.util.Scanner;

public class Mom {
    static String CHATBOT_NAME = "Mom";
    static String DIVIDER = "--------------------------------------------------";
    static ArrayList<Task> taskList = new ArrayList<Task>(100);
    static int taskIndex = 0;



    public static void main(String[] args) {

        System.out.println("Hi, I'm " + CHATBOT_NAME);
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER + "\n");

        Scanner scan = new Scanner(System.in);
        String input = "";

        while (true) {
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

            } else if (inputList[0].equals("delete")) {
                int rank = Integer.parseInt(inputList[1]);
                Mom.deleteTask(rank);

            } else {

                try {
                    addTask(input, inputList);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
        }
        scan.close();

        System.out.println("Bye. See you soon!");
    }

    public static void listTasks() {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size() ; i++) {
            int listIndex = i + 1;
            System.out.println(listIndex + "."
                    + (taskList.get(i).toString()));
        }

        System.out.println(DIVIDER + "\n");
    }

    public static void addTask(String input, String[] inputList) throws InvalidInputException {
        int offset = inputList[0].length() + 1;
        switch (inputList[0]) {
            case "todo": {
                if (inputList.length == 1) {
                    throw new InvalidInputException("A 'todo' task requires a task description. " +
                            "Please include a valid description");
                }

                taskList.add(new Todo(input.substring(offset)));
                break;
            }
            case "deadline": {
                String[] params = input.split(" /by ");
                String by = params[1];
                taskList.add(new Deadline(params[0].substring(offset), by));
                break;
            }
            case "event": {
                String[] params = input.split(" /from ");
                String[] startEnd = params[1].split("/to");
                String from = startEnd[0];
                String to = startEnd[1];
                taskList.add(new Event(params[0].substring(offset), from, to));
                break;
            }
            default: {
                throw new InvalidInputException("Please enter a valid command.");

            }
        }

        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("    " + taskList.get(taskList.size() - 1).toString());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
        taskIndex++;
    }

    public static void markTask(String input, int offset) {
        for (int i = 0; i < taskIndex; i++) {
            if (taskList.get(i).getDescription().equals(input.substring(offset))) {
                taskList.get(i).mark();

                System.out.println(DIVIDER);
                System.out.println("Nice! I've marked this task as done.");
                System.out.println("    " + taskList.get(i).toString());
                System.out.println(DIVIDER + "\n");
                break;
            }
        }
    }

    public static void unmarkTask(String input, int offset) {
        for (int i = 0; i < taskIndex; i++) {
            if (taskList.get(i).getDescription().equals(input.substring(offset))) {
                taskList.get(i).unmark();

                System.out.println(DIVIDER);
                System.out.println("Okay, I've unmarked this task as incomplete.");
                System.out.println("    " + taskList.get(i).toString());
                System.out.println(DIVIDER + "\n");
                break;
            }
        }
    }

    public static void deleteTask(int rank) {
        System.out.println(DIVIDER);
        System.out.println("Understood, removing the task below:");
        System.out.println("    " + rank + "." + taskList.get(rank - 1).toString());
        System.out.println("Now you have " + (taskList.size() - 1) + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
        taskList.remove(rank - 1);
    }

}
