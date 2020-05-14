package model;

import jdk.jfr.Label;
import model.account.Account;
import model.request.AddReviewRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review implements Initializable {
    private static Map<String, Review> allReviews = new HashMap<>();
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

    private static String generateNewId(String reviewerId, String productId) {
        //TODO: implement
        return null;
    }

    public static List<Review> getAllReviews() {
        return new ArrayList<>(allReviews.values());
    }

    public static Review getReviewById(String reviewerId) {
        return allReviews.get(reviewerId);
    }

    @Override
    public void initialize() {
        if (reviewId == null)
            reviewId = generateNewId(reviewerId, productId);
        allReviews.put(reviewId, this);
        getProduct().addReview(reviewId);
    }

    @Label("Model internal use only!")
    public void terminate() {
        allReviews.remove(reviewId);
    }

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
