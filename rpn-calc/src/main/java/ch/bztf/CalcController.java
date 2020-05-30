package ch.bztf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    @FXML
    private void appendToInput(ActionEvent ev) {
        Button button = (Button)ev.getSource();
        input.appendText(button.getText() + " ");
    }

    // appendDigitToInput
    // appendOperatorToInput
}
