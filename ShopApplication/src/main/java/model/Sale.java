package model;

import model.account.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Sale {
    private static HashMap<String, Sale> allSales = new HashMap<>();
    private String saleId;
    private String sellerId;
    private Date startDate;
    private Date endDate;
    private int percentage;
    private int maximumAmount;
    private ArrayList<String> subProductIds;
    private boolean suspended;

    public Sale(String sellerId, Date startDate, Date endDate, int percentage, int maximumAmount) {
        this.saleId = getNewId(sellerId);
        this.sellerId = sellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        suspended = false;
        allSales.put(saleId, this);
        getSeller().addSale(saleId);
    }

    private static String getNewId(String sellerId) {
        //TODO: implement
        return null;
    }

    public static ArrayList<Sale> getAllSales() {
        return (ArrayList<Sale>) allSales.values();
    }

    public static Sale getSaleById(String saleId) {
        return allSales.get(saleId);
    }

    public String getSaleId() {
        return saleId;
    }

    public Seller getSeller() {
        return Seller.getSellerById(sellerId);
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

    public ArrayList<SubProduct> getSubProducts() {
        ArrayList<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }
        return subProducts;
    }

    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }
}
