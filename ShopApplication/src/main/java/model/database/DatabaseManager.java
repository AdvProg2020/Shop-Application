package model.database;

import com.google.gson.Gson;
import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager implements Database {
    private Gson gson;

    public DatabaseManager() {
        this.gson = DatabaseUtilities.getGson();
    }

    private <T> void load(String fileName, Class<T> classType) {
        Scanner scanner = DatabaseUtilities.getScanner(fileName);
        while (scanner.hasNextLine()) {
            String gsonString = scanner.nextLine();
            ((ModelBasic) gson.fromJson(gsonString, classType)).initialize();
        }
        scanner.close();
    }

    private <T> void update(String fileName, Class<T> classType, List<? extends T> allInstances) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(fileName);
        if (classType == Category.class) // writing superCategory first
            printWriter.println(gson.toJson(Category.getSuperCategory(), classType));

        for (T instance : allInstances) {
            printWriter.println(gson.toJson(instance, classType));
        }
        printWriter.close();
    }

    private void updateAdmins() {
        update(FileNames.ADMIN, Account.class, Admin.getAllAdmins(false));
    }

    private void updateSellers() {
        update(FileNames.SELL_LOG, Account.class, Seller.getAllSellers(false));
    }

    private void updateCustomers() {
        update(FileNames.CUSTOMER, Account.class, Customer.getAllCustomers(false));
    }

    private void updateLogs() {
        update(FileNames.BUY_LOG, BuyLog.class, BuyLog.getAllBuyLogs());
        update(FileNames.SELL_LOG, SellLog.class, SellLog.getAllSellLogs());
        update(FileNames.LOG_ITEM, LogItem.class, LogItem.getAllLogItems());
    }

    private void updateRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        requests.addAll(Request.getRequestArchive());
        requests.addAll(Request.getPendingRequests());
        update(FileNames.REQUEST, Request.class, requests);
    }

    private void updateDiscounts() {
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.addAll(Discount.getDiscountArchive());
        discounts.addAll(Discount.getActiveDiscounts());
        update(FileNames.DISCOUNT, Discount.class, discounts);
    }

    private void updateSales() {
        ArrayList<Sale> sales = new ArrayList<>();
        sales.addAll(Sale.getSaleArchive());
        sales.addAll(Sale.getActiveSales());
        update(FileNames.SALE, Sale.class, sales);
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

    @Override
    public void loadAll() {
        load(FileNames.ADMIN, Account.class);
        load(FileNames.SELLER, Account.class);
        load(FileNames.CUSTOMER, Account.class);
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
        updateAdmins();
        updateSellers();
        updateCustomers();
        updateProducts();
        updateSubProducts();
        updateCategories();
        updateCarts();
        updateSales();
        updateDiscounts();
        updateRatings();
        updateReviews();
        updateRequests();
        updateLogs();
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
        updateAdmins();
    }

    @Override
    public void createCustomer() {
        updateCustomers();
        updateCarts();
    }

    @Override
    public void createSeller() {
        updateRequests();
        updateSellers();
    }

    @Override
    public void editAccount() {
        updateAdmins();
        updateSellers();
        updateCustomers();
    }

    @Override
    public void removeAdmin() {
        updateAdmins();
    }

    @Override
    public void removeCustomer() {
        updateCustomers();
        updateDiscounts();
        updateCarts();
    }

    @Override
    public void removeSeller() {
        updateSellers();
        updateSubProducts();
        updateSales();
    }

    @Override
    public void purchase() {
        updateLogs();
        updateSellers();
        updateCustomers();
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
