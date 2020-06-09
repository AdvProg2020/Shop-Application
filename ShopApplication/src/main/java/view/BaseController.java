package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    private static BaseController currentBase;
    @FXML
    private BorderPane mainPane;

    public static void setMainPane(String fxml) {
        currentBase.mainPane.setCenter(View.loadFxml(fxml));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentBase = this;
    }
}
