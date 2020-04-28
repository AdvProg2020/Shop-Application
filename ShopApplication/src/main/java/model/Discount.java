package model;

import model.account.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Discount {
    private static HashMap<String, Discount> allDiscounts = new HashMap<>();
    private String discountId;
    private String discountCode;
    private Date startDate;
    private Date endDate;
    private int percentage;
    private int maximumAmount;
    private HashMap<String, Integer> customerIds;
    private boolean suspended;

    public Discount(String discountCode, Date startDate, Date endDate, int percentage, int maximumAmount) {
        discountId = getNewId();
        this.discountCode = discountCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        suspended = false;
        customerIds = new HashMap<>();
        allDiscounts.put(discountId, this);

    }

    private static String getNewId() {
        //TODO: implement
        return null;
    }

    public static ArrayList<Discount> getAllDiscounts() {
        return (ArrayList<Discount>) allDiscounts.values();
    }

    public static Discount getDiscountById(String discountId) {
        return allDiscounts.get(discountId);
    }

    public static Discount getDiscountByCode(String discountCode) {
        for (Discount discount : allDiscounts.values()) {
            if (discount.getDiscountCode().equals(discountCode)) {
                return discount;
            }
        }
        return null;
    }

    public String getDiscountId() {
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(int maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }

    public HashMap<Customer, Integer> customers() {
        HashMap<Customer, Integer> customers = new HashMap<>();
        for (String customerId : customerIds.keySet()) {
            Customer customer = Customer.getCustomerById(customerId);
            int count = customerIds.get(customerId);
            customers.put(customer, count);
        }
        return customers;
    }

    public void addCustomer(String customerId, int count) {
        customerIds.put(customerId, count);
    }

    public void changeCount(String customerId, int changeAmount) {
        int newCount = customerIds.get(customerId) + changeAmount;
        if (newCount <= 0) {
            removeCustomer(customerId);
        } else {
            addCustomer(customerId, newCount);
        }
    }

    public void removeCustomer(String customerId) {
        customerIds.remove(customerId);
    }

}
