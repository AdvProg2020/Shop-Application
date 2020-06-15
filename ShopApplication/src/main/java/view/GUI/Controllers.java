package view.GUI;

import controller.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    public static class ProductBoxController {

        @FXML
        private Label sale;

        @FXML
        private ImageView image;

        @FXML
        private Label name;

        @FXML
        private Label rating;

        @FXML
        private Label priceBefore;

        @FXML
        private Label priceAfter;

        private String[] subProduct;

        public static Parent createBox(String[] subProduct) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.productBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                ProductBoxController pbc = loader.getController();
                pbc.setInfo(subProduct);
                pbc.setAction(p);
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setInfo(String[] subProductInfo) {
            subProduct = subProductInfo;
        }

        private void setAction(Parent p) {
            p.setOnMouseClicked(e -> ProductDetailMenu.display(subProduct));
        }
    }

    public static class PersonalInfoMenuController implements Initializable {

        @FXML
        private ImageView ProfileIMG;

        @FXML
        private Button ProfileIMGEditBTN;

        @FXML
        private Button logoutBTN;

        @FXML
        private Button changePasswordBTN;

        @FXML
        private Button buyLogBTN;

        @FXML
        private Button discountsBTN;

        @FXML
        private TextField lastName;

        @FXML
        private TextField firstName;

        @FXML
        private Label firstNameLBL;

        @FXML
        private Label lastNameLBL;

        @FXML
        private Button IrlNameEditBTN;

        @FXML
        private TextField phoneNumberField;

        @FXML
        private TextField EmailField;

        @FXML
        private TextField balanceField;

        @FXML
        private TextField storeNameField;

        @FXML
        private Button phoneNumberEditBTN;

        @FXML
        private Button emailEditBTN;

        @FXML
        private Button balanceEditBTN;

        @FXML
        private Button storeNameEditBTN;

        @FXML
        private Label storeNameLBL;

        @FXML
        private Label balanceLBL;

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
            View.getStackTrace().clear();
            View.stackSize.set(0);
            View.setMainPane(Constants.FXMLs.mainMenu);
        }
    }

    public static class ProductsMenuController implements Initializable {


        @FXML
        private ChoiceBox<String> sortBy;

        @FXML
        private ToggleButton isIncreasing;

        @FXML
        private ToggleGroup increasingToggleGroup;

        @FXML
        private ToggleButton isDecreasing;

        @FXML
        private CheckBox available;

        @FXML
        private Slider minPriceSlider;

        @FXML
        private Slider maxPriceSlider;

        @FXML
        private TextField filterName;

        @FXML
        private ChoiceBox<String> filterBrand;

        @FXML
        private ChoiceBox<String> filterSeller;

        @FXML
        private ChoiceBox<String> filterCategory;

        @FXML
        private ScrollPane productsPane;

        public static ArrayList<String[]> products;

        public static void display(ArrayList<String[]> products) {
            ProductsMenuController.products = products;
            View.setMainPane(Constants.FXMLs.productsMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initChoiceBoxes();
            initActions();
        }

        private void initChoiceBoxes() {
            ArrayList<String> brands = (ArrayList<String>) products.stream().map(p -> p[2]).collect(Collectors.toList());
            HashSet<String> b = new HashSet<>(brands);
            filterBrand.setItems(FXCollections.observableArrayList(b));


        }

        private void initActions() {

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

    public static class LoginPopupController implements Initializable {
        private static Stage PopupStage;

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
            PopupStage = stage;
            PopupStage.setWidth(480);
            PopupStage.setHeight(320);
            PopupStage.setResizable(false);
            try {
                PopupStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.loginPopup)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                PopupStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception e) {
            }
            PopupStage.show();
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
                if (String.valueOf(lastInput).matches("\\W")) usernameField.setText(oldValue);
            }));
            passwordField.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (String.valueOf(lastInput).matches("\\W")) passwordField.setText(oldValue);
            }));
        }

        private void initActions() {
            loginBTN.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username == null || password == null) return;

                try {
                    mainController.login(username, password);
                    View.type.set(mainController.getType());
                    PopupStage.close();
                } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                    errorLBL.setText("invalid username or password");
                    errorLBL.setTextFill(Color.RED);
                    ex.printStackTrace();
                }
            });
            registerLink.setOnAction(e -> RegisterPopupController.display(PopupStage));
        }
    }

    public static class RegisterPopupController implements Initializable {
        private static Stage PopupStage;

        @FXML
        private Hyperlink customerLoginHL;

        @FXML
        private Hyperlink sellerLoginHL;

        @FXML
        private TextField customerFirstName;

        @FXML
        private Label customerFirstNameError;

        @FXML
        private TextField customerPhoneNumber;

        @FXML
        private Label customerPhoneNumberError;

        @FXML
        private TextField customerBalance;

        @FXML
        private Label customerBalanceError;

        @FXML
        private TextField customerLastName;

        @FXML
        private Label customerLastNameError;

        @FXML
        private TextField customerEmail;

        @FXML
        private Label customerEmailError;

        @FXML
        private TextField customerUsername;

        @FXML
        private Label customerUsernameError;

        @FXML
        private PasswordField customerPassword;

        @FXML
        private Label customerPasswordError;

        @FXML
        private Button customerRegister;

        @FXML
        private TextField sellerFirstName;

        @FXML
        private Label sellerFirstNameError;

        @FXML
        private TextField sellerPhoneNumber;

        @FXML
        private Label sellerPhoneNumberError;

        @FXML
        private TextField sellerBalance;

        @FXML
        private Label sellerBalanceError;

        @FXML
        private TextField sellerLastName;

        @FXML
        private Label sellerLastNameError;

        @FXML
        private TextField sellerEmail;

        @FXML
        private Label sellerEmailError;

        @FXML
        private TextField sellerStoreName;

        @FXML
        private Label sellerStoreNameError;

        @FXML
        private TextField sellerUsername;

        @FXML
        private Label sellerUsernameError;

        @FXML
        private PasswordField sellerPassword;

        @FXML
        private Label sellerPasswordError;

        @FXML
        private Button sellerRegister;

        public static void display(Stage stage) {
            PopupStage = stage;
            PopupStage.setHeight(390);
            try {
                PopupStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.registerPopup)));
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
                if (!String.valueOf(lastInput).matches(regex)) textField.setText(oldValue);
            }));
        }

        private void initActions() {
            customerRegister.setOnAction(e -> {
                if (areCustomerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.customerUserType, customerUsername.getText(),
                                customerPassword.getText(), customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhoneNumber.getText(), Double.valueOf(customerBalance.getText()), null);
                        LoginPopupController.display(PopupStage);
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
                        LoginPopupController.display(PopupStage);
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        sellerUsernameError.setText("sorry! username already taken");
                        sellerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
            sellerLoginHL.setOnAction(e -> LoginPopupController.display(PopupStage));
            customerLoginHL.setOnAction(e -> LoginPopupController.display(PopupStage));
        }

        private boolean areCustomerFieldsAvailable() {
            boolean areAvailable = true;
            if (customerUsername.getText().equals("")) {
                customerUsernameError.setText("Please enter a username");
                customerUsernameError.setVisible(true);
                areAvailable = false;
            } else customerUsernameError.setVisible(false);
            if (customerPassword.getText().equals("")) {
                customerPasswordError.setVisible(true);
                areAvailable = false;
            } else customerPasswordError.setVisible(false);
            if (!customerFirstName.getText().matches(Constants.IRLNamePattern)) {
                customerFirstNameError.setVisible(true);
                areAvailable = false;
            } else customerFirstNameError.setVisible(false);
            if (!customerLastName.getText().matches(Constants.IRLNamePattern)) {
                customerLastNameError.setVisible(true);
                areAvailable = false;
            } else customerLastNameError.setVisible(false);
            if (customerPhoneNumber.getText().equals("")) {
                customerPhoneNumberError.setVisible(true);
                areAvailable = false;
            } else customerPhoneNumberError.setVisible(false);
            if (!customerEmail.getText().matches(Constants.emailPattern)) {
                customerEmailError.setVisible(true);
                areAvailable = false;
            } else customerEmailError.setVisible(false);
            if (!customerBalance.getText().matches(Constants.doublePattern)) {
                customerBalanceError.setVisible(true);
                areAvailable = false;
            } else customerBalanceError.setVisible(false);
            return areAvailable;
        }


        private boolean areSellerFieldsAvailable() {
            boolean areAvailable = true;
            if (sellerUsername.getText().equals("")) {
                sellerUsernameError.setText("Please enter a username");
                sellerUsernameError.setVisible(true);
                areAvailable = false;
            } else sellerUsernameError.setVisible(false);
            if (sellerPassword.getText().equals("")) {
                sellerPasswordError.setVisible(true);
                areAvailable = false;
            } else sellerPasswordError.setVisible(false);
            if (!sellerFirstName.getText().matches(Constants.IRLNamePattern)) {
                sellerFirstNameError.setVisible(true);
                areAvailable = false;
            } else sellerFirstNameError.setVisible(false);
            if (!sellerLastName.getText().matches(Constants.IRLNamePattern)) {
                sellerLastNameError.setVisible(true);
                areAvailable = false;
            } else sellerLastNameError.setVisible(false);
            if (sellerPhoneNumber.getText().equals("")) {
                sellerPhoneNumberError.setVisible(true);
                areAvailable = false;
            } else sellerPhoneNumberError.setVisible(false);
            if (!sellerEmail.getText().matches(Constants.emailPattern)) {
                sellerEmailError.setVisible(true);
                areAvailable = false;
            } else sellerEmailError.setVisible(false);
            if (!sellerBalance.getText().matches(Constants.doublePattern)) {
                sellerBalanceError.setVisible(true);
                areAvailable = false;
            } else sellerBalanceError.setVisible(false);
            return areAvailable;
        }
    }



    public static class AdminProductManagingMenu {
        public static void display() {
            View.setMainPane(Constants.FXMLs.adminProductManagingMenu);
        }
    }

    public static class SellerLogsMenu {

    }


    public static class AdminDiscountManagingMenu implements Initializable {
        private static ArrayList<String> allDiscounts = new ArrayList<>();
        private static ArrayList<DiscountWrapper> allDiscountWrappers = new ArrayList<>();
        @FXML
        private TableView<DiscountWrapper> discounts;

        @FXML
        private TableColumn<DiscountWrapper, String> idCol;

        @FXML
        private TableColumn<DiscountWrapper, String> codeCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> percentageCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> startDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> endDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addDiscountBTN;

        public static void display() {
            allDiscounts = adminController.viewDiscountCodes();
            View.setMainPane(Constants.FXMLs.adminDiscountManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            addDiscountBTN.setOnAction(e -> AddDiscountPopupController.display(new Stage()));
            initDiscounts();
            initTable();
        }

        private void initDiscounts() {
            allDiscountWrappers.clear();
            for (String discount : allDiscounts) {
                String[] details;
                try {
                    details = adminController.viewDiscountCode(discount);
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                    return;
                }
                allDiscountWrappers.add(new DiscountWrapper(details[0], details[1], details[2], details[3], Double.valueOf(details[5]), Integer.valueOf(details[4])));
            }
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            codeCOL.setCellValueFactory(new PropertyValueFactory<>("code"));
            percentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            startDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("detail"));

            discounts.setItems(FXCollections.observableArrayList(allDiscountWrappers));
        }

        private  class DiscountWrapper {
            String id;
            String code;
            double percentage;
            int maximumUse;
            Button detail = new Button();
            Button remove = new Button();
            String startDate;
            String endDate;

            DiscountWrapper(String id, String code, String startDate, String endDate, double percentage, int maximumUse) {
                this.id = id;
                this.code = code;
                this.percentage = percentage;
                this.maximumUse = maximumUse;
                detail.setOnAction(e -> showDiscountDetails());
                remove.setOnAction(e -> {
                    try {
                        adminController.removeDiscountCode(code);
                    } catch (Exceptions.DiscountCodeException ex) {
                        ex.printStackTrace();
                    }
                });
                detail.getStyleClass().add("detail-button");
                remove.getStyleClass().add("remove-button");
            }

            private void showDiscountDetails() {
                ArrayList<String[]> customersWithCode;
                try {
                    customersWithCode = adminController.peopleWhoHaveThisDiscount(code);
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                    return;
                }

                DiscountPopupController.display(new Stage(), this, customersWithCode);
            }

            public Property percentageProperty() {
                SimpleStringProperty percentageProperty = new SimpleStringProperty();
                percentageProperty.bind(new SimpleStringProperty(String.valueOf(percentage)).concat("%"));
                return percentageProperty;
            }

        }
    }

    public static class DiscountPopupController implements Initializable{



        public static void display(Stage Popup, AdminDiscountManagingMenu.DiscountWrapper discount, ArrayList<String[]> customers) {
            Popup.setTitle("Discount Details");
            Popup.setResizable(false);
            Popup.setWidth(750);
            Popup.setHeight(500);
            Popup.centerOnScreen();

            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.adminDiscountManagingPopup + ".fxml"));
            DiscountPopupController controller = loader.getController();
            //controller.setInfo();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AddDiscountPopupController implements Initializable {
        public static void display(Stage PopupStage) {

        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminRequestManagingMenu {
        public static void display() {
            View.setMainPane(Constants.FXMLs.adminRequestManagingMenu);
        }
    }

    public static class AdminCategoryManagingMenu {


        @FXML
        private TableView<String> categories;

        @FXML
        private TableColumn<?, ?> idCOL;

        @FXML
        private TableColumn<?, ?> nameCOL;

        @FXML
        private TableColumn<?, ?> parentCOL;

        @FXML
        private TableColumn<?, ?> detailsCOL;

        @FXML
        private TableColumn<?, ?> removeCOL;

        @FXML
        private Button addCategoryBTN;

        @FXML
        private Label errorLBL;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminCategoryManagingMenu);
        }
    }


    public static class SellerSalesManagingMenuController implements Initializable {
        private ArrayList<SaleWrapper> sellerSales = new ArrayList<>();

        @FXML
        private TableView<SaleWrapper> sales;

        @FXML
        private TableColumn<SaleWrapper, String> idCol;

        @FXML
        private TableColumn<SaleWrapper, String> percentageCOL;

        @FXML
        private TableColumn<SaleWrapper, String> startDateCOL;

        @FXML
        private TableColumn<SaleWrapper, String> endDateCOL;

        @FXML
        private TableColumn<SaleWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<SaleWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addSaleBTN;

        private class SaleWrapper {
            String id, seller, startDate, endDate;
            double percentage;
            int numOfProducts;
            Button remove = new Button();
            Button details = new Button();

            public SaleWrapper(String[] info) {
                this(info[0], info[1], info[3], info[4], Double.parseDouble(info[2]), Integer.parseInt(info[5]));
            }

            public SaleWrapper(String id, String seller, String startDate, String endDate, double percentage, int numOfProducts) {
                this.id = id;
                this.seller = seller;
                this.startDate = startDate;
                this.endDate = endDate;
                this.percentage = percentage;
                this.numOfProducts = numOfProducts;

                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    //TODO:
                    //sellerController.sale
                    sales.getItems().remove(this);
                });
                details.getStyleClass().add("detail-button");
                //TODO:
                //details.setOnAction(e -> );

            }

            public Property percentageProperty() {
                SimpleStringProperty percentageProperty = new SimpleStringProperty();
                percentageProperty.bind(new SimpleStringProperty(String.valueOf(percentage)).concat("%"));
                return percentageProperty;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerSaleManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            for (String[] sale : sellerController.viewSales()) {
                sellerSales.add(new SaleWrapper(sale));
            }

            initTable();
            initActions();
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            percentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            startDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initActions() {
            //TODO:
            //addSaleBTN.setOnAction(e -> );
        }
    }

    public static class SellerProductManagingMenuController implements Initializable{
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerProductManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerLogMenuController implements Initializable {
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerLogsMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
        }
    }

    //add product detail menu
    public static class ShoppingCartMenuController implements Initializable {
        //TODO decrease/increase buttons.

        private static ArrayList<String[]> cartProducts = new ArrayList<>();
        private static ArrayList<SubProductWrapper> subProducts = new ArrayList<>();
        private SimpleDoubleProperty totalPriceProperty = new SimpleDoubleProperty(0);
        NumberBinding totalPriceBinding = new SimpleDoubleProperty(0).add(0);

        private class SubProductWrapper {
            String id;
            int index;
            Button nameBrandSeller;
            double unitPrice;
            SimpleIntegerProperty countProperty = new SimpleIntegerProperty();
            TextField countField;
            HBox countGroup = new HBox();
            SimpleDoubleProperty totalPrice;
            Button remove;

            public SubProductWrapper(String id, int index, String nameBrandSeller, double unitPrice, int count) {
                this.id = id;
                this.index = index;
                this.nameBrandSeller = new Button(nameBrandSeller);
                this.nameBrandSeller.setOnAction(e -> ProductDetailMenu.display(cartProducts.get(index - 1)));
                this.unitPrice = unitPrice;
                this.countProperty.set(count);
                this.totalPrice.bind(new SimpleDoubleProperty(unitPrice).multiply(countProperty));
                remove = new Button();
                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    try {
                        mainController.removeSubProduct(this.id);
                        productsTable.getItems().remove(this);
                    } catch (Exceptions.InvalidSubProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
            }


            public Button getNameBrandSeller() {
                return nameBrandSeller;
            }

            public double getUnitPrice() {
                return unitPrice;
            }


            public double getTotalPrice() {
                return totalPrice.get();
            }

            public SimpleDoubleProperty getTotalPriceProperty() {
                return totalPrice;
            }
        }

        @FXML
        private TableColumn<SubProductWrapper, Button> removeCOL;

        @FXML
        private TableView<SubProductWrapper> productsTable;

        @FXML
        private TableColumn<SubProductWrapper, String> productName;

        @FXML
        private TableColumn<SubProductWrapper, Double> productUnitPrice;

        @FXML
        private TableColumn<SubProductWrapper, Integer> count;

        @FXML
        private TableColumn<SubProductWrapper, Double> totalPrice;

        @FXML
        private Button clearCartBTN;

        @FXML
        private Label errorLBL;

        @FXML
        private Label totalPriceLBL;

        @FXML
        private Button purchaseBTN;

        public static void display() {
            try {
                cartProducts = mainController.getProductsInCart();
            } catch (Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
                return;
            }
            View.setMainPane(Constants.FXMLs.shoppingCartMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            productsTable.addEventFilter(ScrollEvent.ANY, event -> {
                if (event.getDeltaX() != 0)
                    event.consume();
            });

            iniTable();
            initLabels();
            initActions();
        }

        private void initActions() {
            purchaseBTN.setOnAction(e -> PurchaseMenuController.display());

            clearCartBTN.setOnAction(e -> {
                if (cartProducts.size() == 0) {
                    errorLBL.setText("the cart is already empty!");
                } else {
                    mainController.clearCart();
                }
            });
        }

        private void initLabels() {
            errorLBL.setText("");

            for (SubProductWrapper subProduct : subProducts) {
                if (totalPriceBinding == null) {
                    totalPriceBinding = subProduct.getTotalPriceProperty().add(0);
                } else {
                    totalPriceBinding = totalPriceBinding.add(subProduct.getTotalPriceProperty());
                }
            }
            totalPriceProperty.bind(totalPriceBinding);

            totalPriceLBL.textProperty().bind(totalPriceBinding.asString().concat("$"));
        }

        private void iniTable() {
            initCols();

            //TODO: name-brand(storeName)
            int index = 0;
            for (String[] cartProduct : cartProducts) {
                subProducts.add(new SubProductWrapper(cartProduct[0], ++index,
                        cartProduct[2] + "-" + cartProduct[3] + "(" + cartProduct[5] + ")",
                        Double.valueOf(cartProduct[7]), Integer.valueOf(cartProduct[6])));
            }

            productsTable.setItems(FXCollections.observableArrayList(subProducts));
        }

        private void initCols() {
            productName.setCellValueFactory(new PropertyValueFactory<>("nameBrandSeller"));
            productUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            count.setCellValueFactory(new PropertyValueFactory<>("count"));
            totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }
    }

    public static class PurchaseMenuController {
        public static void display() {
            View.setMainPane(Constants.FXMLs.purchaseMenu);
        }
    }

    //TODO: can be added to CustomerMenu??
    public static class CustomerOrderLogMenu {

    }

    public static class AdminManagingMenuController implements Initializable {
        public static void display() {
            View.setMainPane(Constants.FXMLs.adminManagingMenu);
        }

        @FXML
        private Button manageAccounts;

        @FXML
        private Button manageProducts;

        @FXML
        private Button manageCategories;

        @FXML
        private Button manageDiscounts;

        @FXML
        private Button manageRequests;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initActions();
        }

        private void initActions() {
            manageAccounts.setOnAction(e -> AdminAccountManagingMenuController.display());
            manageCategories.setOnAction(e -> AdminCategoryManagingMenu.display());
            manageDiscounts.setOnAction(e -> AdminDiscountManagingMenu.display());
            manageProducts.setOnAction(e -> AdminProductManagingMenu.display());
            manageRequests.setOnAction(e -> AdminRequestManagingMenu.display());
        }
    }

    public static class AdminAccountManagingMenuController implements Initializable {

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminAccountManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminCategoryManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminDiscountManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminRequestManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminProductManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class AdminAccountManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerSaleManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerProductManagingPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class CategoryPopup implements Initializable {
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }


    public static class SellerManagingMenuController implements Initializable {
        @FXML
        private Button manageProducts;

        @FXML
        private Button manageSales;

        @FXML
        private Button sellLogs;
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initActions();
        }

        private void initActions() {
            manageProducts.setOnAction(e -> SellerProductManagingMenuController.display());
            manageSales.setOnAction(e -> SellerSalesManagingMenuController.display());
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
            loginBTN.setOnAction(e -> LoginPopupController.display(new Stage()));
            cartBTN.setOnAction(e -> ShoppingCartMenuController.display());
            searchBTN.setOnAction(e -> search(searchField.getText()));
            manageBTN.setOnAction(e -> {
                switch (mainController.getType()) {
                    case Constants.adminUserType:
                        AdminManagingMenuController.display();
                        break;
                    case Constants.sellerUserType:
                        SellerManagingMenuController.display();
                }
            });
            backBTN.setOnAction(e -> {
                if (View.getStackTrace().size() < 2) return;

                View.goBack();
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