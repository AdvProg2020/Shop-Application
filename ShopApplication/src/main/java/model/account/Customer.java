package model.account;

import model.ShoppingCart;
import model.log.BuyLog;

import java.util.ArrayList;

public class Customer extends Account {
    private String shoppingCartId;
    private ArrayList<String> buyLogIds;
    private int credit;

    public Customer(String username, String password, String firstName, String lastName, String email, String phone,
                    int credit) {
        super(username, password, firstName, lastName, email, phone);
        this.credit = credit;
        shoppingCartId = null;
        buyLogIds = new ArrayList<>();
    }

    public static Customer getCustomerById(String accountId) {
        return (Customer) Account.getAccountById(accountId);
    }

    @Override
    public String getType() {
        return "customer";
    }

    public ShoppingCart getShoppingCart() {
        return ShoppingCart.getShoppingCartById(shoppingCartId);
    }

    public void setShoppingCart(String shoppingCartId) {
        if (this.shoppingCartId != null) {
            ShoppingCart.mergeShoppingCarts(this.shoppingCartId, shoppingCartId);
        }
        this.shoppingCartId = shoppingCartId;
    }

    public ArrayList<BuyLog> getBuyLogs() {
        ArrayList<BuyLog> buyLogs = new ArrayList<>();
        for (String buyLogId : buyLogIds) {
            buyLogs.add(BuyLog.getBuyLogById(buyLogId));
        }
        return buyLogs;
    }

    public void addBuyLog(String buyLogId) {
        buyLogIds.add(buyLogId);
    }

    public int getCredit() {
        return credit;
    }

    public void changeCredit(int changeAmount) {
        credit += changeAmount;
    }
}
