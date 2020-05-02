package model.log;

import model.SubProduct;
import model.account.Customer;
import model.account.Seller;

import java.util.HashMap;

public class LogItem {
    private static HashMap<String, LogItem> allLogItems = new HashMap<>();
    private String logItemId;
    private String buyLogId;
    private String sellLogId;
    private String subProductId;
    private double unitPrice;
    private double salePercentage;
    private int count;

    public LogItem(String buyLogId, String sellLogId, String subProductId,
                   double unitPrice, double salePercentage, int count) {
        this.buyLogId = buyLogId;
        this.sellLogId = sellLogId;
        this.subProductId = subProductId;
        this.unitPrice = unitPrice;
        this.salePercentage = salePercentage;
        this.count = count;
        initialize();
    }

    private static String generateNewId(String subProductId) {
        //TODO: implement
        return null;
    }

    public static LogItem getLogItemById(String logItemId) {
        return allLogItems.get(logItemId);
    }

    public void initialize() {
        if (logItemId == null) {
            logItemId = generateNewId(subProductId);
        }
        allLogItems.put(logItemId, this);
        getBuyLog().addLogItem(logItemId);
        getSellLog().addLogItem(logItemId);
        getSubProduct().addCustomer(getCustomer().getAccountId());
    }

    public String getLogItemId() {
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
        return SubProduct.getSubProductById(subProductId);
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSalePercentage() {
        return salePercentage;
    }

    public int getCount() {
        return count;
    }
}
