package model;

import jdk.jfr.Label;
import model.account.Customer;
import model.account.Seller;
import model.request.AddProductRequest;

import java.util.*;

public class SubProduct implements Initializable {
    private static Map<String, SubProduct> allSubProducts = new HashMap<>();
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

    private static String generateNewId(String productId, String sellerId) {
        //TODO: implement
        return null;
    }

    public static List<SubProduct> getAllSubProducts() {
        return getAllSubProducts(true);
    }

    public static List<SubProduct> getAllSubProducts(boolean checkSuspense) {
        List<SubProduct> subProducts = new ArrayList<>(allSubProducts.values());
        if (checkSuspense)
            subProducts.removeIf(subProduct -> subProduct.suspended);

        return subProducts;
    }

    public static SubProduct getSubProductById(String subProductId) {
        return getSubProductById(subProductId, true);
    }

    public static SubProduct getSubProductById(String subProductId, boolean checkSuspense) {
        SubProduct subProduct = allSubProducts.get(subProductId);
        if (checkSuspense && subProduct != null && subProduct.suspended)
            subProduct = null;

        return subProduct;
    }

    @Override
    public void initialize() {
        if (subProductId == null)
            subProductId = generateNewId(productId, sellerId);
        allSubProducts.put(subProductId, this);
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

    public String getId() {
        return subProductId;
    }

    public Product getProduct() {
        return Product.getProductById(productId, false);
    }


    @Label("Model internal use only!")
    public void setProductId(String productId) { // only used for accepting productRequest
        if (this.productId == null)
            this.productId = productId;
    }

    public Seller getSeller() {
        return Seller.getSellerById(sellerId, false);
    }

    public Sale getSale() {
        return Sale.getSaleById(saleId);
    }

    @Label("Model internal use only!")
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
        if (saleId == null)
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

    @Label("Model internal use only!")
    public void addCustomer(String customerId) {
        customerIds.add(customerId);
    }
}
