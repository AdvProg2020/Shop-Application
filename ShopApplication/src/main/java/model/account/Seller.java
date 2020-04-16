package model.account;

import model.Sale;
import model.SubProduct;
import model.log.SellLog;

import java.util.ArrayList;

public class Seller extends Account {
    private String storeName;
    private ArrayList<SubProduct> products;
    private ArrayList<SellLog> sellLogs;
    private ArrayList<Sale> Sales;
    private int balance;

    public Seller(String accountId, String username, String password, String firstName, String lastName, String email, String phone, String companyName, int balance) {
        super(accountId, username, password, firstName, lastName, email, phone);
        this.storeName = companyName;
        this.balance = balance;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<SellLog> getSellLogs() {
        return sellLogs;
    }

    public ArrayList<Sale> getSales() {
        return Sales;
    }

    public ArrayList<SubProduct> getProducts() {
        return products;
    }

    public void addProduct(String productId) {
    }

    public void removeProduct(String productId) {
    }

    public void sellLog(String sellLogId) {
    }

    public void addSale(String saleId) {
    }

    public void removeSale(String saleId) {
    }

    @Override
    protected void addAccountToDatabase() {

    }

    @Override
    protected void removeAccountFromDatabase() {

    }

    @Override
    protected void loadDatabase() {

    }

    @Override
    protected void updateAccountInDatabase(String username) {

    }
}


