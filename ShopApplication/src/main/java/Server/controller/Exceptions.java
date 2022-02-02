package Server.controller;

public class Exceptions {

    public static class WrongPasswordException extends Exception {
        public WrongPasswordException() {
            super("Your password is wrong!");
        }
    }

    public static class UsernameDoesntExistException extends Exception {
        public UsernameDoesntExistException(String username) {
            super("There is no account with this username: " + username);
        }
    }

    public static class ManagerDeleteException extends Exception {
        public ManagerDeleteException() {
            super("You can not delete manager!");
        }
    }

    public static class UsernameAlreadyTakenException extends Exception {
        public UsernameAlreadyTakenException(String username) {
            super("There is an account with username: " + username);
        }
    }

    public static class AdminRegisterException extends Exception {
        public AdminRegisterException() {
            super("Admins should be registered by admins!");
        }
    }

    public static class CustomerLoginException extends Exception {
        public CustomerLoginException() {
            super("You should login as a customer to see your orders!");
        }
    }

    public static class InvalidFieldException extends Exception {
        public InvalidFieldException() {
            super("There is no such field or you can't change this field!");
        }
    }

    public static class InsufficientCreditException extends Exception {
        public InsufficientCreditException(double moneyToPay, double balance) {
            super("You should pay " + moneyToPay + " but your balance is " + balance);
        }
    }

    public static class UnavailableProductException extends Exception {
        public UnavailableProductException(String subProductId) {
            super("There is not enough items of this sub product: " + subProductId);
        }
    }

    public static class InvalidSubProductIdException extends Exception {
        public InvalidSubProductIdException(String subProductId) {
            super("SubProductId: " + subProductId + " is invalid!");
        }
    }

    public static class NotSubProductIdInTheCartException extends Exception {
        public NotSubProductIdInTheCartException(String subProductId) {
            super("There is no subProduct with Id: " + subProductId + " in  your shopping cart!");
        }
    }

    public static class InvalidSellableIdException extends Exception {
        public InvalidSellableIdException(String productId) {
            super("ProductId: " + productId + " is Invalid!");
        }
    }

    public static class ExistingProductException extends Exception {
        public ExistingProductException(String productId) {
            super("There is another product with this name and brand; Its Id is: " + productId);
        }
    }

    public static class InvalidCategoryException extends Exception {
        public InvalidCategoryException(String categoryName) {
            super("There is no category with name:" + categoryName);
        }
    }

    public static class SubCategoryException extends Exception {
        public SubCategoryException(String categoryName, String subCategoryName) {
            super("Category " + subCategoryName + " is a subCategory of category " + categoryName + " and you can not make it its parent!");
        }
    }

    public static class ExistingCategoryException extends Exception {
        public ExistingCategoryException(String categoryName) {
            super("There is already a category with this name: " + categoryName);
        }
    }

    public static class InvalidRequestIdException extends Exception {
        public InvalidRequestIdException(String requestId) {
            super("There is no request with this Id: " + requestId);
        }
    }

    public static class DiscountCodeException extends Exception {
        public DiscountCodeException(String discountCode) {
            super("There is no discount with this code: " + discountCode);
        }
    }

    public static class InvalidAccountsForDiscount extends Exception {
        public InvalidAccountsForDiscount(String accountIds) {
            super("You can only add customer account to a discount these accountIds are invalid" + accountIds);
        }
    }

    public static class ExistingDiscountCodeException extends Exception {
        public ExistingDiscountCodeException(String discountCode) {
            super("There is already a discount code with code: " + discountCode);
        }
    }

    public static class NotLoggedInException extends Exception {
        public NotLoggedInException() {
            super("You should login before you do this action!");
        }
    }

    public static class InvalidLogIdException extends Exception {
        public InvalidLogIdException(String logId) {
            super("You don't have any order with Id: " + logId);
        }
    }

    public static class InvalidSaleIdException extends Exception {
        public InvalidSaleIdException(String saleId) {
            super("There is no sale with Id: " + saleId + " for you!");
        }
    }

    public static class SameAsPreviousValueException extends Exception {
        public SameAsPreviousValueException(String field) {
            super("This value is same as the previous value in field: " + field);
        }
    }

    public static class InvalidFormatException extends Exception {
        public InvalidFormatException(String format) {
            super("This data doesn't match the format" + format);
        }
    }

    public static class HaveNotBoughtException extends Exception {
        public HaveNotBoughtException(String productId) {
            super("You haven't bought this product with Id: " + productId);
        }
    }

    public static class InvalidDateException extends Exception {
        public InvalidDateException() {
            super("End date should be after start date!");
        }
    }

    public static class InvalidProductIdsForASeller extends Exception {
        public InvalidProductIdsForASeller(String falseProductIds) {
            super("Some of product Ids are not available for you:" + falseProductIds);
        }
    }

    public static class NotAvailableSubProductsInCart extends Exception {
        public NotAvailableSubProductsInCart(String notAvailableProducts) {
            super("Some of products in your cart are not enough any more from chosen seller: " + notAvailableProducts);
        }
    }

    public static class InvalidDiscountException extends Exception {
        public InvalidDiscountException(String code) {
            super("There is no discount available for you with code: " + code);
        }
    }

    public static class EmptyCartException extends Exception {
        public EmptyCartException() {
            super("Your cart is empty!");
        }
    }

    public static class UnAuthorizedAccountException extends Exception {
        public UnAuthorizedAccountException() {
            super("This account doesnt have access to shopping cart!");
        }
    }

    public static class ExistingPropertyException extends Exception {
        public ExistingPropertyException(String property) {
            super("This category already has a property with name: " + property);
        }
    }

    public static class InvalidChatIdException extends Exception {
        public InvalidChatIdException(String chatId) {
            super("There is no chat with id: " + chatId);
        }
    }

    public static class InvalidAccountTypeException extends Exception {
        public InvalidAccountTypeException() {
            super("You don't have access to this action!");
        }
    }

    public static class NotParticipantInChat extends Exception {
        public NotParticipantInChat() {
            super("You are not in this chat!");
        }
    }

    public static class ExistingFileException extends Exception {
        public ExistingFileException() {
            super("There is a file with this name and extension!");
        }
    }

    public static class InvalidFileIdException extends Exception {
        public InvalidFileIdException(String fileId) {
            super("There is no file with id: " + fileId);
        }
    }

    public static class DontHaveChatException extends Exception {
        public DontHaveChatException() {
            super("You don't have any chat!");
        }
    }

    public static class InvalidAuctionIdException extends Exception {
        public InvalidAuctionIdException(String auctionId) {
            super("There is no auction with id: " + auctionId);
        }
    }

    public static class AlreadyInAChatException extends Exception {
        public AlreadyInAChatException(String chatId) {
            super("You are currently in chat with ID: " + chatId);
        }
    }

    public static class InvalidSupporterIdException extends Exception {
        public InvalidSupporterIdException(String supporterId) {
            super("There is no supporter with id: " + supporterId);
        }
    }

    public static class InvalidCommissionException extends Exception {
        public InvalidCommissionException() {
            super("Commission should be between 0 and 100");
        }
    }
}
