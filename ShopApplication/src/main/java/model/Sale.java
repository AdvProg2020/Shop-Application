package model;

import model.account.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Sale {
    private static HashMap<String, Sale> allSales = new HashMap<>();
    private String saleId;
    private String sellerId;
    private Date startDate;
    private Date endDate;
    private double percentage; // 0 - 100
    private double maximumAmount;
    private HashSet<String> subProductIds;
    private boolean suspended;

    public Sale(String sellerId, Date startDate, Date endDate, int percentage, int maximumAmount) {
        this.sellerId = sellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        suspended = false;
    }

    private static String getNewId(String sellerId) {
        //TODO: implement
        return null;
    }

    public static ArrayList<Sale> getAllSales() {
        ArrayList<Sale> sales = new ArrayList<>();
        for (Sale sale : allSales.values()) {
            if (!sale.suspended) {
                sales.add(sale);
            }
        }
        return sales;
    }

    public static Sale getSaleById(String saleId) {
        return allSales.get(saleId);
    }

    public void initialize() {
        if (saleId == null) {
            saleId = getNewId(sellerId);
        }
        allSales.put(saleId, this);
        if (!suspended) {
            subProductIds = new HashSet<>();
            getSeller().addSale(saleId);
        }
    }

    public void suspend() {
        getSeller().removeSale(saleId);
        for (SubProduct subProduct : getSubProducts()) {
            subProduct.setSale(null);
        }
        subProductIds = null;
        suspended = true;
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

    public ArrayList<SubProduct> getSubProducts() {
        ArrayList<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            SubProduct subProduct = SubProduct.getSubProductById(subProductId);
            if (subProduct == null) {
                subProductIds.remove(subProductId);
            } else {
                subProducts.add(subProduct);
            }
        }
        return subProducts;
    }

    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
        SubProduct.getSubProductById(subProductId).setSale(saleId);
    }

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
        SubProduct.getSubProductById(subProductId).setSale(null);
    }
}
