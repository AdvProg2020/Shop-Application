package model;

import jdk.jfr.Label;
import model.request.AddProductRequest;

import java.util.*;

public class Product implements Initializable {
    private static Map<String, Product> allProducts = new HashMap<>();
    private String productId;
    private String name;
    private String brand;
    private String infoText;
    private int viewCount;
    private String categoryId;
    private List<String> specialProperties;
    private transient Set<String> subProductIds;
    private transient Set<String> reviewIds;
    private transient Set<String> ratingIds;
    private boolean suspended;

    public Product(String name, String brand, String infoText, String categoryId, ArrayList<String> specialProperties, SubProduct subProduct) {
        this.name = name;
        this.brand = brand;
        this.infoText = infoText;
        this.categoryId = categoryId;
        this.specialProperties = specialProperties;
        viewCount = 0;
        suspended = false;
        new AddProductRequest(this, subProduct);
    }

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static List<Product> getAllProducts() {
        return getAllProducts(true);
    }

    public static List<Product> getAllProducts(boolean checkSuspense) {
        List<Product> products = new ArrayList<>(allProducts.values());
        if (checkSuspense) {
            products.removeIf(product -> product.suspended);
        }
        return products;
    }

    public static Product getProductById(String productId) {
        return getProductById(productId, true);
    }

    public static Product getProductById(String productId, boolean checkSuspense) {
        Product product = allProducts.get(productId);
        if (checkSuspense && product != null && product.suspended) {
            product = null;
        }
        return product;
    }

    public static List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        for (Product product : allProducts.values()) {
            if (!product.suspended && product.getName().equals(name)) {
                products.add(product);
            }
        }
        return products;
    }

    public static Product getProductByNameAndBrand(String name, String brand) {
        for (Product product : getProductsByName(name)) {
            if (product.getBrand().equals(brand)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public void initialize() {
        if (productId == null) {
            productId = generateNewId();
        }
        allProducts.put(productId, this);
        if (!suspended) {
            subProductIds = new HashSet<>();
            reviewIds = new HashSet<>();
            ratingIds = new HashSet<>();
            getCategory().addProduct(productId);
        }
    }

    public void suspend() {
        for (SubProduct subProduct : getSubProducts()) {
            subProduct.suspend();
        }
        subProductIds = null;
        for (Review review : getReviews()) {
            review.terminate();
        }
        reviewIds = null;
        for (Rating rating : getRatings()) {
            rating.terminate();
        }
        ratingIds = null;
        getCategory().removeProduct(productId);
        suspended = true;
    }

    public String getId() {
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

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }

        return subProducts;
    }

    public SubProduct getSubProductWithSellerId(String sellerId) {
        for (SubProduct subProduct : getSubProducts()) {
            if (subProduct.getSeller().getId().equals(sellerId))
                return subProduct;
        }

        return null;
    }

    @Label("Model internal use only!")
    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    @Label("Model internal use only!")
    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public List<String> getSpecialProperties() {
        return new ArrayList<>(specialProperties);
    }

    public List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();
        for (String reviewId : reviewIds) {
            reviews.add(Review.getReviewById(reviewId));
        }
        return reviews;
    }

    @Label("Model internal use only!")
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

    @Label("Model internal use only!")
    public void addRating(String ratingId) {
        ratingIds.add(ratingId);
    }

    public double getMinPrice() {
        double minimum = Double.MAX_VALUE;
        for (SubProduct subProduct : getSubProducts()) {
            double toCompare = subProduct.getPriceWithSale();
            if (toCompare < minimum) {
                minimum = toCompare;
            }
        }
        return minimum;
    }

    public double getMaxPrice() {
        double maximum = 0.0;
        for (SubProduct subProduct : getSubProducts()) {
            double toCompare = subProduct.getPriceWithSale();
            if (toCompare > maximum) {
                maximum = toCompare;
            }
        }
        return maximum;
    }

    public int getTotalRemainingCount() {
        int total = 0;
        for (SubProduct subProduct : getSubProducts()) {
            total += subProduct.getRemainingCount();
        }
        return total;
    }
}
