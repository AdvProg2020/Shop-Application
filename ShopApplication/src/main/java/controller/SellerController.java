package controller;

import model.Category;
import model.Product;
import model.Sale;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;
import model.log.LogItem;
import model.log.SellLog;
import model.request.EditProductRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SellerController extends Controller {

    //Done!!
    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        if (field.equals("storeName")) {
            if (((Seller) currentAccount).getStoreName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            ((Seller) currentAccount).setStoreName(newInformation);
        }else
            super.editPersonalInfo(field, newInformation);
    }

    //Done!! any thing other storeName?
    public ArrayList<String> viewCompanyInformation() {
        ArrayList<String> companyInformation = new ArrayList<>();
        companyInformation.add(((Seller) currentAccount).getStoreName());
        return companyInformation;
    }

    //Done!!
    public ArrayList<ArrayList<String>> viewSellHistory() {
        ArrayList<ArrayList<String>> sells = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (SellLog sellLog : ((Seller) currentAccount).getSellLogs()) {
            ArrayList<String> sellPack = new ArrayList<>();
            sellPack.add(sellLog.getId());
            sellPack.add(dateFormat.format(sellLog.getDate()));
            sellPack.add(sellLog.getCustomer().getUsername());
            sellPack.add(Double.toString(sellLog.getReceivedMoney()));
            sellPack.add(Double.toString(sellLog.getTotalSaleAmount()));
            for (LogItem logItem : sellLog.getLogItems()) {
                sellPack.add(logItem.getSubProduct().getId());
                sellPack.add(Integer.toString(logItem.getCount()));
            }
            sells.add(sellPack);
        }
        return sells;
    }

    //Done!!
    public ArrayList<String[]> manageProducts() {
        ArrayList<String[]> products = new ArrayList<>();
        String[] productPack = new String[2];
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            productPack[0] = subProduct.getProduct().getId();
            productPack[1] = subProduct.getProduct().getName();
            products.add(productPack);
        }
        return products;
    }

    //Not necessary
    public ArrayList<String> viewProductsForASeller(String categoryName) {
        return null;
    }

    //Done!!
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
    public String[] getProductEditableFields(){
        String[] editableFields = new String[5];
        editableFields[0] = "name";
        editableFields[1] = "brand";
        editableFields[2] = "info text";
        editableFields[3] = "price";
        editableFields[4] = "count";
        return editableFields;
    }

    //Todo
    public void editProduct(String productID, String field, String newInformation) throws Exceptions.InvalidProductIdException {
        SubProduct targetedSubProduct = null;
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if(subProduct.getProduct().getId().equals(productID)) {
                targetedSubProduct = subProduct;
                break;
            }
        }
        if(targetedSubProduct == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            EditProductRequest.Field fieldToEdit = null;
            if(field.equals("name"))
                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
            else if(field.equals("brand"))
                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
            else if(field.equals(""))
            new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
            else if(field.equals("name"))
            new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
            else if(field.equals("name"))
            new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
        }
            new EditProductRequest(targetedSubProduct.getId(), , newInformation)
    }

    //Done!!

    /**
     *
     * @param productName
     * @param brand
     * @return if there is an existing product returns its Id
     *          else it returns null
     */
    public String exist(String productName, String brand) {
        Product product = Product.getProductsByNameAndBrand(productName, brand);
        if( product != null)
            return product.getId();
        else
            return null;
    }

    //Done!! TODO: Shayan please check this
    public void addNewProduct(String name, String brand, String infoText, String categoryName, ArrayList<String> specialProperties,
                              double price, int count) throws Exceptions.ExistingProductException{
        Product product;
        if((product = Product.getProductsByNameAndBrand(name, brand)) != null)
            throw new Exceptions.ExistingProductException(product.getId());
        else{
            Category category;
            if((category = Category.getCategoryByName(categoryName)) == null)
                category = Category.getSuperCategory();
            SubProduct subProduct = new SubProduct(null, currentAccount.getId(), price, count);
            new Product(name, brand, infoText, category.getId(), specialProperties, subProduct);
        }
    }

    //Done!!
    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws Exceptions.InvalidProductIdException {
        if(Product.getProductById(productId) == null)
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

    //Todo
    public void editSale(String saleId, String field, String newInformation) {
    }

    //Todo: check dates
    public void addSale(ArrayList<String> saleInformation) {
    }

    //Done!
    public double viewBalance() {
        return ((Seller) currentAccount).getBalance();
    }
}
