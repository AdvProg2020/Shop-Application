package model.account;

import model.ModelUtilities;
import model.ModelUtilities.ModelOnly;
import model.Sale;
import model.SubProduct;
import model.log.SellLog;
import model.request.AddSellerRequest;
import model.request.Request;

import java.util.*;

public class Seller extends Account {
    protected static Map<String, Seller> allSellers = new HashMap<>();
    private static int lastNum = 1;
    private String storeName;
    private double balance;
    private transient Set<String> subProductIds;
    private transient Set<String> saleIds;
    private transient Set<String> sellLogIds;
    private transient Set<String> pendingRequestIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone, String image, String storeName, double balance) {
        super(username, password, firstName, lastName, email, phone, image);
        this.storeName = storeName;
        this.balance = balance;
        new AddSellerRequest(this);
    }

    public static List<Seller> getAllSellers(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSellers.values(), suspense);
    }

    public static Seller getSellerById(String accountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSellers, accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSellers.put(accountId, this);
        lastNum++;
        super.initialize();

        sellLogIds = new HashSet<>();
        if (!suspended) {
            subProductIds = new HashSet<>();
            saleIds = new HashSet<>();
            pendingRequestIds = new HashSet<>();
        }
    }

    @Override
    public void suspend() {
        for (SubProduct subProduct : getSubProducts()) {
            subProduct.suspend();
        }
        subProductIds = null;
        for (Sale sale : getSales()) {
            sale.suspend();
        }
        saleIds = null;
        super.suspend();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getBalance() {
        return balance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
    }

    public List<Sale> getSales() {
        List<Sale> sales = new ArrayList<>();
        for (String saleId : saleIds) {
            sales.add(Sale.getSaleById(saleId, false));
        }

        return sales;
    }

    @ModelOnly
    public void addSale(String saleId) {
        saleIds.add(saleId);
    }

    @ModelOnly
    public void removeSale(String saleId) {
        saleIds.remove(saleId);
    }

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
        for (String subProductId : subProductIds) {
            subProducts.add(SubProduct.getSubProductById(subProductId));
        }

        return subProducts;
    }

    @ModelOnly
    public void addSubProduct(String subProductId) {
        subProductIds.add(subProductId);
    }

    @ModelOnly
    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public List<SellLog> getSellLogs() {
        List<SellLog> sellLogs = new ArrayList<>();
        for (String sellLogId : sellLogIds) {
            sellLogs.add(SellLog.getSellLogById(sellLogId));
        }

        return sellLogs;
    }

    @ModelOnly
    public void addSellLog(String sellLogId) {
        sellLogIds.add(sellLogId);
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        for (String requestId : pendingRequestIds) {
            requests.add(Request.getRequestById(requestId));
        }

        return requests;
    }

    @ModelOnly
    public void addRequest(String requestId) {
        pendingRequestIds.add(requestId);
    }

    @ModelOnly
    public void removeRequest(String requestId) {
        pendingRequestIds.remove(requestId);
    }
}


