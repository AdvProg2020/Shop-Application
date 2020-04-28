package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    private static HashMap<String, Product> allProducts = new HashMap<>();
    private String productId;
    private String name;
    private String brand;
    private String infoText;
    private int viewCount;
    private String categoryId;
    private ArrayList<String> specialProperties;
    private ArrayList<String> subProductIds;
    private ArrayList<String> reviewIds;
    private ArrayList<String> ratingIds;
    private boolean suspended;

    public Product(String name, String brand, String infoText, String categoryId, ArrayList<String> specialProperties) {
        productId = getNewId();
        this.name = name;
        this.brand = brand;
        this.infoText = infoText;
        setCategory(categoryId);
        this.specialProperties = specialProperties;
        viewCount = 0;
        subProductIds = new ArrayList<>();
        reviewIds = new ArrayList<>();
        ratingIds = new ArrayList<>();
        suspended = false;
        allProducts.put(productId, this);
        getCategory().addProduct(productId);
    }

    private static String getNewId() {
        //TODO: implement
        return null;
    }

    public static ArrayList<Product> getAllProducts() {
        return (ArrayList<Product>) allProducts.values();
    }

    public static Product getProductById(String productId) {
        return allProducts.get(productId);
    }

    public static Product getProductByName(String name) {
        for (Product product : allProducts.values()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getInfoText() {
        return infoText;
    }

    public Category getCategory() {
        return Category.getCategoryById(categoryId);
    }

    public void setCategory(String categoryId) {
        if (categoryId != null) {
            getCategory().removeProduct(productId);
        }
        this.categoryId = categoryId;
        getCategory().addProduct(productId);
    }

    public ArrayList<SubProduct> getSubProducts() {
        ArrayList<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }
        return subProducts;
    }

    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    public ArrayList<String> getSpecialProperties() {
        return new ArrayList<>(specialProperties);
    }

    public ArrayList<Review> getReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        for (String reviewId : reviewIds) {
            reviews.add(Review.getReviewById(reviewId));
        }
        return reviews;
    }

    public void addReview(String reviewId) {
        reviewIds.add(reviewId);
    }

    public double getAverageRating() {
        if (ratingIds.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (String ratingId : ratingIds) {
            sum += Rating.getRatingById(ratingId).getScore();
        }
        return sum / ratingIds.size();
    }

    public void addRating(String ratingId) {
        ratingIds.add(ratingId);
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
        for (String subProductId : subProductIds) {
            SubProduct.getSubProductById(subProductId).suspend();
        }
    }

    public int getViewCount() {
        return viewCount;
    }

    public void addViewCount() {
        viewCount++;
    }
}
