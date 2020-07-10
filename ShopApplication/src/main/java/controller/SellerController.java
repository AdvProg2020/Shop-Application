package controller;


import model.Category;
import model.Sale;
import model.account.Account;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;
import model.log.LogItem;
import model.log.SellLog;
import model.request.EditProductRequest;
import model.request.EditSaleRequest;
import model.request.Request;
import model.sellable.Product;
import model.sellable.Sellable;
import model.sellable.SubProduct;
import model.sellable.SubSellable;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class SellerController {

    private static final DateFormat dateFormat = Utilities.getDateFormat();
    private Controller mainController;

    public SellerController(Controller controller) {
        mainController = controller;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Database database() {
        return mainController.getDatabase();
    }

    /**
     * @return seller:String[7]
     * { String firstName, String lastName, String phone, String email, String password, String storeName, balance}
     */
    public String[] getPersonalInfoEditableFields() {
        return Utilities.Field.sellerPersonalInfoEditableFields();
    }

    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        if (field.equals("storeName")) {
            if (((Seller) currentAccount()).getStoreName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            ((Seller) currentAccount()).setStoreName(newInformation);
        } else if (field.equals("balance")) {
            if (((Seller) currentAccount()).getWallet().getBalance() == Double.parseDouble(newInformation))
                throw new Exceptions.SameAsPreviousValueException(newInformation);
            ((Seller) currentAccount()).getWallet().changeBalance(Double.parseDouble(newInformation) - ((Seller) currentAccount()).getWallet().getBalance());
        } else
            mainController.editPersonalInfo(field, newInformation);
        database().editAccount();
    }

    public ArrayList<String> viewCompanyInformation() {
        ArrayList<String> companyInformation = new ArrayList<>();
        companyInformation.add(((Seller) currentAccount()).getStoreName());
        return companyInformation;
    }

    public String isProductWithNameAndBrand(String name, String brand){
        Product p = Product.getProductByNameAndBrand(name, brand);
        if (p == null) return null;
        else return p.getId();
    }

    public boolean isNameAndBrandUsed(String name, String brand){
        return Product.isProductNameAndBrandUsed(name, brand);
    }

    public boolean doesSellerSellThisProduct(String productId) {
        Sellable sellable = Sellable.getSellableById(productId);
        return sellable.isSoldInStoreWithName(((Seller) currentAccount()).getStoreName());
    }

    public ArrayList<String[]> getAllSellLogs() {
        ArrayList<String[]> allSells = new ArrayList<>();
        for (SellLog sellLog : ((Seller) currentAccount()).getSellLogs()) {
            allSells.add(Utilities.Pack.sellLog(sellLog));
        }
        return allSells;
    }

    public ArrayList<String[]> getSellLogWithId(String logId) throws Exceptions.InvalidLogIdException {
        SellLog sellLog = null;
        for (SellLog log : ((Seller) currentAccount()).getSellLogs()) {
            if (log.getId().equals(logId))
                sellLog = log;
        }
        if (sellLog == null)
            throw new Exceptions.InvalidLogIdException(logId);
        else {
            ArrayList<String[]> logInfo = new ArrayList<>();
            logInfo.add(Utilities.Pack.sellLog(sellLog));
            for (LogItem item : sellLog.getLogItems()) {
                logInfo.add(Utilities.Pack.sellLogItem(item));
            }
            return logInfo;
        }
    }

    public ArrayList<String[]> manageProducts() {
        ArrayList<String[]> products = new ArrayList<>();
        for (SubSellable subSellable : ((Seller) currentAccount()).getSubSellables()) {
            products.add(Utilities.Pack.sellerSubProduct(subSellable));
        }
        return products;
    }

    //TODO: Useless
    public String[] viewProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubSellable subSellable : ((Seller) currentAccount()).getSubSellables()) {
            if (subSellable.getSellable().getId().equals(productID))
                return Utilities.Pack.subProduct(subSellable);
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    public ArrayList<String> viewProductBuyers(String productID) throws Exceptions.InvalidProductIdException {
        Seller seller = ((Seller) currentAccount());
        for (SubSellable subSellable : seller.getSubSellables()) {
            if (subSellable.getSellable().getId().equals(productID)) {
                ArrayList<String> buyers = new ArrayList<>();
                for (Customer customer : subSellable.getCustomers()) {
                    buyers.add(customer.getId());
                }
                return buyers;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    public String[] getProductEditableFields() {
        return Utilities.Field.productEditableFields();
    }

    public void editProduct(String productID, String field, String newInformation) throws Exceptions.InvalidProductIdException, Exceptions.ExistingProductException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        SubSellable targetedSubSellable = null;
        for (SubSellable subSellable : ((Seller) currentAccount()).getSubSellables()) {
            if (subSellable.getSellable().getId().equals(productID)) {
                targetedSubSellable = subSellable;
                break;
            }
        }
        if (targetedSubSellable == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            switch (field) {
                case "name": {
                    String existingProductId;
                    if ((existingProductId = exist(newInformation, ((Product)targetedSubSellable.getSellable()).getBrand())) == null) {
                        if (targetedSubSellable.getSellable().getName().equals(newInformation))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.NAME, newInformation);
                        database().request();
                    } else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "brand": {
                    String existingProductId;
                    if ((existingProductId = exist(targetedSubSellable.getSellable().getName(), newInformation)) == null) {
                        if (((Product)targetedSubSellable.getSellable()).getBrand().equals(newInformation))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.BRAND, newInformation);
                        database().request();
                    } else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "info text":
                    if (targetedSubSellable.getSellable().getInfoText().equals(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.INFO_TEXT, newInformation);
                    database().request();
                    break;
                case "price":
                    if (targetedSubSellable.getRawPrice() == Double.parseDouble(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.SUB_PRICE, newInformation);
                    database().request();
                    break;
                case "count":
                    if (((SubProduct)targetedSubSellable).getRemainingCount() == Integer.parseInt(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.SUB_COUNT, newInformation);
                    database().request();
                    break;
                case "imagePath":
                    new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.IMAGE_PATH, newInformation);
                    database().request();
                    break;
                case "property":
                    new EditProductRequest(targetedSubSellable.getId(), EditProductRequest.Field.PROPERTY, newInformation);
                    database().request();
                    break;
                default:
                    throw new Exceptions.InvalidFieldException();
            }
        }
    }

    /**
     * @param productName
     * @param brand
     * @return if there is an existing product returns its Id
     * else it returns null
     */
    public String exist(String productName, String brand) {
        Product product = Product.getProductByNameAndBrand(productName, brand);
        if (product != null)
            return product.getId();
        else
            return null;
    }

    //Todo: change it again!
    public void addNewProduct(String name, String brand, String infoText, String imagePath, String categoryName, HashMap<String, String> propertyValues,
                              double price, int count) throws Exceptions.ExistingProductException, Exceptions.InvalidCategoryException {

        Product product = Product.getProductByNameAndBrand(name, brand);
        if (product != null)
            throw new Exceptions.ExistingProductException(product.getId());
        else {
            Category category = Category.getCategoryByName(categoryName);
            if (category == null)
                throw new Exceptions.InvalidCategoryException(categoryName);
            SubSellable subSellable = new SubProduct(null, currentAccount().getId(), price, count, database());
            new Sellable(name, brand, infoText, imagePath, category.getId(), propertyValues, subSellable, database());
        }
    }

    //TODO
    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws Exceptions.InvalidProductIdException {
        if (Sellable.getSellableById(productId) == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            new SubSellable(productId, currentAccount().getId(), price, count, database());
        }
    }

    public void removeProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubSellable subSellable : ((Seller) currentAccount()).getSubSellables()) {
            if (subSellable.getSellable().getId().equals(productID)) {
                subSellable.suspend();
                database().removeSubSellable();
                return;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    public ArrayList<String[]> viewActiveSales() {
        ArrayList<String[]> saleInfos = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            saleInfos.add(Utilities.Pack.saleInfo(sale));
        }
        return saleInfos;
    }

    public ArrayList<String[]> viewArchiveSales() {
        return ((Seller) currentAccount()).getSaleArchive().stream().map(Utilities.Pack::saleInfo).collect(Collectors.toCollection(ArrayList::new));
    }

    public String[] viewSaleWithId(String saleId) throws Exceptions.InvalidSaleIdException {
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            if (sale.getId().equals(saleId)) {
                return Utilities.Pack.saleInfo(sale);
            }
        }
        throw new Exceptions.InvalidSaleIdException(saleId);
    }

    public ArrayList<String[]> getProductsInSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null) throw new Exceptions.InvalidSaleIdException(saleId);

        return sale.getSubSellables().stream().map(Utilities.Pack::productInSale).collect(Collectors.toCollection(ArrayList::new));
    }

    //TODO: DEPRECATED
    public String[] getSaleEditableFields() {
        return Utilities.Field.saleEditableFields();
    }

    public void editSale(String saleId, String field, String newInformation) throws
            Exceptions.InvalidSaleIdException, Exceptions.InvalidFormatException, Exceptions.InvalidDateException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        Sale targetedSale = null;
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            if (sale.getId().equals(saleId)) {
                targetedSale = sale;
                break;
            }
        }
        if (targetedSale == null)
            throw new Exceptions.InvalidSaleIdException(saleId);
        else {
            switch (field) {
                case "start date":
                    try {
                        if (targetedSale.getEndDate().after(dateFormat.parse(newInformation))) {
                            if (dateFormat.parse(newInformation).equals(targetedSale.getStartDate()))
                                throw new Exceptions.SameAsPreviousValueException("start date");
                            new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.START_DATE);
                            database().request();
                        } else
                            throw new Exceptions.InvalidDateException();
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "end date":
                    try {
                        if (targetedSale.getStartDate().before(dateFormat.parse(newInformation))) {
                            if (dateFormat.parse(newInformation).equals(targetedSale.getEndDate()))
                                throw new Exceptions.SameAsPreviousValueException("end date");
                            new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.END_DATE);
                            database().request();
                        } else
                            throw new Exceptions.InvalidDateException();
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "percentage":
                    if (Double.parseDouble(newInformation) == targetedSale.getPercentage())
                        throw new Exceptions.SameAsPreviousValueException("percentage");
                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.PERCENTAGE);
                    database().request();
                    break;
                case "maximum":
                    if (Double.parseDouble(newInformation) == targetedSale.getMaximumAmount())
                        throw new Exceptions.SameAsPreviousValueException("maximum");
                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.MAXIMUM);
                    database().request();
                    break;
                default:
                    throw new Exceptions.InvalidFieldException();
            }

        }
    }

    public void addSale(String StartDate, String EndDate, double percentage, double maximum, ArrayList<String> productIds) throws Exceptions.InvalidDateException, Exceptions.InvalidProductIdsForASeller, Exceptions.InvalidFormatException {
        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(StartDate);
            endDate = dateFormat.parse(EndDate);
        } catch (ParseException e) {
            throw new Exceptions.InvalidFormatException("date");
        }

        if (startDate.before(endDate)) {
            Sale sale = new Sale(currentAccount().getId(), startDate, endDate, percentage, maximum, database());
            Sellable sellable;
            SubSellable subSellable;
            ArrayList<String> invalidSubProductIds = new ArrayList<>();
            for (String productId : productIds) {
                sellable = Sellable.getSellableById(productId);
                if (sellable != null) {
                    subSellable = sellable.getSubSellableOfaSeller(currentAccount().getId());
                    if (subSellable != null)
                        sale.addSubSellable(subSellable.getId());
                    else
                        invalidSubProductIds.add(productId);
                } else
                    invalidSubProductIds.add(productId);
            }
            if (invalidSubProductIds.size() > 0)
                throw new Exceptions.InvalidProductIdsForASeller(Utilities.Pack.invalidProductIds(invalidSubProductIds));
        } else
            throw new Exceptions.InvalidDateException();
    }

    public void addProductsToSale(String saleId, ArrayList<String> subProductIds){
        Sale sale = Sale.getSaleById(saleId);
        for (String subProductId : subProductIds) {
            sale.addSubSellable(subProductId);
        }
    }

    public void removeProductsFromSale(String saleId, ArrayList<String> subProductIds){
        Sale sale = Sale.getSaleById(saleId);
        for (String subProductId : subProductIds) {
            sale.removeSubSellable(subProductId);
        }
    }

    public double viewBalance() {
        return ((Seller) currentAccount()).getWallet().getBalance();
    }

    public void removeSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if( sale != null ){
            sale.suspend();
        }else {
            throw new Exceptions.InvalidSaleIdException(saleId);
        }
    }

//    public void removeSale(ArrayList<String> saleIds) throws Exceptions.InvalidSaleIdException {
//        for (String saleId : saleIds) {
//            removeSale(saleId);
//        }
//    }

    public ArrayList<String[]> getPendingRequests(){
        ArrayList<Request> requests = new ArrayList<>(((Seller)currentAccount()).getPendingRequests());
        ArrayList<String[]> requestPacks = new ArrayList<>();
        for (Request request : requests) {
            requestPacks.add(Utilities.Pack.request(request));
        }
        return requestPacks;
    }

    public ArrayList<String[]> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>(Category.getAllCategories());
        ArrayList<String[]> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(new String[]{category.getName(), category.getParent().getName()});
        }
        return categoryNames;
    }

    public boolean doesSellSubProduct(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null) {
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        } else {
            return ((Seller) currentAccount()) == subSellable.getSeller();
        }
    }
}
