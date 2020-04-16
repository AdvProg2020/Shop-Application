package controller;

import java.util.ArrayList;

public class AdminController extends Controller {
    public String viewPersonalInfo() {
        return null;
    }

    public boolean isFieldValid(String field) {
        return false;
    }

    public void editPersonalInfo(String field, String newContent) {
    }

    public ArrayList<String> manageUsers() {
        return null;
    }

    public boolean isAccountWithUsername() {
        return false;
    }

    public ArrayList<String> viewUsername(String username) {
        return null;
    }

    public void deleteUsername(String username) {
    }

    public void creatManagerProfile(String username, ArrayList<String> information) {
    }

    public void manageAllProducts() {
    }

    public boolean isProductWithId(String productId) {
        return false;
    }

    public void removeProduct(String productId) {
    }

    public void createDiscountCode(ArrayList<String> fields) {
    }

    public ArrayList<String> viewDiscountCodes() {
        return null;
    }

    public boolean isDiscountCodeWithCode(String code) {
        return false;
    }

    public ArrayList<String> viewDiscountCode(String code) {
        return null;
    }

    public void editDiscountCode(String code, String field, String newInformation) {
    }

    public void removeDiscountCode(String code) {
    }

    public ArrayList<String> mangeRequests() {
        return null;
    }

    public boolean isRequestWithId(String requestId) {
        return false;
    }

    public ArrayList<String> detailsOfRequest(String requestId) {
        return null;
    }

    public boolean acceptRequest(String requestID, boolean accepted) {
        return false;
    }

    public ArrayList<String> manageCategories() {
        return null;
    }

    public boolean isCategoryWithName(String categoryName) {
        return false;
    }

    public void editCategory(String categoryName, String field, String newData) {
    }

    public void addCategory(String categoryName, String parentCategoryId) {
    }

    public void createCategory(String categoryName, ArrayList<String> details) {
    }

    public void removeCategory(String categoryName) {
    }
}
