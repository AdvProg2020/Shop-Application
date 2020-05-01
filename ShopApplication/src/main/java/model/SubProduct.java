package model;

import model.account.Customer;
import model.account.Seller;

import java.util.ArrayList;
import java.util.HashMap;

public class SubProduct {
    private static HashMap<String, SubProduct> allSubProducts = new HashMap<>();
    private String subProductId;
    private String productId;
    private String sellerId;
    private String saleId; //can be null
    private double price;
    private int remainingCount;
    private ArrayList<String> customerIds;
    private boolean suspended;

    public SubProduct(String productId, String sellerId, double price, int count) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.price = price;
        remainingCount = count;
        saleId = null;
        suspended = false;
    }

    private static String generateNewId(String productId, String sellerId) {
        //TODO: implement
        return null;
    }

    public void initialize() {
        if (subProductId == null) {
            subProductId = generateNewId(productId, sellerId);
        }
        allSubProducts.put(subProductId, this);
        if (!suspended) {
            customerIds = new ArrayList<>();
            getSeller().addSubProduct(subProductId);
            getProduct().addSubProduct(subProductId);
        }
    }

    public void suspend() {
        getSeller().removeSubProduct(subProductId);
        getProduct().removeSubProduct(subProductId);
        //TODO: remove from sale?
        suspended = true;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public static SubProduct getSubProductById(String subProductId) {
        return allSubProducts.get(subProductId);
    }

    public String getSubProductId() {
        return subProductId;
    }

    public Product getProduct() {
        return Product.getProductById(productId);
    }

    public Seller getSeller() {
        return Seller.getSellerById(sellerId);
    }

    public Sale getSale() {
        return Sale.getSaleById(saleId);
    }

    public void setSale(String saleId) {
        if (getSale() != null) {
            getSale().removeSubProduct(subProductId);
        }
        this.saleId = saleId;
        getSale().addSubProduct(subProductId);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceWithSale() {
        double saleAmount = price * getSale().getPercentage() / 100;
        double maximumAmount = getSale().getMaximumAmount();
        if (saleAmount > maximumAmount) {
            saleAmount = maximumAmount;
        }
        return price - saleAmount;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void changeRemainingCount(int changeAmount) {
        remainingCount += changeAmount;
    }

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (String customerId : new ArrayList<>(customerIds)) {
            Customer customer = Customer.getCustomerById(customerId);
            if (customer == null) {
                customerIds.remove(customerId);
            } else {
                customers.add(customer);
            }
        }
        return customers;
    }

    public void addCustomer(String customerId) {
        customerIds.add(customerId);
    }

    public boolean hasCustomerWithId(String customerId) {
        return customerIds.contains(customerId);
    }
}
