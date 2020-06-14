package model.account;

import model.Cart;
import model.Discount;
import model.ModelUtilities;
import model.ModelUtilities.ModelOnly;
import model.log.BuyLog;

import java.util.*;


public class Customer extends Account {
    protected static Map<String, Customer> allCustomers = new HashMap<>();
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

    public static List<Customer> getAllCustomers(boolean... suspense) {
        return ModelUtilities.getAllInstances(allCustomers.values(), suspense);
    }

    public static Customer getCustomerById(String accountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allCustomers, accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allCustomers.put(accountId, this);
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

    @ModelOnly
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

    @ModelOnly
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

    @ModelOnly
    public void addDiscount(String discountId, int count) {
        discountIds.put(discountId, count);
    }

    @ModelOnly
    public void removeDiscount(String discountId) {
        discountIds.remove(discountId);
    }
}
