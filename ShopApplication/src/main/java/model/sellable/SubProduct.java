package model.sellable;

import model.ModelUtilities;
import model.database.Database;
import model.request.AddProductRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubProduct extends SubSellable {
    private static Map<String, SubProduct> allSubProducts = new HashMap<>();
    private static int lastNum = 1;
    private int remainingCount;

    public SubProduct(String productId, String sellerId, double price, int remainingCount, Database database) {
        super(productId, sellerId, price, database);
        this.remainingCount = remainingCount;
        if (sellableId != null)
            new AddProductRequest(null, this).updateDatabase(database);
    }

    public static List<SubProduct> getAllSubProducts(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSubProducts.values(), suspense);
    }

    public static SubProduct getSubProductById(String subProductId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSubProducts, subProductId, suspense);
    }

    @Override
    public void initialize() {
        if (subSellableId == null)
            subSellableId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSubProducts.put(subSellableId, this);
        lastNum++;
        super.initialize();
    }

    public Product getProduct(boolean... suspense) {
        return Product.getProductById(sellableId, suspense);
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void changeRemainingCount(int changeAmount) {
        remainingCount += changeAmount;
        if (remainingCount < 0)
            remainingCount = 0;
    }

    @Override
    public boolean isAvailable() {
        return (remainingCount > 0);
    }
}
