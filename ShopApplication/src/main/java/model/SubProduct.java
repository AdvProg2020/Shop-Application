package model;

import model.account.Seller;

import java.util.HashMap;

public class SubProduct {
    private static HashMap<String, SubProduct> allSubProducts = new HashMap<String, SubProduct>();
    private String subProductId;
    private Product product;
    private Seller seller;
    private int price;
    private int remainingCount;
    private boolean suspended;

    public SubProduct(Product product, Seller seller, int price, int remainingCount) {
        this.product = product;
        this.seller = seller;
        this.price = price;
        this.remainingCount = remainingCount;
    }

    public Product getProduct() {
        return product;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getPrice() {
        return price;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }
}
