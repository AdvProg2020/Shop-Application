module view {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens model to com.google.gson;
    opens model.account to com.google.gson;
    opens model.request to com.google.gson;
    opens model.log to com.google.gson;

    opens view to javafx.fxml, javafx.base;
    exports view;
}