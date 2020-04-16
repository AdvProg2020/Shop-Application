package controller;

import model.ShoppingCart;
import model.account.Account;

import java.util.ArrayList;

public class Controller {
    protected Account currentAccount;
    protected ShoppingCart currentCart;

    public boolean isLoggedIn() {
        return false;
    }

    public String usernameTypeValidation(String username, String type) {
        return null;
    }

    public void creatAccount(String type, String information) {
    }

    public String login(String username, String password) {
        return null;
    }

    public ArrayList<String> productsStatus() {
        return null;
    }

    private ArrayList<String> sortProducts(String sortBy, ArrayList<String> productIds) {
        return null;
    }

    private ArrayList<String> filterProducts(ArrayList<String> filterBy, ArrayList<String> productIds) {
        return null;
    }

    public ArrayList<String> viewCategories() {
        return null;
    }

    public ArrayList<String> showProducts(String categoryName, String sortBy, ArrayList<String> filterBy) {
        return null;
    }

    public ArrayList<String> showProduct() {
        return null;
    }

    public ArrayList<String> digest(String productId) {
        return null;
    }

    public boolean canAddToTheCart(String productId) {
        return false;
    }

    public void addToCart(String productId) {
    }

    public boolean selectSeller(String productId, String sellerId) {
        return false;
    }

    public ArrayList<String> attributes(String productId) {
        return null;
    }

    public ArrayList<String> compare(String productId1, String productId2) {
        return null;
    }

    public ArrayList<String> comments(String productId) {
        return null;
    }

    public void addComment(String productId, String title, String content) {
    }

    public ArrayList<String> sales() {
        return null;
    }

}
