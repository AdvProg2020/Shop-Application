package model;

import model.ModelUtilities.ModelOnly;
import model.request.AddProductRequest;

import java.util.*;

public class Product implements ModelBasic {
    private static String DEFAULT_IMAGE_PATH = "/img/default-product-pic.png"; //TODO: set value
    private static Map<String, Product> allProducts = new HashMap<>();
    private static int lastNum = 1;
    private String productId;
    private String name;
    private String brand;
    private String infoText;
    private String imagePath;
    private int viewCount;
    private String categoryId;
    private Map<String, String> propertyValues;
    private transient Set<String> subProductIds;
    private transient Set<String> reviewIds;
    private transient Set<String> ratingIds;
    private boolean suspended;

    public Product(String name, String brand, String infoText, String imagePath, String categoryId, List<String> values, SubProduct subProduct) {
        this.name = name;
        this.brand = brand;
        this.infoText = infoText;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
        setPropertyValues(values);
        viewCount = 0;
        suspended = false;
        new AddProductRequest(this, subProduct);
    }

    public static List<Product> getAllProducts(boolean... suspense) {
        return ModelUtilities.getAllInstances(allProducts.values(), suspense);
    }

    public static Product getProductById(String productId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allProducts, productId, suspense);
    }

    public static List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        for (Product product : allProducts.values())
            if (!product.suspended && product.getName().equals(name)) {
                products.add(product);
            }

        return products;
    }

    public static Product getProductByNameAndBrand(String name, String brand) {
        for (Product product : getProductsByName(name)) {
            if (product.getBrand().equals(brand))
                return product;
        }

        return null;
    }

    @Override
    public void initialize() {
        if (productId == null)
            productId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allProducts.put(productId, this);
        lastNum++;
        fixImagePath();

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

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
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

    public String getImagePath() {
        fixImagePath();
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        fixImagePath();
    }

    private void fixImagePath() {
        //TODO: implement
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

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }

        return subProducts;
    }

    public SubProduct getSubProductOfSeller(String sellerId) {
        for (SubProduct subProduct : getSubProducts()) {
            if (subProduct.getSeller().getId().equals(sellerId))
                return subProduct;
        }

        return null;
    }

    public List<SubProduct> getSubProductsInSale() {
        List<SubProduct> subProducts = new ArrayList<>();
        for (SubProduct subProduct : getSubProducts()) {
            if (subProduct.getSale() == null)
                subProducts.add(subProduct);
        }

        return subProducts;
    }

    public boolean isSoldInStoreWithName(String storeName) {
        for (SubProduct subProduct : getSubProducts()) {
            if (subProduct.getSeller().getStoreName().equals(storeName))
                return true;
        }

        return false;
    }

    @ModelOnly
    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    @ModelOnly
    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public SubProduct getDefaultSubProduct() {
        SubProduct defaultSP = null;
        for (SubProduct subProduct : getSubProducts()) {
            if (defaultSP == null) defaultSP = subProduct;

            if (subProduct.getRemainingCount() > 0 && subProduct.getPriceWithSale() < defaultSP.getPriceWithSale())
                defaultSP = subProduct;
        }

        return defaultSP;
    }

    public String getPropertyValue(String property) {
        String value = propertyValues.get(property);
        if (value == null)
            value = "";

        return value;
    }

    private void setPropertyValues(List<String> values) {
        propertyValues = new HashMap<>();
        List<String> properties = getCategory().getProperties(false);
        for (int i = 0; i < values.size(); i++) {
            propertyValues.put(properties.get(i), values.get(i));
        }
    }

    public List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();
        for (String reviewId : reviewIds) {
            reviews.add(Review.getReviewById(reviewId));
        }
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

    //TODO: delete
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

    //TODO: delete
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

    //TODO: delete
    public int getTotalRemainingCount() {
        int total = 0;
        for (SubProduct subProduct : getSubProducts()) {
            total += subProduct.getRemainingCount();
        }
        return total;
    }
}
