package view;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Category;
import model.database.Database;
import model.database.DatabaseManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Dana
 * This is the non-implemented sketch of the View
 **/

public class View extends Application {
    static Database databaseManager = new DatabaseManager();
    static Controller mainController = new Controller(databaseManager);
    static CustomerController customerController = new CustomerController(mainController);
    static AdminController adminController = new AdminController(mainController);
    static SellerController sellerController = new SellerController(mainController);
    private static Scanner sc = new Scanner(System.in); //TODO: delete...
    private static Stage mainStage;
    private static Scene mainScene;

    public static void main(String[] args) {
        databaseManager.loadAll();
        Category.setSuperCategory();
        databaseManager.createCategory();

        Menus.init();
        Actions.init();
        new Menus.AccountMenu("account menu");
        launch(args);
    }

    public static String getNextLineTrimmed() { //TODO: delete...
        return sc.nextLine().trim();
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

    public static Scene loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/fxml/" + fxml + ".fxml"));
        return new Scene(fxmlLoader.load());
    }

    @Override
    public void start(Stage stage) throws IOException {
        View.mainStage = stage;
        setScene(loadFxml("MainMenu"));
        stage.setTitle("ShopApplication");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            event.consume();
            close();
        });
        new Menus.MainMenu("first menu").run();
    }

}
