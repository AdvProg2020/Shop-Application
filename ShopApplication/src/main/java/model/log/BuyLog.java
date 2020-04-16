package model.log;

import model.account.Customer;
import model.account.Seller;

import java.util.Date;
import java.util.HashMap;

public class BuyLog {
    private static HashMap<String, BuyLog> allBuyLogs = new HashMap<String, BuyLog>();
    private String buyLogId;
    private Customer customer;
    private Date date;
    private int paidMoney;
    private int totalDiscountAmount;
    private HashMap<Seller, SellLog> sellLogs;
    private ShippingStatus shippingStatus;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    public BuyLog(String buyLogId, Customer customer, Date date, int paidMoney, int totalDiscountAmount, HashMap<Seller, SellLog> sellLogs, String receiverName, String receiverAddress, String receiverPhone) {
        this.buyLogId = buyLogId;
        this.customer = customer;
        this.date = date;
        this.paidMoney = paidMoney;
        this.totalDiscountAmount = totalDiscountAmount;
        this.sellLogs = sellLogs;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
        shippingStatus = ShippingStatus.processing;
    }

    public static HashMap<String, BuyLog> getAllBuyLogs() {
        return allBuyLogs;
    }

    public static String getBuyLogById() {
        return null;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getDate() {
        return date;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public HashMap<Seller, SellLog> getSellLogs() {
        return sellLogs;
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

    public void addLogToDatabase() {
    }

    public void removeLogFromDatabase() {
    }

    public void loadDatabase() {
    }

}
