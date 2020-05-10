package model;

import model.account.Seller;
import model.request.AddSaleRequest;

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

    public Sale(String sellerId, Date startDate, Date endDate, double percentage, double maximumAmount) {
        this.sellerId = sellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        suspended = false;
        new AddSaleRequest(this);
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
        return getSaleById(saleId, true);
    }

    public static Sale getSaleById(String saleId, boolean checkSuspense) {
        Sale sale = allSales.get(saleId);
        if (checkSuspense && sale != null && sale.suspended) {
            sale = null;
        }
        return sale;
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

    public String getId() {
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
            subProducts.add(subProduct);
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
