package model;

import model.account.Account;

import java.util.HashMap;

public class Review {
    private static HashMap<String, Review> allReviews = new HashMap<>();

    private String reviewId;
    private String reviewerId;
    private String productId;
    private String reviewText;
    private boolean bought;

    public Review(String reviewerId, String productId, String reviewText) {
        reviewId = getNewId(reviewerId, productId);
        this.reviewerId = reviewerId;
        this.productId = productId;
        this.reviewText = reviewText;
        setBought();
        getProduct().addReview(reviewId);
        allReviews.put(reviewId, this);
    }

    private static String getNewId(String reviewerId, String productId) {
        //TODO: implement
        return null;
    }

    public static Review getReviewById(String reviewId) {
        return allReviews.get(reviewId);
    }

    public String getReviewId() {
        return reviewId;
    }

    public Account getReviewer() {
        return Account.getAccountById(reviewerId);
    }

    public Product getProduct() {
        return Product.getProductById(productId);
    }

    public String getReviewText() {
        return reviewText;
    }

    public boolean hasBought() {
        return bought;
    }

    private void setBought() {
        // TODO: implement
    }
}
