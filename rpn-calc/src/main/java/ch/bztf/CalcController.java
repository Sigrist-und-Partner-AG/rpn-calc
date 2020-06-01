package ch.bztf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

/**
 * @author Timothy R. Schmid
 * 
 * JavaFX utilizes an MVC (model-view-controller)
 * architecture. This class is the controller for
 * the calculator represented by the fxml.
 */
public class CalcController {

    /**
     * The free-form text field into which the RPN
     * expression is entered. Validation is performed
     * only as soon as the result is requested.
     */
    @FXML
    private TextField input;

    /**
     * Wipes the input field completely.
     * 
     * @param ev The action event.
     *           It is unused by this function.
     */
    @FXML
    private void clearInput(ActionEvent ev) {
        input.setText("");
        input.requestFocus();
    }

    /**
     * Deletes the character that precedes the current
     * caret position from the input text field.
     * If a selection is active, the selected text
     * is deleted in its entirety instead.
     * <p>
     * Note: This is equivalent to pressing the
     *       "backspace" key on the keyboard.
     * </p>
     * 
     * @param ev The action event.
     *           It is unused by this function.
     */
    @FXML
    private void truncateInputByChar(ActionEvent ev) {
        input.deletePreviousChar();
    }

    /**
     * Deletes the string that precedes the current
     * caret position from the input text field,
     * delimited by whitespace or the start of text.
     * If a selection is active, the selected text
     * is deleted in its entirety instead.
     * <p>
     * Note: This is equivalent to pressing the key
     *       combination "control" + "backspace" on
     *       the keyboard.
     * </p>
     * @implNote This function sends virtual keystrokes
     *           to the calculator by instantiating a
     *           {@code Robot}. This was simpler than
     *           attempting to re-code the built-in
     *           functionality. This may fail if the
     *           appropriate security manager permission
     *           hasn't been set, in which case this
     *           function becomes a no-op.
     * 
     * @param ev The action event.
     *           It is unused by this function.
     */
    @FXML
    private void truncateInputByWord(ActionEvent ev) {
        try {
            Robot r = new Robot();
            r.keyPress(KeyCode.CONTROL);
            r.keyPress(KeyCode.BACK_SPACE);
            r.keyRelease(KeyCode.BACK_SPACE);
            r.keyRelease(KeyCode.CONTROL);
        } catch (SecurityException e) {
            System.err.println("Warning: Permission 'createRobot' not granted");
        }
    }


    @FXML
    private void appendToInput(ActionEvent ev) {
        Button button = (Button)ev.getSource();
        input.appendText(button.getText() + " ");
    }

    // appendDigitToInput
    // appendOperatorToInput
}
