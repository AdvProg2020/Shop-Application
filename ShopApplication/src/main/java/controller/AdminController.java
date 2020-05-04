package controller;

import model.Category;
import model.Discount;
import model.Product;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.request.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    public String[] viewUsername(String username) throws Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.NotExistedUsernameException(username);
        else {
            return getPersonalInfo(account);
        }
    }

    //Done!!
    public void deleteUsername(String username) throws Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.NotExistedUsernameException(username);
        account.suspend();
    }

    //Todo: information = String[5];
    public void creatManagerProfile(String username, String[] information) throws Exceptions.ExistedUsernameException {
        if (Account.getAccountByUsername(username) != null)
            throw new Exceptions.ExistedUsernameException(username);
        new Admin(username, information[0], information[1], information[2], information[3], information[4]);
    }

    //Done!! sort?
    public ArrayList<String[]> manageAllProducts() {
        ArrayList<String[]> products = new ArrayList<>();
        String[] productPack = new String[2];
        for (Product product : Product.getAllProducts()) {
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            products.add(productPack);
        }
        return products;
    }

    //Done!!
    public void removeProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else
            product.suspend();
    }

    //Todo
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
    public String[] viewDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else {
            String[] discountInfo = new String[5];
            discountInfo[0] = discount.getDiscountCode();
            discountInfo[1] = dateFormat.format(discount.getStartDate());
            discountInfo[2] = dateFormat.format(discount.getEndDate());
            discountInfo[3] = Double.toString(discount.getMaximumAmount());
            discountInfo[4] = Double.toString(discount.getPercentage());
            return discountInfo;
        }
    }

    //Done!!
    public ArrayList<String[]> peopleWhoHaveThisDiscount(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else {
            HashMap<Customer, Integer> peopleRemainingCount = discount.getCustomers();
            ArrayList<String[]> peopleWithThisCode = new ArrayList<>();
            String[] personPack = new String[2];
            for (Customer customer : peopleRemainingCount.keySet()) {
                personPack[0] = customer.getUsername();
                personPack[1] = Integer.toString(peopleRemainingCount.get(customer));
                peopleWithThisCode.add(personPack);
            }
            return peopleWithThisCode;
        }
    }

    //Todo
    public void editDiscountCode(String code, String field, String newInformation) {
    }

    //Done!!
    public void removeDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
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

    //Todo
    public ArrayList<String> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestId);
        return null;
    }

    //Todo
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

    //Todo
    public void editCategory(String categoryName, String field, String newField) throws Exceptions.InvalidCategoryException, Exceptions.InvalidFieldException {
        if (Category.getCategoryByName(categoryName) == null)
            throw new Exceptions.InvalidCategoryException(categoryName);

    }

    //Done!!
    public void addCategory(String categoryName, String parentCategory, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException {
        if (Category.getCategoryByName(categoryName) != null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else if (Category.getCategoryByName(parentCategory) == null)
            throw new Exceptions.InvalidCategoryException(parentCategory);
        else
            new Category(categoryName, Category.getCategoryByName(parentCategory).getId(), specialProperties);
    }

    //Done!! Shayan: terminate oke?
    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        category.terminate();
    }
}
