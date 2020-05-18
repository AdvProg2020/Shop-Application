import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import model.Category;
import model.database.Database;
import model.database.DatabaseManager;
import view.View;

public class Main {
    public static void main(String[] args) {
        Database mainDatabase = new DatabaseManager();
        mainDatabase.loadDatabase();
        Category.setSuperCategory();
        mainDatabase.createCategory();
        Controller mainController = new Controller(mainDatabase);
        View appView = new View(mainController, new SellerController(mainController), new AdminController(mainController), new CustomerController(mainController));
        appView.start();
    }
}

