import common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Scanner;

public class Storage implements Parser {
    private final String filePath;
    private final ArrayList<Task> tasks = new ArrayList<>(100);

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws FileNotFoundException, CorruptedFileException {
        File f = new File(this.filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String entry = s.nextLine();

            try {
                String[] entryList = Parser.parseLoadTask(entry);
                handleFileEntries(entryList, entry);
            } catch (CorruptedFileException e) {
                Ui.errorDisplay(e.toString());
            }
        }
        s.close();

        return this.tasks;
    }

    public void handleFileEntries(String[] entryList, String entry) throws CorruptedFileException {
        String commandString = entryList[0];
        switch (commandString) {
            case "T": {
                tasks.add(new Todo(entryList[2], entryList[1]));
                break;
            }
            case "D": {
                Object[] result = Parser.parseLoadDeadline(entryList, entry);
                String description = (String) result[0];
                String status = (String) result[1];
                LocalDateTime by = (LocalDateTime) result[2];
                this.tasks.add(new Deadline(description, status, by));
                break;
            }
            case "E": {
                Object[] result = Parser.parseLoadEvent(entryList, entry);
                String description = (String) result[0];
                String status = (String) result[1];
                LocalDateTime from = (LocalDateTime) result[2];
                LocalDateTime to = (LocalDateTime) result[3];
                this.tasks.add(new Event(description, status, from, to));
                break;
            }
            default: {
                throw new CorruptedFileException("Entry does not contain valid command\n" + entry);
            }
        }
    }

    public void save(TaskList tasklist) throws IOException {
        File f = new File(this.filePath);
        FileWriter fw = new FileWriter(f);
        ArrayList<Task> tasks = tasklist.getTaskList();
        for (int i = 0; i < tasks.size() - 1; i++) {
            fw.write(tasks.get(i).toSaveString() + "\n");
        }
        fw.write(tasks.get(tasks.size() - 1).toSaveString());
        fw.close();
    }

}
