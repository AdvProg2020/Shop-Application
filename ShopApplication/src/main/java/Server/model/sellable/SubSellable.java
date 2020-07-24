package Server.model.sellable;

import Server.model.*;
import Server.model.ModelUtilities.ModelOnly;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.database.Database;

import java.util.*;

public abstract class SubSellable implements ModelBasic {
    private static Map<String, SubSellable> allSubSellables = new HashMap<>();
    protected String subSellableId;
    protected String sellableId;
    protected String sellerId;
    protected double price;
    protected transient String saleId; //can be null
    protected transient String auctionId; //can be null
    protected transient Set<String> buyerIds;
    protected boolean suspended;

    public SubSellable(String sellableId, String sellerId, double price, Database database) {
        this.sellableId = sellableId;
        this.sellerId = sellerId;
        this.price = price;
        saleId = null;
        suspended = false;
    }

    public static List<SubSellable> getAllSubSellables(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSubSellables.values(), suspense);
    }

    public static SubSellable getSubSellableById(String subSellableId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSubSellables, subSellableId, suspense);
    }

    @Override
    public void initialize() {
        allSubSellables.put(subSellableId, this);

        buyerIds = new HashSet<>();
        if (!suspended) {
            getSeller().addSubSellable(subSellableId);
            getSellable().addSubSellable(subSellableId);
        }
    }

    public void suspend() {
        getSeller().removeSubSellable(subSellableId);
        getSellable().removeSubSellable(subSellableId);
        getSale().removeSubSellable(subSellableId);
        getAuction().suspend();
        if (this instanceof SubProduct)
            Cart.removeSubProductFromAll(subSellableId);
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public String getId() {
        return subSellableId;
    }

    public Sellable getSellable(boolean... suspense) {
        return Sellable.getSellableById(sellableId, suspense);
    }

    @ModelOnly
    public void setSellableId(String sellableId) { // only used for accepting productRequest
        if (this.sellableId == null)
            this.sellableId = sellableId;
    }

    public Seller getSeller(boolean... suspense) {
        return Seller.getSellerById(sellerId, suspense);
    }

    public abstract boolean isAvailable();

    public Sale getSale() {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null || !sale.hasStarted()) return null;

        return sale;
    }

    @ModelOnly
    public void setSale(String saleId) {
        if (getSale() != null)
            getSale().removeSubSellable(subSellableId);
        this.saleId = saleId;
    }

    @ModelOnly
    public void removeSale() {
        this.saleId = null;
    }

    public Auction getAuction() {
        Auction auction = Auction.getAuctionById(auctionId);
        if (auction == null || !auction.hasStarted()) return null;

        return auction;
    }

    @ModelOnly
    public void setAuction(String auctionId) {
        if (getAuction() != null)
            getAuction().suspend();
        this.auctionId = auctionId;
    }

    @ModelOnly
    public void removeAuction() {
        this.auctionId = null;
    }

    public double getRawPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceWithSale() {
        if (getSale() == null) return price;

        double saleAmount = price * getSale().getPercentage() / 100;
        double maximumAmount = getSale().getMaximumAmount();
        if (saleAmount > maximumAmount)
            saleAmount = maximumAmount;

        return price - saleAmount;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (String customerId : buyerIds) {
            customers.add(Customer.getCustomerById(customerId, false));
        }

        return customers;
    }

    public boolean hasCustomerWithId(String customerId) {
        return buyerIds.contains(customerId);
    }

    @ModelOnly
    public void addCustomer(String customerId) {
        buyerIds.add(customerId);
    }
}
