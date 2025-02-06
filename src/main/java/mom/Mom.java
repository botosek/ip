package mom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mom.command.Command;
import mom.exceptions.CorruptedFileException;
import mom.exceptions.InvalidInputException;

/**
 * Main class that runs the chatbot "Mom".
 */
public class Mom implements Parser {
    /**
     * Relative file path of hard disk.
     */
    private static final String TASKLIST_FILEPATH = "data" + File.separator + "mom.txt";
    private static TaskList taskList;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates new chatbot Mom tasklist from hard disk.
     */
    public Mom() {
        ui = new Ui();
        storage = new Storage(TASKLIST_FILEPATH);
        try {
            taskList = new TaskList(storage.load());
        } catch (FileNotFoundException e) {
            System.out.println("File not found:\n" + TASKLIST_FILEPATH);
            taskList = new TaskList();
        } catch (CorruptedFileException e) {
            System.out.println("Corrupted file:\n" + e);
            taskList = new TaskList();
        } catch (IOException e) {
            System.out.println("I/O error:\n" + e);
        }
    }

    /**
     * Starts the main program.
     */
    public static void main(String[] args) {
        System.out.println("Hello!");
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Object[] parsedInput = Parser.parseInput(input);
            Command command = (Command) parsedInput[0];
            String inputString = (String) parsedInput[1];
            String[] inputList = (String[]) parsedInput[2];
            int offset = (Integer) parsedInput[3];
            return taskList.handleTaskCommandGui(taskList, command, inputString, inputList, offset);
        } catch (InvalidInputException e) {
            return "Error: " + e.toString();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return "Invalid Command: " + e.getMessage();
        }
    }

    public Command getCommandType(String input) throws InvalidInputException {
        return (Command) Parser.parseInput(input)[0];

    }

    public void saveTasks() throws IOException {
        storage.save(taskList);
    }
}
