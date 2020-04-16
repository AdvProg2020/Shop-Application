package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    private static HashMap<String, Product> allProducts = new HashMap<String, Product>();
    private String productId;
    private String name;
    private String brand;
    private Category category;
    private String specsText;
    private int viewCount;
    private ArrayList<SubProduct> subProducts;
    private ArrayList<SpecialProperty> specialProperties;
    private ArrayList<Review> reviews;
    private ArrayList<Rating> ratings;

    public Product(String productId, String name, String brand, int price, Category category, String specsText) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.specsText = specsText;
    }

    public static HashMap<String, Product> getAllProducts() {
        return allProducts;
    }

    public static void addValidProduct(Product product) {}

    public static void removeValidProduct(Product product) {}

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public ArrayList<SubProduct> getSubProducts() {
        return subProducts;
    }

    public ArrayList<SpecialProperty> getSpecialProperties() {
        return specialProperties;
    }

    public Category getCategory() {
        return category;
    }

    public String getSpecsText() {
        return specsText;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public double getAverageRating() {
        return 0;
    }

    public void addProductToDatabase() {}

    public void removeProductFromDatabase() {}

    public void loadDatabase() {}

    public void updateProductInDatabase(String name) {}

    public int getViewCount() {
        return viewCount;
    }

    public void addViewCount() {}
}
