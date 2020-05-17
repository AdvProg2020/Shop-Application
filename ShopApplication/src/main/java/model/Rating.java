package model;

import jdk.jfr.Label;
import model.account.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rating implements ModelBasic {
    private static Map<String, Rating> allRatings = new HashMap<>();
    private static int lastNum = 1;
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

    public static List<Rating> getAllRatings() {
        return new ArrayList<>(allRatings.values());
    }

    public static Rating getRatingById(String ratingId) {
        return allRatings.get(ratingId);
    }

    @Override
    public void initialize() {
        if (ratingId == null)
            ratingId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
        allRatings.put(ratingId, this);
        lastNum++;

        getProduct().addRating(ratingId);
    }

    @Label("Model internal use only!")
    public void terminate() {
        allRatings.remove(ratingId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
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
