module view {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens Server.model to com.google.gson;
    opens Server.model.account to com.google.gson;
    opens Server.model.request to com.google.gson;
    opens Server.model.log to com.google.gson;
    opens Server.model.sellable to com.google.gson;
    opens Server.model.chat to com.google.gson;

    opens Server to com.google.gson;
    opens Client.HollowController to com.google.gson;

    opens Client.view to javafx.fxml, javafx.base;
    exports Client.view;
    exports Server;
}