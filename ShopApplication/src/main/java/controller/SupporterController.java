package controller;

import model.account.Account;
import model.account.Seller;
import model.account.Supporter;
import model.chat.Chat;
import model.chat.Message;
import model.chat.SupportChat;
import model.database.Database;
import view.Controllers;

import java.text.DateFormat;
import java.util.ArrayList;

public class SupporterController {

    private static final DateFormat dateFormat = Utilities.getDateFormat();
    private Controller mainController;

    public SupporterController(Controller mainController) {
        this.mainController = mainController;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Database database() {
        return mainController.getDatabase();
    }

    public ArrayList<String[]> getActiveChats() throws Exceptions.UnAuthorizedAccountException {
        ArrayList<String[]> chatPacks = new ArrayList<>();
        if(currentAccount().getClass().getSimpleName().equals("Supporter")){
            for (SupportChat chat : ((Supporter) currentAccount()).getActiveChats()) {
                chatPacks.add(Utilities.Pack.supportChat(chat));
            }
            return chatPacks;
        } else {
            throw new Exceptions.UnAuthorizedAccountException();
        }
    }

    public ArrayList<String[]> getArchiveChats() throws Exceptions.UnAuthorizedAccountException {
        ArrayList<String[]> chatPacks = new ArrayList<>();
        if (currentAccount().getClass().getSimpleName().equals("Supporter")) {
            for (SupportChat chat : ((Supporter) currentAccount()).getActiveChats()) {
                chatPacks.add(Utilities.Pack.supportChat(chat));
            }
            return chatPacks;
        } throw new Exceptions.UnAuthorizedAccountException();
    }


    //
    public ArrayList<String[]> viewChat(String chatId) throws Exceptions.InvalidChatIdException {
        SupportChat chat = SupportChat.getSupportChatById(chatId);
        if( chat == null || chat.getSupporter() != currentAccount()){
            throw new Exceptions.InvalidChatIdException(chatId);
        }else {
            ArrayList<String[]> messages = new ArrayList<>();
            String username = currentAccount().getUsername();
            for (Message message : chat.getMessages()) {
                messages.add(Utilities.Pack.message(message, username));
            }
            return messages;
        }
    }

    public String[] viewChatById(String chatId) throws Exceptions.InvalidChatIdException {
        SupportChat chat = SupportChat.getSupportChatById(chatId);
        if (chat == null) throw new Exceptions.InvalidChatIdException(chatId);
        else return Utilities.Pack.supportChat(chat);
    }

    public void sendMessage(String chatId, String text) throws Exceptions.InvalidChatIdException {
        SupportChat chat = SupportChat.getSupportChatById(chatId);
        if( chat == null || chat.getSupporter() != currentAccount()){
            throw new Exceptions.InvalidChatIdException(chatId);
        }else {
            new Message(chatId, currentAccount().getId(), text);
        }
    }

    public void deleteChat(String chatId) throws Exceptions.InvalidChatIdException {
        SupportChat chat = SupportChat.getSupportChatById(chatId);
        if( chat == null || chat.getSupporter() != currentAccount()){
            throw new Exceptions.InvalidChatIdException(chatId);
        }else {
            chat.suspend();
        }
    }

}
