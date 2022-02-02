package Server.model.chat;

import Server.model.ModelBasic;
import Server.model.ModelUtilities;
import Server.model.account.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements ModelBasic {
    private static final Map<String, Message> allMessages = new HashMap<>();
    private static int lastNum = 1;
    private String messageId;
    private final String chatId;
    private final String senderId;
    private final Date sendDate;
    private final String text;

    public Message(String chatId, String senderId, String text) {
        this.chatId = chatId;
        this.senderId = senderId;
        sendDate = new Date();
        this.text = text;
        initialize();
    }

    public static List<Message> getAllMessages() {
        return ModelUtilities.getAllInstances(allMessages.values());
    }

    public static Message getMessageById(String messageId) {
        return ModelUtilities.getInstanceById(allMessages, messageId);
    }

    @Override
    public void initialize() {
        if (messageId == null)
            messageId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allMessages.put(messageId, this);
        lastNum++;

        getChat(false).addMessage(messageId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return messageId;
    }

    public Chat getChat(boolean... suspense) {
        return Chat.getChatById(chatId, suspense);
    }

    public Account getSender() {
        return Account.getAccountById(senderId);
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getText() {
        return text;
    }
}
