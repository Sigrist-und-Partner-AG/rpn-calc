module ch.bztf {
    requires transitive javafx.controls;
    requires javafx.fxml;
    exports ch.bztf;
    opens ch.bztf to javafx.fxml;
}