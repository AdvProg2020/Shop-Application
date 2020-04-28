package model;

import model.account.Seller;

import java.util.HashMap;

public class SubProduct {
    private static HashMap<String, SubProduct> allSubProducts = new HashMap<>();
    private String subProductId;
    private String productId;
    private String sellerId;
    private int price;
    private int remainingCount;
    private boolean suspended;

    public SubProduct(String productId, String sellerId, int price, int count) {
        this.subProductId = getNewId(productId, sellerId);
        this.productId = productId;
        this.sellerId = sellerId;
        this.price = price;
        remainingCount = count;
        suspended = false;
        allSubProducts.put(subProductId, this);
        getSeller().addSubProduct(subProductId);
        getProduct().addSubProduct(subProductId);
    }

    private static String getNewId(String productId, String sellerId) {
        //TODO: implement
        return null;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void changeRemainingCount(int changeAmount) {
        remainingCount += changeAmount;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }
}
