package model.account;

import model.Sale;
import model.SubProduct;
import model.log.SellLog;

import java.util.ArrayList;

public class Seller extends Account {
    private String storeName;
    private int credit;
    private ArrayList<String> subProductIds;
    private ArrayList<String> sellLogIds;
    private ArrayList<String> saleIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone,
                  String storeName, int credit) {
        super(username, password, firstName, lastName, email, phone);
        this.storeName = storeName;
        this.credit = credit;
        subProductIds = new ArrayList<>();
        sellLogIds = new ArrayList<>();
        saleIds = new ArrayList<>();
    }

    public static Seller getSellerById(String accountId) {
        return (Seller) Account.getAccountById(accountId);
    }

    @Override
    public String getType() {
        return "seller";
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getCredit() {
        return credit;
    }

    public void addCredit(int changeAmount) {
        credit += changeAmount;
    }

    public ArrayList<SellLog> getSellLogs() {
        ArrayList<SellLog> sellLogs = new ArrayList<>();
        for (String sellLogId : sellLogIds) {
            sellLogs.add(SellLog.getSellLogById(sellLogId));
        }
        return sellLogs;
    }

    public ArrayList<Sale> getSales() {
        ArrayList<Sale> sales = new ArrayList<>();
        for (String saleId : saleIds) {
            sales.add(Sale.getSaleById(saleId));
        }
        return sales;
    }

    public ArrayList<SubProduct> getSubProducts() {
        ArrayList<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }
        return subProducts;
    }

    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    public void addSellLog(String sellLogId) {
        sellLogIds.add(sellLogId);
    }

    public void addSale(String saleId) {
        saleIds.add(saleId);
    }
}


