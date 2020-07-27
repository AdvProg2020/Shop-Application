package Server.model.chat;

import Server.model.Auction;
import Server.model.ModelUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionChat extends Chat {
    protected static Map<String, Chat> allAuctionChats = new HashMap<>();
    protected static int lastNum = 1;
    private String auctionId;

    public AuctionChat(String auctionId) {
        super();
        this.auctionId = auctionId;
        initialize();
    }

    public static List<Chat> getAllAuctionChats() {
        return ModelUtilities.getAllInstances(allAuctionChats.values(), false);
    }

    public static AuctionChat getAuctionChatById(String chatId) {
        return (AuctionChat) Chat.getChatById(chatId, false);
    }

    @Override
    public void initialize() {
        if (chatId == null)
            chatId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allAuctionChats.put(chatId, this);
        lastNum++;
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
