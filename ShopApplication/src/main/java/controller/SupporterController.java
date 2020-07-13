package controller;

import model.account.Account;
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

    private ArrayList<String[]> getChatsOfSupporter(){
        if(currentAccount().getClass().getSimpleName().equals("Supporter")){

        }
    }
}
