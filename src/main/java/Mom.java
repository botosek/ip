import java.util.Scanner;

public class Mom {
    static String chatbotName = "Mom";
    static String divider = "--------------------------------------------------";
    public static void main(String[] args) {

        System.out.println("Hi, I'm " + chatbotName);
        System.out.println("What can I do for you?");
        System.out.println(divider + "\n");

        Scanner scan = new Scanner(System.in);
        String input = "";

        do {
            input = scan.nextLine();

            System.out.println(divider);
            System.out.println("User Input: " + input);
            System.out.println(divider + "\n");

        } while (!input.equals("bye"));

        System.out.println("Bye. See you soon!");
    }
}
