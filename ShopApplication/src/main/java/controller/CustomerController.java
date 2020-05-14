package controller;

import jdk.jfr.Label;
import model.Discount;
import model.Product;
import model.Rating;
import model.SubProduct;
import model.account.Customer;
import model.log.BuyLog;
import model.log.LogItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerController extends Controller {

    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException{
        super.editPersonalInfo(field, newInformation);
    }

    //Done!!
    public ArrayList<String[]> getProductsInCart() {
        ArrayList<String[]> shoppingCart = new ArrayList<>();
        Map<SubProduct, Integer> subProducts = ((Customer) currentAccount).getCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(productPackInCart(subProduct, subProducts.get(subProduct)));
        }
        return shoppingCart;
    }

    @Label("For getProductInCart method")
    private String[] productPackInCart( SubProduct subProduct, int count){
        String[] productPack = new String[7];
        productPack[0] = subProduct.getId();
        productPack[1] = subProduct.getProduct().getName();
        productPack[2] = subProduct.getProduct().getBrand();
        productPack[3] = subProduct.getSeller().getUsername();
        productPack[4] = subProduct.getSeller().getStoreName();
        productPack[5] = Integer.toString(count);
        productPack[6] = Double.toString(subProduct.getPriceWithSale()*count);
        return productPack;
    }

    //Done!!
    public void viewProductInCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (!currentCart.getSubProducts().containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            try {
                showProduct(subProduct.getProduct().getId());
            } catch (Exceptions.InvalidProductIdException ignored) {
            }
        }
    }

    //Done!!
    public void increaseProductInCart(String subProductId, int number) throws Exceptions.NotSubProductIdInTheCartException, Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException {
        Map<SubProduct, Integer> subProducts = currentCart.getSubProducts();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (!subProducts.containsKey(subProduct))
            throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        else if (number + subProducts.get(subProduct) > subProduct.getRemainingCount())
            throw new Exceptions.UnavailableProductException(subProductId);
        else
            currentCart.addSubProductCount(subProductId, number);
    }

    //Done!!
    public void decreaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException, Exceptions.NotSubProductIdInTheCartException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            Map<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
            if (subProductsInCart.containsKey(subProduct))
                currentCart.addSubProductCount(subProductId, -number);
            else
                throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        }
    }

    //Done!!
    public double getTotalPriceOfCart() {
        return currentCart.getTotalPrice();
    }

    //Done!!
    public boolean isDiscountCodeValid(String code){
        Discount discount = Discount.getDiscountByCode(code);
        if(discount != null)
            return discount.hasCustomerWithId(currentAccount.getId());
        else
            return false;
    }

    //TODO
    public void purchaseTheCart(String receiverName, String address, String receiverPhone, String discountCode) throws Exceptions.InsufficientCreditException {
        double totalPrice = currentCart.getTotalPrice();
        if( isDiscountCodeValid(discountCode) ){
            Discount discount = Discount.getDiscountByCode(discountCode);

        }
    }

    //Done!!

    /**
     *
     * @return ArrayList<String[9]> : { Id, customerUsername,
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
     *
     * @param orderId
     * @return { Id, customerUsername, receiverName, receiverPhone, receiverAddress, date, shippingStatus, paidMoney, totalDiscountAmount}
     *          product pack String[8] : { productId, name, brand, sellerUsername, sellerStoreName, count,  }
     * @throws Exceptions.InvalidLogIdException
     */
    public ArrayList<String[]> getOrderWithId(String orderId) throws Exceptions.InvalidLogIdException {
        BuyLog buyLog = null;
        for (BuyLog log : ((Customer) currentAccount).getBuyLogs()) {
            if(log.getId().equals(orderId))
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
    private String[] orderPack(BuyLog buyLog){
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
    private String[] logItemPack(LogItem item){
        String[] productPack = new String[8];
        Product product = Product.getProductById(item.getSubProduct().getProductId(), false);
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

    //Done!! Todo: Shayan check please
    public void rateProduct(String productID, int score) throws Exceptions.InvalidProductIdException, Exceptions.HaveNotBoughtException {
        Product product = Product.getProductById(productID, false);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productID);
        else {
            for (SubProduct subProduct : product.getSubProducts()) {
                if(subProduct.getCustomers().contains(((Customer)currentAccount))){
                    Rating rating = new Rating(currentAccount.getId(), productID,score);
                    product.addRating(rating.getId());
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
