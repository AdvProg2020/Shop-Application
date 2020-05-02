package controller;

import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    protected static Account currentAccount;
    protected static ShoppingCart currentCart;
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    //Done!
    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if(Account.getAccountByUsername(username) == null )
            throw new Exceptions.ExistedUsernameException();
        else if(type.equals("Admin") && !Admin.isFirstAdmin())
            throw new Exceptions.AdminRegisterException();
    }

    //Todo
    public void creatAccount(String type, ArrayList<String> information) {// lazeme inja ham exception bezarim?

    }

    //Todo
    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if(account == null)
            throw new Exceptions.NotExistedUsernameException();
        if(!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        // baraye cart kari lazem nist bokonim? account cart nadare...
    }

    //Todo
    public ArrayList<String> productsStatus() {
        return null;
    }

    //Todo
    private ArrayList<String> sortProducts(String sortBy, ArrayList<String> productIds) {
        return null;
    }

    //Todo
    private ArrayList<String> filterProducts(ArrayList<String> filterBy, ArrayList<String> productIds) {
        return null;
    }

    //Todo
    public ArrayList<String> viewCategories() {
        return null;
    }

    //Todo
    public ArrayList<String> showProducts(String categoryName, String sortBy, ArrayList<String> filterBy) {
        return null;
    }

    //Done!!
    public ArrayList<ArrayList<String>> showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException();
        else{
            if(currentAccount == null)
                product.increaseViewCount();
            else if(currentAccount.getType().equals("customer"))
                product.increaseViewCount();
            ArrayList<ArrayList<String>> productInfo = new ArrayList<>();
            ArrayList<String> infoPack = new ArrayList<>();
            infoPack.add(productId);
            infoPack.add(product.getName());
            infoPack.add(product.getBrand());
            infoPack.add(product.getInfoText());
            infoPack.add(Double.toString(product.getAverageRatingScore()));
            productInfo.add(infoPack);
            for (SubProduct subProduct : product.getSubProducts()) {
                ArrayList<String> subProductPack = new ArrayList<>();
                subProductPack.add(subProduct.getSubProductId());
                subProductPack.add(subProduct.getSeller().getCompanyName());
                subProductPack.add(Double.toString(subProduct.getPrice()));
                subProductPack.add(Integer.toString(subProduct.getRemainingCount()));
                productInfo.add(subProductPack);
            }
            productInfo.add(null);
            for (Review review : product.getReviews()) {
                ArrayList<String> reviewPack = new ArrayList<>();
                reviewPack.add(review.getReviewer().getUsername());
                reviewPack.add(review.getReviewText());
                productInfo.add(reviewPack);
            }
            return productInfo;
        }
    }

    //Not necessary; show Product
    public ArrayList<String> digest(String productId) {
        return null;
    }

    public void addToCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
    }

    public boolean selectSeller(String productId, String sellerId) {
        return false;
    }

    //Todo
    public ArrayList<String> attributes(String productId) {
        return null;
    }

    //Todo
    public ArrayList<String> compare(String productId1, String productId2) {
        return null;
    }

    //Done!!  [ reviewerUsername, reviewText]
    public ArrayList<String[]> reviews(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException();
        else{
            ArrayList<String[]> reviews = new ArrayList<>();
            String[] reviewPack = new String[2];
            for (Review review : product.getReviews()) {
                reviewPack[0] = review.getReviewer().getUsername();
                reviewPack[1] = review.getReviewText();
                reviews.add(reviewPack);
            }
            return reviews;
        }
    }

    //Done!!
    public void addReview(String productId, String reviewText) throws Exceptions.InvalidProductIdException, Exceptions.NotLoggedInException {
        if(currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else{
            if(Product.getProductById(productId) == null)
                throw new Exceptions.InvalidProductIdException();
            else
                new Review(currentAccount.getAccountId(), productId,reviewText);
        }
    }

    //Done!!
    public ArrayList<ArrayList<String>> sales() {
        ArrayList<ArrayList<String>> sales = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            sales.add(getSaleInfo(sale));
        }
        return sales;
    }

    //Done!!
    protected ArrayList<String> getPersonalInfo(Account account) {
        ArrayList<String> info = new ArrayList<>();
        info.add(account.getUsername());
        info.add(account.getType());
        info.add(account.getFirstName());
        info.add(account.getLastName());
        info.add(account.getEmail());
        info.add(account.getPhone());
        if(account.getType().equals("customer"))
            info.add(String.valueOf(((Customer) account).getBalance()));
        else if(account.getType().equals("seller")){
            info.add(String.valueOf(((Seller) account).getBalance()));
            info.add(((Seller) account).getCompanyName());
        }
        return info;
    }

    //Done!!
    public ArrayList<String> viewPersonalInfo() {
        return getPersonalInfo(currentAccount);
    }
    //Done!!
    protected void editCommonInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {
        switch (field) {
            case "firstName":
                currentAccount.setFirstName(newInformation);
                break;
            case "lastName":
                currentAccount.setLastName(newInformation);
                break;
            case "email":
                currentAccount.setEmail(newInformation);
                break;
            case "phone":
                currentAccount.setPhone(newInformation);
                break;
            case "password":
                currentAccount.setPassword(newInformation);
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    protected ArrayList<String> getSaleInfo(Sale sale){
        ArrayList<String> salePack = new ArrayList<>();
        salePack.add(Double.toString(sale.getPercentage()));
        salePack.add(sale.getSeller().getCompanyName());
        salePack.add(dateFormat.format(sale.getStartDate()));
        salePack.add(dateFormat.format(sale.getEndDate()));
        salePack.add(Integer.toString(sale.getSubProducts().size()));
        for (SubProduct subProduct : sale.getSubProducts()) {
            salePack.add(subProduct.getProduct().getName());
            salePack.add(subProduct.getProduct().getProductId());
        }
        return salePack;
    }
}
