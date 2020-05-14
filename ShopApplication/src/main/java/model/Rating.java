package model;

import jdk.jfr.Label;
import model.account.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rating implements Initializable {
    private static Map<String, Rating> allRatings = new HashMap<>();
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

    public static List<Rating> getAllRatings() {
        return new ArrayList<>(allRatings.values());
    }

    public static Rating getRatingById(String ratingId) {
        return allRatings.get(ratingId);
    }

    @Override
    public void initialize() {
        if (ratingId == null) {
            ratingId = generateNewId(customerId, productId);
        }
        allRatings.put(ratingId, this);
        getProduct().addRating(ratingId);
    }

    @Label("Model internal use only!")
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
