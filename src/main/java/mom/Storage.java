package mom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import mom.exceptions.CorruptedFileException;
import mom.task.Deadline;
import mom.task.Event;
import mom.task.Task;
import mom.task.Todo;

public class Storage implements Parser {
    private final String filePath;
    private final ArrayList<Task> tasks = new ArrayList<>(100);

    public Storage(String filePath) {
        this.filePath = getFilePath(filePath);
    }

    public static String getFilePath(String filePath) {
        File jarFile = new File(Mom.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File f = new File(jarFile.getParentFile().getParent(), filePath);

        if (jarFile.toString().endsWith(".jar")) {
            f.getParentFile().mkdirs();
            return f.getAbsolutePath();
        } else {
            return "." + File.separator + filePath;
        }
    }

    public ArrayList<Task> load() throws CorruptedFileException, IOException, SecurityException {
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
        for (int i = 0; i < tasks.size(); i++) {
            if (i < tasks.size() - 1) {
                fw.write(tasks.get(i).toSaveString() + "\n");
            } else {
                fw.write(tasks.get(i).toSaveString());
            }
        }
        fw.close();
    }

}
