package controller;

import model.Category;
import model.Discount;
import model.Product;
import model.account.Account;
import model.account.Admin;
import model.request.Request;

import java.util.ArrayList;
import java.util.Date;

public class AdminController extends Controller {

    //Done!!
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException {
        editCommonInformation(field, newInformation);
    }

    //Done!!
    public ArrayList<String> manageUsers() {
        ArrayList<String> accounts = new ArrayList<>();
        for (Account account : Account.getAllAccounts()) {
            accounts.add(account.getUsername());
        }
        return accounts;
    }

    //Done!!
    public ArrayList<String> viewUsername(String username) throws Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.NotExistedUsernameException();
        else{
            return getPersonalInfo(account);
        }
    }

    //Done!!
    public void deleteUsername(String username) throws Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if(account == null)
            throw new Exceptions.NotExistedUsernameException();
        account.suspend();
    }

    public void creatManagerProfile(String username, ArrayList<String> information) throws Exceptions.ExistedUsernameException {
        if(Account.getAccountByUsername(username) != null)
            throw new Exceptions.ExistedUsernameException();
        new Admin(username, information.get(0), information.get(1), information.get(2), information.get(3), information.get(4));
    }

    //Done!!todo
    public ArrayList<String> manageAllProducts() {
        ArrayList<String> productIds = new ArrayList<>();
        for (Product product : Product.getAllProducts()) {
            productIds.add(product.getId());
        }
        return productIds;
    }

    //Done!!
    public void removeProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException();
        else
            product.suspend();
    }

    public void createDiscountCode(String discountCode, Date startDate, Date endDate, int percentage, int maximumAmount) {

    }

    //Done!!
    public ArrayList<String> viewDiscountCodes() {
        ArrayList<String> discountCodes = new ArrayList<>();
        for (Discount discount : Discount.getAllDiscounts()) {
            discountCodes.add(discount.getDiscountCode());
        }
        return discountCodes;
    }

    //Done!! Should show people?
    public ArrayList<String> viewDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if(discount == null)
            throw new Exceptions.DiscountCodeException();
        else{
            ArrayList<String> discountInfo = new ArrayList<>();
            discountInfo.add(discount.getDiscountCode());
            discountInfo.add(dateFormat.format(discount.getStartDate()));
            discountInfo.add(dateFormat.format(discount.getEndDate()));
            discountInfo.add(Double.toString(discount.getMaximumAmount()));
            discountInfo.add(Double.toString(discount.getPercentage()));
            return discountInfo;
        }
    }

    public void editDiscountCode(String code, String field, String newInformation) {
    }

    //Done!!
    public void removeDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if(discount == null)
            throw new Exceptions.DiscountCodeException();
        else
            discount.suspend();
    }

    //Done!!
    public ArrayList<String> manageRequests() {
        ArrayList<String> requestIds = new ArrayList<>();
        for (Request request : Request.getAllRequests()) {
            requestIds.add(request.getRequestId());
        }
        return requestIds;
    }

    public ArrayList<String> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId);
        if(request == null)
            throw new Exceptions.InvalidRequestIdException();
        return null;
    }

    public void acceptRequest(String requestID, boolean accepted) throws Exceptions.InvalidRequestIdException {
        Request request;
    }

    //Done!!
    public ArrayList<String> manageCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    public void editCategory(String categoryName, String field, String newField) throws Exceptions.InvalidCategoryException, Exceptions.InvalidFieldException {
        if(Category.getCategoryByName(categoryName) == null)
            throw new Exceptions.InvalidCategoryException(categoryName);

    }

    //Done!!
    public void addCategory(String categoryName, String parentCategory, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException {
        if(Category.getCategoryByName(categoryName) != null)
            throw new Exceptions.InvalidCategoryException();
        else if(Category.getCategoryByName(parentCategory) == null )
            throw new Exceptions.InvalidCategoryException(parentCategory);
        else
            new Category(categoryName, Category.getCategoryByName(parentCategory).getId(), specialProperties);
    }

    //Done!! Shayan: terminate oke?
    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if(category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        category.terminate();
    }
}
