package Client.HollowController;

import Client.HollowController.Exceptions.*;
import Client.view.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class Controller {
    private static final Type stringType = new TypeToken<String>() {
    }.getType();
    private static final Type stringArrayType = new TypeToken<String[]>() {
    }.getType();
    private static final Type booleanType = new TypeToken<Boolean>() {
    }.getType();
    private static final Type stringListType = new TypeToken<ArrayList<String>>() {
    }.getType();
    private static final Type stringArrayListType = new TypeToken<ArrayList<String[]>>() {
    }.getType();
    private static final Type byteArrayType = new TypeToken<byte[]>() {
    }.getType();

    private final Sender sender;

    public Controller() {
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

    public void creatAccount(String type, String username, String password, String firstName, String lastName,
                             String email, String phone, double balance, String storeName, byte[] imagePath) throws UsernameAlreadyTakenException, AdminRegisterException {
        String body = convertToJson(type, username, password, firstName, lastName, email, phone, balance, storeName, imagePath);
        String response = sender.sendRequest(Constants.Commands.createAccount, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Username")) {
                throw new UsernameAlreadyTakenException(nameBody[1]);
            } else {
                throw new AdminRegisterException();
            }
        }
    }

    public boolean isManager() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.isManager, body);
        return new Gson().fromJson(response, booleanType);

    }

    public boolean doesManagerExist() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.doesManagerExist, body);
        return new Gson().fromJson(response, booleanType);
    }

    public void login(String username, String password) throws WrongPasswordException, UsernameDoesntExistException {
        String body = convertToJson(username, password);
        String response = sender.sendRequest(Constants.Commands.login, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Wrong")) {
                throw new WrongPasswordException();
            } else {
                throw new UsernameDoesntExistException(nameBody[1]);
            }
        }
    }

    public void logout() throws NotLoggedInException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.logout, body);
        if (response.startsWith("exception:")) {
            throw new NotLoggedInException();
        }
    }

    /**
     * @return returns the currentAccount type: anonymous, customer, seller, admin.
     */
    public String getType() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.getType, body);
        return new Gson().fromJson(response, stringType);
    }


    public ArrayList<String[]> getSubCategoriesOfThisCategory(String categoryName) throws InvalidCategoryException {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.getSubCategoriesOfThisCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String> getSubCategoriesOfACategory(String categoryName) throws InvalidCategoryException {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.getSubCategoriesOfACategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringListType);
        }
    }

    public ArrayList<String[]> getProductsOfThisCategory(String categoryName) throws InvalidCategoryException {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.getProductsOfThisCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> sortFilterProducts(String categoryName, boolean inSale, boolean inAuction, String sortBy, boolean isIncreasing, boolean available, double minPrice, double maxPrice, String contains, String brand,
                                                  String extension, String storeName, double minRatingScore, HashMap<String, String> propertyFilters) {
        String body = convertToJson(categoryName, inSale, inAuction, sortBy, isIncreasing, available, minPrice, maxPrice, contains, brand, extension, storeName, minRatingScore, propertyFilters);
        String response = sender.sendRequest(Constants.Commands.sortFilterProducts, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public void showProduct(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.showProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }


    public String[] digest(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.digest, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public HashMap<String, String> getPropertyValuesOfAProduct(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.getPropertyValuesOfAProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
    }

    public ArrayList<String> getPropertiesOfCategory(String categoryName, boolean deep) throws InvalidCategoryException {
        String body = convertToJson(categoryName, deep);
        String response = sender.sendRequest(Constants.Commands.getPropertiesOfCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringListType);
        }
    }

    public ArrayList<String[]> subSellablesOfASellable(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.subProductsOfAProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public String[] getSubProductByID(String subProductId) throws InvalidSubProductIdException {
        String body = convertToJson(subProductId);
        String response = sender.sendRequest(Constants.Commands.getSubProductByID, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSubProductIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public ArrayList<String[]> reviewsOfProductWithId(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.reviewsOfProductWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void addToCart(String subProductId, int count) throws UnavailableProductException, InvalidSubProductIdException, UnAuthorizedAccountException {
        String body = convertToJson(subProductId, count);
        String response = sender.sendRequest(Constants.Commands.addToCart, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Unavailable")) throw new UnavailableProductException(nameBody[1]);
            else if (nameBody[0].startsWith("Invalid")) throw new InvalidSubProductIdException(nameBody[1]);
            else throw new UnAuthorizedAccountException();
        }
    }

    public ArrayList<String[]> getProductsInCart() throws UnAuthorizedAccountException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.getProductsInCart, body);
        if (response.startsWith("exception:")) {
            throw new UnAuthorizedAccountException();
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void increaseProductInCart(String subProductId, int number) throws NotSubProductIdInTheCartException,
            UnavailableProductException, InvalidSubProductIdException, UnAuthorizedAccountException {
        String body = convertToJson(subProductId, number);
        String response = sender.sendRequest(Constants.Commands.increaseProductInCart, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("NotSub")) throw new NotSubProductIdInTheCartException(nameBody[1]);
            else if (nameBody[0].startsWith("Unavailable")) throw new UnavailableProductException(nameBody[1]);
            else if (nameBody[0].startsWith("Invalid")) throw new InvalidSubProductIdException(nameBody[1]);
            else throw new UnAuthorizedAccountException();
        }
    }

    public void decreaseProductInCart(String subProductId, int number) throws InvalidSubProductIdException,
            NotSubProductIdInTheCartException, UnAuthorizedAccountException {
        String body = convertToJson(subProductId, number);
        String response = sender.sendRequest(Constants.Commands.decreaseProductInCart, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidSubProductIdException(nameBody[1]);
            else if (nameBody[0].startsWith("NotSubProduct")) throw new NotSubProductIdInTheCartException(nameBody[1]);
            else throw new UnAuthorizedAccountException();
        }
    }

    public void addReview(String productId, String title, String text) throws InvalidSellableIdException, NotLoggedInException {
        String body = convertToJson(productId, title, text);
        String response = sender.sendRequest(Constants.Commands.addReview, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidSellableIdException(nameBody[1]);
            else throw new NotLoggedInException();
        }
    }

    /**
     * @return if
     * admin:     6: username, type, firstName, lastName, email, phone;
     * customer:  7: username, type, firstName, lastName, email, phone, balance;
     * seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() throws NotLoggedInException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.viewPersonalInfoDef, body);
        if (response.startsWith("exception:")) {
            throw new NotLoggedInException();
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }


    public String[] viewPersonalInfo(String username) throws UsernameDoesntExistException {
        String body = convertToJson(username);
        String response = sender.sendRequest(Constants.Commands.viewPersonalInfo, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new UsernameDoesntExistException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public void editPersonalInfo(String field, String newInformation, byte[]... img) throws InvalidFieldException, SameAsPreviousValueException {
        String body = convertToJson(field, newInformation, img);
        String response = sender.sendRequest(Constants.Commands.editPersonalInfo, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

    public void clearCart() {
        String body = convertToJson();
        sender.sendRequest(Constants.Commands.clearCart, body);
    }

    public void removeSubProductFromCart(String subProductId) throws InvalidSubProductIdException {
        String body = convertToJson(subProductId);
        String response = sender.sendRequest(Constants.Commands.removeSubProductFromCart, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSubProductIdException(nameBody[1]);
        }
    }

    public String[] getDefaultSubSellableOfASellable(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.getDefaultSubProductOfAProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public ArrayList<String> getPropertyValuesInCategory(String categoryName, String property) throws InvalidCategoryException {
        String body = convertToJson(categoryName, property);
        String response = sender.sendRequest(Constants.Commands.getPropertyValuesInCategory, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidCategoryException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringListType);
        }
    }

    public ArrayList<String[]> getSubSellablesForAdvertisements(int number) {
        String body = convertToJson(number);
        String response = sender.sendRequest(Constants.Commands.getSubProductsForAdvertisements, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> getSubSellablesInSale(int number) {
        String body = convertToJson(number);
        String response = sender.sendRequest(Constants.Commands.getSubProductsInSale, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> getSubSellablesInAuction(int number) {
        String body = convertToJson(number);
        String response = sender.sendRequest(Constants.Commands.getSubSellablesInAuction, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String> getCategoryTreeOfAProduct(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.getCategoryTreeOfAProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringListType);
        }
    }

    public ArrayList<String> getCategoryTreeOfACategory(String categoryName) {
        String body = convertToJson(categoryName);
        String response = sender.sendRequest(Constants.Commands.getCategoryTreeOfACategory, body);
        return new Gson().fromJson(response, stringListType);

    }

    public ArrayList<String> getBuyersOfASubProduct(String subProductId) throws InvalidSubProductIdException {
        String body = convertToJson(subProductId);
        String response = sender.sendRequest(Constants.Commands.getBuyersOfASubProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSubProductIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringListType);
        }
    }


    public HashMap<String, String> getPropertyValuesOfAFile(String fileId) throws InvalidFileIdException {
        String body = convertToJson(fileId);
        String response = sender.sendRequest(Constants.Commands.getPropertyValuesOfAFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidFileIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
    }

    public ArrayList<String[]> getMessagesInAuctionChat(String chatId) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId);
        String response = sender.sendRequest(Constants.Commands.getMessagesInAuctionChat, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidChatIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> getMessagesInChat(String chatId) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId);
        String response = sender.sendRequest(Constants.Commands.getMessagesInChat, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidChatIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void sendMessage(String chatId, String text) throws Exceptions.InvalidChatIdException, Exceptions.InvalidAccountTypeException, Exceptions.UnAuthorizedAccountException {
        String body = convertToJson(chatId, text);
        String response = sender.sendRequest(Constants.Commands.sendMessage, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidChat")) throw new InvalidChatIdException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidAccount")) throw new InvalidAccountTypeException(nameBody[1]);
            else throw new UnAuthorizedAccountException();
        }
    }

    public byte[] loadFileFromDataBase(String path) {
        String body = convertToJson(path);
        String response = sender.sendRequest(Constants.Commands.loadFromDatabase, body);
        return new Gson().fromJson(response, byteArrayType);
    }
}
