package controller;

import model.database.Database;
import model.database.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ControllerTest {

    public static Database databaseManager;
    public static Controller mainController;
    public static CustomerController customerController;
    public static AdminController adminController;
    public static SellerController sellerController;

    @BeforeAll
    public static void init() {
        databaseManager = new DatabaseManager();
        mainController = new Controller(databaseManager);
        customerController = new CustomerController(mainController);
        adminController = new AdminController(mainController);
        sellerController = new SellerController(mainController);
        databaseManager.loadAll();
    }

    @Test
    void creatAccount() {
        Assertions.assertDoesNotThrow(() -> mainController.creatAccount("Customer", "unexpectedUser", "salam", "user", "user",
                "salam@user.com", "09113204", 3244, "asdf", "/src/main/resources"));
        Assertions.assertDoesNotThrow(() -> mainController.login("unexpectedUser", "salam"));
        Assertions.assertThrows(Exceptions.UsernameAlreadyTakenException.class, () -> mainController.creatAccount("Customer", "unexpectedUser", "salam", "user", "user",
                "salam@user.com", "09113204", 3244, "asdf", "/src/main/resources"));
        Assertions.assertThrows(Exceptions.AdminRegisterException.class, () -> mainController.creatAccount("Admin", "unexpectedUser2", "salam", "user", "user",
                "salam@user.com", "09113204", 3244, "asdf", "/src/main/resources"));

    }
}