# *Mom* Chatbot User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

![img.png](img.png)

// Product intro goes here

The Mom chatbot is a simple desktop chatbot app which main purpose is to help you keep track of tasks and deadlines.
While it has the benefits and appearance of a Graphical User Interface(GUI), it is optimized to be used via a Command
Line Interface(CLI). The faster you type the faster you can get the Mom chatbot to execute different commands and manage
your to do list faster!


---

## How to Start

1. Ensure that you have Java 17 installed on your computer.
2. Download the latest `.jar` release file of the Mom chatbot. If you want the `.jar` file to run and your tasklist file to
be stored in a specific folder, copy the file to the desired folder.

# Features


## Adding Todos
Adds a todo type task to the task list.

Format: `todo {DESCRIPTION}`

Examples:
- `todo read book`
- `todo eat bun`


## Adding Deadlines
Adds a deadline type task to the task list. If no time is specified, the time is set by default to 00:00.

Format: 
- `deadline {DESCRIPTION} /by {DATE} {TIME}`
- `deadline {DESCRIPTION} /by {DATE}`

Examples:
- `deadline read book /by 18/5/2025 `
- `deadline eat bun /by 18/5/2025 5pm`

## Adding events
Adds an event type task to the task list. If no time is specified, the time is set by default to 00:00.

Format:
- `event {DESCRIPTION} /from {DATE} {TIME} /to {DATE} {TIME}`
- `event {DESCRIPTION} /from {DATE} {TIME} /to {DATE}`
- `event {DESCRIPTION} /from {DATE} /to {DATE} {TIME}`
- `event {DESCRIPTION} /from {DATE} /to {DATE}`

Examples:
- `event read book /from 17/5/2025 2pm /to 18/5/2025 5pm`
- `event read book /from 17/5/2025 2pm /to 18/5/2025`
- `event read book /from 17/5/2025 /to 18/5/2025 5pm`
- `event read book /from 17/5/2025 /to 18/5/2025`

## Listing task
Prints the task list.

Format: `list`

Example: `list`

## Mark task
Marks a task as complete. If the task has already been marked as complete, the task status will remain as complete.

## Unmark task
Unmarks a task as incomplete. If the task has already been marked as incomplete, the task status will remain as
incomplete.

## Delete task
Deletes a task in the task list given the rank of the task in the task list.

## Undo command
Reverts the task list to the state before the last command(this does not apply to the list command as the list command
does not make any changes to the task list).


// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details