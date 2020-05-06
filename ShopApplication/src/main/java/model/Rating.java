package model;

import model.account.Customer;

import java.util.HashMap;

public class Rating {
    private static HashMap<String, Rating> allRatings = new HashMap<>();
    private String ratingId;
    private String customerId;
    private String productId;
    private int score; // 1 - 5

    public Rating(String customerId, String productId, int score) {
        this.customerId = customerId;
        this.productId = productId;
        this.score = score;
        initialize();
    }

    private static String generateNewId(String customerId, String productId) {
        //TODO: implement
        return null;
    }

    public static Rating getRatingById(String ratingId) {
        return allRatings.get(ratingId);
    }

    public void initialize() {
        if (ratingId == null) {
            ratingId = generateNewId(customerId, productId);
        }
        allRatings.put(ratingId, this);
        getProduct().addRating(ratingId);
    }

    public void terminate() {
        allRatings.remove(ratingId);
    }

    public String getId() {
        return ratingId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId, false);
    }

    public Product getProduct() {
        return Product.getProductById(productId);
    }

    public int getScore() {
        return score;
    }
}
