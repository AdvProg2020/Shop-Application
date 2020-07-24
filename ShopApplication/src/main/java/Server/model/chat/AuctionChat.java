package Server.model.chat;

import Server.model.Auction;

public class AuctionChat extends Chat {
    private String auctionId;

    public AuctionChat(String auctionId) {
        super();
        this.auctionId = auctionId;
        initialize();
    }

    public static AuctionChat getAuctionChatById(String chatId, boolean... suspense) {
        return (AuctionChat) Chat.getChatById(chatId);
    }

    @Override
    public void initialize() {
        super.initialize();
        getAuction().setChat(auctionId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    public Auction getAuction() {
        return Auction.getAuctionById(auctionId);
    }
}
