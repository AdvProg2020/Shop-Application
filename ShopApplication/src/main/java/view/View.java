package view;

import controller.*;

import java.util.Scanner;

/**
*  @author Dana
 *  This is the non-implemented sketch of the View
**/

public class View {
    public static Controller mainController;
    public static CustomerController customerController;
    public static AdminController adminController;
    public static SellerController sellerController;
    private static Scanner sc = new Scanner(System.in);

    private Menus.ProductDetailMenu productDetailMenu;
    private Menus.AccountMenu accountMenu;
    private Menus.FirstMenu firstMenu;


    public View(Controller mainController, SellerController sellerController, AdminController adminController, CustomerController customerController) {
        this.mainController = mainController;
        this.sellerController = sellerController;
        this.customerController = customerController;
        this.adminController = adminController;
        Menus.init();
        Actions.init();
        productDetailMenu = new Menus.ProductDetailMenu("product detail menu");
        accountMenu = new Menus.AccountMenu("account menu");
        firstMenu = new Menus.FirstMenu("first menu");

    }

    public static  String getNextLineTrimmed() {
        return sc.nextLine().trim();
    }

    public void start(){
        firstMenu.run();
    }
}
