package model.request;

import model.Auction;
import model.account.Seller;
import model.chat.AuctionChat;
import model.database.Database;

public class AddAuctionRequest extends Request implements SellerRequest {
    private Auction auction;

    public AddAuctionRequest(Auction auction) {
        super();
        this.auction = auction;
        initialize();
    }

    @Override
    public void accept() {
        auction.initialize();
        new AuctionChat(auction.getId());
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (auction.getSeller() == null);
    }

    public Auction getAuction() {
        return auction;
    }

    @Override
    public Seller getSeller() {
        return auction.getSeller();
    }

    @Override
    public void updateDatabase(Database database) {
        database.createAuction();
    }
}
