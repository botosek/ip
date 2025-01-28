import common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mom {
    private static final String TASKLIST_FILEPATH = "." + File.separator + "src" + File.separator + "main"
            + File.separator + "java" + File.separator + "data" + File.separator + "mom.txt";
    private static final String CHATBOT_NAME = "Mom";
    private static final String DIVIDER = "--------------------------------------------------";

    private static final List<String> DATETIME_FORMATS = List.of(
            "yyyy-M-d HHmm",
            "yyyy-M-d",
            "d/M/yyyy HHmm",
            "d/M/yyyy");

    private static final ArrayList<Task> taskList = new ArrayList<>(100);


    public static void main(String[] args) {

        System.out.println("Hi, I'm " + CHATBOT_NAME);
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER + "\n");

        try {
            loadTaskList(TASKLIST_FILEPATH);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }


        Scanner scan = new Scanner(System.in);

        while (true) {
            String input = scan.nextLine();
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

        try {
            saveTaskList(TASKLIST_FILEPATH);
        } catch (IOException e) {
            System.out.println("Something went wrong when saving Task List: " + e.toString());
        }

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
        try {
            switch (command) {
                case todo: {
                    if (input.split(" ").length == 1) {
                        throw new InvalidInputException("A 'todo' task requires a task description. " +
                                "Please include a valid description.");
                    }

                    taskList.add(new Todo(input.substring(offset)));
                    break;
                }
                case deadline: {
                    String[] params = input.split(" /by ");
                    String by = params[1];
                    LocalDateTime byDateTime = parseDate(by);
                    taskList.add(new Deadline(params[0].substring(offset), byDateTime));
                    break;
                }
                case event: {
                    String[] params = input.split(" /from ");
                    String[] startEnd = params[1].split(" /to ");
                    String from = startEnd[0];
                    String to = startEnd[1];

                    LocalDateTime fromDateTime = parseDate(from);
                    LocalDateTime toDateTime = parseDate(to);
                    taskList.add(new Event(params[0].substring(offset), fromDateTime, toDateTime));
                    break;
                }
                default: {
                    throw new InvalidInputException("Please enter a valid command.");

                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidInputException("Invalid Command: " + input);
        }

        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("    " + taskList.get(taskList.size() - 1).toString());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
    }


    public static void loadTaskList(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String entry = s.nextLine();

            try {
                if (!entry.contains("|")) {
                    throw new CorruptedFileException("Entry in file not properly formatted:\n" + entry);
                }

                String[] entryList = entry.split(" \\| ");

                if (!entryList[1].equals("1") && !entryList[1].equals("0")) {
                    throw new CorruptedFileException("Status of entry not properly documented:\n" + entry);
                }

                handleFileEntries(entryList, entry);
            } catch (CorruptedFileException e) {
                System.out.println(e.toString());
            }
        }
        s.close();
    }

    public static void handleFileEntries(String[] entryList, String entry) throws CorruptedFileException {

        switch (entryList[0]) {
            case "T": {
                taskList.add(new Todo(entryList[2], entryList[1]));
                break;
            }
            case "D": {
                try {
                    LocalDateTime byDateTime = parseDate(entryList[3]);
                    taskList.add(new Deadline(entryList[2], entryList[1], byDateTime));
                } catch (Exception e) {
                    throw new CorruptedFileException("Invalid date format: " + entryList[3]);
                }
                break;
            }
            case "E": {
                if (!entryList[3].contains("-")) {
                    throw new CorruptedFileException("Time entry in file not properly formatted:\n" + entry);
                }

                try {
                    String[] startEnd = entryList[3].split("-");
                    String from = startEnd[0];
                    String to = startEnd[1];

                    LocalDateTime fromDateTime = parseDate(from);
                    LocalDateTime toDateTime = parseDate(to);
                    taskList.add(new Event(entryList[2], entryList[1], fromDateTime, toDateTime));
                } catch (Exception e) {
                    throw new CorruptedFileException("Invalid date format: " + entryList[3]);
                }
                break;
            }
            default: {
                throw new CorruptedFileException("No valid command found for an entry:\n" + entry);
            }
        }
    }

    public static void saveTaskList(String filePath) throws IOException {
        File f = new File(filePath);
        FileWriter fw = new FileWriter(f);
        for (int i = 0; i < taskList.size() - 1; i++) {
            fw.write(taskList.get(i).toSaveString() + "\n");
        }
        fw.write(taskList.get(taskList.size() - 1).toSaveString());
        fw.close();
    }


    public static LocalDateTime parseDate(String date) throws DateTimeParseException, InvalidInputException {
        for (String datetimeFormat : DATETIME_FORMATS) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datetimeFormat);
                return LocalDateTime.parse(date, dateTimeFormatter);
            } catch (DateTimeParseException failedDateTime) {
                try {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datetimeFormat);
                    LocalDate localDate = LocalDate.parse(date, dateFormatter);
                    return localDate.atStartOfDay();
                } catch (DateTimeParseException failedDate) {
                    continue;
                }
            }
        }

        // If no format matches, throw an exception
        throw new InvalidInputException("Invalid date format: " + date);
    }
}
