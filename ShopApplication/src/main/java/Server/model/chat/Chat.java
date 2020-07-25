package Server.model.chat;

import Server.model.ModelBasic;
import Server.model.ModelUtilities;

import java.util.*;

public abstract class Chat implements ModelBasic {
    protected static Map<String, Chat> allChats = new HashMap<>();
    protected static int lastNum = 1;
    protected String chatId;
    protected transient Set<String> messageIds;

    public static List<Chat> getAllChats(boolean... suspense) {
        return ModelUtilities.getAllInstances(allChats.values(), suspense);
    }

    public static Chat getChatById(String chatId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allChats, chatId, suspense);
    }

    @Override
    public void initialize() {
        if (chatId == null)
            chatId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allChats.put(chatId, this);
        lastNum++;

        messageIds = new HashSet<>();
    }

    @Override
    public abstract boolean isSuspended();

    @Override
    public String getId() {
        return chatId;
    }

    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        for (String messageId : messageIds) {
            messages.add(Message.getMessageById(messageId));
        }

        messages.sort(Comparator.comparing(Message::getId));
        return messages;
    }

    public void addMessage(String messageId) {
        messageIds.add(messageId);
    }

    public void removeMessage(String messageId) {
        messageIds.remove(messageId);
    }
}
