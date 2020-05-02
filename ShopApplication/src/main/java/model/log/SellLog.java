package model.log;

import model.account.Customer;
import model.account.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SellLog {
    private static HashMap<String, SellLog> allSellLogs = new HashMap<>();
    private String sellLogId;
    private String sellerId;
    private String parentBuyLogId;
    private int receivedMoney;
    private int totalSaleAmount;
    private ArrayList<String> logItemIds;

    public SellLog(String sellerId, String parentBuyLogId, int receivedMoney, int totalSaleAmount) {
        this.sellerId = sellerId;
        this.parentBuyLogId = parentBuyLogId;
        this.receivedMoney = receivedMoney;
        this.totalSaleAmount = totalSaleAmount;
        initialize();
    }

    public static String generateNewId(String sellerId) {
        //TODO: implement
        return null;
    }

    public void initialize() {
        if (sellLogId == null) {
            sellLogId = generateNewId(sellerId);
        }
        allSellLogs.put(sellLogId, this);
        logItemIds = new ArrayList<>();
        getSeller().addSellLog(sellLogId);
    }

    public static SellLog getSellLogById(String sellLogId) {
        return allSellLogs.get(sellLogId);
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

    public int getReceivedMoney() {
        return receivedMoney;
    }

    public int getTotalSaleAmount() {
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

    public ArrayList<LogItem> getLogItems() {
        ArrayList<LogItem> logItems = new ArrayList<>();
        for (String logItemId : logItemIds) {
            logItems.add(LogItem.getLogItemById(logItemId));
        }
        return logItems;
    }

    public void addLogItem(String logItemId) {
        logItemIds.add(logItemId);
    }

}
