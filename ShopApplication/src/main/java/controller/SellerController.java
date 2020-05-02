package controller;

import model.Product;
import model.Sale;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;
import model.log.LogItem;
import model.log.SellLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SellerController extends Controller {

    //Done!!
    public void editInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {
        if(field.equals("companyName"))
            ((Seller) currentAccount).setCompanyName(newInformation);
        else
            editCommonInformation(field, newInformation);
    }

    //Done!! any thing other companyName?
    public ArrayList<String> viewCompanyInformation() {
        ArrayList<String> companyInformation = new ArrayList<>();
        companyInformation.add(((Seller)currentAccount).getCompanyName());
        return companyInformation;
    }

    //Done!!
    public ArrayList<ArrayList<String>> viewSellHistory() {
        ArrayList<ArrayList<String>> sells = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (SellLog sellLog : ((Seller) currentAccount).getSellLogs()) {
            ArrayList<String> sellPack = new ArrayList<>();
            sellPack.add(sellLog.getSellLogId());
            sellPack.add(dateFormat.format(sellLog.getDate()));
            sellPack.add(sellLog.getCustomer().getUsername());
            sellPack.add(Double.toString(sellLog.getReceivedMoney()));
            sellPack.add(Double.toString(sellLog.getTotalSaleAmount()));
            for (LogItem logItem : sellLog.getLogItems()) {
                sellPack.add(logItem.getSubProduct().getSubProductId());
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
            productPack[0] = subProduct.getProduct().getProductId();
            productPack[1] = subProduct.getProduct().getName();
            products.add(productPack);
        }
        return products;
    }

    //Not necessary
    public ArrayList<String> viewProductsForASeller(String categoryName) {return null;}

    //Done!!
    public ArrayList<ArrayList<String>> viewProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller) currentAccount).getSubProducts()) {
            if(subProduct.getProduct().getProductId().equals(productID))
                return showProduct(productID);
        }
        throw new Exceptions.InvalidProductIdException();

    }

    //Done!!
    public ArrayList<String> viewProductBuyers(String productID) throws Exceptions.InvalidProductIdException {

        Seller seller = ((Seller)currentAccount);
        for (SubProduct subProduct : seller.getSubProducts()) {
            if(subProduct.getProduct().getProductId().equals(productID)){
                ArrayList<String> buyers = new ArrayList<>();
                for (Customer customer : subProduct.getCustomers()) {
                    buyers.add(customer.getAccountId());
                }
                return buyers;
            }
        }
        throw new Exceptions.InvalidProductIdException();
    }

    //Todo
    public void editProduct(String productID, String field, String newInformation) {
    }

    //Todo
    public void addProduct(ArrayList<String> information) {
    }

    //Done!
    public void removeProduct(String productID) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : ((Seller)currentAccount).getSubProducts()) {
            if(subProduct.getProduct().getProductId().equals(productID)){
                subProduct.suspend();
                return;
            }
        }
        throw new Exceptions.InvalidProductIdException();
    }

    //Done!!
    public ArrayList<String> viewSales() {
        ArrayList<String> saleIds = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount).getSales()) {
            saleIds.add(sale.getSaleId());
        }
        return saleIds;
    }

    //Done!!
    public ArrayList<String> viewSaleWithId(String saleId) throws Exceptions.InvalidSaleIdException {
        for (Sale sale : ((Seller) currentAccount).getSales()) {
            if(sale.getSaleId().equals(saleId)){
                return getSaleInfo(sale);
            }
        }
        throw new Exceptions.InvalidSaleIdException();
    }

    //Todo
    public void editSale(String saleId, String field, String newInformation) {
    }

    //Todo
    public void addSale(ArrayList<String> saleInformation) {
    }

    //Done!
    public double viewBalance() {
        return ((Seller)currentAccount).getBalance();
    }
}
