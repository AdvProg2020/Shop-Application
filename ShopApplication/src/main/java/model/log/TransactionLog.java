package model.log;

import model.SubProduct;

import java.util.Date;
import java.util.HashMap;

public class TransactionLog {
    private static HashMap<String, TransactionLog> allLogs = new HashMap<String, TransactionLog>();
    private String logId;
    private String customerName;
    private Date date;
    private int paidMoney;
    private int totalDiscountAmount;
    private HashMap<SubProduct, LogDetail> purchasedItems;
    private ShippingStatus shippingStatus;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    public TransactionLog(String logId, String customerName, Date date, HashMap<SubProduct, LogDetail> purchasedItems, ShippingStatus shippingStatus, int paidMoney, int totalDiscountAmount, String receiverName, String receiverAddress, String receiverPhone) {
        this.logId = logId;
        this.customerName = customerName;
        this.date = date;
        this.purchasedItems = purchasedItems;
        this.shippingStatus = shippingStatus;
        this.paidMoney = paidMoney;
        this.totalDiscountAmount = totalDiscountAmount;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
    }

    public static HashMap<String, TransactionLog> getAllLogs() {
        return allLogs;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLogId() {
        return logId;
    }

    public Date getDate() {
        return date;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public HashMap<SubProduct, LogDetail> getPurchasedItems() {
        return purchasedItems;
    }

    public ShippingStatus getShippingStatus() {
        return shippingStatus;
    }

    public int getPaidMoney() {
        return paidMoney;
    }

    public int getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void addLogToDatabase() {}

    public void removeLogFromDatabase() {}

    public void loadDatabase() {}

}
