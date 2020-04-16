package controller;

import java.util.ArrayList;

public class CustomerController extends Controller {
    public ArrayList<String> viewPersonalInfo() {
        return null;
    }

    public void editInformation(String field, String newInformation) {
    }

    public ArrayList<String> viewCart() {
        return null;
    }

    public ArrayList<String> showProducts() {
        return null;
    }

    public boolean isProductWithIdInCart(String productId) {
        return false;
    }

    public ArrayList<String> viewProductInCart(String productId) {
        return null;
    }

    public void increaseProductInCart(String productId) {
    }

    public void decreaseProductInCart(String productId) {
    }

    public int showTotalPrice() {
        return 0;
    }

    public boolean canPurchase() {
        return false;
    }

    public String purchaseTheCart() {return null; }

    public ArrayList<String> showOrder(String orderId) {
        return null;
    }

    public String rateProduct(String productID, int rate) {
        return null;
    }

    public int viewBalance() {
        return 0;
    }

    public ArrayList<String> viewDiscountCodes() {
        return null;
    }
}
