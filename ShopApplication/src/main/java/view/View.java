package view;

import controller.*;

import java.util.Scanner;

/**
 * @author Dana
 * This is the non-implemented sketch of the View
 **/

public class View {
    static Controller mainController;
    static CustomerController customerController;
    static AdminController adminController;
    static SellerController sellerController;
    private static Scanner sc = new Scanner(System.in);

    private Menus.FirstMenu firstMenu;

    public View(Controller mainController, SellerController sellerController, AdminController adminController, CustomerController customerController) {
        this.mainController = mainController;
        this.sellerController = sellerController;
        this.customerController = customerController;
        this.adminController = adminController;
        Menus.init();
        Actionsss.init();
        new Menus.ProductDetailMenu("product detail menu");
        new Menus.AccountMenu("account menu");
        firstMenu = new Menus.FirstMenu("first menu");

    }

    public static String getNextLineTrimmed() {
        return sc.nextLine().trim();
    }

    public void start() {
        firstMenu.run();
    }
}
