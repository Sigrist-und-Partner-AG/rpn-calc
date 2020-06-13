package ch.bztf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

/**
 * JavaFX utilizes an MVC (model-view-controller)
 * architecture. This class is the controller for
 * the calculator represented by the fxml.
 * 
* @author Timothy R. Schmid
 */
public class CalcController {

    /**
     * The free-form text field into which the RPN
     * expression is entered. Validation is performed
     * only as soon as the result is requested.
     */
    @FXML
    private TextField input;

    /** The manually uneditable text field for error messages. */ 
    @FXML
    private TextField error;

    /**
     * Wipes the input field completely.
     * 
     * @param ev The action event.
     *           It is unused by this function.
     */
    @FXML
    private void clearInput(ActionEvent ev) {
        input.setText("");
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
    private void deleteInputByChar(ActionEvent ev) {
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
     *           function simply sets the error text.
     * 
     * @param ev The action event.
     *           It is unused by this function.
     */
    @FXML
    private void deleteInputByWord(ActionEvent ev) {
        try {
            Robot r = new Robot();
            r.keyPress(KeyCode.CONTROL);
            r.keyPress(KeyCode.BACK_SPACE);
            r.keyRelease(KeyCode.BACK_SPACE);
            r.keyRelease(KeyCode.CONTROL);
        } catch (SecurityException e) {
            error.setText("Error: permission 'createRobot' not granted");
        }
    }

    /**
     * Inserts the accessibility text of a given
     * button into the input text field at the
     * current caret position.
     * 
     * @param ev The given button's action event
     *           from which the accessible text is
     *           retrieved.
     */
    @FXML
    private void insertInput(ActionEvent ev) {
        Button button = (Button)ev.getSource();
        input.insertText(input.getCaretPosition(), button.getAccessibleText());
    }

    /**
     * Inserts the accessibility text of a given
     * button into the input text field at the
     * current caret position. The text to be
     * inserted is enclosed in a space each if
     * no space is present already, with the
     * exception of the string start or end.
     * 
     * @param ev The given button's action event
     *           from which the accessible text is
     *           retrieved.
     */
    @FXML
    private void insertInputWithSpacing(ActionEvent ev) {
        String text = input.getText();
        int pos = input.getCaretPosition();
        
        String prepend = "";
        if (pos > 0 && text.charAt(pos - 1) != ' ') {
            prepend += ' ';
        }

        String append = "";
        if (pos < text.length() && text.charAt(pos) != ' ') {
            append += ' ';
        }

        Button button = (Button)ev.getSource();
        input.insertText(pos, prepend + button.getAccessibleText() + append);
    }

    @FXML
    private void evalInput(ActionEvent ev) {
        
    }

    @FXML
    private void increasePrecision(ActionEvent ev) {

    }

    @FXML
    private void decreasePrecision(ActionEvent ev) {

    }
}
