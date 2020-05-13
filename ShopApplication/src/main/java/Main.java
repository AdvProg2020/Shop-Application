import controller.*;
import view.View;

public class Main {
    public static void main(String[] args) {
        View appView = new View(new Controller(), new SellerController(), new AdminController(), new CustomerController());
        appView.start();
    }
}

