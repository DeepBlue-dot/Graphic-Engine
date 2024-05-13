module com.testing {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.testing to javafx.fxml;
    exports com.testing;
}
