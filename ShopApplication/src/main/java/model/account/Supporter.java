package model.account;

import model.ModelUtilities;
import model.chat.SupportChat;

import java.util.*;

public class Supporter extends Account {
    protected static Map<String, Supporter> allSupporters = new HashMap<>();
    private static int lastNum = 1;
    private transient Set<String> chatIds;

    public Supporter(String username, String password, String firstName, String lastName, String email, String phone, String image) {
        super(username, password, firstName, lastName, email, phone, image);
        initialize();
    }


    public static List<Supporter> getAllSupporters(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSupporters.values(), suspense);
    }

    public static Supporter getSupporterById(String accountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSupporters, accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSupporters.put(accountId, this);
        lastNum++;
        super.initialize();

        chatIds = new HashSet<>();
    }

    public List<SupportChat> getActiveChats() {
        List<SupportChat> chats = new ArrayList<>();
        for (String chatId : chatIds) {
            chats.add(SupportChat.getSupportChatById(chatId));
        }

        chats.sort(Comparator.comparing(SupportChat::getId));
        return chats;
    }

    public List<SupportChat> getChatArchive() {
        List<SupportChat> chats = new ArrayList<>();
        for (String chatId : chatIds) {
            chats.add(SupportChat.getSupportChatById(chatId, false));
        }
        chats.removeAll(getActiveChats());

        chats.sort(Comparator.comparing(SupportChat::getId));
        return chats;
    }

    public void addChat(String chatId) {
        chatIds.add(chatId);
    }
}
