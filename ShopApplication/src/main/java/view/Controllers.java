package view;

import controller.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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


    public static class EditPersonalInfoPopupController implements Initializable {

        @FXML
        private TextField usernameField;

        @FXML
        private Label usernameError;

        @FXML
        private PasswordField passwordField;

        @FXML
        private TextField showPasswordFIeld;

        @FXML
        private ToggleButton showPasswordBTN;

        @FXML
        private Label passwordError;

        @FXML
        private TextField firstName;

        @FXML
        private Label firstNameError;

        @FXML
        private TextField lastName;

        @FXML
        private Label lastNameError;

        @FXML
        private TextField phoneNumber;

        @FXML
        private Label phoneNumberError;

        @FXML
        private TextField email;

        @FXML
        private Label emailError;

        @FXML
        private TextField balance;

        @FXML
        private Label balanceError;

        @FXML
        private TextField storeName;

        @FXML
        private Label storeNameError;

        @FXML
        private TextField imageField;

        @FXML
        private Button browseBTN;

        @FXML
        private Label ImageError;

        @FXML
        private Button saveBTN;

        @FXML
        private Button discardBTN;

        private String[] info;
        private SimpleBooleanProperty passwordChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty firstNameChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty lastNameChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty imageChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty phoneChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty emailChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty balanceChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty storeNameChanged = new SimpleBooleanProperty(false);

        public static void display() {
            View.popupWindow("Edit Personal Info", Constants.FXMLs.editPersonalInfoPopup, 400, 500);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            try {
                info = mainController.viewPersonalInfo();
            } catch (Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                discardBTN.getScene().getWindow().hide();
            }
            initVisibility();
            initButtons();
            initLabels();
            initValues();
            initBindings();
            initListeners();
            initPasswordStuff();
        }

        private void initVisibility() {
            passwordError.setVisible(false);
            firstNameError.setVisible(false);
            lastNameError.setVisible(false);
            phoneNumberError.setVisible(false);
            emailError.setVisible(false);
            balanceError.setVisible(false);
            storeNameError.setVisible(false);
            imageField.setEditable(false);
            if (View.type.get().equals(Constants.adminUserType)) balance.setVisible(false);
            if (!View.type.get().equals(Constants.sellerUserType)) storeName.setVisible(false);
        }

        private void initButtons() {
            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());

            browseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image file", "*.png"),
                        new FileChooser.ExtensionFilter("Image file", "*.jpg"));
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (fileChooser != null) {
                    imageField.setText(chosenFile.getPath());
                }
            });

            saveBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        if (passwordChanged.get())
                            mainController.editPersonalInfo("password", info[1] = passwordField.getText());
                        if (imageChanged.get())
                            mainController.editPersonalInfo("image path", info[7] = imageField.getText());
                        if (firstNameChanged.get())
                            mainController.editPersonalInfo("firstName", info[3] = firstName.getText());
                        if (lastNameChanged.get())
                            mainController.editPersonalInfo("lastName", info[4] = lastName.getText());
                        if (phoneChanged.get())
                            mainController.editPersonalInfo("phone", info[6] = phoneNumber.getText());
                        if (emailChanged.get()) mainController.editPersonalInfo("email", info[5] = email.getText());
                        if (balanceChanged.get()) {
                            if (View.type.get().equals(Constants.customerUserType)) {
                                customerController.editPersonalInfo("balance", info[8] = balance.getText());
                            } else if (View.type.get().equals(Constants.sellerUserType)) {
                                sellerController.editPersonalInfo("balance", info[8] = balance.getText());
                            }
                        }
                        if (storeNameChanged.get()) {
                            if (View.type.get().equals(Constants.sellerUserType)) {
                                sellerController.editPersonalInfo("storeName", info[9] = storeName.getText());
                            }
                        }
                        PersonalInfoMenuController.current.update();
                        discardBTN.fire();
                    } catch (Exceptions.SameAsPreviousValueException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidFieldException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        private boolean validateFields() {
            boolean valid = true;
            if (passwordField.getText().equals("")) {
                valid = false;
                passwordError.setVisible(true);
            } else passwordError.setVisible(false);

            if (!firstName.getText().matches(Constants.IRLNamePattern)) {
                valid = false;
                firstNameError.setVisible(true);
            } else firstNameError.setVisible(false);

            if (!lastName.getText().matches(Constants.IRLNamePattern)) {
                valid = false;
                lastNameError.setVisible(true);
            } else lastNameError.setVisible(false);

            if (phoneNumber.getText().equals("")) {
                valid = false;
                phoneNumberError.setVisible(true);
            } else phoneNumberError.setVisible(false);

            if (!email.getText().matches(Constants.emailPattern)) {
                valid = false;
                emailError.setVisible(true);
            } else emailError.setVisible(false);

            if (!View.type.get().equals(Constants.adminUserType)) {
                if (!balance.getText().matches(Constants.doublePattern)) {
                    valid = false;
                    balanceError.setVisible(true);
                } else balanceError.setVisible(false);
            }

            if (View.type.get().equals(Constants.sellerUserType)) {
                if (storeName.getText().equals("")) {
                    valid = false;
                    storeNameError.setVisible(true);
                } else storeNameError.setVisible(false);
            }

            return valid;
        }

        private void initLabels() {
            passwordError.setText("Invalid password");
            firstNameError.setText("Invalid first name");
            lastNameError.setText("Invalid last name");
            phoneNumberError.setText("Invalid phone number");
            emailError.setText("Invalid email address");
            balanceError.setText("Invalid balance number");
            storeNameError.setText("Invalid store name");
        }

        private void initValues() {
            passwordField.setText(info[1]);
            imageField.setText(info[7]);
            firstName.setText(info[3]);
            lastName.setText(info[4]);
            phoneNumber.setText(info[6]);
            email.setText(info[5]);
            if (!View.type.get().equals(Constants.adminUserType)) balance.setText(info[8]);
            if (View.type.get().equals(Constants.sellerUserType)) storeName.setText(info[9]);
        }

        private void initBindings() {
            passwordChanged.bind(
                    Bindings.when(passwordField.textProperty().isEqualTo(info[1])).then(false).otherwise(true)
            );
            imageChanged.bind(
                    Bindings.when(imageField.textProperty().isEqualTo(info[7])).then(false).otherwise(true)
            );
            firstNameChanged.bind(
                    Bindings.when(firstName.textProperty().isEqualTo(info[3])).then(false).otherwise(true)
            );
            lastNameChanged.bind(
                    Bindings.when(lastName.textProperty().isEqualTo(info[4])).then(false).otherwise(true)
            );
            phoneChanged.bind(
                    Bindings.when(phoneNumber.textProperty().isEqualTo(info[6])).then(false).otherwise(true)
            );
            emailChanged.bind(
                    Bindings.when(email.textProperty().isEqualTo(info[5])).then(false).otherwise(true)
            );
            if (!View.type.get().equals(Constants.adminUserType))
                balanceChanged.bind(
                        Bindings.when(balance.textProperty().isEqualTo(info[8])).then(false).otherwise(true)
                );
            else balanceChanged.set(false);
            if (View.type.get().equals(Constants.sellerUserType))
                storeNameChanged.bind(
                        Bindings.when(storeName.textProperty().isEqualTo(info[9])).then(false).otherwise(true)
                );
            else storeNameChanged.set(false);

            saveBTN.disableProperty().bind(
                    Bindings.createObjectBinding(() ->
                                    !passwordChanged.get() && !imageChanged.get() && !firstNameChanged.get() && !lastNameChanged.get() &&
                                            !phoneChanged.get() && !emailChanged.get() && !balanceChanged.get() && !storeNameChanged.get()
                            , passwordChanged, firstNameChanged, lastNameChanged, imageChanged, phoneChanged, emailChanged, balanceChanged, storeNameChanged)
            );
        }

        private void initListeners() {
            View.addListener(passwordField, "\\w");
            View.addListener(phoneNumber, "[0-9]");
        }



        private void initPasswordStuff() {
            showPasswordFIeld.textProperty().bind(passwordField.textProperty());
            showPasswordFIeld.setEditable(false);
            showPasswordFIeld.visibleProperty().bind(passwordField.visibleProperty().not());
            passwordField.visibleProperty().bind(showPasswordBTN.selectedProperty().not());
        }
    }

    public static class PersonalInfoMenuController {

        public static PersonalInfoMenuController current;

        @FXML
        private ImageView accountIMG;

        @FXML
        private Button buyLogBTN;

        @FXML
        private Button sellLogBTN;

        @FXML
        private Button editBTN;

        @FXML
        private Button logoutBTN;

        @FXML
        private Label nameLBL;

        @FXML
        private Label usernameLBL;

        @FXML
        private Label phoneProperty;

        @FXML
        private Label phoneValue;

        @FXML
        private Label emailProperty;

        @FXML
        private Label emailValue;

        @FXML
        private Label storeProperty;

        @FXML
        private Label balanceProperty;

        @FXML
        private Label storeValue;

        @FXML
        private Label balanceValue;

        @FXML
        private StackPane additionalInfoStackPane;

        @FXML
        private TabPane discountTABPANE;

        @FXML
        private TableView<DiscountWrapper> customerDiscounts;

        @FXML
        private TableColumn<DiscountWrapper, String> codeCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> discountUntilCOL;

        @FXML
        private TableColumn<String, String> discountPercentageCOL;

        @FXML
        private TabPane requestTABPANE;

        @FXML
        private TableView<RequestWrapper> sellerRequests;

        @FXML
        private TableColumn<RequestWrapper, String> typeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> dateCOL;

        @FXML
        private TableColumn<RequestWrapper, Button> requestDetailsCOL;

        /**
         * edit product
         * edit sale
         * add product
         * add sale
         * add review
         * add seller
         *
         * product detail :
         * add to cart and edit
         * price store name; default ya na.
         *
         * felan: admin edit nadarad.
         *
         * admin: popup if managing va main pane if products menu
         * admin edit darad. price and count ra nemitavanad.
         *
         *
         *
         */
        public class DiscountWrapper {
            String code, endDate;
            String percentage, maximumAmount;

            DiscountWrapper(String code, String endDate, String percentage, String maximumAmount) {
                this.code = code;
                this.percentage = percentage;
                this.maximumAmount = maximumAmount;
                this.endDate = endDate;
            }

            public String getCode() {
                return code;
            }

           public String getPercentageMax() {
                return percentage + " (" + maximumAmount + "$)";
           }

            public String getEndDate() {
                return endDate;
            }
        }

        public class RequestWrapper {
            String id, type, date, status;
            Button details = new Button();

            public RequestWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3]);
            }

            public RequestWrapper(String id, String type, String date, String status) {
                this.id = id;
                this.type = type;
                this.date = date;
                this.status = status;

                initButtons();
            }

            private void initButtons() {
                details.getStyleClass().add("details-button");

                details.setOnAction(e -> {
                    switch (type) {
                        case "AddProductRequest":
                            AddProductRequestPopupController.display(id);
                            break;
                        case "EditProductRequest":
                            EditProductRequestPopupController.display(id);
                            break;
                        case "EditSaleRequest":
                            EditSaleRequestPopupController.display(id);
                            break;
                        case "AddSaleRequest":
                            AddSaleRequestPopupController.display(id);
                            break;
                    }
                });

            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getDate() {
                return date;
            }

            public String getStatus() {
                return status;
            }

            public Button getDetails() {
                return details;
            }
        }


        private String username;
        private String[] info;
        private boolean isPopup;

        public static void display(String username) {
            if (username == null) {
                ((PersonalInfoMenuController) View.setMainPane(Constants.FXMLs.personalInfoMenu)).initialize(null, false);
            } else {
                ((PersonalInfoMenuController)
                        View.popupWindow("Account detail menu", Constants.FXMLs.personalInfoMenu, 632, 472)).initialize(username, true);
            }
        }

        private void initialize(String username, boolean isPopup) {
            this.current = current;
            this.isPopup = isPopup;
            this.username = username;
            try {
                if (username != null)
                    info = mainController.viewPersonalInfo(username);
                else
                    info = mainController.viewPersonalInfo();
            } catch (Exceptions.UsernameDoesntExistException e) {
                e.printStackTrace();
                return;
            } catch (Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                return;
            }

            initVisibilities();
            initActions();
            initTable();
            update();
        }

        private void initVisibilities() {
            String type = info[info.length - 1];
            if (isPopup) {
                editBTN.setVisible(false);
                sellLogBTN.setVisible(false);
                logoutBTN.setVisible(false);
                additionalInfoStackPane.setVisible(false);
            } else {
                if (type.equals(Constants.sellerUserType)) {
                    customerDiscounts.setVisible(false);
                    discountTABPANE.setVisible(false);
                    sellerRequests.setVisible(true);
                    requestTABPANE.setVisible(true);
                } else if (type.equals(Constants.customerUserType)) {
                    customerDiscounts.setVisible(true);
                    sellerRequests.setVisible(false);
                    discountTABPANE.setVisible(true);
                    requestTABPANE.setVisible(false);
                    sellLogBTN.setText("Buy Logs");
                } else {
                    sellLogBTN.setVisible(false);
                    additionalInfoStackPane.setVisible(false);
                }
            }

            if (type.equals(Constants.adminUserType)) {
                balanceProperty.setVisible(false);
                balanceValue.setVisible(false);
                storeProperty.setVisible(false);
                storeValue.setVisible(false);
            } else if (type.equals(Constants.customerUserType)) {
                storeProperty.setVisible(false);
                storeValue.setVisible(false);
            }
        }

        private void initActions() {
            sellLogBTN.setOnAction(e -> {
                if (info[info.length - 1].equals(Constants.customerUserType)) {
                    CustomerBuyLogMenuController.display();
                } else {
                    SellerSellLogsManagingMenuController.display();
                }
            });

            logoutBTN.setOnAction(e -> {
                try {
                    mainController.logout();
                    View.type.set(Constants.anonymousUserType);
                } catch (Exceptions.NotLoggedInException ex) {
                    ex.printStackTrace();
                }
            });

            editBTN.setOnAction(e -> EditPersonalInfoPopupController.display());
        }

        private void initTable() {
            if (isPopup) return;
            if (info[info.length - 1].equals(Constants.customerUserType)) {
                codeCOL.setCellValueFactory(new PropertyValueFactory<>("code"));
                discountUntilCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
                discountPercentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentageMax"));
                for (String[] discountCode : customerController.viewDiscountCodes()) {
                    customerDiscounts.getItems().add(new DiscountWrapper(discountCode[0], discountCode[2], discountCode[4], discountCode[3]));
                }
            } else if (info[info.length - 1].equals(Constants.sellerUserType)) {
                dateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));
                typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
                requestDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
                for (String[] request : sellerController.getPendingRequests()) {
                    sellerRequests.getItems().add(new RequestWrapper(request));
                }
            }
        }

        public void update() {
            try {
                if (username != null)
                    info = mainController.viewPersonalInfo(username);
                else
                    info = mainController.viewPersonalInfo();
            } catch (Exceptions.UsernameDoesntExistException e) {
                e.printStackTrace();
                return;
            } catch (Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                return;
            }

            usernameLBL.setText(info[0]);
            nameLBL.setText(info[3] + " " + info[4]);
            phoneValue.setText(info[6]);
            emailValue.setText(info[5]);
            if ( ! info[info.length - 1].equals(Constants.adminUserType)) {
                balanceValue.setText(info[8] + "$");
            }
            if (info[info.length - 1].equals(Constants.sellerUserType)) {
                storeValue.setText(info[9]);
            }

            accountIMG.setImage(new Image(info[7]));
        }


    }

    public static class AddProductRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class EditProductRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class EditSaleRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class AddSaleRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class AddReviewRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class AddSellerRequestPopupController {
        public static void display(String requestId) {

        }
    }

    public static class MainMenuController implements Initializable {

        @FXML
        private Button allSales;

        @FXML
        private HBox productsInSale;

        @FXML
        private HBox advertisingProducts;

        @FXML
        private BorderPane borderPane;

        @FXML
        private Button productsMenu;

        private static void display() {
            View.getStackTrace().clear();
            View.stackSize.set(0);
            View.setMainPane(Constants.FXMLs.mainMenu);
        }


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            for (String[] subProductPack : mainController.getSubProductsForAdvertisements(6)) {
                advertisingProducts.getChildren().add(ProductBoxController.createBox(subProductPack));
            }

            for (String[] subProduct : mainController.getSubProductsInSale(10)) {
                productsInSale.getChildren().add(ProductBoxController.createBox(subProduct));
            }

            allSales.setOnAction(e -> salesMenu());
            productsMenu.setOnAction(e -> productsMenu());

            initCategoriesBox();
        }

        private void salesMenu(){
            ProductsMenuController.display("SuperCategory", true);
        }

        private void productsMenu(){
            ProductsMenuController.display("SuperCategory", false);
        }

        private void initCategoriesBox(){
            borderPane.setLeft(CategoryBoxController.createBox("SuperCategory", false));
        }
    }

    public static class ProductsMenuController {

        @FXML
        private Button update;

        @FXML
        private ChoiceBox<String> sortByChoiceBox;

        @FXML
        private ToggleButton isIncreasingButton;

        @FXML
        private ToggleGroup increasingToggleGroup;

        @FXML
        private ToggleButton isDecreasing;

        @FXML
        private CheckBox availableCheckBox;

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
        private GridPane propertyFilters;

        @FXML
        private GridPane productsPane;

        @FXML
        private HBox categoryTreeBox;

        @FXML
        private BorderPane borderPane;



        private static final int numberOfColumns = 3;
        public ArrayList<String[]> products;
        private String categoryName;
        private boolean inSale = false;
        private double maximumAvailablePrice;
        private DoubleProperty minPrice;
        private DoubleProperty maxPrice;
        private BooleanProperty available;
        private BooleanProperty isIncreasing;
        private StringProperty sortBy;
        private StringProperty name;
        private StringProperty brand;
        private StringProperty seller;
        private HashMap<String, SimpleStringProperty> properties;
        private HashMap<String, ChoiceBox<String>> propertyChoiceBox;


        public static void display(String categoryName, boolean inSale) {
            ProductsMenuController controller =  View.setMainPane(Constants.FXMLs.productsMenu);
            if( controller != null){
                controller.categoryName = categoryName;
                controller.inSale = inSale;
                controller.update();
                controller.initChoiceBoxes();
                controller.initActions();
                controller.initFilterBar();
                controller.initPropertyFilters();
                controller.initCategoryTree();
                controller.initCategoryBox();
            }
        }

        private void initPropertyFilters(){
            try {
                ArrayList<String> propertyKeys = mainController.getPropertiesOfCategory(categoryName, false);
                int numberOfProperties = propertyKeys.size();
                int numberOfColumns = numberOfProperties / 3 + (numberOfProperties % 3 == 0 ? 0 : 1);
                setFilterPropertiesPaneSize( numberOfColumns );
                for (String propertyKey : propertyKeys) {
                    VBox propertyBox = creatPropertyChoiceBox(propertyKey);
                    int propertyIndex = propertyKeys.indexOf(propertyKey);
                    propertyFilters.add(propertyBox, propertyIndex / 3, propertyIndex % 3, 1, 1);
                }
            } catch (Exceptions.InvalidCategoryException e) {
                e.printStackTrace();
            }
        }

        private void initChoiceBoxes() {
            ArrayList<String> brands = (ArrayList<String>) products.stream().map(p -> p[3]).collect(Collectors.toList());
            brands.add(null);
            HashSet<String> b = new HashSet<>(brands);
            filterBrand.setItems(FXCollections.observableArrayList(b));

            ArrayList<String> sellers = (ArrayList<String>) products.stream().map(p -> p[12]).collect(Collectors.toList());
            sellers.add(null);
            HashSet<String> s = new HashSet<>(sellers);
            filterSeller.setItems(FXCollections.observableArrayList(s));

            ArrayList<String> sorts = new ArrayList<>();
            sorts.add(null);
            sorts.add("view count");
            sorts.add("price");
            sorts.add("name");
            sorts.add("rating score");
            sorts.add("category name");
            sorts.add("remaining count");
            HashSet<String> availableSorts = new HashSet<>(sorts);
            sortByChoiceBox.setItems(FXCollections.observableArrayList(availableSorts));

            }

        //TODO: set max price for sliders
        private void initFilterBar() {
            minPrice = new SimpleDoubleProperty();
            minPrice.bind(minPriceSlider.valueProperty());

            maxPrice = new SimpleDoubleProperty();
            maxPrice.bind(maxPriceSlider.valueProperty());

            available = new SimpleBooleanProperty();
            available.bind(availableCheckBox.selectedProperty());

            isIncreasing = new SimpleBooleanProperty();
            isIncreasing.bind(isIncreasingButton.selectedProperty());

            sortBy = new SimpleStringProperty();
            sortBy.bind(sortByChoiceBox.valueProperty());

            name = new SimpleStringProperty();
            name.bind(filterName.textProperty());

            brand = new SimpleStringProperty();
            brand.bind(filterBrand.valueProperty());

            seller = new SimpleStringProperty();
            seller.bind(filterSeller.valueProperty());

            setMaxPrice();
            maxPriceSlider.setMax(maximumAvailablePrice);
            minPriceSlider.setMax(maximumAvailablePrice);
            //properties
        }

        private void initActions() {
            update.setOnAction(e -> update());
        }

        private void update(){
            updateProducts();
            updatePane();
        }

        private void updateProducts(){
            HashMap<String, String> propertyValues = new HashMap<>();
            for (String s : properties.keySet()) {
                propertyValues.put(s, properties.get(s).getValue());
            }
            products = mainController.sortFilterProducts(categoryName, inSale, sortBy.getValue(), isIncreasing.getValue(), available.getValue(),
            minPrice.getValue(), maxPrice.getValue(), name.getValue(), brand.getValue(), seller.getValue(), 0, propertyValues);
        }

        private void updatePane(){
            int numberOfProducts = products.size();
            int numberOfRows = numberOfProducts / numberOfColumns +1;
            setPaneSize(numberOfRows);
            int index;
            for (String[] subProductPack : products) {
                index = products.indexOf(subProductPack);
                Parent productBox = ProductBoxController.createBox(subProductPack);
                productsPane.add(productBox, index % numberOfColumns, index / numberOfColumns, 1, 1);
            }
        }

        //TODO: creat each row and column , with hGap and vGap, you can give ID to control them
        private void setPaneSize(int numberOfRows){
            productsPane = new GridPane();
            int currentRowsNumber = productsPane.getRowCount();
            int currentColumnsNumber = productsPane.getColumnCount();
            if(numberOfRows > currentRowsNumber) {
                productsPane.addRow(numberOfRows - currentRowsNumber);
            }
            if(numberOfColumns > currentColumnsNumber){
                productsPane.addColumn(numberOfColumns - currentColumnsNumber);
            }

        }

        private void setFilterPropertiesPaneSize(int numberOfColumns){
            propertyFilters = new GridPane();
            int currentRowsNumber = propertyFilters.getRowCount();
            int currentColumnsNumber = propertyFilters.getColumnCount();
            if(numberOfColumns > currentColumnsNumber){
                propertyFilters.addColumn(numberOfColumns - currentColumnsNumber);
            }

            if( currentRowsNumber < 3){
                propertyFilters.addRow( 3 - currentRowsNumber);
            }
        }

        private VBox creatPropertyChoiceBox(String property){
            VBox vBox = new VBox();
            vBox.getChildren().add(new Label(property));
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            try {
                HashSet<String> values = new HashSet<>(mainController.getPropertyValuesInCategory(categoryName, property));
                choiceBox.setItems(FXCollections.observableArrayList(values));
            } catch (Exceptions.InvalidCategoryException e) {
                return null;
            }
            SimpleStringProperty valueProperty = new SimpleStringProperty();
            valueProperty.bind(choiceBox.valueProperty());
            properties.put(property, valueProperty);
            return vBox;
        }

        private void setMaxPrice(){
            ArrayList<Double> prices = new ArrayList<>();
            for (String[] product : products) {
                prices.add(Double.parseDouble(product[8]));
            }
            if(prices.size() == 0)
                maximumAvailablePrice = 0;
            else {
                maximumAvailablePrice = prices.get(0);
                for (Double price : prices) {
                    if( price > maximumAvailablePrice)
                        maximumAvailablePrice = price;
                }
            }
        }

        private void initCategoryTree(){
            ArrayList<String> categoryNames = mainController.getCategoryTreeOfACategory(categoryName);
            for (String s : categoryNames) {
                categoryTreeBox.getChildren().add(createCategoryButton(s));
            }
        }

        private Button createCategoryButton(String category){
            Button button = new Button();
            button.setText(category + " >");
            button.setOnAction(e -> ProductsMenuController.display(category, inSale));
            return button;
        }

        private void initCategoryBox(){
            borderPane.setLeft(CategoryBoxController.createBox(categoryName, inSale));
        }
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

        @FXML
        private Label remainingTime;

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
            name.setText(subProductInfo[2] + " " + subProductInfo[3]);
            image.setImage(new Image(subProductInfo[6]));
            priceBefore.setText(subProductInfo[7]);
            priceAfter.setText(subProductInfo[8]);
            sale.setText(subProductInfo[11] != null ? subProductInfo[11] : "");
            remainingTime.setText(subProductInfo[10] != null ? subProductInfo[10] : "");
        }

        private void setAction(Parent p) {
            p.setOnMouseClicked(e -> ProductDetailMenuController.display(subProduct[0],subProduct[1],false));
        }

    }

    public static class EditProductPopupController {
        //TODO: fix textArea

        @FXML
        private TextField nameField;

        @FXML
        private Label usernameErrLBL;

        @FXML
        private TextField brandField;

        @FXML
        private Label passwordErrLBL;

        @FXML
        private TextField category;

        @FXML
        private TextField imageField;

        @FXML
        private Button browseBTN;

        @FXML
        private Label imageErrLBL;

        @FXML
        private TextArea infoArea;

        @FXML
        private Label emailErrLBL;

        @FXML
        private TextField priceField;

        @FXML
        private Label priceError;

        @FXML
        private TextField countField;

        @FXML
        private Label countError;

        @FXML
        private Label errorLBL;

        @FXML
        private Button saveBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private Label priceLBL;

        @FXML
        private Label countLBL;

        String productId;
        String subProductId;
        private String[] productInfo;
        private String[] subProductInfo;
        private ArrayList<PropertyWrapper> categoryProperties = new ArrayList<>();
        private SimpleBooleanProperty nameFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty brandFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty imageFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty priceFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty countFieldChanged = new SimpleBooleanProperty(false);

        public class PropertyWrapper {
            String property;
            String initialValue;
            TextField value = new TextField();

            public PropertyWrapper(String property) {
                this.property = property;
                value.setPromptText("Enter value...");
                this.initialValue = "";
            }

            public PropertyWrapper(String property, String value) {
                this(property);
                this.value.setText(value);
                this.initialValue = value;
            }

            public String getProperty() {
                return property;
            }

            public TextField getValue() {
                return value;
            }

            public boolean hasChanged() {
                return !value.getText().equals(initialValue);
            }
        }


        public static void display(String productId, String subProductId) {
            ((EditProductPopupController) View.popupWindow("Edit Product Info", Constants.FXMLs.editProductPopup, 860, 505)).initialize(productId, subProductId);
        }

        private void initialize(String productId, String subProductId) {
            try {
                this.productId = productId;
                this.subProductId = subProductId;
                productInfo = mainController.digest(productId);
                subProductInfo = mainController.getSubProductByID(subProductId);
            } catch (Exception e) {
                e.printStackTrace();
                discardBTN.getScene().getWindow().hide();
                return;
            }

            initVisibility();
            initValues();
            initBindings();
            initListeners();
            initButtons();
        }

        private void initVisibility() {
            if (View.type.get().equals(Constants.adminUserType)) {
                priceLBL.setVisible(false);
                countLBL.setVisible(false);
                priceField.setVisible(false);
                countField.setVisible(false);
            }
            imageField.setEditable(false);
            category.setEditable(false);

            usernameErrLBL.setVisible(false);
            passwordErrLBL.setVisible(false);
            emailErrLBL.setVisible(false);
            imageErrLBL.setVisible(false);
            priceError.setVisible(false);
            countError.setVisible(false);
        }

        private void initValues() {
            nameField.setText(productInfo[1]);
            brandField.setText(productInfo[2]);
            imageField.setText(productInfo[8]);
            priceField.setText(subProductInfo[7]);
            countField.setText(subProductInfo[5]);
            infoArea.setText(productInfo[3]);

            usernameErrLBL.setText("Invalid name!");
            passwordErrLBL.setText("Invalid brand!");
            emailErrLBL.setText("Invalid email!");
            priceError.setText("Invalid price!");
            countError.setText("Invalid count!");
        }

        private void initBindings() {
            nameFieldChanged.bind(
                    Bindings.when(nameField.textProperty().isEqualTo(productInfo[1])).then(false).otherwise(true)
            );
            brandFieldChanged.bind(
                    Bindings.when(brandField.textProperty().isEqualTo(productInfo[2])).then(false).otherwise(true)
            );
            priceFieldChanged.bind(
                    Bindings.when(priceField.textProperty().isEqualTo(subProductInfo[7])).then(false).otherwise(true)
            );
            countFieldChanged.bind(
                    Bindings.when(countField.textProperty().isEqualTo(subProductInfo[5])).then(false).otherwise(true)
            );
            imageFieldChanged.bind(
                    Bindings.when(imageField.textProperty().isEqualTo(productInfo[8])).then(false).otherwise(true)
            );

            saveBTN.disableProperty().bind(
                    Bindings.createObjectBinding(() -> !nameFieldChanged.get() && !brandFieldChanged.get() &&
                                    !priceFieldChanged.get() && !countFieldChanged.get(),
                            nameFieldChanged, brandFieldChanged, imageFieldChanged, priceFieldChanged, countFieldChanged)
            );
        }

        private void initListeners() {
            View.addListener(countField, "[0-9]");
        }

        private void initButtons() {
            browseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File", "*.jpg"),
                        new FileChooser.ExtensionFilter("Image File", "*.png"));
                File choseFile = fileChooser.showOpenDialog(new Stage());
                if (choseFile != null) imageField.setText(choseFile.getPath());
            });

            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());

            saveBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        if (nameFieldChanged.get())
                            sellerController.editProduct(productId, "name", productInfo[1] = nameField.getText());
                        if (brandFieldChanged.get())
                            sellerController.editProduct(productId, "brand", productInfo[2] = brandField.getText());
                        if (imageFieldChanged.get())
                            sellerController.editProduct(productId, "imagePath", productInfo[8] = imageField.getText());
                        if (countFieldChanged.get())
                            sellerController.editProduct(productId, "count", subProductInfo[5] = countField.getText());
                        if (priceFieldChanged.get())
                            sellerController.editProduct(productId, "price", subProductInfo[7] = priceField.getText());
                        discardBTN.fire();
                    } catch (Exception ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    }

                }
            });
        }

        private boolean validateFields() {
            boolean valid = true;
            if (nameField.getText().equals("")) {
                valid = false;
                usernameErrLBL.setVisible(true);
            } else usernameErrLBL.setVisible(false);

            if (brandField.getText().equals("")) {
                valid = false;
                passwordErrLBL.setVisible(true);
            } else passwordErrLBL.setVisible(false);

            if (!priceField.getText().matches(Constants.doublePattern)) {
                valid = false;
                priceError.setVisible(true);
            } else priceError.setVisible(false);

            if (!countField.getText().matches(Constants.unsignedIntPattern)) {
                valid = false;
                countError.setVisible(true);
            } else countError.setVisible(false);

            return valid;
        }

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }
    }

    public static class CategoryBoxController{

        @FXML
        private VBox subCategoryBox;

        public static Parent createBox(String categoryName, boolean inSale) {
                try {
                    ArrayList<String> subCategories = mainController.getSubCategoriesOfACategory(categoryName);
                    FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.categoriesBox + ".fxml"));
                    Parent p;
                    p = loader.load();
                    CategoryBoxController cbc = loader.getController();
                    VBox subCategoryBox = cbc.subCategoryBox;
                    for (String subCategory : subCategories) {
                        subCategoryBox.getChildren().add(createCategoryButton(subCategory, inSale));
                    }
                    return p;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return null;
        }



        private static Button createCategoryButton(String categoryName, boolean inSale){
            Button button = new Button();
            button.setText(categoryName);
            button.setOnAction(e -> ProductsMenuController.display(categoryName, inSale));
            return button;
        }
    }

    public static class ProductDetailMenuController {
        @FXML
        private ImageView productIMG;

        @FXML
        private Button compareBTN;

        @FXML
        private Label nameLBL;

        @FXML
        private Label ratingLBL;

        @FXML
        private Label brandLBL;

        @FXML
        private Label categoryLBL;

        @FXML
        private Text productInfoTXT;

        @FXML
        private Label sellerLBL;

        @FXML
        private Label priceBeforeLBL;

        @FXML
        private Label priceAfterLBL;

        @FXML
        private Button addToCartBTN;

        @FXML
        private Button editBTN;

        @FXML
        private TableView<?> sellersTBL;

        @FXML
        private TableColumn<?, ?> sellersTBLNumberCOL;

        @FXML
        private TableColumn<?, ?> sellersTBLSellerCOL;

        @FXML
        private TableColumn<?, ?> sellersTBLPriceCOL;

        @FXML
        private TableColumn<?, ?> sellersTBLNumberAvailableCOL;

        @FXML
        private TableView<?> PropertiesTBL;

        @FXML
        private TableColumn<?, ?> propertyTab;

        @FXML
        private TableColumn<?, ?> valueTab;

        @FXML
        private VBox reviewsVB;

        private String[] productPack;
        private String[] subProductPack;


        public static void display(String productId, boolean editable){
            try {
                display( productId, mainController.getDefaultSubProductOfAProduct(productId)[1], editable);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
        }

        public static void display(String productId, String subProductId, boolean editable) {
            String type = View.type.get();
            ProductDetailMenuController controller;
            if (type.equals(Constants.sellerUserType) || type.equals(Constants.adminUserType)) {
                controller = ((ProductDetailMenuController)
                        View.popupWindow("Product details", Constants.FXMLs.productDetailMenu, 1200, 600));
                controller.initialize(productId, type, editable);
            } else {
                controller = ((ProductDetailMenuController)
                        View.setMainPane(Constants.FXMLs.productDetailMenu));
                //controller.initialize(productId, Constants.customerUserType);
            }
        }

        public class SellerWrapper {
            Label name = new Label();
            Double price;
            int available;

            public SellerWrapper(String name, double price, int available) {
                this.name.setText(name);
                this.price = price;
                this.available = available;

                this.name.setOnMouseClicked(e -> {

                });
            }

            public Label getName() {
                return name;
            }

            public Double getPrice() {
                return price;
            }

            public int getAvailable() {
                return available;
            }
        }

        private void initialize(String productId, String type, boolean editable) {
            initTable();
        }

        private void initTable() {
            sellersTBLSellerCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            sellersTBLPriceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
            sellersTBLNumberAvailableCOL.setCellValueFactory(new PropertyValueFactory<>("available"));

            initItems();
        }

        private void initItems() {
        }

        //TODO: rating count
        private void initMainObjects(){
            nameLBL.setText(productPack[1]);
            brandLBL.setText(productPack[2]);
            productInfoTXT.setText(productPack[3]);
            ratingLBL.setText(productPack[4]);
            categoryLBL.setText(productPack[7]);
            productIMG.setImage(new Image(productPack[8]));
            //productInfo[5] = Integer.toString(product.getRatingsCount());
        }

        private void setPacks(String productId, String subProductId){
            try {
                productPack = mainController.digest(productId);
                subProductPack = mainController.getSubProductByID(subProductId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //TODO: available count in sub product box
        private void updateSubProductBox(){
            sellerLBL.setText(subProductPack[12]);
            priceBeforeLBL.setText(subProductPack[7]);
            if( !subProductPack[7].equals(subProductPack[8]))
                priceAfterLBL.setText(subProductPack[8]);
            else
                priceAfterLBL.setText("");
            //subProductBoxPack[9] = Integer.toString(subProduct.getRemainingCount());
        }

        private void initReviewsVB(){
            try {
                ArrayList<String[]> reviews = mainController.reviewsOfProductWithId(productPack[0]);

            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    //public static class

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
        private TextField showPasswordField;

        @FXML
        private ToggleButton showPasswordBTN;

        @FXML
        private Label errorLBL;

        @FXML
        private Button loginBTN;

        @FXML
        private Hyperlink registerLink;

        public static void display(Stage stage) {
            PopupStage = stage;
            PopupStage.setWidth(600);
            PopupStage.setHeight(400);
            PopupStage.setResizable(false);
            try {
                PopupStage.setScene(new Scene(View.loadFxml(Constants.FXMLs.loginPopup)));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                PopupStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception ignored) {
            }
            PopupStage.show();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            errorLBL.setText("");
            initListeners();
            initActions();
            initPasswordStuff();
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
                    if (!View.type.get().equals(Constants.customerUserType)) MainMenuController.display();

                    PopupStage.close();
                } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                    errorLBL.setText("invalid username or password");
                    errorLBL.setTextFill(Color.RED);
                    ex.printStackTrace();
                }
            });
            registerLink.setOnAction(e -> RegisterPopupController.display(PopupStage));
        }

        private void initPasswordStuff() {
            showPasswordField.textProperty().bind(passwordField.textProperty());
            showPasswordField.setEditable(false);
            showPasswordField.visibleProperty().bind(passwordField.visibleProperty().not());
            passwordField.visibleProperty().bind(showPasswordBTN.selectedProperty().not());
        }
    }

    public static class RegisterPopupController implements Initializable {
        private static Stage PopupStage;

        @FXML
        private TextField customerUsername;

        @FXML
        private Label customerUsernameError;

        @FXML
        private PasswordField customerPassword;

        @FXML
        private TextField customerShowPasswordField;

        @FXML
        private ToggleButton customerShowPasswordBTN;

        @FXML
        private Label customerPasswordError;

        @FXML
        private TextField customerFirstName;

        @FXML
        private Label customerFirstNameError;

        @FXML
        private TextField customerLastName;

        @FXML
        private Label customerLastNameError;

        @FXML
        private TextField customerPhoneNumber;

        @FXML
        private Label customerPhoneNumberError;

        @FXML
        private TextField customerEmail;

        @FXML
        private Label customerEmailError;

        @FXML
        private TextField customerBalance;

        @FXML
        private Label customerBalanceError;

        @FXML
        private TextField customerStoreName;

        @FXML
        private Label customerStoreNameError;

        @FXML
        private TextField customerImageField;

        @FXML
        private Button customerBrowseBTN;

        @FXML
        private Label customerImageError;

        @FXML
        private Button customerRegister;

        @FXML
        private Hyperlink customerLoginHL;

        @FXML
        private TextField sellerUsername;

        @FXML
        private Label sellerUsernameError;

        @FXML
        private PasswordField sellerPassword;

        @FXML
        private TextField sellerShowPasswordFIeld;

        @FXML
        private ToggleButton sellerShowPasswordBTN;

        @FXML
        private Label sellerPasswordError;

        @FXML
        private TextField sellerFirstName;

        @FXML
        private Label sellerFirstNameError;

        @FXML
        private TextField sellerLastName;

        @FXML
        private Label sellerLastNameError;

        @FXML
        private TextField sellerPhoneNumber;

        @FXML
        private Label sellerPhoneNumberError;

        @FXML
        private TextField sellerEmail;

        @FXML
        private Label sellerEmailError;

        @FXML
        private TextField sellerBalance;

        @FXML
        private Label sellerBalanceError;

        @FXML
        private TextField sellerStoreName;

        @FXML
        private Label sellerStoreNameError;

        @FXML
        private TextField sellerImageField;

        @FXML
        private Button sellerBrowseBTN;

        @FXML
        private Label sellerImageError;

        @FXML
        private Button sellerRegister;

        @FXML
        private Hyperlink sellerLoginHL;

        public static void display(Stage stage) {
            PopupStage = stage;
            PopupStage.setWidth(500);
            PopupStage.setHeight(700);
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
            customerEmailError.setText("Invalid email address");
            sellerEmailError.setText("Invalid email address");
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
            View.addListener(customerUsername, "\\w");
            View.addListener(sellerUsername, "\\w");
            View.addListener(customerPassword, "\\w");
            View.addListener(sellerPassword, "\\w");
            View.addListener(customerPhoneNumber, "[0-9]");
            View.addListener(sellerPhoneNumber, "[0-9]");
            customerImageField.setEditable(false);
            sellerImageField.setEditable(false);
        }

        private void initActions() {
            customerRegister.setOnAction(e -> {
                if (areCustomerFieldsAvailable()) {
                    try {
                        mainController.creatAccount(Constants.customerUserType, customerUsername.getText(),
                                customerPassword.getText(), customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhoneNumber.getText(), Double.valueOf(customerBalance.getText()), null, customerImageField.getText());
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
                                sellerEmail.getText(), sellerPhoneNumber.getText(), Double.valueOf(sellerBalance.getText()), null, customerImageField.getText());
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

            customerBrowseBTN.setOnAction(e -> chooseFile(customerImageField));
            sellerBrowseBTN.setOnAction(e -> chooseFile(sellerImageField));
        }

        private void chooseFile(TextField field) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
            File chosenFile = fileChooser.showOpenDialog(new Stage());
            if (chosenFile != null) {
                field.setText(chosenFile.getPath());
            }
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

        private void initPasswordStuff() {
            customerShowPasswordField.textProperty().bind(customerPassword.textProperty());
            customerShowPasswordField.setEditable(false);
            customerShowPasswordField.visibleProperty().bind(customerPassword.visibleProperty().not());
            customerPassword.visibleProperty().bind(customerShowPasswordBTN.selectedProperty().not());

            sellerShowPasswordFIeld.textProperty().bind(sellerPassword.textProperty());
            sellerShowPasswordFIeld.setEditable(false);
            sellerShowPasswordFIeld.visibleProperty().bind(sellerPassword.visibleProperty().not());
            sellerPassword.visibleProperty().bind(sellerShowPasswordBTN.selectedProperty().not());
        }
    }

    public static class AdminProductManagingMenu implements Initializable{
        @FXML
        private TableView<ProductWrapper> productsTable;

        @FXML
        private TableColumn<ProductWrapper, String> idCol;

        @FXML
        private TableColumn<ProductWrapper, String> nameCOL;

        @FXML
        private TableColumn<ProductWrapper, String> categoryCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        private ArrayList<ProductWrapper> allProducts;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminProductManagingMenu);
        }

        private class ProductWrapper {
            String id;
            String nameBrand;
            String category;
            Button detailBTN = new Button();
            Button removeBTN = new Button();

            public ProductWrapper(String[] productPack) {
                this(productPack[0], productPack[1] + "-" + productPack[2], productPack[3]);
            }

            public ProductWrapper(String id, String nameBrand, String category) {
                this.id = id;
                this.nameBrand = nameBrand;
                this.category = category;

                detailBTN.setOnAction(e -> ProductDetailMenuController.display(id, true));
                detailBTN.getStyleClass().add("details-button");

                removeBTN.setOnAction(e -> {
                    try {
                        adminController.removeProduct(id);
                    } catch (Exceptions.InvalidProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
                removeBTN.getStyleClass().add("remove-button");
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            allProducts =  new ArrayList<>();
            for (String[] product : adminController.manageAllProducts()) {
                allProducts.add(new ProductWrapper(product));
            }

            initTable();
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            categoryCOL.setCellValueFactory(new PropertyValueFactory<>("category"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailBTN"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));
        }
    }

    public static class AdminDiscountManagingMenuController implements Initializable {

        public static AdminDiscountManagingMenuController currentObject;
        private static ArrayList<DiscountWrapper> allDiscountWrappers = new ArrayList<>();

        @FXML
        private TableView<DiscountWrapper> discounts;

        @FXML
        private TableColumn<DiscountWrapper, String> idCol;

        @FXML
        private TableColumn<DiscountWrapper, String> codeCOL;

        @FXML
        private TableColumn<DiscountWrapper, SimpleStringProperty> percentageCOL;

        @FXML
        private TableColumn<DiscountWrapper, SimpleStringProperty> startDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, SimpleStringProperty> endDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addDiscountBTN;

        public void addDiscount(String[] info) {
            DiscountWrapper newItem = new DiscountWrapper(info);
            allDiscountWrappers.add(newItem);
            discounts.getItems().add(newItem);
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminDiscountManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            currentObject = this;
            initActions();
            initDiscounts();
            initTable();
        }

        private void initActions() {
            addDiscountBTN.setOnAction(e -> AdminDiscountManagingPopupController.display(null, true));
        }

        private void initDiscounts() {
            allDiscountWrappers.clear();

            for (String discount : adminController.viewDiscountCodes()) {
                String[] details;
                try {
                    details = adminController.viewDiscountCodeByCode(discount);
                    allDiscountWrappers.add(new DiscountWrapper(details));
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                }
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

        public  class DiscountWrapper {
            String id;
            String code;
            SimpleDoubleProperty percentage = new SimpleDoubleProperty();
            SimpleDoubleProperty maximumAmount = new SimpleDoubleProperty();
            Button detail = new Button();
            Button remove = new Button();
            SimpleStringProperty startDate = new SimpleStringProperty();
            SimpleStringProperty endDate = new SimpleStringProperty();
            SimpleStringProperty perMax = new SimpleStringProperty();

            public DiscountWrapper(String[] details) {
                this(details[0], details[1], details[2], details[3], Double.parseDouble(details[5]), Double.parseDouble(details[4]));
            }

            DiscountWrapper(String id, String code, String startDate, String endDate, double percentage, double maximumAmount) {
                this.id = id;
                this.code = code;
                this.percentage.set(percentage);
                this.maximumAmount.set(maximumAmount);
                this.startDate.set(startDate);
                this.endDate.set(endDate);
                perMax.bind(this.percentage.asString().concat("% (").concat(this.maximumAmount).concat("$)"));
                detail.setOnAction(e -> AdminDiscountManagingPopupController.display(this, true));
                remove.setOnAction(e -> {
                    try {
                        adminController.removeDiscountCode(code);
                    } catch (Exceptions.DiscountCodeException ex) {
                        ex.printStackTrace();
                    }
                });
                detail.getStyleClass().add("details-button");
                remove.getStyleClass().add("remove-button");
            }

            public String getId() {
                return id;
            }

            public String getCode() {
                return code;
            }

            public SimpleStringProperty percentageProperty() {
                return perMax;
            }

            public Button getDetail() {
                return detail;
            }

            public Button getRemove() {
                return remove;
            }


            public SimpleDoubleProperty maximumAmountProperty() {
                return maximumAmount;
            }


            public SimpleStringProperty startDateProperty() {
                return startDate;
            }

            public SimpleStringProperty endDateProperty() {
                return endDate;
            }
        }
    }

    public static class AdminRequestManagingMenuController implements Initializable {

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminRequestManagingMenu);
        }

        @FXML
        private TableView<RequestWrapper> pending;

        @FXML
        private TableColumn<RequestWrapper, String> pendingIdCol;

        @FXML
        private TableColumn<RequestWrapper, String> pendingTypeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> pendingDateCOL;

        @FXML
        private TableColumn<RequestWrapper, Button> pendingDetailsCOL;

        @FXML
        private TableColumn<RequestWrapper, HBox> pendingAcceptCOL;

        @FXML
        private TableView<RequestWrapper> archive;

        @FXML
        private TableColumn<RequestWrapper, String> archiveIdCol;

        @FXML
        private TableColumn<RequestWrapper, String> archiveTypeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> archiveDateCOL;

        @FXML
        private TableColumn<RequestWrapper, Button> archiveDetailsCOL;

        @FXML
        private TableColumn<RequestWrapper, String> archiveStatusCOL;

        ArrayList<RequestWrapper> pendingRequests;
        ArrayList<RequestWrapper> archivedRequests;

        public class RequestWrapper {
            String id, type, date, status;
            Button decline = new Button();
            Button details = new Button();
            Button accept = new Button();
            HBox acceptDecline = new HBox(accept, decline);

            public RequestWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3]);
            }

            public RequestWrapper(String id, String type, String date, String status) {
                this.id = id;
                this.type = type;
                this.date = date;
                this.status = status;

                initButtons();
            }

            private void initButtons() {
                decline.getStyleClass().add("decline-button");
                details.getStyleClass().add("details-button");
                accept.getStyleClass().add("accept-button");

                acceptDecline.setAlignment(Pos.CENTER);

                decline.setOnAction(e -> {
                    try {
                        adminController.acceptRequest(id, false);
                    } catch (Exceptions.InvalidRequestIdException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    status = Constants.REQUEST_DECLINE;
                    archive.getItems().add(this);
                    archivedRequests.add(this);

                    pending.getItems().remove(this);
                    pendingRequests.remove(this);
                });
//                TODO: wtf.
//                details.setOnAction(e -> );

                accept.setOnAction(e -> {
                    try {
                        adminController.acceptRequest(id, true);
                    } catch (Exceptions.InvalidRequestIdException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    status = Constants.REQUEST_ACCEPT;
                    archive.getItems().add(this);
                    archivedRequests.add(this);

                    pending.getItems().remove(this);
                    pendingRequests.remove(this);
                });
            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getDate() {
                return date;
            }

            public String getStatus() {
                return status;
            }

            public Button getDecline() {
                return decline;
            }

            public Button getDetails() {
                return details;
            }

            public Button getAccept() {
                return accept;
            }

            public HBox getAcceptDecline() {
                return acceptDecline;
            }
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initRequests();
            initTable();
        }

        private void initRequests() {
            pendingRequests = new ArrayList<>();
            for (String[] pendingRequest : adminController.getPendingRequests()) {
                pendingRequests.add(new RequestWrapper(pendingRequest));
            }

            archivedRequests = new ArrayList<>();
            for (String[] archivedRequest : adminController.getArchivedRequests()) {
                archivedRequests.add(new RequestWrapper(archivedRequest));
            }
        }

        private void initTable() {
            pendingIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            pendingTypeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            pendingDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            pendingAcceptCOL.setCellValueFactory(new PropertyValueFactory<>("acceptDecline"));
            pendingDateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));

            archiveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            archiveTypeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            archiveDateCOL.setCellValueFactory(new PropertyValueFactory<>("date"));
            archiveDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            archiveStatusCOL.setCellValueFactory(new PropertyValueFactory<>("status"));

            pending.getItems().addAll(pendingRequests);
            archive.getItems().addAll(archivedRequests);
        }
    }

    public static class AdminCategoryManagingMenuController implements Initializable{

        static AdminCategoryManagingMenuController currentController;
        ArrayList<CategoryWrapper> wrappers;

        @FXML
        private TableView<CategoryWrapper> categories;

        @FXML
        private TableColumn<CategoryWrapper, String> idCOL;

        @FXML
        private TableColumn<CategoryWrapper, SimpleStringProperty> nameCOL;

        @FXML
        private TableColumn<CategoryWrapper, SimpleStringProperty> parentCOL;

        @FXML
        private TableColumn<CategoryWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<CategoryWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addCategoryBTN;

        void addItem(String[] info) {
            categories.getItems().add(new CategoryWrapper(info));
        }

        public class CategoryWrapper {
            String id;
            Button remove, details;
            SimpleStringProperty name = new SimpleStringProperty();
            SimpleStringProperty parent = new SimpleStringProperty();

            public CategoryWrapper(String[] info) {
                this(info[0], info[1], info[2]);
            }

            public CategoryWrapper(String id, String name, String parent) {
                this.id = id;
                this.name.set(name);
                this.parent.set(parent);
                remove = new Button();
                details = new Button();

                remove.getStyleClass().add("remove-button");
                details.getStyleClass().add("details-button");

                remove.setOnAction(e -> {
                    try {
                        adminController.removeCategory(this.name.get());
                        ArrayList<CategoryWrapper> toBeRemoved = new ArrayList<>();
                        for (CategoryWrapper item : categories.getItems()) {
                            if(item.parent.get().equals(this.name.get()) || item.name.get().equals(this.name.get())) toBeRemoved.add(item);
                        }
                        categories.getItems().removeAll(toBeRemoved);
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    }
                });

                details.setOnAction(e -> AdminCategoryManagingPopupController.display(this));
            }


            public String getId() {
                return id;
            }

            public String getName() {
                return name.get();
            }

            public SimpleStringProperty nameProperty() {
                return name;
            }

            public String getParent() {
                return parent.get();
            }

            public SimpleStringProperty parentProperty() {
                return parent;
            }

            public Button getRemove() {
                return remove;
            }

            public Button getDetails() {
                return details;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminCategoryManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            currentController = this;
            initCategories();
            initTable();
            initActions();
        }

        private void initCategories() {
            var categories = adminController.manageCategories();
            wrappers = new ArrayList<>();
            for (String[] category : categories) {
                wrappers.add(new CategoryWrapper(category));
            }
        }

        private void initTable() {
            idCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            parentCOL.setCellValueFactory(new PropertyValueFactory<>("parent"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));

            this.categories.getItems().addAll(wrappers);
        }

        private void initActions() {
            addCategoryBTN.setOnAction(e -> AdminCategoryManagingPopupController.display(null));
        }
    }

    public static class AdminCategoryManagingPopupController {


        @FXML
        private TableView<String> properties;

        @FXML
        private TableColumn<String, String> propertyCOL;

        @FXML
        private TableView<MiniProductWrapper> products;

        @FXML
        private TableColumn<MiniProductWrapper, String> productCOL;

        @FXML
        private TableColumn<MiniProductWrapper, Button> productRemoveCOL;

        @FXML
        private TableView<SubCategoryWrapper> subCategories;

        @FXML
        private TableColumn<SubCategoryWrapper, String> subCategoryCOL;

        @FXML
        private TableColumn<SubCategoryWrapper, Button> subCategoryRemoveCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Label idKeyLBL;

        @FXML
        private Label idValueLBL;

        @FXML
        private TextField nameField;

        @FXML
        private TextField parentField;

        @FXML
        private Button addBTN;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private HBox editHB;

        @FXML
        private Tab productsTAB;

        @FXML
        private Tab subCategoriesTAB;

        private AdminCategoryManagingMenuController.CategoryWrapper category;
        private ArrayList<String> categoryProperties = new ArrayList<>();
        private ArrayList<MiniProductWrapper> categoryProducts = new ArrayList<>();
        private ArrayList<SubCategoryWrapper> categorySubCategories = new ArrayList<>();

        private SimpleBooleanProperty nameFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty parentFieldChanged = new SimpleBooleanProperty(false);

        public class MiniProductWrapper {
            String id, name, brand, category;
            Button remove = new Button();

            public MiniProductWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3]);
            }

            public MiniProductWrapper(String id, String name, String brand, String category) {
                this.id = id;
                this.name = name;
                this.brand = brand;
                this.category = category;

                remove.getStyleClass().add("remove-button");

                remove.setOnAction(e -> {
                    try {
                        adminController.removeProduct(this.id);
                    } catch (Exceptions.InvalidProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            public String getNameBrand() {
                return name + " (" + brand + ")";
            }

            public Button getRemove() {
                return remove;
            }
        }

        public class SubCategoryWrapper {
            String id, name;
            Button remove = new Button();

            public SubCategoryWrapper(String id, String name) {
                this.name = name;
                remove.getStyleClass().add("remove-button");

                remove.setOnAction(e -> {
                    try {
                        adminController.removeCategory(name);
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            public String getName() {
                return name;
            }

            public Button getRemove() {
                return remove;
            }
        }

        public static void display(AdminCategoryManagingMenuController.CategoryWrapper category) {
            ((AdminCategoryManagingPopupController)
                    View.popupWindow("Add category", Constants.FXMLs.adminCategoryManagingPopup, 800, 600)).initialize(category);
        }

        private void initialize(AdminCategoryManagingMenuController.CategoryWrapper category) {
            this.category = category;

            initVisibility();
            initValues();
            initBindings();
            initTable();
            initActions();
        }

        private void initVisibility() {
            boolean isDetail = category != null;

            editHB.setVisible(isDetail);
            addBTN.setVisible(! isDetail);
            idKeyLBL.setVisible(isDetail);
            idValueLBL.setVisible(isDetail);
            productsTAB.setDisable(! isDetail);
            subCategoriesTAB.setDisable(! isDetail);

        }

        private void initValues() {
            if (category != null) {
                nameField.setText(category.name.get());
                parentField.setText(category.parent.get());
                idValueLBL.setText(category.id);
            }
        }

        private void initBindings() {
            if (category != null) {
                nameFieldChanged.bind(
                        Bindings.when(nameField.textProperty().isEqualTo(category.name)).then(false).otherwise(true)
                );
                parentFieldChanged.bind(
                        Bindings.when(parentField.textProperty().isEqualTo(category.parent)).then(false).otherwise(true)
                );
                editBTN.disableProperty().bind(
                        Bindings.createObjectBinding(() -> {
                            if (nameFieldChanged.get() || parentFieldChanged.get()) return false;
                            else return true;
                        }, nameFieldChanged, parentFieldChanged)
                );
                editBTN.opacityProperty().bind(
                        Bindings.when(editBTN.disableProperty()).then(0.5).otherwise(1)
                );
            }
        }

        private void initTable() {
            productCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            productRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            subCategoryCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            subCategoryRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            initTableItems();
        }

        private void initTableItems() {
            if (category != null) {
                try {
                    categoryProperties = mainController.getPropertiesOfCategory(category.name.get(), false);
                    for (String[] subCategory : mainController.getSubCategoriesOfThisCategory(category.name.get())) {
                        categorySubCategories.add(new SubCategoryWrapper(subCategory[0], subCategory[1]));
                    }
                    for (String[] product : mainController.getProductsOfThisCategory(category.name.get())) {
                        categoryProducts.add(new MiniProductWrapper(product));
                    }
                } catch (Exceptions.InvalidCategoryException e) {
                    e.printStackTrace();
                }
            }
            //TODO: when property is done
        }

        private void initActions() {
            addBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        adminController.addCategory(nameField.getText(), parentField.getText(), categoryProperties);
                        String[] newCategory = adminController.getCategory(nameField.getText());
                        AdminCategoryManagingMenuController.currentController.addItem(newCategory);
                        properties.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                        printError("Invalid parent category.");
                    } catch (Exceptions.ExistingCategoryException ex) {
                        ex.printStackTrace();
                        printError("Sorry, this category already exists.");
                    }
                }
            });

            editBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        if (parentFieldChanged.get()) {
                            adminController.editCategory(category.name.get(), "parent", parentField.getText());
                            category.parent.set(parentField.getText());
                        }
                        if (nameFieldChanged.get()) {
                            adminController.editCategory(category.name.get(), "name", nameField.getText());
                            category.name.set(nameField.getText());
                        }
                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Changes saved successfully");
                    } catch (Exceptions.SubCategoryException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidCategoryException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidFieldException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    } catch (Exceptions.ExistingCategoryException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    } catch (Exceptions.SameAsPreviousValueException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            discardBTN.setOnAction(e -> properties.getScene().getWindow().hide());
        }

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }

        private boolean validateFields() {
            if ( ! nameField.getText().matches("\\w+")) {
                printError("Invalid characters in category name!");
                return false;
            } else if ( ! parentField.getText().matches("\\w+")) {
                printError("Invalid characters in parent category name!");
                return false;
            } else return true;
        }
    }



    public static class SellerAddProductPopupController implements Initializable{
        public static void display() {

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }
    }

    public static class SellerProductManagingMenuController implements Initializable {

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerProductManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
            initButtons();
        }

        private void initTable() {
            ArrayList<SellerSubProductWrapper> sellerProducts = new ArrayList<>();

            for (String[] product : sellerController.manageProducts()) {
                sellerProducts.add(new SellerSubProductWrapper(product));
            }
            productsTBL.getItems().setAll(sellerProducts);

            initColumns();
        }

        private void initColumns() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            priceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
            saleCOL.setCellValueFactory(new PropertyValueFactory<>("saleId"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initButtons() {
            addProductBTN.setOnAction(e -> SellerAddProductPopupController.display());
        }

        public class SellerSubProductWrapper {
            String productId, id, name, brand, saleId, nameBrand;
            double price;
            Button details = new Button(), remove = new Button();

            public SellerSubProductWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3], info[6], Double.parseDouble(info[4]));
            }

            public SellerSubProductWrapper(String productId, String id, String name, String brand, String saleId, double price) {
                this.productId = productId;
                this.id = id;
                this.name = name;
                this.brand = brand;
                this.saleId = saleId;
                this.price = price;
                nameBrand = this.name + " (" + this.brand + ")";

                details.getStyleClass().add("details-button");
                remove.getStyleClass().add("remove-button");

                details.setOnAction(e -> ProductDetailMenuController.display(productId, id, true));

                remove.setOnAction(e -> {
                    try {
                        sellerController.removeProduct(id);
                    } catch (Exceptions.InvalidProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            public String getNameBrand() {
                return nameBrand;
            }

            public String getProductId() {
                return productId;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getBrand() {
                return brand;
            }

            public String getSaleId() {
                return saleId;
            }

            public double getPrice() {
                return price;
            }

            public Button getDetails() {
                return details;
            }

            public Button getRemove() {
                return remove;
            }
        }

        @FXML
        private TableView<SellerSubProductWrapper> productsTBL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> idCol;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> nameCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Double> priceCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> saleCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> detailsCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addProductBTN;
    }

    public static class SellerLogMenuController implements Initializable {
        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerSellLogsManagingMenu);
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
            String subProductId;
            String productId;
            Button nameBrandSeller;
            double unitPrice;
            SimpleIntegerProperty countProperty = new SimpleIntegerProperty();
            TextField countField;
            HBox countGroup = new HBox();
            SimpleDoubleProperty totalPrice;
            Button remove;

            public SubProductWrapper(String[] productInCartPack) {
                this(productInCartPack[0], productInCartPack[1],
                        productInCartPack[2] + " " + productInCartPack[3] + " (" + productInCartPack[4] + ")",
                        Double.parseDouble(productInCartPack[7]), Integer.parseInt(productInCartPack[6]));
            }

            public SubProductWrapper(String id, String productId, String nameBrandSeller, double unitPrice, int count) {
                this.subProductId = id;
                this.productId = productId;
                this.nameBrandSeller = new Button(nameBrandSeller);
                this.nameBrandSeller.setOnAction(e -> ProductDetailMenuController.display(productId, subProductId, false));
                this.unitPrice = unitPrice;
                this.countProperty.set(count);
                this.totalPrice.bind(new SimpleDoubleProperty(unitPrice).multiply(countProperty));
                remove = new Button();
                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    try {
                        mainController.removeSubProductFromCart(this.subProductId);
                        productsTable.getItems().remove(this);
                    } catch (Exceptions.InvalidSubProductIdException ex) {
                        ex.printStackTrace();
                    }
                });
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
            purchaseBTN.setOnAction(e -> {
                if (productsTable.getItems().isEmpty()) {
                    errorLBL.setText("Cart is empty!");
                } else if (View.type.get().equals(Constants.anonymousUserType)) {
                    errorLBL.setText("Login First!");
                    LoginPopupController.display(new Stage());
                } else {
                    PurchaseMenuController.display();
                }
            });

            clearCartBTN.setOnAction(e -> {
                if (cartProducts.size() == 0) {
                    errorLBL.setText("The cart is already empty!");
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
            for (String[] cartProduct : cartProducts) {
                subProducts.add(new SubProductWrapper(cartProduct));
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

    public static class PurchaseMenuController implements Initializable {
        @FXML
        private TextField receiverName;

        @FXML
        private Label nameError;

        @FXML
        private TextField phoneNumber;

        @FXML
        private Label phoneError;

        @FXML
        private TextArea address;

        @FXML
        private TextField discountCode;

        @FXML
        private Button validateBTN;

        @FXML
        private Label discountError;

        @FXML
        private Label totalPrice;

        @FXML
        private Button purchaseBTN;

        public static void display() {
            View.setMainPane(Constants.FXMLs.purchaseMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initButtons();
            initBindings();
            initListeners();
        }

        private void initButtons() {
            purchaseBTN.setOnAction(e -> {
                try {
                    discountError.setText("");
                    customerController.purchaseTheCart(receiverName.getText(), address.getText(), phoneNumber.getText(), discountCode.getText());
                    mainController.clearCart();
                    View.goBack();
                } catch (Exceptions.InsufficientCreditException ex) {
                    ex.printStackTrace();
                } catch (Exceptions.NotAvailableSubProductsInCart notAvailableSubProductsInCart) {
                    notAvailableSubProductsInCart.printStackTrace();
                } catch (Exceptions.InvalidDiscountException ex) {
                    discountError.setText("Invalid discount code!");
                    ex.printStackTrace();
                } catch (Exceptions.EmptyCartException ex) {
                    ex.printStackTrace();
                }
            });
        }

        private void initBindings() {
            discountCode.disableProperty().bind(
                    Bindings.createObjectBinding(() -> !receiverName.getText().matches(Constants.IRLNamePattern) ||
                                    address.getText().equals("") || phoneNumber.getText().equals(""),
                            receiverName.textProperty(), address.textProperty(), phoneNumber.textProperty())
            );
            discountCode.opacityProperty().bind(Bindings.when(discountCode.disableProperty()).then(0.5).otherwise(1));

            purchaseBTN.disableProperty().bind(
                    Bindings.when(discountCode.disableProperty().not().and(discountCode.textProperty().isNotNull())).then(false).otherwise(true)
            );
            purchaseBTN.opacityProperty().bind(Bindings.when(purchaseBTN.disableProperty()).then(0.5).otherwise(1));
        }

        private void initListeners() {
            phoneNumber.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (!String.valueOf(lastInput).matches("[0-9]")) phoneNumber.setText(oldValue);
            }));
            discountCode.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.length() == 0) return;
                char lastInput = newValue.charAt(newValue.length() - 1);
                if (!String.valueOf(lastInput).matches("\\w")) discountCode.setText(oldValue);
            }));


        }
    }

    public static class CustomerBuyLogMenuController implements Initializable {

        public class BuyLogWrapper {
            String id, date, receiverUsername, receiverName, shippingStatus;
            double paidMoney, totalDiscount;
            Button details = new Button();

            public BuyLogWrapper(String[] info) {
                this(info[0], info[5], info[1], info[2], info[6], Double.parseDouble(info[7]), Double.parseDouble(info[8]));
            }

            public BuyLogWrapper(String id, String date, String receiverUsername, String receiverName, String shippingStatus, double paidMoney, double totalDiscount) {
                this.id = id;
                this.date = date;
                this.receiverUsername = receiverUsername;
                this.receiverName = receiverName;
                this.shippingStatus = shippingStatus;
                this.paidMoney = paidMoney;
                this.totalDiscount = totalDiscount;

                details.getStyleClass().add("details-button");
                details.setOnAction(e -> CustomerBuyLogDetailsPopupController.display(id));
            }

            public String getDate() {
                return date;
            }

            public String getId() {
                return id;
            }

            public String getReceiverUsername() {
                return receiverUsername;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public String getShippingStatus() {
                return shippingStatus;
            }

            public double getPaidMoney() {
                return paidMoney;
            }

            public double getTotalDiscount() {
                return totalDiscount;
            }

            public Button getDetails() {
                return details;
            }
        }

        @FXML
        private TableView<BuyLogWrapper> products;

        @FXML
        private TableColumn<BuyLogWrapper, String> dateCol;

        @FXML
        private TableColumn<BuyLogWrapper, String> customerCOL;

        @FXML
        private TableColumn<BuyLogWrapper, Double> paidMoneyCOL;

        @FXML
        private TableColumn<BuyLogWrapper, Double> discountAmountCOL;

        @FXML
        private TableColumn<BuyLogWrapper, String> shippingStatusCOL;

        @FXML
        private TableColumn<BuyLogWrapper, Button> detailsCOL;

        @FXML
        private Label errorLBL;

        public static void display() {
            View.setMainPane(Constants.FXMLs.customerBuyLogMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
        }

        private void initTable() {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            customerCOL.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
            paidMoneyCOL.setCellValueFactory(new PropertyValueFactory<>("paidMoney"));
            discountAmountCOL.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
            shippingStatusCOL.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
        }
    }

    public static class CustomerBuyLogDetailsPopupController {

        private ArrayList<BuyLogItemWrapper> buyItems;

        public static void display(String logId) {
            ((CustomerBuyLogDetailsPopupController)
            View.setMainPane(Constants.FXMLs.customerBuyLogDetailsPopup)).init(logId);
        }

        private void init(String logId) {
            try {
                ArrayList<String[]> logDetails = customerController.getOrderWithId(logId);
                initTable(logDetails.subList(1, logDetails.size()));
                initValues(logDetails.get(0));
            } catch (Exceptions.InvalidLogIdException e) {
                e.printStackTrace();
            } catch (Exceptions.CustomerLoginException e) {
                e.printStackTrace();
            }
        }

        private void initTable(List<String[]> items) {
            buyItems = new ArrayList<>();
            for (String[] item : items) {
                buyItems.add(new BuyLogItemWrapper(item));
            }

            logItems.getItems().addAll(buyItems);

            subProductCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrandSeller"));
            countCOL.setCellValueFactory(new PropertyValueFactory<>("count"));
            saleCOL.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
            unitPriceCOL.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        }

        private void initValues(String[] info) {
            idLBL.setText(info[0]);
            dateLBL.setText(info[5]);
            priceLBL.setText(info[7] + "$");
            discountLBL.setText(info[8]);
            shipStatusLBL.setText(info[6]);
            receiverNameLBL.setText(info[6]);
            receiverPhoneLBL.setText(info[3]);

            StringBuilder address = new StringBuilder(info[4]);
            int offset = 0 , size = address.length();
            while (offset + 19 < size) {
                offset += 19;
                address.insert(offset, "\n");
            }
            addressArea.setText(address.toString());
            addressArea.setEditable(false);

            double totalSale = 0;
            for (BuyLogItemWrapper buyItem : buyItems) {
                totalSale += buyItem.getSaleAmount();
            }

            saleLBL.setText(totalSale + "$");
        }

        public class BuyLogItemWrapper {
            String id, nameBrandSeller, sellerStoreName;
            int count;
            double unitPrice, saleAmount;

            private BuyLogItemWrapper(String[] info) {
                this(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[5]), Double.parseDouble(info[6]), Double.parseDouble(info[7]));
            }

            public BuyLogItemWrapper(String id, String name, String brand, String sellerName, String sellerStoreName, int count, double unitPrice, double saleAmount) {
                this.id = id;
                nameBrandSeller = name + " " + brand + "(" + sellerName + ")";
                this.sellerStoreName = sellerStoreName;
                this.count = count;
                this.unitPrice = unitPrice;
                this.saleAmount = saleAmount;
            }

            public String getId() {
                return id;
            }

            public String getNameBrandSeller() {
                return nameBrandSeller;
            }

            public String getSellerStoreName() {
                return sellerStoreName;
            }

            public int getCount() {
                return count;
            }

            public double getUnitPrice() {
                return unitPrice;
            }

            public double getSaleAmount() {
                return saleAmount;
            }
        }

        @FXML
        private TableView<BuyLogItemWrapper> logItems;

        @FXML
        private TableColumn<BuyLogItemWrapper, String> subProductCOL;

        @FXML
        private TableColumn<BuyLogItemWrapper, Integer> countCOL;

        @FXML
        private TableColumn<BuyLogItemWrapper, Double> unitPriceCOL;

        @FXML
        private TableColumn<BuyLogItemWrapper, Double> saleCOL;

        @FXML
        private Label idLBL;

        @FXML
        private Label receiverPhoneLBL;

        @FXML
        private Label receiverNameLBL;

        @FXML
        private Label saleLBL;

        @FXML
        private Label priceLBL;

        @FXML
        private Label dateLBL;

        @FXML
        private Label shipStatusLBL;

        @FXML
        private TextArea addressArea;

        @FXML
        private Label discountLBL;
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
            manageCategories.setOnAction(e -> AdminCategoryManagingMenuController.display());
            manageDiscounts.setOnAction(e -> AdminDiscountManagingMenuController.display());
            manageProducts.setOnAction(e -> AdminProductManagingMenu.display());
            manageRequests.setOnAction(e -> AdminRequestManagingMenuController.display());
        }
    }

    public static class AdminAccountManagingMenuController implements Initializable {

        public static AdminAccountManagingMenuController current;
        public ArrayList<AccountWrapper> allAdmins;
        public ArrayList<AccountWrapper> allCustomers;
        public ArrayList<AccountWrapper> allSellers;

        public class AccountWrapper {
            String id, username, firstName, lastName, phone, email, fullName;
            SimpleStringProperty type;
            Button remove = new Button();
            Button details = new Button();
            TableView holder;

            public AccountWrapper(String[] pack, TableView holder) {
                this(pack[0], pack[1], pack[2], pack[3], pack[4], pack[5], pack[6], holder);
            }

            public AccountWrapper(String id, String username, String firstName, String lastName, String phone, String email, String type, TableView holder) {
                this.id = id;
                this.username = username;
                this.firstName = firstName;
                this.lastName = lastName;
                this.phone = phone;
                this.email = email;
                this.type = new SimpleStringProperty(type);
                this.holder = holder;
                fullName = firstName + " " + lastName;

                this.remove.getStyleClass().add("remove-button");
                this.details.getStyleClass().add("details-button");
                initButtons();
            }

            private void initButtons() {
                remove.setOnAction(e -> {
                    try {
                        adminController.deleteUsername(username);
                        holder.getItems().remove(this);
                    } catch (Exceptions.UsernameDoesntExistException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.ManagerDeleteException ex) {
                        ex.printStackTrace();
                    }
                });

                remove.disableProperty().bind(
                        Bindings.when(View.isManager.or(type.isEqualTo(Constants.sellerUserType)).or(type.isEqualTo(Constants.customerUserType)))
                                .then(false).otherwise(true)
                );
                remove.opacityProperty().bind(Bindings.when(remove.disableProperty()).then(0.5).otherwise(1));

                details.setOnAction(e -> PersonalInfoMenuController.display(username));
            }

            public String getId() {
                return id;
            }

            public String getUsername() {
                return username;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public String getPhone() {
                return phone;
            }

            public String getEmail() {
                return email;
            }

            public String getFullName() {
                return fullName;
            }

            public Button getRemove() {
                return remove;
            }

            public Button getDetails() {
                return details;
            }
        }

        @FXML
        private TableView<AccountWrapper> admins;

        @FXML
        private TableColumn<AccountWrapper, String> adminIdCOL;

        @FXML
        private TableColumn<AccountWrapper, String> adminUsernameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> adminFullNameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> adminPhoneCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> adminDetailsCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> adminRemoveCOL;

        @FXML
        private Button registerAdminBTN;

        @FXML
        private TableView<AccountWrapper> sellers;

        @FXML
        private TableColumn<AccountWrapper, String> sellerIdCOL;

        @FXML
        private TableColumn<AccountWrapper, String> sellerUsernameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> sellerFullNameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> sellerPhoneCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> sellerDetailsCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> sellerRemoveCOL;

        @FXML
        private TableView<AccountWrapper> customers;

        @FXML
        private TableColumn<AccountWrapper, String> customerIdCOL;

        @FXML
        private TableColumn<AccountWrapper, String> customerUsernameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> customerFullNameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> customerPhoneCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> customerDetailsCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> customerRemoveCOL;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminAccountManagingMenu);
        }

        public void addAdmin(String username) {
            try {
                String[] info = adminController.viewUsername(username);
                admins.getItems().add(new AccountWrapper(info[2], username, info[3], info[4], info[6], info[5], Constants.adminUserType, admins));
            } catch (Exceptions.UsernameDoesntExistException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            current = this;
            initActions();
            initTable();
        }

        private void initActions() {
            registerAdminBTN.setOnAction(e -> AdminRegistrationPopupController.display());
        }

        private void initTable() {
            adminIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            sellerIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            adminUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            sellerUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            customerUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            adminFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            sellerFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            customerFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            adminPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            sellerPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            adminDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            sellerDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            customerDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            adminRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            sellerRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            customerRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));

            initItems();
        }

        private void initItems() {
            ArrayList<String[]> all = adminController.manageUsers();
            allSellers = new ArrayList<>();
            allCustomers = new ArrayList<>();
            allAdmins = new ArrayList<>();

            for (String[] strings : all) {
                if (strings[6].equals("Seller")) {
                    allSellers.add(new AccountWrapper(strings, sellers));
                } else if (strings[6].equals("Customer")) {
                    allCustomers.add(new AccountWrapper(strings, customers));
                } else {
                    allAdmins.add(new AccountWrapper(strings, admins));
                }
            }

            admins.getItems().addAll(allAdmins);
            sellers.getItems().addAll(allSellers);
            customers.getItems().addAll(allCustomers);
        }
    }

    public static class AdminRegistrationPopupController implements Initializable {
        @FXML
        private TextField adminUsername;

        @FXML
        private Label adminUsernameError;

        @FXML
        private PasswordField adminPassword;

        @FXML
        private TextField showPasswordFIeld;

        @FXML
        private ToggleButton showPasswordBTN;

        @FXML
        private Label adminPasswordError;

        @FXML
        private TextField adminFirstName;

        @FXML
        private Label adminFirstNameError;

        @FXML
        private TextField adminLastName;

        @FXML
        private Label adminLastNameError;

        @FXML
        private TextField adminPhoneNumber;

        @FXML
        private Label adminPhoneNumberError;

        @FXML
        private TextField adminEmail;

        @FXML
        private Label adminEmailError;

        @FXML
        private TextField adminBalance;

        @FXML
        private Label adminBalanceError;

        @FXML
        private TextField adminStoreName;

        @FXML
        private Label adminStoreNameError;

        @FXML
        private TextField adminImageField;

        @FXML
        private Button adminBrowseBTN;

        @FXML
        private Label adminImageError;

        @FXML
        private Button adminRegister;


        public static void display() {
            View.popupWindow("Admin registration window", Constants.FXMLs.adminRegistrationPopup, 500, 700);
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            adminImageField.setEditable(false);
            initActions();
            initLBLs();
            initVisibilities();
            initListeners();
        }

        private void initActions() {
            adminBrowseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (chosenFile != null) {
                    adminImageField.setText(chosenFile.getPath());
                }
            });

            adminRegister.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        adminController.creatAdminProfile(adminUsername.getText(), adminPassword.getText(), adminFirstName.getText(),
                                adminLastName.getText(), adminEmail.getText(), adminPhoneNumber.getText(), adminImageField.getText());
                        AdminAccountManagingMenuController.current.addAdmin(adminUsername.getText());
                        adminUsername.getScene().getWindow().hide();
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        adminUsernameError.setText("Sorry! this username is already taken.");
                        adminUsernameError.setVisible(true);
                        ex.printStackTrace();
                    }
                }
            });
        }

        private boolean validateFields() {
            boolean valid = true;
            if (adminUsername.getText().equals("")) {
                adminUsernameError.setText("You can only use alphabet, digits and \"_\"");
                adminUsernameError.setVisible(true);
                valid = false;
            } else adminUsernameError.setVisible(false);

            if (adminPassword.getText().equals("")) {
                adminPasswordError.setVisible(true);
                valid = false;
            } else adminPasswordError.setVisible(false);

            if (!adminFirstName.getText().matches(Constants.IRLNamePattern)) {
                adminFirstNameError.setVisible(true);
                valid = false;
            } else adminFirstNameError.setVisible(false);

            if (!adminLastName.getText().matches(Constants.IRLNamePattern)) {
                adminLastNameError.setVisible(true);
                valid = false;
            } else adminLastNameError.setVisible(false);

            if (adminPhoneNumber.getText().equals("")) {
                adminPhoneNumberError.setVisible(true);
                valid = false;
            } else adminPhoneNumberError.setVisible(false);

            if (!adminEmail.getText().matches(Constants.emailPattern)) {
                adminEmailError.setVisible(true);
                valid = false;
            } else adminEmailError.setVisible(false);

            return valid;
        }

        private void initLBLs() {
            adminPasswordError.setText("You can only use alphabet, digits and \"_\"");
            adminFirstNameError.setText("Invalid first name!");
            adminLastNameError.setText("Invalid last name");
            adminPhoneNumberError.setText("Enter a phone number!");
            adminEmailError.setText("Invalid email address!");
        }

        private void initVisibilities() {
            adminUsernameError.setVisible(false);
            adminPasswordError.setVisible(false);
            adminFirstNameError.setVisible(false);
            adminLastNameError.setVisible(false);
            adminPhoneNumberError.setVisible(false);
            adminEmailError.setVisible(false);
            adminImageError.setVisible(false);
        }

        private void initListeners() {
            View.addListener(adminUsername, "\\w");
            View.addListener(adminPassword, "\\w");
            View.addListener(adminPhoneNumber, "[0-9]");
        }
    }

    public static class AdminDiscountManagingPopupController {

        @FXML
        private Label idKeyLBL;

        @FXML
        private Label idValueLBL;

        @FXML
        private TableView<CustomerWrapper> customersTable;

        @FXML
        private TableColumn<CustomerWrapper, String> usernameCOL;

        @FXML
        private TableColumn<CustomerWrapper, TextField> countCOL;

        @FXML
        private TableColumn<CustomerWrapper, CheckBox> removeCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Label idLBL;

        @FXML
        private TextField codeField;

        @FXML
        private TextField percentageField;

        @FXML
        private TextField maxField;

        @FXML
        private DatePicker startDate;

        @FXML
        private DatePicker endDate;

        @FXML
        private Button addBTN;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private HBox saveDiscardHBox;

        private String[] discountInfo;
        private AdminDiscountManagingMenuController.DiscountWrapper discount;
        private ArrayList<CustomerWrapper> allCustomers = new ArrayList<>();
        private ArrayList<CustomerWrapper> removedCustomers = new ArrayList<>();
        private ArrayList<CustomerWrapper> customersWithDiscount = new ArrayList<>();

        private SimpleBooleanProperty codeFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty percentageFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty maxFieldChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        public class CustomerWrapper {
            CheckBox hasCode;
            String id;
            String username;
            TextField count = new TextField();
            int initCount;

            public CustomerWrapper(String[] customerPack, boolean hasCode) {
                this(customerPack[2], customerPack[0], Integer.parseInt(customerPack[1]), true);
            }

            public CustomerWrapper(String id, String username, int count, boolean hasCode) {
                if (hasCode) this.count.setText(count + "");
                else this.count.setText("");
                this.id = id;
                this.username = username;
                this.hasCode.setSelected(hasCode);
                this.initCount = count;
                this.count.editableProperty().bind(this.hasCode.selectedProperty());
                this.count.opacityProperty().bind(
                        Bindings.when(this.hasCode.selectedProperty()).then(1).otherwise(0.5)
                );
                View.addListener(this.count, Constants.unsignedIntPattern);
                this.hasCode.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        removedCustomers.remove(this);
                        this.count.setText("1");
                    } else {
                        removedCustomers.add(this);
                        this.count.setText("");
                    }
                });
            }

            public Property hasCodeProperty() {
                return hasCode.selectedProperty();
            }

            public CheckBox getHasCode() {
                return hasCode;
            }

            public String getId() {
                return id;
            }

            public String getUsername() {
                return username;
            }

            public TextField getCount() {
                return count;
            }

            @Override
            public boolean equals(Object obj) {
                return this.id.equals(((CustomerWrapper)obj).id);
            }
        }

        public static void display(AdminDiscountManagingMenuController.DiscountWrapper discount, boolean editable) {
            String discountId = discount == null ? null : discount.getId();
            ((AdminDiscountManagingPopupController)
                    View.popupWindow((discountId == null) ? "Create Discount":"Discount Details", Constants.FXMLs.adminDiscountManagingPopup, 800, 500)).initialize(discountId, discount, editable);
        }

        private void initialize(String discountId, AdminDiscountManagingMenuController.DiscountWrapper discount, boolean editable) {
            ArrayList<String[]> customersWithCode = null;

            if (discountId != null) {
                try {
                    this.discount = discount;
                    discountInfo = adminController.viewDiscountCodeById(discountId);
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                    return;
                }
            }

            initVisibility(discountId, editable);
            initActions(discountId);
            initTable(discountId);
            initBindings(discountId);
            initValues(discountId);
        }

        private void initBindings(String discountId) {
            if (discountId != null) {
                discount.startDate.set("20" + discount.startDate.get());
                discount.endDate.set("20" + discount.endDate.get());

                codeFieldChanged.bind(Bindings.when(codeField.textProperty().isEqualTo(discount.getCode())).then(false).otherwise(true));
                percentageFieldChanged.bind(Bindings.when(percentageField.textProperty().isEqualTo(discount.percentage.asString())).then(false).otherwise(true));
                maxFieldChanged.bind(Bindings.when(maxField.textProperty().isEqualTo(discount.maximumAmount.asString())).then(false).otherwise(true));
                startDateChanged.bind(Bindings.when(startDate.valueProperty().isEqualTo(LocalDate.parse(discount.startDate.get()))).then(false).otherwise(true));
                endDateChanged.bind(Bindings.when(endDate.valueProperty().isEqualTo(LocalDate.parse(discount.endDate.get()))).then(false).otherwise(true));
            }
        }

        private void initVisibility(String discountId, boolean editable) {
            boolean isDetail = discountId != null;
            saveDiscardHBox.setVisible(isDetail && editable);
            addBTN.setVisible( ! isDetail && editable);
            idKeyLBL.setVisible(isDetail);
            idValueLBL.setVisible(isDetail);

            editBTN.opacityProperty().bind(
                    Bindings.createObjectBinding(() -> {
                        if (codeFieldChanged.get() || percentageFieldChanged.get() || maxFieldChanged.get()
                        || endDateChanged.get() || startDateChanged.get()) return 1;
                        else return 0.5;
                    }, codeFieldChanged, percentageFieldChanged, maxFieldChanged, endDateChanged, startDateChanged)
            );

            editBTN.disableProperty().bind(editBTN.opacityProperty().isNotEqualTo(1));

            codeField.setEditable( ! isDetail);

            maxField.setEditable(editable);
            codeField.setEditable(editable);
            percentageField.setEditable(editable);
        }

        private void initActions(String discountId) {
            addBTN.setOnAction(e -> {
                if (fieldValidation()) {
                    try {
                        adminController.createDiscountCode(codeField.getText(), startDate.getValue().toString(), endDate.getValue().toString(),
                                Double.parseDouble(percentageField.getText()), Double.parseDouble(maxField.getText()),
                                allCustomers.stream().filter(c -> c.hasCode.isSelected()).map(c -> new String[]{c.id, String.valueOf(c.count)}).collect(Collectors.toCollection(ArrayList::new)));
                        AdminDiscountManagingMenuController.currentObject.addDiscount(adminController.viewDiscountCodeByCode(codeField.getText()));
                        customersTable.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidAccountsForDiscount invalidAccountsForDiscount) {
                        invalidAccountsForDiscount.printStackTrace();
                    } catch (Exceptions.InvalidFormatException ex) {
                        ex.printStackTrace();
                        printError("Invalid date format question mark.");
                    } catch (Exceptions.ExistingDiscountCodeException ex) {
                        ex.printStackTrace();
                        printError("This code already exists!");
                    } catch (Exceptions.DiscountCodeException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            editBTN.setOnAction(e -> {
                if (fieldValidation()) {
                    try {
                        if (percentageFieldChanged.get()) {
                            adminController.editDiscountCode(discountInfo[1], "percentage", percentageField.getText());
                            discount.percentage.set(Double.parseDouble(percentageField.getText()));
                        }
                        if (maxFieldChanged.get()) {
                            adminController.editDiscountCode(discountInfo[1], "maximum amount", maxField.getText());
                            discount.maximumAmount.set(Double.parseDouble(maxField.getText()));
                        }
                        if (startDateChanged.get()) {
                            adminController.editDiscountCode(discountInfo[1], "start date", startDate.getValue().toString());
                            discount.startDate.set(startDate.getValue().toString());
                        }
                        if (endDateChanged.get()) {
                            adminController.editDiscountCode(discountInfo[1], "end date", endDate.getValue().toString());
                            discount.endDate.set(endDate.getValue().toString());
                        }

                        adminController.setAccounts(discountInfo[1],
                                customersTable.getItems().stream().filter(cw -> cw.hasCode.isSelected()).
                                        map(cw -> new String[]{cw.getId(), cw.getCount().getText()}).collect(Collectors.toCollection(ArrayList::new)));
                        adminController.removeAccountsFromDiscount(discountInfo[1],
                                removedCustomers.stream().map(cw -> cw.getId()).collect(Collectors.toCollection(ArrayList::new)));

                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Changed saved successfully!");
                    } catch (Exception ex) {
                        printError(ex.getMessage());
                    }
                }
            });

            discardBTN.setOnAction(e -> customersTable.getScene().getWindow().hide());
        }

        private void printError(String errorText) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(errorText);
        }

        private boolean fieldValidation() {
            if ( ! codeField.getText().matches("^\\w+$")) {
                printError("Invalid discount code! use only characters, digits and _ .");
                return false;
            } else if ( ! percentageField.getText().matches(Constants.doublePattern)) {
                printError("Invalid percentage! enter a floating point number (ex. 50.5)");
                return false;
            } else if ( ! maxField.getText().matches(Constants.doublePattern)) {
                printError("Invalid maximum amount! enter a floating point number (ex. 40.5)");
                return false;
            } else if ( startDate.getValue() == null) {
                printError("Please enter a valid starting date");
                return false;
            } else if ( endDate.getValue() == null || endDate.getValue().compareTo(startDate.getValue()) <= 0) {
                printError("Please enter a valid ending date.");
                return false;
            } else return true;
        }

        private void initTable(String discountId) {
            //CheckBox hasCode;
            //            String id;
            //            String username;
            //            TextField count;
            countCOL.setCellValueFactory(new PropertyValueFactory<>("count"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("hasCode"));
            usernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));

            initTableContent(discountId);
        }

        private void initTableContent(String discountId) {
            ArrayList<String[]> withCode = new ArrayList<>();
            if (discountId != null) {
                try {
                    withCode = adminController.peopleWhoHaveThisDiscount(discountId);
                } catch (Exceptions.DiscountCodeException e) {
                    return;
                }
            }

            for (String[] customer : withCode) {
                customersWithDiscount.add(new CustomerWrapper(customer, true));
            }
            allCustomers.addAll(customersWithDiscount);

            for (String[] user : adminController.manageUsers()) {
                if (user[6].equals(Constants.customerUserType)) {
                    CustomerWrapper cw = new CustomerWrapper(user[0], user[1], 0, false);
                    if ( ! customersWithDiscount.contains(cw)) {
                        allCustomers.add(cw);
                    }
                }
            }
            customersTable.getItems().setAll(allCustomers);
        }

        private void initValues(String discountId) {
            if (discountId != null) {
                idValueLBL.setText(discount.getId());
                codeField.setText(discount.code);
                percentageField.setText(discount.percentage.get() + "");
                maxField.setText(discount.maximumAmount.get() + "");
                startDate.setValue(LocalDate.parse(discount.startDate.get()));
                endDate.setValue(LocalDate.parse(discount.endDate.get()));
            }
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

    public static class SellerSaleManagingMenuController implements Initializable {
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

        public class SaleWrapper {
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

                details.getStyleClass().add("details-button");
                details.setOnAction(e -> SellerSaleManagingPopupController.display(id, true));
            }

            public String getId() {
                return id;
            }

            public String getSeller() {
                return seller;
            }

            public String getStartDate() {
                return startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public double getPercentage() {
                return percentage;
            }

            public int getNumOfProducts() {
                return numOfProducts;
            }

            public Button getRemove() {
                return remove;
            }

            public Button getDetails() {
                return details;
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

            sales.getItems().setAll(sellerSales);
        }

        private void initActions() {
            addSaleBTN.setOnAction(e -> SellerSaleManagingPopupController.display(null, true));
        }
    }

    public static class SellerSaleManagingPopupController {

        private ArrayList<ProductInSaleWrapper> inSales = new ArrayList<>();
        private ArrayList<ProductInSaleWrapper> removedFormSale = new ArrayList<>();
        private ArrayList<ProductInSaleWrapper> addedToSale = new ArrayList<>();

        private String[] sale;

        public class ProductInSaleWrapper {
            CheckBox hasSale = new CheckBox();
            String productId, name, brand;

            public ProductInSaleWrapper(String productId, String name, String brand, boolean hasSale) {
                this.productId = productId;
                this.name = name;
                this.brand = brand;
                this.hasSale.setSelected(hasSale);

                this.hasSale.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        inSales.add(this);
                        addedToSale.add(this);
                        removedFormSale.remove(this);
                    } else {
                        inSales.remove(this);
                        addedToSale.remove(this);
                        removedFormSale.add(this);
                    }
                });
            }

            public String getNameBrand() {
                return name + " (" + brand + ")";
            }

            public CheckBox getHasSale() {
                return hasSale;
            }

            public String getProductId() {
                return productId;
            }

            @Override
            public boolean equals(Object o) {
                ProductInSaleWrapper p = (ProductInSaleWrapper) o;
                return this.productId.equals(p.productId);
            }
        }

        public static void display(String saleId, boolean editable) {
            String title = saleId == null ? "Add Sale" : "Sale Details";
            ((SellerSaleManagingPopupController)
            View.popupWindow(title, Constants.FXMLs.sellerSaleManagingPopup, 650, 500)).initialize(saleId, editable);
        }

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }

        private void initialize(String saleId, boolean editable) {
            if (saleId != null) {
                try {
                    sale = sellerController.viewSaleWithId(saleId);
                } catch (Exceptions.InvalidSaleIdException e) {
                    e.printStackTrace();
                }
            }
            initTable(saleId);
            initValues(saleId);
            initButtons(saleId);
            initBindings(saleId);
            initVisibilities(saleId, editable);
        }

        private void initTable(String saleId) {
            selectCOL.setCellValueFactory(new PropertyValueFactory<>("hasSale"));
            nameBrandCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));

            initItems(saleId);
        }

        private void initItems(String saleId) {
            ArrayList<ProductInSaleWrapper> allProducts = new ArrayList<>();
            if (saleId == null) {
                for (String[] product : sellerController.manageProducts()) {
                    allProducts.add(new ProductInSaleWrapper(product[0], product[2], product[3], false));
                }
            } else {
                try {
                    for (String[] product : sellerController.getProductsInSale(saleId)) {
                        inSales.add(new ProductInSaleWrapper(product[0], product[1], product[2], true));
                    }
                    allProducts.addAll(inSales);

                    for (String[] product : sellerController.manageProducts()) {
                        ProductInSaleWrapper p = new ProductInSaleWrapper(product[0], product[2], product[3], false);
                        if ( ! inSales.contains(p)) allProducts.add(p);
                    }
                } catch (Exceptions.InvalidSaleIdException e) {
                    e.printStackTrace();
                }
            }

            products.getItems().setAll(allProducts);
        }

        private void initValues(String saleId) {
            if (saleId != null) {
                sale[3] = "20" + sale[3];
                sale[4] = "20" + sale[4];
                percentageField.setText(sale[2]);
                maxField.setText(sale[6]);
                startDate.setValue(LocalDate.parse(sale[3]));
                endDate.setValue(LocalDate.parse(sale[4]));
            }
        }

        private void initButtons(String saleId) {
            addBTN.setOnAction(e -> {
                if (validateFields()) {
                    ArrayList<String> productIds;
                    productIds = inSales.stream().map(ProductInSaleWrapper::getProductId).collect(Collectors.toCollection(ArrayList::new));
                    try {
                        sellerController.addSale(startDate.getValue().toString(), endDate.getValue().toString(),
                                Double.parseDouble(percentageField.getText()), Double.parseDouble(maxField.getText()), productIds);
                        products.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidDateException ex) {
                        printError("Dates selected do not match");
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidProductIdsForASeller invalidProductIdsForASeller) {
                        invalidProductIdsForASeller.printStackTrace();
                    } catch (Exceptions.InvalidFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            editBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        if (percentageChanged.get())
                            sellerController.editSale(sale[0], "percentage", percentageField.getText());
                        if (maxFieldChanged.get())
                            sellerController.editSale(sale[0], "maximum", maxField.getText());
                        if (startDateChanged.get())
                            sellerController.editSale(sale[0], "start date", startDate.getValue().toString());
                        if (endDateChanged.get())
                            sellerController.editSale(sale[0], "end date", endDate.getValue().toString());
                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Edition request has been sent!");

                    } catch (Exceptions.InvalidDateException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidFieldException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidFormatException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidSaleIdException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.SameAsPreviousValueException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            discardBTN.setOnAction(e -> products.getScene().getWindow().hide());
        }

        private boolean validateFields() {
            if( ! percentageField.getText().matches(Constants.doublePattern)) {
                printError("Invalid percentage! (ex. 33.33)");
                return false;
            } else if ( ! maxField.getText().matches(Constants.doublePattern)) {
                printError("Invalid maximum amount! (ex. 25.75)");
                return false;
            } else if (startDate.getValue() == null) {
                printError("Invalid start date!");
                return false;
            } else if (endDate.getValue() == null) {
                printError("Invalid end date");
                return false;
            } else return true;
        }

        private void initBindings(String saleId) {
            if(saleId != null) {
                percentageChanged.bind(
                        Bindings.when(percentageField.textProperty().isEqualTo(sale[2])).then(false).otherwise(true)
                );
                maxFieldChanged.bind(
                        Bindings.when(maxField.textProperty().isEqualTo(sale[6])).then(false).otherwise(true)
                );
                startDateChanged.bind(
                        Bindings.when(startDate.valueProperty().isEqualTo(LocalDate.parse(sale[3]))).then(false).otherwise(true)
                );
                endDateChanged.bind(
                        Bindings.when(endDate.valueProperty().isEqualTo(LocalDate.parse(sale[4]))).then(false).otherwise(true)
                );

                editBTN.disableProperty().bind(
                        Bindings.createObjectBinding(() -> {
                            if (endDateChanged.get() || startDateChanged.get() || maxFieldChanged.get() || percentageChanged.get()) {
                                return false;
                            } else return true;
                        }, endDateChanged,  startDateChanged, maxFieldChanged, percentageChanged)
                );
                editBTN.opacityProperty().bind(
                        Bindings.when(editBTN.disableProperty()).then(0.5).otherwise(1)
                );
            }
        }

        private void initVisibilities(String saleId, boolean editable) {
            editHB.setVisible((saleId != null) && editable);
            addBTN.setVisible((! editHB.isVisible()) && editable);
            idKeyLBL.setVisible(saleId != null);
            idValueLBL.setVisible(saleId != null);

            percentageField.setEditable(editable);
            maxField.setEditable(editable);
            startDate.setEditable(editable);
            endDate.setEditable(editable);
        }

        @FXML
        private Label idKeyLBL;

        @FXML
        private Label idValueLBL;

        @FXML
        private TableView<ProductInSaleWrapper> products;

        @FXML
        private TableColumn<ProductInSaleWrapper, CheckBox> selectCOL;

        @FXML
        private TableColumn<ProductInSaleWrapper, String> nameBrandCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Label idLBL;

        @FXML
        private TextField percentageField;
        private SimpleBooleanProperty percentageChanged = new SimpleBooleanProperty(false);

        @FXML
        private TextField maxField;
        private SimpleBooleanProperty maxFieldChanged = new SimpleBooleanProperty(false);

        @FXML
        private DatePicker startDate;
        private SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);

        @FXML
        private DatePicker endDate;
        private SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        @FXML
        private Button addBTN;

        @FXML
        private HBox editHB;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

    }

    public static class SellerProductManagingPopupController implements Initializable {
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
            manageSales.setOnAction(e -> SellerSaleManagingMenuController.display());
            sellLogs.setOnAction(e -> SellerSellLogsManagingMenuController.display());
        }
    }

    public static class SellerSellLogsManagingMenuController implements Initializable {

        @FXML
        private TableView<SellLogWrapper> sellLogs;

        @FXML
        private TableColumn<SellLogWrapper, String> dateCol;

        @FXML
        private TableColumn<SellLogWrapper, String> customerCOL;

        @FXML
        private TableColumn<SellLogWrapper, Double> receivedMoneyCOL;

        @FXML
        private TableColumn<SellLogWrapper, Double> saleAmountCOL;

        @FXML
        private TableColumn<SellLogWrapper, String> shippingStatusCOL;

        @FXML
        private TableColumn<SellLogWrapper, Button> detailsCOL;

        @FXML
        private Label errorLBL;

        private ArrayList<SellLogWrapper> allSellLogs = new ArrayList<>();

        public static class SellLogWrapper {
            String id, date, username, receiverName, receiverPhone, receiverAddress, shippingStatus;
            Double receivedMoney, totalSale;
            Button details = new Button();

            public SellLogWrapper(String[] info) {
                this(info[0], info[1], info[2], info[5], info[6], info[7], info[8], Double.parseDouble(info[3]), Double.valueOf(info[4]));
            }

            public SellLogWrapper(String id, String date, String username, String receiverName, String receiverPhone,
                                  String receiverAddress, String shippingStatus, Double receivedMoney, Double totalSale) {
                this.id = id;
                this.date = date;
                this.username = username;
                this.receiverName = receiverName;
                this.receiverPhone = receiverPhone;
                this.receiverAddress = receiverAddress;
                this.shippingStatus = shippingStatus;
                this.receivedMoney = receivedMoney;
                this.totalSale = totalSale;

                details.getStyleClass().add("details-button");
                details.setOnAction(e -> SellerSellLogDetailsPopupController.display(this.id));
            }

            public String getId() {
                return id;
            }

            public String getDate() {
                return date;
            }

            public String getUsername() {
                return username;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public String getReceiverPhone() {
                return receiverPhone;
            }

            public String getReceiverAddress() {
                return receiverAddress;
            }

            public String getShippingStatus() {
                return shippingStatus;
            }

            public Double getReceivedMoney() {
                return receivedMoney;
            }

            public Double getTotalSale() {
                return totalSale;
            }

            public Button getDetails() {
                return details;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerSellLogsManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
        }

        private void initTable() {
            initColumns();
            initTableItems();
        }

        private void initColumns() {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            customerCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            receivedMoneyCOL.setCellValueFactory(new PropertyValueFactory<>("receivedMoney"));
            saleAmountCOL.setCellValueFactory(new PropertyValueFactory<>("totalSale"));
            shippingStatusCOL.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
        }

        private void initTableItems() {
            for (String[] sellLog : sellerController.getAllSellLogs()) {
                allSellLogs.add(new SellLogWrapper(sellLog));
            }

            sellLogs.getItems().addAll(allSellLogs);
        }
    }

    public static class SellerSellLogDetailsPopupController {

        @FXML
        private TableView<SellLogItemWrapper> logItems;

        @FXML
        private TableColumn<SellLogItemWrapper, String> subProductCOL;

        @FXML
        private TableColumn<SellLogItemWrapper, Integer> countCOL;

        @FXML
        private TableColumn<SellLogItemWrapper, Double> priceCOL;

        @FXML
        private TableColumn<SellLogItemWrapper, Double> saleCOL;

        @FXML
        private Label idLBL;

        @FXML
        private Label customerLBL;

        @FXML
        private Label phoneLBL;

        @FXML
        private Label nameLBL;

        @FXML
        private Label saleLBL;

        @FXML
        private Label priceLBL;

        @FXML
        private Label dateLBL;

        @FXML
        private Label shipStatusLBL;

        @FXML
        private TextArea addressArea;

        SellerSellLogsManagingMenuController.SellLogWrapper sellLog;
        ArrayList<String[]> sellItems = new ArrayList<>();

        public class SellLogItemWrapper {
            String id, name, brand;
            Double price, saleAmount;
            int count;

            public SellLogItemWrapper(String[] pack) {
                this(pack[0], pack[1], pack[2], Double.parseDouble(pack[4]), Double.parseDouble(pack[5]), Integer.parseInt(pack[3]));
            }

            public SellLogItemWrapper(String id, String name, String brand, Double price, Double saleAmount, int count) {
                this.id = id;
                this.name = name;
                this.brand = brand;
                this.price = price;
                this.saleAmount = saleAmount;
                this.count = count;
            }

            public String nameBrand() {
                return name + " (" + brand + ")";
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getBrand() {
                return brand;
            }

            public Double getPrice() {
                return price;
            }

            public Double getSaleAmount() {
                return saleAmount;
            }

            public int getCount() {
                return count;
            }
        }

        public static void display(String id) {
            ((SellerSellLogDetailsPopupController)
                    View.popupWindow("Sell Log Details", Constants.FXMLs.sellerSellLogDetailsPopup, 850, 500)).initialize(id);
        }

        public void initialize(String id) {
            ArrayList<String[]> info;
            try {
                info = sellerController.getSellLogWithId(id);
            } catch (Exceptions.InvalidLogIdException e) {
                e.printStackTrace();
                return;
            }
            sellLog = new SellerSellLogsManagingMenuController.SellLogWrapper(info.remove(0));
            sellItems.addAll(info);

            initValues();
            initTable();
        }

        private void initValues() {
            idLBL.setText(sellLog.id);
            customerLBL.setText(sellLog.username);
            dateLBL.setText(sellLog.date);
            nameLBL.setText(sellLog.receiverName);
            phoneLBL.setText(sellLog.receiverPhone);
            priceLBL.setText(sellLog.receivedMoney + "");
            saleLBL.setText(sellLog.totalSale + "");
            shipStatusLBL.setText(sellLog.shippingStatus);

            StringBuilder address = new StringBuilder(sellLog.receiverAddress);
            int offset = 0 , size = address.length();
            while (offset + 19 < size) {
                offset += 19;
                address.insert(offset, "\n");
            }
            addressArea.setText(address.toString());
            addressArea.setEditable(false);
        }

        private void initTable() {
            initColumns();
            initItems();
        }

        private void initColumns() {
            subProductCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            countCOL.setCellValueFactory(new PropertyValueFactory<>("count"));
            saleCOL.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
            priceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

        private void initItems() {
            ArrayList<SellLogItemWrapper> items = new ArrayList<>();
            for (String[] sellItem : sellItems) {
                items.add(new SellLogItemWrapper(sellItem));
            }

            logItems.getItems().addAll(items);
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
            logoBTN.setOnAction(e -> MainMenuController.display());
            accountBTN.setOnAction(e -> PersonalInfoMenuController.display(null));
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
        }

        private void search(String input) {
            if (input != null) {
                ArrayList<String[]> products = getCurrentProducts();
                if (products != null) {
                        ProductsMenuController.display("SuperCategory", false);
                }
            }
        }

        //search utils.
        private ArrayList<String[]> getCurrentProducts() {
            try {
                return new ArrayList<>( mainController.getProductsOfThisCategory(Constants.SUPER_CATEGORY_NAME));

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

    public static class AddProductPopupController_Page1 implements Initializable {

        @FXML
        private TextField ameField;

        @FXML
        private PasswordField brandField;

        @FXML
        private Label errorLBL;

        @FXML
        private Button newProductBTN;

        @FXML
        private Button existingProductBTN;

        public static void display() {
            View.popupWindow("Add new Product (1 of 2)", Constants.FXMLs.addProductPage1, 600, 400);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initButtons();
        }

        private void initButtons() {
            newProductBTN.setOnAction(e -> {
                if (validateFields()) {
                    String productId = sellerController.isProductWithNameAndBrand(ameField.getText(), brandField.getText());
                    if (productId != null) {
                        printError("This product already exits!");
                    } else {
                        AddProductPopupController_Page2.display(ameField.getText(), brandField.getText(), productId);
                        ameField.getScene().getWindow().hide();
                    }
                }
            });

            existingProductBTN.setOnAction(e -> {
                if (validateFields()) {
                    String productId = sellerController.isProductWithNameAndBrand(ameField.getText(), brandField.getText());
                    if (productId == null) {
                        printError("There is no such product!");
                    } else {
                        AddProductPopupController_Page2.display(ameField.getText(), brandField.getText(), productId);
                        ameField.getScene().getWindow().hide();
                    }
                }
            });
        }

        private boolean validateFields() {
            if (ameField.getText().equals("")) {
                printError("Enter a name");
                return false;
            }
            if (brandField.getText().equals("")) {
                printError("Enter a brand");
                return false;
            }

            return true;
        }

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }
    }

    public static class AddProductPopupController_Page2 {

        @FXML
        private TextField nameField;

        @FXML
        private Label usernameErrLBL;

        @FXML
        private TextField brandField;

        @FXML
        private Label passwordErrLBL;

        @FXML
        private ChoiceBox<String> category;

        @FXML
        private TextField imageField;

        @FXML
        private Button browseBTN;

        @FXML
        private Label imageErrLBL;

        @FXML
        private TextArea infoArea;

        @FXML
        private Label emailErrLBL;

        @FXML
        private TextField priceField;

        @FXML
        private Label priceError;

        @FXML
        private TextField countField;

        @FXML
        private Label countError;

        @FXML
        private Button backBTN;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addProductBTN;

        @FXML
        private TableView<PropertyWrapper> properties;

        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;

        @FXML
        private TableColumn<PropertyWrapper, TextField> valueCOL;

        public class PropertyWrapper {
            String property;
            TextField value = new TextField();

            public PropertyWrapper(String property) {
                this.property = property;
                value.setPromptText("Enter value...");
                value.setEditable( ! exists);
            }

            public String getProperty() {
                return property;
            }

            public TextField getValue() {
                return value;
            }
        }


        private String name;
        private String brand;
        private String productId;
        private String[] info;
        private boolean exists;
        public static void display(String name, String brand, String productId) {
            ((AddProductPopupController_Page2) View.popupWindow("Add new Product (2 of 2)", Constants.FXMLs.addProductPage1, 860, 505)).initialize(name, brand, productId);
        }

        private void initialize(String name, String brand, String productId) {
            this.name = name;
            this.brand = brand;
            this.productId = productId;
            this.exists = productId != null;

            if (exists) {
                try {
                    info = mainController.digest(productId);
                } catch (Exceptions.InvalidProductIdException e) {
                    e.printStackTrace();
                    printError(e.getMessage());
                }
            }

            initAccessControls();
            initListeners();
            initChoiceBox();
            initValues();
            initActions();
        }

        private void initAccessControls() {
            if (exists) {
                category.setDisable(true);
                browseBTN.setDisable(true);
                infoArea.setEditable(false);
            }
        }

        private void initChoiceBox() {
            category.getItems().addAll(sellerController.getAllCategories());
            if (exists) {
                category.getSelectionModel().select(productId);
            }
        }

        private void initValues() {
            nameField.setText(name);
            brandField.setText(brand);
            if (exists) infoArea.setText(info[3]);
        }

        private void initListeners() {
            category.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> updateProperties(newValue)));

            View.addListener(countField, "[0-9]");

            infoArea.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.lastIndexOf("\n") - newValue.length() == 70 && newValue.length() > oldValue.length()) {
                    ((TextArea) observable).setText(newValue + "\n");
                }
            }));
        }

        private void initActions() {
            browseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File", "*.png"),
                        new FileChooser.ExtensionFilter("Image File", "*.jpg"));
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (chosenFile != null) {
                    imageField.setText(chosenFile.getPath());
                }
            });

            backBTN.setOnAction(e -> {
                AddProductPopupController_Page1.display();
                backBTN.getScene().getWindow().hide();
            });

            addProductBTN.setOnAction(e -> {
                if (validateFields()) {
                    HashMap propertyMap = new HashMap();
                    for (PropertyWrapper item : properties.getItems()) {
                        propertyMap.put(item.property, item.value);
                    }
                    try {
                        if ( ! exists)
                            sellerController.addNewProduct(nameField.getText(), brandField.getText(), infoArea.getText(), imageField.getText(), category.getValue(),
                                propertyMap, Double.parseDouble(priceField.getText()), Integer.parseInt(countField.getText()));
                        else
                            sellerController.addNewSubProductToAnExistingProduct(productId, Double.parseDouble(priceField.getText()), Integer.parseInt(countField.getText()));
                    } catch (Exception ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        }

        private boolean validateFields() {
            if (category.getValue() == null) {
                printError("Please choose a category");
                return false;
            }
            if (countField.equals("")) {
                printError("Please enter the number of available items");
                return false;
            }
            if ( ! priceField.getText().matches(Constants.doublePattern)) {
                printError("Invalid price! Please enter a double number");
                return false;
            }
            return true;
        }

        private void updateProperties(String categoryName) {
            try {
                properties.getItems().clear();
                for (String category : mainController.getPropertiesOfCategory(categoryName, true)) {
                    properties.getItems().add(new PropertyWrapper(category));
                }
            } catch (Exceptions.InvalidCategoryException e) {
                e.printStackTrace();
                errorLBL.setText("error in method updateProperties(): " + e.getMessage());
            }
        }

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }
    }
}