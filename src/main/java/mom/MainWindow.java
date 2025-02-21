package mom;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mom.command.Command;
import mom.exceptions.InvalidInputException;
import mom.gui.DialogBox;
import mom.resources.StateList;
import mom.resources.Ui;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Mom mom;

    /**
     * Start up main window
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String response = Ui.displayIntro();
        dialogContainer.getChildren().addAll(DialogBox.getDukeDialog(response, dukeImage, Command.regular));
    }

    /**
     * Injects the Mom instance
     */
    public void setMom(Mom d) {
        mom = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Mom's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.equals("bye")) {
            try {
                mom.saveTasks();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            Stage stage = (Stage) userInput.getScene().getWindow();
            stage.close();
        } else if (input.equals("undo")) {
            StateList.undo();
            String response = Ui.displayUndo();
            dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage), DialogBox.getDukeDialog(response, dukeImage, Command.regular));
            userInput.clear();
        } else {
            String response = mom.getResponse(input);
            try {
                Command commandType = mom.getCommandType(input);
                dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                        DialogBox.getDukeDialog(response, dukeImage, commandType));
            } catch (IllegalArgumentException | InvalidInputException e) {
                dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                        DialogBox.getDukeDialog(response, dukeImage, Command.regular));
            }

            userInput.clear();
        }
    }
}
