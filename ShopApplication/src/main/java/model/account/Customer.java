package model.account;

import model.Discount;
import model.ShoppingCart;
import model.log.TransactionLog;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Account {
    private ShoppingCart shoppingCart;
    private ArrayList<TransactionLog> transactionLogs;
    private HashMap<Discount, Integer> discounts;
    private int balance;

    public Customer(String accountId, String username, String password, String firstName, String lastName, String email, String phone, int balance) {
        super(accountId, username, password, firstName, lastName, email, phone);
        this.balance = balance;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public ArrayList<TransactionLog> getTransactionLogs() {
        return transactionLogs;
    }

    public HashMap<Discount, Integer> getDiscounts() {
        return discounts;
    }

    public void addBuyLog(TransactionLog transactionLog) {}

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void addDiscount(Discount discount) {}

    public void removeDiscount(Discount discount) {}

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String getType() {
        return "customer";
    }

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
