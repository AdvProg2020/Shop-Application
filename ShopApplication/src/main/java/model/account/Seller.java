package model.account;

import model.Sale;
import model.SubProduct;

import java.util.ArrayList;

public class Seller extends Account {
    private String storeName;
    private ArrayList<SubProduct> products;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void addProduct(String productId) {}

    public void removeProduct(String productId) {}

    public void addSale(String saleId) {}

    public void removeSale(String saleId) {}

    @Override
    public void addAccountToDatabase() {

    }

    @Override
    public void removeAccountFromDatabase() {

    }

    @Override
    public void loadDatabase() {

    }

    @Override
    public void updateAccountInDatabase(String username) {

    }
}


