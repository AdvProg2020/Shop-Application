package view.GUI;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
        private static Button loginBTN;
        private static Button accountBTN;
        private static Button cartBTN;
        private static Button manageBTN;
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
        private VBox accountBTNWrapper;
        @FXML
        private VBox cartBTNWrapper;

        static {
            loginBTN = new Button("login");
            loginBTN.setId("login-button");
            loginBTN.setOnAction(event -> LoginPopUpController.display());

            cartBTN = new Button();
            cartBTN.setId("cart-button");
            cartBTN.setOnAction(event -> LoginPopUpController.display());
            //TODO: set the other 3 buttons
        }

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
//            initActions();
//            initTexts();
            cartBTNWrapper.getChildren().removeAll();
            cartBTNWrapper.getChildren().add(cartBTN);
            accountBTNWrapper.getChildren().removeAll();
            accountBTNWrapper.getChildren().add(loginBTN);
        }

//        private void initActions() {
//            logoBTN.setOnAction(e -> setMainPane(Constants.FXMLs.mainMenu));
//            accountBTN.setOnAction(e -> {
//                switch (View.mainController.getType()) {
//                    case Constants.anonymousUserType:
//                        LoginPopUpController.display();
//                    case Constants.customerUserType:
//                        setMainPane(Constants.FXMLs.customerAccountMenu);
//                    case Constants.sellerUserType:
//                        setMainPane(Constants.FXMLs.sellerAccountMenu);
//                    case Constants.adminUserType:
//                        setMainPane(Constants.FXMLs.adminAccountMenu);
//
//                }
//            });
//        }
//
//        private void initTexts() {
//            logoBTN.setText("SSD (TM)");
//            SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(!View.mainController.getType().equals(Constants.anonymousUserType));
//            accountBTN.textProperty().bind(
//                    Bindings.when(isLoggedIn).then("Account Menu")
//                            .otherwise("Login")
//            );
//        }


        private void setBTN(Button btn, String text, EventHandler<ActionEvent> e) {
            btn.setText(text);
            btn.setOnAction(e);
        }

    }
}