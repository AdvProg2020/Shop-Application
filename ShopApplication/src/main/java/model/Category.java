package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private static HashMap<String, Category> allCategories = new HashMap<String, Category>();
    private String categoryId;
    private String name;
    private ArrayList<Product> products;
    private ArrayList<SpecialProperty> specialProperties;
    private Category parent;
    private ArrayList<Category> subcategories;

    public Category(String name) {
        this.name = name;
    }

    public static HashMap<String, Category> getAllCategories() {
        return allCategories;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<SpecialProperty> getSpecialProperties() {
        return specialProperties;
    }

    public Category getParent() {
        return parent;
    }

    public ArrayList<Category> getSubcategories() {
        return subcategories;
    }

    public void addProduct(Product product){}

    public void removeProduct(Product product){}

    public void addSpecialProperty(SpecialProperty specialProperty) {}

    public void removeSpecialProperty(SpecialProperty specialProperty) {}

    public void addSubcategory(Category category){}

    public void removeSubcategory(Category category){}

    public void addCategoryToDatabase() {}

    public void removeCategoryFromDatabase() {}

    public void loadDatabase() {}

    public void updateCategoryInDatabase(String name) {}
}
