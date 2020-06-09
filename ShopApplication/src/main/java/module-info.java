module view {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.base;

    opens model to com.google.gson, javafx.base;
    opens model.account to com.google.gson, javafx.base;
    opens model.request to com.google.gson, javafx.base;
    opens model.log to com.google.gson, javafx.base;

    opens view to javafx.fxml, javafx.base;
    exports view;
}