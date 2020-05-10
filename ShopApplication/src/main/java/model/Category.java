package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Category {
    private static HashMap<String, Category> allCategories = new HashMap<>();
    private static Category superCategory;
    private String categoryId;
    private String name;
    private String parentId;
    private ArrayList<String> specialProperties;
    private transient HashSet<String> productIds;
    private transient HashSet<String> subCategoryIds;

    public Category(String name, String parentId, ArrayList<String> specialProperties) {
        this.name = name;
        this.parentId = parentId;
        this.specialProperties = specialProperties;
        initialize();
    }

    private static String generateNewId(String parentId) {
        //TODO: implement
        return null;
    }

    public static ArrayList<Category> getAllCategories() {
        return (ArrayList<Category>) allCategories.values();
    }

    public static Category getCategoryByName(String name) {
        for (Category category : allCategories.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public static Category getCategoryById(String categoryId) {
        return allCategories.get(categoryId);
    }

    public static Category getSuperCategory() {
        return superCategory;
    }

    public void initialize() {
        if (categoryId == null) {
            categoryId = generateNewId(parentId);
        }
        subCategoryIds = new HashSet<>();
        productIds = new HashSet<>();
        if (parentId != null) {
            allCategories.put(categoryId, this);
            getParent().addSubCategory(categoryId);
        } else {
            superCategory = this;
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

    public Category getParent() {
        return Category.getCategoryById(parentId);
    }

    public void setParent(String parentId) {
        // first check if new parent is a child of the category -> !hasChildWithId(parentId)
        getParent().removeSubCategory(categoryId);
        this.parentId = parentId;
        getParent().addSubCategory(categoryId);
    }

    public boolean hasChildWithId(String categoryId) {
        for (Category subCategory : getSubCategories()) {
            if (categoryId.equals(subCategory.getId()) || subCategory.hasChildWithId(categoryId)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getSpecialProperties() {
        return new ArrayList<>(specialProperties);
    }

    public void setSpecialProperties(ArrayList<String> specialProperties) {
        this.specialProperties = specialProperties;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Category subCategory : getSubCategories()) {
            products.addAll(subCategory.getProducts());
        }
        for (String productId : productIds) {
            products.add(Product.getProductById(productId));
        }
        return products;
    }

    public void addProduct(String productId) {
        productIds.add(productId);
    }

    public void removeProduct(String productId) {
        productIds.remove(productId);
    }

    public ArrayList<Category> getSubCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        for (String subCategoryId : subCategoryIds) {
            categories.add(Category.getCategoryById(subCategoryId));
        }
        return categories;
    }

    public void addSubCategory(String subCategoryId) {
        subCategoryIds.add(subCategoryId);
    }

    public void removeSubCategory(String subCategoryId) {
        subCategoryIds.remove(subCategoryId);
    }
}
