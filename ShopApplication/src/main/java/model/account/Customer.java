package model.account;

import model.Discount;
import model.ShoppingCart;
import model.log.BuyLog;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Account {
    private ShoppingCart shoppingCart;
    private ArrayList<BuyLog> buyLogs;
    private HashMap<Discount, Integer> discounts;
    private int balance;

    public Customer(String accountId, String username, String password, String firstName, String lastName, String email, String phone, int balance) {
        super(accountId, username, password, firstName, lastName, email, phone);
        this.balance = balance;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<BuyLog> getBuyLogs() {
        return buyLogs;
    }

    public HashMap<Discount, Integer> getDiscounts() {
        return discounts;
    }

    public void addBuyLog(BuyLog buyLog) {
    }

    public void addDiscount(Discount discount) {
    }

    public void removeDiscount(Discount discount) {
    }

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
