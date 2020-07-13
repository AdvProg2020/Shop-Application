package model.chat;

import model.ModelBasic;
import model.ModelUtilities;
import model.account.Account;

import java.util.*;

public class Chat implements ModelBasic {
    private static Map<String, Chat> allChats = new HashMap<>();
    private static int lastNum = 1;
    private String chatId;
    private Set<String> accountIds;
    private transient Set<String> messageIds;

    public Chat(String... initialAccountIds) {
        accountIds = new HashSet<>(Arrays.asList(initialAccountIds));
        initialize();
    }

    public static List<Chat> getAllChats() {
        return ModelUtilities.getAllInstances(allChats.values(), false);
    }

    public static Chat getChatById(String chatId) {
        return ModelUtilities.getInstanceById(allChats, chatId, false);
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
    public boolean isSuspended() {
        return false;
    }

    public void terminate() {
        allChats.remove(chatId);
        for (Message message : getMessages()) {
            message.terminate();
        }
        for (Account account : getAccounts()) {

        }
    }

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

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (String accountId : accountIds) {
            accounts.add(Account.getAccountById(accountId));
        }

        accounts.sort(Comparator.comparing(Account::getId));
        return accounts;
    }

    public void addAccount(String accountId) {
        messageIds.add(accountId);
    }

    public void removeAccount(String messageId) {
        messageIds.remove(messageId);
    }

    public boolean isAccountInChat(Account targetedAccount){
        for (Account account : getAccounts()) {
            if(account == targetedAccount){
                return true;
            }
        }
        return false;
    }
}
