package controller;
import model.Discount;

import java.lang.Exception;
public class Exceptions{

    public static class WrongPasswordException extends Exception{
        public WrongPasswordException(){
            super("Your password is wrong!");
        }
    }

    public static class NotExistedUsernameException extends Exception{
        public NotExistedUsernameException(){
            super("There is no account with this username!");
        }
    }

    public static class ExistedUsernameException extends Exception{
        public ExistedUsernameException(){
            super("Such username has already existed!");
        }
    }

    public static class AdminRegisterException extends Exception{
        public AdminRegisterException(){
            super("Admins should be registered by admins!");
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
        public UnavailableProductException(String name, String subProductId){
            super("Product: "+name+" with Id: "+subProductId +" is unavailable!");
        }
        public UnavailableProductException(){
            super("There is not enough items of this sub product!");
        }
    }

    public static class InvalidSubProductIdException extends Exception{
        public InvalidSubProductIdException(){
            super("SubProductId is invalid!");
        }

        public InvalidSubProductIdException(String subProductId){
            super("There is no sub product with Id: "+ subProductId + " in  your shopping cart!");
        }
    }

    public static class InvalidProductIdException extends Exception{
        public InvalidProductIdException(){
            super("ProductId is Invalid!");
        }
    }

    public static class InvalidCategoryException extends Exception{
        public InvalidCategoryException(String categoryName){
            super("There is no category with name:"+categoryName);
        }

        public InvalidCategoryException(){
            super("There is already a category with this name!");
        }
    }

    public static class InvalidRequestIdException extends Exception{
        public InvalidRequestIdException(){
            super("There is no request with this Id!");
        }
    }

    public static class DiscountCodeException extends Exception{
        public DiscountCodeException(){
            super("There is no discount with this code!");
        }
    }

    public static class NotLoggedInException extends Exception{
        public NotLoggedInException(){
            super("You should login before you do this action!");
        }
    }
}
