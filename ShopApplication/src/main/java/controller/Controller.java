package controller;

import model.Product;
import model.Review;
import model.ShoppingCart;
import model.SubProduct;
import model.account.Account;
import model.account.Admin;

import java.util.ArrayList;

public class Controller {
    protected static Account currentAccount;
    protected static ShoppingCart currentCart;

    public boolean isLoggedIn() {
        return false;
    }

    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if(Account.getAccountByUsername(username) == null )
            throw new Exceptions.ExistedUsernameException();
        else if(type.equals("Admin") && !Admin.isFirstAdmin())
            throw new Exceptions.AdminRegisterException();
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

    public ArrayList<String> showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException();
        else{
            ArrayList<String> productInfo = new ArrayList<>();
            productInfo.add(productId);
            productInfo.add(product.getName());
            productInfo.add(product.getBrand());
            productInfo.add("Info text: "+ product.getInfoText());
            productInfo.add("Sub Products: ");
            for (SubProduct subProduct : product.getSubProducts()) {
                productInfo.add(subProduct.getSubProductId()+ " Seller: "+ subProduct.getSeller()+
                        "  Price: "+subProduct.getPrice()+"  Remaining count: "+subProduct.getRemainingCount());
            }
            productInfo.add("Average rating: "+ product.getAverageRating());
            productInfo.add("Reviews: ");
            for (Review review : product.getReviews()) {
                productInfo.add(review.getReviewer()+ ": "+ review.getReviewText());
            }
            return productInfo;
        }
    }

    public ArrayList<String> digest(String productId) {
        return null;
    }

    public void addToCart(String subProductId) throws Exceptions.InvalidSubProductIdException {

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

    public ArrayList<String> reviews(String productId) throws Exceptions.InvalidProductIdException {
        return null;
    }

    public void addReview(String productId, String reviewText) throws Exceptions.InvalidProductIdException, Exceptions.NotLoggedInException {
        if(currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else{
            Product product = Product.getProductById(productId);
            if(product == null)
                throw new Exceptions.InvalidProductIdException();
            else{
                //Todo
                }
        }
    }

    public ArrayList<String> sales() {
        return null;
    }

    protected ArrayList<String> viewCommonPersonalInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add(currentAccount.getUsername());
        info.add(currentAccount.getType());
        info.add(currentAccount.getFirstName());
        info.add(currentAccount.getLastName());
        info.add(currentAccount.getEmail());
        info.add(currentAccount.getPhone());
        return info;

    }

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
}
