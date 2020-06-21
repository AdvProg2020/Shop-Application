package model;

import model.account.Customer;

import java.util.*;

public class Discount implements ModelBasic {
    private static Map<String, Discount> allDiscounts = new HashMap<>();
    private static int lastNum = 1;
    private String discountId;
    private String discountCode;
    private Date startDate;
    private Date endDate;
    private double percentage; // 0 - 100
    private double maximumAmount;
    private Map<String, Integer> customerIds;
    private boolean suspended;

    public Discount(String discountCode, Date startDate, Date endDate, double percentage, double maximumAmount) {
        this.discountCode = discountCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        customerIds = new HashMap<>();
        suspended = false;
        initialize();
    }

    public static List<Discount> getActiveDiscounts() {
        return ModelUtilities.getAllInstances(allDiscounts.values());
    }

    public static List<Discount> getDiscountArchive() {
        ArrayList<Discount> archive = new ArrayList<>(allDiscounts.values());
        archive.removeAll(getActiveDiscounts());

        return archive;
    }

    public static Discount getDiscountById(String discountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allDiscounts, discountId, suspense);
    }

    public static Discount getDiscountByCode(String discountCode) {
        for (Discount discount : allDiscounts.values()) {
            if (!discount.isSuspended() && discount.getDiscountCode().equals(discountCode))
                return discount;
        }

        return null;
    }

    @Override
    public void initialize() {
        if (discountId == null)
            discountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allDiscounts.put(discountId, this);
        lastNum++;

        if (!suspended) {
            for (Map.Entry<Customer, Integer> entry : getCustomers().entrySet()) {
                entry.getKey().addDiscount(discountId, entry.getValue());
            }
        }
    }

    public void suspend() {
        for (Customer customer : getCustomers().keySet()) {
            customer.removeDiscount(discountId);
        }
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        if (new Date().after(endDate))
            suspend();

        return suspended;
    }

    public boolean hasStarted() {
        return !(new Date().before(startDate));
    }

    @Override
    public String getId() {
        return discountId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(double maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public double calculateDiscountAmount(double price) {
        double discountAmount = price * percentage / 100;
        if (discountAmount > maximumAmount)
            discountAmount = maximumAmount;

        return discountAmount;
    }

    public Map<Customer, Integer> getCustomers() {
        Map<Customer, Integer> customers = new HashMap<>();
        for (Map.Entry<String, Integer> entry : customerIds.entrySet()) {
            Customer customer = Customer.getCustomerById(entry.getKey());
            customers.put(customer, entry.getValue());
        }

        return customers;
    }

    public boolean hasCustomerWithId(String customerId) {
        return customerIds.containsKey(customerId);
    }

    public void addCustomer(String customerId, int count) {
        customerIds.put(customerId, count);
        Customer.getCustomerById(customerId).addDiscount(discountId, count);
    }

    //TODO: delete
    public void changeCount(String customerId, int changeAmount) {
        int newCount = customerIds.get(customerId) + changeAmount;
        if (newCount <= 0)
            removeCustomer(customerId);
        else
            addCustomer(customerId, newCount);
    }

    public void removeCustomer(String customerId) {
        customerIds.remove(customerId);
        Customer.getCustomerById(customerId).removeDiscount(discountId);
    }

}
