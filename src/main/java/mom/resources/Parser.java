package mom.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import mom.command.Command;
import mom.exceptions.CorruptedFileException;
import mom.exceptions.InvalidInputException;

/**
 * Contains methods to parse input from user and data collected from hard disk file.
 */
public interface Parser {
    /**
     * List of valid datetime formats that can be parsed by chatbot
     */

    List<String> DATETIME_FORMATS = List.of("yyyy-M-d HHmm", "yyyy-M-d", "d/M/yyyy HHmm", "d/M/yyyy");

    /**
     * Parse raw user input.
     *
     * @param input Raw input string from user.
     * @return An array containing the command, raw input string, the input divided by " " in a
     *         String[] array, and an integer offset where command ends in the raw input.
     * @throws InvalidInputException If the command is not a valid Command enum type.
     */
    static Object[] parseInput(String input) throws InvalidInputException {
        String[] inputList = input.split(" ");
        Command command = Command.valueOf(inputList[0]);
        if (!checkValidCommand(inputList[0])) {
            throw new InvalidInputException("Please enter a valid command.");
        }

        int offset = inputList[0].length() + 1;
        return new Object[]{command, input, inputList, offset};
    }

    /**
     * Checks if command from user input is valid.
     *
     * @param userCommand String taken from user input.
     * @return A boolean indicating if the string is a valid command.
     */
    static boolean checkValidCommand(String userCommand) {
        for (Command command : Command.values()) {
            if (userCommand.equals(command.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse date time string into LocalDateTime type. If no time is given, assumes time is at 0000H.
     *
     * @param date Raw input string from user.
     * @return Parsed LocalDateTime date and time.
     * @throws DateTimeParseException If the string is not formatted properly or if no date was found.
     * @throws InvalidInputException  If the string format is not valid.
     */
    static LocalDateTime parseDate(String date) throws DateTimeParseException, InvalidInputException {
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

    /**
     * Parse Todo task from hard disk task list.
     *
     * @param entry Raw input string from user.
     * @return Parsed string with Todo description.
     * @throws InvalidInputException If the entry is not valid.
     */
    static String parseEntryTodo(String entry, int offset) throws InvalidInputException {
        if (entry.split(" ").length == 1) {
            throw new InvalidInputException(
                    "A 'todo' task requires a task description. " + "Please include a valid description.");
        }
        return entry.substring(offset);
    }

    /**
     * Parse Deadline task from hard disk task list.
     *
     * @param entry Raw input string from user.
     * @return Parsed string with Deadline description.
     * @throws InvalidInputException If the entry is not valid.
     */
    static Object[] parseEntryDeadline(String entry, int offset) throws InvalidInputException {
        String[] params = entry.split(" /by ");
        String by = params[1];
        LocalDateTime byDateTime = Parser.parseDate(by);
        return new Object[]{params[0].substring(offset), byDateTime};
    }

    /**
     * Parse Event task from hard disk task list.
     *
     * @param entry Raw input string from user.
     * @return Parsed string with Event description.
     * @throws InvalidInputException If the entry is not valid.
     */
    static Object[] parseEntryEvent(String entry, int offset) throws InvalidInputException {
        String[] params = entry.split(" /from ");
        String[] startEnd = params[1].split(" /to ");
        String from = startEnd[0];
        String to = startEnd[1];

        LocalDateTime fromDateTime = Parser.parseDate(from);
        LocalDateTime toDateTime = Parser.parseDate(to);
        return new Object[]{params[0].substring(offset), fromDateTime, toDateTime};
    }

    /**
     * Parse task entry from hard disk task list.
     *
     * @param entry Raw input string from user.
     * @return entryList Parsed string array of the task entry.
     * @throws CorruptedFileException If the entry is not properly formatted.
     */
    static String[] parseLoadTask(String entry) throws CorruptedFileException {
        if (!entry.contains("|")) {
            throw new CorruptedFileException("Entry in file not properly formatted:\n" + entry);
        }

        String[] entryList = entry.split(" \\| ");

        if (!entryList[1].equals("1") && !entryList[1].equals("0")) {
            throw new CorruptedFileException("Status of entry not properly documented:\n" + entry);
        }

        return entryList;
    }


    /**
     * Parse Deadline task entry from hard disk task list.
     *
     * @param entryList Raw input string in an array split by " " from user.
     * @param entry     Raw input string from user.
     * @return Parsed string array of the task entry containing the description, status, and
     *         deadline time.
     * @throws CorruptedFileException If the entry is not properly formatted.
     */
    static Object[] parseLoadDeadline(String[] entryList, String entry) throws CorruptedFileException {
        try {
            LocalDateTime byDateTime = Parser.parseDate(entryList[3]);
            return new Object[]{entryList[2], entryList[1], byDateTime};
        } catch (Exception e) {
            throw new CorruptedFileException("Invalid date format: " + entry);
        }
    }

    /**
     * Parse Event task entry from hard disk task list.
     *
     * @param entryList Raw input string in an array split by " " from user.
     * @param entry     Raw input string from user.
     * @return Parsed string array of the task entry containing the description, status, and
     *         start and end time.
     * @throws CorruptedFileException If the entry is not properly formatted.
     */
    static Object[] parseLoadEvent(String[] entryList, String entry) throws CorruptedFileException {
        if (!entryList[3].contains("-")) {
            throw new CorruptedFileException("Time entry in file not properly formatted:\n" + entry);
        }

        try {
            String[] startEnd = entryList[3].split("-");
            String from = startEnd[0];
            String to = startEnd[1];

            LocalDateTime fromDateTime = Parser.parseDate(from);
            LocalDateTime toDateTime = Parser.parseDate(to);
            return new Object[]{entryList[2], entryList[1], fromDateTime, toDateTime};
        } catch (Exception e) {
            throw new CorruptedFileException("Invalid date format: " + entryList[3]);
        }
    }

}
