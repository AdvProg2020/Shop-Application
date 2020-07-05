package controller;


import model.Cart;
import model.Discount;
import model.Rating;
import model.account.Account;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.log.ShippingStatus;
import model.sellable.Sellable;
import model.sellable.SubSellable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

    //Todo: check please
    public void purchaseTheCart(String receiverName, String address, String receiverPhone, String discountCode) throws Exceptions.InsufficientCreditException,
            Exceptions.NotAvailableSubProductsInCart, Exceptions.InvalidDiscountException, Exceptions.EmptyCartException {
        String notAvailableSubProducts;
        Map<SubSellable, Integer> subProductsInCart = currentCart().getSubSellables();
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
        if (paidMoney > ((Customer) currentAccount()).getWallet().getBalance())
            throw new Exceptions.InsufficientCreditException(paidMoney, ((Customer) currentAccount()).getWallet().getBalance());
        BuyLog buyLog = new BuyLog(currentAccount().getId(), paidMoney, discountAmount, receiverName, address, receiverPhone, ShippingStatus.PROCESSING);
        HashMap<Seller, SellLog> sellLogs = new HashMap<>();
        SellLog sellLog;
        Seller seller;
        int subProductCount;
        for (SubSellable subSellable : subProductsInCart.keySet()) {
            seller = subSellable.getSeller();
            if (sellLogs.containsKey(seller))
                sellLog = sellLogs.get(seller);
            else {
                sellLog = new SellLog(buyLog.getId(), seller.getId());
                sellLogs.put(seller, sellLog);
            }
            subProductCount = subProductsInCart.get(subSellable);
            new LogItem(buyLog.getId(), sellLog.getId(), subSellable.getId(), subProductCount);
            subSellable.changeRemainingCount(-subProductCount);
            seller.getWallet().changeBalance(subSellable.getPriceWithSale() * subProductCount);
        }
        if (discount != null)
            discount.changeCount(currentAccount().getId(), -1);
        ((Customer) currentAccount()).getWallet().changeBalance(-paidMoney);
        currentCart().clearCart();
        database().purchase();
    }

    private String notAvailableSubProductsInCart() {
        StringBuilder notAvailableSubProducts = new StringBuilder();
        Map<SubSellable, Integer> subProductsInCart = currentCart().getSubSellables();
        for (SubSellable subSellable : subProductsInCart.keySet()) {
            if (subSellable.getRemainingCount() < subProductsInCart.get(subSellable)) {
                String notAvailableProduct = "\n" + subSellable.getId() + " number in cart: " + subProductsInCart.get(subSellable) +
                        " available count: " + subSellable.getRemainingCount();
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
            Exceptions.InvalidProductIdException, Exceptions.HaveNotBoughtException {
        Sellable sellable = Sellable.getSellableById(productID);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            if (currentAccount() != null) {
                for (SubSellable subSellable : sellable.getSubSellables()) {
                    if (new ArrayList<>(subSellable.getCustomers()).contains(currentAccount())) {
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

    public boolean hasBought(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null) {
            throw new Exceptions.InvalidProductIdException(productId);
        } else {
            return sellable.hasBought(currentAccount().getId());
        }
    }
}
