package controller;
import model.Discount;
import model.account.Customer;

import java.lang.Exception;
public class Exceptions{

    public static class WrongPasswordException extends Exception{
        public WrongPasswordException(){
            super("Your password is wrong!");
        }
    }

    public static class NotExistedUsernameException extends Exception{
        public NotExistedUsernameException(String username){
            super("There is no account with this username: " + username);
        }
    }

    public static class ExistedUsernameException extends Exception{
        public ExistedUsernameException(String username){
            super("There is an account with username: " + username);
        }
    }

    public static class AdminRegisterException extends Exception{
        public AdminRegisterException(){
            super("Admins should be registered by admins!");
        }
    }

    public static class CustomerLoginException extends Exception{
        public CustomerLoginException(){
            super("You should login as a customer to see your orders!");
        }
    }

    public static class InvalidFieldException extends Exception{
        public InvalidFieldException(){
            super("There is no such field or you can't change this field!");
        }
    }

    public static class InsufficientCreditException extends Exception{
        public InsufficientCreditException(){
            super("Your credit is not enough!");
        }
    }

    public static class UnavailableProductException extends Exception{
        public UnavailableProductException(String subProductId){
            super("There is not enough items of this sub product: " + subProductId);
        }
    }

    public static class InvalidSubProductIdException extends Exception{
        public InvalidSubProductIdException(String subProductId){
            super("SubProductId: " + subProductId + " is invalid!");
        }
    }

    public static class NotSubProductIdInTheCartException extends Exception{
        public NotSubProductIdInTheCartException(String subProductId){
            super("There is no subProduct with Id: "+ subProductId + " in  your shopping cart!");
        }
    }

    public static class InvalidProductIdException extends Exception{
        public InvalidProductIdException(String productId){
            super("ProductId: "+ productId + " is Invalid!");
        }
    }

    public static class ExistingProductException extends Exception{
        public ExistingProductException( String productId){
            super("There is another product with this name and brand; Its Id is: "+ productId);
        }
    }

    public static class InvalidCategoryException extends Exception{
        public InvalidCategoryException(String categoryName){
            super("There is no category with name:"+categoryName);
        }
    }

    public static class ExistedCategoryException extends Exception{
        public ExistedCategoryException(String categoryName){ super("There is already a category with this name: "+ categoryName); }
    }

    public static class InvalidRequestIdException extends Exception{
        public InvalidRequestIdException(String requestId){
            super("There is no request with this Id: "+ requestId);
        }
    }

    public static class DiscountCodeException extends Exception{
        public DiscountCodeException(String discountCode){
            super("There is no discount with this code: "+ discountCode);
        }
    }

    public static class ExistingDiscountCodeException extends Exception{
        public ExistingDiscountCodeException(String discountCode){
            super("There is already a discount code with code: " + discountCode);
        }
    }

    public static class CustomerIdException extends Exception{
        public CustomerIdException(String Id){
            super("There is no customer with this Id: "+ Id);
        }
    }

    public static class NotLoggedInException extends Exception{
        public NotLoggedInException(){
            super("You should login before you do this action!");
        }
    }

    public static class InvalidLogIdException extends Exception{
        public InvalidLogIdException(String logId){
            super("You don't have any order with Id: "+ logId);
        }
    }

    public static class InvalidSaleIdException extends Exception{
        public InvalidSaleIdException(String saleId){
            super("There is no sale with Id: " + saleId +" for you!");
        }
    }

    public static class SameAsPreviousValueException extends Exception{
        public SameAsPreviousValueException(String field){
            super("This value is same as the previous value in field: "+ field);
        }
    }

    public static class InvalidFormatException extends Exception{
        public InvalidFormatException(String format){
            super("This data doesn't match the format" + format);
        }
    }

    public static class HaveNotBoughtException extends Exception{
        public HaveNotBoughtException(String productId){
            super("You haven't bought this product with Id: "+ productId);
        }
    }
}
