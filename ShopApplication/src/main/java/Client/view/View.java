package Client.view;

import Client.HollowController.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dana
 * This is the non-implemented sketch of the View
 **/

public class View extends Application {
    public static Controller mainController = new Controller();
    public static CustomerController customerController = new CustomerController();
    public static AdminController adminController = new AdminController();
    public static SellerController sellerController = new SellerController();
    public static SupporterController supporterController = new SupporterController();
    public static SimpleIntegerProperty stackSize = new SimpleIntegerProperty(0);
    public static SimpleStringProperty type = new SimpleStringProperty(Constants.anonymousUserType);
    public static SimpleBooleanProperty isManager = new SimpleBooleanProperty(false);
    private static Stage mainStage;
    private static Scene mainScene;
    public static boolean inSale;
    public static boolean inAuction;
    public static String categoryName;
    static ArrayList<String> stackTrace = new ArrayList<>();

    public static void main(String[] args) {
        Controllers.init();
        launch(args);
    }

    public static void addListener(TextField textField, String regex) {
        textField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) return;
            char lastInput = newValue.charAt(newValue.length() - 1);
            if (!String.valueOf(lastInput).matches(regex)) textField.setText(oldValue);
        }));
    }

    public static Scene getScene() {
        return mainScene;
    }

    public static void setScene(Scene scene) {
        mainScene = scene;
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void close() {
        mainStage.close();
    }

    public static Parent loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static <T> T setMainPane(String fxml) {
        FXMLLoader loader = new FXMLLoader(View.class.getResource(getLocation(fxml)));
        Parent p;
        try {
            p = loader.load();
        } catch (IOException e) {
            System.out.println("could not load " + fxml + ".fxml");
            e.printStackTrace();
            return null;
        }

        addToStack(fxml);

        Controllers.BaseController.setMainPane(p);

        return loader.getController();
    }

    public static void addToStack(String fxml) {
        if (stackTrace.size() == 0) {
            stackTrace.add(fxml);
            stackSize.set(stackSize.get() + 1);
        } else if (!stackTrace.get(stackTrace.size() - 1).equals(fxml)) {
            stackTrace.add(fxml);
            stackSize.set(stackSize.get() + 1);
        }
    }

    public static SimpleIntegerProperty getStackSizeProperty() {
        return stackSize;
    }

    public static ArrayList<String> getStackTrace() {
        return stackTrace;
    }

    public static void goBack() {
        stackSize.set(stackSize.get() - 1);
        stackTrace.remove(stackSize.get());
        Parent p;
        String fxml = stackTrace.get(stackSize.get() - 1);
        try {
            if (fxml.equals(Constants.FXMLs.personalInfoMenu)) {
                Controllers.PersonalInfoMenuController.display(null);
            } else if (fxml.equals(Constants.FXMLs.productsMenu)) {
                Controllers.ProductsMenuController.display(categoryName, inSale, inAuction);
            } else {
                p = loadFxml(fxml);
                Controllers.BaseController.setMainPane(p);
            }
        } catch (IOException e) {
            System.out.println("could not load " + fxml + ".fxml");
        }
    }


    public static <T> T popupWindow(String title, String fxml, int width, int height) {
        if (fxml.equals(Constants.FXMLs.adminRegistrationPopup) && !mainController.doesManagerExist()) {
            Stage popup = new Stage();
            popup.setOnCloseRequest(e -> System.exit(-1));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initStyle(StageStyle.UTILITY);
            popup.setTitle("Admin boot-up menu");
            popup.setResizable(false);
            popup.setWidth(1000);
            popup.setHeight(700);
            popup.centerOnScreen();
            FXMLLoader loader = new FXMLLoader(View.class.getResource(getLocation(Constants.FXMLs.adminRegistrationPopup)));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("could not load " + Constants.FXMLs.adminRegistrationPopup + ".fxml");
            }
            popup.setScene(new Scene(parent));
            popup.show();
            return null;
        } else {
            Stage popup = new Stage();
            popup.setTitle(title);
//        popup.setOnCloseRequest(close);
            popup.setResizable(false);
            popup.setWidth(width);
            popup.setHeight(height);
            popup.centerOnScreen();
            FXMLLoader loader = new FXMLLoader(View.class.getResource(getLocation(fxml)));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("could not load " + fxml + ".fxml");
            }
            popup.setScene(new Scene(parent));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.show();
            return loader.getController();
        }

    }

    public static String getLocation(String fxml) {
        return "/fxml/" + fxml + ".fxml";
    }

    @Override
    public void start(Stage stage) throws IOException {

        Sender.getInstance().sendRequest(Constants.Commands.authTokenRequest, "");

        if (!mainController.doesManagerExist()) {
            Controllers.AdminRegistrationPopupController.display();
        } else {
            subStart(stage);
        }

    }

    public static void subStart(Stage stage) {
        View.mainStage = stage;
        stage.setTitle("ShopApplication");
        stage.setOnCloseRequest(event -> {
            event.consume();
            close();
        });

        stage.setMaximized(true);
        stage.setMinWidth(1050);
        stage.setMinHeight(700);
        stage.setWidth(1050);
        stage.setHeight(700);
        stage.centerOnScreen();


        try {
            setScene(new Scene(loadFxml(Constants.FXMLs.base)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMainPane(Constants.FXMLs.mainMenu);

        isManager.bind(
                Bindings.createBooleanBinding(() -> mainController.isManager(), type)
        );
    }
}
