package model;

import model.account.Seller;

public class SubProduct {
    private String subProductId;
    private Product product;
    private Seller seller;
    private int price;
    private int remainingCount;

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
}
