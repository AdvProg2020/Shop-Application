package model;

import model.account.Account;

public class Review {
    private Account reviewer;
    private Product product;
    private String reviewText;
    private boolean bought;

    public Review(Account reviewer, Product product, String reviewText, boolean bought) {
        this.reviewer = reviewer;
        this.product = product;
        this.reviewText = reviewText;
        this.bought = bought;
    }

    public Account getReviewer() {
        return reviewer;
    }

    public Product getProduct() {
        return product;
    }

    public String getReviewText() {
        return reviewText;
    }

    public boolean hasBought() {
        return bought;
    }

    private void addRatingToDatabase() {
    }

    private void loadDatabase() {
    }
}
