package model.database;

import com.google.gson.Gson;
import model.*;
import model.account.Account;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager implements Database {
    private Gson gson;

    public DatabaseManager() {
        this.gson = Builder.buildGson();
    }

    private <T> void load(String fileName, Class<T> classType) {
        Scanner scanner = Builder.buildScanner(fileName);
        while (scanner.hasNextLine()) {
            String gsonString = scanner.nextLine();
            ((ModelBasic) gson.fromJson(gsonString, classType)).initialize();
        }
        scanner.close();
    }

    private <T> void update(String fileName, Class<T> classType, List<T> allInstances) {
        PrintWriter printWriter = Builder.buildPrintWriter(fileName);
        if (classType == Category.class) { // writing superCategory first
            printWriter.println(gson.toJson(Category.getSuperCategory(), classType));
        }

        for (T instance : allInstances) {
            printWriter.println(gson.toJson(instance, classType));
        }
        printWriter.close();
    }

    private void updateAccounts() {
        update(FileNames.ACCOUNT, Account.class, Account.getAllAccounts(false));
    }

    private void updateBuyLogs() {
        update(FileNames.BUY_LOG, BuyLog.class, BuyLog.getAllBuyLogs());
    }

    private void updateSellLogs() {
        update(FileNames.SELL_LOG, SellLog.class, SellLog.getAllSellLogs());
    }

    private void updateLogItems() {
        update(FileNames.LOG_ITEM, LogItem.class, LogItem.getAllLogItems());
    }

    private void updateRequests() {
        update(FileNames.REQUEST, Request.class, Request.getRequestArchive());
        update(FileNames.REQUEST, Request.class, Request.getPendingRequests());
    }

    private void updateCategories() {
        update(FileNames.CATEGORY, Category.class, Category.getAllCategories(false));
    }

    private void updateProducts() {
        update(FileNames.PRODUCT, Product.class, Product.getAllProducts(false));
    }

    private void updateSubProducts() {
        update(FileNames.SUB_PRODUCT, SubProduct.class, SubProduct.getAllSubProducts(false));
    }

    private void updateCarts() {
        update(FileNames.CART, Cart.class, Cart.getAllCarts());
    }

    private void updateReviews() {
        update(FileNames.REVIEW, Review.class, Review.getAllReviews());
    }

    private void updateRatings() {
        update(FileNames.RATING, Rating.class, Rating.getAllRatings());
    }

    private void updateDiscounts() {
        update(FileNames.DISCOUNT, Discount.class, Discount.getAllDiscounts(false));
    }

    private void updateSales() {
        update(FileNames.SALE, Sale.class, Sale.getAllSales(false));
    }

    @Override
    public void loadAll() {
        load(FileNames.ACCOUNT, Account.class);
        load(FileNames.CATEGORY, Category.class);
        load(FileNames.PRODUCT, Product.class);
        load(FileNames.SUB_PRODUCT, SubProduct.class);
        load(FileNames.CART, Cart.class);
        load(FileNames.SALE, Sale.class);
        load(FileNames.DISCOUNT, Discount.class);
        load(FileNames.REVIEW, Review.class);
        load(FileNames.RATING, Rating.class);
        load(FileNames.REQUEST, Request.class);
        load(FileNames.BUY_LOG, BuyLog.class);
        load(FileNames.SELL_LOG, SellLog.class);
        load(FileNames.LOG_ITEM, LogItem.class);
    }

    @Override
    public void updateAll() {
        updateAccounts();
        updateProducts();
        updateSubProducts();
        updateCategories();
        updateCarts();
        updateSales();
        updateDiscounts();
        updateRatings();
        updateReviews();
        updateRequests();
        updateBuyLogs();
        updateSellLogs();
        updateLogItems();
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
