package controller;

import jdk.jfr.Label;
import model.Discount;
import model.Product;
import model.Rating;
import model.SubProduct;
import model.account.Customer;
import model.account.Seller;
import model.database.DatabaseManager;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.log.ShippingStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO: database constructor
public class CustomerController extends Controller {


    public CustomerController(DatabaseManager DataBaseManager) {
        super(DataBaseManager);
    }

    //Done!!

    /**
     * @return customer:
     * { String firstName, String lastName, String phone, String email, String password, balance}
     */
    public String[] getPersonalInfoEditableFields() {
        String[] editableFields = new String[6];
        editableFields[0] = "firstName";
        editableFields[1] = "lastName";
        editableFields[2] = "phone";
        editableFields[3] = "email";
        editableFields[4] = "password";
        editableFields[5] = "balance";
        return editableFields;
    }

    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException,
            Exceptions.SameAsPreviousValueException {
        super.editPersonalInfo(field, newInformation);
        databaseManager.editAccount();
    }

    //Done!!
    public boolean isDiscountCodeValid(String code) {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount != null)
            return discount.hasCustomerWithId(currentAccount.getId());
        else
            return false;
    }

    //Done!! Todo: Shayan check please
    public void purchaseTheCart(String receiverName, String address, String receiverPhone, String discountCode) throws Exceptions.InsufficientCreditException,
            Exceptions.NotAvailableSubProductsInCart, Exceptions.InvalidDiscountException, Exceptions.EmptyCartException {
        String notAvailableSubProducts;
        Map<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
        if (subProductsInCart.isEmpty())
            throw new Exceptions.EmptyCartException();
        if (!(notAvailableSubProducts = notAvailableSubProductsInCart()).equals(""))
            throw new Exceptions.NotAvailableSubProductsInCart(notAvailableSubProducts);
        double totalPrice = currentCart.getTotalPrice();
        double discountAmount = 0;
        Discount discount = null;
        if (discountCode != null) {
            if (isDiscountCodeValid(discountCode) && (discount = Discount.getDiscountByCode(discountCode)) != null) {
                discountAmount = discount.calculateDiscountAmount(totalPrice);
            } else
                throw new Exceptions.InvalidDiscountException(discountCode);
        }
        double paidMoney = totalPrice - discountAmount;
        if (paidMoney > ((Customer) currentAccount).getBalance())
            throw new Exceptions.InsufficientCreditException(paidMoney, ((Customer) currentAccount).getBalance());
        BuyLog buyLog = new BuyLog(currentAccount.getId(), paidMoney, discountAmount, receiverName, address, receiverPhone, ShippingStatus.PROCESSING);
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
            seller.changeBalance(subProduct.getPriceWithSale() * subProductCount);
        }
        if (discount != null)
            discount.changeCount(currentAccount.getId(), -1);
        ((Customer) currentAccount).changeBalance(-paidMoney);
        databaseManager.purchase();
    }

    //Done!!
    private String notAvailableSubProductsInCart() {
        StringBuilder notAvailableSubProducts = new StringBuilder();
        Map<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
        for (SubProduct subProduct : subProductsInCart.keySet()) {
            if (subProduct.getRemainingCount() < subProductsInCart.get(subProduct)) {
                String notAvailableProduct = "\n" + subProduct.getId() + " number in cart: " + subProductsInCart.get(subProduct) +
                        " available count: " + subProduct.getRemainingCount();
                notAvailableSubProducts.append(notAvailableProduct);
            }
        }
        return notAvailableSubProducts.toString();
    }

    //Done!!
    /**
     * @return ArrayList<String [ 9 ]> : { Id, customerUsername,
     * receiverName, receiverPhone, receiverAddress, date, shippingStatus, paidMoney, totalDiscountAmount}
     * @throws Exceptions.CustomerLoginException
     */
    public ArrayList<String[]> getOrders() throws Exceptions.CustomerLoginException {
        if (currentAccount instanceof Customer) {
            ArrayList<String[]> orders = new ArrayList<>();
            for (BuyLog buyLog : ((Customer) currentAccount).getBuyLogs()) {
                orders.add(orderPack(buyLog));
            }
            return orders;
        } else
            throw new Exceptions.CustomerLoginException();
    }
    //Done!!

    /**
     * @param orderId
     * @return { Id, customerUsername, receiverName, receiverPhone, receiverAddress, date, shippingStatus, paidMoney, totalDiscountAmount}
     * product pack String[8] : { productId, name, brand, sellerUsername, sellerStoreName, count,  }
     * @throws Exceptions.InvalidLogIdException
     */
    public ArrayList<String[]> getOrderWithId(String orderId) throws Exceptions.InvalidLogIdException, Exceptions.CustomerLoginException {
        if (!(currentAccount instanceof Customer))
            throw new Exceptions.CustomerLoginException();
        BuyLog buyLog = null;
        for (BuyLog log : ((Customer) currentAccount).getBuyLogs()) {
            if (log.getId().equals(orderId))
                buyLog = log;
        }
        if (buyLog == null)
            throw new Exceptions.InvalidLogIdException(orderId);
        else {
            ArrayList<String[]> orderInfo = new ArrayList<>();
            orderInfo.add(orderPack(buyLog));
            for (LogItem item : buyLog.getLogItems()) {
                orderInfo.add(logItemPack(item));
            }
            return orderInfo;
        }
    }

    @Label("For showing order methods")
    private String[] orderPack(BuyLog buyLog) {
        String[] orderPack = new String[9];
        orderPack[0] = buyLog.getId();
        orderPack[1] = buyLog.getCustomer().getUsername();
        orderPack[2] = buyLog.getReceiverName();
        orderPack[3] = buyLog.getReceiverPhone();
        orderPack[4] = buyLog.getReceiverAddress();
        orderPack[5] = dateFormat.format(buyLog.getDate());
        orderPack[6] = buyLog.getShippingStatus().toString();
        orderPack[7] = Double.toString(buyLog.getPaidMoney());
        orderPack[8] = Double.toString(buyLog.getTotalDiscountAmount());
        return orderPack;
    }

    @Label("For showing products in an order")
    private String[] logItemPack(LogItem item) {
        String[] productPack = new String[8];
        Product product = item.getSubProduct().getProduct();
        productPack[0] = product.getId();
        productPack[1] = product.getName();
        productPack[2] = product.getBrand();
        productPack[3] = item.getSeller().getUsername();
        productPack[4] = item.getSeller().getStoreName();
        productPack[5] = Integer.toString(item.getCount());
        productPack[6] = Double.toString(item.getPrice());
        productPack[7] = Double.toString(item.getSaleAmount());
        return productPack;
    }

    //Done!! Todo: Shayan check should I add rating to product
    public void rateProduct(String productID, int score) throws
            Exceptions.InvalidProductIdException, Exceptions.HaveNotBoughtException {
        Product product = Product.getProductById(productID);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            for (SubProduct subProduct : product.getSubProducts()) {
                if (subProduct.getCustomers().contains(((Customer) currentAccount))) {
                    new Rating(currentAccount.getId(), productID, score);
                    databaseManager.addRating();
                    return;
                }
            }
            throw new Exceptions.HaveNotBoughtException(productID);
        }
    }

    //Done!!
    public double viewBalance() {
        return ((Customer) currentAccount).getBalance();
    }

    //Done!!
    public ArrayList<String[]> viewDiscountCodes() {
        Map<Discount, Integer> discounts = ((Customer) currentAccount).getDiscounts();
        ArrayList<String[]> discountCodes = new ArrayList<>();
        String[] discountInfo = new String[2];
        for (Discount discount : discounts.keySet()) {
            discountInfo[0] = discount.getDiscountCode();
            discountInfo[1] = Integer.toString(discounts.get(discount));
            discountCodes.add(discountInfo);
        }
        return discountCodes;
    }
}
