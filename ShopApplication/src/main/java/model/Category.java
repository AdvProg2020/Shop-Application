package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private static HashMap<String, Category> allCategories = new HashMap<>();
    private String categoryId;
    private String name;
    private String parentId;
    private ArrayList<String> specialProperties;
    private ArrayList<String> productIds;
    private ArrayList<String> subCategoryIds;
    private boolean suspended;

    public Category(String name, String parentId, ArrayList<String> specialProperties) {
        categoryId = getNewId(parentId);
        this.name = name;
        setParent(parentId);
        this.specialProperties = specialProperties;
        suspended = false;
        productIds = new ArrayList<>();
        subCategoryIds = new ArrayList<>();
        allCategories.put(categoryId, this);
        getParent().addSubCategory(categoryId);
    }

    private static String getNewId(String parentId) {
        //TODO: implement
        return null;
    }

    public static ArrayList<Category> getAllCategories() {
        return (ArrayList<Category>) allCategories.values();
    }

    public static Category getCategoryById(String categoryId) {
        return allCategories.get(categoryId);
    }

    public static Category getCategoryByName(String name) {
        for (Category category : allCategories.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public String getCategoryId() {
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
        if (parentId != null) {
            getParent().removeSubCategory(categoryId);
        }
        this.parentId = parentId;
        getParent().addSubCategory(categoryId);
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

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }
}
