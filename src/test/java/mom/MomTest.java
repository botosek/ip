package mom;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import mom.resources.Parser;
import mom.task.Deadline;
import mom.task.Event;
import mom.task.Task;
import mom.task.Todo;

public class MomTest {
    @Test
    public void parser_noDescription_exceptionThrown() {
        try {
            Object[] result = Parser.parseInput("exit");
            fail();
        } catch (Exception e) {
            assertEquals("No enum constant mom.command.Command.exit", e.getMessage());
        }
    }

    @Test
    public void todo_toSaveString_expectedOutcome() {
        Task task = new Todo("eat");
        assertEquals("T | 0 | eat", task.toSaveString());
    }

    @Test
    public void deadline_toSaveString_expectedOutcome() {
        try {
            LocalDateTime by = Parser.parseDate("5/5/2025");
            Task task = new Deadline("eat", by);
            assertEquals("D | 0 | eat | 5/5/2025 0000", task.toSaveString());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void event_toSaveString_expectedOutcome() {
        try {
            LocalDateTime from = Parser.parseDate("5/5/2025 1800");
            LocalDateTime to = Parser.parseDate("6/5/2025");
            Task task = new Event("eat", from, to);
            assertEquals("E | 0 | eat | 5/5/2025 1800-6/5/2025 0000", task.toSaveString());
        } catch (Exception e) {
            fail();
        }
    }
}

