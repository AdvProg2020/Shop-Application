package Server.controller;


import Server.model.*;
import Server.model.account.*;
import Server.model.chat.Chat;
import Server.model.chat.SupportChat;
import Server.model.database.Database;
import Server.model.log.*;
import Server.model.sellable.Product;
import Server.model.sellable.SubFile;
import Server.model.sellable.SubProduct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomerController {

    private Controller mainController;

    public CustomerController(Controller controller) {
        mainController = controller;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Cart currentCart() {
        return mainController.getCurrentCart();
    }

    private Database database() {
        return mainController.getDatabase();
    }

    /**
     * @return customer:
     * { String firstName, String lastName, String phone, String email, String password, balance}
     */
    public String[] getPersonalInfoEditableFields() {
        return Utilities.Field.customerPersonalInfoEditableFields();
    }

    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException,
            Exceptions.SameAsPreviousValueException {
        if (field.equals("balance")) {
            if (((Customer) currentAccount()).getWallet().getBalance() == Double.parseDouble(newInformation))
                throw new Exceptions.SameAsPreviousValueException(newInformation);
            ((Customer) currentAccount()).getWallet().changeBalance(Double.parseDouble(newInformation) - ((Customer) currentAccount()).getWallet().getBalance());
        } else
            mainController.editPersonalInfo(field, newInformation);
        database().editAccount();
    }

    private boolean isDiscountCodeValid(String code) {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount != null)
            return discount.hasCustomerWithId(currentAccount().getId());
        else
            return false;
    }

    public double getTotalPriceOfCartWithDiscount(String discountCode) throws Exceptions.InvalidDiscountException {
        Discount discount = Discount.getDiscountByCode(discountCode);
        if (discount == null || ! discount.hasCustomerWithId(currentAccount().getId())) {
            throw new Exceptions.InvalidDiscountException(discountCode);
        } else {
            return discount.calculateDiscountAmount(currentCart().getTotalPrice());
        }
    }

    public double getTotalPriceOfFileWithDiscount(String discountCode, double fileCost) throws Exceptions.InvalidDiscountException {
        Discount discount = Discount.getDiscountByCode(discountCode);
        if (discount == null || ! discount.hasCustomerWithId(currentAccount().getId())) {
            throw new Exceptions.InvalidDiscountException(discountCode);
        } else {
            return discount.calculateDiscountAmount(fileCost);
        }
    }

    //Todo: check please
    public void purchaseTheCart(String receiverName, String address, String receiverPhone, String discountCode) throws Exceptions.InsufficientCreditException,
            Exceptions.NotAvailableSubProductsInCart, Exceptions.InvalidDiscountException, Exceptions.EmptyCartException {
        String notAvailableSubProducts;
        Map<SubProduct, Integer> subProductsInCart = currentCart().getSubProducts();
        if (subProductsInCart.isEmpty())
            throw new Exceptions.EmptyCartException();
        if (!(notAvailableSubProducts = notAvailableSubProductsInCart()).isEmpty())
            throw new Exceptions.NotAvailableSubProductsInCart(notAvailableSubProducts);
        double totalPrice = currentCart().getTotalPrice();
        double discountAmount = 0;
        Discount discount = null;
        if (discountCode != null) {
            if (isDiscountCodeValid(discountCode) && (discount = Discount.getDiscountByCode(discountCode)) != null) {
                discountAmount = discount.calculateDiscountAmount(totalPrice);
            } else
                throw new Exceptions.InvalidDiscountException(discountCode);
        }
        double paidMoney = totalPrice - discountAmount;
        if (Wallet.getMinBalance() > ((Customer) currentAccount()).getWallet().getBalance() - paidMoney)
            throw new Exceptions.InsufficientCreditException(paidMoney, ((Customer) currentAccount()).getWallet().getBalance());
        BuyLog buyLog = new BuyLog(currentAccount().getId(), paidMoney, discountAmount, receiverName, address, receiverPhone, ShippingStatus.PROCESSING);
        HashMap<Seller, SellLog> sellLogs = new HashMap<>();
        SellLog sellLog;
        Seller seller;
        int subProductCount;
        for (SubProduct subProduct : subProductsInCart.keySet()) {
            seller = subProduct.getSeller();
            if (sellLogs.containsKey(seller))
                sellLog = sellLogs.get(seller);
            else {
                sellLog = new SellLog(buyLog.getId(), seller.getId());
                sellLogs.put(seller, sellLog);
            }
            subProductCount = subProductsInCart.get(subProduct);
            new LogItem(buyLog.getId(), sellLog.getId(), subProduct.getId(), subProductCount);
            subProduct.changeRemainingCount(-subProductCount);
            seller.getWallet().changeBalance(subProduct.getPriceWithSale() * subProductCount*(100 - Admin.getCommission())/100);
        }
        if (discount != null)
            discount.changeCount(currentAccount().getId(), -1);
        ((Customer) currentAccount()).getWallet().changeBalance(-paidMoney);
        currentCart().clearCart();
        database().purchase();
    }

    public void purchaseTheFile(String subFileId, String discountCode) throws Exceptions.InvalidFileIdException, Exceptions.InvalidDiscountException, Exceptions.InsufficientCreditException {
        SubFile subFile = SubFile.getSubFileById(subFileId);
        Discount discount = null;
        if(subFile == null){
            throw new Exceptions.InvalidFileIdException(subFileId);
        }else {
            double totalPrice = subFile.getPriceWithSale();
            double discountAmount = 0;
            if( discountCode != null){
                discount = Discount.getDiscountByCode(discountCode);
                if (isDiscountCodeValid(discountCode) && (discount = Discount.getDiscountByCode(discountCode)) != null) {
                    discountAmount = discount.calculateDiscountAmount(totalPrice);
                    totalPrice -= discountAmount;
                } else
                    throw new Exceptions.InvalidDiscountException(discountCode);
            }
            if (Wallet.getMinBalance() > ((Customer) currentAccount()).getWallet().getBalance() - totalPrice)
                throw new Exceptions.InsufficientCreditException(totalPrice, ((Customer) currentAccount()).getWallet().getBalance());
            FileLog fileLog = new FileLog(subFileId, currentAccount().getId(), discountAmount);
            if (discount != null)
                discount.changeCount(currentAccount().getId(), -1);
            ((Customer) currentAccount()).getWallet().changeBalance(-totalPrice);
            (SubFile.getSubSellableById(subFileId)).getSeller().getWallet().changeBalance(totalPrice * (100 - Admin.getCommission())/100);
            database().purchase();
        }
    }

    public byte[] downloadFile(String subFileId) throws Exceptions.InvalidFileIdException, Exceptions.HaveNotBoughtException {
        SubFile subFile = SubFile.getSubFileById(subFileId);
        try {
            if(subFile != null ){
                if( subFile.hasCustomerWithId(currentAccount().getId())){
                    return Files.readAllBytes(Paths.get(subFile.getDownloadPath()));
                }else {
                    throw new Exceptions.HaveNotBoughtException(subFileId);
                }
            }else {
                throw new Exceptions.InvalidFileIdException(subFileId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String notAvailableSubProductsInCart() {
        StringBuilder notAvailableSubProducts = new StringBuilder();
        Map<SubProduct, Integer> subProductsInCart = currentCart().getSubProducts();
        for (SubProduct subProduct : subProductsInCart.keySet()) {
            if (subProduct.getRemainingCount() < subProductsInCart.get(subProduct)) {
                String notAvailableProduct = "\n" + subProduct.getId() + " number in cart: " + subProductsInCart.get(subProduct) +
                        " available count: " + subProduct.getRemainingCount();
                notAvailableSubProducts.append(notAvailableProduct);
            }
        }
        return notAvailableSubProducts.toString();
    }

    /**
     * @return ArrayList<String [ 9 ]> : { Id, customerUsername,
     * receiverName, receiverPhone, receiverAddress, date, shippingStatus, paidMoney, totalDiscountAmount}
     * @throws Exceptions.CustomerLoginException
     */
    public ArrayList<String[]> getOrders() throws Exceptions.CustomerLoginException {
        if (currentAccount() instanceof Customer) {
            ArrayList<String[]> orders = new ArrayList<>();
            for (BuyLog buyLog : ((Customer) currentAccount()).getBuyLogs()) {
                orders.add(Utilities.Pack.buyLog(buyLog));
            }
            return orders;
        } else
            throw new Exceptions.CustomerLoginException();
    }

    /**
     * @param orderId
     * @return { Id, customerUsername, receiverName, receiverPhone, receiverAddress, date, shippingStatus, paidMoney, totalDiscountAmount}
     * product pack String[8] : { productId, name, brand, sellerUsername, sellerStoreName, count,  }
     * @throws Exceptions.InvalidLogIdException
     */
    public ArrayList<String[]> getOrderWithId(String orderId) throws Exceptions.InvalidLogIdException, Exceptions.CustomerLoginException {
        if (!(currentAccount() instanceof Customer))
            throw new Exceptions.CustomerLoginException();
        BuyLog buyLog = null;
        for (BuyLog log : ((Customer) currentAccount()).getBuyLogs()) {
            if (log.getId().equals(orderId))
                buyLog = log;
        }
        if (buyLog == null)
            throw new Exceptions.InvalidLogIdException(orderId);
        else {
            ArrayList<String[]> orderInfo = new ArrayList<>();
            orderInfo.add(Utilities.Pack.buyLog(buyLog));
            for (LogItem item : buyLog.getLogItems()) {
                orderInfo.add(Utilities.Pack.buyLogItem(item));
            }
            return orderInfo;
        }
    }

    public void rateProduct(String productID, int score) throws
            Exceptions.InvalidSellableIdException, Exceptions.HaveNotBoughtException {
        Product product = Product.getProductById(productID);
        if (product == null)
            throw new Exceptions.InvalidSellableIdException(productID);
        else {
            if (currentAccount() != null) {
                for (SubProduct subProduct : product.getSubProducts()) {
                    if (new ArrayList<>(subProduct.getCustomers()).contains(currentAccount())) {
                        new Rating(currentAccount().getId(), productID, score);
                        database().addRating();
                        return;
                    }
                }
                throw new Exceptions.HaveNotBoughtException(productID);
            }
        }
    }

    public double viewBalance() {
        return ((Customer) currentAccount()).getWallet().getBalance();
    }

    public ArrayList<String[]> viewDiscountCodes() {
        Map<Discount, Integer> discounts = ((Customer) currentAccount()).getDiscounts();
        ArrayList<String[]> discountCodes = new ArrayList<>();
        String[] discountInfo = new String[5];
        for (Discount discount : discounts.keySet()) {
            discountInfo[0] = discount.getDiscountCode();
            discountInfo[1] = Integer.toString(discounts.get(discount));
            discountInfo[2] = Utilities.getDateFormat().format(discount.getEndDate());
            discountInfo[3] = Double.toString(discount.getMaximumAmount());
            discountInfo[4] = Double.toString(discount.getPercentage());
            discountCodes.add(discountInfo);
        }
        return discountCodes;
    }

    public double getTotalPriceOfCart() throws Exceptions.UnAuthorizedAccountException {
        return mainController.getTotalPriceOfCart();
    }

    public boolean hasBought(String productId) throws Exceptions.InvalidSellableIdException {
        Product product = Product.getProductById(productId);
        if(product == null){
            throw new Exceptions.InvalidSellableIdException(productId);
        }else {
            return product.hasBought(currentAccount().getId());
        }
    }


    public String getSupportChatId() throws Exceptions.DontHaveChatException {
        SupportChat chat = ((Customer) currentAccount()).getSupportChat();
        if( chat == null ){
            throw new Exceptions.DontHaveChatException();
        }else {
            return chat.getId();
        }
    }

    public String createSupportChat(String supporterId) throws Exceptions.AlreadyInAChatException, Exceptions.InvalidSupporterIdException {
        Chat chat = ((Customer)currentAccount()).getSupportChat();
        if (((Customer) currentAccount()).getSupportChat() != null) {
            throw new Exceptions.AlreadyInAChatException(chat.getId());
        } else {
            if (Supporter.getSupporterById(supporterId) == null) {
                throw new Exceptions.InvalidSupporterIdException(supporterId);
            } else {
                chat = new SupportChat(supporterId, currentAccount().getId());
                database().chat();
                return chat.getId();
            }
        }
    }

    public void deleteSupportChat(String chatId) throws Exceptions.InvalidChatIdException {
        SupportChat chat = SupportChat.getSupportChatById(chatId);
        if( chat == null || chat.getCustomer() != currentAccount()){
            throw new Exceptions.InvalidChatIdException(chatId);
        }else {
            chat.suspend();
            database().chat();
        }
    }

    public String[] viewAuction(String auctionId) throws Exceptions.InvalidAuctionIdException {
        Auction auction = Auction.getAuctionById(auctionId);
        if( auction == null ){
            throw new Exceptions.InvalidAuctionIdException(auctionId);
        }else {
            return Utilities.Pack.auction(auction);
        }
    }


    public void bid(String auctionId, double bidAmount) throws Exceptions.InvalidAuctionIdException {
        Auction auction = Auction.getAuctionById(auctionId);
        if(auction == null){
            throw new Exceptions.InvalidAuctionIdException(auctionId);
        }else {
            auction.bid(currentAccount().getId(), bidAmount);
            database().editAuction();
        }
    }

    public ArrayList<String[]> getAllSupporters() {
        return Supporter.getAllSupporters().stream().map(s -> new String[] {s.getId(), s.getUsername()}).collect(Collectors.toCollection(ArrayList::new));
    }
}
