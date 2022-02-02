package Server.model.log;


import Server.model.ModelBasic;
import Server.model.ModelUtilities;
import Server.model.ModelUtilities.ModelOnly;
import Server.model.account.Customer;

import java.util.*;

public class BuyLog implements ModelBasic {
    private static final Map<String, BuyLog> allBuyLogs = new HashMap<>();
    private static int lastNum = 1;
    private String buyLogId;
    private final String customerId;
    private final double paidMoney;
    private final double totalDiscountAmount;
    private final Date date;
    private final String receiverName;
    private final String receiverAddress;
    private final String receiverPhone;
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

    public static List<BuyLog> getAllBuyLogs() {
        return ModelUtilities.getAllInstances(allBuyLogs.values(), false);
    }

    public static BuyLog getBuyLogById(String buyLogId) {
        return ModelUtilities.getInstanceById(allBuyLogs, buyLogId, false);
    }

    @Override
    public void initialize() {
        if (buyLogId == null)
            buyLogId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allBuyLogs.put(buyLogId, this);
        lastNum++;

        logItemIds = new HashSet<>();
        getCustomer().addBuyLog(buyLogId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
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

    @ModelOnly
    public void addLogItem(String logItemId) {
        logItemIds.add(logItemId);
    }

}
