package controller;

import jdk.jfr.Label;
import model.Category;
import model.Product;
import model.Sale;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;
import model.database.DatabaseManager;
import model.log.LogItem;
import model.log.SellLog;
import model.request.EditProductRequest;
import model.request.EditSaleRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

//TODO: database constructor
public class SellerController extends Controller {

    public SellerController(DatabaseManager DataBaseManager) {
        super(DataBaseManager);
    }

    //Done!!

    /**
     * @return seller:String[7]
     *         { String firstName, String lastName, String phone, String email, String password, String storeName, balance}
     */
    public String[] getPersonalInfoEditableFields(){
        String[] editableFields = new String[7];
        editableFields[0] = "firstName";
        editableFields[1] = "lastName";
        editableFields[2] = "phone";
        editableFields[3] = "email";
        editableFields[4] = "password";
        editableFields[5] = "storeName";
        editableFields[6] = "balance";
        return editableFields;
    }


    //Done!!
    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        if (field.equals("storeName")) {
            if (((Seller) currentAccount).getStoreName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            ((Seller) currentAccount).setStoreName(newInformation);
        } else
            super.editPersonalInfo(field, newInformation);
        databaseManager.editAccount();
    }

    //Done!! any thing other storeName?
    public ArrayList<String> viewCompanyInformation() {
        ArrayList<String> companyInformation = new ArrayList<>();
        companyInformation.add(((Seller) currentAccount).getStoreName());
        return companyInformation;
    }

    public ArrayList<String[]> getAllSellLogs() {
        ArrayList<String[]> allSells = new ArrayList<>();
        for (SellLog sellLog : ((Seller) currentAccount).getSellLogs()) {
            allSells.add(sellPack(sellLog));
        }
        return allSells;
    }

    //Done!!
    public ArrayList<String[]> getSellLogWithId(String logId) throws Exceptions.InvalidLogIdException {
        SellLog sellLog = null;
        for (SellLog log : ((Seller) currentAccount).getSellLogs()) {
            if (log.getId().equals(logId))
                sellLog = log;
        }
        if (sellLog == null)
            throw new Exceptions.InvalidLogIdException(logId);
        else {
            ArrayList<String[]> logInfo = new ArrayList<>();
            logInfo.add(sellPack(sellLog));
            for (LogItem item : sellLog.getLogItems()) {
                logInfo.add(logItemPack(item));
            }
            return logInfo;
        }
    }

    @Label("For showing-sellLog-methods")
    private String[] sellPack(SellLog sellLog) {
        String[] sellPack = new String[9];
        sellPack[0] = sellLog.getId();
        sellPack[1] = dateFormat.format(sellLog.getDate());
        sellPack[2] = sellLog.getCustomer().getUsername();
        sellPack[3] = Double.toString(sellLog.getReceivedMoney());
        sellPack[4] = Double.toString(sellLog.getTotalSaleAmount());
        sellPack[5] = sellLog.getReceiverName();
        sellPack[6] = sellLog.getReceiverPhone();
        sellPack[7] = sellLog.getReceiverAddress();
        sellPack[8] = sellLog.getShippingStatus().toString();
        return sellPack;
    }

    @Label("For showing a product in a sellLog")
    private String[] logItemPack(LogItem item) {
        String[] productPack = new String[8];
        Product product = item.getSubProduct().getProduct();
        productPack[0] = product.getId();
        productPack[1] = product.getName();
        productPack[2] = product.getBrand();
        productPack[3] = Integer.toString(item.getCount());
        productPack[4] = Double.toString(item.getPrice());
        productPack[5] = Double.toString(item.getSaleAmount());
        return productPack;
    }

