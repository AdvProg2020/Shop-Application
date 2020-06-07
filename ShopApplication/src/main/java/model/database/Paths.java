package model.database;

import java.nio.file.Path;

final class Paths {

    static final Path accounts = getPath("accounts");
    static final Path requests = getPath("requests");

    static final Path buyLogs = getPath("log/buyLogs");
    static final Path sellLogs = getPath("log/sellLogs");
    static final Path logItems = getPath("log/logItems");

    static final Path categories = getPath("categories");
    static final Path products = getPath("products");
    static final Path subProducts = getPath("subProducts");
    static final Path carts = getPath("carts");
    static final Path reviews = getPath("reviews");
    static final Path ratings = getPath("ratings");
    static final Path discounts = getPath("discounts");
    static final Path sales = getPath("sales");

    private static Path getPath(String fileName) {
        return Path.of("./src/main/resources/database/" + fileName + ".txt");
    }
}
