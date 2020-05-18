package view.Actions;

import controller.Exceptions;
import view.Action;
import view.Constants;
import view.Form;

public class Register extends Action {
    Register() {
        super(Constants.Actions.registerPattern, Constants.Actions.registerCommand);
    }

    private String[] registerCustomer(String username) {
        Form registerForm;
        String[] fields;
        String[] fieldRegex;
        String[] results;
        fields = new String[]{"password", "first name", "last name", "email", "phone", "balance"};
        fieldRegex = new String[]{Constants.usernamePattern, Constants.IRLNamePattern, Constants.IRLNamePattern,
                Constants.emailPattern, Constants.phonePattern, Constants.doublePattern};
        registerForm = new Form(fields, fieldRegex);
        if (registerForm.takeInput() == 0) {
            results = registerForm.getResults();
            try {
                mainController.creatAccount(Constants.customerUserType, username, results[0], results[1], results[2], results[3], results[4], Double.parseDouble(results[5]), null);
                return new String[] {username, results[0]};
            } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                //wont happen.
                System.out.println("sigh! " + e.getMessage());
            }
        }
        return null;
    }

    private String[] registerSeller(String username) {
        Form registerForm;
        String[] fields;
        String[] fieldRegex;
        String[] results;
        fields = new String[]{"password", "first name", "last name", "email", "phone", "balance", "store name"};
        fieldRegex = new String[]{Constants.usernamePattern, Constants.IRLNamePattern, Constants.IRLNamePattern,
                Constants.emailPattern, Constants.phonePattern, Constants.doublePattern, Constants.IRLNamePattern};
        registerForm = new Form(fields, fieldRegex);
        if (registerForm.takeInput() == 0) {
            results = registerForm.getResults();
            try {
                mainController.creatAccount(Constants.sellerUserType, username, results[0], results[1], results[2], results[3], results[4], Double.parseDouble(results[5]), results[6]);
                return new String[] {username, results[0]};
            } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                //wont happen.
                System.out.println("sigh! " + e.getMessage());
            }
        }
        return null;
    }

    private String[] registerAdmin(String username) {
        Form registerForm;
        String[] fields;
        String[] fieldRegex;
        String[] results;
        fields = new String[]{"password", "first name", "last name", "email", "phone"};
        fieldRegex = new String[]{Constants.usernamePattern, Constants.IRLNamePattern, Constants.IRLNamePattern,
                Constants.emailPattern, Constants.phonePattern};
        registerForm = new Form(fields, fieldRegex);
        if (registerForm.takeInput() == 0) {
            results = registerForm.getResults();
            try {
                mainController.creatAccount(Constants.adminUserType, username, results[0], results[1], results[2], results[3], results[4], 0.00, null);
                return new String[] {username, results[0]};
            } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                //wont happen.
                System.out.println("sigh! " + e.getMessage());
            }
        }
        return null;
    }

    private String[] register(int typeIndex, String username) {
        if (username != null) {
            if (typeIndex == 1) {
                return registerCustomer(username);
            } else if (typeIndex == 2) {
                return registerSeller(username);
            } else {
                return registerAdmin(username);
            }
        }
        return null;
    }

    @Override
    public void execute(String command) {
        String type = getGroup(command, 1);
        String username = getGroup(command, 2);
        int index = Constants.getTypeByIndex(type);
        if (index < 1) {
            System.out.println("invalid type. you can enter customer, seller or admin as type");
        } else {
            try {
                mainController.usernameTypeValidation(username, type);
                register(index, username);
            } catch (Exceptions.UsernameAlreadyTakenException e) {
                System.out.println("username already exists!");
            } catch (Exceptions.AdminRegisterException e) {
                System.out.println("only admin can create another admin!");
                printSeparator();
                return;
            }
        }
        printSeparator();
    }

    public void runNLogin(String command) {
        String type = getGroup(command, 1);
        String username = getGroup(command, 2);
        int index = Constants.getTypeByIndex(type);
        if (index < 1) {
            System.out.println("invalid type. you can enter customer, seller or admin as type");
        } else {
            try {
                mainController.usernameTypeValidation(username, type);
                System.out.println("after registration you will automatically get logged-in:");
                String[] usePass = register(index, username);
                if (usePass != null) {
                    mainController.login(usePass[0], usePass[1]);
                }
            } catch (Exceptions.UsernameAlreadyTakenException e) {
                System.out.println("username already exists!");
            } catch (Exceptions.AdminRegisterException e) {
                System.out.println("only admin can create another admin!");
                printSeparator();
                return;
            } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException e) {
                //wont happen
            }
        }
        printSeparator();
    }
}