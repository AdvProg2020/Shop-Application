package model;

import model.account.Account;
import model.request.reviewRequest;

public class Review {
    private Account reviewer;
    private Product product;
    private String reviewText;
    private boolean hasBought;

    public Review(Account reviewer, Product product, String reviewText, boolean hasBought) {
        this.reviewer = reviewer;
        this.product = product;
        this.reviewText = reviewText;
        this.hasBought = hasBought;
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

    public boolean isHasBought() {
        return hasBought;
    }
}
