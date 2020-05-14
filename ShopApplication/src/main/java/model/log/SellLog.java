package model.log;

import jdk.jfr.Label;
import model.Initializable;
import model.account.Customer;
import model.account.Seller;

import java.util.*;

public class SellLog implements Initializable {
    private static Map<String, SellLog> allSellLogs = new HashMap<>();
    private String sellLogId;
    private String parentBuyLogId;
    private String sellerId;
    private double receivedMoney;
    private double totalSaleAmount;
    private transient List<String> logItemIds;

    public SellLog(String parentBuyLogId, String sellerId) {
        this.parentBuyLogId = parentBuyLogId;
        this.sellerId = sellerId;
        receivedMoney = 0;
        totalSaleAmount = 0;
        initialize();
    }

    public static String generateNewId(String sellerId) {
        //TODO: implement
        return null;
    }

    public static List<SellLog> getAllSellLogs() {
        return new ArrayList<>(allSellLogs.values());
    }

    public static SellLog getSellLogById(String sellLogId) {
        return allSellLogs.get(sellLogId);
    }

    @Override
    public void initialize() {
        if (sellLogId == null)
            sellLogId = generateNewId(sellerId);
        allSellLogs.put(sellLogId, this);
        logItemIds = new ArrayList<>();
        getSeller().addSellLog(sellLogId);
    }

    public String getId() {
        return sellLogId;
    }

    private BuyLog getParentBuyLog() {
        return BuyLog.getBuyLogById(parentBuyLogId);
    }

    public Seller getSeller() {
        return Seller.getSellerById(sellerId, false);
    }

    public Customer getCustomer() {
        return getParentBuyLog().getCustomer();
    }

    public double getReceivedMoney() {
        return receivedMoney;
    }

    public double getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public Date getDate() {
        return getParentBuyLog().getDate();
    }

    public String getReceiverName() {
        return getParentBuyLog().getReceiverName();
    }

    public String getReceiverAddress() {
        return getParentBuyLog().getReceiverAddress();
    }

    public String getReceiverPhone() {
        return getParentBuyLog().getReceiverPhone();
    }

    public ShippingStatus getShippingStatus() {
        return getParentBuyLog().getShippingStatus();
    }

    public void setShippingStatus(ShippingStatus status) {
        getParentBuyLog().setShippingStatus(status);
    }

    public List<LogItem> getLogItems() {
        ArrayList<LogItem> logItems = new ArrayList<>();
        for (String logItemId : logItemIds) {
            logItems.add(LogItem.getLogItemById(logItemId));
        }
        return logItems;
    }

    @Label("Model internal use only!")
    public void addLogItem(String logItemId) {
        logItemIds.add(logItemId);
        LogItem item = LogItem.getLogItemById(logItemId);
        receivedMoney += (item.getPrice() - item.getSaleAmount()) * item.getCount();
        totalSaleAmount += item.getSaleAmount() * item.getCount();
    }

}
