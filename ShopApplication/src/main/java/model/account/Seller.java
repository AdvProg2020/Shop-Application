package model.account;

import model.Sale;
import model.SubProduct;
import model.log.SellLog;
import model.request.AddSellerRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class Seller extends Account {
    private String storeName;
    private double balance;
    private transient HashSet<String> subProductIds;
    private transient HashSet<String> saleIds;
    private transient HashSet<String> sellLogIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone,
                  String storeName, double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.storeName = storeName;
        this.balance = balance;
        new AddSellerRequest(this);
    }

    public static Seller getSellerById(String accountId) {
        return getSellerById(accountId, true);
    }

    public static Seller getSellerById(String accountId, boolean checkSuspense) {
        Seller seller = (Seller) allAccounts.get(accountId);
        if (checkSuspense && seller != null && seller.suspended) {
            seller = null;
        }
        return seller;
    }

    @Override
    public void initialize() {
        super.initialize();
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
        for (Sale sale : getSales()) {
            sale.suspend();
        }
        subProductIds = null;
        saleIds = null;
        super.suspend();
    }

    @Override
    public String getType() {
        return "Seller";
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


