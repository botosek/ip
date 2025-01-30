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

/**
 * Storage class that loads and saves tasklist from hard disk.
 */
public class Storage implements Parser {
    /** Absolute filepath of hard disk file. */
    private final String filePath;
    private final ArrayList<Task> tasks = new ArrayList<>(100);

    /**
     * Creates Storage object and saves filepath of hard disk file.
     *
     * @param filePath File path of the hard disk file in reference to the program file.
     */
    public Storage(String filePath) {
        this.filePath = getFilePath(filePath);
    }

    /**
     * If it is run as a packaged .jar file, looks for or creates hard disk file in reference to project root.
     * Else, looks for or creates it reference to the project root.
     *
     * @param filePath File path of the hard disk file in reference to the program file.
     * @return         File path of the hard disk file to be loaded from or saved into.
     */
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

    /**
     * Loads tasks from hard disk file
     *
     * @return tasks Arraylist of tasks from hard disk
     * @throws CorruptedFileException If the task entry is not correctly formatted.
     * @throws IOException If the contents of the file is not correctly formatted.
     * @throws SecurityException If the program is unable to access the file due to security permissions.
     */
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

    /**
     * For each entry raw string, create its corresponding task object and saves it to an arraylist
     *
     * @param entryList Parsed String[] array of the entry string.
     * @param entry     Raw entry string.
     * @throws CorruptedFileException If no valid task was found for the entry string.
     */
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


    /**
     * Save task list to hard disk.
     *
     * @param tasklist ArrayList of tasks to be saved.
     * @throws IOException If issues are faced when trying to write into the hard disk file.
     */
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
