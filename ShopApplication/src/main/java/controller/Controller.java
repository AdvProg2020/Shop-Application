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

    public void sortProducts(String availableSort, String categoryName) {
    }

    public void disableSort(String categoryName) {
    }

    public ArrayList<String> filtering(String categoryName, ArrayList<String> filterBy) {
        return null;
    }

    public void disableFilter() {}

    public ArrayList<String> viewCategories() {
        return null;
    }

    public ArrayList<String> showProducts() {
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

    public ArrayList<String> offs() {
        return null;
    }

}
