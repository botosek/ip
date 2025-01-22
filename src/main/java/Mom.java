import java.util.Scanner;

public class Mom {
    static String chatbotName = "Mom";
    static String divider = "--------------------------------------------------";
    static Task[] taskList = new Task[100];
    static int  taskIndex = 0;

    public static void main(String[] args) {

        System.out.println("Hi, I'm " + chatbotName);
        System.out.println("What can I do for you?");
        System.out.println(divider + "\n");

        Scanner scan = new Scanner(System.in);
        String input = "";

        do {
            input = scan.nextLine();
            String[] inputList = input.split(" ");

            if (input.equals("list")) {
                System.out.println(divider);
                for(int i = 0; i < taskIndex; i++) {
                    int listIndex = i + 1;
                    System.out.println(listIndex + ". "
                            + taskList[i].getStatusIcon() + " "
                            + taskList[i].getDescription());
                }
                System.out.println(divider + "\n");

            } else if (inputList[0].equals("mark")) {
                for(int i = 0; i < taskIndex; i++) {
                    if (taskList[i].getDescription().equals(input.substring(5))) {
                        taskList[i].mark();

                        System.out.println(divider);
                        System.out.println("Nice! I've marked this task as done.");
                        System.out.println("    " + taskList[i].getStatusIcon()
                                + " " + taskList[i].getDescription());
                        System.out.println(divider + "\n");
                        break;
                    }
                }

            } else if (inputList[0].equals("unmark")) {
                for(int i = 0; i < taskIndex; i++) {
                    if (taskList[i].getDescription().equals(input.substring(7))) {
                        taskList[i].unmark();

                        System.out.println(divider);
                        System.out.println("Okay, I've unmarked this task as incomplete.");
                        System.out.println("    " + taskList[i].getStatusIcon()
                                + " " + taskList[i].getDescription());
                        System.out.println(divider + "\n");
                        break;
                    }
                }

            } else {
                taskList[taskIndex] = new Task(input);
                taskIndex++;

                System.out.println(divider);
                System.out.println("Task added: " + input);
                System.out.println(divider + "\n");

            }
        } while (!input.equals("bye"));

        System.out.println("Bye. See you soon!");
    }
}
