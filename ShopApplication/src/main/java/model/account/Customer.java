package model.account;

import model.Discount;
import model.ShoppingCart;
import model.log.BuyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Customer extends Account {
    private double balance;
    private transient String shoppingCartId;
    private transient HashSet<String> buyLogIds;
    private transient HashMap<String, Integer> discountIds;

    public Customer(String username, String password, String firstName, String lastName, String email, String phone,
                    double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.balance = balance;
        new ShoppingCart(accountId);
        initialize();
    }

    public static Customer getCustomerById(String accountId) {
        return getCustomerById(accountId, true);
    }

    public static Customer getCustomerById(String accountId, boolean checkSuspense) {
        Customer customer = (Customer) allAccounts.get(accountId);
        if (checkSuspense && customer != null && customer.suspended) {
            customer = null;
        }
        return customer;
    }

    @Override
    public void initialize() {
        super.initialize();
        buyLogIds = new HashSet<>();
        if (!suspended) {
            discountIds = new HashMap<>();
        }
    }

    @Override
    public void suspend() {
        getShoppingCart().terminate();
        for (Discount discount : getDiscounts().keySet()) {
            discount.removeCustomer(accountId);
        }
        shoppingCartId = null;
        discountIds = null;
        super.suspend();
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
            getShoppingCart().terminate();
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
            discounts.put(discount, count);
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
