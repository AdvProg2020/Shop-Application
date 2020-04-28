package model;

import model.account.Customer;

import java.util.HashMap;

public class Rating {
    private static HashMap<String, Rating> allRatings = new HashMap<>();
    private String ratingId;
    private String customerId;
    private String productId;
    private int score;

    public Rating(String customerId, String productId, int score) {
        ratingId = getNewId(customerId, productId);
        this.customerId = customerId;
        this.productId = productId;
        this.score = score;
        allRatings.put(ratingId, this);
        getProduct().addRating(ratingId);
    }

    private static String getNewId(String customerId, String productId) {
        //TODO: implement
        return null;
    }

    public static Rating getRatingById(String ratingId) {
        return allRatings.get(ratingId);
    }

    public String getRatingId() {
        return ratingId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId);
    }

    public Product getProduct() {
        return Product.getProductById(productId);
    }

    public int getScore() {
        return score;
    }
}
