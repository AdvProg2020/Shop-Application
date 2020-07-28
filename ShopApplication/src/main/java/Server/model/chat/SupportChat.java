package Server.model.chat;

import Server.model.ModelUtilities;
import Server.model.account.Customer;
import Server.model.account.Supporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportChat extends Chat {
    protected static Map<String, SupportChat> allSupportChats = new HashMap<>();
    protected static int lastNum = 1;
    private String supporterId;
    private String customerId;
    private boolean suspended;

    public SupportChat(String supporterId, String customerId) {
        super();
        this.supporterId = supporterId;
        this.customerId = customerId;
        suspended = false;
        initialize();
    }

    public static List<SupportChat> getAllSupportChats(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSupportChats.values(), suspense);
    }

    public static SupportChat getSupportChatById(String chatId, boolean... suspense) {
        return (SupportChat) Chat.getChatById(chatId, suspense);
    }

    @Override
    public void initialize() {
        if (chatId == null)
            chatId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSupportChats.put(chatId, this);
        lastNum++;
        super.initialize();

        getSupporter().addChat(chatId);
        if (!suspended)
            getCustomer().setSupportChat(chatId);
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        getCustomer().removeSupportChat();
        suspended = true;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId);
    }

    public Supporter getSupporter() {
        return Supporter.getSupporterById(supporterId);
    }
}
