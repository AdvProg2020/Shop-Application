package controller;
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


}
