package model;

import model.ModelUtilities.ModelOnly;
import model.account.Account;
import model.request.AddReviewRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review implements ModelBasic {
    private static Map<String, Review> allReviews = new HashMap<>();
    private static int lastNum = 1;
    private String reviewId;
    private String reviewerId;
    private String productId;
    private String title;
    private String text;
    private boolean bought;

    public Review(String reviewerId, String productId, String title, String text) {
        this.reviewerId = reviewerId;
        this.productId = productId;
        this.title = title;
        this.text = text;
        setBought();
        new AddReviewRequest(this);
    }

    public static List<Review> getAllReviews() {
        return ModelUtilities.getAllInstances(allReviews.values(), false);
    }

    public static Review getReviewById(String reviewerId) {
        return ModelUtilities.getInstanceById(allReviews, reviewerId, false);
    }

    @Override
    public void initialize() {
        if (reviewId == null)
            reviewId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allReviews.put(reviewId, this);
        lastNum++;

        getProduct().addReview(reviewId);
    }

    @ModelOnly
    public void terminate() {
        allReviews.remove(reviewId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return reviewId;
    }

    public Account getReviewer() {
        return Account.getAccountById(reviewerId, false);
    }

    public Product getProduct() {
        return Product.getProductById(productId);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean hasBought() {
        return bought;
    }

    private void setBought() {
        bought = false;
        for (SubProduct subProduct : getProduct().getSubProducts()) {
            if (subProduct.hasCustomerWithId(reviewerId))
                bought = true;
        }
    }
}
