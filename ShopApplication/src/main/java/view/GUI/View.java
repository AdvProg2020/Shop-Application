package view.GUI;

import controller.*;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Category;
import model.database.Database;
import model.database.DatabaseManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dana
 * This is the non-implemented sketch of the View
 **/

public class View extends Application {
    public static Database databaseManager = new DatabaseManager();
    public static Controller mainController = new Controller(databaseManager);
    public static CustomerController customerController = new CustomerController(mainController);
    public static AdminController adminController = new AdminController(mainController);
    public static SellerController sellerController = new SellerController(mainController);
    private static Stage mainStage;
    private static Scene mainScene;

    private static ArrayList<String> stackTrace = new ArrayList<>();
    public static SimpleIntegerProperty stackSize = new SimpleIntegerProperty(0);
    public static SimpleStringProperty type = new SimpleStringProperty(Constants.anonymousUserType);

    public static void main(String[] args) {
        databaseManager.loadAll();
        Category.setSuperCategory();
        databaseManager.createCategory();
        Controllers.init();
        launch(args);
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
        databaseManager.updateAll();
        mainStage.close();
    }

    public static Parent loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setMainPane(String fxml) {
        Parent p;
        try {
            p = loadFxml(fxml);
        } catch (IOException e) {
            System.out.println("could not load " + fxml + ".fxml");
            return;
        }
        if (stackTrace.size() == 0) {
            stackTrace.add(fxml);
            stackSize.set(stackSize.get() + 1);
        } else if ( ! stackTrace.get(stackTrace.size() - 1).equals(fxml)) {
            stackTrace.add(fxml);
            stackSize.set(stackSize.get() + 1);
        }
        Controllers.BaseController.setMainPane(p);
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
            p = loadFxml(fxml);
        } catch (IOException e) {
            System.out.println("could not load " + fxml + ".fxml");
            return;
        }
        Controllers.BaseController.setMainPane(p);
    }

    @Override
    public void start(Stage stage) throws IOException {

        View.mainStage = stage;
        stage.setTitle("ShopApplication");
//        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            event.consume();
            close();
        });
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        setScene(new Scene(loadFxml(Constants.FXMLs.base)));
        setMainPane(Constants.FXMLs.mainMenu);
//        try {
//            mainController.login("adana", "1");
//            type.set(Constants.adminUserType);
//        } catch (Exceptions.WrongPasswordException | Exceptions.UsernameDoesntExistException  e) {
//            e.printStackTrace();
//        }
    }
}
