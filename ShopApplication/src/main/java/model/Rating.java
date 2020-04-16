package model;

import model.account.Customer;

public class Rating {
    private Customer customer;
    private Product product;
    private int score;

    public Rating(Customer customer, Product product, int score) {
        this.customer = customer;
        this.product = product;
        this.score = score;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getScore() {
        return score;
    }

    public void addReviewToDatabase() {}

    public void loadDatabase() {}
}
