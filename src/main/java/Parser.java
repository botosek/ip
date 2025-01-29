import common.Command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;

public interface Parser {
    List<String> DATETIME_FORMATS = List.of(
            "yyyy-M-d HHmm",
            "yyyy-M-d",
            "d/M/yyyy HHmm",
            "d/M/yyyy");

    static Object[] parseInput(String input) throws InvalidInputException {
        String[] inputList = input.split(" ");
        Command command = Command.valueOf(inputList[0]);
        if (!checkValidCommand(inputList[0])) {
            throw new InvalidInputException("Please enter a valid command.");
        }

        int offset = inputList[0].length() + 1;
        return new Object[]{command, input, inputList, offset};
    }

    static boolean checkValidCommand(String userCommand) {
        for (Command command : Command.values()) {
            if (userCommand.equals(command.toString())) {
                return true;
            }
        }
        return false;
    }

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

    static String parseEntryTodo(String entry, int offset) throws InvalidInputException{
        if (entry.split(" ").length == 1) {
            throw new InvalidInputException("A 'todo' task requires a task description. " +
                    "Please include a valid description.");
        }
        return entry.substring(offset);
    }

    static Object[] parseEntryDeadline(String entry, int offset) throws InvalidInputException{
        String[] params = entry.split(" /by ");
        String by = params[1];
        LocalDateTime byDateTime = Parser.parseDate(by);
        return new Object[]{params[0].substring(offset), byDateTime};
    }

    static Object[] parseEntryEvent(String entry, int offset) throws InvalidInputException{
        String[] params = entry.split(" /from ");
        String[] startEnd = params[1].split(" /to ");
        String from = startEnd[0];
        String to = startEnd[1];

        LocalDateTime fromDateTime = Parser.parseDate(from);
        LocalDateTime toDateTime = Parser.parseDate(to);
        return new Object[]{params[0].substring(offset), fromDateTime, toDateTime};
    }

    static String[] parseLoadTask(String entry) throws CorruptedFileException{
        if (!entry.contains("|")) {
            throw new CorruptedFileException("Entry in file not properly formatted:\n" + entry);
        }

        String[] entryList = entry.split(" \\| ");

        if (!entryList[1].equals("1") && !entryList[1].equals("0")) {
            throw new CorruptedFileException("Status of entry not properly documented:\n" + entry);
        }

        return entryList;
    }

    static Object[] parseLoadDeadline(String[] entryList, String entry) throws CorruptedFileException{
        try {
            LocalDateTime byDateTime = Parser.parseDate(entryList[3]);
            return new Object[]{entryList[2], entryList[1], byDateTime};
        } catch (Exception e) {
            throw new CorruptedFileException("Invalid date format: " + entry);
        }
    }

    static Object[] parseLoadEvent(String[] entryList, String entry) throws CorruptedFileException{
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
