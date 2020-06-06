module view {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;

    opens model to com.google.gson;
    opens model.account to com.google.gson;
    opens model.request to com.google.gson;
    opens model.log to com.google.gson;

    opens fxml to javafx.fxml;
    exports view;
}