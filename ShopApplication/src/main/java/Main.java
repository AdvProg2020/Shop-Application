import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import model.Category;
import model.database.DatabaseManager;
import view.View;

public class Main {
    public static void main(String[] args) {
        new DatabaseManager().loadDatabase();
        Category.setSuperCategory();

        View appView = new View(new Controller(), new SellerController(), new AdminController(), new CustomerController());
        appView.start();
    }
}

