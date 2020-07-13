package controller;

import model.account.Account;
import model.account.Supporter;
import model.chat.Chat;
import model.database.Database;

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

    private ArrayList<String[]> getChatsOfSupporter() throws Exceptions.UnAuthorizedAccountException {
        ArrayList<String[]> chatPacks = new ArrayList<>();
        if(currentAccount().getClass().getSimpleName().equals("Supporter")){
            String username = currentAccount().getUsername();
            for (Chat chat : ((Supporter) currentAccount()).getChats()) {
                chatPacks.add(Utilities.Pack.chat(chat, username));
            }
            return chatPacks;
        }else {
            throw new Exceptions.UnAuthorizedAccountException();
        }
    }
}
