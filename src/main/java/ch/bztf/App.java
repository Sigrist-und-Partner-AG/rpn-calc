package ch.bztf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
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
     * Sets up a calculator instance with constants predefined.
     * 
     * @return The fully initialized calculator instance.
     */
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
     * Runs an action after the initial window layout has settled.
     * {@code runLater()} needs to be wrapped again for this to work.
     * 
     * @param action The action to defer.
     */
    private void deferUntilStable(Runnable action) {
        Platform.runLater(() -> Platform.runLater(action));
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
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/bztf/calc.fxml"));
        final Parent root = loader.load();

        /* Inject calculator instance into controller */
        final CalcController controller = loader.<CalcController>getController();
        controller.initialize(makeCalcInstance());

        /* Enable proportional scaling */
        final double baseWidth = root.prefWidth(-1);
        final double baseHeight = root.prefHeight(-1);
        final Group scaledGroup = new Group(root);
        final StackPane scaledRoot = new StackPane(scaledGroup);
        root.scaleXProperty().bind(
            Bindings.min(
                scaledRoot.widthProperty().divide(baseWidth),
                scaledRoot.heightProperty().divide(baseHeight)
            )
        );
        root.scaleYProperty().bind(root.scaleXProperty());

        /* Set window properties */
        final Scene scene = new Scene(scaledRoot, baseWidth, baseHeight);
        stage.setScene(scene);
        stage.setTitle("RPN Calculator");
        stage.setResizable(true);

        /* Override application icon in different sizes */
        stage.getIcons().addAll(
            new Image(getClass().getResourceAsStream("/ch/bztf/icon-16.png")),
            new Image(getClass().getResourceAsStream("/ch/bztf/icon-32.png")),
            new Image(getClass().getResourceAsStream("/ch/bztf/icon-64.png")),
            new Image(getClass().getResourceAsStream("/ch/bztf/icon-128.png"))
        );

        /* Lock minimum width and height */
        stage.show();
        deferUntilStable(() -> {
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
