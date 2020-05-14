package model.database;

import java.nio.file.Path;

final class Paths {
    static final Path accounts = Path.of("src/main/resources/database/accounts.txt");
    static final Path requests = Path.of("src/main/resources/database/requests.txt");

    static final Path buyLogs = Path.of("src/main/resources/database/logs/buyLogs.txt");
    static final Path sellLogs = Path.of("src/main/resources/database/logs/sellLogs.txt");
    static final Path logItems = Path.of("src/main/resources/database/logs/logItems.txt");

    static final Path categories = Path.of("src/main/resources/database/categories.txt");
    static final Path products = Path.of("src/main/resources/database/products.txt");
    static final Path subProducts = Path.of("src/main/resources/database/subProducts.txt");
    static final Path carts = Path.of("src/main/resources/database/carts.txt");
    static final Path reviews = Path.of("src/main/resources/database/reviews.txt");
    static final Path ratings = Path.of("src/main/resources/database/ratings.txt");
    static final Path discounts = Path.of("src/main/resources/database/discounts.txt");
    static final Path sales = Path.of("src/main/resources/database/sales.txt");
}
