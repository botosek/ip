import java.util.Scanner;

public class Mom {
    static String chatbotName = "Mom";
    static String divider = "--------------------------------------------------";
    static String[] taskList = new String[100];
    static int  taskIndex = 0;

    public static void main(String[] args) {

        System.out.println("Hi, I'm " + chatbotName);
        System.out.println("What can I do for you?");
        System.out.println(divider + "\n");

        Scanner scan = new Scanner(System.in);
        String input = "";

        do {
            input = scan.nextLine();

            if (input.equals("list")) {
                System.out.println(divider);
                for(int i = 0; i < taskIndex; i++) {
                    int listIndex = i + 1;
                    System.out.println(listIndex + ". " + taskList[i]);
                }
                System.out.println(divider + "\n");
            } else {
                taskList[taskIndex] = input;
                taskIndex++;

                System.out.println(divider);
                System.out.println("Task added: " + input);
                System.out.println(divider + "\n");
            }
        } while (!input.equals("bye"));

        System.out.println("Bye. See you soon!");
    }
}
