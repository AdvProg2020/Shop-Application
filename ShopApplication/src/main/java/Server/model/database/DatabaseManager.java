package Server.model.database;

import Server.model.*;
import Server.model.account.*;
import Server.model.chat.AuctionChat;
import Server.model.chat.Chat;
import Server.model.chat.Message;
import Server.model.chat.SupportChat;
import Server.model.log.BuyLog;
import Server.model.log.FileLog;
import Server.model.log.LogItem;
import Server.model.log.SellLog;
import Server.model.request.Request;
import Server.model.sellable.File;
import Server.model.sellable.Product;
import Server.model.sellable.SubFile;
import Server.model.sellable.SubProduct;
import com.google.gson.Gson;

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
        Scanner scanner = DatabaseUtilities.getScanner(FileNames.DATABASE_DIR + fileName);
        while (scanner.hasNextLine()) {
            String gsonString = scanner.nextLine();
            ((ModelBasic) gson.fromJson(gsonString, classType)).initialize();
        }
        scanner.close();
    }

    private <T> void update(String fileName, Class<T> classType, List<? extends T> allInstances) {
        PrintWriter printWriter = DatabaseUtilities.getPrintWriter(FileNames.DATABASE_DIR + fileName);
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

    private void updateCarts() {
        update(FileNames.CART, Cart.class, Cart.getAllCarts());
    }

    private void updateWallets() {
        update(FileNames.WALLET, Wallet.class, Wallet.getAllWallets());
    }

    private void updateChats() {
        update(FileNames.SUPPORT_CHAT, Chat.class, SupportChat.getAllSupportChats(false));
        update(FileNames.AUCTION_CHAT, Chat.class, AuctionChat.getAllAuctionChats());
        update(FileNames.MESSAGE, Message.class, Message.getAllMessages());
    }

    private void updateProductLogs() {
        update(FileNames.BUY_LOG, BuyLog.class, BuyLog.getAllBuyLogs());
        update(FileNames.SELL_LOG, SellLog.class, SellLog.getAllSellLogs());
        update(FileNames.LOG_ITEM, LogItem.class, LogItem.getAllLogItems());
    }

    private void updateFileLogs() {
        update(FileNames.FILE_LOG, FileLog.class, FileLog.getAllFileLogs());
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

    private void updateProducts() {
        update(FileNames.PRODUCT, Product.class, Product.getAllProducts(false));
    }

    private void updateSubProducts() {
        update(FileNames.SUB_PRODUCT, SubProduct.class, SubProduct.getAllSubProducts(false));
    }

    private void updateFiles() {
        update(FileNames.FILE, File.class, File.getAllFiles(false));
    }

    private void updateSubFiles() {
        update(FileNames.SUB_FILE, SubFile.class, SubFile.getAllSubFiles(false));
    }

    private void updateReviews() {
        update(FileNames.REVIEW, Review.class, Review.getAllReviews());
    }

    private void updateRatings() {
        update(FileNames.RATING, Rating.class, Rating.getAllRatings());
    }

    @Override
    public void loadAll() {
        DatabaseUtilities.createMissingDirectory(FileNames.DATABASE_DIR);
        DatabaseUtilities.createMissingDirectory(FileNames.ACCOUNT_IMG_DIR);
        DatabaseUtilities.createMissingDirectory(FileNames.SELLABLE_IMG_DIR);
        DatabaseUtilities.createMissingDirectory(FileNames.FILES_DIR);

        load(FileNames.ADMIN, Account.class);
        load(FileNames.SELLER, Account.class);
        load(FileNames.CUSTOMER, Account.class);
        load(FileNames.SUPPORTER, Account.class);
        load(FileNames.CART, Cart.class);
        load(FileNames.WALLET, Wallet.class);
        load(FileNames.REQUEST, Request.class);
        load(FileNames.CATEGORY, Category.class);
        load(FileNames.PRODUCT, Product.class);
        load(FileNames.SUB_PRODUCT, SubProduct.class);
        load(FileNames.FILE, File.class);
        load(FileNames.SUB_FILE, SubFile.class);
        load(FileNames.SALE, Sale.class);
        load(FileNames.AUCTION, Auction.class);
        load(FileNames.DISCOUNT, Discount.class);
        load(FileNames.REVIEW, Review.class);
        load(FileNames.RATING, Rating.class);
        load(FileNames.BUY_LOG, BuyLog.class);
        load(FileNames.SELL_LOG, SellLog.class);
        load(FileNames.LOG_ITEM, LogItem.class);
        load(FileNames.FILE_LOG, FileLog.class);
        load(FileNames.SUPPORT_CHAT, Chat.class);
        load(FileNames.AUCTION_CHAT, Chat.class);
        load(FileNames.MESSAGE, Message.class);
    }

    @Override
    public void updateAll() {
        updateAdmins();
        updateSellers();
        updateCustomers();
        updateSupporters();
        updateCarts();
        updateWallets();
        updateRequests();
        updateCategories();
        updateProducts();
        updateSubProducts();
        updateFiles();
        updateSubFiles();
        updateSales();
        updateAuctions();
        updateDiscounts();
        updateRatings();
        updateReviews();
        updateProductLogs();
        updateFileLogs();
        updateChats();
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
        updateSubProducts();
        updateSales();
    }

    @Override
    public void removeSupporter() {
        updateSupporters();
        chat();
    }

    @Override
    public void chat() {
        updateChats();
    }

    @Override
    public void cart() {
        updateCarts();
    }

    public void wallet() {
        updateWallets();
    }

    @Override
    public void purchase() {
        updateProductLogs();
        updateFileLogs();
        updateSellers();
        updateCustomers();
        updateSubProducts();
        updateCarts();
    }

    @Override
    public void request() {
        updateRequests();
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
        chat();
    }

    @Override
    public void editAuction() {
        updateRequests();
        updateAuctions();
    }

    @Override
    public void removeAuction() {
        updateAuctions();
        chat();
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
    public void createFile() {
        updateRequests();
        updateFiles();
        updateSubFiles();
    }

    @Override
    public void editFile() {
        updateRequests();
        updateFiles();
    }

    @Override
    public void removeFile() {
        updateFiles();
        removeSubFile();
        updateReviews();
        updateRatings();
    }

    @Override
    public void createSubFile() {
        updateRequests();
        updateSubFiles();
    }

    @Override
    public void editSubFile() {
        updateRequests();
        updateSubFiles();
    }

    @Override
    public void removeSubFile() {
        updateSubFiles();
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
