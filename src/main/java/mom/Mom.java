package mom;

import mom.command.Command;
import mom.exceptions.CorruptedFileException;
import mom.exceptions.InvalidInputException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.Array;
import java.util.Scanner;

public class Mom implements Parser {
    private static final String TASKLIST_FILEPATH = "data" + File.separator + "mom.txt";
    private static final String CHATBOT_NAME = "Mom";

    private final Storage storage;
    private final Ui ui;

    private static TaskList taskList;

    public Mom(String filePath) {
        ui = new Ui(CHATBOT_NAME);
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage.load());
        } catch (FileNotFoundException e) {
            Ui.errorDisplay("File not found:\n" + filePath);
            taskList = new TaskList();
        } catch (CorruptedFileException e) {
            Ui.errorDisplay("Corrupted file:\n" + e);
            taskList = new TaskList();
        } catch (IOException e) {
            Ui.errorDisplay("I/O error:\n" + e);
        }
    }

    public void run() {
        ui.displayIntro();

        Scanner scan = new Scanner(System.in);
        while (true) {
            String input = scan.nextLine();
            try {
                Object[] parsedInput = Parser.parseInput(input);
                Command command = (Command) parsedInput[0];
                String inputString = (String) parsedInput[1];
                String[] inputList = (String[]) parsedInput[2];
                int offset = (Integer) parsedInput[3];

                if (command == Command.bye) {
                    break;
                } else {
                    taskList.handleTaskCommand(ui, taskList, command, inputString, inputList, offset);
                }
            } catch (InvalidInputException e) {
                Ui.errorDisplay(e.toString());
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                Ui.errorDisplay("Invalid Command:\n" + e.getMessage());
            }
        }

        try {
            storage.save(taskList);
        } catch (IOException e) {
            Ui.errorDisplay("Something went wrong when saving Task List: " + e);
        }

        ui.displayOutro();
    }

    public static void main(String[] args) {
        new Mom(TASKLIST_FILEPATH).run();
    }

}
