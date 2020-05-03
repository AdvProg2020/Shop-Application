package controller;

import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;

import java.sql.DataTruncation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Controller {
    protected static Account currentAccount;
    protected static ShoppingCart currentCart;
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    //Done!
    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if(Account.getAccountByUsername(username) == null )
            throw new Exceptions.ExistedUsernameException(username);
        else if(type.equalsIgnoreCase("admin") && !Admin.isFirstAdmin())
            throw new Exceptions.AdminRegisterException();
    }

    //Todo
    public void creatAccount(String type, ArrayList<String> information) {// lazeme inja ham exception bezarim?
    }

    //Todo
    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if(account == null)
            throw new Exceptions.NotExistedUsernameException(username);
        if(!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        // baraye cart kari lazem nist bokonim? account cart nadare...
    }

    //Done!!
    public String getType(){
        if (currentAccount == null)
            return "anonymous";
        return currentAccount.getType();
    }

    //Todo
    public ArrayList<String> productsStatus() {
        return null;
    }

    //Done!!
    private ArrayList<Product> sortProducts(String sortBy, ArrayList<Product> products){
        switch (sortBy){
            case "view count":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return Integer.compare(o1.getViewCount(), o2.getViewCount());
                    }
                });
            case "price":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return Double.compare(o1.getMinPrice(), o2.getMinPrice());
                    }
                });
            case "rating score":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
                    }
                });
            case "Name":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            case "category name":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getCategory().getName().compareTo(o2.getCategory().getName());
                    }
                });
        }
        return products;
    }

    //Done!!
    private ArrayList<String> productToId(ArrayList<Product> products){
        ArrayList<String> productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }
        return productIds;
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
    public void showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else{
            product.increaseViewCount();
        }
    }

    //Done!!
    public String[] digest(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        String[] productInfo = new String[5];
        productInfo[0] = productId;
        productInfo[1] = product.getName();
        productInfo[2] = product.getBrand();
        productInfo[3] = product.getInfoText();
        productInfo[4] = Double.toString(product.getAverageRatingScore());
        return productInfo;
    }

    //Todo
    public ArrayList<String> attributes(String productId) {
        return null;
    }

    //Done!!
    public ArrayList<String[]> subProductsOfAProduct(String productId) throws Exceptions.InvalidProductIdException{
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> subProducts = new ArrayList<>();
        String[] subProductPack = new String[4];
        for (SubProduct subProduct : product.getSubProducts()) {
            subProductPack[0] = subProduct.getId();
            subProductPack[1] = subProduct.getSeller().getCompanyName();
            subProductPack[2] = Double.toString(subProduct.getPrice());
            subProductPack[3] = Integer.toString(subProduct.getRemainingCount());
            subProducts.add(subProductPack);
        }
        return subProducts;
    }

    //Done!!
    public ArrayList<String[]> reviewsOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> reviews = new ArrayList<>();
        String[] reviewPack = new String[3];
        for (Review review : product.getReviews()) {
            reviewPack[0] = review.getReviewer().getUsername();
            reviewPack[1] = review.getReviewTitle();
            reviewPack[2] = review.getReviewText();
            reviews.add(reviewPack);
        }
        return reviews;
    }

    //Todo
    public void addToCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
    }

    //Todo
    public boolean selectSeller(String productId, String sellerId) {
        return false;
    }

    //Todo
    public ArrayList<String> compare(String productId1, String productId2) {
        return null;
    }

    //Done!!  [ reviewerUsername, reviewText]
    public ArrayList<String[]> reviews(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
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
                throw new Exceptions.InvalidProductIdException(productId);
            else
                new Review(currentAccount.getId(), productId, reviewText);
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
    protected String[] getPersonalInfo(Account account) {
        String[] info;
        if(account.getType().equals("customer")) {
            info = new String[7];
            info[6] = String.valueOf(((Customer) account).getBalance());
        }else if(account.getType().equals("seller")){
            info = new String[8];
            info[6] = String.valueOf(((Seller) account).getBalance());
            info[7] = ((Seller) account).getCompanyName();
        }else
            info = new String[6];
        info[0] = account.getUsername();
        info[1] = account.getType();
        info[2] = account.getFirstName();
        info[3] = account.getLastName();
        info[4] = account.getEmail();
        info[5] = account.getPhone();
        return info;
    }

    //Done!!
    public String[] viewPersonalInfo() {
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

    //Done!! Todo
    protected ArrayList<String> getSaleInfo(Sale sale){
        ArrayList<String> salePack = new ArrayList<>();
        salePack.add(Double.toString(sale.getPercentage()));
        salePack.add(sale.getSeller().getCompanyName());
        salePack.add(dateFormat.format(sale.getStartDate()));
        salePack.add(dateFormat.format(sale.getEndDate()));
        salePack.add(Integer.toString(sale.getSubProducts().size()));
        for (SubProduct subProduct : sale.getSubProducts()) {
            salePack.add(subProduct.getProduct().getName());
            salePack.add(subProduct.getProduct().getId());
        }
        return salePack;
    }
}
