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
    private boolean suspended;

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

    public static Product getProductById(String productId){
        return null;
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

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
        //todo : suspend all subProducts
    }

    public int getViewCount() {
        return viewCount;
    }

    public void addViewCount() {
        viewCount ++;
    }

    private void addProductToDatabase() {
    }

    private void removeProductFromDatabase() {
    }

    private void loadDatabase() {
    }

    private void updateProductInDatabase(String name) {
    }


}
