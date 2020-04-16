package model.log;

import model.SubProduct;

import java.util.HashMap;

public class LogDetail {
    private TransactionLog mainLog;
    private int receivedMoney;
    private int totalSaleAmount;
    private HashMap<SubProduct, Integer> products;

    public LogDetail(TransactionLog mainLog, int receivedMoney, int totalSaleAmount, HashMap<SubProduct, Integer> products) {
        this.mainLog = mainLog;
        this.receivedMoney = receivedMoney;
        this.totalSaleAmount = totalSaleAmount;
        this.products = products;
    }

    public TransactionLog getMainLog() {
        return mainLog;
    }

    public int getReceivedMoney() {
        return receivedMoney;
    }

    public int getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public HashMap<SubProduct, Integer> getProducts() {
        return products;
    }

    public void addLogToDatabase() {}

    public void removeLogFromDatabase() {}

    public void loadDatabase() {}
}
