module com.game_engine {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.game_engine to javafx.fxml;
    exports com.game_engine;
}
