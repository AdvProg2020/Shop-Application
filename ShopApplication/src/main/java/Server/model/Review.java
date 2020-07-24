package Server.model;

import Server.model.ModelUtilities.ModelOnly;
import Server.model.account.Account;
import Server.model.database.Database;
import Server.model.request.AddReviewRequest;
import Server.model.sellable.Sellable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review implements ModelBasic {
    private static Map<String, Review> allReviews = new HashMap<>();
    private static int lastNum = 1;
    private String reviewId;
    private String reviewerId;
    private String sellableId;
    private String title;
    private String text;
    private boolean bought;

    public Review(String reviewerId, String sellableId, String title, String text, Database database) {
        this.reviewerId = reviewerId;
        this.sellableId = sellableId;
        this.title = title;
        this.text = text;
        setBought();
        new AddReviewRequest(this).updateDatabase(database);
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

        getSellable().addReview(reviewId);
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

    public Sellable getSellable() {
        return Sellable.getSellableById(sellableId);
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
        bought = getSellable().hasBought(reviewerId);
    }
}
