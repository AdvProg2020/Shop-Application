package controller;

import model.Discount;
import model.Product;
import model.Rating;
import model.SubProduct;
import model.account.Customer;
import model.log.BuyLog;
import model.log.LogItem;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerController extends Controller {

    //Done!! can edit credit?
    public void editInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {
        editCommonInformation(field, newInformation);
    }

    //Done!!
    public ArrayList<String> viewCart() {
        return showProductsInCart();
    }

    //Done!!
    public ArrayList<String> showProductsInCart() {
        ArrayList<String> shoppingCart = new ArrayList<>();
        HashMap<SubProduct, Integer> subProducts = ((Customer)currentAccount).getShoppingCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(subProduct.getId() + "   " + subProduct.getProduct().getName() + "  " + subProduct.getSeller().getCompanyName() + " number in carts: " + subProducts.get(subProduct));
        }
        return shoppingCart;
    }

    //Done!!
    public ArrayList<ArrayList<String>> viewProductInCart(String subProductId) throws Exceptions.InvalidSubProductIdException{
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if(!currentCart.getSubProducts().containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else{
            try {
                return showProduct(subProduct.getProduct().getId());
            }
            catch (Exceptions.InvalidProductIdException ex) {
                return null;
            }
        }
    }

    //Done!!
    public void increaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException, Exceptions.UnavailableProductException {
        HashMap<SubProduct, Integer> subProducts = currentCart.getSubProducts();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if(subProduct == null)
            throw new Exceptions.InvalidSubProductIdException();
        else if(!subProducts.containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if(number + subProducts.get(subProduct) > subProduct.getRemainingCount())
            throw new Exceptions.UnavailableProductException();
        else
            currentCart.addSubProductCount(subProductId, number);
    }

    //Done!!
    public void decreaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if( subProduct == null)
            throw new Exceptions.InvalidSubProductIdException();
        else{
            HashMap<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
            if(subProductsInCart.containsKey(subProduct))
                currentCart.addSubProductCount(subProductId, -number);
            else
                throw new Exceptions.InvalidSubProductIdException(subProductId);
        }
    }

    //Done!!
    public double showTotalPriceOfCart() {
        HashMap<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
        double totalSum = 0;
        for (SubProduct subProduct : subProductsInCart.keySet()) {
            totalSum += subProduct.getPriceWithSale() * subProductsInCart.get(subProduct);
        }
        return totalSum;
    }

    public String purchaseTheCart() {
        return null;
    }

    //Done!!
    public ArrayList<String> viewOrders() throws Exceptions.NotLoggedInException {
        if(currentAccount.getType().equals("customer")){
            ArrayList<String> orderIds = new ArrayList<>();
            for (BuyLog buyLog : ((Customer) currentAccount).getBuyLogs()) {
                orderIds.add(buyLog.getId());
            }
            return orderIds;
        }else
            throw new Exceptions.NotLoggedInException("customer");
    }

    //Done!!
    public ArrayList<ArrayList<String>> showOrder(String orderId) throws Exceptions.InvalidLogIdException {
        BuyLog buyLog = BuyLog.getBuyLogById(orderId);
        if(buyLog == null)
            throw new Exceptions.InvalidLogIdException(orderId);
        else{
            ArrayList<ArrayList<String>> orderInfo = new ArrayList<>();
            ArrayList<String> infoPack = new ArrayList<>();
            infoPack.add(orderId);
            infoPack.add(buyLog.getCustomer().getUsername());
            infoPack.add(buyLog.getReceiverName());
            infoPack.add(buyLog.getReceiverPhone());
            infoPack.add(buyLog.getReceiverAddress());
            infoPack.add(dateFormat.format(buyLog.getDate()));
            infoPack.add(buyLog.getShippingStatus().toString());
            infoPack.add(Double.toString(buyLog.getPaidMoney()));
            infoPack.add(Double.toString(buyLog.getTotalDiscountAmount()));
            orderInfo.add(infoPack);
            for (LogItem item : buyLog.getLogItems()) {//Todo:(Shayan) this getter should pass even it was suspended!
                ArrayList<String> productPack = new ArrayList<>();
                Product product = item.getSubProduct().getProduct();
                productPack.add(product.getName());
                productPack.add(product.getId());
                productPack.add(item.getSeller().getUsername());
                productPack.add(item.getSeller().getCompanyName());
                productPack.add(Integer.toString(item.getCount()));
                productPack.add(Double.toString(item.getUnitPrice()*item.getCount()));
                productPack.add(Double.toString(item.getSalePercentage()));
                orderInfo.add(productPack);
            }
            return orderInfo;
        }
    }

    //Done!!
    public void rateProduct(String productID, int rate) throws Exceptions.InvalidProductIdException {
        for (SubProduct subProduct : currentCart.getSubProducts().keySet()) {
            if (subProduct.getProduct().getId().equals(productID)) {
                new Rating(currentAccount.getId(), productID, rate);
                return;
            }
        }
        throw new Exceptions.InvalidProductIdException(productID);
    }

    //Done!!
    public double viewBalance() {
        return ((Customer) currentAccount).getBalance();
    }

    //Done!!
    public ArrayList<String[]> viewDiscountCodes() {
        HashMap<Discount, Integer> discounts = ((Customer)currentAccount).getDiscounts();
        ArrayList<String[]> discountCodes = new ArrayList<>();
        String[] discountInfo =new String[2];
        for (Discount discount : discounts.keySet()) {
            discountInfo[0] = discount.getDiscountCode();
            discountInfo[1] = Integer.toString(discounts.get(discount));
            discountCodes.add(discountInfo);
        }
        return discountCodes;
    }
}
