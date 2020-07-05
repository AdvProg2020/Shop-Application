package model.database;

import com.google.gson.Gson;
import model.*;
import model.account.*;
import model.chat.Chat;
import model.chat.Message;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;
import model.sellable.Sellable;
import model.sellable.SubSellable;

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
        update(FileNames.SELLER, Account.class, Seller.getAllSellers(false));
    }

    private void updateCustomers() {
        update(FileNames.CUSTOMER, Account.class, Customer.getAllCustomers(false));
    }

    private void updateSupporters() {
        update(FileNames.SUPPORTER, Account.class, Supporter.getAllSupporters(false));
    }

    private void updateLogs() {
        update(FileNames.BUY_LOG, BuyLog.class, BuyLog.getAllBuyLogs());
        update(FileNames.SELL_LOG, SellLog.class, SellLog.getAllSellLogs());
        update(FileNames.LOG_ITEM, LogItem.class, LogItem.getAllLogItems());
    }

    private void updateChats() {
        update(FileNames.CHAT, Chat.class, Chat.getAllChats());
    }

    private void updateMessages() {
        update(FileNames.MESSAGE, Message.class, Message.getAllMessages());
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
        update(FileNames.SALE, Sale.class, Sale.getAllSales(false));
    }

    private void updateAuctions() {
        update(FileNames.AUCTION, Auction.class, Auction.getAllAuctions(false));
    }

    private void updateCategories() {
        update(FileNames.CATEGORY, Category.class, Category.getAllCategories(false));
    }

    private void updateSellables() {
        update(FileNames.SELLABLE, Sellable.class, Sellable.getAllSellables(false));
    }

    private void updateSubSellables() {
        update(FileNames.SUB_SELLABLE, SubSellable.class, SubSellable.getAllSubSellables(false));
    }

    private void updateCarts() {
        update(FileNames.CART, Cart.class, Cart.getAllCarts());
    }

    private void updateWallets() {
        update(FileNames.WALLET, Wallet.class, Wallet.getAllWallets());
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
        load(FileNames.SUPPORTER, Account.class);
        load(FileNames.CATEGORY, Category.class);
        load(FileNames.SELLABLE, Sellable.class);
        load(FileNames.SUB_SELLABLE, SubSellable.class);
        load(FileNames.CART, Cart.class);
        load(FileNames.WALLET, Wallet.class);
        load(FileNames.SALE, Sale.class);
        load(FileNames.AUCTION, Auction.class);
        load(FileNames.DISCOUNT, Discount.class);
        load(FileNames.REVIEW, Review.class);
        load(FileNames.RATING, Rating.class);
        load(FileNames.REQUEST, Request.class);
        load(FileNames.BUY_LOG, BuyLog.class);
        load(FileNames.SELL_LOG, SellLog.class);
        load(FileNames.LOG_ITEM, LogItem.class);
        // TODO: load chats
    }

    @Override
    public void updateAll() {
        updateAdmins();
        updateSellers();
        updateCustomers();
        updateSellables();
        updateSubSellables();
        updateCategories();
        updateCarts();
        updateWallets();
        updateSales();
        updateAuctions();
        updateDiscounts();
        updateRatings();
        updateReviews();
        updateRequests();
        updateLogs();
        //TODO: update chats
    }

    @Override
    public void cart() {
        updateCarts();
    }

    public void wallet() {
        updateWallets();
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
        updateWallets();
    }

    @Override
    public void createSeller() {
        updateRequests();
        updateSellers();
        updateWallets();
    }

    @Override
    public void createSupporter() {
        updateSupporters();
    }

    @Override
    public void editAccount() {
        updateAdmins();
        updateSellers();
        updateCustomers();
        updateSupporters();
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
        updateSubSellables();
        updateSales();
    }

    @Override
    public void removeSupporter() {
        updateSupporters();
        chat();
    }

    @Override
    public void purchase() {
        updateLogs();
        updateSellers();
        updateCustomers();
        updateSubSellables();
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
    public void createAuction() {
        updateRequests();
        updateAuctions();
    }

    @Override
    public void editAuction() {
        updateRequests();
        updateAuctions();
    }

    @Override
    public void removeAuction() {
        updateAuctions();
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
        removeSellable();
    }

    @Override
    public void createSellable() {
        updateRequests();
        updateSellables();
        updateSubSellables();
    }

    @Override
    public void editSellable() {
        updateRequests();
        updateSellables();
    }

    @Override
    public void removeSellable() {
        updateSellables();
        removeSubSellable();
        updateReviews();
        updateRatings();
    }

    @Override
    public void createSubSellable() {
        updateRequests();
        updateSubSellables();
    }

    @Override
    public void editSubSellable() {
        updateRequests();
        updateSubSellables();
    }

    @Override
    public void removeSubSellable() {
        updateSubSellables();
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

    @Override
    public void chat() {
        updateChats();
        updateMessages();
    }
}
