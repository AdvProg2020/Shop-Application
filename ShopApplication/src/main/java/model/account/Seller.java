package model.account;

import jdk.jfr.Label;
import model.BasicMethods;
import model.Sale;
import model.SubProduct;
import model.log.SellLog;
import model.request.AddSellerRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Seller extends Account {
    private static int lastNum = 1;
    private String storeName;
    private double balance;
    private transient Set<String> subProductIds;
    private transient Set<String> saleIds;
    private transient Set<String> sellLogIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone,
                  String storeName, double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.storeName = storeName;
        this.balance = balance;
        new AddSellerRequest(this);
    }

    public static Seller getSellerById(String accountId, boolean... suspense) {
        return (Seller) getAccountById(accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
        allAccounts.put(accountId, this);
        lastNum++;
        sellLogIds = new HashSet<>();
        if (!suspended) {
            subProductIds = new HashSet<>();
            saleIds = new HashSet<>();
        }
    }

    @Override
    public void suspend() {
        for (SubProduct subProduct : getSubProducts()) {
            subProduct.suspend();
        }
        subProductIds = null;
        for (Sale sale : getSales()) {
            sale.suspend();
        }
        saleIds = null;
        super.suspend();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getBalance() {
        return balance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
    }

    public List<Sale> getSales() {
        List<Sale> sales = new ArrayList<>();
        for (String saleId : saleIds) {
            sales.add(Sale.getSaleById(saleId));
        }

        return sales;
    }

    @Label("Model internal use only!")
    public void addSale(String saleId) {
        saleIds.add(saleId);
    }

    @Label("Model internal use only!")
    public void removeSale(String saleId) {
        saleIds.remove(saleId);
    }

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }

        return subProducts;
    }

    @Label("Model internal use only!")
    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    @Label("Model internal use only!")
    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public List<SellLog> getSellLogs() {
        List<SellLog> sellLogs = new ArrayList<>();
        for (String sellLogId : sellLogIds) {
            sellLogs.add(SellLog.getSellLogById(sellLogId));
        }

        return sellLogs;
    }

    @Label("Model internal use only!")
    public void addSellLog(String sellLogId) {
        sellLogIds.add(sellLogId);
    }
}


