package Server.model.account;

import Server.model.Auction;
import Server.model.ModelUtilities;
import Server.model.ModelUtilities.ModelOnly;
import Server.model.Sale;
import Server.model.Wallet;
import Server.model.database.Database;
import Server.model.log.FileLog;
import Server.model.log.SellLog;
import Server.model.request.AddSellerRequest;
import Server.model.request.Request;
import Server.model.sellable.SubFile;
import Server.model.sellable.SubProduct;
import Server.model.sellable.SubSellable;

import java.util.*;

public class Seller extends Account {
    protected static Map<String, Seller> allSellers = new HashMap<>();
    private static int lastNum = 1;
    private String storeName;
    private transient String walletId;
    private transient Set<String> subSellableIds;
    private transient Set<String> saleIds;
    private transient Set<String> auctionIds;
    private transient Set<String> sellLogIds;
    private transient Set<String> fileLogIds;
    private transient Set<String> pendingRequestIds;

    public Seller(String username, String password, String firstName, String lastName, String email, String phone, String image, String storeName, double balance, Database database) {
        super(username, password, firstName, lastName, email, phone, image);
        this.storeName = storeName;
        new AddSellerRequest(this, balance).updateDatabase(database);
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
        fileLogIds = new HashSet<>();
        if (!suspended) {
            subSellableIds = new HashSet<>();
            saleIds = new HashSet<>();
            auctionIds = new HashSet<>();
            pendingRequestIds = new HashSet<>();
        }
    }

    @Override
    public void suspend() {
        for (SubSellable subSellable : getSubSellables()) {
            subSellable.suspend();
        }
        subSellableIds = null;
        for (Sale sale : getActiveSales()) {
            sale.suspend();
        }
        saleIds = null;
        for (Auction auction : getActiveAuctions()) {
            auction.suspend();
        }
        auctionIds = null;
        setWallet(null);
        super.suspend();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Wallet getWallet() {
        return Wallet.getWalletById(walletId);
    }

    @ModelOnly
    public void setWallet(String walletId) {
        if (this.walletId != null)
            getWallet().terminate();
        this.walletId = walletId;
    }

    public List<Sale> getActiveSales() {
        List<Sale> sales = new ArrayList<>();
        Sale sale;
        for (String saleId : saleIds) {
            if ((sale = Sale.getSaleById(saleId)) != null)
                sales.add(sale);
        }

        sales.sort(Comparator.comparing(Sale::getId));
        return sales;
    }

    public List<Sale> getSaleArchive() {
        List<Sale> sales = new ArrayList<>();
        for (String saleId : saleIds) {
            sales.add(Sale.getSaleById(saleId, false));
        }
        sales.removeAll(getActiveSales());

        sales.sort(Comparator.comparing(Sale::getId));
        return sales;
    }

    @ModelOnly
    public void addSale(String saleId) {
        saleIds.add(saleId);
    }

    public List<Auction> getActiveAuctions() {
        List<Auction> auctions = new ArrayList<>();
        Auction auction;
        for (String auctionId : auctionIds) {
            if ((auction = Auction.getAuctionById(auctionId)) != null)
                auctions.add(auction);
        }

        auctions.sort(Comparator.comparing(Auction::getId));
        return auctions;
    }

    public List<Auction> getAuctionArchive() {
        List<Auction> auctions = new ArrayList<>();
        for (String saleId : saleIds) {
            auctions.add(Auction.getAuctionById(saleId, false));
        }
        auctions.removeAll(getActiveAuctions());

        auctions.sort(Comparator.comparing(Auction::getId));
        return auctions;
    }

    @ModelOnly
    public void addAuction(String auctionId) {
        auctionIds.add(auctionId);
    }

    public List<SubSellable> getSubSellables() {
        List<SubSellable> subSellables = new ArrayList<>();
        for (String subSellableId : this.subSellableIds) {
            subSellables.add(SubSellable.getSubSellableById(subSellableId));
        }

        subSellables.sort(Comparator.comparing(SubSellable::getId));
        return subSellables;
    }

    @ModelOnly
    public void addSubSellable(String subSellableId) {
        subSellableIds.add(subSellableId);
    }

    @ModelOnly
    public void removeSubSellable(String subSellableId) {
        subSellableIds.remove(subSellableId);
    }

    public List<SubProduct> getSubProducts() {
        List<SubProduct> subProducts = new ArrayList<>();
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
        for (String subSellableId : subSellableIds) {
            SubSellable subSellable = SubSellable.getSubSellableById(subSellableId);
            if (subSellable instanceof SubFile)
                subFiles.add((SubFile) subSellable);
        }

        subFiles.sort(Comparator.comparing(SubFile::getId));
        return subFiles;
    }

    public List<SellLog> getSellLogs() {
        List<SellLog> sellLogs = new ArrayList<>();
        for (String sellLogId : sellLogIds) {
            sellLogs.add(SellLog.getSellLogById(sellLogId));
        }

        sellLogs.sort(Comparator.comparing(SellLog::getId));
        return sellLogs;
    }

    @ModelOnly
    public void addSellLog(String sellLogId) {
        sellLogIds.add(sellLogId);
    }

    public List<FileLog> getFileLogs() {
        List<FileLog> fileLogs = new ArrayList<>();
        for (String fileLogId : fileLogIds) {
            fileLogs.add(FileLog.getFileLogById(fileLogId));
        }

        fileLogs.sort(Comparator.comparing(FileLog::getId));
        return fileLogs;
    }

    @ModelOnly
    public void addFileLog(String fileLogId) {
        fileLogIds.add(fileLogId);
    }

    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        for (String requestId : pendingRequestIds) {
            requests.add(Request.getRequestById(requestId));
        }

        requests.sort(Comparator.comparing(Request::getId));
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


