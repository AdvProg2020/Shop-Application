package model.sellable;

import model.*;
import model.ModelUtilities.ModelOnly;
import model.database.Database;

import java.util.*;

public abstract class Sellable implements ModelBasic {
    private static Map<String, Sellable> allSellables = new HashMap<>();
    protected String sellableId;
    protected String name;
    protected String infoText;
    protected String imagePath;
    protected int viewCount;
    protected String categoryId;
    protected Map<String, String> propertyValues;
    protected transient Set<String> subSellableIds;
    protected transient Set<String> reviewIds;
    protected transient Set<String> ratingIds;
    protected boolean suspended;

    public Sellable(String name, String infoText, String imagePath, String categoryId, Map<String, String> propertyValues, SubSellable subSellable, Database database) {
        this.name = name;
        this.infoText = infoText;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
        this.propertyValues = propertyValues;
        viewCount = 0;
        suspended = false;
    }

    public static List<Sellable> getAllSellables(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSellables.values(), suspense);
    }

    public static Sellable getSellableById(String sellableId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSellables, sellableId, suspense);
    }

    public static List<Sellable> getSellablesByName(String name) {
        List<Sellable> sellables = new ArrayList<>();
        for (Sellable sellable : allSellables.values()) {
            if (!sellable.suspended && sellable.getName().equals(name))
                sellables.add(sellable);
        }

        return sellables;
    }

    @Override
    public void initialize() {
        allSellables.put(sellableId, this);

        if (!suspended) {
            subSellableIds = new HashSet<>();
            reviewIds = new HashSet<>();
            ratingIds = new HashSet<>();
            getCategory().addSellable(sellableId);
            setPropertyValues(propertyValues);
        }
    }

    public void suspend() {
        for (SubSellable subSellable : getSubSellables()) {
            subSellable.suspend();
        }
        subSellableIds = null;
        for (Review review : getReviews()) {
            review.terminate();
        }
        reviewIds = null;
        for (Rating rating : getRatings()) {
            rating.terminate();
        }
        ratingIds = null;
        getCategory().removeSellable(sellableId);
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public String getId() {
        return sellableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    protected abstract String getDefaultImagePath();

    public String getImagePath() {
        return ModelUtilities.fixedPath(imagePath, getDefaultImagePath());
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public Category getCategory() {
        return Category.getCategoryById(categoryId);
    }

    public ArrayList<SubSellable> getSubSellables() {
        ArrayList<SubSellable> subSellables = new ArrayList<>();
        for (String subSellableId : subSellableIds) {
            subSellables.add(SubSellable.getSubSellableById(subSellableId));
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    public SubSellable getSubSellableOfSeller(String sellerId) {
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getSeller().getId().equals(sellerId))
                return subSellable;
        }

        return null;
    }

    public List<SubSellable> getSubSellablesInSale() {
        List<SubSellable> subSellables = new ArrayList<>();
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getSale() != null)
                subSellables.add(subSellable);
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    public List<SubSellable> getSubSellablesInAuction() {
        List<SubSellable> subSellables = new ArrayList<>();
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getAuction() != null)
                subSellables.add(subSellable);
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }



    public boolean isSoldInStoreWithName(String storeName) {
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getSeller().getStoreName().equals(storeName))
                return true;
        }

        return false;
    }

    public boolean hasBought(String customerId) {
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.hasCustomerWithId(customerId))
                return true;
        }
        return false;
    }

    @ModelOnly
    public void addSubSellable(String subSellableId) {
        subSellableIds.add(subSellableId);
    }

    @ModelOnly
    public void removeSubSellable(String subSellableId) {
        subSellableIds.remove(subSellableId);
    }

    public SubSellable getDefaultSubSellable() {
        SubSellable defaultSS = null;
        for (SubSellable subSellable : getSubSellables()) {
            if (defaultSS == null) defaultSS = subSellable;

            if (subSellable.isAvailable() && subSellable.getPriceWithSale() < defaultSS.getPriceWithSale())
                defaultSS = subSellable;
        }

        return defaultSS;
    }

    public String getValue(String property) {
        String value = propertyValues.get(property);
        if (value == null)
            value = "";

        return value;
    }

    public Map<String, String> getPropertyValues() {
        return new HashMap<>(propertyValues);
    }

    private void setPropertyValues(Map<String, String> values) {
        propertyValues = new HashMap<>();
        List<String> properties = getCategory().getProperties(true);
        for (String property : properties) {
            propertyValues.put(property, values.getOrDefault(property, ""));
        }
    }

    public void setProperty(String property, String value) {
        if (!propertyValues.containsKey(property)) return;

        propertyValues.replace(property, value);
    }

    @ModelOnly
    public void addProperty(String property) {
        propertyValues.put(property, "");
    }

    @ModelOnly
    public void removeProperty(String property) {
        propertyValues.remove(property);
    }

    public List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();
        for (String reviewId : reviewIds) {
            reviews.add(Review.getReviewById(reviewId));
        }
        reviews.sort(Comparator.comparing(Review::getId));
        return reviews;
    }

    @ModelOnly
    public void addReview(String reviewId) {
        reviewIds.add(reviewId);
    }

    public List<Rating> getRatings() {
        List<Rating> ratings = new ArrayList<>();
        for (String ratingId : ratingIds) {
            ratings.add(Rating.getRatingById(ratingId));
        }
        return ratings;
    }

    @ModelOnly
    public void addRating(String ratingId) {
        ratingIds.add(ratingId);
    }

    @ModelOnly
    public void removeRating(String ratingId) {
        ratingIds.remove(ratingId);
        Rating.getRatingById(ratingId).terminate();
    }

    public Rating getRatingByCustomerId(String customerId) {
        for (Rating rating : getRatings()) {
            if (rating.getCustomer().getId().equals(customerId))
                return rating;
        }

        return null;
    }

    public double getAverageRatingScore() {
        if (ratingIds.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (String ratingId : ratingIds) {
            sum += Rating.getRatingById(ratingId).getScore();
        }
        return sum / ratingIds.size();
    }

    public int getRatingsCount() {
        return ratingIds.size();
    }

    //TODO: delete?
    public double getMinPrice() {
        double minimum = Double.MAX_VALUE;
        for (SubSellable subSellable : getSubSellables()) {
            double toCompare = subSellable.getPriceWithSale();
            if (toCompare < minimum) {
                minimum = toCompare;
            }
        }
        return minimum;
    }

    //TODO: delete?
    public double getMaxPrice() {
        double maximum = 0.0;
        for (SubSellable subSellable : getSubSellables()) {
            double toCompare = subSellable.getPriceWithSale();
            if (toCompare > maximum) {
                maximum = toCompare;
            }
        }
        return maximum;
    }


}
