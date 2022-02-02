package Server.model;

import Server.model.ModelUtilities.ModelOnly;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.chat.AuctionChat;
import Server.model.database.Database;
import Server.model.request.AddAuctionRequest;
import Server.model.sellable.SubSellable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auction implements ModelBasic {
    private static final Map<String, Auction> allAuctions = new HashMap<>();
    private static int lastNum = 1;
    private String auctionId;
    private final String sellerId;
    private final String subSellableId;
    private Date startDate;
    private Date endDate;
    private String highestBidderId;
    private double highestBid;
    private transient String chatId;
    private boolean suspended;

    public Auction(String sellerId, String subSellableId, Date startDate, Date endDate, Database database) {
        this.sellerId = sellerId;
        this.subSellableId = subSellableId;
        this.startDate = startDate;
        this.endDate = endDate;
        highestBid = 0;
        chatId = null;
        suspended = false;
        new AddAuctionRequest(this).updateDatabase(database);
    }

    public static List<Auction> getAllAuctions(boolean... suspense) {
        return ModelUtilities.getAllInstances(allAuctions.values(), suspense);
    }

    public static Auction getAuctionById(String saleId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allAuctions, saleId, suspense);
    }

    @Override
    public void initialize() {
        if (auctionId == null)
            auctionId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allAuctions.put(auctionId, this);
        lastNum++;

        getSeller().addAuction(auctionId);
        if (!suspended) {
            getSubSellable().setAuction(auctionId);
        }
    }

    public void suspend() {
        getSubSellable().removeAuction();
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
        return auctionId;
    }

    public Seller getSeller(boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)
        return Seller.getSellerById(sellerId, checkSuspense);
    }

    public AuctionChat getChat() {
        return AuctionChat.getAuctionChatById(chatId);
    }

    @ModelOnly
    public void setChat(String chatId) {
        this.chatId = chatId;
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

    public double getHighestBid() {
        return highestBid;
    }

    public Customer getHighestBidder() {
        return Customer.getCustomerById(highestBidderId);
    }

    public void bid(String accountId, double bid) {
        if (bid > highestBid) {
            highestBidderId = accountId;
            highestBid = bid;
        }
    }

    @ModelOnly
    public SubSellable getSubSellable() {
        return SubSellable.getSubSellableById(subSellableId);
    }
}
