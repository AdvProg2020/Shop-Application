package Client.HollowController;

public class Exceptions {

    public static class WrongPasswordException extends Exception {
        public WrongPasswordException() {
            super("Your password is wrong!");
        }
    }

    public static class UsernameDoesntExistException extends Exception {
        public UsernameDoesntExistException(String message) {
            super(message);
        }
    }

    public static class ManagerDeleteException extends Exception {
        public ManagerDeleteException() {
            super("You can not delete manager!");
        }
    }

    public static class UsernameAlreadyTakenException extends Exception {
        public UsernameAlreadyTakenException(String message) {
            super(message);
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
        public InsufficientCreditException(String message) {
            super(message);
        }
    }

    public static class UnavailableProductException extends Exception {
        public UnavailableProductException(String message) {
            super(message);
        }
    }

    public static class InvalidSubProductIdException extends Exception {
        public InvalidSubProductIdException(String message) {
            super(message);
        }
    }

    public static class NotSubProductIdInTheCartException extends Exception {
        public NotSubProductIdInTheCartException(String message) {
            super(message);
        }
    }

    public static class InvalidSellableIdException extends Exception {
        public InvalidSellableIdException(String message) {
            super(message);
        }
    }

    public static class ExistingProductException extends Exception {
        public ExistingProductException(String message) {
            super(message);
        }
    }

    public static class InvalidCategoryException extends Exception {
        public InvalidCategoryException(String message) {
            super(message);
        }
    }

    public static class SubCategoryException extends Exception {
        public SubCategoryException(String message) {
            super(message);
        }
    }

    public static class ExistingCategoryException extends Exception {
        public ExistingCategoryException(String message) {
            super(message);
        }
    }

    public static class InvalidRequestIdException extends Exception {
        public InvalidRequestIdException(String message) {
            super(message);
        }
    }

    public static class DiscountCodeException extends Exception {
        public DiscountCodeException(String message) {
            super(message);
        }
    }

    public static class InvalidAccountsForDiscount extends Exception {
        public InvalidAccountsForDiscount(String message) {
            super(message);
        }
    }

    public static class ExistingDiscountCodeException extends Exception {
        public ExistingDiscountCodeException(String message) {
            super(message);
        }
    }

    public static class NotLoggedInException extends Exception {
        public NotLoggedInException() {
            super("You should login before you do this action!");
        }
    }

    public static class InvalidLogIdException extends Exception {
        public InvalidLogIdException(String message) {
            super(message);
        }
    }

    public static class InvalidSaleIdException extends Exception {
        public InvalidSaleIdException(String message) {
            super(message);
        }
    }

    public static class SameAsPreviousValueException extends Exception {
        public SameAsPreviousValueException(String message) {
            super(message);
        }
    }

    public static class InvalidFormatException extends Exception {
        public InvalidFormatException(String message) {
            super(message);
        }
    }

    public static class HaveNotBoughtException extends Exception {
        public HaveNotBoughtException(String message) {
            super(message);
        }
    }

    public static class InvalidDateException extends Exception {
        public InvalidDateException() {
            super("End date should be after start date!");
        }
    }

    public static class InvalidProductIdsForASeller extends Exception {
        public InvalidProductIdsForASeller(String message) {
            super(message);
        }
    }

    public static class NotAvailableSubProductsInCart extends Exception {
        public NotAvailableSubProductsInCart(String message) {
            super(message);
        }
    }

    public static class InvalidDiscountException extends Exception {
        public InvalidDiscountException(String message) {
            super(message);
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
        public ExistingPropertyException(String message) {
            super(message);
        }
    }

    public static class InvalidFileIdException extends Exception {
        public InvalidFileIdException(String message) {
            super(message);
        }
    }

    public static class ExistingFileException extends Exception {
        public ExistingFileException(String message) {
            super(message);
        }
    }

    public static class InvalidChatIdException extends Exception {
        public InvalidChatIdException(String message) {
            super(message);
        }
    }

    public static class InvalidAuctionIdException extends Exception {
        public InvalidAuctionIdException(String message) {
            super(message);
        }
    }

    public static class DontHaveChatException extends Exception {
        public DontHaveChatException(String message) {
            super(message);
        }
    }

    public static class InvalidAccountTypeException extends Exception {
        public InvalidAccountTypeException(String message) {
            super(message);
        }
    }

    public static class AlreadyInAChatException extends Exception {
        public AlreadyInAChatException(String message) {
            super(message);
        }
    }

    public static class InvalidSupporterIdException extends Exception {
        public InvalidSupporterIdException(String message) {
            super(message);
        }
    }

    public static class InvalidCommissionException extends Exception {
        public InvalidCommissionException(String message) {
            super(message);
        }
    }
}
