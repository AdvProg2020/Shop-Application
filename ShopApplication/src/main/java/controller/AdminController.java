package controller;

import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.request.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AdminController extends Controller {

    //Done!!
    @Override
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        super.editPersonalInfo(field, newInformation);
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

    //Done!!
    public void creatManagerProfile(String username, String password, String firstName, String lastName, String email, String phone) throws Exceptions.ExistedUsernameException {
        if (Account.getAccountByUsername(username) != null)
            throw new Exceptions.ExistedUsernameException(username);
        new Admin(username, password, firstName, lastName, email, phone);
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

    //Done!!
    public void createDiscountCode(String discountCode, Date startDate, Date endDate, int percentage, int maximumAmount) throws Exceptions.ExistingDiscountCodeException {
        if(Discount.getDiscountByCode(discountCode) != null)
            throw new Exceptions.ExistingDiscountCodeException(discountCode);
        else
            new Discount(discountCode, startDate, endDate, percentage, maximumAmount);
    }

    //Done!!
    public void addCustomerToDiscount(String customerId, String code, int count) throws Exceptions.DiscountCodeException, Exceptions.CustomerIdException {
        Discount discount = Discount.getDiscountByCode(code);
        Account account = Account.getAccountById(customerId);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else if (account instanceof Customer)
            discount.addCustomer(customerId, count);
        else
            throw new Exceptions.CustomerIdException(customerId);
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
            Map<Customer, Integer> peopleRemainingCount = discount.getCustomers();
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

    //Done!!
    public String[] getDiscountEditableFields(){
        String[] editableFields = new String[4];
        editableFields[0] = "start date";
        editableFields[1] = "end date";
        editableFields[2] = "maximum amount";
        editableFields[3] = "percentage";
        return editableFields;
    }

    //Done!!
    /**
     *
     * @param code String
     * @param field String ->                     "start date", "end date", "maximum amount", "percentage"
     * @param newInformation String: should match  dateFormat , dateFormat,  Double         ,  Double
     * @throws Exceptions.DiscountCodeException
     * @throws Exceptions.SameAsPreviousValueException
     * @throws Exceptions.InvalidFormatException
     */
    public void editDiscountCode(String code, String field, String newInformation) throws Exceptions.DiscountCodeException, Exceptions.SameAsPreviousValueException, Exceptions.InvalidFormatException {
        Discount discount = Discount.getDiscountByCode(code);
        if( discount == null )
            throw new Exceptions.DiscountCodeException(code);
        else{
            if(field.equalsIgnoreCase("start date")){
                if(discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    try {
                        discount.setStartDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                }
            }else if(field.equalsIgnoreCase("end date")){
                if(discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    try {
                        discount.setEndDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                }
            }else if(field.equalsIgnoreCase("maximum amount")){
                if(discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    discount.setMaximumAmount(Double.parseDouble(newInformation));
                }
            }else if(field.equalsIgnoreCase("percentage")){
                if(discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    discount.setPercentage(Double.parseDouble(newInformation));
                }
            }
        }
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

    /**
     *
     * @param requestId
     * @return AddProduct: { {"AddProduct"}, { productId, productName, ProductBrand, infoText, categoryName, sellerUsername, storeName, rawPrice, remainingCount }, {specialProperties}}
     *         AddReview: { {"AddReview"}, {}}
     * @throws Exceptions.InvalidRequestIdException
     */
    public ArrayList<String[]> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestId);
        else{
            String[] typeDate = new String[2];
            typeDate[0] = request.getClass().getName();
            typeDate[1] = dateFormat.format(request.getDate());
            ArrayList<String[]> detailsOfRequest = new ArrayList<>();
            detailsOfRequest.add(typeDate);
            switch (typeDate[0]) {
                case "AddProduct":
                    detailsOfRequest.add(getSubProductInfo(((AddProductRequest)request).getSubProduct()));
                    String[] specialProperties = new String[((AddProductRequest)request).getProduct().getSpecialProperties().size()];
                    detailsOfRequest.add(((AddProductRequest)request).getProduct().getSpecialProperties().toArray(specialProperties));
                    break;
                case "AddReview":
                    detailsOfRequest.add(getReviewInfo(((AddReviewRequest)request).getReview()));
                    break;
                case "AddSale":
                    detailsOfRequest.add(getSaleInfo(((AddSaleRequest)request).getSale()));
                    break;
                case "AddSeller":
                    detailsOfRequest.add(getPersonalInfo(((AddSellerRequest)request).getSeller()));
                    break;
                case "EditProduct":
                    detailsOfRequest.add(getSubProductInfo(SubProduct.getSubProductById(((EditProductRequest)request).getSubProductId())));
                    String[] productChange = new String[2];
                    productChange[0] = ((EditProductRequest)request).getField().toString();
                    productChange[1] = ((EditProductRequest)request).getNewValue();
                    detailsOfRequest.add(productChange);
                    break;
                case "EditSale":
                    detailsOfRequest.add(getSaleInfo(Sale.getSaleById(((EditSaleRequest)request).getSaleId())));
                    String[] saleChange = new String[2];
                    saleChange[0] = ((EditSaleRequest)request).getField().toString();
                    saleChange[1] = ((EditSaleRequest)request).getNewValue();
                    detailsOfRequest.add(saleChange);
                    break;
            }
            return detailsOfRequest;
        }
    }

    /**
     *
     * @param subProduct
     * @return String[9]: { productId, productName, ProductBrand, infoText, categoryName, sellerUsername, storeName, rawPrice, remainingCount }
     */
    private String[] getSubProductInfo(SubProduct subProduct){
        String[] subProductInfo = new String[9];
        Product product = subProduct.getProduct();
        subProductInfo[0] = product.getId();
        subProductInfo[1] = product.getName();
        subProductInfo[2] = product.getBrand();
        subProductInfo[3] = product.getInfoText();
        subProductInfo[4] = product.getCategory().getName();
        subProductInfo[5] = subProduct.getSeller().getUsername();
        subProductInfo[6] = subProduct.getSeller().getStoreName();
        subProductInfo[7] = Double.toString(subProduct.getRawPrice());
        subProductInfo[8] = Integer.toString(subProduct.getRemainingCount());
        return subProductInfo;
    }

    //Done!!

    /**
     *
     * @param review
     * @return String[6]: { reviewerUsername, productId, productName, productBrand, reviewTitle, reviewText }
     */
    private String[] getReviewInfo(Review review){
        String[] reviewInfo = new String[6];
        reviewInfo[0] = review.getReviewer().getUsername();
        reviewInfo[1] = review.getProduct().getId();
        reviewInfo[2] = review.getProduct().getName();
        reviewInfo[3] = review.getProduct().getBrand();
        reviewInfo[4] = review.getTitle();
        reviewInfo[5] = review.getText();
        return reviewInfo;
    }

    //Done!!
    public void acceptRequest(String requestID, boolean accepted) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestID);
        if(request == null)
            throw new Exceptions.InvalidRequestIdException(requestID);
        else {
            if(accepted)
                request.accept();
            else
                request.decline();
        }
    }

    //Done!!
    public ArrayList<String> manageCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    public String[] getCategoryEditableFields(){
        String[] editableFields = new String[2];
        editableFields[0] = "name";
        editableFields[1] = "parent name";
        return editableFields;
    }

    //Done!!

    /**
     *
     * @param categoryName
     * @param field             "name", "parent name"
     * @param newInformation    newName, nameOfNewParent
     * @throws Exceptions.InvalidCategoryException if this category doesn't exist
     * @throws Exceptions.InvalidFieldException if there is no such field to edit
     * @throws Exceptions.SameAsPreviousValueException if new information is as the same as the previous one
     * @throws Exceptions.ExistedCategoryException if there is already a category with new name
     * @throws Exceptions.SubCategoryException if the chosen new parent is a child of this category
     */
    public void editCategory(String categoryName, String field, String newInformation) throws Exceptions.InvalidCategoryException,
            Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException, Exceptions.ExistedCategoryException, Exceptions.SubCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if ( category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        if( field.equalsIgnoreCase("name")){
            if( category.getName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            else {
                if(Category.getCategoryByName(newInformation) != null)
                    throw new Exceptions.ExistedCategoryException(newInformation);
                else {
                    category.setName(newInformation);
                }
            }
        }else if ( field.equalsIgnoreCase("parent name")){
            Category newParentCategory = Category.getCategoryByName(newInformation);
            if(newParentCategory == null){
                category.setParent(Category.getSuperCategory().getId());
            }else {
                if (category.hasSubCategoryWithId(newParentCategory.getId()))
                    throw new Exceptions.SubCategoryException(categoryName, newInformation);
                else
                    category.setParent(newParentCategory.getId());
            }
        }else
            throw new Exceptions.InvalidFieldException();
    }

    //Done!!
    public void addCategory(String categoryName, String parentCategoryName, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException {
        if (Category.getCategoryByName(categoryName) != null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else{
            Category parentCategory = Category.getCategoryByName(parentCategoryName);
            String parentCategoryId = parentCategory == null ? Category.getSuperCategory().getId() : parentCategory.getId();
            new Category(categoryName, parentCategoryId, specialProperties);
        }
    }

    //Done!!
    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        category.terminate();
    }

}
