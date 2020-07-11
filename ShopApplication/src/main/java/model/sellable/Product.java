package model.sellable;

import model.database.Database;
import model.request.AddProductRequest;
import model.request.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Product extends Sellable {
    private static final String DEFAULT_IMAGE_PATH = "/src/main/resources/img/default-product-pic.png";
    private String brand;

    public Product(String name, String brand, String infoText, String imagePath, String categoryId, Map<String, String> propertyValues, SubSellable subSellable, Database database) {
        super(name, infoText, imagePath, categoryId, propertyValues, subSellable, database);
        this.brand = brand;
        new AddProductRequest(this, (SubProduct) subSellable).updateDatabase(database);
    }

    public static List<Product> getAllProducts() {
        //TODO: implement
        return null;
    }

    public static Product getProductById(String productId, boolean... suspense) {
        Sellable sellable = getSellableById(productId, suspense);
        if (sellable instanceof Product)
            return (Product) sellable;

        return null;
    }

    public static Product getProductByNameAndBrand(String name, String brand) {
        for (Sellable sellable : getSellablesByName(name)) {
            if (sellable instanceof Product && ((Product) sellable).getBrand().equals(brand))
                return (Product) sellable;
        }

        return null;
    }

    public static boolean isProductNameAndBrandUsed(String name, String brand) {
        if (getProductByNameAndBrand(name, brand) != null) return true;

        for (Request request : Request.getPendingRequests()) {
            if (request instanceof AddProductRequest)
                if (((AddProductRequest) request).getProduct().getName().equals(name) && ((AddProductRequest) request).getProduct().getBrand().equals(brand))
                    return true;
        }

        return false;
    }

    @Override
    protected String getDefaultImagePath() {
        return DEFAULT_IMAGE_PATH;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getTotalRemainingCount() {
        int total = 0;
        for (SubSellable subSellable : getSubSellables()) {
            total += ((SubProduct) subSellable).getRemainingCount();
        }
        return total;
    }

    public List<SubProduct> getSubProducts() {
        List<SubSellable> subSellables = new ArrayList<>();
        for (String subSellableId : subSellableIds) {
            subSellables.add(SubSellable.getSubSellableById(subSellableId));
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    public SubProduct getSubProductOfSeller(String sellerId) {
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getSeller().getId().equals(sellerId))
                return subSellable;
        }

        return null;
    }

    public List<SubProduct> getSubProductsInSale() {
        List<SubProduct> subSellables = new ArrayList<>();
        for (SubSellable subSellable : getSubSellables()) {
            if (subSellable.getSale() != null)
                subSellables.add(subSellable);
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    public SubProduct getDefaultSubProduct() {
        return (SubProduct) getDefaultSubSellable();
    }
}
