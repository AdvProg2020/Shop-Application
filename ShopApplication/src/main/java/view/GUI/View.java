package view.GUI;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    public static Database databaseManager = new DatabaseManager();
    public static Controller mainController = new Controller(databaseManager);
    public static CustomerController customerController = new CustomerController(mainController);
    public static AdminController adminController = new AdminController(mainController);
    public static SellerController sellerController = new SellerController(mainController);
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
        System.out.println("salam");
    }

    public static void close() {
        databaseManager.updateAll();
        mainStage.close();
    }

    public static Parent loadFxml(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/fxml/" + fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(Stage stage) {
        View.mainStage = stage;
        stage.setTitle("ShopApplication");
//        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            event.consume();
            close();
        });
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        setScene(new Scene(loadFxml("Base")));
        //setScene(new Scene(new VBox()));
        BaseController.setMainPane("MainMenu");
        //new Menus.MainMenu("first menu").run();
    }

    public static void setFXML(String name) {
        try {
            View.setScene(new Scene(FXMLLoader.load(View.class.getResource( "/fxml/" + name))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
