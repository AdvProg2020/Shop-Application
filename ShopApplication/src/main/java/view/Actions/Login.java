package view.Actions;

import controller.Exceptions;
import view.Action;
import view.Constants;
import view.View;

import java.util.regex.Matcher;

public class Login extends Action {
    Login() {
        super(Constants.Actions.loginPattern, Constants.Actions.loginCommand);
    }

    private int getPassword(StringBuilder password) {
        System.out.println("Enter your password (enter \"back\" to go back):");
        String input = View.getNextLineTrimmed();
        if (input.equalsIgnoreCase("back")) {
            return -1;
        } else if (input.matches(Constants.usernamePattern)){
            password.setLength(0);
            password.append(input);
            return 0;
        } else {
            return -2;
        }
    }


    @Override
    public void execute(String command) {
        Matcher commandMatcher = getMatcherReady(command);
        String username = commandMatcher.group(1);
        StringBuilder password = new StringBuilder();
        while (true) {
            int output = getPassword(password);
            if (output  == 0) {
                try {
                    mainController.login(username, password.toString());
                    //if without problem
                    System.out.println("logged-in successfully!");
                } catch (Exceptions.UsernameDoesntExistException e) {
                    System.out.println(e.getMessage());
                    break;
                } catch (Exceptions.WrongPasswordException e) {
                    System.out.println(e.getMessage());
                }
                break;
            } else if (output == -1){
                break;
            } else {
                System.out.println("invalid entry");
                continue;
            }
        }
        printSeparator();
    }
}