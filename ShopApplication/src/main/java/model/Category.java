package model;

import model.ModelUtilities.ModelOnly;

import java.util.*;

public class Category implements ModelBasic {
    public static final String SUPER_CATEGORY_NAME = "SuperCategory";
    private static Category superCategory = null; // parent of all main categories (shouldn't be suspended)
    private static Map<String, Category> allCategories = new HashMap<>();
    private static int lastNum = 1;
    private String categoryId;
    private String name;
    private String parentId;
    private List<String> properties;
    private transient Set<String> productIds;
    private transient Set<String> subCategoryIds;
    private boolean suspended;

    public Category(String name, String parentId, ArrayList<String> properties) {
        this.name = name;
        this.parentId = parentId;
        this.properties = properties;
        suspended = false;
        initialize();
    }

    public static Category getSuperCategory() {
        return superCategory;
    }

    public static void setSuperCategory() {
        if (superCategory == null)
            new Category(SUPER_CATEGORY_NAME, null, null);
    }

    public static List<Category> getAllCategories(boolean... suspense) {
        return ModelUtilities.getInstances(allCategories.values(), suspense);
    }

    public static Category getCategoryById(String categoryId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allCategories, categoryId, suspense);
    }

    public static Category getCategoryByName(String name) {
        if (name.equals(SUPER_CATEGORY_NAME))
            return superCategory;

        for (Category category : allCategories.values()) {
            if (!category.suspended && category.getName().equals(name))
                return category;
        }

        return null;
    }

    @Override
    public void initialize() {
        if (superCategory == null) {
            superCategory = this;
            subCategoryIds = new HashSet<>();
        } else {
            if (categoryId == null)
                categoryId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
            allCategories.put(categoryId, this);
            lastNum++;
            if (!suspended) {
                subCategoryIds = new HashSet<>();
                productIds = new HashSet<>();
                getParent().addSubCategory(categoryId);
            }
        }
    }

    public void suspend() {
        for (Category subCategory : getSubCategories()) {
            subCategory.suspend();
        }
        subCategoryIds = null;
        for (Product product : getProducts()) {
            product.suspend();
        }
        productIds = null;
        getParent().removeSubCategory(categoryId);
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public String getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProperties() {
        return new ArrayList<>(properties);
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

    @ModelOnly
    public void addProduct(String productId) {
        productIds.add(productId);
    }

    @ModelOnly
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

    @ModelOnly
    public void addSubCategory(String subCategoryId) {
        subCategoryIds.add(subCategoryId);
    }

    @ModelOnly
    public void removeSubCategory(String subCategoryId) {
        subCategoryIds.remove(subCategoryId);
    }
}