    //Done!!
    public ArrayList<String[]> manageProducts() {
        ArrayList<String[]> products = new ArrayList<>();
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            products.add(productPack(subProduct.getProduct()));
        }
        return products;
    }

    //Done!!
    public String[] viewProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID)){
                String[] subProductPack = new String[9];
                subProductPack[0] = subProduct.getProduct().getId();
                subProductPack[1] = subProduct.getProduct().getName();
                subProductPack[2] = subProduct.getProduct().getBrand();
                subProductPack[3] = subProduct.getProduct().getCategory().getName();
                subProductPack[4] = subProduct.getProduct().getInfoText();
                subProductPack[5] = Integer.toString(subProduct.getRemainingCount());
                subProductPack[6] = Double.toString(subProduct.getRawPrice());
                subProductPack[7] = subProduct.getSale() != null ? subProduct.getSale().getId() : "-";
                subProductPack[8] = subProduct.getSale() != null ? Double.toString(subProduct.getPriceWithSale()) : "-";
                return subProductPack;
            }

        }
        throw new Exceptions.InvalidProductIdException(productID);

    }

    //Done!!
    public ArrayList<String> viewProductBuyers(String productID) throws Exceptions.InvalidProductIdException {
        Seller seller = ((Seller) currentAccount);
        for (SubProduct subProduct : seller.getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID)) {
                ArrayList<String> buyers = new ArrayList<>();
                for (Customer customer : subProduct.getCustomers()) {
                    buyers.add(customer.getId());
                }
                return buyers;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    //Done!!
    public String[] getProductEditableFields() {
        String[] editableFields = new String[5];
        editableFields[0] = "name";
        editableFields[1] = "brand";
        editableFields[2] = "info text";
        editableFields[3] = "price";
        editableFields[4] = "count";
        return editableFields;
    }

    //Done!!
    public void editProduct(String productID, String field, String newInformation) throws Exceptions.InvalidProductIdException, Exceptions.ExistingProductException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        SubProduct targetedSubProduct = null;
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID)) {
                targetedSubProduct = subProduct;
                break;
            }
        }
        if (targetedSubProduct == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            switch (field) {
                case "name": {
                    String existingProductId;
                    if ((existingProductId = exist(newInformation, targetedSubProduct.getProduct().getBrand())) == null) {
                        if (targetedSubProduct.getProduct().getName().equals(newInformation))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
                        databaseManager.request();
                    } else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "brand": {
                    String existingProductId;
                    if ((existingProductId = exist(targetedSubProduct.getProduct().getName(), newInformation)) == null) {
                        if (targetedSubProduct.getProduct().getBrand().equals(newInformation))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.BRAND, newInformation);
                        databaseManager.request();
                    } else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "info text":
                    if (targetedSubProduct.getProduct().getInfoText().equals(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.INFO_TEXT, newInformation);
                    databaseManager.request();
                    break;
                case "price":
                    if (targetedSubProduct.getRawPrice() == Double.parseDouble(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.SUB_PRICE, newInformation);
                    databaseManager.request();
                    break;
                case "count":
                    if (targetedSubProduct.getRemainingCount() == Integer.parseInt(newInformation))
                        throw new Exceptions.SameAsPreviousValueException(field);
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.SUB_COUNT, newInformation);
                    databaseManager.request();
                    break;
                default:
                    throw new Exceptions.InvalidFieldException();
            }
        }
    }

    //Done!!

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

    //Done!! TODO: Shayan please check this
    public void addNewProduct(String name, String brand, String infoText, String categoryName, ArrayList<String> specialProperties,
                              double price, int count) throws Exceptions.ExistingProductException, Exceptions.InvalidCategoryException {
        Product product = Product.getProductByNameAndBrand(name, brand);
        if (product != null)
            throw new Exceptions.ExistingProductException(product.getId());
        else {
            Category category = Category.getCategoryByName(categoryName);
            if (category == null)
                throw new Exceptions.InvalidCategoryException(categoryName);
            SubProduct subProduct = new SubProduct(null, currentAccount.getId(), price, count);
            new Product(name, brand, infoText, category.getId(), specialProperties, subProduct);
            databaseManager.request();
        }
    }

    //Done!!
    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws Exceptions.InvalidProductIdException {
        if (Product.getProductById(productId) == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            new SubProduct(productId, currentAccount.getId(), price, count);
            databaseManager.request();
        }
    }

    //Done!!
    public void removeProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID)) {
                subProduct.suspend();
                databaseManager.removeSubProduct();
                return;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    //Done!!
    public ArrayList<String[]> viewSales() {
        ArrayList<String[]> saleInfos = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount).getSales()) {
            saleInfos.add(getSaleInfo(sale));
        }
        return saleInfos;
    }

    //Done!!
    public String[] viewSaleWithId(String saleId) throws Exceptions.InvalidSaleIdException {
        for (Sale sale : ((Seller) currentAccount).getSales()) {
            if (sale.getId().equals(saleId)) {
                return getSaleInfo(sale);
            }
        }
        throw new Exceptions.InvalidSaleIdException(saleId);
    }

    //Done!!
    public String[] getSaleEditableFields() {
        String[] saleEditableFields = new String[4];
        saleEditableFields[0] = "start date";
        saleEditableFields[1] = "end date";
        saleEditableFields[2] = "percentage";
        saleEditableFields[3] = "maximum";
        return saleEditableFields;
    }

    //Done!!
    public void editSale(String saleId, String field, String newInformation) throws
            Exceptions.InvalidSaleIdException, Exceptions.InvalidFormatException, Exceptions.InvalidDateException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        Sale targetedSale = null;
        for (Sale sale : ((Seller) currentAccount).getSales()) {
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
                            databaseManager.request();
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
                            databaseManager.request();
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
                    databaseManager.request();
                    break;
                case "maximum":
                    if (Double.parseDouble(newInformation) == targetedSale.getMaximumAmount())
                        throw new Exceptions.SameAsPreviousValueException("maximum");
                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.MAXIMUM);
                    databaseManager.request();
                    break;
                default:
                    throw new Exceptions.InvalidFieldException();
            }

        }
    }

    //Done!! TODO
    public void addSale(Date startDate, Date endDate, double percentage, double maximum, ArrayList<String> productIds) throws Exceptions.InvalidDateException, Exceptions.InvalidProductIdsForASeller {
        if (startDate.before(endDate)) {
            Sale sale = new Sale(((Seller) currentAccount).getId(), startDate, endDate, percentage, maximum);
            Product product = null;
            SubProduct subProduct = null;
            ArrayList<String> invalidSubProductIds = new ArrayList<>();
            for (String productId : productIds) {
                product = Product.getProductById(productId);
                if (product != null) {
                    subProduct = product.getSubProductWithSellerId(((Seller) currentAccount).getId());
                    if (subProduct != null)
                        sale.addSubProduct(subProduct.getId());
                    else
                        invalidSubProductIds.add(productId);
                } else
                    invalidSubProductIds.add(productId);
            }
            databaseManager.request();
            if (invalidSubProductIds.size() > 0)
                throw new Exceptions.InvalidProductIdsForASeller(invalidProductIds(invalidSubProductIds));
        } else
            throw new Exceptions.InvalidDateException();
    }

    private String invalidProductIds(ArrayList<String> subProductIds) {
        StringBuilder invalidSubProductIds = new StringBuilder();
        String falseSubProduct = null;
        for (String subProductId : subProductIds) {
            falseSubProduct = "\n" + subProductId;
            invalidSubProductIds.append(falseSubProduct);
        }
        return invalidSubProductIds.toString();
    }

    //Done!
    public double viewBalance() {
        return ((Seller) currentAccount).getBalance();
    }
}
