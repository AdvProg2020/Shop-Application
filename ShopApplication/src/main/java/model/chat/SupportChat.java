package model.chat;

import model.account.Customer;
import model.account.Supporter;

public class SupportChat extends Chat {
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

    public static SupportChat getSupportChatById(String chatId, boolean... suspense) {
        return (SupportChat) Chat.getChatById(chatId, suspense);
    }

    @Override
    public void initialize() {
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
