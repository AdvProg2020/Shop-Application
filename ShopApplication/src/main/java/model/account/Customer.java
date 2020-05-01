package model.account;

import model.Discount;
import model.ShoppingCart;
import model.log.BuyLog;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Account {
    private double balance;
    private String shoppingCartId; // can be null
    private ArrayList<String> buyLogIds;
    private HashMap<String, Integer> discountIds;

    public Customer(String username, String password, String firstName, String lastName, String email, String phone,
                    double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.balance = balance;
        shoppingCartId = null;
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!suspended) {
            buyLogIds = new ArrayList<>();
            discountIds = new HashMap<>();
        }
    }

    public static Customer getCustomerById(String accountId) {
        return (Customer) Account.getAccountById(accountId);
    }

    @Override
    public String getType() {
        return "customer";
    }

    public double getBalance() {
        return balance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
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

    public HashMap<Discount, Integer> getDiscounts() {
        HashMap<Discount, Integer> discounts = new HashMap<>();
        for (String discountId : discountIds.keySet()) {
            Discount discount = Discount.getDiscountById(discountId);
            int count = discountIds.get(discountId);
            if (discount == null) {
                discountIds.remove(discountId);
            } else {
                discounts.put(discount, count);
            }
        }
        return discounts;
    }

    public void addDiscount(String discountId, int count) {
        discountIds.put(discountId, count);
    }

    public void removeDiscount(String discountId) {
        discountIds.remove(discountId);
    }
}
