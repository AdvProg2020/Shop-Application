package model;

import model.account.Seller;
import model.database.Database;
import model.request.AddSaleRequest;

import java.util.*;

public class Sale implements ModelBasic {
    private static Map<String, Sale> allSales = new HashMap<>();
    private static int lastNum = 1;
    private String saleId;
    private String sellerId;
    private Date startDate;
    private Date endDate;
    private double percentage; // 0 - 100
    private double maximumAmount;
    private Set<String> subProductIds;
    private boolean suspended;

    public Sale(String sellerId, Date startDate, Date endDate, double percentage, double maximumAmount, Database database) {
        this.sellerId = sellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        subProductIds = new HashSet<>();
        suspended = false;
        new AddSaleRequest(this).updateDatabase(database);
    }

    public static List<Sale> getAllSales(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSales.values(), suspense);
    }

    public static Sale getSaleById(String saleId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSales, saleId, suspense);
    }

    @Override
    public void initialize() {
        if (saleId == null)
            saleId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSales.put(saleId, this);
        lastNum++;

        getSeller().addSale(saleId);
        if (!suspended) {
            for (SubProduct subProduct : getSubProducts()) {
                subProduct.setSale(saleId);
            }
        }
    }

    public void suspend() {
        for (SubProduct subProduct : getSubProducts()) {
            subProduct.setSale(null);
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
        return !(suspended || new Date().before(startDate));
    }

    @Override
    public String getId() {
        return saleId;
    }

    public Seller getSeller(boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)
        return Seller.getSellerById(sellerId, checkSuspense);
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

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        subProductIds.removeIf(subProductId -> SubProduct.getSubProductById(subProductId) == null);
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }

        return subProducts;
    }

    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
        if (saleId != null)
            SubProduct.getSubProductById(subProductId).setSale(saleId);
    }

    public void removeSubProduct(String subProductId, boolean... deep) {
        boolean isDeep = (deep.length == 0) || deep[0]; // optional (default = true)
        subProductIds.remove(subProductId);
        if (isDeep)
            SubProduct.getSubProductById(subProductId).setSale(null);
    }
}
