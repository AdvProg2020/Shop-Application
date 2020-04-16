package model;

import model.account.Account;
import model.request.reviewRequest;

public class Review {
    private Account reviewer;
    private Product product;
    private String reviewText;
    private reviewRequest.ReviewStatus reviewStatus;
    private boolean hasBought;

    public Review(Account reviewer, Product product, String reviewText, boolean hasBought) {
        this.reviewer = reviewer;
        this.product = product;
        this.reviewText = reviewText;
        this.reviewStatus = reviewRequest.ReviewStatus.pending;
        this.hasBought = hasBought;
    }

    public void setReviewStatus(reviewRequest.ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
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

    public reviewRequest.ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public boolean isHasBought() {
        return hasBought;
    }
}
