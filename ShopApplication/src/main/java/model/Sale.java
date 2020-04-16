package model;

import model.account.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Sale {
    private static HashMap<String, Sale> allSales = new HashMap<String, Sale>();
    private String saleId;
    private Seller seller;
    private Date startDate;
    private Date endDate;
    private int percentage;
    private ArrayList<SubProduct> products;
    private boolean suspended;

    public Sale(String saleId, Seller seller, Date startDate, Date endDate, int percentage, ArrayList<SubProduct> products) {
        this.saleId = saleId;
        this.seller = seller;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.products = products;
    }

    public static Sale getSaleById(String saleId) {
        return null;
    }

    public static HashMap<String, Sale> getAllSales() {
        return allSales;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
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

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }

    public ArrayList<SubProduct> getProducts() {
        return products;
    }

    public boolean hasProductWithId(String productId) {
        return false;
    }

    public void addProduct(String productId) {
    }

    public void removeProduct(String productId) {
    }

    private void addSaleToDatabase() {
    }

    private void removeSaleFromDatabase() {
    }

    private void loadDatabase() {
    }

    private void updateSaleInDatabase(String name) {
    }
}
