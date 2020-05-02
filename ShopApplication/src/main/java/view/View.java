package view;

import controller.*;

/**
*  @author Dana
 *  This is the non-implemented sketch of the View
**/

public class View {
    private Controller mainController;
    private CustomerController customerController;
    private AdminController adminController;
    private SellerController sellerController;

    private Menus.AccountMenu accountMenu;
    private Menus.FirstMenu firstMenu;


    //Todo: create Menus
    public View(Controller mainController, SellerController sellerController, AdminController adminController, CustomerController customerController) {
        this.mainController = mainController;
        this.sellerController = sellerController;
        this.customerController = customerController;
        this.adminController = adminController;
    }

    public void start(){}
}
