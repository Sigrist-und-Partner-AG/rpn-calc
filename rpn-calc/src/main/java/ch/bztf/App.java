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
        Parent root = FXMLLoader.load(getClass().getResource("/ch/bztf/calc.fxml"));
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