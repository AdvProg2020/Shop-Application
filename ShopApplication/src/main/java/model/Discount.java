package model;

import model.account.Customer;

import java.util.Date;
import java.util.HashMap;

public class Discount {
    private HashMap<String, Discount> allDiscounts = new HashMap<String, Discount>();
    private String discountId;
    private String discountCode;
    private Date startDate;
    private Date endDate;
    private int percentage;
    private int maximumAmount;
    private HashMap<Customer, Integer> validCustomers;
    private boolean suspended;

    public Discount(String discountCode, Date startDate, Date endDate, int percentage, int maximumAmount) {
        this.discountCode = discountCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
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

    public void addCustomer(Customer customer) {
    }

    public void removeCustomer(Customer customer) {
    }

    private void addDiscountToDatabase() {
    }

    private void removeDiscountFromDatabase() {
    }

    private void loadDatabase() {
    }

    private void updateDiscountInDatabase(String name) {
    }
}
