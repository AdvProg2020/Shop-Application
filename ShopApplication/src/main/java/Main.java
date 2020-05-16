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

        View appView = new View(new Controller(), new SellerController(), new AdminController(), new CustomerController());
        appView.start();
    }
}

