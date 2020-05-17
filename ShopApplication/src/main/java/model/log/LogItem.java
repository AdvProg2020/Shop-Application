package model.log;

import model.BasicMethods;
import model.ModelBasic;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogItem implements ModelBasic {
    private static Map<String, LogItem> allLogItems = new HashMap<>();
    private static int lastNum = 1;
    private String logItemId;
    private String buyLogId;
    private String sellLogId;
    private String subProductId;
    private int count;
    private double price;
    private double saleAmount;

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
        return new ArrayList<>(allLogItems.values());
    }

    public static LogItem getLogItemById(String logItemId) {
        return allLogItems.get(logItemId);
    }

    @Override
    public void initialize() {
        if (logItemId == null)
            logItemId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
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
