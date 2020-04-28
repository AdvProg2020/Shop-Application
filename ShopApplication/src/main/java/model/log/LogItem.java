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
    private int count;
    private int price;
    private int salePercentage;

    public LogItem(String buyLogId, String sellLogId, String subProductId, int count, int price, int salePercentage) {
        logItemId = getNewId(subProductId);
        this.buyLogId = buyLogId;
        this.sellLogId = sellLogId;
        this.subProductId = subProductId;
        this.count = count;
        this.price = price;
        this.salePercentage = salePercentage;
        allLogItems.put(logItemId, this);
        getBuyLog().addLogItem(logItemId);
        getSellLog().addLogItem(logItemId);
    }

    private static String getNewId(String subProductId) {
        //TODO: implement
        return null;
    }

    public static LogItem getLogItemById(String logItemId) {
        return allLogItems.get(logItemId);
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

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public int getSalePercentage() {
        return salePercentage;
    }
}
