package model.account;

import jdk.jfr.Label;
import model.BasicMethods;
import model.Cart;
import model.Discount;
import model.log.BuyLog;

import java.util.*;


public class Customer extends Account {
    private static int lastNum = 1;
    private double balance;
    private transient String cartId;
    private transient Map<String, Integer> discountIds;
    private transient Set<String> buyLogIds;

    public Customer(String username, String password, String firstName, String lastName, String email, String phone,
                    double balance) {
        super(username, password, firstName, lastName, email, phone);
        this.balance = balance;
        initialize();
        new Cart(accountId);
    }

    public static Customer getCustomerById(String accountId, boolean... suspense) {
        return (Customer) getAccountById(accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
        allAccounts.put(accountId, this);
        lastNum++;
        buyLogIds = new HashSet<>();
        if (!suspended) {
            discountIds = new HashMap<>();
        }
    }

    @Override
    public void suspend() {
        for (Discount discount : getDiscounts().keySet()) {
            discount.removeCustomer(accountId);
        }
        discountIds = null;
        setCart(null);
        super.suspend();
    }

    public double getBalance() {
        return balance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
    }

    public Cart getCart() {
        return Cart.getCartById(cartId);
    }

    @Label("Model internal use only!")
    public void setCart(String cartId) {
        if (this.cartId != null)
            getCart().terminate();
        this.cartId = cartId;
    }

    public void mergeCart(String cartId) {
        Cart.mergeCarts(cartId, this.cartId);
    }

    public List<BuyLog> getBuyLogs() {
        List<BuyLog> buyLogs = new ArrayList<>();
        for (String buyLogId : buyLogIds) {
            buyLogs.add(BuyLog.getBuyLogById(buyLogId));
        }

        return buyLogs;
    }

    @Label("Model internal use only!")
    public void addBuyLog(String buyLogId) {
        buyLogIds.add(buyLogId);
    }

    public Map<Discount, Integer> getDiscounts() {
        Map<Discount, Integer> discounts = new HashMap<>();
        for (Map.Entry<String, Integer> entry : discountIds.entrySet()) {
            Discount discount = Discount.getDiscountById(entry.getKey());
            discounts.put(discount, entry.getValue());
        }

        return discounts;
    }

    @Label("Model internal use only!")
    public void addDiscount(String discountId, int count) {
        discountIds.put(discountId, count);
    }

    @Label("Model internal use only!")
    public void removeDiscount(String discountId) {
        discountIds.remove(discountId);
    }
}
