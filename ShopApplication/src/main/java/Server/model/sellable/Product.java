package Server.model.sellable;

import Server.model.ModelUtilities;
import Server.model.database.Database;
import Server.model.request.AddProductRequest;
import Server.model.request.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product extends Sellable {
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/img/default-product-pic.png";
    private static Map<String, Product> allProducts = new HashMap<>();
    private static int lastNum = 1;
    private String brand;

    public Product(String name, String brand, String infoText, String imagePath, String categoryId, Map<String, String> propertyValues, SubSellable subSellable, Database database) {
        super(name, infoText, imagePath, categoryId, propertyValues, subSellable, database);
        this.brand = brand;
        new AddProductRequest(this, (SubProduct) subSellable).updateDatabase(database);
    }

    public static List<Product> getAllProducts(boolean... suspense) {
        return ModelUtilities.getAllInstances(allProducts.values(), suspense);
    }

    public static Product getProductById(String productId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allProducts, productId, suspense);
    }

    public static List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        for (Product product : allProducts.values()) {
            if (!product.suspended && product.getName().equals(name))
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

    public static boolean isProductNameAndBrandUsed(String name, String brand) {
        if (getProductByNameAndBrand(name, brand) != null) return true;

        for (Request request : Request.getPendingRequests()) {
            if (request instanceof AddProductRequest) {
                Product product = ((AddProductRequest) request).getProduct();
                if (product.getName().equals(name) && product.getBrand().equals(brand))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void initialize() {
        if (sellableId == null)
            sellableId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allProducts.put(sellableId, this);
        lastNum++;
        super.initialize();
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
        for (SubProduct subProduct : getSubProducts()) {
            total += subProduct.getRemainingCount();
        }
        return total;
    }

    public List<SubProduct> getSubProducts() { // TODO: check if it works properly
        return (List<SubProduct>) (List<?>) getSubSellables();
    }

    public SubProduct getSubProductOfSeller(String sellerId) {
        return (SubProduct) getSubSellableOfSeller(sellerId);
    }

    public List<SubProduct> getSubProductsInSale() { // TODO: delete?!
        return (List<SubProduct>) (List<?>) getSubSellablesInSale();
    }

    public SubProduct getDefaultSubProduct() {
        return (SubProduct) getDefaultSubSellable();
    }
}
