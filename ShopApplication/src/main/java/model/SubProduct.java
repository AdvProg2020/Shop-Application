package model;

import model.ModelUtilities.ModelOnly;
import model.account.Customer;
import model.account.Seller;
import model.request.AddProductRequest;

import java.util.*;

public class SubProduct implements ModelBasic {
    private static Map<String, SubProduct> allSubProducts = new HashMap<>();
    private static int lastNum = 1;
    private String subProductId;
    private String productId;
    private String sellerId;
    private double price;
    private int remainingCount;
    private transient String saleId; //can be null
    private transient Set<String> customerIds;
    private boolean suspended;

    public SubProduct(String productId, String sellerId, double price, int count) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.price = price;
        remainingCount = count;
        saleId = null;
        suspended = false;
        if (productId != null)
            new AddProductRequest(null, this);
    }

    public static List<SubProduct> getAllSubProducts(boolean... suspense) {
        return ModelUtilities.getInstances(allSubProducts.values(), suspense);
    }

    public static SubProduct getSubProductById(String subProductId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSubProducts, subProductId, suspense);
    }

    @Override
    public void initialize() {
        if (subProductId == null)
            subProductId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSubProducts.put(subProductId, this);
        lastNum++;

        customerIds = new HashSet<>();
        if (!suspended) {
            getSeller().addSubProduct(subProductId);
            getProduct().addSubProduct(subProductId);
        }
    }

    public void suspend() {
        getSeller().removeSubProduct(subProductId);
        getProduct().removeSubProduct(subProductId);
        setSale(null);
        Cart.updateSubProducts();
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public String getId() {
        return subProductId;
    }

    public Product getProduct(boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)
        return Product.getProductById(productId, checkSuspense);
    }

    @ModelOnly
    public void setProductId(String productId) { // only used for accepting productRequest
        if (this.productId == null)
            this.productId = productId;
    }

    public Seller getSeller(boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)
        return Seller.getSellerById(sellerId, checkSuspense);
    }

    public Sale getSale() {
        return Sale.getSaleById(saleId);
    }

    @ModelOnly
    public void setSale(String saleId) {
        if (this.saleId != null)
            getSale().removeSubProduct(subProductId);
        this.saleId = saleId;
    }

    public double getRawPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceWithSale() {
        if (getSale() == null)
            return price;

        double saleAmount = price * getSale().getPercentage() / 100;
        double maximumAmount = getSale().getMaximumAmount();
        if (saleAmount > maximumAmount)
            saleAmount = maximumAmount;

        return price - saleAmount;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void changeRemainingCount(int changeAmount) {
        remainingCount += changeAmount;
        if (remainingCount < 0)
            remainingCount = 0;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (String customerId : customerIds) {
            customers.add(Customer.getCustomerById(customerId, false));
        }

        return customers;
    }

    public boolean hasCustomerWithId(String customerId) {
        return customerIds.contains(customerId);
    }

    @ModelOnly
    public void addCustomer(String customerId) {
        customerIds.add(customerId);
    }
}
