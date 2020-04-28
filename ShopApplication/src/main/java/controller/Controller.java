package controller;

import model.Product;
import model.ShoppingCart;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;

import java.util.ArrayList;

public class Controller {
    protected Account currentAccount;
    protected ShoppingCart currentCart;

    public boolean isLoggedIn() {
        return false;
    }

    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if(Account.isAccountWithUsername(username))
            throw new Exceptions.ExistedUsernameException();
        //if(Admin.isAnyAdmin())
        //    throw new Exceptions.AdminRegisterException();
    }

    public void creatAccount(String type, ArrayList<String> information) {// lazeme inja ham exception bezarim?

    }

    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if(account == null)
            throw new Exceptions.NotExistedUsernameException();
        if(!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        // baraye cart kari lazem nist bokonim? account cart nadare...
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
