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
        new DatabaseManager().loadDatabase();
        Category.setSuperCategory();
        Database mainDatabase = new DatabaseManager();
        Controller mainController = new Controller(mainDatabase);
        View appView = new View(mainController, new SellerController(mainController), new AdminController(mainController), new CustomerController(mainController));
        appView.start();
    }
}

