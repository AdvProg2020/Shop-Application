package Client.HollowController;

import Client.HollowController.Exceptions.*;
import Server.model.Wallet;
import Server.model.account.Account;
import Server.model.account.Admin;
import Server.model.account.Supporter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Client.view.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class AdminController {
    private static Type stringArrayType = new TypeToken<String[]>() {
    }.getType();
    private static Type stringListType = new TypeToken<ArrayList<String>>() {
    }.getType();
    private static Type stringArrayListType = new TypeToken<ArrayList<String[]>>() {
    }.getType();

    private Sender sender;

    public AdminController() {
        sender = Sender.getInstance();
    }

    private String convertToJson(Object... args) {
        StringBuilder array = new StringBuilder();
        Gson gson = new Gson();
        for (int i = 0; i < args.length; i++) {
            array.append(gson.toJson(args[i]));
            if (i != args.length - 1) array.append("*");
        }
        return array.toString();
    }

    private String[] getExceptionNameAndBody(String response) {
        String name = response.substring(response.indexOf(":") + 1, response.indexOf("\n"));
        String body = response.substring(response.indexOf("\n") + 1);
        return new String[]{name, body};
    }

    public ArrayList<String[]> getAllBuyLogs() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminGetAllBuyLogs, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public void editPersonalInfo(String field, String newInformation) throws InvalidFieldException, SameAsPreviousValueException {
        String body = convertToJson(field, newInformation);
        String response = sender.sendRequest(Constants.Commands.adminEditPersonalInfo, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

    public ArrayList<String[]> manageUsers() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminManageUsers, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public String[] viewUsername(String username) throws UsernameDoesntExistException {
        String body = convertToJson(username);
        String response = sender.sendRequest(Constants.Commands.adminViewUsername, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new UsernameDoesntExistException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public void deleteUsername(String username) throws UsernameDoesntExistException, ManagerDeleteException {
        String body = convertToJson(username);
        String response = sender.sendRequest(Constants.Commands.adminDeleteUsername, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Username")) throw new UsernameDoesntExistException(nameBody[1]);
            else throw new ManagerDeleteException();
        }
    }


    public void creatAdminProfile(String username, String password, String firstName, String lastName, String email, String phone, byte[] imagePath) throws UsernameAlreadyTakenException {
        String body = convertToJson(username, password, firstName, lastName, email, phone, imagePath);
        String response = sender.sendRequest(Constants.Commands.adminCreatAdminProfile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new UsernameAlreadyTakenException(nameBody[1]);
        }
    }


    public ArrayList<String[]> manageAllProducts() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminManageAllProducts, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public void removeProduct(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.adminRemoveProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void createDiscountCode(String discountCode, String startDate, String endDate, double percentage,
                                   double maximumAmount, ArrayList<String[]> customersIdCount) throws ExistingDiscountCodeException, InvalidAccountsForDiscount, InvalidFormatException {

        String body = convertToJson(discountCode, startDate, endDate, percentage, maximumAmount, customersIdCount);
        String response = sender.sendRequest(Constants.Commands.adminCreateDiscountCode, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Existing")) throw new ExistingDiscountCodeException(nameBody[1]);
            else if (nameBody[1].startsWith("Invalid")) throw new InvalidAccountsForDiscount(nameBody[1]);
            else throw new InvalidFormatException(nameBody[1]);
        }
    }


    public ArrayList<String> viewActiveDiscountCodes() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminViewActiveDiscountCodes, body);
        return new Gson().fromJson(response, stringListType);
    }

    public ArrayList<String> viewArchiveDiscountCodes() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminViewArchiveDiscountCodes, body);
        return new Gson().fromJson(response, stringListType);
    }


    public String[] viewDiscountCodeByCode(String code) throws DiscountCodeException {
        String body = convertToJson(code);
        String response = sender.sendRequest(Constants.Commands.adminViewDiscountCodeByCode, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new DiscountCodeException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public String[] viewDiscountCodeById(String discountId) throws DiscountCodeException {
        String body = convertToJson(discountId);
        String response = sender.sendRequest(Constants.Commands.adminViewDiscountCodeById, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new DiscountCodeException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public ArrayList<String[]> peopleWhoHaveThisDiscount(String id) throws DiscountCodeException {
        String body = convertToJson(id);
        String response = sender.sendRequest(Constants.Commands.adminPeopleWhoHaveThisDiscount, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new DiscountCodeException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void editDiscountCode(String code, String field, String newInformation) throws DiscountCodeException, SameAsPreviousValueException, InvalidFormatException {
        String body = convertToJson(code, field, newInformation);
        String response = sender.sendRequest(Constants.Commands.adminEditDiscountCode, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Discount")) throw new DiscountCodeException(nameBody[1]);
            else if (nameBody[0].startsWith("SameAsPrevious")) throw new SameAsPreviousValueException(nameBody[1]);
            else throw new InvalidFormatException(nameBody[1]);
        }
    }


    public void removeDiscountCode(String code) throws DiscountCodeException {
        String body = convertToJson(code);
        String response = sender.sendRequest(Constants.Commands.adminRemoveDiscountCode, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new DiscountCodeException(nameBody[1]);
        }
    }

    public ArrayList<String[]> getArchivedRequests() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminGetArchivedRequests, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> getPendingRequests() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminGetPendingRequests, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> detailsOfRequest(String requestId) throws InvalidRequestIdException {
        String body = convertToJson(requestId);
        String response = sender.sendRequest(Constants.Commands.adminDetailsOfRequest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidRequestIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void acceptRequest(String requestID, boolean accepted) throws InvalidRequestIdException {
        String body = convertToJson(requestID, accepted);
        String response = sender.sendRequest(Constants.Commands.adminAcceptRequest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidRequestIdException(nameBody[1]);
        }
    }


    public ArrayList<String[]> manageCategories() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.adminManageCategories, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public void editCategory(String categoryName, String field, String newInformation) throws InvalidCategoryException,
            InvalidFieldException, SameAsPreviousValueException, ExistingCategoryException, SubCategoryException {
        String body = convertToJson(categoryName, field, newInformation);
        String response = sender.sendRequest(Constants.Commands.adminEditCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidCategory")) throw new InvalidCategoryException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidField")) throw new InvalidFieldException();
            else if (nameBody[0].startsWith("SameAsPrevious")) throw new SameAsPreviousValueException(nameBody[1]);
            else if (nameBody[0].startsWith("Existing")) throw new ExistingCategoryException(nameBody[1]);
            else throw new SubCategoryException(nameBody[1]);
        }
    }

    public void addCategory(String categoryName, String parentCategoryName, ArrayList<String> specialProperties) throws InvalidCategoryException, ExistingCategoryException {
        String body = convertToJson(categoryName, parentCategoryName, specialProperties);
        String response = sender.sendRequest(Constants.Commands.adminAddCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidCategoryException(nameBody[1]);
            else throw new ExistingCategoryException(nameBody[1]);
        }
    }

    public String[] getCategory(String categoryName) throws InvalidCategoryException {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.adminGetCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public void removeCategory(String categoryName) throws InvalidCategoryException {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.adminRemoveCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        }
    }

    public void setAccounts(String code, ArrayList<String[]> customerIds) {
        String body = convertToJson(code, customerIds);
        sender.sendRequest(Constants.Commands.adminSetAccounts, body);
    }

    public void removeAccountsFromDiscount(String code, ArrayList<String> customerIds) {
        String body = convertToJson(code, customerIds);
        sender.sendRequest(Constants.Commands.adminRemoveAccountsFromDiscount, body);
    }

    public HashMap<String, String> getPropertyValuesOfAProductInARequest(String requestId) throws InvalidRequestIdException {
        String body = convertToJson(requestId);
        String response = sender.sendRequest(Constants.Commands.adminGetPropertyValuesOfAProductInARequest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidRequestIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
    }

    public ArrayList<String[]> getProductsInSaleRequest(String requestId) throws InvalidRequestIdException {
        String body = convertToJson(requestId);
        String response = sender.sendRequest(Constants.Commands.adminGetProductsInSaleRequest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidRequestIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void addPropertyToACategory(String categoryName, String property) throws InvalidCategoryException, ExistingPropertyException {
        String body = convertToJson(categoryName, property);
        String response = sender.sendRequest(Constants.Commands.adminAddPropertyToACategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidCategoryException(nameBody[1]);
            else throw new ExistingPropertyException(nameBody[1]);
        }
    }

    public void removePropertyFromACategory(String categoryName, String property) throws InvalidCategoryException {
        String body = convertToJson(categoryName, property);
        String response = sender.sendRequest(Constants.Commands.adminRemovePropertyFromACategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        }
    }

    public void editBrandOfProduct(String productId, String newBrand) throws InvalidSellableIdException {
        String body = convertToJson(productId, newBrand);
        String response = sender.sendRequest(Constants.Commands.adminEditBrandOfProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void editImageOfProduct(String productId, String newImage) throws InvalidSellableIdException {
        String body = convertToJson(productId, newImage);
        String response = sender.sendRequest(Constants.Commands.adminEditImageOfProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void editPropertyOfProduct(String productId, String newProperty) throws InvalidSellableIdException {
        String body = convertToJson(productId, newProperty);
        String response = sender.sendRequest(Constants.Commands.adminEditPropertyOfProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void editInfoTextOfProduct(String productId, String newInfoText) throws InvalidSellableIdException {
        String body = convertToJson(productId, newInfoText);
        String response = sender.sendRequest(Constants.Commands.adminEditInfoTextOfProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void editNameOfProduct(String productId, String newName) throws InvalidSellableIdException {
        String body = convertToJson(productId, newName);
        String response = sender.sendRequest(Constants.Commands.adminEditNameOfProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public HashMap<String, String> getPropertyValuesOfAFileInRequest(String id) throws InvalidRequestIdException {
        String body = convertToJson(id);
        String response = sender.sendRequest(Constants.Commands.adminGetPropertyValuesOfAFileInRequest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidRequestIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
    }

    public String[] getBuyLogWithId(String logId) throws InvalidLogIdException {
        String body = convertToJson(logId);
        String response = sender.sendRequest(Constants.Commands.adminGetBuyLogById, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidLogIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public ArrayList<String[]> getBuyLogItemsWithId(String logId) throws InvalidLogIdException {
        String body = convertToJson(logId);
        String response = sender.sendRequest(Constants.Commands.adminGetBuyLogItemsWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidLogIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void editBuyLogStatus(String logId, String newStatus) throws InvalidLogIdException {
        String body = convertToJson(logId, newStatus);
        String response = sender.sendRequest(Constants.Commands.adminEditBuyLogStatus, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidLogIdException(nameBody[1]);
        }
    }

    public void createSupporterProfile(String username, String password, String firstName, String lastName, String email, String phone, byte[] imagePath) throws UsernameAlreadyTakenException {
        String body = convertToJson(username, password, firstName, lastName, email, phone, imagePath);
        String response = sender.sendRequest(Constants.Commands.adminCreateSupporterProfile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new UsernameAlreadyTakenException(nameBody[1]);
        }
    }

    public void setCommission(double percentage) throws InvalidCommissionException {
        String body = convertToJson(percentage);
        String response = sender.sendRequest(Constants.Commands.adminSetCommission, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCommissionException(nameBody[1]);
        }
    }

    public void setWalletMin(double amount) {
        String body = convertToJson(amount);
        String response = sender.sendRequest(Constants.Commands.adminSetWalletMin, body);
    }

}
