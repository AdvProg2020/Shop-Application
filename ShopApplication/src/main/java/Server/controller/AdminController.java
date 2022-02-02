package Server.controller;

import Server.model.Category;
import Server.model.Discount;
import Server.model.Wallet;
import Server.model.account.Account;
import Server.model.account.Admin;
import Server.model.account.Customer;
import Server.model.account.Supporter;
import Server.model.database.Database;
import Server.model.log.BuyLog;
import Server.model.log.FileLog;
import Server.model.log.ShippingStatus;
import Server.model.request.*;
import Server.model.sellable.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminController {

    private static final DateFormat dateFormat = Utilities.getDateFormat();
    private final Controller mainController;

    public AdminController(Controller controller) {
        mainController = controller;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Database database() {
        return mainController.getDatabase();
    }


    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        mainController.editPersonalInfo(field, newInformation);
        database().editAccount();
    }

    public ArrayList<String[]> manageUsers() {
        ArrayList<String[]> accounts = new ArrayList<>();
        for (Account account : Account.getAllAccounts()) {
            if (account != currentAccount()) {
                String[] IdUsername = new String[7];
                IdUsername[0] = account.getId();
                IdUsername[1] = account.getUsername();
                IdUsername[2] = account.getFirstName();
                IdUsername[3] = account.getLastName();
                IdUsername[4] = account.getPhone();
                IdUsername[5] = account.getEmail();
                IdUsername[6] = account.getClass().getSimpleName();
                accounts.add(IdUsername);
            }
        }
        return accounts;
    }

    public String[] viewUsername(String username) throws Exceptions.UsernameDoesntExistException {
        Account account = Account.getAccountByUsername(username);
        if (account == null) throw new Exceptions.UsernameDoesntExistException(username);

        return Utilities.Pack.personalInfo(account);
    }

    public void deleteUsername(String username) throws Exceptions.UsernameDoesntExistException, Exceptions.ManagerDeleteException {
        Account account = Account.getAccountByUsername(username);
        if (account == null) throw new Exceptions.UsernameDoesntExistException(username);
        if (account == Admin.getManager() || account == currentAccount()) throw new Exceptions.ManagerDeleteException();

        account.suspend();

        switch (account.getClass().getSimpleName()) {
            case "Admin":
                database().removeAdmin();
                break;
            case "Customer":
                database().removeCustomer();
                break;
            case "Seller":
                database().removeSeller();
                break;
            case "Supporter":
                database().removeSupporter();
                break;
        }
    }


    public void createAdminProfile(String username, String password, String firstName, String lastName, String email, String phone, byte[] image) throws Exceptions.UsernameAlreadyTakenException {
        if (Account.isUsernameUsed(username)) throw new Exceptions.UsernameAlreadyTakenException(username);

        String imagePath = image.length != 0 ? mainController.saveFileInDataBase(image, "accountImg", username + ".png") : null;
        new Admin(username, password, firstName, lastName, email, phone, imagePath);
        database().createAdmin();
    }

    public void createSupporterProfile(String username, String password, String firstName, String lastName, String email, String phone, byte[] image) throws Exceptions.UsernameAlreadyTakenException {
        if (Account.isUsernameUsed(username)) throw new Exceptions.UsernameAlreadyTakenException(username);

        String imagePath = image.length != 0 ? mainController.saveFileInDataBase(image, "accountImg", username + ".png") : null;
        new Supporter(username, password, firstName, lastName, email, phone, imagePath);
        database().createSupporter();
    }

    public ArrayList<String[]> manageAllProducts() {
        ArrayList<String[]> productPacks = new ArrayList<>();
        for (Product product : Product.getAllProducts()) {
            productPacks.add(Utilities.Pack.product(product));
        }

        return productPacks;
    }

    public ArrayList<String[]> manageAllFiles() {
        ArrayList<String[]> filePacks = new ArrayList<>();
        for (File file : File.getAllFiles()) {
            filePacks.add(Utilities.Pack.file(file));
        }

        return filePacks;
    }

    public void removeSellable(String sellableId) throws Exceptions.InvalidSellableIdException {
        Sellable sellable = Sellable.getSellableById(sellableId);
        if (sellable == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        sellable.suspend();
        switch (sellable.getClass().getSimpleName()) {
            case "Product":
                database().removeProduct();
                break;
            case "File":
                database().removeFile();
                break;
        }
    }

    public void createDiscountCode(String discountCode, String startDate, String endDate, double percentage,
                                   double maximumAmount, ArrayList<String[]> customersIdCount)
            throws Exceptions.ExistingDiscountCodeException, Exceptions.InvalidAccountsForDiscount, Exceptions.InvalidFormatException {

        if (Discount.getDiscountByCode(discountCode) != null)
            throw new Exceptions.ExistingDiscountCodeException(discountCode);

        ArrayList<String> wrongIds = new ArrayList<>();
        for (String[] Id : new ArrayList<>(customersIdCount)) {
            Account account = Account.getAccountById(Id[0]);
            if (!(account instanceof Customer)) {
                wrongIds.add(Id[0]);
                customersIdCount.remove(Id);
            }
        }
        try {
            Discount discount = new Discount(discountCode, dateFormat.parse(startDate), dateFormat.parse(endDate), percentage, maximumAmount);
            for (String[] IdCount : customersIdCount) {
                discount.addCustomer(IdCount[0], Integer.parseInt(IdCount[1]));
            }
            database().createDiscount();
            if (wrongIds.size() > 0)
                throw new Exceptions.InvalidAccountsForDiscount(Utilities.Pack.invalidAccountIds(wrongIds));
        } catch (ParseException e) {
            throw new Exceptions.InvalidFormatException("date");
        }
    }


    public ArrayList<String> viewActiveDiscountCodes() {
        ArrayList<String> discountCodes = new ArrayList<>();
        for (Discount discount : Discount.getActiveDiscounts()) {
            discountCodes.add(discount.getDiscountCode());
        }

        return discountCodes;
    }

    public ArrayList<String> viewArchiveDiscountCodes() {
        ArrayList<String> discountCodes = new ArrayList<>();
        for (Discount discount : Discount.getDiscountArchive()) {
            discountCodes.add(discount.getDiscountCode());
        }

        return discountCodes;
    }

    public String[] viewDiscountCodeByCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null) throw new Exceptions.DiscountCodeException(code);

        return Utilities.Pack.discountInfo(discount);
    }

    public String[] viewDiscountCodeById(String discountId) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountById(discountId, false);
        if (discount == null) throw new Exceptions.DiscountCodeException(discountId);

        return Utilities.Pack.discountInfo(discount);
    }

    public ArrayList<String[]> peopleWhoHaveThisDiscount(String id) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountById(id, false);
        if (discount == null) throw new Exceptions.DiscountCodeException(id);

        ArrayList<String[]> peopleWithThisCode = new ArrayList<>();
        for (Map.Entry<Customer, Integer> entry : discount.getCustomers().entrySet()) {
            peopleWithThisCode.add(Utilities.Pack.customerDiscountRemainingCount(entry.getKey(), entry.getValue()));
        }
        return peopleWithThisCode;
    }

    /**
     * @param code           String
     * @param field          String ->                     "start date", "end date", "maximum amount", "percentage"
     * @param newInformation String: should match  dateFormat , dateFormat,  Double         ,  Double
     * @throws Exceptions.DiscountCodeException
     * @throws Exceptions.SameAsPreviousValueException
     * @throws Exceptions.InvalidFormatException
     */
    public void editDiscountCode(String code, String field, String newInformation) throws Exceptions.DiscountCodeException, Exceptions.SameAsPreviousValueException, Exceptions.InvalidFormatException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null) throw new Exceptions.DiscountCodeException(code);

        switch (field) {
            case "start date":
                try {
                    if (dateFormat.parse(newInformation).equals(discount.getStartDate()))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    discount.setStartDate(dateFormat.parse(newInformation));
                } catch (ParseException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            case "end date":
                try {
                    if (dateFormat.parse(newInformation).equals(discount.getEndDate()))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    discount.setEndDate(dateFormat.parse(newInformation));
                } catch (ParseException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            case "maximum amount":
                if (Double.parseDouble(newInformation) == discount.getMaximumAmount())
                    throw new Exceptions.SameAsPreviousValueException(field);
                else
                    discount.setMaximumAmount(Double.parseDouble(newInformation));
                break;
            case "percentage":
                if (Double.parseDouble(newInformation) == discount.getPercentage())
                    throw new Exceptions.SameAsPreviousValueException(field);
                else
                    discount.setPercentage(Double.parseDouble(newInformation));
                break;
        }
        database().editDiscount();
    }


    public void removeDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null) throw new Exceptions.DiscountCodeException(code);

        discount.suspend();
        database().removeDiscount();
    }

    public ArrayList<String[]> getArchivedRequests() {
        ArrayList<String[]> requestIds = new ArrayList<>();
        for (Request request : Request.getRequestArchive()) {
            requestIds.add(Utilities.Pack.request(request));
        }

        return requestIds;
    }

    public ArrayList<String[]> getPendingRequests() {
        ArrayList<String[]> requestPacks = new ArrayList<>();
        for (Request request : Request.getPendingRequests()) {
            requestPacks.add(Utilities.Pack.request(request));
        }

        return requestPacks;
    }

    public ArrayList<String[]> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId, false);
        if (request == null) throw new Exceptions.InvalidRequestIdException(requestId);

        ArrayList<String[]> detailsOfRequest = new ArrayList<>();
        detailsOfRequest.add(Utilities.Pack.request(request));
        switch (Utilities.Pack.request(request)[1]) {
            case "AddProductRequest":
                detailsOfRequest.add(Utilities.Pack.addProductRequest(((AddProductRequest) request).getSubProduct(), ((AddProductRequest) request).getProduct()));
                break;
            case "AddReviewRequest":
                detailsOfRequest.add(Utilities.Pack.getReviewInfo(((AddReviewRequest) request).getReview()));
                break;
            case "AddSaleRequest":
                detailsOfRequest.add(Utilities.Pack.newSaleInRequest(((AddSaleRequest) request).getSale()));
                break;
            case "AddSellerRequest":
                detailsOfRequest.add(Utilities.Pack.sellerInRequest(((AddSellerRequest) request).getSeller()));
                break;
            case "EditProductRequest":
                detailsOfRequest.add(Utilities.Pack.subProduct(((EditProductRequest) request).getSubProduct()));
                detailsOfRequest.add(Utilities.Pack.productChange(((EditProductRequest) request)));
                break;
            case "EditSaleRequest":
                detailsOfRequest.add(Utilities.Pack.saleInfo(((EditSaleRequest) request).getSale()));
                detailsOfRequest.add(Utilities.Pack.saleChange(((EditSaleRequest) request)));
                break;
            case "AddFileRequest":
                detailsOfRequest.add(Utilities.Pack.addFileRequest(((AddFileRequest) request).getSubFile(), ((AddFileRequest) request).getFile()));
                break;
            case "AddAuctionRequest":
                detailsOfRequest.add(Utilities.Pack.addAuctionRequest(((AddAuctionRequest) request).getAuction()));
                break;
            case "EditFileRequest":
                detailsOfRequest.add(Utilities.Pack.subFile(((EditFileRequest) request).getSubFile()));
                detailsOfRequest.add(Utilities.Pack.fileChange(((EditFileRequest) request)));
                break;
        }
        return detailsOfRequest;
    }

    public void acceptRequest(String requestID, boolean accepted) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestID);
        if (request == null) throw new Exceptions.InvalidRequestIdException(requestID);

        if (accepted)
            request.accept();
        else
            request.decline();

        request.updateDatabase(database());
    }

    public ArrayList<String[]> manageCategories() {
        ArrayList<String[]> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            String[] info = new String[3];
            info[0] = category.getId();
            info[1] = category.getName();
            info[2] = category.getParent().getName();
            categoryNames.add(info);
        }
        return categoryNames;
    }

    /**
     * @param categoryName
     * @param field          "name", "parent"
     * @param newInformation newName, nameOfNewParent
     * @throws Exceptions.InvalidCategoryException     if this category doesn't exist
     * @throws Exceptions.InvalidFieldException        if there is no such field to edit
     * @throws Exceptions.SameAsPreviousValueException if new information is as the same as the previous one
     * @throws Exceptions.ExistingCategoryException    if there is already a category with new name
     * @throws Exceptions.SubCategoryException         if the chosen new parent is a child of this category
     */
    public void editCategory(String categoryName, String field, String newInformation) throws Exceptions.InvalidCategoryException,
            Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException, Exceptions.ExistingCategoryException, Exceptions.SubCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        switch (field) {
            case "name":
                if (category.getName().equals(newInformation)) throw new Exceptions.SameAsPreviousValueException(field);
                if (Category.getCategoryByName(newInformation) != null)
                    throw new Exceptions.ExistingCategoryException(newInformation);
                category.setName(newInformation);
                break;
            case "parent":
                Category newParentCategory = Category.getCategoryByName(newInformation);
                if (newParentCategory == null) {
                    category.setParent(Category.getSuperCategory().getId());
                } else {
                    if (category.hasSubCategoryWithId(newParentCategory.getId()))
                        throw new Exceptions.SubCategoryException(categoryName, newInformation);
                    category.setParent(newParentCategory.getId());
                }
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
        database().editCategory();
    }

    public void addCategory(String categoryName, String parentCategoryName, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException, Exceptions.ExistingCategoryException {
        if (Category.getCategoryByName(categoryName) != null || categoryName.equals("superCategory"))
            throw new Exceptions.ExistingCategoryException(categoryName);
        Category parentCategory = Category.getCategoryByName(parentCategoryName);
        if (parentCategory == null) throw new Exceptions.InvalidCategoryException(parentCategoryName);

        new Category(categoryName, parentCategory.getId(), specialProperties);
        database().createCategory();
    }

    public String[] getCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        return Utilities.Pack.category(category);
    }

    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        category.suspend();
        database().removeCategory();
    }

    public void setAccounts(String code, ArrayList<String[]> customerIds) {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount != null) {
            for (String[] customerId : customerIds) {
                discount.addCustomer(customerId[0], Integer.parseInt(customerId[1]));
            }
        }
    }

    public void removeAccountsFromDiscount(String code, ArrayList<String> customerIds) {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount != null) {
            for (String customerId : customerIds) {
                discount.removeCustomer(customerId);
            }
        }
    }

    public boolean existManager() {
        return Admin.getManager() != null;
    }

    public HashMap<String, String> getPropertyValuesOfAProductInARequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId, false);
        if (request == null || !request.getClass().getSimpleName().equals("AddProductRequest"))
            throw new Exceptions.InvalidRequestIdException(requestId);

        return new HashMap<>(((AddProductRequest) request).getProduct().getPropertyValues());
    }

    public HashMap<String, String> getPropertyValuesOfAFileInRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId, false);
        if (request == null || !request.getClass().getSimpleName().equals("AddFileRequest"))
            throw new Exceptions.InvalidRequestIdException(requestId);

        return new HashMap<>(((AddFileRequest) request).getFile().getPropertyValues());
    }

    public ArrayList<String[]> getSellablesInSaleRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId, false);
        if (request == null || !request.getClass().getSimpleName().equals("AddSaleRequest"))
            throw new Exceptions.InvalidRequestIdException(requestId);

        ArrayList<SubSellable> subSellables = new ArrayList<>(((AddSaleRequest) request).getSale().getSubProducts());
        ArrayList<String[]> sellablePacks = new ArrayList<>();
        for (SubSellable subSellable : subSellables) {
            if (subSellable instanceof SubProduct)
                sellablePacks.add(Utilities.Pack.product(((SubProduct) subSellable).getProduct()));
            else
                sellablePacks.add(Utilities.Pack.file(((SubFile) subSellable).getFile()));
        }
        return sellablePacks;
    }

    public void addPropertyToACategory(String categoryName, String property) throws Exceptions.InvalidCategoryException, Exceptions.ExistingPropertyException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);
        if (category.hasProperty(property)) throw new Exceptions.ExistingPropertyException(property);

        category.addProperty(property);
    }

    public void removePropertyFromACategory(String categoryName, String property) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        category.removeProperty(property);
    }

    public void editNameOfSellable(String sellableId, String newName) throws Exceptions.InvalidSellableIdException {
        Sellable sellable = Sellable.getSellableById(sellableId);
        if (sellable == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        sellable.setName(newName);
    }

    public void editBrandOfProduct(String sellableId, String newBrand) throws Exceptions.InvalidSellableIdException {
        Product product = Product.getProductById(sellableId);
        if (product == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        product.setBrand(newBrand);
    }

    public void editExtensionOfFile(String sellableId, String newBrand) throws Exceptions.InvalidSellableIdException {
        File file = File.getFileById(sellableId);
        if (file == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        file.setExtension(newBrand);
    }

    public void editImageOfSellable(String sellableId, String newImage) throws Exceptions.InvalidSellableIdException {
        Sellable sellable = Sellable.getSellableById(sellableId);
        if (sellable == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        sellable.setImagePath(newImage);
    }

    public void editPropertyOfSellable(String sellableId, String newProperty) throws Exceptions.InvalidSellableIdException {
        Sellable sellable = Sellable.getSellableById(sellableId);
        if (sellable == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        String[] keyValue = newProperty.split(",");
        sellable.setProperty(keyValue[0], keyValue[1]);
    }

    public void editInfoTextOfSellable(String sellableId, String newInfoText) throws Exceptions.InvalidSellableIdException {
        Sellable sellable = Sellable.getSellableById(sellableId);
        if (sellable == null) throw new Exceptions.InvalidSellableIdException(sellableId);

        sellable.setInfoText(newInfoText);
    }

    public ArrayList<String[]> getAllBuyLogs() {
        ArrayList<String[]> buyLogPacks = new ArrayList<>();
        for (BuyLog buyLog : BuyLog.getAllBuyLogs()) {
            buyLogPacks.add(Utilities.Pack.buyLog(buyLog));
        }
        for (FileLog fileLog : FileLog.getAllFileLogs()) {
            buyLogPacks.add(Utilities.Pack.fileLogAsBuyLog(fileLog));
        }
        return buyLogPacks;
    }

    public String[] getBuyLogWithId(String logId) throws Exceptions.InvalidLogIdException {
        BuyLog buyLog = BuyLog.getBuyLogById(logId);
        if (buyLog == null) throw new Exceptions.InvalidLogIdException(logId);

        return Utilities.Pack.buyLog(buyLog);
    }

    public ArrayList<String[]> getBuyLogItemsWithId(String logId) throws Exceptions.InvalidLogIdException {
        BuyLog buyLog = BuyLog.getBuyLogById(logId);
        if (buyLog == null) throw new Exceptions.InvalidLogIdException(logId);

        ArrayList<String[]> items = new ArrayList<>();
        buyLog.getLogItems().forEach(li -> items.add(Utilities.Pack.buyLogItem(li)));
        return items;
    }

    public void editBuyLogStatus(String logId, String newStatus) throws Exceptions.InvalidLogIdException {
        BuyLog buyLog = BuyLog.getBuyLogById(logId);
        if (buyLog == null) throw new Exceptions.InvalidLogIdException(logId);

        switch (newStatus) {
            case "Processing":
                buyLog.setShippingStatus(ShippingStatus.PROCESSING);
                break;
            case "Sending":
                buyLog.setShippingStatus(ShippingStatus.SENDING);
                break;
            case "Received":
                buyLog.setShippingStatus(ShippingStatus.RECEIVED);
                break;
        }
    }

    public void setCommission(double percentage) throws Exceptions.InvalidCommissionException {
        if (percentage < 0 || percentage > 100) throw new Exceptions.InvalidCommissionException();

        Admin.setCommission(percentage);
    }

    public void setWalletMin(double amount) {
        Wallet.setMinBalance(amount);
    }
}
