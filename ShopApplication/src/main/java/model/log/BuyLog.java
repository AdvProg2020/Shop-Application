package model.log;

import model.account.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class BuyLog {
    private static HashMap<String, BuyLog> allBuyLogs = new HashMap<>();
    private String buyLogId;
    private String customerId;
    private int paidMoney;
    private int totalDiscountAmount;
    private Date date;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private ShippingStatus shippingStatus;
    private transient HashSet<String> logItemIds;

    public BuyLog(String customerId, int paidMoney, int totalDiscountAmount, Date date, String receiverName,
                  String receiverAddress, String receiverPhone, ShippingStatus shippingStatus) {
        this.customerId = customerId;
        this.paidMoney = paidMoney;
        this.totalDiscountAmount = totalDiscountAmount;
        this.date = date;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
        this.shippingStatus = shippingStatus;
        initialize();
    }

    private static String generateNewId(String customerId) {
        //TODO: implement
        return null;
    }

    public static BuyLog getBuyLogById(String buyLogId) {
        return allBuyLogs.get(buyLogId);
    }

    public void initialize() {
        if (buyLogId == null) {
            buyLogId = generateNewId(customerId);
        }
        allBuyLogs.put(buyLogId, this);
        logItemIds = new HashSet<>();
        getCustomer().addBuyLog(buyLogId);
    }

    public String getId() {
        return buyLogId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId, false);
    }

    public int getPaidMoney() {
        return paidMoney;
    }

    public int getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public Date getDate() {
        return date;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public ShippingStatus getShippingStatus() {
        return shippingStatus;
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
