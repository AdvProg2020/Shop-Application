package Client.view;

import Client.HollowController.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class Controllers {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;
    private static SupporterController supporterController;

    public static void init() {
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
        supporterController = View.supporterController;
    }


    public static class EditPersonalInfoPopupController implements Initializable {

        @FXML
        private TextField usernameField;

        @FXML
        private Label usernameError;

        @FXML
        private PasswordField passwordField;

        @FXML
        private TextField showPasswordField;

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

        @FXML
        private Label balanceKeyLBL;
        @FXML
        private Label storeNameKeyLBL;

        private String[] info;
        private final SimpleBooleanProperty passwordChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty firstNameChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty lastNameChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty imageChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty phoneChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty emailChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty balanceChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty storeNameChanged = new SimpleBooleanProperty(false);

        public static void display() {
            View.popupWindow("Edit Personal Info", Constants.FXMLs.editPersonalInfoPopup, 550, 600);
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
            if (View.type.get().equals(Constants.adminUserType)) {
                balanceKeyLBL.setVisible(false);
                balance.setVisible(false);
            }
            if (!View.type.get().equals(Constants.sellerUserType)) {
                storeNameKeyLBL.setVisible(false);
                storeName.setVisible(false);
            }
        }

        private void initButtons() {
            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());

            browseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg"));
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
                        if (imageChanged.get()) {
                            byte[] img = new byte[0];
                            try {
                                img = Files.readAllBytes(Paths.get(info[7] = imageField.getText()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            mainController.editPersonalInfo("image path", "", img);
                        }
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
                    } catch (Exceptions.SameAsPreviousValueException | Exceptions.InvalidFieldException ex) {
                        ex.printStackTrace();
                    } finally {
                        discardBTN.getScene().getWindow().hide();
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
            usernameField.setText(info[0]);
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
            showPasswordField.textProperty().bindBidirectional(passwordField.textProperty());
            showPasswordField.visibleProperty().bind(passwordField.visibleProperty().not());
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
        private Label typeLBL;

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
        private TableColumn<DiscountWrapper, String> discountPercentageCOL;

        @FXML
        private TabPane requestTABPANE;

        @FXML
        private TableView<RequestWrapper> sellerRequests;

        @FXML
        private TableColumn<RequestWrapper, String> typeCOL;

        @FXML
        private TableColumn<RequestWrapper, String> dateCOL;

        @FXML
        private TableColumn<RequestWrapper, String> requestDetailsCOL;

        @FXML
        private TableView<CategoryWrapper> categories;

        @FXML
        private TableColumn<CategoryWrapper, String> nameCOL;

        @FXML
        private TableColumn<CategoryWrapper, String> parentCOL;

        @FXML
        private BorderPane mainBorderPane;

        @FXML
        private TabPane onlineAccountsTABPANE;

        @FXML
        private TableView<OnlineAccountWrapper> onlineAccounts;

        @FXML
        private TableColumn<OnlineAccountWrapper, String> accountsCOL;

        @FXML
        private TableColumn<OnlineAccountWrapper, Button> detailsCOL;

        private void initialize(String username, boolean isPopup) {
            current = this;
            this.isPopup = isPopup;
            this.username = username;
            try {
                if (username != null)
                    info = mainController.viewPersonalInfo(username);
                else
                    info = mainController.viewPersonalInfo();
            } catch (Exceptions.UsernameDoesntExistException | Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                return;
            }

            typeLBL.setText(info[info.length - 1]);
            initVisibilities();
            initActions();
            initTable();
            update();
        }

        public void update() {
            try {
                if (username != null)
                    info = mainController.viewPersonalInfo(username);
                else
                    info = mainController.viewPersonalInfo();
            } catch (Exceptions.UsernameDoesntExistException | Exceptions.NotLoggedInException e) {
                e.printStackTrace();
                return;
            }

            usernameLBL.setText(info[0]);
            nameLBL.setText(info[3] + " " + info[4]);
            phoneValue.setText(info[6]);
            emailValue.setText(info[5]);
            if (!info[info.length - 1].equals(Constants.adminUserType)) {
                balanceValue.setText(info[8] + "$");
            }
            if (info[info.length - 1].equals(Constants.sellerUserType)) {
                storeValue.setText(info[9]);
            }

            String imgPath = "src/main/resources/temp/accountImage.jpg";
            File f = Path.of(imgPath).toFile();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] byteArray = mainController.loadFileFromDataBase(info[7]);
            try (FileOutputStream stream = new FileOutputStream(imgPath)) {
                stream.write(byteArray);
                accountIMG.setImage(new Image("file:" + imgPath));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public static class OnlineAccountWrapper {
            String username;
            Button details = new Button();

            public OnlineAccountWrapper(String username) {
                this.username = username;

                details.getStyleClass().add("details-button");
                details.setOnAction(e -> PersonalInfoMenuController.display(username));
            }

            public String getUsername() {
                return username;
            }

            public Button getDetails() {
                return details;
            }
        }

        public static class CategoryWrapper {
            String name, parent;

            public CategoryWrapper(String name, String parent) {
                this.name = name;
                this.parent = parent;
            }

            public String getName() {
                return name;
            }

            public String getParent() {
                return parent;
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
                        View.popupWindow("Account detail menu", Constants.FXMLs.personalInfoMenu, 900, 600)).initialize(username, true);
            }
        }

        public static class DiscountWrapper {
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

        private void initVisibilities() {
            String type = info[info.length - 1];
            if (isPopup) {
                editBTN.setVisible(false);
                sellLogBTN.setVisible(false);
                buyLogBTN.setVisible(false);
                logoutBTN.setVisible(false);
                mainBorderPane.rightProperty().set(null);
            } else {
                if (type.equals(Constants.sellerUserType)) {
                    customerDiscounts.setVisible(false);
                    discountTABPANE.setVisible(false);
                    buyLogBTN.setVisible(false);
                    sellLogBTN.setVisible(true);
                    sellerRequests.setVisible(true);
                    requestTABPANE.setVisible(true);
                } else if (type.equals(Constants.customerUserType)) {
                    customerDiscounts.setVisible(true);
                    sellerRequests.setVisible(false);
                    buyLogBTN.setVisible(true);
                    sellLogBTN.setVisible(false);
                    discountTABPANE.setVisible(true);
                    requestTABPANE.setVisible(false);
                } else {
                    sellLogBTN.setVisible(false);
                    buyLogBTN.setVisible(false);
                    onlineAccountsTABPANE.setVisible(true);
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
            sellLogBTN.setOnAction(e -> SellerSellLogsManagingMenuController.display());

            buyLogBTN.setOnAction(e -> CustomerBuyLogMenuController.display());

            logoutBTN.setOnAction(e -> {
                try {
                    mainController.logout();
                    View.type.set(Constants.anonymousUserType);
                    MainMenuController.display();
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
                nameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
                parentCOL.setCellValueFactory(new PropertyValueFactory<>("parent"));
                requestDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
                for (String[] request : sellerController.getPendingRequests()) {
                    sellerRequests.getItems().add(new RequestWrapper(request));
                }
                for (String[] category : sellerController.getAllCategories()) {
                    categories.getItems().add(new CategoryWrapper(category[0], category[1]));
                }
            } else {
                accountsCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
                detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
                adminController.getOnlineAccounts().forEach(oa -> onlineAccounts.getItems().add(new OnlineAccountWrapper(oa)));
            }
        }

        public static class RequestWrapper {
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
                        case "EditSaleRequest":
                        case "EditFileRequest":
                            EditRequestPopupController.display(id);
                            break;
                        case "AddSaleRequest":
                            AddSaleRequestPopupController.display(id);
                            break;
                        case "AddAuctionRequest":
                            AddAuctionRequestPopupController.display(id);
                            break;
                        case "AddFileRequest":
                            AddFileRequestPopupController.display(id);
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


    }

    public static class CommissionManagingPopupController {
        @FXML
        private Label errorLBL;

        @FXML
        private TextField commissionField;

        @FXML
        private TextField walletMinField;

        @FXML
        private Button addBTN;

        @FXML
        private HBox saveDiscardHBox;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        public static void display() {
            ((CommissionManagingPopupController)
                    View.popupWindow("Commission managing popup", Constants.FXMLs.commissionManagingPopup, 650, 450)).initialize();
        }

        private void initialize() {
            editBTN.setOnAction(e -> saveChanges());
            discardBTN.setOnAction(e -> discard());
        }

        private void saveChanges() {
            String commission = commissionField.getText();
            String walletMin = walletMinField.getText();
            if (!commission.isEmpty()) {
                try {
                    adminController.setCommission(Double.parseDouble(commission));
                } catch (Exceptions.InvalidCommissionException e) {
                    e.printStackTrace();
                }
            }
            if (!walletMin.isEmpty()) {
                adminController.setWalletMin(Double.parseDouble(walletMin));
                editBTN.getScene().getWindow().hide();
            }
        }

        private void discard() {
            walletMinField.setText("");
            commissionField.setText("");
            editBTN.getScene().getWindow().hide();
        }
    }

    public static class AddProductRequestPopupController {
        @FXML
        private TextField nameField;

        @FXML
        private TextField brandField;

        @FXML
        private TextField categoryField;

        @FXML
        private TextField imageField;

        @FXML
        private TextArea infoArea;

        @FXML
        private TextField priceField;

        @FXML
        private TextField countField;

        @FXML
        private TableView<PropertyWrapper> properties;

        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;

        @FXML
        private TableColumn<PropertyWrapper, String> valueCOL;

        String[] primaryDetails;
        String[] secondaryDetails;

        private void initTable() {
            propertyCOL.setCellValueFactory(new PropertyValueFactory<>("property"));
            valueCOL.setCellValueFactory(new PropertyValueFactory<>("value"));

            try {
                adminController.getPropertyValuesOfAProductInARequest(primaryDetails[0]).forEach((key, value) ->
                        properties.getItems().add(new PropertyWrapper(key, value)));
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
            }
        }

        public static void display(String requestId) {
            ((AddProductRequestPopupController)
                    View.popupWindow("Add product request details", Constants.FXMLs.addProductRequestPopup, 860, 480)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            nameField.setDisable(true);
            brandField.setDisable(true);
            categoryField.setDisable(true);
            imageField.setDisable(true);
            infoArea.setDisable(true);
            countField.setDisable(true);
            priceField.setDisable(true);
            countField.setDisable(true);

            initValues();
            initTable();
        }

        private void initValues() {
            nameField.setText(secondaryDetails[0]);
            brandField.setText(secondaryDetails[1]);
            categoryField.setText(secondaryDetails[3]);
            imageField.setText(secondaryDetails[2]);
            infoArea.setText(secondaryDetails[4]);
            countField.setText(secondaryDetails[5]);
            priceField.setText(secondaryDetails[6]);
        }

        public static class PropertyWrapper {
            String property;
            String value;

            public PropertyWrapper(String property, String value) {
                this.property = property;
                this.value = value;
            }

            public String getProperty() {
                return property;
            }

            public String getValue() {
                return value;
            }
        }
    }

    public static class EditRequestPopupController {
        @FXML
        private Label idProperty;
        @FXML
        private Label idValue;
        @FXML
        private Label fieldLBL;
        @FXML
        private TextArea newValue;
        @FXML
        private TextArea oldValue;

        private String[] primaryDetails;
        private String[] secondaryDetails;
        private String[] tertiaryDetails;

        public static void display(String requestId) {
            ((EditRequestPopupController)
                    View.popupWindow("Edit product request details", Constants.FXMLs.editRequestDetailsPopup, 500, 300)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
                tertiaryDetails = detailsOfRequest.get(2);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            oldValue.setEditable(false);
            newValue.setEditable(false);

            initValues();
        }

        private void initValues() {
            fieldLBL.setText(tertiaryDetails[0]);
            oldValue.setText(tertiaryDetails[2]);
            idValue.setText(secondaryDetails[0]);
            newValue.setText(tertiaryDetails[1]);
        }
    }

    /**
     * public static String[] saleEditableFields() {
     * String[] saleEditableFields = new String[4];
     * saleEditableFields[0] = "start date";
     * saleEditableFields[1] = "end date";
     * saleEditableFields[2] = "percentage";
     * saleEditableFields[3] = "maximum";
     * return saleEditableFields;
     * }
     */

    public static class AddSaleRequestPopupController {
        @FXML
        private TableView<MyStringWrapper> products;
        @FXML
        private TableColumn<MyStringWrapper, String> nameBrandCOL;
        @FXML
        private Label errorLBL;
        @FXML
        private Label idKeyLBL;
        @FXML
        private Label idValueLBL;
        @FXML
        private TextField percentageField;
        @FXML
        private TextField maxField;
        @FXML
        private TextField startDate;
        @FXML
        private TextField endDate;

        String[] primaryDetails;
        String[] secondaryDetails;

        public static class MyStringWrapper {
            String content;

            public MyStringWrapper(String content) {
                this.content = content;
            }

            public String getContent() {
                return content;
            }
        }

        public static void display(String requestId) {
            ((AddSaleRequestPopupController)
                    View.popupWindow("Add sale request details", Constants.FXMLs.addSaleRequestPopup, 650, 500)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            percentageField.setDisable(true);
            maxField.setDisable(true);
            startDate.setDisable(true);
            endDate.setDisable(true);

            initValues();
            initTable();
        }

        private void initValues() {
            idValueLBL.setText(secondaryDetails[0]);
            percentageField.setText(secondaryDetails[1]);
            maxField.setText(secondaryDetails[2]);
            startDate.setText(secondaryDetails[3]);
            endDate.setText(secondaryDetails[4]);
        }

        private void initTable() {
            nameBrandCOL.setCellValueFactory(new PropertyValueFactory<>("content"));

            try {
                products.getItems().addAll(adminController.getProductsInSaleRequest(primaryDetails[0]).stream().map(s -> s[1] + " - " + s[2]).map(MyStringWrapper::new).collect(Collectors.toCollection(ArrayList::new)));
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
            }
        }
    }

    public static class AddReviewRequestPopupController {
        @FXML
        private Label nameLBL;
        @FXML
        private Label boughtLBL;
        @FXML
        private Label didntBuyLBL;
        @FXML
        private Label nameBrandLBL;
        @FXML
        private Label titleLBL;
        @FXML
        private TextArea textArea;

        String[] primaryDetails;
        String[] secondaryDetails;

        public static void display(String requestId) {
            ((AddReviewRequestPopupController)
                    View.popupWindow("Add review request details", Constants.FXMLs.addReviewRequestPopup, 500, 350)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            textArea.setDisable(true);

            initValues();
        }

        private void initValues() {
            nameLBL.setText(secondaryDetails[0]);
            nameBrandLBL.setText(secondaryDetails[2] + " - " + secondaryDetails[3]);
            titleLBL.setText(secondaryDetails[4]);
            textArea.setText(secondaryDetails[5]);

            if (secondaryDetails[6].equals("yes")) {
                boughtLBL.setVisible(true);
                didntBuyLBL.setVisible(false);
            } else {
                boughtLBL.setVisible(false);
                didntBuyLBL.setVisible(true);
            }
        }
    }

    public static class AddSellerRequestPopupController {
        @FXML
        private TextField usernameField;
        @FXML
        private TextField imageField;
        @FXML
        private TextField firstName;
        @FXML
        private TextField lastName;
        @FXML
        private TextField phoneNumber;
        @FXML
        private TextField email;
        @FXML
        private TextField balance;
        @FXML
        private TextField storeName;

        private String[] primaryDetails;
        private String[] secondaryDetails;

        public static void display(String requestId) {
            ((AddSellerRequestPopupController)
                    View.popupWindow("Add seller request details", Constants.FXMLs.addSellerRequestPopup, 500, 400)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            initDisable();
            initValues();
        }

        private void initDisable() {
            usernameField.setDisable(true);
            firstName.setDisable(true);
            lastName.setDisable(true);
            email.setDisable(true);
            phoneNumber.setDisable(true);
            balance.setDisable(true);
            storeName.setDisable(true);
            imageField.setDisable(true);
        }

        private void initValues() {
            usernameField.setText(secondaryDetails[0]);
            firstName.setText(secondaryDetails[1]);
            lastName.setText(secondaryDetails[2]);
            email.setText(secondaryDetails[3]);
            phoneNumber.setText(secondaryDetails[4]);
            balance.setText(secondaryDetails[5]);
            storeName.setText(secondaryDetails[6]);
            imageField.setText(secondaryDetails[7]);
        }
    }

    public static class AddFileRequestPopupController {
        @FXML
        private TextField nameField;
        @FXML
        private TextField extensionField;
        @FXML
        private TextField categoryField;
        @FXML
        private TextField imageField;
        @FXML
        private TextArea infoArea;
        @FXML
        private TextField priceField;
        @FXML
        private TextField pathField;
        @FXML
        private TableView<PropertyWrapper> properties;
        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;
        @FXML
        private TableColumn<PropertyWrapper, String> valueCOL;

        String[] primaryDetails;
        String[] secondaryDetails;

        public static class PropertyWrapper {
            String property;
            String value;

            public PropertyWrapper(String property, String value) {
                this.property = property;
                this.value = value;
            }

            public String getProperty() {
                return property;
            }

            public String getValue() {
                return value;
            }
        }

        public static void display(String requestId) {
            ((AddFileRequestPopupController) View.popupWindow("Add file request details.", Constants.FXMLs.addFileRequestPopup, 860, 480)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var detailsOfRequest = adminController.detailsOfRequest(requestId);
                primaryDetails = detailsOfRequest.get(0);
                secondaryDetails = detailsOfRequest.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
                return;
            }

            nameField.setDisable(true);
            extensionField.setDisable(true);
            categoryField.setDisable(true);
            imageField.setDisable(true);
            infoArea.setDisable(true);
            pathField.setDisable(true);
            priceField.setDisable(true);
            pathField.setDisable(true);

            initValues();
            initTable();
        }

        private void initValues() {
            nameField.setText(secondaryDetails[0]);
            extensionField.setText(secondaryDetails[1]);
            categoryField.setText(secondaryDetails[3]);
            imageField.setText(secondaryDetails[2]);
            infoArea.setText(secondaryDetails[4]);
            priceField.setText(secondaryDetails[5]);
            pathField.setText(secondaryDetails[6]);
        }

        private void initTable() {
            propertyCOL.setCellValueFactory(new PropertyValueFactory<>("property"));
            valueCOL.setCellValueFactory(new PropertyValueFactory<>("value"));

            try {
                adminController.getPropertyValuesOfAFileInRequest(primaryDetails[0]).forEach((key, value) -> properties.getItems().add(new PropertyWrapper(key, value)));
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
            }
        }
    }

    public static class AddAuctionRequestPopupController {
        @FXML
        private Label errorLBL;
        @FXML
        private Label idKeyLBL;
        @FXML
        private Label sellerLBL;
        @FXML
        private TextField startDate;
        @FXML
        private TextField endDate;
        @FXML
        private Label subsellableLBL;

        String[] primaryDetails;
        String[] secondaryDetails;

        public static void display(String requestId) {
            ((AddAuctionRequestPopupController)
                    View.popupWindow("Add Auction request details", Constants.FXMLs.addAuctionRequestPopup, 450, 450)).initialize(requestId);
        }

        private void initialize(String requestId) {
            try {
                var details = adminController.detailsOfRequest(requestId);
                primaryDetails = details.get(0);
                secondaryDetails = details.get(1);
            } catch (Exceptions.InvalidRequestIdException e) {
                e.printStackTrace();
            }

            startDate.setEditable(false);
            endDate.setEditable(false);
            initValues();
        }

        private void initValues() {
            sellerLBL.setText(secondaryDetails[1]);
            subsellableLBL.setText(secondaryDetails[2]);
            startDate.setText(secondaryDetails[3]);
            endDate.setText(secondaryDetails[4]);
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

        @FXML
        private Button allAuctions;

        @FXML
        private HBox productsInAuction;

        private static void display() {
            View.getStackTrace().clear();
            View.stackSize.set(0);
            View.setMainPane(Constants.FXMLs.mainMenu);
        }


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            for (String[] subProductPack : mainController.getSubSellablesForAdvertisements(6)) {
                advertisingProducts.getChildren().add(SellableBoxController.createBox(subProductPack, null, null, false, false));
            }

            for (String[] subProduct : mainController.getSubSellablesInSale(10)) {
                productsInSale.getChildren().add(SellableBoxController.createBox(subProduct, null, null, false, false));
            }

            for (String[] subProduct : mainController.getSubSellablesInAuction(10)) {
                productsInAuction.getChildren().add(SellableBoxController.createBox(subProduct, null, null, false, false));
            }

            allSales.setOnAction(e -> salesMenu());
            productsMenu.setOnAction(e -> productsMenu());
            allAuctions.setOnAction(e -> auctionMenu());

            initCategoriesBox();
        }

        private void salesMenu() {
            ProductsMenuController.display("SuperCategory", true, false);
        }

        private void productsMenu() {
            ProductsMenuController.display("SuperCategory", false, false);
        }

        private void auctionMenu() {
            ProductsMenuController.display("SuperCategory", false, true);
        }

        private void initCategoriesBox() {
            borderPane.setLeft(CategoryBoxController.createBox("SuperCategory", false, false));
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
        private ScrollPane scrollPane;

        @FXML
        private BorderPane borderPane;

        @FXML
        private ScrollPane propertiesScrollPane;

        @FXML
        private Button searchBTN;

        private int numberOfColumns = 5;
        public ArrayList<String[]> products;
        private String categoryName;
        private double maximumAvailablePrice;
        private final HashMap<String, SimpleStringProperty> properties = new HashMap<>();
        private boolean toCompare;
        //products menu mode:
        private boolean inSale = false;
        private boolean inAuction = false;

        //comparison mode:
        private String productIdToCompareWith;

        public static void display(String categoryName, boolean inSale, boolean inAuction) {
            ProductsMenuController controller = View.setMainPane(Constants.FXMLs.productsMenu);
            if (controller != null) {
                controller.categoryName = categoryName;
                controller.inSale = inSale;
                controller.inAuction = inAuction;

                controller.initPageObjects();
                controller.setValuesOfPageObjects();
            }
        }

        public static void displayACategoryProductsToCompare(String categoryName, String productId) {
            ProductsMenuController controller = View.popupWindow("Product choosing menu", Constants.FXMLs.productsMenu, 1200, 800);
            if (controller != null) {
                currentController = controller;
                controller.numberOfColumns = 4;
                controller.categoryName = categoryName;
                controller.productIdToCompareWith = productId;
                controller.inSale = false;
                controller.toCompare = true;

                controller.initPageObjects();
                controller.setValuesOfPageObjects();
            }
        }

        public static ProductsMenuController currentController;

        public void close() {
            currentController.availableCheckBox.getScene().getWindow().hide();
            currentController = null;
        }

        private void initPageObjects() {
            initActions();
            initPropertyFilters();
            if (!toCompare) {
                initCategoryBox();
                initCategoryTree();
            }
            initChoiceBoxes();
            initBindings();
        }

        private void initBindings() {
            sortByChoiceBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, s, t1) -> update()));
            isIncreasingButton.getToggleGroup().selectedToggleProperty().addListener((observableValue, toggle, t1) -> update());
            maxPriceSlider.setOnMouseReleased(e -> update());
            minPriceSlider.setOnMouseReleased(e -> update());
            filterSeller.getSelectionModel().selectedItemProperty().addListener(((observable) -> update()));
            filterBrand.getSelectionModel().selectedItemProperty().addListener(((observable) -> update()));
            searchBTN.setOnAction(e -> update());
            availableCheckBox.selectedProperty().addListener((observable -> update()));
        }

        private void setValuesOfPageObjects() {
            update();
            setChoiceBoxesValues();
            setSliderBounds();
        }

        private void initPropertyFilters() {
            try {
                ArrayList<String> propertyKeys = mainController.getPropertiesOfCategory(categoryName, false);
                GridPane propertyFilters = new GridPane();
                propertyFilters.setVgap(10);
                propertyFilters.setHgap(20);
                propertyFilters.setAlignment(Pos.CENTER);
                propertyFilters.setPadding(new Insets(5, 5, 5, 5));
                propertiesScrollPane.setContent(propertyFilters);
                int propertyIndex = 0;
                for (String propertyKey : propertyKeys) {
                    VBox propertyBox = creatPropertyChoiceBox(propertyKey);
                    propertyFilters.add(propertyBox, propertyIndex / 3, propertyIndex % 3);
                    propertyIndex++;
                }
            } catch (Exceptions.InvalidCategoryException e) {
                e.printStackTrace();
            }
        }

        private void initChoiceBoxes() {
            filterSeller.getItems().add("");
            filterSeller.getSelectionModel().select(0);
            filterBrand.getItems().add("");
            filterBrand.getSelectionModel().select(0);
            sortByChoiceBox.getItems().add("");
            sortByChoiceBox.getSelectionModel().select(0);
        }

        private void setChoiceBoxesValues() {
            HashSet<String> sellersSet = new HashSet<>();
            HashSet<String> brandSet = new HashSet<>();
            for (String[] product : products) {
                try {
                    sellersSet.addAll(mainController.subSellablesOfASellable(product[0]).stream().map(e -> e[12]).collect(Collectors.toList()));
                } catch (Exceptions.InvalidSellableIdException e) {
                    e.printStackTrace();
                }
                brandSet.add(product[3]);
            }

            filterBrand.getItems().addAll(new ArrayList<>(brandSet));
            filterSeller.getItems().addAll(new ArrayList<>(sellersSet));

            ArrayList<String> sorts = new ArrayList<>();
            sorts.add("view count");
            sorts.add("price");
            sorts.add("name");
            sorts.add("rating score");
            sorts.add("category name");
            sorts.add("remaining count");
            sortByChoiceBox.getItems().addAll(sorts);

        }

        private void setSliderBounds() {
            if (products != null) {
                setMaxPrice();
                maxPriceSlider.setMax(maximumAvailablePrice);
                minPriceSlider.setMax(maximumAvailablePrice);
            }
        }

        private void initActions() {
            update.setOnAction(e -> update());
        }

        private void update() {
            updateProducts();
            updatePane();
        }

        private void updateProducts() {
            HashMap<String, String> propertyValues = new HashMap<>();
            for (String s : properties.keySet()) {
                propertyValues.put(s, properties.get(s).getValue());
            }
            //TODO: fix the auction stuff
            products = mainController.sortFilterProducts(categoryName, inSale, false, sortByChoiceBox.getValue(), isIncreasingButton.isSelected(), availableCheckBox.isSelected(),
                    minPriceSlider.getValue(), maxPriceSlider.getValue(), filterName.getText(), filterBrand.getValue(), "", filterSeller.getValue(), 0, propertyValues);
        }

        private void updatePane() {
            var productsPane = new GridPane();
            productsPane.setHgap(30);
            productsPane.setVgap(30);
            scrollPane.setContent(productsPane);
            productsPane.setPadding(new Insets(30, 30, 30, 30));
            int index = 0;
            for (String[] subProductPack : products) {
                Parent productBox = SellableBoxController.createBox(subProductPack, productIdToCompareWith, categoryName, inSale, inAuction);
                productsPane.add(productBox, index % numberOfColumns, index / numberOfColumns);
                index++;
            }
        }

        private VBox creatPropertyChoiceBox(String property) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(5);
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            vBox.getChildren().addAll(new Label(property), choiceBox);
            choiceBox.getStylesheets().add(View.class.getResource("/css/ChoiceBox.css").toString());
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable -> update()));
            try {
                ArrayList<String> propertyValues = mainController.getPropertyValuesInCategory(categoryName, property);
                propertyValues.removeIf(s -> s.equals(""));
                choiceBox.getItems().add("");
                choiceBox.getSelectionModel().select(0);
                choiceBox.getItems().addAll(propertyValues);
            } catch (Exceptions.InvalidCategoryException e) {
                return null;
            }
            SimpleStringProperty valueProperty = new SimpleStringProperty();
            valueProperty.bind(choiceBox.valueProperty());
            properties.put(property, valueProperty);
            return vBox;
        }

        private void setMaxPrice() {
            ArrayList<Double> prices = new ArrayList<>();
            for (String[] product : products) {
                prices.add(Double.parseDouble(product[8]));
            }
            if (prices.size() == 0)
                maximumAvailablePrice = 0;
            else {
                maximumAvailablePrice = prices.get(0);
                for (Double price : prices) {
                    if (price > maximumAvailablePrice)
                        maximumAvailablePrice = price;
                }
            }
        }

        private void initCategoryTree() {
            HBox categoryTreeBox = CategoryTreeBoxController.createBox();
            if (categoryTreeBox != null) {
                ArrayList<String> categoryNames = mainController.getCategoryTreeOfACategory(categoryName);
                for (String s : categoryNames) {
                    categoryTreeBox.getChildren().add(createCategoryButton(s));
                }
                borderPane.setTop(categoryTreeBox);
            }
        }

        private Button createCategoryButton(String category) {
            Button button = new Button();
            button.setText(category);
            button.setOnAction(e -> ProductsMenuController.display(category, inSale, inAuction));
            return button;
        }

        private void initCategoryBox() {
            borderPane.setLeft(CategoryBoxController.createBox(categoryName, inSale, inAuction));
        }
    }

    public static class SellableBoxController {

        @FXML
        public Label auction;

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

        @FXML
        private Label available;

        @FXML
        private ImageView fullStar1;
        @FXML
        private ImageView fullStar2;
        @FXML
        private ImageView fullStar3;
        @FXML
        private ImageView fullStar4;
        @FXML
        private ImageView fullStar5;

        @FXML
        private ImageView halfStar1;
        @FXML
        private ImageView halfStar2;
        @FXML
        private ImageView halfStar3;
        @FXML
        private ImageView halfStar4;
        @FXML
        private ImageView halfStar5;

        @FXML
        private HBox remainingDateBox;

        private String[] subProduct;


        private String categoryName;
        private boolean inSale;
        private boolean inAuction;

        public static Parent createBox(String[] subSellable, String sellableToCompare, String categoryName, boolean inSale, boolean inAuction) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.productBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                SellableBoxController pbc = loader.getController();
                pbc.setInfo(subSellable);
                pbc.setAction(p, sellableToCompare);
                pbc.categoryName = categoryName;
                pbc.inSale = inSale;
                pbc.inAuction = inAuction;
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setInfo(String[] subProductInfo) {
            subProduct = subProductInfo;
            name.setText(subProductInfo[2] + " " + subProductInfo[3]);

            String imgPath = "src/main/resources/temp/subProduct_" + subProductInfo[0] + ".png";
            File f = Path.of(imgPath).toFile();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] byteArray = mainController.loadFileFromDataBase(subProductInfo[6]);
            try (FileOutputStream stream = new FileOutputStream(imgPath)) {
                stream.write(byteArray);
                image.setImage(new Image("file:" + imgPath));
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (subProductInfo[16] != null) {
                auctionMode(subProductInfo);
            } else {
                auction.setVisible(false);
                if (subProductInfo[7].equals(subProductInfo[8])) {
                    priceBefore.setVisible(false);
                    priceAfter.setText(subProductInfo[7] + "$");
                } else {
                    priceBefore.setText(subProductInfo[7] + "$");
                    priceAfter.setText(subProductInfo[8] + "$");
                    priceBefore.setVisible(true);
                }
                if (subProductInfo[11] != null) {
                    sale.setText(subProductInfo[11] + "%");
                } else
                    sale.setVisible(false);
                if (subProductInfo[10] != null) {
                    try {
                        Date endDate = Constants.dateFormat.parse(subProductInfo[10]);
                        LocalDate now = LocalDate.now();
                        ZoneId defaultZoneId = ZoneId.systemDefault();
                        Instant instant = endDate.toInstant();
                        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
                        Period period = Period.between(now, localDate);
                        int daysLeft = period.getDays();
                        remainingTime.setText(daysLeft + " days");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    remainingDateBox.setVisible(false);
                }
            }
            if (subProductInfo[9].equals("-")) {
                available.setVisible(false);
            } else
                available.setVisible(Integer.parseInt(subProductInfo[9]) == 0);
            rating.setText(subProductInfo[5]);
            initRatingStars(Double.parseDouble(subProductInfo[4]));
        }

        private void auctionMode(String[] subProductInfo) {
            sale.setVisible(false);
            auction.setVisible(true);
            priceAfter.setText(subProductInfo[16]);
            priceBefore.setVisible(false);
            try {
                Date endDate = Constants.dateFormat.parse(subProductInfo[18]);
                LocalDate now = LocalDate.now();
                ZoneId defaultZoneId = ZoneId.systemDefault();
                Instant instant = endDate.toInstant();
                LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
                Period period = Period.between(now, localDate);
                int daysLeft = period.getDays();
                remainingTime.setText(daysLeft + " days");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void setAction(Parent p, String productToCompare) {
            if (productToCompare == null)
                p.setOnMouseClicked(e -> {
                    View.categoryName = categoryName;
                    View.inSale = inSale;
                    View.inAuction = inAuction;
                    SellableDetailMenuController.display(subProduct[0], subProduct[1], false);
                });
            else {
                p.setOnMouseClicked(e -> {
                    ProductsMenuController.currentController.close();
                    CompareMenuController.display(productToCompare, subProduct[0]);
                });
            }
        }

        private void initRatingStars(double rating) {
            fullStar1.setVisible(rating >= 1);
            fullStar2.setVisible(rating >= 2);
            fullStar3.setVisible(rating >= 3);
            fullStar4.setVisible(rating >= 4);
            fullStar5.setVisible(rating >= 5);

            halfStar1.setVisible(rating >= 0.5);
            halfStar2.setVisible(rating >= 1.5);
            halfStar3.setVisible(rating >= 2.5);
            halfStar4.setVisible(rating >= 3.5);
            halfStar5.setVisible(rating >= 4.5);
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
        private Label priceLBL;

        @FXML
        private TextField priceField;

        @FXML
        private Label priceError;

        @FXML
        private Label countLBL;

        @FXML
        private TextField countField;

        @FXML
        private Label countError;

        @FXML
        private TableView<PropertyWrapper> properties;

        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;

        @FXML
        private TableColumn<PropertyWrapper, TextField> valueCOL;

        @FXML
        private Button saveBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private Label errorLBL;

        String productId;
        String subProductId;
        private String[] productInfo;
        private String[] subProductInfo;
        private final ArrayList<PropertyWrapper> categoryProperties = new ArrayList<>();
        private final SimpleBooleanProperty nameFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty brandFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty imageFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty priceFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty countFieldChanged = new SimpleBooleanProperty(false);

        public static class PropertyWrapper {
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
            initTable();
        }

        private void initTable() {
            try {
                HashMap<String, String> propertyMap = mainController.getPropertyValuesOfAProduct(productId);
                for (String s : propertyMap.keySet()) {
                    properties.getItems().add(new PropertyWrapper(s, propertyMap.get(s)));
                }
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }
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
            countField.setText(subProductInfo[9]);
            infoArea.setText(productInfo[3]);
            category.setText(productInfo[7]);

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
                    Bindings.when(countField.textProperty().isEqualTo(subProductInfo[9])).then(false).otherwise(true)
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
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File", "*.jpg", "*.png"));
                File choseFile = fileChooser.showOpenDialog(new Stage());
                if (choseFile != null) imageField.setText(choseFile.getPath());
            });

            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());

            saveBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        ArrayList<String> changed =
                                properties.getItems().stream().filter(PropertyWrapper::hasChanged).map(c -> c.property + "," + c.value.getText()).collect(Collectors.toCollection(ArrayList::new));
                        if (View.type.get().equals(Constants.sellerUserType)) {
                            if (nameFieldChanged.get())
                                sellerController.editProduct(productId, "name", productInfo[1] = nameField.getText());
                            if (brandFieldChanged.get())
                                sellerController.editProduct(productId, "brand", productInfo[2] = brandField.getText());
                            if (imageFieldChanged.get())
                                sellerController.editProduct(productId, "imagePath", productInfo[8] = imageField.getText());
                            if (countFieldChanged.get())
                                sellerController.editProduct(productId, "count", subProductInfo[9] = countField.getText());
                            if (priceFieldChanged.get())
                                sellerController.editProduct(productId, "price", subProductInfo[7] = priceField.getText());
                            for (String properties : changed) {
                                sellerController.editProduct(productId, "property", properties);
                            }
                        } else {
                            if (nameFieldChanged.get())
                                adminController.editNameOfProduct(productId, productInfo[1] = nameField.getText());
                            if (brandFieldChanged.get())
                                adminController.editBrandOfProduct(productId, productInfo[2] = brandField.getText());
                            if (imageFieldChanged.get())
                                adminController.editImageOfProduct(productId, productInfo[8] = imageField.getText());
                            for (String properties : changed) {
                                adminController.editPropertyOfProduct(productId, properties);
                            }
                        }
                        discardBTN.getScene().getWindow().hide();
                    } catch (Exception ex) {
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
    }

    public static class CategoryBoxController {

        @FXML
        private VBox subCategoryBox;

        public static Parent createBox(String categoryName, boolean inSale, boolean inAuction) {
            try {
                ArrayList<String> subCategories = mainController.getSubCategoriesOfACategory(categoryName);
                FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.categoriesBox + ".fxml"));
                Parent p;
                p = loader.load();
                CategoryBoxController cbc = loader.getController();
                VBox subCategoryBox = cbc.subCategoryBox;
                for (String subCategory : subCategories) {
                    subCategoryBox.getChildren().add(createCategoryButton(subCategory, inSale, inAuction));
                }
                return p;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }


        private static Button createCategoryButton(String categoryName, boolean inSale, boolean inAuction) {
            Button button = new Button();
            button.setText(categoryName);
            button.setOnAction(e -> ProductsMenuController.display(categoryName, inSale, inAuction));
            return button;
        }
    }

    public static class SellableDetailMenuController {
        @FXML
        private ImageView sellableIMG;
        @FXML
        private Button compareBTN;
        @FXML
        private Label nameLBL;
        @FXML
        private Label brandExtensionLBL;
        @FXML
        private Label brandExtension;
        @FXML
        private Label categoryLBL;
        @FXML
        private TextArea sellableInfo;
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
        private Label auctionLBL;
        @FXML
        private Button auctionBTN;
        @FXML
        private Button downloadBTN;
        @FXML
        private TableView<SellerWrapper> sellersTBL;
        @FXML
        private TableColumn<SellerWrapper, String> sellersTBLSellerCOL;
        @FXML
        private TableColumn<SellerWrapper, String> sellersTBLPriceCOL;
        @FXML
        private TableColumn<SellerWrapper, String> sellersTBLNumberAvailableCOL;
        @FXML
        private TableView<PropertyWrapper> PropertiesTBL;
        @FXML
        private TableColumn<PropertyWrapper, String> propertyTab;
        @FXML
        private TableColumn<PropertyWrapper, String> valueTab;
        @FXML
        private VBox reviewsVB;
        @FXML
        private Label ratingCountLBL;
        @FXML
        private Button rateBTN;
        @FXML
        private ImageView halfStar1;
        @FXML
        private ImageView fullStar1;
        @FXML
        private ImageView halfStar2;
        @FXML
        private ImageView fullStar2;
        @FXML
        private ImageView halfStar3;
        @FXML
        private ImageView fullStar3;
        @FXML
        private ImageView halfStar4;
        @FXML
        private ImageView fullStar4;
        @FXML
        private ImageView halfStar5;
        @FXML
        private ImageView fullStar5;
        @FXML
        private TabPane tabPane;
        @FXML
        private Tab buyersTab;
        @FXML
        private TableView<BuyerWrapper> BuyersTBL;
        @FXML
        private TableColumn<BuyerWrapper, String> buyerCOL;
        @FXML
        private Button addReviewBTN;
        @FXML
        private BorderPane borderPane;
        @FXML
        private Label soldOutLBL;
        @FXML
        private Label salePercentageLBL;
        @FXML
        private HBox ratingsBox;
        @FXML
        private StackPane ratingsStackPane;
        @FXML
        private TableColumn<PropertyWrapper, Label> propertyCOL;
        @FXML
        private TableColumn<PropertyWrapper, Label> valueCOL;

        private String[] sellablePack;
        private String[] subSellablePack;
        private final ArrayList<PropertyWrapper> properties = new ArrayList<>();
        private final ArrayList<SellerWrapper> sellers = new ArrayList<>();
        private ArrayList<String[]> subSellablePacks = new ArrayList<>();
        private boolean editable;
        private String type;

        public static void display(String sellableId, boolean editable) {
            try {
                display(sellableId, mainController.getDefaultSubSellableOfASellable(sellableId)[1], editable);
            } catch (Exceptions.InvalidSellableIdException e) {
                System.out.println(e.getMessage());
            }
        }

        public static void display(String sellableId, String subSellableId, boolean editable) {
            String type = View.type.get();
            SellableDetailMenuController controller;
            if ((type.equals(Constants.sellerUserType) || type.equals(Constants.adminUserType)) && editable) {
                controller = (View.popupWindow("Sellable details", Constants.FXMLs.sellableDetailMenu, 1200, 800));
            } else {
                controller = View.setMainPane(Constants.FXMLs.sellableDetailMenu);
            }
            if (controller != null) {
                controller.editable = editable;
                controller.type = View.type.get();
                controller.initialize(sellableId, subSellableId);
            } else
                System.out.println("There is an error with loading the controller...");

        }

        public static class SellerWrapper {
            Label name = new Label();
            String price;
            String available;
            String[] subSellablePack;
            SellableDetailMenuController controller;

            public SellerWrapper(String name, String price, String available, String[] subProductPack, SellableDetailMenuController controller) {
                this.name.setText(name);
                this.price = price;
                this.available = available;
                this.subSellablePack = subProductPack;
                this.controller = controller;

                this.name.setOnMouseClicked(e -> {
                    controller.subSellablePack = subProductPack;
                    controller.updateSubSellableBox();
                });
            }

            public Label getName() {
                return name;
            }

            public String getPrice() {
                return price;
            }

            public String getAvailable() {
                return available;
            }

            public String[] getSubSellablePack() {
                return subSellablePack;
            }
        }

        public static class PropertyWrapper {
            Label propertyLBL = new Label();
            Label valueLBL = new Label();

            public PropertyWrapper(String property, String value) {
                propertyLBL.setText(property);
                valueLBL.setText(value);
            }

            public Label getPropertyLBL() {
                return propertyLBL;
            }

            public Label getValueLBL() {
                return valueLBL;
            }
        }

        public static class BuyerWrapper {
            String username;

            public BuyerWrapper(String username) {
                this.username = username;
            }

            public String getUsername() {
                return username;
            }
        }

        private void initialize(String sellableId, String subSellableId) {
            if (type.equals(Constants.customerUserType) || type.equals(Constants.anonymousUserType)) {
                try {
                    mainController.showProduct(sellableId);
                } catch (Exceptions.InvalidSellableIdException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            setPacks(sellableId, subSellableId);


            initMainObjects();
            initCategoryHBox();
            initReviewsVB();
            initPropertiesTable();
            initButtons();
            updateSubSellableBox();
            initSellersTable();

        }

        private void initSellersTable() {
            sellersTBLSellerCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            sellersTBLPriceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
            sellersTBLNumberAvailableCOL.setCellValueFactory(new PropertyValueFactory<>("available"));
            for (String[] pack : subSellablePacks) {
                sellers.add(new SellerWrapper(pack[12], pack[8], pack[9], pack, this));
            }
            sellersTBL.setItems(FXCollections.observableArrayList(sellers));
        }

        private void updateBuyersTable() {
            try {
                switch (type) {
                    case Constants.customerUserType:
                    case Constants.anonymousUserType:
                        tabPane.getTabs().remove(buyersTab);
                        return;
                    case Constants.sellerUserType:
                        if (sellerController.doesSellSubProduct(subSellablePack[1])) {
                            if (!tabPane.getTabs().contains(buyersTab))
                                tabPane.getTabs().add(buyersTab);
                        } else
                            tabPane.getTabs().remove(buyersTab);
                        break;
                    case Constants.adminUserType:
                        if (!tabPane.getTabs().contains(buyersTab))
                            tabPane.getTabs().add(buyersTab);
                        break;
                }
                ArrayList<BuyerWrapper> buyers = new ArrayList<>();
                for (String s : mainController.getBuyersOfASubProduct(subSellablePack[1])) {
                    buyers.add(new BuyerWrapper(s));
                }
                buyerCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
                BuyersTBL.setItems(FXCollections.observableArrayList(buyers));
            } catch (Exceptions.InvalidSubProductIdException e) {
                e.printStackTrace();
            }

        }

        private void initMainObjects() {
            if (subSellablePack[15].equals("SubFile")) {
                brandExtension.setText("Extension: ");
            }
            nameLBL.setText(subSellablePack[2]);
            brandExtensionLBL.setText(subSellablePack[3]);
            sellableInfo.setText(subSellablePack[13]);
            ratingCountLBL.setText(subSellablePack[5]);
            categoryLBL.setText(sellablePack[7]);

            String imgPath = "src/main/resources/temp/digestImg.png";
            File f = Path.of(imgPath).toFile();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] byteArray = mainController.loadFileFromDataBase(sellablePack[8]);
            try (FileOutputStream stream = new FileOutputStream(imgPath)) {
                stream.write(byteArray);
                sellableIMG.setImage(new Image("file:" + imgPath));
            } catch (IOException e) {
                e.printStackTrace();
            }


            initRatingStars();
        }


        //Done...
        private void initRatingStars() {
            double rating = Double.parseDouble(sellablePack[4]);
            fullStar1.setVisible(rating >= 1);
            fullStar2.setVisible(rating >= 2);
            fullStar3.setVisible(rating >= 3);
            fullStar4.setVisible(rating >= 4);
            fullStar5.setVisible(rating >= 5);

            halfStar1.setVisible(rating >= 0.5);
            halfStar2.setVisible(rating >= 1.5);
            halfStar3.setVisible(rating >= 2.5);
            halfStar4.setVisible(rating >= 3.5);
            halfStar5.setVisible(rating >= 4.5);

        }

        //Done...
        private void setPacks(String sellableId, String subSellableId) {
            try {
                sellablePack = mainController.digest(sellableId);
                subSellablePack = mainController.getSubProductByID(subSellableId);
                subSellablePacks = mainController.subSellablesOfASellable(sellablePack[0]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //Done...
        private void updateSubSellableBox() {
            sellerLBL.setText(subSellablePack[12]);
            if (subSellablePack[19] == null) {
                if (!subSellablePack[7].equals(subSellablePack[8])) {
                    priceBeforeLBL.setText(subSellablePack[7] + "$");
                    priceAfterLBL.setText(subSellablePack[8] + "$");
                    priceBeforeLBL.setVisible(true);
                } else {
                    priceBeforeLBL.setVisible(false);
                    priceAfterLBL.setText(subSellablePack[7] + "$");
                }
                if (subSellablePack[11] != null) {
                    salePercentageLBL.setVisible(true);
                    salePercentageLBL.setText(subSellablePack[11] + "%");
                } else {
                    salePercentageLBL.setVisible(false);
                }
                soldOutLBL.setVisible(subSellablePack[15].equals("SubProduct") && Integer.parseInt(subSellablePack[9]) == 0);
                auctionLBL.setVisible(false);
            } else {
                soldOutLBL.setVisible(false);
                salePercentageLBL.setVisible(false);
                auctionLBL.setVisible(true);
                priceBeforeLBL.setVisible(false);
                priceAfterLBL.setVisible(true);
                priceAfterLBL.setText(subSellablePack[16]);
            }

            //subProductBoxPack[9] = Integer.toString(subProduct.getRemainingCount());
            updateShowOfButtons();
            updateBuyersTable();
        }

        //Done...
        private void initReviewsVB() {
            try {
                ArrayList<String[]> reviews = mainController.reviewsOfProductWithId(sellablePack[0]);
                for (String[] review : reviews) {
                    reviewsVB.getChildren().add(ReviewBoxController.createReviewBox(review));
                }
            } catch (Exceptions.InvalidSellableIdException e) {
                System.out.println(e.getMessage());
            }
        }

        //Done...
        private void initPropertiesTable() {
            try {
                HashMap<String, String> propertyValues = mainController.getPropertyValuesOfAProduct(sellablePack[0]);
                for (String s : propertyValues.keySet()) {
                    properties.add(new PropertyWrapper(s, propertyValues.get(s)));
                }
                propertyCOL.setCellValueFactory(new PropertyValueFactory<>("propertyLBL"));
                valueCOL.setCellValueFactory(new PropertyValueFactory<>("valueLBL"));
                PropertiesTBL.setItems(FXCollections.observableArrayList(properties));
            } catch (Exceptions.InvalidSellableIdException e) {
                System.out.println(e.getMessage());
            }
        }

        //Done...
        private void initCategoryHBox() {
            try {
                HBox categoryHBox = CategoryTreeBoxController.createBox();
                if (categoryHBox != null) {
                    for (String s : mainController.getCategoryTreeOfAProduct(sellablePack[0])) {
                        categoryHBox.getChildren().add(new Label(s));
                    }
                    categoryHBox.getChildren().add(new Label(sellablePack[1]));
                    borderPane.setTop(categoryHBox);
                }
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }
        }

        //Done...
        private void addToCart() {
            try {
                mainController.addToCart(subSellablePack[1], 1);
            } catch (Exceptions.UnavailableProductException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (Exceptions.InvalidSubProductIdException | Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
            }
        }

        //Done...
        private void edit() {
            EditProductPopupController.display(sellablePack[0], subSellablePack[1]);
        }

        //Done...
        private void compare() {
            ProductsMenuController.displayACategoryProductsToCompare(sellablePack[7], sellablePack[0]);
            if (editable) {
                compareBTN.getScene().getWindow().hide();
            }
        }

        //Done...
        private void addReview() {
            AddReviewPopupController.display(sellablePack[0]);
        }

        //Done
        private void rate() {
            ratingsStackPane.getChildren().remove(rateBTN);
            ratingsBox.setVisible(false);
            ratingsStackPane.getChildren().add(RatingBoxController.createBox(sellablePack[0]));
        }

        private void download() {
            if (View.type.get().equals(Constants.customerUserType)) {
                try {

                    byte[] file = customerController.downloadFile(subSellablePack[1]);
                    DirectoryChooser dc = new DirectoryChooser();
                    File f = dc.showDialog(new Stage());
                    File f2 = new File(f.getAbsolutePath() + "/" + subSellablePack[2] + "." + subSellablePack[3]);
                    if (!f2.exists()) {
                        f2.createNewFile();
                    }
                    OutputStream outputStream = new FileOutputStream(f2);
                    outputStream.write(file);
                    outputStream.close();
                } catch (Exceptions.InvalidFileIdException | IOException e) {
                    e.printStackTrace();
                } catch (Exceptions.HaveNotBoughtException e) {
                    purchaseTheFile();
                }
            }
        }

        private void purchaseTheFile() {
            PurchaseMenuController.displayFileMode(Double.parseDouble(subSellablePack[8]), subSellablePack[1]);
        }

        //Done
        private void auction() {
            AuctionPopupController.display(subSellablePack[19]);
        }

        //Done
        private void initButtons() {
            addToCartBTN.setOnAction(e -> addToCart());
            editBTN.setOnAction(e -> edit());
            compareBTN.setOnAction(e -> compare());
            addReviewBTN.setOnAction(e -> addReview());
            rateBTN.setOnAction(e -> rate());
            downloadBTN.setOnAction(e -> download());
            auctionBTN.setOnAction(e -> auction());

            updateShowOfButtons();
        }


        private void updateShowOfButtons() {
            if (type.equals(Constants.customerUserType) || type.equals(Constants.anonymousUserType)) {
                if (subSellablePack[19] == null) // not in an auction
                {
                    if (subSellablePack[15].equals("SubProduct")) {
                        if (Integer.parseInt(subSellablePack[9]) != 0) {
                            addToCartBTN.setVisible(true);
                            addToCartBTN.setDisable(false);
                        } else {
                            addToCartBTN.setVisible(true);
                            addToCartBTN.setDisable(true);
                        }
                        downloadBTN.setVisible(false);
                    } else {
                        downloadBTN.setVisible(true);
                        addToCartBTN.setVisible(false);
                    }
                    auctionBTN.setVisible(false);
                } else {// in an auction
                    auctionBTN.setVisible(true);
                    downloadBTN.setVisible(false);
                    addToCartBTN.setVisible(false);
                }
            } else {
                addToCartBTN.setVisible(false);
                downloadBTN.setVisible(false);
            }

            if (type.equals(Constants.anonymousUserType)) {
                downloadBTN.setVisible(false);
            }

            if ((type.equals(Constants.adminUserType)) && editable) {
                editBTN.setVisible(true);
            } else editBTN.setVisible(type.equals(Constants.sellerUserType) && editable);

            try {
                //rate button
                rateBTN.setVisible((type.equals(Constants.customerUserType)) && customerController.hasBought(sellablePack[0]));
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }
            //add review button
            addReviewBTN.setVisible(type.equals(Constants.customerUserType));

            compareBTN.setVisible(true);

        }

    }

    public static class ReviewBoxController {
        @FXML
        private Label titleLBL;

        @FXML
        private Label nameLBL;

        @FXML
        private TextArea text;

        @FXML
        private Label boughtLBL;

        @FXML
        private Label didntBuyLBL;

        public static Parent createReviewBox(String[] reviewPack) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.reviewBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                ReviewBoxController rbc = loader.getController();
                rbc.setInfo(reviewPack);
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setInfo(String[] review) {
            nameLBL.setText(review[0]);
            titleLBL.setText(review[1]);
            text.setText(review[2]);
            boughtLBL.setVisible(review[3].equals("yes"));
            didntBuyLBL.setVisible(review[3].equals("no"));

        }
    }

    public static class AddReviewPopupController implements Initializable {
        @FXML
        private Button sendReviewBTN;

        @FXML
        private TextArea text;

        @FXML
        private TextField title;

        private String productId;

        public static void display(String productId) {
            AddReviewPopupController controller = View.popupWindow("Add Review", Constants.FXMLs.addReviewPopup, 550, 200);
            if (controller != null) {
                controller.productId = productId;
            }

        }

        private void addReview() {
            if (title.getText() != null && text.getText() != null) {
                try {
                    mainController.addReview(productId, title.getText(), text.getText());
                    title.getScene().getWindow().hide();
                } catch (Exceptions.InvalidSellableIdException | Exceptions.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            sendReviewBTN.setOnAction(e -> addReview());
        }
    }

    public static class CategoryTreeBoxController {
        @FXML
        private HBox HBox;

        public static HBox createBox() {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.categoryTreeBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                return (HBox) p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public static class LoginPopupController implements Initializable {

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

        public static void display() {
            View.popupWindow("Login Menu", Constants.FXMLs.loginPopup, 600, 450);
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
                    registerLink.getScene().getWindow().hide();
                } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException ex) {
                    errorLBL.setText("invalid username or password");
                    errorLBL.setTextFill(Color.RED);
                    ex.printStackTrace();
                }
            });
            registerLink.setOnAction(e -> {
                RegisterPopupController.display();
                registerLink.getScene().getWindow().hide();
            });
        }

        private void initPasswordStuff() {
            showPasswordField.textProperty().bindBidirectional(passwordField.textProperty());
            showPasswordField.visibleProperty().bind(passwordField.visibleProperty().not());
            passwordField.visibleProperty().bind(showPasswordBTN.selectedProperty().not());
        }
    }

    public static class RegisterPopupController implements Initializable {

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
        private TextField sellerShowPasswordField;

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

        public static void display() {
            View.popupWindow("Register Menu", Constants.FXMLs.registerPopup, 1000, 700);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTexts();
            initVisibilities();
            initListeners();
            initActions();
            initPasswordStuff();
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

            sellerStoreNameError.setText("Please enter a store name!");
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

            sellerStoreNameError.setVisible(false);
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
                        byte[] image = new byte[0];
                        try {
                            image = Files.readAllBytes(Paths.get(customerImageField.getText()));
                        } catch (IOException ex) {
                            //do nothing;
                        }
                        mainController.creatAccount(Constants.customerUserType, customerUsername.getText(),
                                customerPassword.getText(), customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhoneNumber.getText(), Double.parseDouble(customerBalance.getText()), null, image);
                        sellerLoginHL.getScene().getWindow().hide();
                        LoginPopupController.display();
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
                        byte[] image = new byte[0];
                        try {
                            image = Files.readAllBytes(Paths.get(customerImageField.getText()));
                        } catch (IOException ex) {
                            //nothing
                        }
                        mainController.creatAccount(Constants.sellerUserType, sellerUsername.getText(),
                                sellerPassword.getText(), sellerFirstName.getText(), sellerLastName.getText(),
                                sellerEmail.getText(), sellerPhoneNumber.getText(), Double.parseDouble(sellerBalance.getText()), sellerStoreName.getText(), image);
                        sellerLoginHL.getScene().getWindow().hide();
                        LoginPopupController.display();
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        sellerUsernameError.setText("sorry! username already taken");
                        sellerUsernameError.setVisible(true);
                    } catch (Exceptions.AdminRegisterException ex) {
                        //wont happen
                    }
                }
            });
            sellerLoginHL.setOnAction(e -> {
                sellerLoginHL.getScene().getWindow().hide();
                LoginPopupController.display();
            });
            customerLoginHL.setOnAction(e -> {
                sellerLoginHL.getScene().getWindow().hide();
                LoginPopupController.display();
            });

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
            if (sellerStoreName.getText().equals("")) {
                sellerStoreNameError.setVisible(true);
                areAvailable = false;
            } else sellerStoreNameError.setVisible(false);
            return areAvailable;
        }

        private void initPasswordStuff() {
            customerShowPasswordField.textProperty().bindBidirectional(customerPassword.textProperty());
            customerShowPasswordField.visibleProperty().bind(customerPassword.visibleProperty().not());
            customerPassword.visibleProperty().bind(customerShowPasswordBTN.selectedProperty().not());

            sellerShowPasswordField.textProperty().bindBidirectional(sellerPassword.textProperty());
            sellerShowPasswordField.visibleProperty().bind(sellerPassword.visibleProperty().not());
            sellerPassword.visibleProperty().bind(sellerShowPasswordBTN.selectedProperty().not());
        }
    }

    public static class AdminProductManagingMenu implements Initializable {
        @FXML
        private TableView<ProductWrapper> products;

        @FXML
        private TableColumn<ProductWrapper, String> productIdCol;

        @FXML
        private TableColumn<ProductWrapper, String> productNameCOL;

        @FXML
        private TableColumn<ProductWrapper, String> productCategoryCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> productDetailsCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> productRemoveCOL;

        @FXML
        private Label productErrorLBL;

        @FXML
        private TableView<ProductWrapper> files;

        @FXML
        private TableColumn<ProductWrapper, String> fileIdCol;

        @FXML
        private TableColumn<ProductWrapper, String> fileNameCOL;

        @FXML
        private TableColumn<ProductWrapper, String> fileCategoryCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> fileDetailsCOL;

        @FXML
        private TableColumn<ProductWrapper, Button> fileRemoveCOL;

        @FXML
        private Label fileErrorLBL;

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminProductManagingMenu);
        }

        public class ProductWrapper {
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

                detailBTN.setOnAction(e -> SellableDetailMenuController.display(id, true));
                detailBTN.getStyleClass().add("details-button");

                removeBTN.setOnAction(e -> {
                    try {
                        adminController.removeProduct(id);
                        products.getItems().remove(this);
                        files.getItems().remove(this);
                    } catch (Exceptions.InvalidSellableIdException ex) {
                        ex.printStackTrace();
                    }
                });
                removeBTN.getStyleClass().add("remove-button");
            }

            public String getId() {
                return id;
            }

            public String getNameBrand() {
                return nameBrand;
            }

            public String getCategory() {
                return category;
            }

            public Button getDetailBTN() {
                return detailBTN;
            }

            public Button getRemoveBTN() {
                return removeBTN;
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
        }

        private void initTable() {
            productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            fileIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            productNameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            fileNameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            productCategoryCOL.setCellValueFactory(new PropertyValueFactory<>("category"));
            fileCategoryCOL.setCellValueFactory(new PropertyValueFactory<>("category"));
            productDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailBTN"));
            fileDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailBTN"));
            productRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));
            fileRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));

            initItems();
        }

        private void initItems() {
            var allProducts = adminController.manageAllProducts().stream().map(ProductWrapper::new).collect(Collectors.toCollection(ArrayList::new));
            products.getItems().setAll(allProducts);

            var allFiles = adminController.manageAllFiles().stream().map(ProductWrapper::new).collect(Collectors.toCollection(ArrayList::new));
            files.getItems().setAll(allFiles);
        }
    }

    public static class AdminDiscountManagingMenuController implements Initializable {

        public static AdminDiscountManagingMenuController currentObject;
        private static ArrayList<DiscountWrapper> activeDiscountWrappers;
        private static ArrayList<DiscountWrapper> archiveDiscountWrappers;

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
        private TableView<DiscountWrapper> archive;

        @FXML
        private TableColumn<DiscountWrapper, String> archiveIdCol;

        @FXML
        private TableColumn<DiscountWrapper, String> archiveCodeCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> archivePercentageCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> archiveStartDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, String> archiveEndDateCOL;

        @FXML
        private TableColumn<DiscountWrapper, Button> archiveDetailsCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addDiscountBTN;

        public void addDiscount(String[] info) {
            DiscountWrapper newItem = new DiscountWrapper(info);
            activeDiscountWrappers.add(newItem);
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
            activeDiscountWrappers = new ArrayList<>();
            archiveDiscountWrappers = new ArrayList<>();

            for (String discount : adminController.viewActiveDiscountCodes()) {
                String[] details;
                try {
                    details = adminController.viewDiscountCodeByCode(discount);
                    activeDiscountWrappers.add(new DiscountWrapper(details));
                } catch (Exceptions.DiscountCodeException e) {
                    e.printStackTrace();
                }
            }

            for (String discountCode : adminController.viewArchiveDiscountCodes()) {
                String[] details;
                try {
                    details = adminController.viewDiscountCodeById(discountCode);
                    archiveDiscountWrappers.add(new DiscountWrapper(details));
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

            archiveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            archiveCodeCOL.setCellValueFactory(new PropertyValueFactory<>("code"));
            archivePercentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            archiveStartDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            archiveEndDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            archiveDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("detail"));

            discounts.getItems().setAll(activeDiscountWrappers);
            archive.getItems().setAll(archiveDiscountWrappers);
        }

        public class DiscountWrapper {
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
                detail.setOnAction(e -> AdminDiscountManagingPopupController.display(this, discounts.getItems().contains(this)));
                remove.setOnAction(e -> {
                    try {
                        adminController.removeDiscountCode(code);
                        archive.getItems().add(this);
                        discounts.getItems().remove(this);
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

                details.setOnAction(e -> {
                    switch (type) {
                        case "AddProductRequest":
                            AddProductRequestPopupController.display(id);
                            break;
                        case "AddReviewRequest":
                            AddReviewRequestPopupController.display(id);
                            break;
                        case "AddSaleRequest":
                            AddSaleRequestPopupController.display(id);
                            break;
                        case "AddSellerRequest":
                            AddSellerRequestPopupController.display(id);
                            break;
                        case "EditProductRequest":
                        case "EditSaleRequest":
                        case "EditFileRequest":
                            EditRequestPopupController.display(id);
                            break;
                        case "AddFileRequest":
                            AddFileRequestPopupController.display(id);
                            break;
                        case "AddAuctionRequest":
                            AddAuctionRequestPopupController.display(id);
                            break;
                    }
                });

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

    public static class AdminCategoryManagingMenuController implements Initializable {

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

        void removeItem(String name) {
            try {
                adminController.removeCategory(name);
                ArrayList<CategoryWrapper> toBeRemoved = new ArrayList<>();
                for (CategoryWrapper item : categories.getItems()) {
                    if (item.parent.get().equals(name) || item.name.get().equals(name)) toBeRemoved.add(item);
                }
                categories.getItems().removeAll(toBeRemoved);
            } catch (Exceptions.InvalidCategoryException ex) {
                ex.printStackTrace();
            }
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

                remove.setOnAction(e -> removeItem(name));

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
        private TabPane tableTabPane;

        @FXML
        private TableView<PropertyWrapper> properties;

        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;

        @FXML
        private TableColumn<PropertyWrapper, Button> propertyRemoveCOL;

        @FXML
        private Label propertyErrorLBL;

        @FXML
        private TextField newPropertyField;

        @FXML
        private Button confirmBTN;

        @FXML
        private Button cancelBTN;

        @FXML
        private Tab productsTAB;

        @FXML
        private TableView<MiniProductWrapper> products;

        @FXML
        private TableColumn<MiniProductWrapper, String> productCOL;

        @FXML
        private TableColumn<MiniProductWrapper, Button> productRemoveCOL;

        @FXML
        private Label productErrorLBL;

        @FXML
        private Tab subCategoriesTAB;

        @FXML
        private TableView<SubCategoryWrapper> subCategories;

        @FXML
        private TableColumn<SubCategoryWrapper, String> subCategoryCOL;

        @FXML
        private TableColumn<SubCategoryWrapper, Button> subCategoryRemoveCOL;

        @FXML
        private Label subCategoryErrorLBL;

        @FXML
        private Label idKeyLBL;

        @FXML
        private Label idValueLBL;

        @FXML
        private TextField nameField;

        @FXML
        private TextField parentField;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addBTN;

        @FXML
        private HBox editHB;

        @FXML
        private Button editBTN;

        @FXML
        private Button discardBTN;

        @FXML
        private HBox addPropertyHB;

        private AdminCategoryManagingMenuController.CategoryWrapper category;
        private final ArrayList<PropertyWrapper> categoryProperties = new ArrayList<>();
        private final ArrayList<MiniProductWrapper> categoryProducts = new ArrayList<>();
        private final ArrayList<SubCategoryWrapper> categorySubCategories = new ArrayList<>();

        private final SimpleBooleanProperty nameFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty parentFieldChanged = new SimpleBooleanProperty(false);

        public class PropertyWrapper {
            String property;
            Button removeBTN = new Button();

            public PropertyWrapper(String property) {
                this.property = property;
                removeBTN.getStyleClass().add("remove-button");

                removeBTN.setOnAction(e -> {
                    if (category != null) {
                        try {
                            adminController.removePropertyFromACategory(category.name.get(), this.property);
                        } catch (Exceptions.InvalidCategoryException ex) {
                            ex.printStackTrace();
                        }
                    }
                    properties.getItems().remove(this);
                });
            }

            public String getProperty() {
                return property;
            }

            public Button getRemoveBTN() {
                return removeBTN;
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
                        Bindings.createObjectBinding(() ->
                                !nameFieldChanged.get() && !parentFieldChanged.get(), nameFieldChanged, parentFieldChanged)
                );
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
                        adminController.removeCategory(this.name);
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    }
                    subCategories.getItems().remove(this);
                    AdminCategoryManagingMenuController.currentController.removeItem(name);
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

            parentField.setPromptText("Leave blank to have no parent.");

            initVisibility();
            initValues();
            initBindings();
            initTable();
            initActions();
        }

        private void initVisibility() {
            boolean isDetail = category != null;

            parentField.setEditable(!isDetail);
            editHB.setVisible(isDetail);
            addBTN.setVisible(!isDetail);
            idKeyLBL.setVisible(isDetail);
            idValueLBL.setVisible(isDetail);
            if (!isDetail) {
                tableTabPane.getTabs().removeAll(subCategoriesTAB, productsTAB);
            }
        }

        private void initValues() {
            if (category != null) {
                nameField.setText(category.name.get());
                parentField.setText(category.parent.get());
                idValueLBL.setText(category.id);
            }
        }

        private void initActions() {
            addBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        adminController.addCategory(nameField.getText(), parentField.getText().equals("") ? "SuperCategory" : parentField.getText(),
                                properties.getItems().stream().map(PropertyWrapper::getProperty).collect(Collectors.toCollection(ArrayList::new)));
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
                        discardBTN.getScene().getWindow().hide();
                    } catch (Exceptions.SubCategoryException | Exceptions.SameAsPreviousValueException |
                            Exceptions.ExistingCategoryException | Exceptions.InvalidFieldException |
                            Exceptions.InvalidCategoryException ex) {
                        printError(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            discardBTN.setOnAction(e -> properties.getScene().getWindow().hide());
            cancelBTN.setOnAction(e -> newPropertyField.setText(""));

            confirmBTN.setOnAction(e -> {
                if (newPropertyField.getText().equals("")) {
                    printError("Field is empty");
                    return;
                }

                if (category == null) {
                    properties.getItems().add(new PropertyWrapper(newPropertyField.getText()));
                    newPropertyField.setText("");
                } else {
                    try {
                        adminController.addPropertyToACategory(category.name.get(), newPropertyField.getText());
                        properties.getItems().add(new PropertyWrapper(newPropertyField.getText()));
                        newPropertyField.setText("");
                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Property added successfully");
                    } catch (Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.ExistingPropertyException ex) {
                        printError("This property already exists!");
                    }
                }
            });
        }

        private void initTable() {
            productCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            productRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            subCategoryCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
            subCategoryRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            propertyCOL.setCellValueFactory(new PropertyValueFactory<>("property"));
            propertyRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));

            initTableItems();
        }

        private void initTableItems() {
            if (category != null) {
                try {
                    for (String[] subCategory : mainController.getSubCategoriesOfThisCategory(category.name.get())) {
                        categorySubCategories.add(new SubCategoryWrapper(subCategory[0], subCategory[1]));
                    }
                    for (String[] product : mainController.getProductsOfThisCategory(category.name.get())) {
                        categoryProducts.add(new MiniProductWrapper(product));
                    }
                    for (String property : mainController.getPropertiesOfCategory(category.name.get(), false)) {
                        categoryProperties.add(new PropertyWrapper(property));
                    }

                    subCategories.getItems().setAll(categorySubCategories);
                    products.getItems().setAll(categoryProducts);
                    properties.getItems().setAll(categoryProperties);
                } catch (Exceptions.InvalidCategoryException e) {
                    e.printStackTrace();
                }
            }
        }

        public static class MiniProductWrapper {
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
                    } catch (Exceptions.InvalidSellableIdException ex) {
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

        private void printError(String err) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(err);
        }

        private boolean validateFields() {
            if (!nameField.getText().matches("\\w+")) {
                printError("Invalid characters in category name!");
                return false;
            } else return true;
        }
    }

    public static class SellerProductManagingMenuController implements Initializable {

        @FXML
        private TableView<SellerSubProductWrapper> productsTBL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> productIdCol;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> productNameCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> productPriceCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> productSaleCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> productDetailsCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> productRemoveCOL;

        @FXML
        private TableView<SellerSubProductWrapper> filesTBL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> fileIdCol;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> fileNameCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> filePriceCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, String> fileSaleCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> fileDetailsCOL;

        @FXML
        private TableColumn<SellerSubProductWrapper, Button> fileRemoveCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addSellableBTN;

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerSellableManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
            initButtons();
        }

        private void initTable() {
            initColumns();
            sellerController.manageProducts().forEach(p -> productsTBL.getItems().add(new SellerSubProductWrapper(p)));
            sellerController.manageFiles().forEach(f -> filesTBL.getItems().add(new SellerSubProductWrapper(f)));
        }

        private void initColumns() {
            productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            fileIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            productNameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            fileNameCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrand"));
            productPriceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
            filePriceCOL.setCellValueFactory(new PropertyValueFactory<>("price"));
            productSaleCOL.setCellValueFactory(new PropertyValueFactory<>("saleId"));
            fileSaleCOL.setCellValueFactory(new PropertyValueFactory<>("saleId"));
            productDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            fileDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            productRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            fileRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initButtons() {
            addSellableBTN.setOnAction(e -> AddProductPopupController_Page1.display());
        }

        public static class SellerSubProductWrapper {
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

                details.setOnAction(e -> SellableDetailMenuController.display(productId, id, true));

                remove.setOnAction(e -> {
                    try {
                        sellerController.removeProduct(id);
                    } catch (Exceptions.InvalidSellableIdException ex) {
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
                return saleId == null ? "-" : saleId;
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
    }

    //add product detail menu
    public static class ShoppingCartMenuController implements Initializable {

        public static ShoppingCartMenuController current;
        private static ArrayList<String[]> cartProducts = new ArrayList<>();
        private static ArrayList<SubProductWrapper> subProducts = new ArrayList<>();
        private final SimpleDoubleProperty totalPriceProperty = new SimpleDoubleProperty(0);
        NumberBinding totalPriceBinding = new SimpleDoubleProperty(0).add(0);

        public class SubProductWrapper {
            ImageView img = new ImageView();
            String imagePath;
            String subProductId;
            String productId;
            Label nameBrandSeller;
            double unitPrice;
            SimpleIntegerProperty countProperty = new SimpleIntegerProperty();
            TextField countField = new TextField();
            HBox countGroup = new HBox();
            Button increaseBTN = new Button();
            Button decreaseBTN = new Button();
            SimpleDoubleProperty totalPrice = new SimpleDoubleProperty();
            Button remove = new Button();

            public SubProductWrapper(String[] productInCartPack) {
                this(productInCartPack[0], productInCartPack[1],
                        productInCartPack[2] + " - " + productInCartPack[3] + " (" + productInCartPack[4] + ")",
                        Double.parseDouble(productInCartPack[7]), Integer.parseInt(productInCartPack[6]), productInCartPack[8]);
            }

            public SubProductWrapper(String id, String productId, String nameBrandSeller, double unitPrice, int count, String imagePath) {
                this.subProductId = id;
                this.productId = productId;
                this.nameBrandSeller = new Label(nameBrandSeller);
                this.unitPrice = unitPrice;
                this.countProperty.set(count);
                this.imagePath = imagePath;
                this.totalPrice.bind(countProperty.multiply(unitPrice));

                countField.textProperty().bindBidirectional(countProperty, new NumberStringConverter());
                View.addListener(countField, "[0-9]");
                countProperty.addListener(((observable, oldValue, newValue) -> {
                    if (newValue.intValue() > oldValue.intValue()) {
                        if (oldValue.intValue() == 0) return;
                        try {
                            mainController.increaseProductInCart(this.subProductId, newValue.intValue() - oldValue.intValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (newValue.intValue() == 0) ((IntegerProperty) observable).set(1);

                        try {
                            mainController.decreaseProductInCart(this.subProductId, oldValue.intValue() - newValue.intValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));

                img.setFitHeight(60);
                img.setPreserveRatio(true);

                String imgPath = "src/main/resources/temp/shoppingCart_" + subProductId + ".png";
                File f = Path.of(imgPath).toFile();
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                byte[] byteArray = mainController.loadFileFromDataBase(imagePath);
                try (FileOutputStream stream = new FileOutputStream(imgPath)) {
                    stream.write(byteArray);
                    img.setImage(new Image("file:" + imgPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                initButtons();
            }

            private void initButtons() {
                remove.getStyleClass().add("remove-button");
                remove.setOnAction(e -> {
                    try {
                        mainController.removeSubProductFromCart(this.subProductId);
                        totalPrice.unbind();
                        totalPrice.set(0);
                        productsTable.getItems().remove(this);
                    } catch (Exceptions.InvalidSubProductIdException ex) {
                        ex.printStackTrace();
                    }
                });

                this.nameBrandSeller.setOnMouseClicked(e -> SellableDetailMenuController.display(productId, subProductId, false));

                increaseBTN.getStyleClass().add("increase-button");
                decreaseBTN.getStyleClass().add("decrease-button");

                increaseBTN.setOnAction(e -> countProperty.set(countProperty.get() + 1));
                decreaseBTN.setOnAction(e -> countProperty.set(countProperty.get() - 1));

                countGroup.getChildren().addAll(decreaseBTN, countField, increaseBTN);
                countGroup.setPadding(new Insets(5, 5, 5, 5));
                countGroup.setAlignment(Pos.CENTER);

            }


            public ImageView getImg() {
                return img;
            }

            public String getSubProductId() {
                return subProductId;
            }

            public String getProductId() {
                return productId;
            }

            public Label getNameBrandSeller() {
                return nameBrandSeller;
            }

            public double getUnitPrice() {
                return unitPrice;
            }

            public int getCountProperty() {
                return countProperty.get();
            }

            public SimpleIntegerProperty countPropertyProperty() {
                return countProperty;
            }

            public TextField getCountField() {
                return countField;
            }

            public HBox getCountGroup() {
                return countGroup;
            }

            public double getTotalPrice() {
                return totalPrice.get();
            }

            public SimpleDoubleProperty totalPriceProperty() {
                return totalPrice;
            }

            public Button getRemove() {
                return remove;
            }

            public SimpleDoubleProperty getTotalPriceProperty() {
                return totalPrice;
            }
        }

        @FXML
        private TableView<SubProductWrapper> productsTable;

        @FXML
        private TableColumn<SubProductWrapper, ImageView> imageCOL;

        @FXML
        private TableColumn<SubProductWrapper, Label> productName;

        @FXML
        private TableColumn<SubProductWrapper, Double> productUnitPrice;

        @FXML
        private TableColumn<SubProductWrapper, HBox> count;

        @FXML
        private TableColumn<SubProductWrapper, SimpleDoubleProperty> totalPrice;

        @FXML
        private TableColumn<SubProductWrapper, Button> removeCOL;

        @FXML
        private Button clearCartBTN;

        @FXML
        private Label errorLBL;

        @FXML
        private Label totalPriceLBL;

        @FXML
        private Button purchaseBTN;

        public static void display() {

            View.setMainPane(Constants.FXMLs.shoppingCartMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            current = this;
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
                    LoginPopupController.display();
                } else {
                    PurchaseMenuController.display();
                }
            });

            clearCartBTN.setOnAction(e -> {
                if (cartProducts.size() == 0) {
                    errorLBL.setText("The cart is already empty!");
                } else {
                    mainController.clearCart();
                    productsTable.getItems().clear();
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

        public void iniTable() {
            initCols();
            try {
                cartProducts = mainController.getProductsInCart();
            } catch (Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
                return;
            }

            subProducts = new ArrayList<>();
            for (String[] cartProduct : cartProducts) {
                subProducts.add(new SubProductWrapper(cartProduct));
            }

            productsTable.getItems().setAll(subProducts);
        }

        private void initCols() {
            imageCOL.setCellValueFactory(new PropertyValueFactory<>("img"));
            productName.setCellValueFactory(new PropertyValueFactory<>("nameBrandSeller"));
            productUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            count.setCellValueFactory(new PropertyValueFactory<>("countGroup"));
            totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }
    }


    public static class PurchaseConfirmationController {

        @FXML
        private Label priceLBL;

        @FXML
        private Button dismissBTN;

        public static void display(String totalPrice) {
            ((PurchaseConfirmationController) View.popupWindow("Purchase confirmation", Constants.FXMLs.purchaseConfirmation, 500, 190)).initialize(totalPrice);
        }

        private void initialize(String totalPrice) {
            priceLBL.setText(totalPrice);
            dismissBTN.setOnAction(e -> {
                dismissBTN.getScene().getWindow().hide();
                MainMenuController.display();
            });
        }
    }


    public static class PurchaseMenuController {

        @FXML
        private TextField receiverName;

        @FXML
        private Label receiverNameLBL;

        @FXML
        private Label addressLBL;

        @FXML
        private Label phoneNumberLBL;

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

        @FXML
        private Label addressError;

        private double fileCost = 0;
        private String subFileId = null;

        public static void display() {
            PurchaseMenuController pmc = View.setMainPane(Constants.FXMLs.purchaseMenu);
            pmc.myInitialize();
        }

        public static void displayFileMode(double fileCost, String subFileId) {
            PurchaseMenuController pmc;
            pmc = View.setMainPane(Constants.FXMLs.purchaseMenu);
            pmc.fileCost = fileCost;
            pmc.subFileId = subFileId;
            pmc.initializeFileMode();

        }

        public void initializeFileMode() {
            purchaseBTN.setOnAction(e -> {
                try {
                    customerController.purchaseTheFile(subFileId, discountCode.getText() == null ? "" : discountCode.getText());
                    PurchaseConfirmationController.display(totalPrice.getText());
                } catch (Exceptions.InsufficientCreditException ex) {
                    discountError.setText("You dont have enough money!");
                } catch (Exceptions.InvalidDiscountException ex) {
                    discountError.setText("Invalid discount code!");
                } catch (Exceptions.InvalidFileIdException invalidFileIdException) {
                    invalidFileIdException.printStackTrace();
                }

            });
            totalPrice.setText("$" + fileCost);
            validateBTN.setOnAction(e -> validateDiscountFileMode());
            address.setVisible(false);
            phoneNumber.setVisible(false);
            receiverName.setVisible(false);
            addressLBL.setVisible(false);
            phoneNumberLBL.setVisible(false);
            receiverNameLBL.setVisible(false);
        }

        public void myInitialize() {
            initButtons();
            initListeners();

            try {
                totalPrice.setText("$" + customerController.getTotalPriceOfCart());
            } catch (Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
            }
        }

        private void initButtons() {
            purchaseBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        customerController.purchaseTheCart(receiverName.getText(), address.getText(), phoneNumber.getText(), discountCode.getText().equals("") ? null : discountCode.getText() == null ? "" : discountCode.getText());
                        PurchaseConfirmationController.display(totalPrice.getText());
                    } catch (Exceptions.InsufficientCreditException ex) {
                        discountError.setText("You dont have enough money!");
                    } catch (Exceptions.NotAvailableSubProductsInCart | Exceptions.EmptyCartException notAvailableSubProductsInCart) {
                        notAvailableSubProductsInCart.printStackTrace();
                    } catch (Exceptions.InvalidDiscountException ex) {
                        discountError.setText("Invalid discount code!");
                    }
                }
            });

            validateBTN.setOnAction(e -> validateDiscount());

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

        private boolean validateFields() {
            boolean valid = true;
            if (!receiverName.getText().matches(Constants.IRLNamePattern)) {
                nameError.setText("Invalid name!");
                valid = false;
            } else nameError.setText("");

            if (phoneNumber.getText().equals("")) {
                phoneError.setText("Please enter a phone number");
                valid = false;
            } else phoneError.setText("");

            if (address.getText().equals("")) {
                addressError.setText("Please enter an address!");
                valid = false;
            } else addressError.setText("");

            if (!validateDiscount()) valid = false;

            return valid;
        }

        private boolean validateDiscount() {
            if (discountCode.getText().equals("")) {
                return true;
            } else {
                try {
                    double newPrice = customerController.getTotalPriceOfCartWithDiscount(discountCode.getText());
                    discountError.setText("");
                    totalPrice.setText("$" + newPrice);
                    totalPrice.setTextFill(Color.RED);
                    return true;
                } catch (Exceptions.InvalidDiscountException e) {
                    discountError.setText("Invalid discount code");
                    return false;
                }
            }
        }

        private void validateDiscountFileMode() {
            if (!discountCode.getText().equals("")) {
                try {
                    double newPrice = customerController.getTotalPriceOfFileWithDiscount(discountCode.getText(), fileCost);
                    discountError.setText("");
                    totalPrice.setText("$" + newPrice);
                    totalPrice.setTextFill(Color.RED);
                } catch (Exceptions.InvalidDiscountException e) {
                    discountError.setText("Invalid discount code");
                }
            }
        }
    }

    public static class CustomerBuyLogMenuController implements Initializable {

        public static class BuyLogWrapper {
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

            try {
                for (String[] order : customerController.getOrders()) {
                    products.getItems().add(new BuyLogWrapper(order));
                }
            } catch (Exceptions.CustomerLoginException e) {
                e.printStackTrace();
            }
        }
    }

    public static class CustomerBuyLogDetailsPopupController {

        private ArrayList<BuyLogItemWrapper> buyItems;

        public static void display(String logId) {
            ((CustomerBuyLogDetailsPopupController)
                    View.popupWindow("Buy Log detail", Constants.FXMLs.customerBuyLogDetailsPopup, 910, 470)).init(logId);
        }

        private void init(String logId) {
            try {
                ArrayList<String[]> logDetails = customerController.getOrderWithId(logId);
                initTable(logDetails.subList(1, logDetails.size()));
                initValues(logDetails.get(0));
            } catch (Exceptions.InvalidLogIdException | Exceptions.CustomerLoginException e) {
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
            ratingCOL.setCellValueFactory(new PropertyValueFactory<>("ratingBox"));
            unitPriceCOL.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        }

        private void initValues(String[] info) {
            idLBL.setText(info[0]);
            dateLBL.setText(info[5]);
            priceLBL.setText(info[7] + "$");
            discountLBL.setText(info[8]);
            shipStatusLBL.setText(info[6]);
            receiverNameLBL.setText(info[2]);
            receiverPhoneLBL.setText(info[3]);

            StringBuilder address = new StringBuilder(info[4]);
            int offset = 0, size = address.length();
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

        public static class BuyLogItemWrapper {
            String id, nameBrandSeller, sellerStoreName;
            int count;
            double unitPrice, saleAmount;
            Parent ratingBox;

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
                ratingBox = RatingBoxController.createBox(this.id);
            }

            public Parent getRatingBox() {
                return ratingBox;
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
        private TableColumn<BuyLogItemWrapper, Parent> ratingCOL;

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
        private Button manageSellables;

        @FXML
        private Button manageCategories;

        @FXML
        private Button manageDiscounts;

        @FXML
        private Button manageRequests;

        @FXML
        private Button manageCommission;

        @FXML
        private Button manageShippings;

        @Override
        public void initialize(URL location, ResourceBundle resources) {

            initActions();
        }

        private void initActions() {
            manageAccounts.setOnAction(e -> AdminAccountManagingMenuController.display());
            manageCategories.setOnAction(e -> AdminCategoryManagingMenuController.display());
            manageDiscounts.setOnAction(e -> AdminDiscountManagingMenuController.display());
            manageSellables.setOnAction(e -> AdminProductManagingMenu.display());
            manageRequests.setOnAction(e -> AdminRequestManagingMenuController.display());
            manageCommission.setOnAction(e -> CommissionManagingPopupController.display());
            manageShippings.setOnAction(e -> AdminBuyLogManagingMenuController.display());
        }
    }

    public static class AdminBuyLogManagingMenuController implements Initializable {
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

        public static class BuyLogWrapper {
            String id, date, receiverName, shippingStatus;
            Double receivedMoney, totalDiscount;
            Button details = new Button();

            public BuyLogWrapper(String[] info) {
                this(info[0], info[5], info[2], info[6], Double.parseDouble(info[7]), Double.parseDouble(info[8]));
            }

            public BuyLogWrapper(String id, String date, String receiverName, String shippingStatus, Double receivedMoney, Double totalDiscount) {
                this.id = id;
                this.date = date;
                this.receiverName = receiverName;
                this.shippingStatus = shippingStatus;
                this.receivedMoney = receivedMoney;
                this.totalDiscount = totalDiscount;

                details.getStyleClass().add("details-button");
                details.setOnAction(e -> AdminBuyLogManagingPopupController.display(this.id));
            }

            public String getId() {
                return id;
            }

            public String getDate() {
                return date;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public String getShippingStatus() {
                return shippingStatus;
            }

            public Double getReceivedMoney() {
                return receivedMoney;
            }

            public Double getTotalDiscount() {
                return totalDiscount;
            }

            public Button getDetails() {
                return details;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.adminBuyLogManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initCols();
            initItems();
        }

        private void initCols() {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            customerCOL.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
            paidMoneyCOL.setCellValueFactory(new PropertyValueFactory<>("receivedMoney"));
            discountAmountCOL.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
            shippingStatusCOL.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
        }

        private void initItems() {
            adminController.getAllBuyLogs().forEach(bl -> products.getItems().add(new BuyLogWrapper(bl)));
        }
    }

    public static class AdminBuyLogManagingPopupController {
        @FXML
        private TableView<LogItemWrapper> logItems;
        @FXML
        private TableColumn<LogItemWrapper, String> subProductCOL;
        @FXML
        private TableColumn<LogItemWrapper, Integer> countCOL;
        @FXML
        private TableColumn<LogItemWrapper, Double> unitPriceCOL;
        @FXML
        private TableColumn<LogItemWrapper, Double> ratingCOL;
        @FXML
        private Label idLBL;
        @FXML
        private Label receiverPhoneLBL;
        @FXML
        private Label receiverNameLBL;
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
        @FXML
        private RadioButton processingBTN;
        @FXML
        private ToggleGroup shippingStatus;
        @FXML
        private RadioButton sendingBTN;
        @FXML
        private RadioButton receivedBTN;
        @FXML
        private Button editBTN;
        @FXML
        private Button discardBTN;

        private String[] info;
        private String logId;
        private final SimpleBooleanProperty statusChanged = new SimpleBooleanProperty(false);
        private RadioButton defaultStatus;

        public static class LogItemWrapper {
            String name, brand, seller, nameBrandSeller;
            double unitPrice, rate;
            int count;

            public LogItemWrapper(String[] pack) {
                this(pack[1], pack[2], pack[4], Double.parseDouble(pack[6]), Double.parseDouble(pack[8]), Integer.parseInt(pack[5]));
            }

            public LogItemWrapper(String name, String brand, String seller, double unitPrice, double rate, int count) {
                this.name = name;
                this.brand = brand;
                this.seller = seller;
                this.unitPrice = unitPrice;
                this.rate = rate;
                this.count = count;
                nameBrandSeller = name + " - " + brand + " (" + seller + ")";
            }

            public String getName() {
                return name;
            }

            public String getBrand() {
                return brand;
            }

            public String getSeller() {
                return seller;
            }

            public String getNameBrandSeller() {
                return nameBrandSeller;
            }

            public double getUnitPrice() {
                return unitPrice;
            }

            public double getRate() {
                return rate;
            }

            public int getCount() {
                return count;
            }
        }

        public static void display(String logId) {
            ((AdminBuyLogManagingPopupController)
                    View.popupWindow("buy log details", Constants.FXMLs.adminBuyLogMangingPopup, 1000, 550)).initialize(logId);
        }

        private void initialize(String buyLogId) {
            try {
                logId = buyLogId;
                info = adminController.getBuyLogWithId(logId);
            } catch (Exceptions.InvalidLogIdException e) {
                e.printStackTrace();
            }
            initCols();
            initItems();
            iniToggleGroup();
            initBindings();
            initValues();
            initActions();
        }

        private void initCols() {
            countCOL.setCellValueFactory(new PropertyValueFactory<>("count"));
            ratingCOL.setCellValueFactory(new PropertyValueFactory<>("rate"));
            unitPriceCOL.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            subProductCOL.setCellValueFactory(new PropertyValueFactory<>("nameBrandSeller"));
        }

        private void initItems() {
            try {
                adminController.getBuyLogItemsWithId(logId).forEach(li -> logItems.getItems().add(new LogItemWrapper(li)));
            } catch (Exceptions.InvalidLogIdException e) {
                e.printStackTrace();
            }
        }

        private void iniToggleGroup() {
            if (info[6].equals("Processing")) shippingStatus.selectToggle(defaultStatus = processingBTN);
            else if (info[6].equals("Sending")) shippingStatus.selectToggle(defaultStatus = sendingBTN);
            else shippingStatus.selectToggle(defaultStatus = receivedBTN);
        }

        private void initBindings() {
            statusChanged.bind(shippingStatus.selectedToggleProperty().isEqualTo(defaultStatus).not());
            editBTN.disableProperty().bind(statusChanged.not());
        }

        private void initValues() {
            idLBL.setText(info[0]);
            dateLBL.setText(info[5]);
            priceLBL.setText(info[7]);
            addressArea.setText(info[4]);
            discountLBL.setText(info[8]);
            shipStatusLBL.setText(info[6]);
            receiverNameLBL.setText(info[2]);
            receiverPhoneLBL.setText(info[3]);
        }

        private void initActions() {
            editBTN.setOnAction(e -> {
                String newStatus;
                RadioButton selected = ((RadioButton) shippingStatus.getSelectedToggle());
                if (selected == processingBTN) newStatus = "Processing";
                else if (selected == sendingBTN) newStatus = "Sending";
                else newStatus = "Received";
                try {
                    adminController.editBuyLogStatus(info[0], newStatus);
                    discardBTN.getScene().getWindow().hide();
                } catch (Exceptions.InvalidLogIdException ex) {
                    ex.printStackTrace();
                }
            });

            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());
        }
    }

    public static class AdminAccountManagingMenuController implements Initializable {

        public static AdminAccountManagingMenuController current;
        public ArrayList<AccountWrapper> allAdmins;
        public ArrayList<AccountWrapper> allCustomers;
        public ArrayList<AccountWrapper> allSellers;
        public ArrayList<AccountWrapper> allSupporters;

        private void initItems() {
            ArrayList<String[]> all = adminController.manageUsers();
            allSellers = new ArrayList<>();
            allCustomers = new ArrayList<>();
            allAdmins = new ArrayList<>();
            allSupporters = new ArrayList<>();

            for (String[] strings : all) {
                switch (strings[6]) {
                    case "Seller":
                        allSellers.add(new AccountWrapper(strings, sellers));
                        break;
                    case "Customer":
                        allCustomers.add(new AccountWrapper(strings, customers));
                        break;
                    case "Admin":
                        allAdmins.add(new AccountWrapper(strings, admins));
                        break;
                    default:
                        allSupporters.add(new AccountWrapper(strings, supporters));
                        break;
                }
            }

            admins.getItems().addAll(allAdmins);
            sellers.getItems().addAll(allSellers);
            customers.getItems().addAll(allCustomers);
            supporters.getItems().addAll(allSupporters);
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

        @FXML
        private TableView<AccountWrapper> supporters;

        @FXML
        private TableColumn<AccountWrapper, String> supporterIdCOL;

        @FXML
        private TableColumn<AccountWrapper, String> supporterUsernameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> supporterFullNameCOL;

        @FXML
        private TableColumn<AccountWrapper, String> supporterPhoneCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> supporterDetailsCOL;

        @FXML
        private TableColumn<AccountWrapper, Button> supporterRemoveCOL;

        @FXML
        private Button registerSupporterBTN;


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

        public void addSupporter(String username) {
            try {
                String[] info = adminController.viewUsername(username);
                supporters.getItems().add(new AccountWrapper(info[2], username, info[3], info[4], info[6], info[5], Constants.supporterUserType, supporters));
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
            registerSupporterBTN.setOnAction(e -> SupporterRegistrationPopupController.display());
        }

        private void initTable() {
            adminIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            sellerIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            supporterIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
            adminUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            sellerUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            customerUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            supporterUsernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            adminFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            sellerFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            customerFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            supporterFullNameCOL.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            adminPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            sellerPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            supporterPhoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            adminDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            sellerDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            customerDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            supporterDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));
            adminRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            sellerRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            customerRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));
            supporterRemoveCOL.setCellValueFactory(new PropertyValueFactory<>("remove"));

            initItems();
        }

        public static class AccountWrapper {
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
                    } catch (Exceptions.UsernameDoesntExistException | Exceptions.ManagerDeleteException ex) {
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
    }

    public static class AdminRegistrationPopupController implements Initializable {
        @FXML
        private TextField adminUsername;

        @FXML
        private Label adminUsernameError;

        @FXML
        private PasswordField adminPassword;

        @FXML
        private TextField adminShowPasswordField;

        @FXML
        private ToggleButton adminShowPasswordBTN;

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
            View.popupWindow("Admin registration window", Constants.FXMLs.adminRegistrationPopup, 1000, 700);
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            adminImageField.setEditable(false);
            initBindings();
            initActions();
            initLBLs();
            initVisibilities();
            initListeners();
        }

        private void initBindings() {
            adminShowPasswordField.textProperty().bindBidirectional(adminPassword.textProperty());
            adminShowPasswordField.visibleProperty().bind(adminPassword.visibleProperty().not());
            adminPassword.visibleProperty().bind(adminShowPasswordBTN.selectedProperty().not());
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
                        byte[] image = new byte[0];
                        try {
                            image = Files.readAllBytes(Paths.get(adminImageField.getText()));
                        } catch (IOException ex) {
                            //nothing
                        }
                        boolean bootUp = !mainController.doesManagerExist();
                        adminController.creatAdminProfile(adminUsername.getText(), adminPassword.getText(), adminFirstName.getText(),
                                adminLastName.getText(), adminEmail.getText(), adminPhoneNumber.getText(), image);
                        if (!bootUp) {
                            AdminAccountManagingMenuController.current.addAdmin(adminUsername.getText());
                        } else {
                            View.subStart(new Stage());
                        }
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
        private TableColumn<CustomerWrapper, HBox> countCOL;

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
        private final ArrayList<CustomerWrapper> allCustomers = new ArrayList<>();
        private final ArrayList<CustomerWrapper> removedCustomers = new ArrayList<>();
        private final ArrayList<CustomerWrapper> customersWithDiscount = new ArrayList<>();

        private final SimpleBooleanProperty codeFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty percentageFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty maxFieldChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        private void initActions(String discountId) {
            addBTN.setOnAction(e -> {
                if (fieldValidation()) {
                    try {
                        adminController.createDiscountCode(codeField.getText(), startDate.getValue().toString(), endDate.getValue().toString(),
                                Double.parseDouble(percentageField.getText()), Double.parseDouble(maxField.getText()),
                                allCustomers.stream().filter(c -> c.hasCode.isSelected()).map(c -> new String[]{c.id, String.valueOf(c.count.getText())}).collect(Collectors.toCollection(ArrayList::new)));
                        AdminDiscountManagingMenuController.currentObject.addDiscount(adminController.viewDiscountCodeByCode(codeField.getText()));
                        customersTable.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidAccountsForDiscount | Exceptions.DiscountCodeException invalidAccountsForDiscount) {
                        invalidAccountsForDiscount.printStackTrace();
                    } catch (Exceptions.InvalidFormatException ex) {
                        ex.printStackTrace();
                        printError("Invalid date format question mark.");
                    } catch (Exceptions.ExistingDiscountCodeException ex) {
                        ex.printStackTrace();
                        printError("This code already exists!");
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
                                removedCustomers.stream().map(CustomerWrapper::getId).collect(Collectors.toCollection(ArrayList::new)));

                        errorLBL.setTextFill(Color.GREEN);
                        errorLBL.setText("Changed saved successfully!");
                    } catch (Exception ex) {
                        printError(ex.getMessage());
                    }
                }
            });

            discardBTN.setOnAction(e -> customersTable.getScene().getWindow().hide());
        }

        private boolean editable;

        public static void display(AdminDiscountManagingMenuController.DiscountWrapper discount, boolean editable) {
            String discountId = discount == null ? null : discount.getId();
            ((AdminDiscountManagingPopupController)
                    View.popupWindow((discountId == null) ? "Create Discount" : "Discount Details", Constants.FXMLs.adminDiscountManagingPopup, 800, 500)).initialize(discountId, discount, editable);
        }

        private void initialize(String discountId, AdminDiscountManagingMenuController.DiscountWrapper discount, boolean editable) {
            ArrayList<String[]> customersWithCode = null;
            this.editable = editable;

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
                codeFieldChanged.bind(Bindings.when(codeField.textProperty().isEqualTo(discount.getCode())).then(false).otherwise(true));
                percentageFieldChanged.bind(Bindings.when(percentageField.textProperty().isEqualTo(discount.percentage.asString())).then(false).otherwise(true));
                maxFieldChanged.bind(Bindings.when(maxField.textProperty().isEqualTo(discount.maximumAmount.asString())).then(false).otherwise(true));
                startDateChanged.bind(Bindings.when(startDate.valueProperty().isEqualTo(LocalDate.parse("20" + discount.startDate.get()))).then(false).otherwise(true));
                endDateChanged.bind(Bindings.when(endDate.valueProperty().isEqualTo(LocalDate.parse("20" + discount.endDate.get()))).then(false).otherwise(true));
            }
        }

        private void initVisibility(String discountId, boolean editable) {
            boolean isDetail = discountId != null;
            saveDiscardHBox.setVisible(isDetail && editable);
            addBTN.setVisible(!isDetail && editable);
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

            codeField.setEditable(!isDetail);

            maxField.setEditable(editable);
            codeField.setEditable(editable);
            percentageField.setEditable(editable);

            startDate.setDisable(!editable);
            startDate.setStyle("-fx-opacity: 1");
            startDate.getEditor().setStyle("-fx-opacity: 1");
            endDate.setDisable(!editable);
            endDate.setStyle("-fx-opacity: 1");
            endDate.getEditor().setStyle("-fx-opacity: 1");
        }

        public class CustomerWrapper {
            CheckBox hasCode = new CheckBox();
            String id;
            String username;
            TextField count = new TextField();
            Button increaseBTN = new Button();
            Button decreaseBTN = new Button();
            HBox countGroup = new HBox();
            int initCount;

            public CustomerWrapper(String[] customerPack, boolean hasCode) {
                this(customerPack[2], customerPack[0], Integer.parseInt(customerPack[1]), hasCode);
            }

            public CustomerWrapper(String id, String username, int count, boolean hasCode) {
                if (hasCode) this.count.setText(count + "");
                else this.count.setText("");
                this.id = id;
                this.username = username;
                this.hasCode.setSelected(hasCode);
                this.initCount = count;
                this.count.editableProperty().bind(this.hasCode.selectedProperty().and(new SimpleBooleanProperty(editable)));
                this.countGroup.opacityProperty().bind(
                        Bindings.when(this.hasCode.selectedProperty()).then(1).otherwise(0.5)
                );
                View.addListener(this.count, Constants.unsignedIntPattern);
                this.count.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("") && Integer.parseInt(newValue) == 0) ((StringProperty) observable).set("1");
                });
                this.hasCode.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        removedCustomers.remove(this);
                        this.count.setText("1");
                    } else {
                        removedCustomers.add(this);
                        this.count.setText("");
                    }
                });

                increaseBTN.getStyleClass().add("increase-button");
                decreaseBTN.getStyleClass().add("decrease-button");

                increaseBTN.setOnAction(e -> this.count.setText((Integer.parseInt(this.count.getText()) + 1) + ""));
                decreaseBTN.setOnAction(e -> this.count.setText((Integer.parseInt(this.count.getText()) - 1) + ""));

                countGroup.getChildren().addAll(decreaseBTN, this.count, increaseBTN);
                countGroup.setPadding(new Insets(5, 5, 5, 5));
                if (!editable) {
                    increaseBTN.setVisible(false);
                    decreaseBTN.setVisible(false);
                }
            }

            public HBox getCountGroup() {
                return countGroup;
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
                return this.id.equals(((CustomerWrapper) obj).id);
            }
        }

        private void printError(String errorText) {
            errorLBL.setTextFill(Color.RED);
            errorLBL.setText(errorText);
        }

        private boolean fieldValidation() {
            if (!codeField.getText().matches("^\\w+$")) {
                printError("Invalid discount code! use only characters, digits and _ .");
                return false;
            } else if (!percentageField.getText().matches(Constants.doublePattern) || Double.parseDouble(percentageField.getText()) >= 100) {
                printError("Invalid percentage! enter a number less than 100!");
                return false;
            } else if (!maxField.getText().matches(Constants.doublePattern)) {
                printError("Invalid maximum amount! enter a number (ex. 40.5)");
                return false;
            } else if (startDate.getValue() == null) {
                printError("Please enter a valid starting date");
                return false;
            } else if (endDate.getValue() == null || endDate.getValue().compareTo(startDate.getValue()) <= 0) {
                printError("Please enter a valid ending date.");
                return false;
            } else return true;
        }

        private void initTable(String discountId) {
            countCOL.setCellValueFactory(new PropertyValueFactory<>("countGroup"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("hasCode"));
            usernameCOL.setCellValueFactory(new PropertyValueFactory<>("username"));
            if (!editable) customersTable.getColumns().remove(removeCOL);

            initItems(discountId);
        }

        private void initItems(String discountId) {
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
                    if (!customersWithDiscount.contains(cw)) {
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
                startDate.setValue(LocalDate.parse("20" + discount.startDate.get()));
                endDate.setValue(LocalDate.parse("20" + discount.endDate.get()));
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
        private ArrayList<SaleWrapper> activeSales;
        private ArrayList<SaleWrapper> archiveSales;

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
        private TableView<SaleWrapper> archive;

        @FXML
        private TableColumn<SaleWrapper, String> archiveIdCol;

        @FXML
        private TableColumn<SaleWrapper, String> archivePercentageCOL;

        @FXML
        private TableColumn<SaleWrapper, String> archiveStartDateCOL;

        @FXML
        private TableColumn<SaleWrapper, String> archiveEndDateCOL;

        @FXML
        private TableColumn<SaleWrapper, Button> archiveDetailsCOL;

        @FXML
        private Label errorLBL;

        @FXML
        private Button addSaleBTN;

        @FXML
        private Button manageAuctions;

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
                    try {
                        sellerController.removeSale(this.id);
                        archive.getItems().add(this);
                        sales.getItems().remove(this);
                    } catch (Exceptions.InvalidSaleIdException ex) {
                        ex.printStackTrace();
                    }
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
            activeSales = new ArrayList<>();
            archiveSales = new ArrayList<>();
            for (String[] sale : sellerController.viewActiveSales()) {
                activeSales.add(new SaleWrapper(sale));
            }
            for (String[] sale : sellerController.viewArchiveSales()) {
                archiveSales.add(new SaleWrapper(sale));
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

            archiveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            archivePercentageCOL.setCellValueFactory(new PropertyValueFactory<>("percentage"));
            archiveStartDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            archiveEndDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            archiveDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("details"));

            sales.getItems().setAll(activeSales);
            archive.getItems().setAll(archiveSales);
        }

        private void initActions() {
            addSaleBTN.setOnAction(e -> SellerSaleManagingPopupController.display(null, true));
        }
    }

    public static class SellerSaleManagingPopupController {

        private final ArrayList<ProductInSaleWrapper> inSales = new ArrayList<>();
        private final ArrayList<ProductInSaleWrapper> removedFormSale = new ArrayList<>();
        private final ArrayList<ProductInSaleWrapper> addedToSale = new ArrayList<>();

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
                        if (!inSales.contains(p)) allProducts.add(p);
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
        private final SimpleBooleanProperty percentageChanged = new SimpleBooleanProperty(false);

        private boolean validateFields() {
            if (!percentageField.getText().matches(Constants.doublePattern) || Double.parseDouble(percentageField.getText()) >= 100) {
                printError("Invalid percentage! enter a number less than 100!");
                return false;
            } else if (!maxField.getText().matches(Constants.doublePattern)) {
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
        private final SimpleBooleanProperty maxFieldChanged = new SimpleBooleanProperty(false);

        private void initVisibilities(String saleId, boolean editable) {
            editHB.setVisible((saleId != null) && editable);
            addBTN.setVisible((!editHB.isVisible()) && editable);
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
        private final SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);

        @FXML
        private TextField maxField;
        private final SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        @FXML
        private DatePicker startDate;

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
                    } catch (Exceptions.InvalidProductIdsForASeller | Exceptions.InvalidFormatException invalidProductIdsForASeller) {
                        invalidProductIdsForASeller.printStackTrace();
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
                        editBTN.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidDateException | Exceptions.SameAsPreviousValueException |
                            Exceptions.InvalidSaleIdException | Exceptions.InvalidFormatException |
                            Exceptions.InvalidFieldException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            discardBTN.setOnAction(e -> products.getScene().getWindow().hide());
        }

        @FXML
        private DatePicker endDate;

        private void initBindings(String saleId) {
            if (saleId != null) {
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
                        Bindings.createObjectBinding(() ->
                                !endDateChanged.get() && !startDateChanged.get() && !maxFieldChanged.get() && !percentageChanged.get(),
                                endDateChanged, startDateChanged, maxFieldChanged, percentageChanged)
                );
                editBTN.opacityProperty().bind(
                        Bindings.when(editBTN.disableProperty()).then(0.5).otherwise(1)
                );
            }
        }

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
        private Button manageSellables;

        @FXML
        private Button manageSales;

        @FXML
        private Button manageAuctions;

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
            manageSellables.setOnAction(e -> SellerProductManagingMenuController.display());
            manageSales.setOnAction(e -> SellerSaleManagingMenuController.display());
            sellLogs.setOnAction(e -> SellerSellLogsManagingMenuController.display());
            manageAuctions.setOnAction(e -> SellerAuctionMangingMenuController.display());
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

        private final ArrayList<SellLogWrapper> allSellLogs = new ArrayList<>();

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

        public static class SellLogItemWrapper {
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

            public String getNameBrand() {
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
            int offset = 0, size = address.length();
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
        private Button customerChatBTN;
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
        private Button manageBTN;
        @FXML
        private Button supporterChatBTN;
        @FXML
        private Button cartBTN;
        @FXML
        private Button loginBTN;
        @FXML
        private Button accountBTN;

        public static Parent getMainPane() {
            return currentBase.mainPane;
        }

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
                    Bindings.when(View.type.isEqualTo(Constants.adminUserType).or(View.type.isEqualTo(Constants.sellerUserType)).or(View.type.isEqualTo(Constants.supporterUserType)))
                            .then(false).otherwise(true)
            );
            manageBTN.visibleProperty().bind(cartBTN.visibleProperty().not());
            loginBTN.visibleProperty().bind(
                    Bindings.when(View.type.isEqualTo(Constants.anonymousUserType))
                            .then(true).otherwise(false)
            );
            accountBTN.visibleProperty().bind(loginBTN.visibleProperty().not());
            backBTN.visibleProperty().bind(View.getStackSizeProperty().greaterThan(1));
            supporterChatBTN.visibleProperty().bind(View.type.isEqualTo(Constants.supporterUserType));
            customerChatBTN.visibleProperty().bind(View.type.isEqualTo(Constants.customerUserType));
        }

        private void initActions() {
            logoBTN.setOnAction(e -> MainMenuController.display());
            accountBTN.setOnAction(e -> PersonalInfoMenuController.display(null));
            loginBTN.setOnAction(e -> LoginPopupController.display());
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
            supporterChatBTN.setOnAction(e -> SupporterChatMenuController.display());
            customerChatBTN.setOnAction(e -> CustomerChatPopupController.display());
        }

        private void initTexts() {
            accountBTN.textProperty().bind(
                    Bindings.createObjectBinding(() -> {
                        try {
                            return mainController.viewPersonalInfo()[0];
                        } catch (Exceptions.NotLoggedInException e) {
                            return null;
                        }
                    }, View.type)
            );
            //TODO: temporary
        }

        private void search(String input) {
            if (!input.equals("")) {
                ArrayList<String[]> products = getCurrentProducts();
                if (products != null) {
                    ProductsMenuController.display("SuperCategory", false, false);
                }
            }
        }

        //search utils.
        private ArrayList<String[]> getCurrentProducts() {
            try {
                return new ArrayList<>(mainController.getProductsOfThisCategory(Constants.SUPER_CATEGORY_NAME));

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
        private TextField productNameField;

        @FXML
        private TextField productBrandField;

        @FXML
        private Label productErrorLBL;

        @FXML
        private Button newProductBTN;

        @FXML
        private Button existingProductBTN;

        @FXML
        private TextField fileNameField;

        @FXML
        private TextField fileExtensionField;

        @FXML
        private Label fileErrorLBL;

        @FXML
        private Button newFileBTN;

        @FXML
        private Button existingFileBTN;

        public static void display() {
            View.popupWindow("Add new Product (1 of 2)", Constants.FXMLs.addSellablePopup_Page1, 900, 550);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initButtons();
        }

        private void initButtons() {
            newProductBTN.setOnAction(e -> {
                if (validateProductFields()) {
                    String productId = sellerController.isProductWithNameAndBrand(productNameField.getText(), productBrandField.getText());
                    if (productId != null || sellerController.isNameAndBrandUsed(productNameField.getText(), productBrandField.getText())) {
                        printProductError("This product already exists!");
                    } else {
                        AddProductPopupController_Page2.display(productNameField.getText(), productBrandField.getText(), null);
                        productNameField.getScene().getWindow().hide();
                    }
                }
            });

            existingProductBTN.setOnAction(e -> {
                if (validateProductFields()) {
                    String productId = sellerController.isProductWithNameAndBrand(productNameField.getText(), productBrandField.getText());
                    if (productId == null) {
                        printProductError("There is no such product!");
                    } else if (sellerController.doesSellerSellThisProduct(productId)) {
                        printProductError("You already sell this product!");
                    } else {
                        AddProductPopupController_Page2.display(productNameField.getText(), productBrandField.getText(), productId);
                        productNameField.getScene().getWindow().hide();
                    }
                }
            });

            newFileBTN.setOnAction(e -> {
                if (validateFileFields()) {
                    String fileId = sellerController.isFileWithNameAndExtension(fileNameField.getText(), fileExtensionField.getText());
                    if (fileId != null || sellerController.isNameAndExtensionUsed(fileNameField.getText(), fileExtensionField.getText())) {
                        printFileError("This file already exists!");
                    } else {
                        AddFilePopupController_Page2.display(fileNameField.getText(), fileExtensionField.getText(), null);
                        fileNameField.getScene().getWindow().hide();
                    }
                }
            });

            existingFileBTN.setOnAction(e -> {
                if (validateFileFields()) {
                    String fileId = sellerController.isFileWithNameAndExtension(fileNameField.getText(), fileExtensionField.getText());
                    if (fileId == null) {
                        printFileError("There is no such file!");
                    } else if (sellerController.doesSellerSellThisFile(fileId)) {
                        printFileError("You already sell this file!");
                    } else {
                        AddFilePopupController_Page2.display(fileNameField.getText(), fileExtensionField.getText(), fileId);
                        fileNameField.getScene().getWindow().hide();
                    }
                }
            });
        }

        private boolean validateProductFields() {
            if (productNameField.getText().equals("")) {
                printProductError("Enter a name");
                return false;
            }
            if (productBrandField.getText().equals("")) {
                printProductError("Enter a brand");
                return false;
            }

            return true;
        }

        private boolean validateFileFields() {
            if (fileNameField.getText().equals("")) {
                printProductError("Enter a name");
                return false;
            }
            if (fileExtensionField.getText().equals("")) {
                printProductError("Enter an extension");
                return false;
            }

            return true;
        }

        private void printProductError(String err) {
            productErrorLBL.setTextFill(Color.RED);
            productErrorLBL.setText(err);
        }

        private void printFileError(String err) {
            fileErrorLBL.setTextFill(Color.RED);
            productErrorLBL.setText(err);
        }
    }

    public static class AddFilePopupController_Page2 {
        @FXML
        private TextField nameField;
        @FXML
        private Label usernameErrLBL;
        @FXML
        private TextField extensionField;
        @FXML
        private Label passwordErrLBL;
        @FXML
        private ChoiceBox<String> category;
        @FXML
        private Label imageErrLBL1;
        @FXML
        private TextField imageField;
        @FXML
        private Button imageBrowseBTN;
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
        private TextField pathField;
        @FXML
        private Button pathBrowseBTN;
        @FXML
        private Label countError;
        @FXML
        private TableView<PropertyWrapper> properties;
        @FXML
        private TableColumn<PropertyWrapper, String> propertyCOL;
        @FXML
        private TableColumn<PropertyWrapper, TextField> valueCOL;
        @FXML
        private Button backBTN;
        @FXML
        private Label errorLBL;
        @FXML
        private Button addFileBTN;

        public class PropertyWrapper {
            String property;
            TextField value = new TextField();

            public PropertyWrapper(String property, String value) {
                this.property = property;
                if (value == null) {
                    this.value.setPromptText("Enter value...");
                } else {
                    this.value.setText(value);
                }
                this.value.setEditable(!exists);
            }

            public String getProperty() {
                return property;
            }

            public TextField getValue() {
                return value;
            }
        }

        private String name;
        private String extension;
        private String fileId;
        private String[] info;
        private boolean exists;

        public static void display(String name, String extension, String fileId) {
            ((AddFilePopupController_Page2) View.popupWindow("Add file popup", Constants.FXMLs.addFilePage2, 1250, 550)).initialize(name, extension, fileId);
        }

        private void initialize(String name, String extension, String fileId) {
            this.name = name;
            this.extension = extension;
            this.fileId = fileId;
            exists = (fileId != null);

            initAccessControls();
            initListeners();
            initChoiceBox();
            initValues();
            initActions();
            initTable();
        }

        private void initTable() {
            propertyCOL.setCellValueFactory(new PropertyValueFactory<>("property"));
            valueCOL.setCellValueFactory(new PropertyValueFactory<>("value"));

            if (exists)
                initItems();
        }

        private void initItems() {
            try {
                HashMap<String, String> values = mainController.getPropertyValuesOfAFile(info[0]);
                for (String s : values.keySet()) {
                    properties.getItems().add(new PropertyWrapper(s, values.get(s)));
                }
            } catch (Exceptions.InvalidFileIdException e) {
                e.printStackTrace();
            }
        }

        private void initAccessControls() {
            if (exists) {
                category.setDisable(true);
                imageBrowseBTN.setDisable(true);
                infoArea.setEditable(false);
            }
            imageField.setEditable(false);
            pathField.setEditable(false);
        }

        private void initChoiceBox() {
            category.getItems().addAll(sellerController.getAllCategories().stream().map(c -> c[0]).collect(Collectors.toCollection(ArrayList::new)));
        }

        private void initValues() {
            nameField.setText(name);
            extensionField.setText(extension);
            if (exists) {
                infoArea.setText(info[3]);
                category.getSelectionModel().select(info[7]);
            }

        }

        private void initListeners() {
            category.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> updateProperties(newValue)));

            infoArea.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue.lastIndexOf("\n") - newValue.length() == 70 && newValue.length() > oldValue.length()) {
                    ((TextArea) observable).setText(newValue + "\n");
                }
            }));
        }

        private void initActions() {
            imageBrowseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File", "*.png"),
                        new FileChooser.ExtensionFilter("Image File", "*.jpg"));
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (chosenFile != null) {
                    imageField.setText(chosenFile.getPath());
                }
            });

            pathBrowseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (chosenFile != null) {
                    pathField.setText(chosenFile.getPath());
                }
            });

            backBTN.setOnAction(e -> {
                AddProductPopupController_Page1.display();
                backBTN.getScene().getWindow().hide();
            });

            addFileBTN.setOnAction(e -> {
                if (validateFields()) {
                    HashMap<String, String> propertyMap = new HashMap<>();
                    for (PropertyWrapper item : properties.getItems()) {
                        propertyMap.put(item.property, item.value.getText());
                    }
                    try {
                        if (!exists) {
                            byte[] image = new byte[0];
                            try {
                                image = Files.readAllBytes(Paths.get(imageField.getText()));
                            } catch (IOException ex) {
                                //nothing
                            }
                            byte[] file = new byte[0];
                            try {
                                file = Files.readAllBytes(Paths.get(pathField.getText()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            sellerController.addNewFile(nameField.getText(), extensionField.getText(), infoArea.getText(), image, category.getValue(),
                                    propertyMap, Double.parseDouble(priceField.getText()), file);
                        } else {
                            byte[] file = new byte[0];
                            try {
                                file = Files.readAllBytes(Paths.get(pathField.getText()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            sellerController.addNewSubFileToAnExistingFile(fileId, Double.parseDouble(priceField.getText()), file);
                        }
                    } catch (Exceptions.InvalidFileIdException | Exceptions.ExistingFileException | Exceptions.InvalidCategoryException ex) {
                        ex.printStackTrace();
                    }
                    addFileBTN.getScene().getWindow().hide();
                }
            });
        }

        private boolean validateFields() {
            if (category.getValue() == null) {
                printError("Please choose a category");
                return false;
            }
            if (pathField.getText().equals("")) {
                printError("Please choose a file");
                return false;
            }
            if (!priceField.getText().matches(Constants.doublePattern)) {
                printError("Invalid price! Please enter a double number");
                return false;
            }
            return true;
        }

        private void updateProperties(String categoryName) {
            try {
                if (!exists) {
                    properties.getItems().clear();
                    for (String category : mainController.getPropertiesOfCategory(categoryName, true)) {
                        properties.getItems().add(new PropertyWrapper(category, null));
                    }
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

            public PropertyWrapper(String property, String value) {
                this.property = property;
                if (value == null) {
                    this.value.setPromptText("Enter value...");
                } else {
                    this.value.setText(value);
                }
                this.value.setEditable(!exists);
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
            ((AddProductPopupController_Page2) View.popupWindow("Add new Product (2 of 2)", Constants.FXMLs.addProductPage2, 1300, 600)).initialize(name, brand, productId);
        }

        private void initialize(String name, String brand, String productId) {
            this.name = name;
            this.brand = brand;
            this.productId = productId;
            this.exists = productId != null;

            if (exists) {
                try {
                    info = mainController.digest(productId);
                } catch (Exceptions.InvalidSellableIdException e) {
                    e.printStackTrace();
                    printError(e.getMessage());
                }
            }

            initAccessControls();
            initListeners();
            initChoiceBox();
            initValues();
            initActions();
            initTable();
        }

        private void initTable() {
            propertyCOL.setCellValueFactory(new PropertyValueFactory<>("property"));
            valueCOL.setCellValueFactory(new PropertyValueFactory<>("value"));

            if (exists)
                initItems();
        }

        private void initItems() {
            try {
                HashMap<String, String> values = mainController.getPropertyValuesOfAProduct(info[0]);
                for (String s : values.keySet()) {
                    properties.getItems().add(new PropertyWrapper(s, values.get(s)));
                }
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }
        }

        private void initAccessControls() {
            if (exists) {
                category.setDisable(true);
                browseBTN.setDisable(true);
                infoArea.setEditable(false);
            }
        }

        private void initChoiceBox() {
            category.getItems().addAll(sellerController.getAllCategories().stream().map(c -> c[0]).collect(Collectors.toCollection(ArrayList::new)));
        }

        private void initValues() {
            nameField.setText(name);
            brandField.setText(brand);
            if (exists) {
                infoArea.setText(info[3]);
                category.getSelectionModel().select(info[7]);
            }

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
                    HashMap<String, String> propertyMap = new HashMap<>();
                    for (PropertyWrapper item : properties.getItems()) {
                        propertyMap.put(item.property, item.value.getText());
                    }
                    try {
                        if (!exists) {
                            byte[] image = new byte[0];
                            try {
                                image = Files.readAllBytes(Paths.get(imageField.getText()));
                            } catch (IOException ex) {
                                //nothing
                            }
                            sellerController.addNewProduct(nameField.getText(), brandField.getText(), infoArea.getText(), image, category.getValue(),
                                    propertyMap, Double.parseDouble(priceField.getText()), Integer.parseInt(countField.getText()));
                        } else
                            sellerController.addNewSubProductToAnExistingProduct(productId, Double.parseDouble(priceField.getText()), Integer.parseInt(countField.getText()));
                    } catch (Exceptions.InvalidSellableIdException | Exceptions.InvalidCategoryException | Exceptions.ExistingProductException ex) {
                        ex.printStackTrace();
                    }
                    addProductBTN.getScene().getWindow().hide();
                }
            });
        }

        private boolean validateFields() {
            if (category.getValue() == null) {
                printError("Please choose a category");
                return false;
            }
            if (countField.getText().equals("")) {
                printError("Please enter the number of available items");
                return false;
            }
            if (!priceField.getText().matches(Constants.doublePattern)) {
                printError("Invalid price! Please enter a double number");
                return false;
            }
            return true;
        }

        private void updateProperties(String categoryName) {
            try {
                if (!exists) {
                    properties.getItems().clear();
                    for (String category : mainController.getPropertiesOfCategory(categoryName, true)) {
                        properties.getItems().add(new PropertyWrapper(category, null));
                    }
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

    public static class RatingBoxController {

        @FXML
        private HBox ratingBox;
        @FXML
        private StackPane star1;
        @FXML
        private ImageView fullStar1;
        @FXML
        private StackPane star2;
        @FXML
        private ImageView fullStar2;
        @FXML
        private StackPane star3;
        @FXML
        private ImageView fullStar3;
        @FXML
        private StackPane star4;
        @FXML
        private ImageView fullStar4;
        @FXML
        private StackPane star5;
        @FXML
        private ImageView fullStar5;

        private String productId;

        public static Parent createBox(String productId) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.ratingBox + ".fxml"));
            try {
                Parent p = loader.load();
                RatingBoxController controller = loader.getController();
                controller.initialize(productId);
                ((HBox) p).setAlignment(Pos.CENTER);
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void initialize(String productId) {
            this.productId = productId;
            initActions();
        }

        private void initActions() {
            star1.setOnMouseClicked(e -> {
                try {
                    customerController.rateProduct(productId, 1);
                    colorStars(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            star2.setOnMouseClicked(e -> {
                try {
                    customerController.rateProduct(productId, 2);
                    colorStars(2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            star3.setOnMouseClicked(e -> {
                try {
                    customerController.rateProduct(productId, 3);
                    colorStars(3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            star4.setOnMouseClicked(e -> {
                try {
                    customerController.rateProduct(productId, 4);
                    colorStars(4);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            star5.setOnMouseClicked(e -> {
                try {
                    customerController.rateProduct(productId, 5);
                    colorStars(5);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        private void colorStars(int num) {
            switch (num) {
                case 1:
                    fullStar2.setVisible(false);
                case 2:
                    fullStar3.setVisible(false);
                case 3:
                    fullStar4.setVisible(false);
                case 4:
                    fullStar5.setVisible(false);
            }

            switch (num) {
                case 5:
                    fullStar5.setVisible(true);
                case 4:
                    fullStar4.setVisible(true);
                case 3:
                    fullStar3.setVisible(true);
                case 2:
                    fullStar2.setVisible(true);
                case 1:
                    fullStar1.setVisible(true);
            }
        }
    }

    public static class CompareMenuController {

        @FXML
        private GridPane productProperties;

        @FXML
        private Label brand1;

        @FXML
        private Label brand2;

        @FXML
        private Label category1;

        @FXML
        private Label category2;

        @FXML
        private Label rating1;

        @FXML
        private Label rating2;

        @FXML
        private ImageView image1;

        @FXML
        private ImageView image2;

        @FXML
        private Label maxPrice2;

        @FXML
        private Label minPrice2;

        @FXML
        private Label maxPrice1;

        @FXML
        private Label minPrice1;

        @FXML
        private Label name2;

        @FXML
        private Label name1;

        private String[] firstProductInfo;
        private String[] secondProductInfo;

        public static void display(String firstProductId, String secondProductId) {
            ((CompareMenuController) View.popupWindow("Compare menu", Constants.FXMLs.compareMenu, 520, 620)).initialize(firstProductId, secondProductId);
        }

        private void initialize(String firstProductId, String secondProductId) {
            try {
                firstProductInfo = mainController.digest(firstProductId);
                secondProductInfo = mainController.digest(secondProductId);
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }

            initValues();
            initProperties();
        }

        private void initValues() {
            name1.setText(firstProductInfo[1]);
            name2.setText(secondProductInfo[1]);
            brand1.setText(firstProductInfo[2]);
            brand2.setText(secondProductInfo[2]);
            category1.setText(firstProductInfo[7]);
            category2.setText(secondProductInfo[7]);
            rating1.setText(firstProductInfo[4]);
            rating2.setText(secondProductInfo[4]);
            minPrice1.setText(firstProductInfo[9]);
            minPrice2.setText(secondProductInfo[9]);
            maxPrice1.setText(firstProductInfo[10]);
            maxPrice2.setText(secondProductInfo[10]);

            String firstPath = "src/main/resources/temp/compareFirstImage.png";
            File f1 = Path.of(firstPath).toFile();
            if (!f1.exists()) {
                try {
                    f1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] byteArray1 = mainController.loadFileFromDataBase(firstProductInfo[8]);
            try (FileOutputStream stream = new FileOutputStream(firstPath)) {
                stream.write(byteArray1);
                image1.setImage(new Image("file:" + firstPath));
            } catch (IOException e) {
                e.printStackTrace();
            }


            String secondPath = "src/main/resources/temp/compareSecondImage.png";
            File f2 = Path.of(secondPath).toFile();
            if (!f2.exists()) {
                try {
                    f2.createNewFile();
                    image2.setImage(new Image("file:" + secondPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] byteArray2 = mainController.loadFileFromDataBase(firstProductInfo[8]);
            try (FileOutputStream stream = new FileOutputStream(secondPath)) {
                stream.write(byteArray2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void initProperties() {
            try {
                //ArrayList<String> properties = mainController.getPropertiesOfCategory(firstProductInfo[7], true);
                HashMap<String, String> firstValues = mainController.getPropertyValuesOfAProduct(firstProductInfo[0]);
                HashMap<String, String> secondValues = mainController.getPropertyValuesOfAProduct(secondProductInfo[0]);
                int index = 0;
                for (String property : firstValues.keySet()) {
                    var firstWrapper = new Label(firstValues.get(property).equals("") ? "-" : firstValues.get(property));
                    firstWrapper.getStyleClass().add("value-label");
                    var secondWrapper = new Label(secondValues.get(property).equals("") ? "-" : secondValues.get(property));
                    secondWrapper.getStyleClass().add("value-label");
                    var propertyWrapper = new Label(property);
                    propertyWrapper.getStyleClass().add("property-label");
                    productProperties.addRow(7 + index++, firstWrapper, propertyWrapper, secondWrapper);
                }
            } catch (Exceptions.InvalidSellableIdException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SupporterChatMenuController implements Initializable {
        @FXML
        private AnchorPane chatPane;
        @FXML
        private TableView<ChatWrapper> activeChats;
        @FXML
        private TableColumn<ChatWrapper, Hyperlink> activeCustomers;
        @FXML
        private TableColumn<ChatWrapper, Button> activeRemove;
        @FXML
        private TableView<ChatWrapper> archiveChats;
        @FXML
        private TableColumn<ChatWrapper, Hyperlink> archiveCustomers;

        private String currentChat;

        public class ChatWrapper {
            String chatId;
            Hyperlink customer = new Hyperlink();
            Button remove = new Button();
            boolean archived;

            public ChatWrapper(String[] pack, boolean archived) {
                this(pack[0], pack[1]);
                this.archived = archived;
            }

            public ChatWrapper(String chatId, String username) {
                this.chatId = chatId;
                this.customer.setText(username);
                this.remove.getStyleClass().add("remove-button");
                customer.setOnAction(e -> {
                    if (currentChat != null && !customer.getText().equals(currentChat)) {
                        currentChat = customer.getText();
                        chatPane.getChildren().clear();
                        chatPane.getChildren().add(ChatPageController.getChatPage(chatId));
                    } else if (currentChat == null) {
                        currentChat = customer.getText();
                        chatPane.getChildren().add(ChatPageController.getChatPage(chatId));
                    }
                });

                remove.setOnAction(e -> {
                    try {
                        supporterController.deleteChat(chatId);
                        archiveChats.getItems().add(this);
                        activeChats.getItems().remove(this);
                    } catch (Exceptions.InvalidChatIdException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            public String getChatId() {
                return chatId;
            }

            public Hyperlink getCustomer() {
                return customer;
            }

            public Button getRemove() {
                return remove;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.supporterChatMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initCols();
            initItems();
        }

        private void initCols() {
            activeCustomers.setCellValueFactory(new PropertyValueFactory<>("customer"));
            archiveCustomers.setCellValueFactory(new PropertyValueFactory<>("customer"));
            activeRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));
        }

        private void initItems() {
            try {
                supporterController.getActiveChats().forEach(ch -> activeChats.getItems().add(new ChatWrapper(ch, false)));
                supporterController.getArchiveChats().forEach(ch -> archiveChats.getItems().add(new ChatWrapper(ch, true)));
            } catch (Exceptions.UnAuthorizedAccountException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused")
    public static class SellerAuctionMangingMenuController implements Initializable {
        @FXML
        private TableView<AuctionWrapper> auctions;
        @FXML
        private TableColumn<AuctionWrapper, String> idCol;
        @FXML
        private TableColumn<AuctionWrapper, Double> highestBidCOL;
        @FXML
        private TableColumn<AuctionWrapper, String> startDateCOL;
        @FXML
        private TableColumn<AuctionWrapper, String> endDateCOL;
        @FXML
        private TableColumn<AuctionWrapper, Button> detailsCOL;
        @FXML
        private TableColumn<AuctionWrapper, Button> removeCOL;
        @FXML
        private TableView<AuctionWrapper> archive;
        @FXML
        private TableColumn<AuctionWrapper, String> archiveIdCol;
        @FXML
        private TableColumn<AuctionWrapper, Double> archiveHighestBidCOL;
        @FXML
        private TableColumn<AuctionWrapper, String> archiveStartDateCOL;
        @FXML
        private TableColumn<AuctionWrapper, String> archiveEndDateCOL;
        @FXML
        private TableColumn<AuctionWrapper, Button> archiveDetailsCOL;
        @FXML
        private Label errorLBL;
        @FXML
        private Button addAuctionBTN;

        public class AuctionWrapper {
            String id, startDate, endDate;
            double highestBid;
            Button detailsBTN, removeBTN;

            public AuctionWrapper(String[] pack) {
                this(pack[0], pack[3], pack[4], Double.parseDouble(pack[5]));
            }

            public AuctionWrapper(String id, String startDate, String endDate, double highestBid) {
                this.id = id;
                this.startDate = startDate;
                this.endDate = endDate;
                this.highestBid = highestBid;

                detailsBTN = new Button();
                removeBTN = new Button();
                detailsBTN.getStyleClass().add("details-button");
                removeBTN.getStyleClass().add("remove-button");

                removeBTN.setOnAction(e -> {
                    try {
                        sellerController.removeAuction(id);
                        archive.getItems().add(this);
                        auctions.getItems().remove(this);
                    } catch (Exceptions.InvalidAuctionIdException ex) {
                        ex.printStackTrace();
                    }
                });

                detailsBTN.setOnAction(e -> SellerAuctionMangingPopupController.display(id));
            }

            public String getId() {
                return id;
            }

            public String getStartDate() {
                return startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public double getHighestBid() {
                return highestBid;
            }

            public Button getDetailsBTN() {
                return detailsBTN;
            }

            public Button getRemoveBTN() {
                return removeBTN;
            }
        }

        public static void display() {
            View.setMainPane(Constants.FXMLs.sellerAuctionManagingMenu);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initTable();
            initItems();
            initActions();
        }

        private void initTable() {
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            highestBidCOL.setCellValueFactory(new PropertyValueFactory<>("highestBid"));
            startDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            detailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailsBTN"));
            removeCOL.setCellValueFactory(new PropertyValueFactory<>("removeBTN"));

            archiveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            archiveHighestBidCOL.setCellValueFactory(new PropertyValueFactory<>("highestBid"));
            archiveStartDateCOL.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            archiveEndDateCOL.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            archiveDetailsCOL.setCellValueFactory(new PropertyValueFactory<>("detailsBTN"));
        }

        private void initItems() {
            sellerController.viewActiveAuctions().forEach(auction -> auctions.getItems().add(new AuctionWrapper(auction)));
            sellerController.viewArchiveAuctions().forEach(auction -> archive.getItems().add(new AuctionWrapper(auction)));
        }

        private void initActions() {
            addAuctionBTN.setOnAction(e -> SellerAuctionMangingPopupController.display(null));
        }
    }

    public static class SellerAuctionMangingPopupController {
        @FXML
        private Label idKeyLBL;
        @FXML
        private Label idValueLBL;
        @FXML
        private DatePicker startDate;
        @FXML
        private DatePicker endDate;
        @FXML
        private Label customerLBL;
        @FXML
        private Label highestBidLBL;
        @FXML
        private ChoiceBox<String> subSellable;
        @FXML
        private Button addBTN;
        @FXML
        private HBox editHB;
        @FXML
        private Button editBTN;
        @FXML
        private Button discardBTN;
        @FXML
        private Label errorLBL;
        @FXML
        private Label highestBidKey;
        @FXML
        private Label highestBidderKey;

        private String auctionId;
        private String[] info;
        private boolean exists;
        private final HashMap<String, String> nameToId = new HashMap<>();

        private final SimpleBooleanProperty startDateChanged = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty endDateChanged = new SimpleBooleanProperty(false);

        public static void display(String auctionId) {
            ((SellerAuctionMangingPopupController) View.popupWindow("seller auction managing popup", Constants.FXMLs.sellerAuctionManagingPopup, 600, 500)).initialize(auctionId);
        }

        private void initialize(String auctionId) {
            this.auctionId = auctionId;
            exists = auctionId != null;
            if (exists) {
                try {
                    info = sellerController.viewAuctionWithId(auctionId);
                } catch (Exceptions.InvalidAuctionIdException e) {
                    e.printStackTrace();
                }
            }

            initChoiceBox();
            initActions();
            initValues();
            initVisibilities();
            initBindings();
        }

        private void initActions() {
            editBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        if (startDateChanged.get())
                            sellerController.editAuction(auctionId, "start date", startDate.getValue().toString());
                        if (endDateChanged.get())
                            sellerController.editAuction(auctionId, "end date", endDate.getValue().toString());
                        editBTN.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidDateException | Exceptions.SameAsPreviousValueException |
                            Exceptions.InvalidAuctionIdException | Exceptions.InvalidFormatException |
                            Exceptions.InvalidFieldException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            addBTN.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        sellerController.addAuction(startDate.getValue().toString(), endDate.getValue().toString(), nameToId.get(subSellable.getSelectionModel().getSelectedItem()));
                        addBTN.getScene().getWindow().hide();
                    } catch (Exceptions.InvalidFormatException | Exceptions.InvalidDateException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            discardBTN.setOnAction(e -> discardBTN.getScene().getWindow().hide());
        }

        private void initValues() {
            if (exists) {
                idValueLBL.setText(info[0]);
                customerLBL.setText(info[6]);
                highestBidLBL.setText(info[5] + "$");
                startDate.setValue(LocalDate.parse("20" + info[3]));
                endDate.setValue(LocalDate.parse("20" + info[4]));
            }
        }

        private void initVisibilities() {
            editHB.setVisible(exists);
            idKeyLBL.setVisible(exists);
            idValueLBL.setVisible(exists);
            highestBidLBL.setVisible(exists);
            highestBidKey.setVisible(exists);
            highestBidderKey.setVisible(exists);
            customerLBL.setVisible(exists);
            addBTN.setVisible(!editHB.isVisible());
        }

        private void initBindings() {
            if (exists) {
                startDateChanged.bind(
                        Bindings.when(startDate.valueProperty().isEqualTo(LocalDate.parse("20" + info[3]))).then(false).otherwise(true)
                );
                endDateChanged.bind(
                        Bindings.when(endDate.valueProperty().isEqualTo(LocalDate.parse("20" + info[4]))).then(false).otherwise(true)
                );
                editBTN.disableProperty().bind(startDateChanged.or(endDateChanged).not());
            }
        }

        private boolean validateFields() {
            if (subSellable.getSelectionModel().getSelectedItem() == null) {
                errorLBL.setText("choose a product");
                return false;
            }
            if (startDate.getValue() == null) {
                errorLBL.setText("choose a start date");
                return false;
            }
            if (endDate.getValue() == null) {
                errorLBL.setText("choose an end date");
                return false;
            }

            return true;
        }

        private void initChoiceBox() {
            sellerController.manageProducts().forEach(p -> {
                nameToId.put(p[2], p[1]);
                subSellable.getItems().add(p[2]);
            });

            if (exists) {
                try {
                    subSellable.getSelectionModel().select(mainController.getSubProductByID(info[2])[2]);
                } catch (Exceptions.InvalidSubProductIdException e) {
                    e.printStackTrace();
                }
                subSellable.setDisable(true);
            }
        }
    }

    public static class SupporterRegistrationPopupController implements Initializable {
        @FXML
        private TextField supporterUsername;
        @FXML
        private Label supporterUsernameError;
        @FXML
        private PasswordField supporterPassword;
        @FXML
        private TextField supporterShowPasswordField;
        @FXML
        private ToggleButton supporterShowPasswordBTN;
        @FXML
        private Label supporterPasswordError;
        @FXML
        private TextField supporterImageField;
        @FXML
        private Button supporterBrowseBTN;
        @FXML
        private Label supporterImageError;
        @FXML
        private TextField supporterFirstName;
        @FXML
        private Label supporterFirstNameError;
        @FXML
        private TextField supporterLastName;
        @FXML
        private Label supporterLastNameError;
        @FXML
        private TextField supporterPhoneNumber;
        @FXML
        private Label supporterPhoneNumberError;
        @FXML
        private TextField supporterEmail;
        @FXML
        private Label supporterEmailError;
        @FXML
        private Button supporterRegister;

        public static void display() {
            View.popupWindow("Supporter registration popup", Constants.FXMLs.supporterRegistrationPopup, 1000, 700);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            supporterImageField.setEditable(false);
            initBindings();
            initActions();
            initLBLs();
            initVisibilities();
            initListeners();
        }

        private void initBindings() {
            supporterShowPasswordField.textProperty().bindBidirectional(supporterPassword.textProperty());
            supporterShowPasswordField.visibleProperty().bind(supporterPassword.visibleProperty().not());
            supporterPassword.visibleProperty().bind(supporterShowPasswordBTN.selectedProperty().not());
        }


        private void initActions() {
            supporterBrowseBTN.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
                File chosenFile = fileChooser.showOpenDialog(new Stage());
                if (chosenFile != null) {
                    supporterImageField.setText(chosenFile.getPath());
                }
            });

            supporterRegister.setOnAction(e -> {
                if (validateFields()) {
                    try {
                        byte[] image = new byte[0];
                        try {
                            image = Files.readAllBytes(Paths.get(supporterImageField.getText()));
                        } catch (IOException ex) {
                            //nothing
                        }
                        adminController.createSupporterProfile(supporterUsername.getText(), supporterPassword.getText(), supporterFirstName.getText(),
                                supporterLastName.getText(), supporterEmail.getText(), supporterPhoneNumber.getText(), image);
                        AdminAccountManagingMenuController.current.addSupporter(supporterUsername.getText());
                        supporterUsername.getScene().getWindow().hide();
                    } catch (Exceptions.UsernameAlreadyTakenException ex) {
                        supporterUsernameError.setText("Sorry! this username is already taken.");
                        supporterUsernameError.setVisible(true);
                        ex.printStackTrace();
                    }
                }
            });
        }

        private boolean validateFields() {
            boolean valid = true;
            if (supporterUsername.getText().equals("")) {
                supporterUsernameError.setText("You can only use alphabet, digits and \"_\"");
                supporterUsernameError.setVisible(true);
                valid = false;
            } else supporterUsernameError.setVisible(false);

            if (supporterPassword.getText().equals("")) {
                supporterPasswordError.setVisible(true);
                valid = false;
            } else supporterPasswordError.setVisible(false);

            if (!supporterFirstName.getText().matches(Constants.IRLNamePattern)) {
                supporterFirstNameError.setVisible(true);
                valid = false;
            } else supporterFirstNameError.setVisible(false);

            if (!supporterLastName.getText().matches(Constants.IRLNamePattern)) {
                supporterLastNameError.setVisible(true);
                valid = false;
            } else supporterLastNameError.setVisible(false);

            if (supporterPhoneNumber.getText().equals("")) {
                supporterPhoneNumberError.setVisible(true);
                valid = false;
            } else supporterPhoneNumberError.setVisible(false);

            if (!supporterEmail.getText().matches(Constants.emailPattern)) {
                supporterEmailError.setVisible(true);
                valid = false;
            } else supporterEmailError.setVisible(false);

            return valid;
        }

        private void initLBLs() {
            supporterPasswordError.setText("You can only use alphabet, digits and \"_\"");
            supporterFirstNameError.setText("Invalid first name!");
            supporterLastNameError.setText("Invalid last name");
            supporterPhoneNumberError.setText("Enter a phone number!");
            supporterEmailError.setText("Invalid email address!");
        }

        private void initVisibilities() {
            supporterUsernameError.setVisible(false);
            supporterPasswordError.setVisible(false);
            supporterFirstNameError.setVisible(false);
            supporterLastNameError.setVisible(false);
            supporterPhoneNumberError.setVisible(false);
            supporterEmailError.setVisible(false);
            supporterImageError.setVisible(false);
        }

        private void initListeners() {
            View.addListener(supporterUsername, "\\w");
            View.addListener(supporterPassword, "\\w");
            View.addListener(supporterPhoneNumber, "[0-9]");
        }
    }

    public static class ChatPageController {

        @FXML
        private VBox messages;

        @FXML
        private Button refreshBTN;

        @FXML
        private TextField messageField;

        @FXML
        private Button sendBTN;


        private String chatPageId;
        private int lastMessageNumber = 0;

        public static Parent getChatPage(String chatPageId) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.chatPage + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                ChatPageController cpc = loader.getController();
                cpc.chatPageId = chatPageId;
                cpc.updateMessages();
                cpc.setOnActions();
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void updateMessages() {
            try {
                ArrayList<String[]> messagePacks = mainController.getMessagesInChat(chatPageId);
                int numberOfMessages = messagePacks.size();
                for (int i = lastMessageNumber; i < numberOfMessages; i++) {
                    messages.getChildren().add(MessageBoxController.createMessageBox(messagePacks.get(i)));
                }
                lastMessageNumber = numberOfMessages;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void setOnActions() {
            refreshBTN.setOnAction(e -> updateMessages());
            sendBTN.setOnAction((e -> sendMessage()));
        }

        private void sendMessage() {
            String text = messageField.getText();
            if (!text.isEmpty()) {
                try {
                    mainController.sendMessage(chatPageId, text);
                    messageField.setText("");
                    updateMessages();
                } catch (Exceptions.InvalidChatIdException | Exceptions.InvalidAccountTypeException | Exceptions.UnAuthorizedAccountException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static class MessageBoxController {
        @FXML
        private Label sender;

        @FXML
        private Text message;

        @FXML
        private Label date;


        public static Parent createMessageBox(String[] messagePack) {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("/fxml/" + Constants.FXMLs.messageBox + ".fxml"));
            Parent p;
            try {
                p = loader.load();
                MessageBoxController mbc = loader.getController();
                mbc.setInfo(messagePack);
                return p;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void setInfo(String[] messagePack) {
            message.setText(messagePack[3]);
            date.setText(messagePack[2]);
            sender.setText(messagePack[1]);
        }

    }

    public static class AuctionPopupController {
        @FXML
        private Label customerLBL;
        @FXML
        private Label highestBidLBL;
        @FXML
        private Button refreshBTN;
        @FXML
        private TextField bidField;
        @FXML
        private Button bidBTN;
        @FXML
        private AnchorPane chatPane;
        @FXML
        private Label errorLBL;

        private String[] info;
        private String auctionId;

        public static void display(String auctionId) {
            ((AuctionPopupController) View.popupWindow("Auction page", Constants.FXMLs.auctionPopup, 900, 500)).initialize(auctionId);
        }

        private void initialize(String auctionId) {
            this.auctionId = auctionId;
            updateValues();
            initChat();
            initActions();
        }

        private void updateValues() {
            try {
                info = sellerController.viewAuctionWithId(auctionId);
                highestBidLBL.setText(info[5] + "$");
                customerLBL.setText(info[6]);
            } catch (Exceptions.InvalidAuctionIdException e) {
                e.printStackTrace();
            }
        }

        private void initChat() {
            chatPane.getChildren().add(ChatPageController.getChatPage(info[7]));
        }

        private void initActions() {
            refreshBTN.setOnAction(e -> updateValues());
            bidBTN.setOnAction(e -> {
                if (bidField.getText().matches(Constants.doublePattern)) {
                    try {
                        customerController.bid(auctionId, Double.parseDouble(bidField.getText()));
                        errorLBL.setText("");
                        bidField.setText("");
                    } catch (Exceptions.InvalidAuctionIdException ex) {
                        ex.printStackTrace();
                    }
                } else errorLBL.setText("please enter a valid double");
            });
        }
    }

    public static class CustomerChatPopupController implements Initializable {
        @FXML
        private AnchorPane chatPane;
        @FXML
        private ChoiceBox<String> supporterBox;
        @FXML
        private Button createChatBTN;

        private final HashMap<String, String> nameToId = new HashMap<>();
        private boolean hasChat;
        private String chatId;
        private String[] chatInfo;

        public static void display() {
            View.popupWindow("Customer Chat menu", Constants.FXMLs.customerChatPopup, 600, 500);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            initChat();
            initChoiceBox();
            initButton();
        }

        private void initAll() {
            initChat();
            initChoiceBox();
            initButton();
        }

        private void initChat() {
            try {
                chatId = customerController.getSupportChatId();
                chatInfo = supporterController.viewChatById(chatId);
                chatPane.getChildren().add(ChatPageController.getChatPage(chatId));
            } catch (Exceptions.DontHaveChatException | Exceptions.InvalidChatIdException e) {
                e.printStackTrace();
            }

            hasChat = chatId != null;
        }

        private void initChoiceBox() {
            supporterBox.getItems().clear();
            customerController.getAllSupporters().forEach(s -> {
                nameToId.put(s[1], s[0]);
                supporterBox.getItems().add(s[1]);
            });
            if (hasChat) {
                createChatBTN.setDisable(true);
                supporterBox.getSelectionModel().select(chatInfo[2]);
                supporterBox.setDisable(true);
            } else {
                createChatBTN.disableProperty().unbind();
                createChatBTN.disableProperty().bind(supporterBox.getSelectionModel().selectedItemProperty().isNull());
            }
        }

        private void initButton() {
            createChatBTN.setOnAction(e -> {
                try {
                    customerController.createSupportChat(nameToId.get(supporterBox.getSelectionModel().getSelectedItem()));
                    initAll();
                } catch (Exceptions.AlreadyInAChatException | Exceptions.InvalidSupporterIdException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }
}