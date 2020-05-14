package model.log;

import jdk.jfr.Label;
import model.Initializable;
import model.account.Customer;

import java.util.*;

public class BuyLog implements Initializable {
    private static Map<String, BuyLog> allBuyLogs = new HashMap<>();
    private String buyLogId;
    private String customerId;
    private double paidMoney;
    private double totalDiscountAmount;
    private Date date;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private ShippingStatus shippingStatus;
    private transient Set<String> logItemIds;

    public BuyLog(String customerId, double paidMoney, double totalDiscountAmount, String receiverName,
                  String receiverAddress, String receiverPhone, ShippingStatus shippingStatus) {
        this.customerId = customerId;
        this.paidMoney = paidMoney;
        this.totalDiscountAmount = totalDiscountAmount;
        date = new Date();
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

    public static List<BuyLog> getAllBuyLogs() {
        return new ArrayList<>(allBuyLogs.values());
    }

    public static BuyLog getBuyLogById(String buyLogId) {
        return allBuyLogs.get(buyLogId);
    }

    @Override
    public void initialize() {
        if (buyLogId == null)
            buyLogId = generateNewId(customerId);
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

    public double getPaidMoney() {
        return paidMoney;
    }

    public double getTotalDiscountAmount() {
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

    public void setShippingStatus(ShippingStatus status) {
        this.shippingStatus = status;
    }

    public List<LogItem> getLogItems() {
        List<LogItem> logItems = new ArrayList<>();
        for (String logItemId : logItemIds) {
            logItems.add(LogItem.getLogItemById(logItemId));
        }
        return logItems;
    }

    @Label("Model internal use only!")
    public void addLogItem(String logItemId) {
        logItemIds.add(logItemId);
    }

}
