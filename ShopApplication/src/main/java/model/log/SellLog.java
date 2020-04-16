package model.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SellLog {
    private static HashMap<String, SellLog> allSellLogs = new HashMap<String, SellLog>();
    private BuyLog mainLog;
    private String sellLogId;
    private int receivedMoney;
    private int totalSaleAmount;
    private ArrayList<LogItem> items;

    public SellLog(BuyLog mainLog, String sellLogId, int receivedMoney, int totalSaleAmount) {
        this.mainLog = mainLog;
        this.sellLogId = sellLogId;
        this.receivedMoney = receivedMoney;
        this.totalSaleAmount = totalSaleAmount;
    }

    public int getReceivedMoney() {
        return receivedMoney;
    }

    public int getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public Date getDate() {
        return null;
    }

    public String getReceiverPhone() {
        return null;
    }

    public ShippingStatus getShippingStatus() {
        return null;
    }

    public int getPaidMoney() {
        return 0;
    }

    public int getTotalDiscountAmount() {
        return 0;
    }

    public String getReceiverName() {
        return null;
    }

    public String getReceiverAddress() {
        return null;
    }

    public ArrayList<LogItem> getItems() {
        return items;
    }

    public String getSellLogId() {
        return sellLogId;
    }

    public void addLogToDatabase() {
    }

    public void removeLogFromDatabase() {
    }

    public void loadDatabase() {
    }

}
