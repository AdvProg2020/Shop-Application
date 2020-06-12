package view.GUI;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

//TODO: purchase menu

public class Controllers {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;

    public static void init() {
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
    }

    public static class AccountMenu {

    }

    public static class MainMenu {

    }

    public static class AllProductsMenu {

    }

    public static class ProductDetailMenu {
    }

    //TODO: deprecated: added in product detail menu
    public static class ProductReviewMenu {

    }

    //TODO: deprecated: added in products menu and sale menu
    public static class SortMenu {

    }

    //TODO: deprecated: added in products menu and sale menu
    public static class FilterMenu {

    }

    public static class SaleMenu {

    }

    //TODO: controls loginPopUp
    public static class LoginPopUpController {

        public static void display() {

        }
    }

    public static class AdminAccountMenu {

    }

    public static class AdminUserManagingMenu {

    }

    //TODO: uses products menu but with different properties.
    public static class AdminProductManagingMenu {

    }


    public static class AdminDiscountManagingMenu {

    }

    public static class AdminRequestManagingMenu {

    }

    public static class AdminCategoryManagingMenu {

    }

    public static class SellerAccountMenu {

    }

    public static class SellerSalesMenu {

    }

    public static class SellerProductMenu {

    }

    public static class CustomerAccountMenu {

    }

    //add product detail menu
    public static class ShoppingCartMenu {

    }

    //TODO: can be added to CustomerMenu??
    public static class CustomerOrderLogMenu {

    }

    public static class ProductBoxController implements Initializable {

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }
    }


    public static class BaseController implements Initializable {
        private static BaseController currentBase;

        @FXML
        private BorderPane mainPane;
        @FXML
        private Button backBTN;
        @FXML
        private Button logoBTN;
        @FXML
        private TextField searchField;
        @FXML
        private Button searchBTN;
        @FXML
        private Button accountBTN;
        @FXML
        private Button loginBTN;
        @FXML
        private Button cartBTN;
        @FXML
        private Button manageBTN;

        public static BaseController getCurrentBase() {
            return currentBase;
        }

        public static void setMainPane(String fxml) {
            currentBase.mainPane.setCenter(View.loadFxml(fxml));
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            currentBase = this;
            initButtons();

        }

        private void initButtons() {
            initActions();
            initTexts();
            initVisibility();
        }

        private void initVisibility() {
            boolean cartBTNVisible = true;
            boolean loginBTNVisible = true;

            switch (View.mainController.getType()) {
                case Constants.customerUserType:
                    loginBTNVisible = false;
                    break;
                case Constants.sellerUserType:
                case Constants.adminUserType:
                    cartBTNVisible = false;
                    loginBTNVisible = false;
                    break;
            }

            cartBTN.setVisible(cartBTNVisible);
            manageBTN.setVisible(!cartBTNVisible);
            loginBTN.setVisible(loginBTNVisible);
            accountBTN.setVisible(!loginBTNVisible);
        }

        private void initActions() {
            logoBTN.setOnAction(e -> setMainPane(Constants.FXMLs.mainMenu));
            accountBTN.setOnAction(e -> setMainPane(Constants.FXMLs.customerAccountMenu));
            loginBTN.setOnAction(e -> LoginPopUpController.display());
            //TODO : search, cart, manage
        }

        private void initTexts() {
            // TODO: bind accountBTN text to <username>
            SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(!View.mainController.getType().equals(Constants.anonymousUserType));
            accountBTN.textProperty().bind(
                    Bindings.when(isLoggedIn).then("Account Menu")
                            .otherwise("Login")
            );
        }
    }
}