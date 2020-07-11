package model.sellable;

import model.database.Database;
import model.request.AddProductRequest;

public class SubProduct extends SubSellable {
    private int remainingCount;

    public SubProduct(String productId, String sellerId, double price, int remainingCount, Database database) {
        super(productId, sellerId, price, database);
        this.remainingCount = remainingCount;
        if (sellableId != null)
            new AddProductRequest(null, this).updateDatabase(database);
    }

    public static SubProduct getSubProductById(String subProductId, boolean... suspense) {
        SubSellable subSellable = getSubSellableById(subProductId, suspense);
        if (subSellable instanceof SubProduct)
            return (SubProduct) subSellable;

        return null;
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

    public Product getProduct() {
        return (Product) getSellable();
    }

    @Override
    public boolean isAvailable() {
        return (remainingCount > 0);
    }
}
