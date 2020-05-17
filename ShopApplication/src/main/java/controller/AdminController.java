package controller;

import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.database.Database;
import model.request.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class AdminController {

    private Controller mainController;
    private static  final DateFormat dateFormat = Utilities.getDateFormat();
    private Database databaseManager;

    public AdminController(Controller controller) {
        databaseManager = controller.getDatabaseManager();
        mainController = controller;
    }

    private Account currentAccount(){
        return mainController.getCurrentAccount();
    }
    
    

    /**
     * @return admin:
     * { String firstName, String lastName, String phone, String email, String password}
     */
    public String[] getPersonalInfoEditableFields() {
        return Utilities.Field.adminPersonalInfoEditableFields();
    }

    
    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        mainController.editPersonalInfo(field, newInformation);
        databaseManager.editAccount();
    }

    
    public ArrayList<String[]> manageUsers() {
        ArrayList<String[]> accounts = new ArrayList<>();
        for (Account account : Account.getAllAccounts()) {
            if( account != currentAccount()) {
                String[] IdUsername = new String[2];
                IdUsername[0] = account.getId();
                IdUsername[1] = account.getUsername();
                accounts.add(IdUsername);
            }
        }
        return accounts;
    }

    
    public String[] viewUsername(String username) throws Exceptions.UsernameDoesntExistException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.UsernameDoesntExistException(username);
        else {
            return Utilities.Pack.personalInfo(account);
        }
    }

    
    public void deleteUsername(String username) throws Exceptions.UsernameDoesntExistException, Exceptions.ManagerDeleteException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.UsernameDoesntExistException(username);
        if( account != Admin.getManager())
            if(account != currentAccount())
                account.suspend();
        else
            throw new Exceptions.ManagerDeleteException();
        switch (account.getClass().getSimpleName()) {
            case "Admin":
                databaseManager.removeAdmin();
                break;
            case "Customer":
                databaseManager.removeCustomer();
                break;
            case "Seller":
                databaseManager.removeSeller();
                break;
        }
    }

    
    public void creatAdminProfile(String username, String password, String firstName, String lastName, String email, String phone) throws Exceptions.UsernameAlreadyTakenException {
        if (Account.getAccountByUsername(username) != null)
            throw new Exceptions.UsernameAlreadyTakenException(username);
        new Admin(username, password, firstName, lastName, email, phone);
        databaseManager.createAdmin();
    }

    
    public ArrayList<String[]> manageAllProducts() {
        ArrayList<String[]> productPacks = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>(Product.getAllProducts());
        products.sort( new Utilities.Sort.ProductViewCountComparator(true));
        for (Product product : products) {
            productPacks.add(Utilities.Pack.product(product));
        }
        return productPacks;
    }

    
    public void removeProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            product.suspend();
            databaseManager.removeProduct();
        }
    }

    
    public void createDiscountCode(String discountCode, Date startDate, Date endDate, double percentage,
                                   int maximumAmount, ArrayList<String[]> customersIdCount) throws Exceptions.ExistingDiscountCodeException, Exceptions.InvalidAccountsForDiscount {

        if (Discount.getDiscountByCode(discountCode) != null)
            throw new Exceptions.ExistingDiscountCodeException(discountCode);
        else {
            ArrayList<String> wrongIds = new ArrayList<>();
            for (String[] Id : new ArrayList<>(customersIdCount)) {
                Account account = Account.getAccountById(Id[0]);
                if(!(account instanceof Customer)){
                    wrongIds.add(Id[0]);
                    customersIdCount.remove(Id);
                }
            }
            Discount discount = new Discount(discountCode, startDate, endDate, percentage, maximumAmount);
            for (String[] IdCount : customersIdCount) {
                discount.addCustomer(IdCount[0], Integer.parseInt(IdCount[1]));
            }
            databaseManager.createDiscount();
            if(wrongIds.size() > 0)
                throw new Exceptions.InvalidAccountsForDiscount(Utilities.Pack.invalidAccountIds(wrongIds));
        }
    }

    
    public ArrayList<String> viewDiscountCodes() {
        ArrayList<String> discountCodes = new ArrayList<>();
        for (Discount discount : Discount.getAllDiscounts()) {
            discountCodes.add(discount.getDiscountCode());
        }
        return discountCodes;
    }

    
    public String[] viewDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else
            return Utilities.Pack.discountInfo(discount);
    }

    
    public ArrayList<String[]> peopleWhoHaveThisDiscount(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else {
            Map<Customer, Integer> peopleRemainingCount = discount.getCustomers();
            ArrayList<String[]> peopleWithThisCode = new ArrayList<>();
            for (Customer customer : peopleRemainingCount.keySet()) {
                peopleWithThisCode.add(Utilities.Pack.customerDiscountRemainingCount(customer, peopleRemainingCount.get(customer)));
            }
            return peopleWithThisCode;
        }
    }

    
    public String[] getDiscountEditableFields() {
        return Utilities.Field.discountEditableFields();
    }

    

    /**
     * @param code           String
     * @param field          String ->                     "start date", "end date", "maximum amount", "percentage"
     * @param newInformation String: should match  dateFormat , dateFormat,  Double         ,  Double
     * @throws Exceptions.DiscountCodeException
     * @throws Exceptions.SameAsPreviousValueException
     * @throws Exceptions.InvalidFormatException
     */
    public void editDiscountCode(String code, String field, String newInformation) throws Exceptions.DiscountCodeException, Exceptions.SameAsPreviousValueException, Exceptions.InvalidFormatException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else {
            if (field.equalsIgnoreCase("start date")) {
                if (discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    try {
                        discount.setStartDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                }
            } else if (field.equalsIgnoreCase("end date")) {
                if (discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    try {
                        discount.setEndDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                }
            } else if (field.equalsIgnoreCase("maximum amount")) {
                if (discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    discount.setMaximumAmount(Double.parseDouble(newInformation));
                }
            } else if (field.equalsIgnoreCase("percentage")) {
                if (discount.getDiscountCode().equalsIgnoreCase(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    discount.setPercentage(Double.parseDouble(newInformation));
                }
            }
            databaseManager.editDiscount();
        }
    }

    
    public void removeDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else{
            discount.suspend();
            databaseManager.removeDiscount();
        }
    }

    //TODO: Dana: Id, type, date, status,
    public ArrayList<String[]> manageRequests() {
        ArrayList<String[]> requestIds = new ArrayList<>();
        for (Request request : Request.getPendingRequests()) {
            requestIds.add(Utilities.Pack.request(request));
        }
        if(currentAccount() == Admin.getManager()){
            for (Request request : Request.getRequestArchive()) {
                requestIds.add(Utilities.Pack.request(request));
            }
        }
        return requestIds;
    }

    //Todo: Dana consider the output
    /**
     * @param requestId
     * @return AddProduct: { {"AddProduct"}, { productId, productName, ProductBrand, infoText, categoryName, sellerUsername, storeName, rawPrice, remainingCount }, {specialProperties}
     * @throws Exceptions.InvalidRequestIdException
     */
    public ArrayList<String[]> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestId);
        else {
            ArrayList<String[]> detailsOfRequest = new ArrayList<>();
            detailsOfRequest.add(Utilities.Pack.request(request));
            switch (Utilities.Pack.request(request)[0]) {
                case "AddProduct":
                    detailsOfRequest.add(Utilities.Pack.subProductExtended(((AddProductRequest) request).getSubProduct()));
                    String[] specialProperties = new String[((AddProductRequest) request).getProduct().getSpecialProperties().size()];
                    detailsOfRequest.add(((AddProductRequest) request).getProduct().getSpecialProperties().toArray(specialProperties));
                    break;
                case "AddReview":
                    detailsOfRequest.add(getReviewInfo(((AddReviewRequest) request).getReview()));
                    break;
                case "AddSale":
                    detailsOfRequest.add(Utilities.Pack.saleInfo(((AddSaleRequest) request).getSale()));
                    break;
                case "AddSeller":
                    detailsOfRequest.add(Utilities.Pack.personalInfo(((AddSellerRequest) request).getSeller()));
                    break;
                case "EditProduct":
                    detailsOfRequest.add(Utilities.Pack.subProductExtended(((EditProductRequest) request).getSubProduct()));
                    detailsOfRequest.add(Utilities.Pack.productChange(((EditProductRequest)request)));
                    break;
                case "EditSale":
                    detailsOfRequest.add(Utilities.Pack.saleInfo(((EditSaleRequest) request).getSale()));
                    detailsOfRequest.add(Utilities.Pack.saleChange(((EditSaleRequest)request)));
                    break;
            }
            return detailsOfRequest;
        }
    }

    /**
     * @param review
     * @return String[6]: { reviewerUsername, productId, productName, productBrand, reviewTitle, reviewText }
     */
    private String[] getReviewInfo(Review review) {
        String[] reviewInfo = new String[6];
        reviewInfo[0] = review.getReviewer().getUsername();
        reviewInfo[1] = review.getProduct().getId();
        reviewInfo[2] = review.getProduct().getName();
        reviewInfo[3] = review.getProduct().getBrand();
        reviewInfo[4] = review.getTitle();
        reviewInfo[5] = review.getText();
        return reviewInfo;
    }

    public void acceptRequest(String requestID, boolean accepted) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestID);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestID);
        else {
            if (accepted) {
                request.accept();
                request.updateDatabase(databaseManager);
            }else
                request.decline();
        }
    }

    
    public ArrayList<String> manageCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    public String[] getCategoryEditableFields() {
        return Utilities.Field.getCategoryEditableFields();
    }

    /**
     * @param categoryName
     * @param field          "name", "parent name"
     * @param newInformation newName, nameOfNewParent
     * @throws Exceptions.InvalidCategoryException     if this category doesn't exist
     * @throws Exceptions.InvalidFieldException        if there is no such field to edit
     * @throws Exceptions.SameAsPreviousValueException if new information is as the same as the previous one
     * @throws Exceptions.ExistedCategoryException     if there is already a category with new name
     * @throws Exceptions.SubCategoryException         if the chosen new parent is a child of this category
     */
    public void editCategory(String categoryName, String field, String newInformation) throws Exceptions.InvalidCategoryException,
            Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException, Exceptions.ExistedCategoryException, Exceptions.SubCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        switch (field) {
            case "name":
                if (category.getName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    if (Category.getCategoryByName(newInformation) != null)
                        throw new Exceptions.ExistedCategoryException(newInformation);
                    else {
                        category.setName(newInformation);
                    }
                }
                databaseManager.editCategory();
                break;
            case "parent name":
                Category newParentCategory = Category.getCategoryByName(newInformation);
                if (newParentCategory == null) {
                    category.setParent(Category.getSuperCategory().getId());
                } else {
                    if (category.hasSubCategoryWithId(newParentCategory.getId()))
                        throw new Exceptions.SubCategoryException(categoryName, newInformation);
                    else
                        category.setParent(newParentCategory.getId());
                }
                databaseManager.editCategory();
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    public void addCategory(String categoryName, String parentCategoryName, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException {
        if (Category.getCategoryByName(categoryName) != null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else {
            Category parentCategory = Category.getCategoryByName(parentCategoryName);
            String parentCategoryId = parentCategory == null ? Category.getSuperCategory().getId() : parentCategory.getId();
            new Category(categoryName, parentCategoryId, specialProperties);
            databaseManager.createCategory();
        }
    }

    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        category.terminate();
        databaseManager.removeCategory();
    }
}
