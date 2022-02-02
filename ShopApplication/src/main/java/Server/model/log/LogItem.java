package Server.model.log;

import Server.model.ModelBasic;
import Server.model.ModelUtilities;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.sellable.SubProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogItem implements ModelBasic {
    private static final Map<String, LogItem> allLogItems = new HashMap<>();
    private static int lastNum = 1;
    private String logItemId;
    private final String buyLogId;
    private final String sellLogId;
    private final String subProductId;
    private final int count;
    private final double price;
    private final double saleAmount;

    public LogItem(String buyLogId, String sellLogId, String subProductId, int count) {
        this.buyLogId = buyLogId;
        this.sellLogId = sellLogId;
        this.subProductId = subProductId;
        this.count = count;
        price = getSubProduct().getRawPrice();
        saleAmount = price - getSubProduct().getPriceWithSale();
        initialize();
    }

    public static List<LogItem> getAllLogItems() {
        return ModelUtilities.getAllInstances(allLogItems.values(), false);
    }

    public static LogItem getLogItemById(String logItemId) {
        return ModelUtilities.getInstanceById(allLogItems, logItemId, false);
    }

    @Override
    public void initialize() {
        if (logItemId == null)
            logItemId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allLogItems.put(logItemId, this);
        lastNum++;

        getBuyLog().addLogItem(logItemId);
        getSellLog().addLogItem(logItemId);
        getSubProduct().addCustomer(getCustomer().getId());
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return logItemId;
    }

    public BuyLog getBuyLog() {
        return BuyLog.getBuyLogById(buyLogId);
    }

    public SellLog getSellLog() {
        return SellLog.getSellLogById(sellLogId);
    }

    public Customer getCustomer() {
        return getBuyLog().getCustomer();
    }

    public Seller getSeller() {
        return getSellLog().getSeller();
    }

    public SubProduct getSubProduct() {
        return SubProduct.getSubProductById(subProductId, false);
    }

    public double getPrice() {
        return price;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public int getCount() {
        return count;
    }
}
