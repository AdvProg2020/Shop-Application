package model;

import model.account.Seller;
import model.database.Database;
import model.request.AddSaleRequest;
import model.sellable.SubFile;
import model.sellable.SubProduct;
import model.sellable.SubSellable;

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
    private Set<String> subSellableIds;
    private boolean suspended;

    public Sale(String sellerId, Date startDate, Date endDate, double percentage, double maximumAmount, Database database) {
        this.sellerId = sellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.maximumAmount = maximumAmount;
        subSellableIds = new HashSet<>();
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
            for (SubSellable subSellable : getSubSellables()) {
                subSellable.setSale(saleId);
            }
        }
    }

    public void suspend() {
        for (SubSellable subSellable : getSubSellables()) {
            subSellable.removeSale();
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

    public List<SubSellable> getSubSellables() {
        List<SubSellable> subSellables = new ArrayList<>();
        subSellableIds.removeIf(subSellableId -> SubSellable.getSubSellableById(subSellableId) == null);
        for (String subSellableId : subSellableIds) {
            subSellables.add(SubSellable.getSubSellableById(subSellableId));
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    public void addSubSellable(String subSellableId) {
        subSellableIds.add(subSellableId);
        if (saleId != null)
            SubSellable.getSubSellableById(subSellableId).setSale(saleId);
    }

    public void removeSubSellable(String subSellableId) {
        subSellableIds.remove(subSellableId);
        SubSellable.getSubSellableById(subSellableId).removeSale();
    }

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        subSellableIds.removeIf(subProductId -> SubSellable.getSubSellableById(subProductId) == null);
        for (String subSellableId : subSellableIds) {
            SubSellable subSellable = SubSellable.getSubSellableById(subSellableId);
            if (subSellable instanceof SubProduct)
                subProducts.add((SubProduct) subSellable);
        }

        subProducts.sort(Comparator.comparing(SubProduct::getId));
        return subProducts;
    }

    public List<SubFile> getSubFiles() {
        List<SubFile> subFiles = new ArrayList<>();
        subSellableIds.removeIf(subProductId -> SubSellable.getSubSellableById(subProductId) == null);
        for (String subSellableId : subSellableIds) {
            SubSellable subSellable = SubSellable.getSubSellableById(subSellableId);
            if (subSellable instanceof SubFile)
                subFiles.add((SubFile) subSellable);
        }

        subFiles.sort(Comparator.comparing(SubFile::getId));
        return subFiles;
    }
}
