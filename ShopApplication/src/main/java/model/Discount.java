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
    private double percentage; // 0 - 100
    private double maximumAmount;
    private HashMap<String, Integer> customerIds;
    private boolean suspended;

    public Discount(String discountCode, Date startDate, Date endDate, double percentage, double maximumAmount) {
        this.discountCode = discountCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        suspended = false;
        initialize();
    }

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static ArrayList<Discount> getAllDiscounts() {
        ArrayList<Discount> discounts = new ArrayList<>();
        for (Discount discount : allDiscounts.values()) {
            if (!discount.suspended) {
                discounts.add(discount);
            }
        }
        return discounts;
    }

    public static Discount getDiscountByCode(String discountCode) {
        for (Discount discount : allDiscounts.values()) {
            if (!discount.suspended && discount.getDiscountCode().equals(discountCode)) {
                return discount;
            }
        }
        return null;
    }

    public static Discount getDiscountById(String discountId) {
        return getDiscountById(discountId, true);
    }

    public static Discount getDiscountById(String discountId, boolean checkSuspense) {
        Discount discount = allDiscounts.get(discountId);
        if (checkSuspense && discount != null && discount.suspended) {
            discount = null;
        }
        return discount;
    }

    public void initialize() {
        if (discountId == null) {
            discountId = generateNewId();
        }
        allDiscounts.put(discountId, this);
        if (!suspended) {
            customerIds = new HashMap<>();
        }
    }

    public void suspend() {
        for (Customer customer : getCustomers().keySet()) {
            customer.removeDiscount(discountId);
        }
        customerIds = null;
        suspended = true;
    }

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

    public HashMap<Customer, Integer> getCustomers() {
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
        Customer.getCustomerById(customerId).addDiscount(discountId, count);
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
        Customer.getCustomerById(customerId).removeDiscount(discountId);
    }

}
