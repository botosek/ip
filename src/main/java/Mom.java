import java.util.ArrayList;
import java.util.Scanner;

public class Mom {
    static String CHATBOT_NAME = "Mom";
    static String DIVIDER = "--------------------------------------------------";
    static ArrayList<Task> taskList = new ArrayList<Task>(100);


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

            try {
                if (!checkValidCommand(inputList[0])) {
                    throw new InvalidInputException("Please enter a valid command.");
                }

                Command command = Command.valueOf(inputList[0]);
                if (command == Command.bye) {
                    break;
                } else {
                    handleTaskCommand(command, input, inputList, offset);
                }

            } catch (InvalidInputException e) {
                System.out.println(e.toString());
            }


        }
        scan.close();

        System.out.println("Bye. See you soon!");
    }

    public static boolean checkValidCommand(String userCommand) {
        for (Command command : Command.values()) {
            if (userCommand.equals(command.toString())) {
                return true;
            }
        }
        return false;
    }

    public static void handleTaskCommand(Command command, String input, String[] inputList, int offset) {
        switch (command) {
            case list: {
                listTasks();
                break;
            }
            case mark: {
                markTask(input, offset);
                break;
            }
            case unmark: {
                unmarkTask(input, offset);
                break;
            }
            case delete: {
                int rank = Integer.parseInt(inputList[1]);
                deleteTask(rank);
                break;
            }
            default: {
                try {
                    addTask(command, input, offset);
                } catch (InvalidInputException e) {
                    System.out.println(e.toString());
                }
                break;
            }
        }
    }

    public static void listTasks() {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            int listIndex = i + 1;
            System.out.println(listIndex + "."
                    + (taskList.get(i).toString()));
        }

        System.out.println(DIVIDER + "\n");
    }

    public static void markTask(String input, int offset) {
        for (Task task : taskList) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.mark();

                System.out.println(DIVIDER);
                System.out.println("Nice! I've marked this task as done.");
                System.out.println("    " + task.toString());
                System.out.println(DIVIDER + "\n");
                break;
            }
        }
    }

    public static void unmarkTask(String input, int offset) {
        for (Task task : taskList) {
            if (task.getDescription().equals(input.substring(offset))) {
                task.unmark();

                System.out.println(DIVIDER);
                System.out.println("Okay, I've unmarked this task as incomplete.");
                System.out.println("    " + task.toString());
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

    public static void addTask(Command command, String input, int offset) throws InvalidInputException {
        switch (command) {
            case todo: {
                if (input.split(" ").length == 1) {
                    throw new InvalidInputException("A 'todo' task requires a task description. " +
                            "Please include a valid description");
                }

                taskList.add(new Todo(input.substring(offset)));
                break;
            }
            case deadline: {
                String[] params = input.split(" /by ");
                String by = params[1];
                taskList.add(new Deadline(params[0].substring(offset), by));
                break;
            }
            case event: {
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
    }

}
