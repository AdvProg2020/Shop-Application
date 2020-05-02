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
        this.reviewerId = reviewerId;
        this.productId = productId;
        this.reviewText = reviewText;
        setBought();

    }

    private static String generateNewId(String reviewerId, String productId) {
        //TODO: implement
        return null;
    }

    public static Review getReviewById(String reviewId) {
        return allReviews.get(reviewId);
    }

    public void initialize() {
        if (reviewId == null) {
            reviewId = generateNewId(reviewerId, productId);
        }
        allReviews.put(reviewId, this);
        getProduct().addReview(reviewId);
    }

    public void terminate() {
        allReviews.remove(reviewId);
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
        bought = false;
        for (SubProduct subProduct : getProduct().getSubProducts()) {
            if (subProduct.hasCustomerWithId(reviewerId)) {
                bought = true;
            }
        }
    }
}
