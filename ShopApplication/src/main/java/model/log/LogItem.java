package model.log;

import model.Initializable;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogItem implements Initializable {
    private static Map<String, LogItem> allLogItems = new HashMap<>();
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
        price = (getSubProduct().getRawPrice())*count;
        saleAmount = (price - getSubProduct().getPriceWithSale())*count;
        initialize();
    }

    private static String generateNewId(String subProductId) {
        //TODO: implement
        return null;
    }

    public static List<LogItem> getAllLogItems() {
        return new ArrayList<>(allLogItems.values());
    }

    public static LogItem getLogItemById(String logItemId) {
        return allLogItems.get(logItemId);
    }

    @Override
    public void initialize() {
        if (logItemId == null) {
            logItemId = generateNewId(subProductId);
        }
        allLogItems.put(logItemId, this);
        getBuyLog().addLogItem(logItemId);
        getSellLog().addLogItem(logItemId);
        getSubProduct().addCustomer(getCustomer().getId());
    }

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
