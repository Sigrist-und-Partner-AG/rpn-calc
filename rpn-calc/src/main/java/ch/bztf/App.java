package ch.bztf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App initialization.
 * View the README for operative details.
 * 
 * @author Timothy R. Schmid
 */
public class App extends Application {

    private RPNCalc makeCalcInstance() {
        RPNCalc calc = new RPNCalc();
        calc.addRegister("PI",  Math.PI);
        calc.addRegister("E",   Math.E);
        calc.addRegister("MIN", Double.MIN_VALUE);
        calc.addRegister("MAX", Double.MAX_VALUE);
        calc.addRegister("INF", Double.POSITIVE_INFINITY);
        calc.addRegister("NAN", Double.NaN);
        return calc;
    }

    /**
     * JavaFX start hook.
     * This is used to bring the user interface up.
     * The scene is set to the default window size.
     * 
     * @param stage The stage to be coupled to the
     *              RPN calculator scene.
     */
    @Override
    public void start(Stage stage) throws Exception {
        /* Load FXML */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/bztf/calc.fxml"));
        Parent root = loader.load();
        /* Inject calculator instance into controller */
        CalcController controller = loader.<CalcController>getController();
        controller.initialize(makeCalcInstance());
        /* Set window properties */
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("RPN Calculator");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}