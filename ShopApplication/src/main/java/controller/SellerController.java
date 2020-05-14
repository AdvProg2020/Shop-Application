package controller;

import jdk.jfr.Label;
import model.Category;
import model.Product;
import model.Sale;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;
import model.log.LogItem;
import model.log.SellLog;
import model.request.EditProductRequest;
import model.request.EditSaleRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellerController extends Controller {

    //Done!!
    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        if (field.equals("storeName")) {
            if (((Seller) currentAccount).getStoreName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            ((Seller) currentAccount).setStoreName(newInformation);
        } else
            super.editPersonalInfo(field, newInformation);
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
        String[] sellPack = new String[5];
        sellPack[0] = sellLog.getId();
        sellPack[1] = dateFormat.format(sellLog.getDate());
        sellPack[2] = sellLog.getCustomer().getUsername();
        sellPack[3] = Double.toString(sellLog.getReceivedMoney());
        sellPack[4] = Double.toString(sellLog.getTotalSaleAmount());
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
    //TODO: since we are returning sub product in some way, you can return price as well!
    public String[] viewProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID))
                return digest(subProduct.getProduct());
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
    //TODO: same value exception
    public void editProduct(String productID, String field, String newInformation) throws Exceptions.InvalidProductIdException, Exceptions.ExistingProductException, Exceptions.InvalidFieldException {
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
            EditProductRequest.Field fieldToEdit = null;
            switch (field) {
                case "name": {
                    String existingProductId;
                    if ((existingProductId = exist(newInformation, targetedSubProduct.getProduct().getBrand())) == null)
                        new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
                    else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "brand": {
                    String existingProductId;
                    if ((existingProductId = exist(targetedSubProduct.getProduct().getName(), newInformation)) == null)
                        new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.BRAND, newInformation);
                    else
                        throw new Exceptions.ExistingProductException(existingProductId);
                    break;
                }
                case "info text":
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.INFO_TEXT, newInformation);
                    break;
                case "price":
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.PRICE, newInformation);
                    break;
                case "count":
                    new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.COUNT, newInformation);
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
                              double price, int count) throws Exceptions.ExistingProductException {
        Product product;
        if ((product = Product.getProductByNameAndBrand(name, brand)) != null)
            throw new Exceptions.ExistingProductException(product.getId());
        else {
            Category category;
            if ((category = Category.getCategoryByName(categoryName)) == null)
                category = Category.getSuperCategory();
            SubProduct subProduct = new SubProduct(null, currentAccount.getId(), price, count);
            new Product(name, brand, infoText, category.getId(), specialProperties, subProduct);
        }
    }

    //Done!!
    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws Exceptions.InvalidProductIdException {
        if (Product.getProductById(productId) == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else
            new SubProduct(productId, currentAccount.getId(), price, count);
    }

    //Done!!
    public void removeProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productID)) {
                subProduct.suspend();
                return;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    //Done!!
    public ArrayList<String> viewSales() {
        ArrayList<String> saleIds = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount).getSales()) {
            saleIds.add(sale.getId());
        }
        return saleIds;
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
                        }else
                            throw new Exceptions.InvalidDateException();
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "end date":
                    try {
                        if (targetedSale.getStartDate().before(dateFormat.parse(newInformation))) {
                            if(dateFormat.parse(newInformation).equals(targetedSale.getEndDate()))
                                throw new Exceptions.SameAsPreviousValueException("end date");
                            new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.END_DATE);
                        }else
                            throw new Exceptions.InvalidDateException();
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "percentage":
                    if(Double.parseDouble(newInformation) == targetedSale.getPercentage())
                        throw new Exceptions.SameAsPreviousValueException("percentage");
                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.PERCENTAGE);
                    break;
                case "maximum":
                    if(Double.parseDouble(newInformation) == targetedSale.getMaximumAmount())
                        throw new Exceptions.SameAsPreviousValueException("maximum");
                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.MAXIMUM);
                    break;
                default:
                    throw new Exceptions.InvalidFieldException();
            }

        }
    }

    //Done!!
    public void addSale(Date startDate, Date endDate, double percentage, double maximum, ArrayList<String> productIds) throws Exceptions.InvalidDateException, Exceptions.InvalidProductIdsForASeller {
        if (startDate.before(endDate)) {
            Sale sale = new Sale(((Seller) currentAccount).getId(), startDate, endDate, percentage, maximum);
            Product product = null;
            SubProduct subProduct = null;
            ArrayList<String> falseSubProductIds = new ArrayList<>();
            for (String productId : productIds) {
                product = Product.getProductById(productId);
                if (product != null) {
                    subProduct = product.getSubProductWithSellerId(((Seller) currentAccount).getId());
                    if (subProduct != null)
                        sale.addSubProduct(subProduct.getId());
                    else
                        falseSubProductIds.add(productId);
                } else
                    falseSubProductIds.add(productId);
            }
            if (falseSubProductIds.size() > 0)
                throw new Exceptions.InvalidProductIdsForASeller(falseSubProductIds(falseSubProductIds));
        } else
            throw new Exceptions.InvalidDateException();
    }

    private String falseSubProductIds(ArrayList<String> subProductIds) {
        StringBuilder falseSubProductIds = new StringBuilder();
        String falseSubProduct = null;
        for (String subProductId : subProductIds) {
            falseSubProduct = "\n" + subProductId;
            falseSubProductIds.append(falseSubProduct);
        }
        return falseSubProductIds.toString();
    }

    //Done!
    public double viewBalance() {
        return ((Seller) currentAccount).getBalance();
    }
}
