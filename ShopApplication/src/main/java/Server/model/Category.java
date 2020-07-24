package Server.model;

import Server.model.ModelUtilities.ModelOnly;
import Server.model.sellable.File;
import Server.model.sellable.Product;
import Server.model.sellable.Sellable;

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
    private transient Set<String> sellableIds;
    private transient Set<String> subCategoryIds;
    private boolean suspended;

    public Category(String name, String parentId, List<String> properties) {
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
        if (superCategory == null) {
            new Category(SUPER_CATEGORY_NAME, null, new ArrayList<>());
        }
    }

    public static List<Category> getAllCategories(boolean... suspense) {
        return ModelUtilities.getAllInstances(allCategories.values(), suspense);
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
        if (categoryId == null)
            categoryId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        lastNum++;

        if (superCategory == null) {
            superCategory = this;
            subCategoryIds = new HashSet<>();
        } else {
            allCategories.put(categoryId, this);
            if (!suspended) {
                subCategoryIds = new HashSet<>();
                sellableIds = new HashSet<>();
                getParent().addSubCategory(categoryId);
            }
        }
    }

    public void suspend() {
        for (Category subCategory : getSubCategories()) {
            subCategory.suspend();
        }
        subCategoryIds = null;
        for (Sellable sellable : getSellables(false)) {
            sellable.suspend();
        }
        sellableIds = null;
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

    public List<String> getProperties(boolean deep) {
        if (!deep || getParent() == superCategory) return new ArrayList<>(properties);

        List<String> deepProperties = getParent().getProperties(true);
        deepProperties.addAll(properties);

        return deepProperties;
    }

    public void addProperty(String property) {
        properties.add(property);
        for (Sellable sellable : getSellables(true)) {
            sellable.addProperty(property);
        }
    }

    public void removeProperty(String property) {
        properties.remove(property);
        for (Sellable sellable : getSellables(true)) {
            sellable.removeProperty(property);
        }
    }

    public boolean hasProperty(String property) {
        return properties.contains(property);
    }

    public Category getParent() {
        if (parentId.equals(superCategory.getId())) return superCategory;

        return Category.getCategoryById(parentId);
    }

    public void setParent(String parentId) {
        // first check if new parent is a child of the category -> !hasSubCategoryWithId(parentId)
        getParent().removeSubCategory(categoryId);
        this.parentId = parentId;
        getParent().addSubCategory(categoryId);
    }

    public List<Sellable> getSellables(boolean deep) {
        List<Sellable> sellables = new ArrayList<>();
        if (deep) {
            for (Category subCategory : getSubCategories()) {
                sellables.addAll(subCategory.getSellables(true));
            }
        }
        if (this != superCategory) {
            for (String sellableId : sellableIds) {
                sellables.add(Sellable.getSellableById(sellableId));
            }
        }

        return sellables;
    }

    @ModelOnly
    public void addSellable(String sellableId) {
        sellableIds.add(sellableId);
    }

    @ModelOnly
    public void removeSellable(String sellableId) {
        sellableIds.remove(sellableId);
    }

    public List<Product> getProducts(boolean deep) {
        List<Product> products = new ArrayList<>();
        for (Sellable sellable : getSellables(deep)) {
            if (sellable instanceof Product)
                products.add((Product) sellable);
        }

        return products;
    }

    public List<File> getFiles(boolean deep) {
        List<File> files = new ArrayList<>();
        for (Sellable sellable : getSellables(deep)) {
            if (sellable instanceof File)
                files.add((File) sellable);
        }

        return files;
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
