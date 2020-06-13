package view.GUI;

import controller.*;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static class PersonalInfoMenuController implements Initializable{

        @FXML private ImageView ProfileIMG;

        @FXML private Button ProfileIMGEditBTN;

        @FXML private Button logoutBTN;

        @FXML private Button changePasswordBTN;

        @FXML private Button buyLogBTN;

        @FXML private Button discountsBTN;

        @FXML private TextField lastName;

        @FXML private TextField firstName;

        @FXML private Label firstNameLBL;

        @FXML private Label lastNameLBL;

        @FXML private Button IrlNameEditBTN;

        @FXML private TextField phoneNumberField;

        @FXML private TextField EmailField;

        @FXML private TextField balanceField;

        @FXML private TextField storeNameField;

        @FXML private Button phoneNumberEditBTN;

        @FXML private Button emailEditBTN;

        @FXML private Button balanceEditBTN;

        @FXML private Button storeNameEditBTN;

        @FXML private Label storeNameLBL;

        @FXML private Label balanceLBL;

        private String[] personalInfo;

        public static void display() {
            View.setMainPane(Constants.FXMLs.personalInfoMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            try {
                personalInfo = mainController.viewPersonalInfo();
            } catch (Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                return;
            }
            switch (View.type.get()) {
                case Constants.customerUserType:
                    initCustomer();
                    break;
                case Constants.sellerUserType:
                    initSeller();
                    break;
                case Constants.adminUserType:
                    initAdmin();
                    break;
            }
        }

        private void initCustomer() {
            storeNameEditBTN.setVisible(false);
            storeNameField.setVisible(false);
            storeNameLBL.setVisible(false);

            initCustomerValues();
            initCustomerActions();
        }

        private void initCustomerValues() {
            initAdminValues();
        }

        private void initCustomerActions() {

        }

        private void initSeller() {
            discountsBTN.setVisible(false);
            buyLogBTN.setVisible(false);

            initSellerValues();
            initSellerActions();
        }

        private void initSellerValues() {
            initCustomerValues();
        }

        private void initSellerActions() {

        }

        private void initAdmin() {
            storeNameEditBTN.setVisible(false);
            storeNameField.setVisible(false);
            storeNameLBL.setVisible(false);

            balanceEditBTN.setVisible(false);
            balanceField.setVisible(false);
            balanceLBL.setVisible(false);

            discountsBTN.setVisible(false);
            buyLogBTN.setVisible(false);

            initAdminValues();
            initAdminActions();
        }

        private void initAdminValues() {

        }

        private void initAdminActions() {

        }

        private void showPersonalInfo(String[] info) {
            System.out.println("1. username: " + info[0]);
            System.out.println("2. type: " + info[1]);
            System.out.println("3. first name: " + info[2]);
            System.out.println("4. last name: " + info[3]);
            System.out.println("5. email: " + info[4]);
            System.out.println("6. phone number: " + info[5]);
            if (info.length > 6) {
                System.out.println("7. balance: " + info[6]);
            }
            if (info.length > 7) {
                System.out.println("8. store name: " + info[7]);
            }
        }
    }

    public static class MainMenu {
        private static void display() {
            View.setMainPane(Constants.FXMLs.mainMenu);
        }
    }

    public static class ProductsMenuController {
        public static void display(ArrayList<String[]> products) {

        }
    }

    public static class ProductDetailMenu {
        public static void display(String[] subProduct) {

        }
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
       public static void display() {

       }
    }

    public static class LoginPopUpController implements Initializable {
        private static Stage popUpStage;

        @FXML
        private ImageView usernameIcon;

        @FXML
        private TextField usernameField;

        @FXML
        private ImageView passwordIcon;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Label errorLBL;

        @FXML
        private Button loginBTN;

        @FXML
        private Hyperlink registerLink;

        public static void display(Stage stage) {
            popUpStage = stage;
            popUpStage.setWidth(480);
            popUpStage.setHeight(320);
            popUpStage.setResizable(false);
            try {
                popUpStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.loginPopUp)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                popUpStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception e) {}
            popUpStage.show();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            errorLBL.setText("");
            initListeners();
            initActions();
        }

        private void initListeners() {
            usernameField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W"))  usernameField.setText(oldValue);
            }));
            passwordField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W"))  passwordField.setText(oldValue);
            }));
        }

        private void initActions() {
            loginBTN.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username == null || password == null) return;
                else {
                    try {
                        mainController.login(username, password);
                        View.type.set(mainController.getType());
                        popUpStage.close();
                    } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                        errorLBL.setText("invalid username or password");
                        errorLBL.setTextFill(Color.RED);
                        ex.printStackTrace();
                    }
                }
            });
            registerLink.setOnAction(e -> RegisterPopUpController.display(popUpStage));
        }
    }

    public static class RegisterPopUpController implements Initializable {
        private static Stage popUpStage;

        @FXML private TextField customerFirstName;

        @FXML private Label customerFirstNameError;

        @FXML private TextField customerPhoneNumber;

        @FXML private Label customerPhoneNumberError;

        @FXML private TextField customerBalance;

        @FXML private Label customerBalanceError;

        @FXML private TextField customerLastName;

        @FXML private Label customerLastNameError;

        @FXML private TextField customerEmail;

        @FXML private Label customerEmailError;

        @FXML private TextField customerUsername;

        @FXML private Label customerUsernameError;

        @FXML private PasswordField customerPassword;

        @FXML private Label customerPasswordError;

        @FXML private Button customerRegister;

        @FXML private TextField sellerFirstName;

        @FXML private Label sellerFirstNameError;

        @FXML private TextField sellerPhoneNumber;

        @FXML private Label sellerPhoneNumberError;

        @FXML private TextField sellerBalance;

        @FXML private Label sellerBalanceError;

        @FXML private TextField sellerLastName;

        @FXML private Label sellerLastNameError;

        @FXML private TextField sellerEmail;

        @FXML private Label sellerEmailError;

        @FXML private TextField sellerStoreName;

        @FXML private Label sellerStoreNameError;

        @FXML private TextField sellerUsername;

        @FXML private Label sellerUsernameError;

        @FXML private PasswordField sellerPassword;

        @FXML private Label sellerPasswordError;

        @FXML private Button sellerRegister;

        public static void display(Stage stage) {
            popUpStage = stage;
            popUpStage.setHeight(390);
            try {
                popUpStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.registerPopUp)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTexts();
            initVisibilities();
            initListeners();
            initActions();
        }

        private void initTexts() {
            sellerUsernameError.setText("Please enter a username");
            customerPasswordError.setText("Please enter a password");
            sellerPasswordError.setText("Please enter a password");
            customerFirstNameError.setText("Invalid entry!");
            sellerFirstNameError.setText("Invalid entry!");
            customerLastNameError.setText("Invalid entry!");
            sellerLastNameError.setText("Invalid entry!");
            customerPhoneNumberError.setText("Please enter a phone number");
            sellerPhoneNumberError.setText("Please enter a phone number");
            customerEmailError.setText("Invalid entry! enter a valid email address");
            sellerEmailError.setText("Please enter an email address");
            customerBalanceError.setText("Please enter your initial balance");
            sellerBalanceError.setText("Please enter your initial balance");
        }

        private void initVisibilities() {
            customerUsernameError.setVisible(false);
            sellerUsernameError.setVisible(false);
            customerPasswordError.setVisible(false);
            sellerPasswordError.setVisible(false);
            customerFirstNameError.setVisible(false);
            sellerFirstNameError.setVisible(false);
            customerLastNameError.setVisible(false);
            sellerLastNameError.setVisible(false);
            customerPhoneNumberError.setVisible(false);
            sellerPhoneNumberError.setVisible(false);
            customerEmailError.setVisible(false);
            sellerEmailError.setVisible(false);
            customerBalanceError.setVisible(false);
            sellerBalanceError.setVisible(false);
        }

        private void initListeners() {
            addListener(customerUsername, "\\w");
            addListener(sellerUsername, "\\w");
            addListener(customerPassword, "\\w");
            addListener(sellerPassword, "\\w");
            addListener(customerPhoneNumber, "[0-9]");
            addListener(sellerPhoneNumber, "[0-9]");
        }

        private void addListener(TextField textField, String regex) {
            textField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if ( ! String.valueOf(lastInput).matches(regex))  textField.setText(oldValue);
            }));
        }

        private void initActions() {
            customerRegister.setOnAction(e -> {
                if (areCustomerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.customerUserType, customerUsername.getText(),
                                customerPassword.getText(), customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhoneNumber.getText(), Double.valueOf(customerBalance.getText()), null);
                        LoginPopUpController.display(popUpStage);
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        customerUsernameError.setText("sorry! username already taken");
                        customerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
            sellerRegister.setOnAction(e -> {
                if (areSellerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.sellerUserType, sellerUsername.getText(),
                                sellerPassword.getText(), sellerFirstName.getText(), sellerLastName.getText(),
                                sellerEmail.getText(), sellerPhoneNumber.getText(), Double.valueOf(sellerBalance.getText()), null);
                        LoginPopUpController.display(popUpStage);
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        sellerUsernameError.setText("sorry! username already taken");
                        sellerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
        }

        private boolean areCustomerFieldsAvailable() {
            boolean areAvailable = true;
            if (customerUsername.getText().equals("")) {
                customerUsernameError.setText("Please enter a username");
                customerUsernameError.setVisible(true);
                areAvailable = false;
            } else customerUsernameError.setVisible(false);
            if (customerPassword.getText().equals("")) {customerPasswordError.setVisible(true); areAvailable = false;}
            else customerPasswordError.setVisible(false);
            if ( ! customerFirstName.getText().matches(Constants.IRLNamePattern)) {customerFirstNameError.setVisible(true); areAvailable = false;}
            else customerFirstNameError.setVisible(false);
            if ( ! customerLastName.getText().matches(Constants.IRLNamePattern)) {customerLastNameError.setVisible(true); areAvailable = false;}
            else customerLastNameError.setVisible(false);
            if (customerPhoneNumber.getText().equals("")){customerPhoneNumberError.setVisible(true); areAvailable = false;}
            else customerPhoneNumberError.setVisible(false);
            if ( ! customerEmail.getText().matches(Constants.emailPattern)) {customerEmailError.setVisible(true); areAvailable = false;}
            else customerEmailError.setVisible(false);
            if ( ! customerBalance.getText().matches(Constants.doublePattern)) {customerBalanceError.setVisible(true); areAvailable = false;}
            else customerBalanceError.setVisible(false);
            return areAvailable;
        }


        private boolean areSellerFieldsAvailable() {
            boolean areAvailable = true;
            if (sellerUsername.getText().equals("")) {
                sellerUsernameError.setText("Please enter a username");
                sellerUsernameError.setVisible(true);
                areAvailable = false;
            } else sellerUsernameError.setVisible(false);
            if (sellerPassword.getText().equals("")) {sellerPasswordError.setVisible(true); areAvailable = false;}
            else sellerPasswordError.setVisible(false);
            if ( ! sellerFirstName.getText().matches(Constants.IRLNamePattern)) {sellerFirstNameError.setVisible(true); areAvailable = false;}
            else sellerFirstNameError.setVisible(false);
            if ( ! sellerLastName.getText().matches(Constants.IRLNamePattern)) {sellerLastNameError.setVisible(true); areAvailable = false;}
            else sellerLastNameError.setVisible(false);
            if (sellerPhoneNumber.getText().equals("")){sellerPhoneNumberError.setVisible(true); areAvailable = false;}
            else sellerPhoneNumberError.setVisible(false);
            if ( ! sellerEmail.getText().matches(Constants.emailPattern)) {sellerEmailError.setVisible(true); areAvailable = false;}
            else sellerEmailError.setVisible(false);
            if ( ! sellerBalance.getText().matches(Constants.doublePattern)) {sellerBalanceError.setVisible(true); areAvailable = false;}
            else sellerBalanceError.setVisible(false);
            return areAvailable;
        }
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


    public static class SellerSalesMenu {

    }

    public static class SellerProductMenu {

    }

    //add product detail menu
    public static class ShoppingCartMenu implements Initializable {
        @FXML
        private TableView<String> productsTable;

        public static void display() {
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            productsTable.addEventFilter(ScrollEvent.ANY, event -> {
                if (event.getDeltaX() != 0)
                    event.consume();
            });
        }
    }

    //TODO: can be added to CustomerMenu??
    public static class CustomerOrderLogMenu {

    }

    public static class AdminManagingMenuController implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerManagingMenuController implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
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

        public static void setMainPane(Parent parent) {
            currentBase.mainPane.setCenter(parent);
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
            View.type.set(mainController.getType());
            cartBTN.visibleProperty().bind(
                    Bindings.when(View.type.isEqualTo(Constants.adminUserType).or(View.type.isEqualTo(Constants.sellerUserType)))
                    .then(false).otherwise(true)
            );
            manageBTN.visibleProperty().bind(cartBTN.visibleProperty().not());
            loginBTN.visibleProperty().bind(
                    Bindings.when(View.type.isEqualTo(Constants.anonymousUserType))
                            .then(true).otherwise(false)
            );
            accountBTN.visibleProperty().bind(loginBTN.visibleProperty().not());
            backBTN.visibleProperty().bind(View.getStackSizeProperty().greaterThan(1));
        }

        private void initActions() {
            logoBTN.setOnAction(e -> MainMenu.display());
            accountBTN.setOnAction(e -> PersonalInfoMenuController.display());
            loginBTN.setOnAction(e -> LoginPopUpController.display(new Stage()));
            cartBTN.setOnAction(e -> ShoppingCartMenu.display());
            searchBTN.setOnAction(e -> search(searchField.getText()));
            manageBTN.setOnAction(e -> {
                switch (mainController.getType()) {
                    case Constants.adminUserType:
                        AdminManagingMenuController.display();
                    case Constants.sellerUserType:
                        SellerManagingMenuController.display();
                }
            });
            backBTN.setOnAction(e -> {
                if (View.getStackTrace().size() < 2) return;
                else {
                    View.goBack();
                }
            });
        }

        private void initTexts() {
            accountBTN.textProperty().bind(
                    Bindings.createObjectBinding(() -> {
                        try {
                            String username = mainController.viewPersonalInfo()[0];
                            return username;
                        } catch (Exceptions.NotLoggedInException e) {
                            return null;
                        }
                    }, View.type)
            );
            //TODO: temporary
            loginBTN.setText("Login");
            manageBTN.setText("manage");
        }

        private void search(String input) {
            if (input != null) {
                ArrayList<String[]> products = getCurrentProducts();
                if (products != null) {
                    try {
                        ProductsMenuController.display(mainController.showProducts(getProductIDs(products),
                                null, false, new String[]{"false", "0", "0", input, null, null, "0"},
                                new HashMap<>()));
                    } catch (Exceptions.InvalidProductIdException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //search utils.
        private ArrayList<String[]> getCurrentProducts() {
            try {
                return (ArrayList<String[]>) mainController.getProductsOfThisCategory(Constants.SUPER_CATEGORY_NAME).clone();
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private ArrayList<String> getProductIDs(ArrayList<String[]> currentProducts) {
            ArrayList<String> productIDS = new ArrayList<>();
            for (String[] product : currentProducts) {
                productIDS.add(product[0]);
            }
            return productIDS;
        }
    }
}