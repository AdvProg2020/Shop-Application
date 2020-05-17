package model.database;

import com.google.gson.Gson;
import model.*;
import model.account.Account;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager implements Database {
    private Gson gson;

    public DatabaseManager() {
        this.gson = Builder.buildGson();
    }

    private <T> void load(Path path, Class<T> classType) {
        Scanner scanner = Builder.buildScanner(path);
        while (scanner.hasNextLine()) {
            String gsonString = scanner.nextLine();
            ((ModelBasic) gson.fromJson(gsonString, classType)).initialize();
        }
        scanner.close();
    }

    private <T> void update(Path path, Class<T> classType, List<T> allInstances) {
        PrintWriter printWriter = Builder.buildPrintWriter(path);
        if (classType == Category.class) { // writing superCategory first
            printWriter.println(gson.toJson(Category.getSuperCategory(), classType));
        }

        for (T instance : allInstances) {
            printWriter.println(gson.toJson(instance, classType));
        }
        printWriter.close();
    }

    private void updateAccounts() {
        update(Paths.accounts, Account.class, Account.getAllAccounts(false));
    }

    private void updateBuyLogs() {
        update(Paths.buyLogs, BuyLog.class, BuyLog.getAllBuyLogs());
    }

    private void updateSellLogs() {
        update(Paths.sellLogs, SellLog.class, SellLog.getAllSellLogs());
    }

    private void updateLogItems() {
        update(Paths.logItems, LogItem.class, LogItem.getAllLogItems());
    }

    private void updateRequests() {
        update(Paths.requests, Request.class, Request.getRequestArchive());
        update(Paths.requests, Request.class, Request.getPendingRequests());
    }

    private void updateCategories() {
        update(Paths.categories, Category.class, Category.getAllCategories());
    }

    private void updateProducts() {
        update(Paths.products, Product.class, Product.getAllProducts(false));
    }

    private void updateSubProducts() {
        update(Paths.subProducts, SubProduct.class, SubProduct.getAllSubProducts(false));
    }

    private void updateCarts() {
        update(Paths.carts, Cart.class, Cart.getAllCarts());
    }

    private void updateReviews() {
        update(Paths.reviews, Review.class, Review.getAllReviews());
    }

    private void updateRatings() {
        update(Paths.ratings, Rating.class, Rating.getAllRatings());
    }

    private void updateDiscounts() {
        update(Paths.discounts, Discount.class, Discount.getAllDiscounts(false));
    }

    private void updateSales() {
        update(Paths.sales, Sale.class, Sale.getAllSales(false));
    }

    @Override
    public void loadDatabase() {
        load(Paths.accounts, Account.class);
        load(Paths.categories, Category.class);
        load(Paths.products, Product.class);
        load(Paths.subProducts, SubProduct.class);
        load(Paths.carts, Cart.class);
        load(Paths.sales, Sale.class);
        load(Paths.discounts, Discount.class);
        load(Paths.reviews, Review.class);
        load(Paths.ratings, Rating.class);
        load(Paths.requests, Request.class);
        load(Paths.buyLogs, BuyLog.class);
        load(Paths.sellLogs, SellLog.class);
        load(Paths.logItems, LogItem.class);
    }

    @Override
    public void cart() {
        updateCarts();
    }

    @Override
    public void request() {
        updateRequests();
    }

    @Override
    public void createAdmin() {
        updateAccounts();
    }

    @Override
    public void createCustomer() {
        updateAccounts();
        updateCarts();
    }

    @Override
    public void createSeller() {
        updateRequests();
        updateAccounts();
    }

    @Override
    public void editAccount() {
        updateAccounts();
    }

    @Override
    public void removeAdmin() {
        updateAccounts();
    }

    @Override
    public void removeCustomer() {
        updateAccounts();
        updateDiscounts();
        cart();
    }

    @Override
    public void removeSeller() {
        updateAccounts();
        updateSubProducts();
        updateSales();
    }

    @Override
    public void purchase() {
        updateBuyLogs();
        updateSellLogs();
        updateLogItems();
        updateAccounts();
        updateSubProducts();
        updateCarts();
    }

    @Override
    public void createDiscount() {
        updateDiscounts();
    }

    @Override
    public void editDiscount() {
        updateDiscounts();
    }

    @Override
    public void removeDiscount() {
        updateDiscounts();
    }

    @Override
    public void createSale() {
        updateRequests();
        updateSales();
    }

    @Override
    public void editSale() {
        updateRequests();
        updateSales();
    }

    @Override
    public void removeSale() {
        updateSales();
    }

    @Override
    public void createCategory() {
        updateCategories();
    }

    @Override
    public void editCategory() {
        updateCategories();
    }

    @Override
    public void removeCategory() {
        updateCategories();
        removeProduct();
    }

    @Override
    public void createProduct() {
        updateRequests();
        updateProducts();
        updateSubProducts();
    }

    @Override
    public void editProduct() {
        updateRequests();
        updateProducts();
    }

    @Override
    public void removeProduct() {
        updateProducts();
        removeSubProduct();
        updateReviews();
        updateRatings();
    }

    @Override
    public void createSubProduct() {
        updateRequests();
        updateSubProducts();
    }

    @Override
    public void editSubProduct() {
        updateRequests();
        updateSubProducts();
    }

    @Override
    public void removeSubProduct() {
        updateSubProducts();
        updateSales();
        updateCarts();
    }

    @Override
    public void addReview() {
        updateReviews();
    }

    @Override
    public void addRating() {
        updateRatings();
    }
}
