package model;

import model.account.Customer;

import java.util.Date;
import java.util.HashMap;

public class Discount {
    private String discountCode;
    private Date startDate;
    private Date endDate;
    private int percentage;
    private int maximumAmount;
    private HashMap<Customer, Integer> validCustomers;

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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setMaximumAmount(int maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public void addCustomer(Customer customer){}

    public void removeCustomer(Customer customer){}
}
