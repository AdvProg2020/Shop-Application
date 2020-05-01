package model.account;

import model.Sale;
import model.SubProduct;
import model.log.SellLog;

import java.util.ArrayList;

public class Seller extends Account {
    private String companyName;
    private double balance;
    private ArrayList<String> subProductIds;
    private ArrayList<String> saleIds;
    private ArrayList<String> sellLogIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone,
                  String companyName, double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.companyName = companyName;
        this.balance = balance;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!suspended) {
            subProductIds = new ArrayList<>();
            saleIds = new ArrayList<>();
            sellLogIds = new ArrayList<>();
        }
    }

    public static Seller getSellerById(String accountId) {
        return (Seller) Account.getAccountById(accountId);
    }

    @Override
    public String getType() {
        return "seller";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getBalance() {
        return balance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
    }

    public ArrayList<Sale> getSales() {
        ArrayList<Sale> sales = new ArrayList<>();
        for (String saleId : saleIds) {
            sales.add(Sale.getSaleById(saleId));
        }
        return sales;
    }

    public void addSale(String saleId) {
        saleIds.add(saleId);
    }

    public void removeSale(String saleId) {
        saleIds.remove(saleId);
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

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public ArrayList<SellLog> getSellLogs() {
        ArrayList<SellLog> sellLogs = new ArrayList<>();
        for (String sellLogId : sellLogIds) {
            sellLogs.add(SellLog.getSellLogById(sellLogId));
        }
        return sellLogs;
    }

    public void addSellLog(String sellLogId) {
        sellLogIds.add(sellLogId);
    }
}


