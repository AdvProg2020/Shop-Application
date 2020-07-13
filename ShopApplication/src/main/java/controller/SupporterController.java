package controller;

import model.account.Account;
import model.database.Database;

import java.text.DateFormat;

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


}
