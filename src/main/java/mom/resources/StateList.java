package mom.resources;

import java.util.ArrayList;

/**
 * StateList class that contains the states of TaskLists
 */
public class StateList {
    private static final ArrayList<TaskList> states = new ArrayList<>();
    private static int currentState = 0;

    /**
     * Instantiate task list with old task list data loaded in.
     */
    public StateList() {
    }

    /**
     * Add a new state and move current state number forward by one.
     */
    public static void addState(TaskList state) {
        while (states.size() > currentState) {
            states.remove(states.size() - 1);
        }

        StateList.states.add(new TaskList(state));
        StateList.currentState++;
    }

    /**
     * Move current state number back by one
     */
    public static void undo() {
        if (currentState > 1) {
            currentState--;
        }
    }

    /**
     * Get the current state.
     */
    public static TaskList getCurrentState() {
        System.out.println("Current State: " + StateList.currentState);
        return states.get(currentState - 1);
    }

    /**
     * Print all states and their respective task lists for debugging.
     */
    public static void printStates() {
        System.out.println("All States:");
        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < states.get(i).getSize(); j++) {
                System.out.println("Task " + j + ": " + states.get(i).getTask(j + 1));
            }
        }
        System.out.println("Current State Pointer: " + currentState);
    }
}
