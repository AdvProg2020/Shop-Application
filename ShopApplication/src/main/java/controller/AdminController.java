package controller;

import model.Category;
import model.Discount;
import model.Product;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.database.Database;
import model.request.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminController {

    private static final DateFormat dateFormat = Utilities.getDateFormat();
    private Controller mainController;

    public AdminController(Controller controller) {
        mainController = controller;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Database database() {
        return mainController.getDatabase();
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
        database().editAccount();
    }


    public ArrayList<String[]> manageUsers() {
        ArrayList<String[]> accounts = new ArrayList<>();
        for (Account account : Account.getAllAccounts()) {
            if (account != currentAccount()) {
                String[] IdUsername = new String[7];
                IdUsername[0] = account.getId();
                IdUsername[1] = account.getUsername();
                IdUsername[2] = account.getFirstName();
                IdUsername[3] = account.getLastName();
                IdUsername[4] = account.getPhone();
                IdUsername[5] = account.getEmail();
                IdUsername[6] = account.getClass().getSimpleName();
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
        if (account != Admin.getManager()) {
            if (account != currentAccount())
                account.suspend();
        }
        else
            throw new Exceptions.ManagerDeleteException();
        switch (account.getClass().getSimpleName()) {
            case "Admin":
                database().removeAdmin();
                break;
            case "Customer":
                database().removeCustomer();
                break;
            case "Seller":
                database().removeSeller();
                break;
        }
    }


    public void creatAdminProfile(String username, String password, String firstName, String lastName, String email, String phone, String imagePath) throws Exceptions.UsernameAlreadyTakenException {
        if (Account.isUsernameUsed(username))
            throw new Exceptions.UsernameAlreadyTakenException(username);
        new Admin(username, password, firstName, lastName, email, phone, imagePath);
        database().createAdmin();
    }


    public ArrayList<String[]> manageAllProducts() {
        ArrayList<String[]> productPacks = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>(Product.getAllProducts());
        products.sort(new Utilities.Sort.ProductViewCountComparator(true));
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
            database().removeProduct();
        }
    }

    public void createDiscountCode(String discountCode, String startDate, String endDate, double percentage,
                                   double maximumAmount, ArrayList<String[]> customersIdCount) throws Exceptions.ExistingDiscountCodeException, Exceptions.InvalidAccountsForDiscount, Exceptions.InvalidFormatException {

        if (Discount.getDiscountByCode(discountCode) != null)
            throw new Exceptions.ExistingDiscountCodeException(discountCode);
        else {
            ArrayList<String> wrongIds = new ArrayList<>();
            for (String[] Id : new ArrayList<>(customersIdCount)) {
                Account account = Account.getAccountById(Id[0]);
                if (!(account instanceof Customer)) {
                    wrongIds.add(Id[0]);
                    customersIdCount.remove(Id);
                }
            }
            Discount discount;
            try {
                discount = new Discount(discountCode, dateFormat.parse(startDate), dateFormat.parse(endDate), percentage, maximumAmount);
                for (String[] IdCount : customersIdCount) {
                    discount.addCustomer(IdCount[0], Integer.parseInt(IdCount[1]));
                }
                database().createDiscount();
                if (wrongIds.size() > 0)
                    throw new Exceptions.InvalidAccountsForDiscount(Utilities.Pack.invalidAccountIds(wrongIds));
            } catch (ParseException ignored) {
                throw new Exceptions.InvalidFormatException("date");
            }
        }
    }


    public ArrayList<String> viewDiscountCodes() {
        ArrayList<String> discountCodes = new ArrayList<>();
        for (Discount discount : Discount.getActiveDiscounts()) {
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
            switch (field) {
                case "start date":
                    try {
                        if (dateFormat.parse(newInformation).equals(discount.getStartDate()))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        discount.setStartDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "end date":
                    try {
                        if (dateFormat.parse(newInformation).equals(discount.getEndDate()))
                            throw new Exceptions.SameAsPreviousValueException(field);
                        discount.setEndDate(dateFormat.parse(newInformation));
                    } catch (ParseException e) {
                        throw new Exceptions.InvalidFormatException("date");
                    }
                    break;
                case "maximum amount":
                    if (Double.parseDouble(newInformation) == discount.getMaximumAmount())
                        throw new Exceptions.SameAsPreviousValueException(field);
                    else {
                        discount.setMaximumAmount(Double.parseDouble(newInformation));
                    }
                    break;
                case "percentage":
                    if (Double.parseDouble(newInformation) == discount.getPercentage())
                        throw new Exceptions.SameAsPreviousValueException(field);
                    else {
                        discount.setPercentage(Double.parseDouble(newInformation));
                    }
                    break;
            }
            database().editDiscount();
        }
    }


    public void removeDiscountCode(String code) throws Exceptions.DiscountCodeException {
        Discount discount = Discount.getDiscountByCode(code);
        if (discount == null)
            throw new Exceptions.DiscountCodeException(code);
        else {
            discount.suspend();
            database().removeDiscount();
        }
    }

    public ArrayList<String[]> getArchivedRequests() {
        ArrayList<String[]> requestIds = new ArrayList<>();
        for (Request request : Request.getRequestArchive()) {
            requestIds.add(Utilities.Pack.request(request));
        }
        return requestIds;
    }

    public ArrayList<String[]> getPendingRequests() {
        ArrayList<String[]> requestPacks = new ArrayList<>();
        for (Request request : Request.getPendingRequests()) {
            requestPacks.add(Utilities.Pack.request(request));
        }
        return requestPacks;
    }

    public ArrayList<String[]> detailsOfRequest(String requestId) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestId);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestId);
        else {
            ArrayList<String[]> detailsOfRequest = new ArrayList<>();
            detailsOfRequest.add(Utilities.Pack.request(request));
            switch (Utilities.Pack.request(request)[1]) {
                case "AddProductRequest":
                    detailsOfRequest.add(Utilities.Pack.addProductRequest(((AddProductRequest) request).getSubProduct(), ((AddProductRequest)request).getProduct()));
                    break;
                case "AddReviewRequest":
                    detailsOfRequest.add(Utilities.Pack.getReviewInfo(((AddReviewRequest) request).getReview()));
                    break;
                case "AddSaleRequest":
                    detailsOfRequest.add(Utilities.Pack.newSaleInRequest(((AddSaleRequest) request).getSale()));
                    break;
                case "AddSellerRequest":
                    detailsOfRequest.add(Utilities.Pack.sellerInRequest(((AddSellerRequest) request).getSeller()));
                    break;
                case "EditProductRequest":
                    detailsOfRequest.add(Utilities.Pack.subProduct(((EditProductRequest) request).getSubProduct()));
                    detailsOfRequest.add(Utilities.Pack.productChange(((EditProductRequest) request)));
                    break;
                case "EditSaleRequest":
                    detailsOfRequest.add(Utilities.Pack.saleInfo(((EditSaleRequest) request).getSale()));
                    detailsOfRequest.add(Utilities.Pack.saleChange(((EditSaleRequest) request)));
                    break;
            }
            return detailsOfRequest;
        }
    }

    public void acceptRequest(String requestID, boolean accepted) throws Exceptions.InvalidRequestIdException {
        Request request = Request.getRequestById(requestID);
        if (request == null)
            throw new Exceptions.InvalidRequestIdException(requestID);
        else {
            if (accepted) {
                request.accept();
                request.updateDatabase(database());
            } else
                request.decline();
        }
    }


    public ArrayList<String[]> manageCategories() {
        ArrayList<String[]> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            String[] info = new String[3];
            info[0] = category.getId();
            info[1] = category.getName();
            info[2] = category.getParent().getName();
            categoryNames.add(info);
        }
        return categoryNames;
    }

    public String[] getCategoryEditableFields() {
        return Utilities.Field.getCategoryEditableFields();
    }

    /**
     * @param categoryName
     * @param field          "name", "parent"
     * @param newInformation newName, nameOfNewParent
     * @throws Exceptions.InvalidCategoryException     if this category doesn't exist
     * @throws Exceptions.InvalidFieldException        if there is no such field to edit
     * @throws Exceptions.SameAsPreviousValueException if new information is as the same as the previous one
     * @throws Exceptions.ExistingCategoryException     if there is already a category with new name
     * @throws Exceptions.SubCategoryException         if the chosen new parent is a child of this category
     */
    public void editCategory(String categoryName, String field, String newInformation) throws Exceptions.InvalidCategoryException,
            Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException, Exceptions.ExistingCategoryException, Exceptions.SubCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        switch (field) {
            case "name":
                if (category.getName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                else {
                    if (Category.getCategoryByName(newInformation) != null)
                        throw new Exceptions.ExistingCategoryException(newInformation);
                    else {
                        category.setName(newInformation);
                    }
                }
                database().editCategory();
                break;
            case "parent":
                Category newParentCategory = Category.getCategoryByName(newInformation);
                if (newParentCategory == null) {
                    category.setParent(Category.getSuperCategory().getId());
                } else {
                    if (category.hasSubCategoryWithId(newParentCategory.getId()))
                        throw new Exceptions.SubCategoryException(categoryName, newInformation);
                    else
                        category.setParent(newParentCategory.getId());
                }
                database().editCategory();
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    public void addCategory(String categoryName, String parentCategoryName, ArrayList<String> specialProperties) throws Exceptions.InvalidCategoryException, Exceptions.ExistingCategoryException {
        if (Category.getCategoryByName(categoryName) != null || categoryName.equals("superCategory"))
            throw new Exceptions.ExistingCategoryException(categoryName);
        else {
            Category parentCategory = Category.getCategoryByName(parentCategoryName);
            if( parentCategory == null)
                throw new Exceptions.InvalidCategoryException(parentCategoryName);
            new Category(categoryName, parentCategory.getId(), specialProperties);
            database().createCategory();
        }
    }

    public void removeCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        category.suspend();
        database().removeCategory();
    }

    public void setAccounts(String code, HashMap<String, Integer> customerIds){
        Discount discount = Discount.getDiscountByCode(code);
        if( discount != null){
            for (String customerId : customerIds.keySet()) {
                discount.addCustomer( customerId, customerIds.get(customerId));
            }
        }
    }

    public void removeAccounts(String code, ArrayList<String> customerIds){
        Discount discount = Discount.getDiscountByCode(code);
        if( discount != null){
            for (String customerId : customerIds) {
                discount.removeCustomer(customerId);
            }
        }
    }
}
