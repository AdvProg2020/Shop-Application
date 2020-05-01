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
    private transient ArrayList<String> subProductIds;
    private transient ArrayList<String> reviewIds;
    private transient ArrayList<String> ratingIds;
    private boolean suspended;

    public Product(String name, String brand, String infoText, String categoryId, ArrayList<String> specialProperties) {
        this.name = name;
        this.brand = brand;
        this.infoText = infoText;
        this.categoryId = categoryId;
        this.specialProperties = specialProperties;
        viewCount = 0;
        suspended = false;
    }

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Product product : allProducts.values()) {
            if (!product.suspended) {
                products.add(product);
            }
        }
        return products;
    }

    public static Product getProductByName(String name) {
        for (Product product : allProducts.values()) {
            if (!product.suspended && product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public static Product getProductById(String productId) {
        return allProducts.get(productId);
    }

    public void initialize() {
        if (productId == null) {
            productId = generateNewId();
        }
        allProducts.put(productId, this);
        if (!suspended) {
            subProductIds = new ArrayList<>();
            reviewIds = new ArrayList<>();
            ratingIds = new ArrayList<>();
            getCategory().addProduct(productId);
        }
    }

    public void suspend() {
        for (String subProductId : subProductIds) {
            SubProduct.getSubProductById(subProductId).suspend();
        }
        getCategory().removeProduct(productId);
        suspended = true;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
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

    public void setCategory(String categoryId) {
        getCategory().removeProduct(productId);
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

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
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
}
