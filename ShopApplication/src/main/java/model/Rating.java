package model;

import model.ModelUtilities.ModelOnly;
import model.account.Customer;
import model.sellable.Sellable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rating implements ModelBasic {
    private static Map<String, Rating> allRatings = new HashMap<>();
    private static int lastNum = 1;
    private String ratingId;
    private String customerId;
    private String sellableId;
    private int score; // 1 - 5

    public Rating(String customerId, String sellableId, int score) {
        this.sellableId = sellableId;
        this.customerId = customerId;
        this.score = score;
        initialize();
    }

    public static List<Rating> getAllRatings() {
        return ModelUtilities.getAllInstances(allRatings.values(), false);
    }

    public static Rating getRatingById(String ratingId) {
        return ModelUtilities.getInstanceById(allRatings, ratingId, false);
    }

    @Override
    public void initialize() {
        if (ratingId == null)
            ratingId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allRatings.put(ratingId, this);
        lastNum++;

        Rating oldRating = getSellable().getRatingByCustomerId(customerId);
        if (oldRating != null)
            getSellable().removeRating(oldRating.getId());

        getSellable().addRating(ratingId);

    }

    @ModelOnly
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

    public int getScore() {
        return score;
    }

    @ModelOnly
    public Sellable getSellable() {
        return Sellable.getSellableById(sellableId);
    }
}
