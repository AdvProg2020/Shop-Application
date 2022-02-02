package Server;

import Server.ServerGate.ServerListener;
import Server.model.Category;
import Server.model.database.Database;
import Server.model.database.DatabaseManager;

import java.io.IOException;

public class ServerInitializer {
    public static Database databaseManager = new DatabaseManager();

    public static void main(String[] args) {
        databaseManager.loadAll();
        Category.setSuperCategory();
        databaseManager.createCategory();

        try {
            new ServerListener();
        } catch (IOException e) {
            System.out.println("failed to get the server online. sorry senpai...");
            e.printStackTrace();
        }
    }
}
