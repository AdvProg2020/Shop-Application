package model;

import jdk.jfr.Label;

import java.util.*;

public class Category {
    private static Map<String, Category> allCategories = new HashMap<>();
    private static Category superCategory = null; // parent of all main categories (shouldn't be terminated)
    private String categoryId;
    private String name;
    private String parentId;
    private List<String> specialProperties;
    private transient Set<String> productIds;
    private transient Set<String> subCategoryIds;

    public Category(String name, String parentId, ArrayList<String> specialProperties) {
        this.name = name;
        this.parentId = parentId;
        this.specialProperties = specialProperties;
        initialize();
    }

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static Category getSuperCategory() {
        return superCategory;
    }

    public static void setSuperCategory() {
        if (superCategory == null)
            new Category(null, null, null);
    }

    public static List<Category> getAllCategories() {
        return new ArrayList<>(allCategories.values());
    }

    public static Category getCategoryById(String categoryId) {
        return allCategories.get(categoryId);
    }

    public static Category getCategoryByName(String name) {
        for (Category category : allCategories.values()) {
            if (category.getName().equals(name))
                return category;
        }

        return null;
    }

    public void initialize() {
        subCategoryIds = new HashSet<>();
        productIds = new HashSet<>();
        if (superCategory == null) {
            superCategory = this;
        } else {
            if (categoryId == null)
                categoryId = generateNewId();
            allCategories.put(categoryId, this);
            getParent().addSubCategory(categoryId);
        }
    }

    public void terminate() {
        for (Category subCategory : getSubCategories()) {
            subCategory.terminate();
        }
        for (Product product : getProducts()) {
            product.setCategory(parentId);
        }
        getParent().removeSubCategory(categoryId);
        allCategories.remove(categoryId);
    }

    public String getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSpecialProperties() {
        return new ArrayList<>(specialProperties);
    }

    public Category getParent() {
        return Category.getCategoryById(parentId);
    }

    public void setParent(String parentId) {
        // first check if new parent is a child of the category -> !hasSubCategoryWithId(parentId)
        getParent().removeSubCategory(categoryId);
        this.parentId = parentId;
        getParent().addSubCategory(categoryId);
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        for (Category subCategory : getSubCategories()) {
            products.addAll(subCategory.getProducts());
        }
        for (String productId : productIds) {
            products.add(Product.getProductById(productId));
        }

        return products;
    }

    @Label("Model internal use only!")
    public void addProduct(String productId) {
        productIds.add(productId);
    }

    @Label("Model internal use only!")
    public void removeProduct(String productId) {
        productIds.remove(productId);
    }

    public List<Category> getSubCategories() {
        List<Category> categories = new ArrayList<>();
        for (String subCategoryId : subCategoryIds) {
            categories.add(Category.getCategoryById(subCategoryId));
        }

        return categories;
    }

    public boolean hasSubCategoryWithId(String categoryId) {
        for (Category subCategory : getSubCategories()) {
            if (categoryId.equals(subCategory.getId()) || subCategory.hasSubCategoryWithId(categoryId))
                return true;
        }

        return false;
    }

    @Label("Model internal use only!")
    public void addSubCategory(String subCategoryId) {
        subCategoryIds.add(subCategoryId);
    }

    @Label("Model internal use only!")
    public void removeSubCategory(String subCategoryId) {
        subCategoryIds.remove(subCategoryId);
    }
}
