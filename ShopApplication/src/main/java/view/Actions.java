package view;

import controller.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Matcher;

//TODO: printSeparator();
//TODO: isInProducts --> int IndexByID
//TODO: getGroup()
//TODO: types start with capital.
//TODO: sout completion messages. ex: .... done successfully.
//TODO: index choosing
//TODO: nullptr exception.
//TODO: bayad in fact ke age taraf anonymous bd betone be sabad kharid az inja ezafe kone mahsoolo handle konim va inke age customer ya anonymous nabd natone.
//TODO: getDefaultSubProductID();
public class Actions {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;

    static {
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
    }

    public static class BackAction extends Action {
        private Menu parent;
        BackAction(String name, Menu parent) {
            super(name, Constants.Actions.backPattern, Constants.Actions.backCommand);
            this.parent = parent;
        }

        @Override
        public void execute(String command) {
            parent.run();
        }

        public void setParent(Menu newParent) {
            this.parent = newParent;
        }
    }

    public static class ExitAction extends Action {
        ExitAction(String name) {
            super(name, Constants.Actions.exitPattern, Constants.Actions.exitCommand);
        }

        @Override
        public void execute(String command) {
            System.exit(1);
        }
    }

    public static  class LoginAction extends Action {
        LoginAction(String name) {
            super(name, Constants.Actions.loginPattern, Constants.Actions.loginCommand);
        }

        private String getPassword() {
            System.out.println("Enter your password (enter \"back\" to go back):");
             String input = View.getNextLineTrimmed();
             if (input.equalsIgnoreCase("back")) {return null;}
             else {return input;}
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String username = commandMatcher.group(1);
            while(true) {
                String password = getPassword();
                if (password != null) {
                    try {
                        mainController.login(username, password);
                    } catch (Exceptions.UsernameDoesntExistException | Exceptions.WrongPasswordException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    //if without problem
                    System.out.println("logged-in successfully!");
                    return;
                } else {
                    break;
                }
            }
        }
    }

    public static class RegisterAction extends Action {
        RegisterAction(String name) {
            super(name, Constants.Actions.registerPattern, Constants.Actions.registerCommand);
        }

        private void registerCustomer(String username) {
            Form registerForm;
            String[] fields;
            String[] fieldRegex;
            String[] results;
            fields = new String[] { "password", "first name", "last name", "email", "phone", "balance"};
            fieldRegex = new String[] {Constants.argumentPattern, Constants.argumentPattern,Constants.argumentPattern,
                    Constants.argumentPattern, Constants.argumentPattern, Constants.doublePattern};
            registerForm = new Form(fields, fieldRegex);
            if(registerForm.takeInput() == 0) {
                results = registerForm.getResults();
                try {
                    mainController.creatAccount(Constants.customerUserType, username, results[0], results[1], results[2], results[3], results[4], Double.valueOf(results[5]), null);
                } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                    //wont happen.
                    System.out.println("sigh! " + e.getMessage());
                }
            }
        }

        private void registerSeller(String username) {
            Form registerForm;
            String[] fields;
            String[] fieldRegex;
            String[] results;
            fields = new String[] { "password", "first name", "last name", "email", "phone", "balance", "store name"};
            fieldRegex = new String[] {Constants.argumentPattern, Constants.argumentPattern,Constants.argumentPattern,
                    Constants.argumentPattern, Constants.argumentPattern, Constants.doublePattern, Constants.argumentPattern};
            registerForm = new Form(fields, fieldRegex);
            if(registerForm.takeInput() == 0) {
                results = registerForm.getResults();
                try {
                    mainController.creatAccount(Constants.sellerUserType, username, results[0], results[1], results[2], results[3], results[4], Double.valueOf(results[5]), results[6]);
                } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                    //wont happen.
                    System.out.println("sigh! " + e.getMessage());
                }
            }
        }

        private void registerAdmin(String username) {
            Form registerForm;
            String[] fields;
            String[] fieldRegex;
            String[] results;
            fields = new String[] { "password", "first name", "last name", "email", "phone"};
            fieldRegex = new String[] {Constants.argumentPattern, Constants.argumentPattern,Constants.argumentPattern,
                    Constants.argumentPattern, Constants.argumentPattern};
            registerForm = new Form(fields, fieldRegex);
            if(registerForm.takeInput() == 0) {
                results = registerForm.getResults();
                try {
                    mainController.creatAccount(Constants.adminUserType, username, results[0], results[1], results[2], results[3], results[4], 0.00, null);
                } catch (Exceptions.UsernameAlreadyTakenException | Exceptions.AdminRegisterException e) {
                    //wont happen.
                    System.out.println("sigh! " + e.getMessage());
                }
            }
        }

        private void register(int typeIndex, String username) {
            if (username != null) {
                if (typeIndex == 1) {
                    registerCustomer(username);
                } else if (typeIndex == 2) {
                    registerSeller(username);
                } else {
                    registerAdmin(username);
                }
            }
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
    }

    public static class ShowProductsAction extends Action {
        private ArrayList<String> categoryTree;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String[]> currentProducts;

        ShowProductsAction(String name, ArrayList<String> categoryTree, String[] currentFilters, StringBuilder currentSort, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.showProductsPattern, Constants.Actions.showProductsCommand);
            this.categoryTree = categoryTree;
            this.currentFilters = currentFilters;
            this.currentSort = currentSort;
            this.currentProducts = currentProducts;
        }

        private void refreshCurrentProducts(String categoryName) {
            try {
                currentProducts.clear();
                currentProducts.addAll(mainController.getProductsOfThisCategory(categoryName));
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
        }

        private void showAllProducts() {
            refreshCurrentProducts("superCategory");
            try {
                printList(mainController.showProducts(productIDs(), null, true,
                        new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)}));
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
        }

        private String getSortingField() {
            if (currentSort.length() == 0) {return null;}
            int indicatorIndex = currentSort.lastIndexOf("creasing");
            indicatorIndex -= 3;
            return currentSort.substring(0, indicatorIndex);
        }

        private boolean isIncreasing() {
            if (currentSort.length() == 0) {return true;}
            int indicatorIndex = currentSort.lastIndexOf("creasing");
            indicatorIndex -= 2;
             return currentSort.substring(indicatorIndex).equalsIgnoreCase("increasing");
        }

        private ArrayList<String> productIDs() {
            ArrayList<String> productIDS = new ArrayList<>();
            for (String[] product : currentProducts) {
                productIDS.add(product[0]);
            }
            return productIDS;
        }

        @Override
        public void execute(String command) {
            String categoryName;
            if ( getMatcherReady(command).groupCount() == 1) {showAllProducts();}
            else {
                if (categoryTree.size() == 0) {
                    categoryName = "superCategory";
                } else {
                    categoryName = categoryTree.get(categoryTree.size() - 1);
                }
                try {
                    refreshCurrentProducts(categoryName);
                    printList(mainController.showProducts(productIDs(), getSortingField(), isIncreasing(), currentFilters));
                } catch (Exceptions.InvalidProductIdException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class ShowCategories extends Action {
        private ArrayList<String> categoryTree;
        private ArrayList<String[]> availableCategories;
        ShowCategories(String name, ArrayList<String> categoryTree, ArrayList<String[]> availableCategories) {
            super(name, Constants.Actions.showCategoriesPattern, Constants.Actions.showCategoriesCommand);
            this.availableCategories = availableCategories;
        }

        private void refreshCategories(String lastCategory) throws Exceptions.InvalidCategoryException {
            availableCategories.clear();
            availableCategories.addAll(mainController.getSubCategoriesOfThisCategory(lastCategory));
        }

        @Override
        public void execute(String command) {
            try {
                String lastCategory;
                if (categoryTree.size() == 0) {
                    lastCategory = "superCategory";
                } else {
                    lastCategory = categoryTree.get(categoryTree.size() - 1);
                }
                refreshCategories(lastCategory);
                printList(availableCategories);
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ChooseCategoryAction extends Action {
        private ArrayList<String> categoryTree;
        private ArrayList<String[]> availableCategories;
        ChooseCategoryAction(String name, ArrayList<String> categoryTree, ArrayList<String[]> availableCategories) {
            super(name, Constants.Actions.chooseCategoryPattern, Constants.Actions.chooseCategoryCommand);
            this.availableCategories = availableCategories;
            this.categoryTree = categoryTree;
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, availableCategories);
            if (index != 0) {
                categoryTree.add(availableCategories.get(index - 1)[1]);
            }
        }
    }

    public static class RevertCategoryAction extends Action {
        private ArrayList<String> categoryTree;
        RevertCategoryAction(String name, ArrayList<String> categoryTree) {
            super(name, Constants.Actions.revertCategoryPattern, Constants.Actions.revertCategoryCommand);
            this.categoryTree = categoryTree;
        }

        private void revertCategory(int revertNumber) {
            int size = categoryTree.size();
            if (revertNumber >= size) {
                categoryTree.clear();
            } else {
                categoryTree.removeAll(categoryTree.subList(size - revertNumber, size));
            }
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            int revertNumber;
            if (commandMatcher.groupCount() > 0) {
                revertNumber = Integer.parseInt(commandMatcher.group(1));
            } else {
                revertNumber = 1;
            }
            revertCategory(revertNumber);
            System.out.println("reverted successfully");
            printSeparator();
        }
    }

    public static class ChooseSorting extends Action {
        private StringBuilder currentSort;
        private String[] availableSorts;

        ChooseSorting(String name, StringBuilder currentSort, String[] availableSorts) {
            super(name, Constants.Actions.sortPattern, Constants.Actions.sortCommand);
            this.currentSort = currentSort;
            this.availableSorts = availableSorts;
        }

        private void showAvailableSorts() {
            for (int i = 0; i < availableSorts.length; i++) {
                System.out.println((i + 1) + ". " + availableSorts[i]);
            }
        }

        private int modifySortingArgument() {
            while(true) {
                System.out.println("choose the sorting method (enter \"back\" to go back):");
                String input = View.getNextLineTrimmed();
                if (input.equalsIgnoreCase("back")) {return 0;}
                int entry = checkSortingArgument(input);
                if (entry == -1) {continue;}
                else {
                    currentSort.setLength(0);
                    currentSort.append(availableSorts[entry]);
                    return 1;
                }
            }
        }

        private int checkSortingArgument(String input) {
            for (int i = 0; i < availableSorts.length; i++) {
                if (input.equals(Integer.toString(i + 1)) || input.equalsIgnoreCase(availableSorts[i])) {
                    return i + 1;
                }
            }
            System.out.println("invalid entry");
            return -1;
        }

        private int modifySortingMethod() {
            while(true) {
                System.out.println("do want sorting to be increasing or decreasing? (I or D):");
                String input = View.getNextLineTrimmed();
                if (input.equalsIgnoreCase("back")) {return 0;}
                if (input.equalsIgnoreCase("I")) {
                    currentSort.append(" increasing");
                } else if (input.equalsIgnoreCase("D")) {
                    currentSort.append(" decreasing");
                } else {
                    System.out.println("invalid entry");
                    continue;
                }
            }
        }

        @Override
        public void execute(String command) {
            showAvailableSorts();
            while (true) {
                int response = modifySortingArgument();
                if (response == 0){break;}
                response = modifySortingMethod();
                if (response != 0){break;}
            }
            printSeparator();
        }
    }

    public static class ShowCurrentSort extends Action {
        private StringBuilder currentSort;

        ShowCurrentSort(String name, StringBuilder currentSort) {
            super(name, Constants.Actions.showCurrentSortPattern, Constants.Actions.showCurrentSortCommand);
            this.currentSort = currentSort;
        }

        @Override
        public void execute(String command) {
            if (currentSort != null) {
                System.out.println(currentSort);
            } else {
                System.out.println("no sorting method is chosen.");
            }
            printSeparator();
        }
    }

    public static class DisableSort extends Action {
        private StringBuilder currentSort;

        DisableSort(String name, StringBuilder currentSort) {
            super(name, Constants.Actions.disableSortPattern, Constants.Actions.disableSortCommand);
            this.currentSort = currentSort;
        }

        @Override
        public void execute(String command) {
            currentSort.setLength(0);
            printSeparator();
        }
    }

    public static class ChooseFiltering extends Action {
        private String[] currentFilters;
        private String[] availableFilters;
        ChooseFiltering(String name, String[] currentFilters, String[] availableFilters) {
            super(name, Constants.Actions.filterPattern, Constants.Actions.filterCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters;
        }

        private void showAvailableFilters() {
            for (int i = 0; i < availableFilters.length; i++) {
                System.out.println((i + 1) + ". " + availableFilters[i] + ": " + currentFilters[i]);
            }
        }

        private int modifyFilter(int filterIndex) {
            String[] expectingEntry = new String[] {"Y or N", "Double number", "Double number", "non-space String", "String", "String", "Double number"};
            String[] expectingRegex = new String[] {Constants.caseInsensitiveMode + "[YN]", Constants.doublePattern, Constants.doublePattern,
                                        Constants.argumentPattern, ".+", ".+", Constants.doublePattern};
            while (true) {
                System.out.println("enter new filtering (" + expectingEntry[filterIndex - 1] + "):");
                String entry = View.getNextLineTrimmed();
                if (entry.matches(expectingRegex[filterIndex - 1])) {
                    if (filterIndex == 1) { currentFilters[0] = (entry.equalsIgnoreCase("y") ? "true":"false");}
                    else {currentFilters[filterIndex - 1] = entry;}
                    return 0;
                } else if (entry.equalsIgnoreCase("back")) return -1;
                else {
                    System.out.println("invalid entry.");
                }
            }
        }

        @Override
        public void execute(String command) {
            while (true) {
                showAvailableFilters();
                System.out.println("choose the filtering field by index (or \"back\" to go back):");
                String input = View.getNextLineTrimmed();
                if (input.matches(Constants.unsignedIntPattern) && Integer.parseInt(input) <= availableFilters.length) {
                    if(modifyFilter(Integer.parseInt(input)) == -1) continue;
                    else break;
                } else if (input.equalsIgnoreCase("back")) break;
                else {
                    System.out.println("invalid entry.");
                    continue;
                }
            }
            printSeparator();
        }
    }

    public static class ShowCurrentFilters extends Action {
        private String[] currentFilters;
        private String[] availableFilters;

        ShowCurrentFilters(String name, String[] currentFilters, String[] availableFilters) {
            super(name, Constants.Actions.showCurrentFiltersPattern, Constants.Actions.showCurrentFiltersCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters;
        }

        @Override
        public void execute(String command) {
            for (int i = 0; i < currentFilters.length; i++) {
                System.out.print(availableFilters[i] + ": ");
                if (currentFilters[i] == null) {
                    System.out.println("N/A");
                } else {
                    System.out.println(currentFilters[i]);
                }
            }
            printSeparator();
        }
    }

    public static class DisableFilter extends Action {
        private String[] currentFilters;
        private String[] availableFilters;
        DisableFilter(String name, String[] currentFilters, String[] availableFilters) {
            super(name, Constants.Actions.disableFilterPattern, Constants.Actions.disableFilterCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters;
        }

        private void showAvailableFilters() {
            for (int i = 0; i < availableFilters.length; i++) {
                System.out.println((i + 1) + ". " + availableFilters[i]);
            }
        }

        private void disableFilter(int filterIndex) {
            switch (filterIndex) {
                case 1:
                    currentFilters[0] = "true";
                    break;
                case 2:
                case 3:
                case 7:
                    currentFilters[filterIndex - 1] = "0.00";
                    break;
                case 4:
                case 5:
                case 6:
                    currentFilters[filterIndex - 1] = null;
                    break;
            }
        }

        @Override
        public void execute(String command) {
            while (true) {
                showAvailableFilters();
                System.out.println("choose the filtering field by index (or \"back\" to go back):");
                String input = View.getNextLineTrimmed();
                if (input.matches(Constants.unsignedIntPattern) && Integer.parseInt(input) <= availableFilters.length) {
                    disableFilter(Integer.parseInt(input));
                    break;
                } else if (input.equalsIgnoreCase("back")) break;
                else {
                    System.out.println("invalid entry.");
                    continue;
                }
            }
            printSeparator();
        }
    }

    public static class ShowSales extends Action {
        private ArrayList<String[]> currentSales;
        ShowSales(String name, ArrayList<String[]> currentSales) {
            super(name, Constants.Actions.showOffsPattern, Constants.Actions.showOffsCommand);
            this.currentSales = currentSales;
        }

        private void refreshCurrentSales() {
            currentSales.clear();
            currentSales.addAll(mainController.sales());
        }

        private void printSaleInfo(String[] sale) throws Exceptions.InvalidSaleIdException {
            for (int i = 0; i < sale.length; i++) {
                System.out.print(" " + sale[i]);
            }
            System.out.print("\n");
            ArrayList<String[]> productsInSale = mainController.getProductsInSale(sale[0]);
            int size = productsInSale.size();
            for (int i = 0; i < size; i++) {
                System.out.print("\t");
                for (int j = 0; j < productsInSale.get(i).length; i++) {
                    System.out.print(" " + productsInSale.get(i)[j]);
                }
            }
        }

        @Override
        public void execute(String command) {
            try {
                refreshCurrentSales();
                int size = currentSales.size();
                for (int i = 0; i < size; i++) {
                    System.out.print((i + 1) + ".");
                    printSaleInfo(currentSales.get(i));
                }
            } catch (Exceptions.InvalidSaleIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ShowInSaleProducts extends Action {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String[]> currentProducts;

        public ShowInSaleProducts(String name, StringBuilder currentSort, String[] currentFilters, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.showInSaleProductsPattern, Constants.Actions.showInSaleProductsCommand);
            this.currentSort = currentSort;
            this.currentFilters = currentFilters;
            this.currentProducts = currentProducts;
        }

        private String getSortingField() {
            if (currentSort.length() == 0) {return null;}
            int indicatorIndex = currentSort.lastIndexOf("creasing");
            indicatorIndex -= 3;
            return currentSort.substring(0, indicatorIndex);
        }

        private boolean isIncreasing() {
            if (currentSort.length() == 0) {return true;}
            int indicatorIndex = currentSort.lastIndexOf("creasing");
            indicatorIndex -= 2;
            return currentSort.substring(indicatorIndex).equalsIgnoreCase("increasing");
        }

        private void refreshCurrentProducts() {
            currentProducts.clear();
            currentProducts.addAll(mainController.showInSaleProducts(getSortingField(), isIncreasing(), currentFilters));
        }

        @Override
        public void execute(String command) {
            refreshCurrentProducts();
            System.out.println("current sales:");
            printList(currentProducts);
            printSeparator();
        }
    }

    public static class ViewPersonalInfo extends Action {
        ViewPersonalInfo(String name) {
            super(name, Constants.Actions.viewPersonalInfoPattern, Constants.Actions.viewPersonalInfoCommand);
        }

        private void showPersonalInfo(String[] info) {
            System.out.println("1. username: " + info[0]);
            System.out.println("2. type: " + info[1]);
            System.out.println("3. first name: " + info[2]);
            System.out.println("4. last name: " + info[3]);
            System.out.println("5. email: " + info[4]);
            System.out.println("6. phone number: " + info[5]);
            if (info.length > 6) {
                System.out.println("7. balance: " + info[6]);
            }
            if (info.length > 7) {
                System.out.println("8. store name: " + info[7]);
            }
        }

        @Override
        public void execute(String command) {
            String[] info = mainController.viewPersonalInfo();
            showPersonalInfo(info);
        }
    }

    public static class EditField extends Action {
        private String[] editableFields;

        EditField(String name, String[] editableFields) {
            super(name, Constants.Actions.editFieldPattern, Constants.Actions.editFieldCommand);
            this.editableFields = editableFields;
        }

        private void showEditableFields() {
            for (int i = 0; i < editableFields.length; i++) {
                System.out.println((i + 1) + ". " + editableFields[i]);
            }
        }

        private int editField(int fieldIndex) {
            String type = mainController.getType();
            String response;
            while(true) {
                System.out.println("enter new info");
                response = View.getNextLineTrimmed();
                if (response.equalsIgnoreCase("back")) {return -1;}
                try {
                    if (type.equals("Customer")) {
                        customerController.editPersonalInfo(editableFields[fieldIndex], response);
                    } else if (type.equals("Seller")) {
                        sellerController.editPersonalInfo(editableFields[fieldIndex], response);
                    } else {
                        adminController.editPersonalInfo(editableFields[fieldIndex], response);
                    }
                    return 0;
                } catch (Exceptions.InvalidFieldException e) {
                    //wont happen.
                    System.out.println(e.getMessage());
                } catch (Exceptions.SameAsPreviousValueException e) {
                    System.out.println("new value cant be the same as previous!");
                    continue;
                }
            }
        }

        @Override
        public void execute(String command) {
            while(true) {
                showEditableFields();
                System.out.println("enter the field to edit (index):");
                String response = View.getNextLineTrimmed();
                if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                    if (editField(Integer.parseInt(response)) == -1) {continue;}
                    else break;
                } else if (response.equalsIgnoreCase("back")) {
                    break;
                } else {
                    System.out.println("invalid entry");
                    continue;
                }
            }
            printSeparator();
        }
    }

    public static class ShowSellerCompanyInfo extends Action {
        ShowSellerCompanyInfo(String name) {
            super(name, Constants.Actions.showSellerCompanyInfoPattern, Constants.Actions.showSellerCompanyInfoCommand);
        }

        @Override
        public void execute(String command) {
            ArrayList<String> info = sellerController.viewCompanyInformation();
            info.forEach(i -> System.out.println(i));
            printSeparator();
        }
    }

    public static class ShowSellerSellHistory extends Action {
        private ArrayList<String[]> sellLogs;
        ShowSellerSellHistory(String name, ArrayList<String[]> sellLogs) {
            super(name, Constants.Actions.showSellerSellHistoryPattern, Constants.Actions.showSellerSellHistoryCommand);
            this.sellLogs = sellLogs;
        }

        private void refreshSellLogs() {
            sellLogs.clear();
            sellLogs.addAll(sellerController.getAllSellLogs());
        }

        @Override
        public void execute(String command) {
            refreshSellLogs();
            System.out.println("sell history:");
            printList(sellLogs);
            printSeparator();
        }
    }

    public static class ViewSingleSellHistory extends Action {
        private ArrayList<String[]> sellLogs;
        ViewSingleSellHistory(String name, ArrayList<String[]> sellLogs) {
            super(name, Constants.Actions.showSingleSellLogPattern, Constants.Actions.showSingleSellLogCommand);
            this.sellLogs = sellLogs;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, sellLogs);
                if (index != 0) {
                    printList(sellerController.getSellLogWithId(sellLogs.get(index - 1)[0]));
                }
            } catch (Exceptions.InvalidLogIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ShowSellerBalance extends Action {
        ShowSellerBalance(String name) {
            super(name, Constants.Actions.showSellerBalancePattern, Constants.Actions.showSellerBalanceCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("your balance is: " + sellerController.viewBalance());
            printSeparator();
        }
    }

    public static class ShowCustomerBalance extends Action {
        ShowCustomerBalance(String name) {
            super(name, Constants.Actions.showCustomerBalancePattern, Constants.Actions.showCustomerBalanceCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("your balance is: " + customerController.viewBalance());
            printSeparator();
        }
    }

    public static class ShowCustomerDiscountCodes extends Action {
        ShowCustomerDiscountCodes(String name) {
            super(name, Constants.Actions.showCustomerDiscountCodesPattern, Constants.Actions.showCustomerDiscountCodesCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("discount codes:");
            printList(customerController.viewDiscountCodes());
            printSeparator();
        }
    }

    public static class ProductDetailMenu extends Action {
        private ArrayList<String[]> currentProducts;
        ProductDetailMenu(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.productDetailMenuPattern, Constants.Actions.productDetailMenuCommand);
            this.currentProducts = currentProducts;
        }

        @Override
        public void execute(String command) {
            int index = Integer.parseInt(getGroup(command, 1));
            if (index > currentProducts.size()) {
                System.out.println("invalid index. please enter a number between 1 and " + currentProducts.size());
            } else {
                try {
                    mainController.showProduct(currentProducts.get(index - 1)[0]);
                } catch (Exceptions.InvalidProductIdException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            Menu.getProductDetailMenu().runByProductID(currentProducts.get(index - 1)[0]);
        }
    }

    public static class DigestProduct extends Action {
        private StringBuilder productID;
        DigestProduct(String name, StringBuilder productID) {
            super(name ,Constants.Actions.digestProductPattern, Constants.Actions.digestProductCommand);
            this.productID = productID;
        }

        private void showInfo(String[] info) {
            System.out.println("1. product ID: " + info[0]);
            System.out.println("2. product name: " + info[1]);
            System.out.println("3. product brand: " + info[2]);
            System.out.println("4. product average rating score: " + info[4]);
            System.out.println("5. product description: " + info[3]);
        }

        @Override
        public void execute(String command) {
            try {
                String[] productInfo = mainController.digest(productID.toString());
                showInfo(productInfo);
                System.out.println("attributes");
                mainController.getSpecialPropertiesOfAProduct(productID.toString()).forEach(att -> System.out.println(att));
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AddToCart extends Action {
        private StringBuilder subProductID;
        AddToCart(String name, StringBuilder subProductID) {
            super(name, Constants.Actions.addToCartPattern, Constants.Actions.addToCartCommand);
            this.subProductID = subProductID;
        }

        @Override
        public void execute(String command) {
            try {
                mainController.addToCart(subProductID.toString(), 1);
                System.out.println("added to the cart");
            } catch (Exceptions.InvalidSubProductIdException | Exceptions.UnavailableProductException e) {
                System.out.println(e.getMessage());
                System.out.println("please change seller and try again");
            }
            printSeparator();
        }
    }

    public static class SelectSeller extends Action {
        private StringBuilder productID;
        private StringBuilder subProductID;
        private ArrayList<String[]> subProducts;

        SelectSeller(String name, StringBuilder productID, StringBuilder subProductID) {
            super(name, Constants.Actions.selectSellerPattern, Constants.Actions.selectSellerCommand);
            this.subProductID = subProductID;
            this.productID = productID;
            this.subProducts = new ArrayList<>();
        }

        private void refreshSubProducts() throws Exceptions.InvalidProductIdException {
            subProducts.clear();
            subProducts.addAll(mainController.subProductsOfAProduct(productID.toString()));
        }

        private void selectSeller(int sellerIndex) {
            subProductID.setLength(0);
            subProductID.append(subProducts.get(sellerIndex - 1)[0]);
        }

        @Override
        public void execute(String command) {
            try {
                refreshSubProducts();
                while(true) {
                    printList(subProducts);
                    System.out.println("enter the seller index:");
                    String input = View.getNextLineTrimmed();
                    if (input.matches(Constants.unsignedIntPattern) && Integer.parseInt(input) <= subProducts.size()) {
                        selectSeller(Integer.parseInt(input));
                    } else if (input.equalsIgnoreCase("back")) break;
                    else {
                        System.out.println("invalid entry");
                    }
                }
                printSeparator();
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println("product ID is invalid. unable to refresh sub-products.");
            }
            printSeparator();
        }
    }

    public static class ShowCurrentSeller extends Action {
        private StringBuilder subProductID;
        ShowCurrentSeller(String name, StringBuilder subProductID) {
            super(name, Constants.Actions.showCurrentSellerPattern, Constants.Actions.showCurrentSellerCommand);
        }

        @Override
        public void execute(String command) {
            try {
                System.out.println("current seller is: " + mainController.getSubProductByID(subProductID.toString())[1]);
            } catch (Exceptions.InvalidSubProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CompareProductByID extends Action {
        private StringBuilder productID;
        CompareProductByID(String name, StringBuilder productID) {
            super(name, Constants.Actions.compareProductByIDPattern, Constants.Actions.compareProductByIDCommand);
            this.productID = productID;
        }


        private void printDigestInfo(String[] productInfo, String[] otherProductInfo) {
            System.out.println("1. ID: " + productInfo[0] + " | " + otherProductInfo[0]);
            System.out.println("2. name: " + productInfo[1] + " | " + otherProductInfo[1]);
            System.out.println("3. brand: " + productInfo[2] + " | " + otherProductInfo[2]);
            System.out.println("4. average rating score: " + productInfo[4] + " | " + otherProductInfo[4]);
            System.out.println("5. description: " + productInfo[3] + " | " + otherProductInfo[3]);
        }

        private void printSpecialProperties(String productID, ArrayList<String> specialProperties) {
            System.out.println("product with ID: " + productID + "'s special properties:");
            specialProperties.forEach(sp -> System.out.println(sp));
        }

        @Override
        public void execute(String command) {
            String otherProductID = getGroup(command, 1);
            try {
                String[] productInfo = mainController.digest(productID.toString());
                String[] otherProductInfo = mainController.digest(otherProductID);
                ArrayList<String> productSP = mainController.getSpecialPropertiesOfAProduct(productID.toString());
                ArrayList<String> otherProductSP = mainController.getSpecialPropertiesOfAProduct(otherProductID);
                printDigestInfo(productInfo, otherProductInfo);
                printSeparator();
                printSpecialProperties(productID.toString(), productSP);
                printSeparator();
                printSpecialProperties(otherProductID, otherProductSP);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AddComment extends Action {
        private StringBuilder productID;
        AddComment(String name, StringBuilder productID) {
            super(name, Constants.Actions.addReviewPattern, Constants.Actions.addReviewCommand);
            this.productID = productID;
        }

        @Override
        public void execute(String command) {
            try {
                String[] fields = new String[]{"title", "body"};
                String[] regex = new String[]{Constants.argumentPattern, ".+"};
                Form reviewForm = new Form(fields, regex);
                if (reviewForm.takeInput() == 0) {
                    String[] results = reviewForm.getResults();
                    mainController.addReview(productID.toString(), results[0], results[1]);
                }
            } catch (Exceptions.InvalidProductIdException | Exceptions.NotLoggedInException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ShowReviews extends Action {
        private StringBuilder productID;
        ShowReviews(String name, StringBuilder productID) {
            super(name, Constants.Actions.showReviewsPattern, Constants.Actions.showReviewsCommand);
            this.productID = productID;
        }

        @Override
        public void execute(String command) {
            try {
                System.out.println("product reviews:");
                printList(mainController.reviewsOfProductWithId(productID.toString()));
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminViewAllUsers extends Action {
        private ArrayList<String[]> users;
        AdminViewAllUsers(String name, ArrayList<String[]> users) {
            super(name, Constants.Actions.adminViewAllUsersPattern, Constants.Actions.adminViewAllUsersCommand);
            this.users = users;
        }

        private void refreshUsers() {
            users.clear();
            users.addAll(adminController.manageUsers());
        }

        @Override
        public void execute(String command) {
            refreshUsers();
            printList(users);
            printSeparator();
        }
    }

    public static class AdminViewUser extends Action {
        private ArrayList<String[]> users;
        AdminViewUser(String name, ArrayList<String[]> users) {
            super(name, Constants.Actions.adminViewUserPattern, Constants.Actions.adminViewUserCommand);
            this.users = users;
        }

        private void showPersonalInfo(String[] info) {
            System.out.println("1. username: " + info[0]);
            System.out.println("2. type: " + info[1]);
            System.out.println("3. first name: " + info[2]);
            System.out.println("4. last name: " + info[3]);
            System.out.println("5. email: " + info[4]);
            System.out.println("6. phone number: " + info[5]);
            if (info.length > 6) {
                System.out.println("7. balance: " + info[6]);
            }
            if (info.length > 7) {
                System.out.println("8. store name: " + info[7]);
            }
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, users);
                if (index != 0) {
                    String[] info = adminController.viewUsername(users.get(index - 1)[1]);
                    showPersonalInfo(info);
                }
            } catch (Exceptions.UsernameDoesntExistException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminDeleteUser extends Action {
        private ArrayList<String[]> users;
        AdminDeleteUser(String name, ArrayList<String[]> users) {
            super(name, Constants.Actions.adminDeleteUserPattern, Constants.Actions.adminDeleteUserCommand);
            this.users = users;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, users);
                if (index != 0) {
                    adminController.deleteUsername(users.get(index - 1)[1]);
                    System.out.println("user removed successfully");
                }
            } catch (Exceptions.UsernameDoesntExistException | Exceptions.ManagerDeleteException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminCreateAdmin extends Action {
        AdminCreateAdmin(String name) {
            super(name, Constants.Actions.adminCreateAdminPattern, Constants.Actions.adminCreateAdminCommand);
        }

        @Override
        public void execute(String command) {
            try {
                Form registerForm;
                String[] fields;
                String[] fieldRegex;
                String[] results;
                fields = new String[] {"username", "password", "first name", "last name", "email", "phone"};
                fieldRegex = new String[] {Constants.argumentPattern, Constants.argumentPattern, Constants.argumentPattern,Constants.argumentPattern,
                        Constants.argumentPattern, Constants.argumentPattern};
                registerForm = new Form(fields, fieldRegex);
                if(registerForm.takeInput() == 0) {
                    results = registerForm.getResults();
                    adminController.creatAdminProfile(results[0], results[1], results[2], results[3], results[4],results[5]);
                }
            }  catch (Exceptions.UsernameAlreadyTakenException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminShowProducts extends Action {
        private ArrayList<String[]> currentProducts;
        AdminShowProducts(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.adminShowProductsPattern, Constants.Actions.adminShowProductsCommand);
            this.currentProducts = currentProducts;
        }

        private void refreshCurrentProducts() {
            currentProducts.clear();
            currentProducts.addAll(adminController.manageAllProducts());
        }

        @Override
        public void execute(String command) {
            refreshCurrentProducts();
            printList(currentProducts);
            printSeparator();
        }
    }


    public static class AdminRemoveProductByID extends Action {
        private ArrayList<String[]> currentProducts;
        AdminRemoveProductByID(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.adminRemoveProductByIDPattern, Constants.Actions.adminRemoveProductByIDCommand);
            this.currentProducts = currentProducts;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, currentProducts);
                if (index != 0) {
                    adminController.removeProduct(currentProducts.get(index - 1)[0]);
                    System.out.println("product removed successfully");
                }
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminCreateDiscountCode extends Action {
        AdminCreateDiscountCode(String name) {
            super(name, Constants.Actions.adminCreateDiscountCodePattern, Constants.Actions.adminCreateDiscountCodeCommand);
        }

        @Override
        public void execute(String command) {
            String[] fields = new String[] {"discount code", "start date", "end date", "percentage", "maximum amount of use"};
            String[] fieldRegex = new String[] {Constants.argumentPattern, Constants.datePattern, Constants.datePattern, "^%?[0-99]\\.\\d+%?$", Constants.unsignedIntPattern};
            Form discountCodeForm = new Form(fields, fieldRegex);
            discountCodeForm.setupArrayForm(new String[]{"customer ID to add", "numberOfUses"}, new String[]{Constants.argumentPattern, Constants.unsignedIntPattern});
            if (discountCodeForm.takeInput() == 0) {
                String[] results = discountCodeForm.getResults();
                try {
                    adminController.createDiscountCode(results[0], Date.valueOf(results[1]), Date.valueOf(results[2]),
                            Double.valueOf(results[3]), Integer.parseInt(results[4]), discountCodeForm.getListResult());
                    System.out.println("discount code created successfully");
                } catch (Exceptions.ExistingDiscountCodeException | Exceptions.InvalidAccountsForDiscount e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class AdminShowDiscountCodes extends Action {
        private ArrayList<String> discountCodes;
        AdminShowDiscountCodes(String name, ArrayList<String> discountCodes) {
            super(name, Constants.Actions.adminShowDiscountCodesPattern, Constants.Actions.adminShowDiscountCodesCommand);
            this.discountCodes = discountCodes;
        }

        private void refreshDiscountCodes() {
            discountCodes.clear();
            discountCodes.addAll(adminController.viewDiscountCodes());
        }

        @Override
        public void execute(String command) {
            refreshDiscountCodes();
            int size = discountCodes.size();
            for (int i = 0; i < size; i++) {
                System.out.println(i + ". " + discountCodes.get(i));
            }
            printSeparator();
        }
    }

    public static class AdminViewDiscountCode extends Action {
        private ArrayList<String> discountCodes;
        AdminViewDiscountCode(String name, ArrayList<String> discountCodes) {
            super(name, Constants.Actions.adminViewDiscountCodePattern, Constants.Actions.adminViewDiscountCodeCommand);
            this.discountCodes = discountCodes;
        }

        private void showDiscountCode(String[] info) {
            System.out.println("1. discount code: " + info[0]);
            System.out.println("2. start date: " + info[1]);
            System.out.println("3. end date: " + info[2]);
            System.out.println("4. maximum amount of use: " + info[3]);
            System.out.println("5. discount percentage: " + info[4]);
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, discountCodes);
                if (index != 0) {
                    String[] info = adminController.viewDiscountCode(discountCodes.get(index - 1));
                    showDiscountCode(info);
                    System.out.println("people who have this discount:");
                    printList(adminController.peopleWhoHaveThisDiscount(discountCodes.get(index - 1)));
                }
            } catch (Exceptions.DiscountCodeException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminEditDiscountCode extends Action {
        private String[] editableFields;
        private ArrayList<String> discountCodes;
        AdminEditDiscountCode(String name, ArrayList<String> discountCodes, String[] editableFields) {
            super(name, Constants.Actions.adminEditDiscountCodePattern, Constants.Actions.adminEditDiscountCodeCommand);
            this.editableFields = editableFields;
            this.discountCodes = discountCodes;
        }

        private void showEditableFields() {
            for (int i = 0; i < editableFields.length; i++) {
                System.out.println((i + 1) + ". " + editableFields[i]);
            }
        }

        private int editField(int fieldIndex, String discountCode) {
            String response;
            while(true) {
                System.out.println("enter new info");
                response = View.getNextLineTrimmed();
                if (response.equalsIgnoreCase("back")) {return -1;}
                try {
                    adminController.editDiscountCode(discountCode, editableFields[fieldIndex], response);
                    return 0;
                } catch (Exceptions.DiscountCodeException | Exceptions.InvalidFormatException e) {
                    //wont happen.
                    System.out.println(e.getMessage());
                    return -1;
                } catch (Exceptions.SameAsPreviousValueException e) {
                    System.out.println("new value cant be the same as previous!");
                    continue;
                }
            }
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, discountCodes);
            if (index != 0) {
                while (true) {
                    showEditableFields();
                    System.out.println("enter the field to edit (index):");
                    String response = View.getNextLineTrimmed();
                    if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                        if (editField(Integer.parseInt(response), discountCodes.get(index - 1)) == -1) {
                            continue;
                        } else break;
                    } else if (response.equalsIgnoreCase("back")) {
                        break;
                    } else {
                        System.out.println("invalid entry");
                        continue;
                    }
                }
            }
            printSeparator();
        }
    }

    public static class AdminRemoveDiscountCode extends Action {
        private ArrayList<String> discountCodes;
        AdminRemoveDiscountCode(String name, ArrayList<String> discountCodes) {
            super(name, Constants.Actions.adminRemoveDiscountCodePattern, Constants.Actions.adminRemoveDiscountCodeCommand);
            this.discountCodes = discountCodes;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, discountCodes);
                if (index != 0) {
                    adminController.removeDiscountCode(discountCodes.get(index - 1));
                }
            } catch (Exceptions.DiscountCodeException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminShowPendingRequests extends Action {
        private ArrayList<String[]> pendingRequests;
        AdminShowPendingRequests(String name, ArrayList<String[]> pendingRequests) {
            super(name, Constants.Actions.adminShowRequestsPattern, Constants.Actions.adminShowRequestsCommand);
            this.pendingRequests = pendingRequests;
        }

        private void refreshPendingRequests() {
            pendingRequests.clear();
            pendingRequests.addAll(adminController.getPendingRequests());
        }

        @Override
        public void execute(String command) {
            refreshPendingRequests();
            System.out.println("pending requests:");
            printList(pendingRequests);
            System.out.println("you can see a request's detail by: details [index]");
            printSeparator();
        }
    }

    public static class AdminShowArchiveRequests extends Action {
        AdminShowArchiveRequests(String name) {
            super(name, Constants.Actions.adminShowArchiveRequestsPattern, Constants.Actions.adminShowArchiveRequestsCommand);
        }


        @Override
        public void execute(String command) {
            ArrayList<String[]> archives = adminController.getArchivedRequests();
            if (archives.isEmpty()) {
                System.out.println("you cant see archived requests");
            } else {
                System.out.println("archived requests:");
                printList(archives);
            }
            printSeparator();
        }
    }

    public static class AdminViewRequestDetail extends Action {
        private ArrayList<String[]> pendingRequests;
        AdminViewRequestDetail(String name, ArrayList<String[]> pendingRequests) {
            super(name, Constants.Actions.adminViewRequestDetailPattern, Constants.Actions.adminViewRequestDetailCommand);
            this.pendingRequests = pendingRequests;
        }

        private void acceptOrDeclineRequest(String requestID) {
            try {
                while (true) {
                    System.out.println("please accept/decline the request (accept or decline or \"back\" to go back):");
                    String response = View.getNextLineTrimmed();
                    if (response.equalsIgnoreCase("back")) {

                    } else if (response.matches("(accept|decline)")) {
                        if (response.equalsIgnoreCase("accept")) {
                            adminController.acceptRequest(requestID, true);
                        } else {
                            adminController.acceptRequest(requestID, false);
                        }
                    } else {
                        System.out.println("invalid entry.");
                    }
                }
            } catch (Exceptions.InvalidRequestIdException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, pendingRequests);
            if (index != 0) {
                try {
                    String requestID = pendingRequests.get(index - 1)[0];
                    printList(adminController.detailsOfRequest(requestID));
                    acceptOrDeclineRequest(requestID);
                } catch (Exceptions.InvalidRequestIdException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class AdminShowCategories extends Action {
        private ArrayList<String> currentCategories;
        AdminShowCategories(String name, ArrayList<String> currentCategories) {
            super(name, Constants.Actions.adminShowCategoriesPattern, Constants.Actions.adminShowCategoriesCommand);
            this.currentCategories = currentCategories;
        }

        private void refreshCurrentCategories() {
            currentCategories.clear();
            currentCategories.addAll(adminController.manageCategories());
        }

        @Override
        public void execute(String command) {
            refreshCurrentCategories();
            System.out.println("categories (category inheritance is not shown in this list): ");
            currentCategories.forEach(cc -> System.out.println(cc));
            printSeparator();
        }
    }

    public static class AdminEditCategory extends Action {
        private String[] editableFields;
        private ArrayList<String> currentCategories;
        AdminEditCategory(String name, String[] editableFields, ArrayList<String> currentCategories) {
            super(name, Constants.Actions.adminEditCategoryPattern, Constants.Actions.adminEditCategoryCommand);
            this.editableFields = editableFields;
            this.currentCategories = currentCategories;
        }

        private void showEditableFields() {
            for (int i = 0; i < editableFields.length; i++) {
                System.out.println((i + 1) + ". " + editableFields[i]);
            }
        }

        private int editField(int fieldIndex, String categoryName) {
            String response;
            while(true) {
                System.out.println("enter new info");
                response = View.getNextLineTrimmed();
                if (response.equalsIgnoreCase("back")) {return -1;}
                try {
                    adminController.editCategory(categoryName, editableFields[fieldIndex], response);
                    return 0;
                } catch (Exceptions.InvalidCategoryException | Exceptions.InvalidFieldException
                        | Exceptions.ExistedCategoryException | Exceptions.SubCategoryException e) {
                    System.out.println(e.getMessage());
                    return -1;
                } catch (Exceptions.SameAsPreviousValueException e) {
                    System.out.println("new value cant be the same as previous!");
                    continue;
                }
            }
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, currentCategories);
            if(index != 0) {
                while (true) {
                    showEditableFields();
                    System.out.println("enter the field to edit (index):");
                    String response = View.getNextLineTrimmed();
                    if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                        if (editField(Integer.parseInt(response), currentCategories.get(index - 1)) == -1) {
                            continue;
                        } else break;
                    } else if (response.equalsIgnoreCase("back")) {
                        break;
                    } else {
                        System.out.println("invalid entry");
                        continue;
                    }
                }
            }
            printSeparator();
        }
    }

    public static class AdminAddCategory extends Action {
        AdminAddCategory(String name) {
            super(name, Constants.Actions.adminAddCategoryPattern, Constants.Actions.adminAddCategoryCommand);
        }

        private ArrayList<String> getListResult(ArrayList<String[]> firstResult) {
            ArrayList<String> listResult = new ArrayList<>();
            for (String[] result : firstResult) {
                listResult.add(result[0]);
            }
            return listResult;
        }

        @Override
        public void execute(String command) {
            try {
                String categoryName = getGroup(command, 1);
                String[] fields = new String[] {"parent category (enter \"root\" for no parent)"};
                String[] regex = new String[] {".+"};
                Form categoryForm = new Form(fields, regex);
                categoryForm.setupArrayForm(new String[]{"special properties"}, new String[]{".+"});
                if(categoryForm.takeInput() == 0) {
                    String[] results = categoryForm.getResults();
                    ArrayList<String> specialProperties = getListResult(categoryForm.getListResult());
                    adminController.addCategory(categoryName, (results[0].equalsIgnoreCase("root")) ? "superCategory" : results[0], specialProperties);
                }
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminRemoveCategory extends Action {
        private ArrayList<String> currentCategories;
        AdminRemoveCategory(String name, ArrayList<String> currentCategories) {
            super(name, Constants.Actions.adminRemoveCategoryPattern, Constants.Actions.adminRemoveCategoryCommand);
            this.currentCategories = currentCategories;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command,currentCategories);
                if (index != 0) {
                    adminController.removeCategory(currentCategories.get(index - 1));
                }
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerShowSales extends Action {
        private ArrayList<String[]> currentSales;
        SellerShowSales(String name, ArrayList<String[]> currentSales) {
            super(name, Constants.Actions.sellerShowSalesPattern, Constants.Actions.sellerShowSalesCommand);
            this.currentSales = currentSales;
        }

        private void refreshCurrentSales() {
            currentSales.clear();
            currentSales.addAll(sellerController.viewSales());
        }

        @Override
        public void execute(String command) {
            refreshCurrentSales();
            System.out.println("seller sales:");
            printList(currentSales);
            printSeparator();
        }
    }

    public static class SellerViewSaleDetails extends Action {
        private ArrayList<String[]> currentSales;
        SellerViewSaleDetails(String name, ArrayList<String[]> currentSales) {
            super(name, Constants.Actions.sellerViewSaleDetailsPattern, Constants.Actions.sellerViewSaleDetailsCommand);
            this.currentSales = currentSales;
        }

        private void showSaleInfo(String[] info) {
            System.out.println("1. sale ID: " + info[0]);
            System.out.println("2. percentage: " + info[1]);
            System.out.println("3. store name: " + info[2]);
            System.out.println("4. start date: " + info[3]);
            System.out.println("5. end date: " + info[4]);
            System.out.println("6. number of products in sale: " + info[5]);
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, currentSales);
                if (index != 0) {
                showSaleInfo(sellerController.viewSaleWithId(currentSales.get(index - 1)[0]));
                }
            } catch (Exceptions.InvalidSaleIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerEditSale extends Action {
        private String[] editableFields;
        private ArrayList<String[]> currentSales;
        SellerEditSale(String name, String[] editableFields, ArrayList<String[]> currentSales) {
            super(name, Constants.Actions.sellerEditSalePattern, Constants.Actions.sellerEditSaleCommand);
            this.editableFields = editableFields;
            this.currentSales = currentSales;
        }

        private void showEditableFields() {
            for (int i = 0; i < editableFields.length; i++) {
                System.out.println((i + 1) + ". " + editableFields[i]);
            }
        }

        private int editField(int fieldIndex, String saleID) {
            String response;
            while(true) {
                System.out.println("enter new info");
                response = View.getNextLineTrimmed();
                if (response.equalsIgnoreCase("back")) {return -1;}
                try {
                    sellerController.editSale(saleID, editableFields[fieldIndex], response);
                    System.out.println("field edited successfully");
                    return 0;
                } catch (Exceptions.InvalidSaleIdException | Exceptions.InvalidFormatException |
                        Exceptions.InvalidDateException | Exceptions.InvalidFieldException e) {
                    System.out.println(e.getMessage());
                    return -1;
                } catch (Exceptions.SameAsPreviousValueException e) {
                    System.out.println("new value cant be the same as previous!");
                }
            }
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, currentSales);
            if (index != 0) {
                while (true) {
                    showEditableFields();
                    System.out.println("enter the field to edit (index):");
                    String response = View.getNextLineTrimmed();
                    if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                        if (editField(Integer.parseInt(response), currentSales.get(index - 1)[0]) == -1) {
                            continue;
                        } else {
                            break;
                        }
                    } else if (response.equalsIgnoreCase("back")) {
                        break;
                    } else {
                        System.out.println("invalid entry");
                        continue;
                    }
                }
            }
            printSeparator();
        }
    }

    public static class SellerAddSale extends Action {
        SellerAddSale(String name) {
            super(name, Constants.Actions.sellerAddSalePattern, Constants.Actions.sellerAddSaleCommand);
        }

        private ArrayList<String> getListResult(ArrayList<String[]> firstResult) {
            ArrayList<String> listResult = new ArrayList<>();
            for (String[] result : firstResult) {
                listResult.add(result[0]);
            }
            return listResult;
        }

        @Override
        public void execute(String command) {
            String[] fields = new String[] {"start date", "end date", "percentage", "maximum price reduction"};
            String[] fieldRegex = new String[] {Constants.datePattern, Constants.datePattern, "^%?[0-99]\\.\\d+%?$", Constants.doublePattern};
            Form saleForm = new Form(fields, fieldRegex);
            saleForm.setupArrayForm(new String[]{"product ID"}, new String[]{Constants.argumentPattern});
            if (saleForm.takeInput() == 0) {
                String[] results = saleForm.getResults();
                ArrayList<String> listResult = getListResult(saleForm.getListResult());
                try {
                    sellerController.addSale(Date.valueOf(results[0]), Date.valueOf(results[1]),
                            Double.valueOf(results[2]), Double.valueOf(results[3]), listResult);
                } catch (Exceptions.InvalidDateException | Exceptions.InvalidProductIdsForASeller e) {
                    System.out.println(e.getMessage());
                }
                printSeparator();
            }
        }
    }

    public static class SellerShowProducts extends Action {
        private ArrayList<String[]> sellerProducts;
        SellerShowProducts(String name, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerShowProductsPattern, Constants.Actions.sellerShowProductsCommand);
            this.sellerProducts = sellerProducts;
        }

        private void refreshProducts() {
            sellerProducts.clear();
            sellerProducts.addAll(sellerController.manageProducts());
        }

        @Override
        public void execute(String command) {
            refreshProducts();
            System.out.println("seller products:");
            printList(sellerProducts);
            printSeparator();
        }
    }

    public static class SellerViewProductDetails extends Action {
        private ArrayList<String[]> sellerProducts;
        SellerViewProductDetails(String name, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerViewProductDetailsPattern, Constants.Actions.sellerViewProductDetailsCommand);
            this.sellerProducts = sellerProducts;
        }

        private void printInfo(String[] info) {
            System.out.println("1. product ID: " + info[0]);
            System.out.println("2. product name: " + info[1]);
            System.out.println("3. product brand: " + info[2]);
            System.out.println("4. product description: " + info[3]);
            System.out.println("5. product average rating: " + info[4]);
        }


        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, sellerProducts);
                if (index != 0) {
                    mainController.showProduct(sellerProducts.get(index - 1)[0]);
                    printInfo(sellerController.viewProduct(sellerProducts.get(index - 1)[0]));
                }
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerViewProductBuyers extends Action {
        private ArrayList<String[]> sellerProducts;
        SellerViewProductBuyers(String name, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerViewProductBuyersPattern, Constants.Actions.sellerViewProductBuyersCommand);
            this.sellerProducts = sellerProducts;
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, sellerProducts);
                if (index != 0) {
                    System.out.println("buyers:");
                    sellerController.viewProductBuyers(sellerProducts.get(index - 1)[0]).forEach(by -> System.out.println(by));
                }
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerEditProduct extends Action {
        private String[] editableFields;
        private ArrayList<String[]> sellerProducts;
        SellerEditProduct(String name, String[] editableFields, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerEditProductPattern, Constants.Actions.sellerEditProductCommand);
            this.editableFields = editableFields;
            this.sellerProducts = sellerProducts;
        }

        private void showEditableFields() {
            for (int i = 0; i < editableFields.length; i++) {
                System.out.println((i + 1) + ". " + editableFields[i]);
            }
        }

        private int editField(int fieldIndex, String productID) {
            String response;
            while(true) {
                System.out.println("enter new info");
                response = View.getNextLineTrimmed();
                if (response.equalsIgnoreCase("back")) {return -1;}
                try {
                    sellerController.editProduct(productID, editableFields[fieldIndex], response);
                    System.out.println("field edited successfully");
                    return 0;
                } catch (Exceptions.InvalidProductIdException | Exceptions.ExistingProductException
                        | Exceptions.InvalidFieldException e) {
                    System.out.println(e.getMessage());
                    return -1;
                } catch (Exceptions.SameAsPreviousValueException e) {
                    System.out.println("new value cant be the same as previous!");
                }
            }
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, sellerProducts);
            if(index != 0) {
                while (true) {
                    showEditableFields();
                    System.out.println("enter the field to edit (index):");
                    String response = View.getNextLineTrimmed();
                    if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                        if (editField(Integer.parseInt(response), sellerProducts.get(index - 1)[0]) == -1) {
                            continue;
                        } else {
                            break;
                        }
                    } else if (response.equalsIgnoreCase("back")) {
                        break;
                    } else {
                        System.out.println("invalid entry");
                        continue;
                    }
                }
            }
            printSeparator();
        }
    }

    public static class SellerAddProduct extends Action {
        SellerAddProduct(String name) {
            super(name, Constants.Actions.sellerAddProductPattern, Constants.Actions.sellerAddProductCommand);
        }

        private ArrayList<String> getListResult(ArrayList<String[]> firstResult) {
            ArrayList<String> listResult = new ArrayList<>();
            for (String[] result : firstResult) {
                listResult.add(result[0]);
            }
            return listResult;
        }

        private void createNewProduct(String[] results) throws Exceptions.ExistingProductException, Exceptions.InvalidCategoryException {
            String[] fields = new String[] {"description", "category name", "price", "count"};
            String[] regex = new String[] { ".+", ".+", Constants.doublePattern, Constants.unsignedIntPattern};
            Form productSecondForm = new Form(fields, regex);
            productSecondForm.setupArrayForm(new String[]{"special properties"}, new String[]{".+"});
            if (productSecondForm.takeInput() == 0) {
                String[] secondResults = productSecondForm.getResults();
                ArrayList<String> specialProperties = getListResult(productSecondForm.getListResult());
                sellerController.addNewProduct(results[0], results[1], secondResults[0], secondResults[1],
                        specialProperties, Double.parseDouble(secondResults[2]), Integer.parseInt(secondResults[3]));
            }
        }

        private void createExistingProduct(String productID) throws Exceptions.InvalidProductIdException {
            String[] fields = new String[] {"price", "count"};
            String[] regex = new String[] {Constants.doublePattern, Constants.unsignedIntPattern};
            Form productSecondForm = new Form(fields, regex);
            if (productSecondForm.takeInput() == 0) {
                String[] results = productSecondForm.getResults();
                sellerController.addNewSubProductToAnExistingProduct(productID, Double.parseDouble(results[0]), Integer.parseInt(results[1]));
            }
        }

        @Override
        public void execute(String command) {
            try {
                String[] fields = new String[]{"name", "brand"};
                String[] fieldRegex = new String[]{Constants.argumentPattern, Constants.argumentPattern};
                Form productFirstForm = new Form(fields, fieldRegex);
                if (productFirstForm.takeInput() == 0) {
                    String[] results = productFirstForm.getResults();
                    String productID = sellerController.exist(results[0], results[1]);
                    if (productID == null) {
                        createNewProduct(results);
                    } else {
                        createExistingProduct(productID);
                    }
                }
            } catch (Exceptions.InvalidProductIdException | Exceptions.ExistingProductException | Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerRemoveProduct extends Action {
        private ArrayList<String[]> sellerProducts;
        SellerRemoveProduct(String name, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerRemoveProductPattern, Constants.Actions.sellerRemoveProductCommand);
            this.sellerProducts = sellerProducts;
        }

        @Override
        public void execute(String command) {
            int index = getIndex(command, sellerProducts);
            if (index != 0) {
                try {
                    sellerController.removeProduct(sellerProducts.get(index - 1)[0]);
                } catch (Exceptions.InvalidProductIdException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class ShoppingCartShowProducts extends Action {
        private ArrayList<String[]> currentProducts;
        ShoppingCartShowProducts(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.shoppingCartShowProductsPattern, Constants.Actions.shoppingCartShowProductsCommand);
            this.currentProducts = currentProducts;
        }

        private void refreshCurrentProducts() {
            currentProducts.clear();
            currentProducts.addAll(mainController.getProductsInCart());
        }

        @Override
        public void execute(String command) {
            refreshCurrentProducts();
            System.out.println("shopping cart products:");
            printList(currentProducts);
            printSeparator();
        }
    }

    public static class ShoppingCartViewProduct extends Action {
        private ArrayList<String[]> currentProducts;
        ShoppingCartViewProduct(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.shoppingCartViewProductPattern, Constants.Actions.shoppingCartViewProductCommand);
            this.currentProducts = currentProducts;
        }

        private void printInfo(int index) {
            String[] info = currentProducts.get(index);
            System.out.println("1. product ID: " + info[0]);
            System.out.println("2. product name: " + info[1]);
            System.out.println("3. product brand: " + info[2]);
            System.out.println("4. seller username: " + info[3]);
            System.out.println("5. seller store name: " + info[4]);
            System.out.println("6. product count: " + info[5]);
            System.out.println("7. " + info[6]);
        }

        @Override
        public void execute(String command) {
            try {
                int index = getIndex(command, currentProducts);
                if (index != 0) {
                    mainController.viewProductInCart(currentProducts.get(index - 1)[0]);

                }
            } catch (Exceptions.InvalidSubProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ShoppingCartIncreaseProductCount extends Action {
        private ArrayList<String[]> currentProducts;
        ShoppingCartIncreaseProductCount(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.shoppingCartIncreaseProductCountPattern, Constants.Actions.shoppingCartIncreaseProductCountCommand);
            this.currentProducts = currentProducts;
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            int index = Integer.parseInt(commandMatcher.group(1));
            int count;
            if (index > currentProducts.size()) {
                System.out.println("invalid index. please enter a number between 1 and " + currentProducts.size());
            } else {
                if (commandMatcher.groupCount() == 2) {
                    count = Integer.parseInt(commandMatcher.group(2));
                } else {
                    count = 1;
                }
                try {
                    mainController.increaseProductInCart(currentProducts.get(index - 1)[0], count);
                } catch (Exceptions.InvalidSubProductIdException | Exceptions.NotSubProductIdInTheCartException | Exceptions.UnAuthorizedAccountException e) {
                    System.out.println(e.getMessage());
                } catch (Exceptions.UnavailableProductException e) {
                    System.out.println("not enough product. try choosing another seller");
                }
            }
            printSeparator();
        }
    }

    public static class ShoppingCartDecreaseProductCount extends Action {
        private ArrayList<String[]> currentProducts;
        ShoppingCartDecreaseProductCount(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.shoppingCartDecreaseProductCountPattern, Constants.Actions.shoppingCartDecreaseProductCountCommand);
            this.currentProducts = currentProducts;
        }

        private int getIndexByID(String productID) {
            int size = currentProducts.size();
            for (int i = 0; i < size; i++) {
                if (currentProducts.get(i)[0].equals(productID)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            int index = Integer.parseInt(commandMatcher.group(1));
            int count;
            if (index > currentProducts.size()) {
                System.out.println("invalid index. please enter a number between 1 and " + currentProducts.size());
            } else {
                if (commandMatcher.groupCount() == 2) {
                    count = Integer.parseInt(commandMatcher.group(2));
                } else {
                    count = 1;
                }
                try {
                    mainController.decreaseProductInCart(currentProducts.get(index - 1)[0], count);
                } catch (Exceptions.InvalidSubProductIdException | Exceptions.NotSubProductIdInTheCartException | Exceptions.UnAuthorizedAccountException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class ShoppingCartShowTotalPrice extends Action {
        ShoppingCartShowTotalPrice(String name) {
            super(name, Constants.Actions.shoppingCartShowTotalPricePattern, Constants.Actions.shoppingCartShowTotalPriceCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("Total price (ofcourse ghabel nadare :p):" + customerController.getTotalPriceOfCart());
            printSeparator();
        }
    }

    public static class ShoppingCartPurchase extends Action {
        private Menu shoppingCartMenu;
        ShoppingCartPurchase(String name, Menu shoppingCartMenu) {
            super(name, Constants.Actions.shoppingCartPurchasePattern, Constants.Actions.shoppingCartPurchaseCommand);
            this.shoppingCartMenu = shoppingCartMenu;
        }

        @Override
        public void execute(String command) {
            if (mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                System.out.println("you have to be logged-in in order to be able to purchase. please login and try again.");
                Menu.getAccountMenu().run(shoppingCartMenu,shoppingCartMenu);
            }
            try {
                String[] fields = new String[] {"receiver name", "receiver address", "receiver phone",
                        "discount code (if you have any, enter \"-\" if you dont)"};
                String[] regex = new String[] {".+", ".+", Constants.unsignedIntPattern, Constants.argumentPattern};
                Form purchaseForm = new Form(fields, regex);
                if (purchaseForm.takeInput() == 0) {
                    String[] result = purchaseForm.getResults();
                    customerController.purchaseTheCart(result[0], result[1], result[2], result[3]);
                }
            } catch (Exceptions.InsufficientCreditException | Exceptions.NotAvailableSubProductsInCart
             | Exceptions.EmptyCartException | Exceptions.InvalidDiscountException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CustomerShowOrders extends Action {
        private ArrayList<String[]> currentOrderLogs;
        CustomerShowOrders(String name, ArrayList<String[]> currentOrderLogs) {
            super(name, Constants.Actions.customerShowOrdersPattern, Constants.Actions.customerShowOrdersCommand);
            this.currentOrderLogs = currentOrderLogs;
        }

        private void refreshCurrentOrderLogs() throws Exceptions.CustomerLoginException {
            currentOrderLogs.clear();
            currentOrderLogs.addAll(customerController.getOrders());
        }

        @Override
        public void execute(String command) {
            try {
                refreshCurrentOrderLogs();
                printList(currentOrderLogs);
            } catch (Exceptions.CustomerLoginException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CustomerViewOrder extends Action {
        private ArrayList<String[]> currentOrderLogs;
        CustomerViewOrder(String name, ArrayList<String[]> currentOrderLogs) {
            super(name, Constants.Actions.customerViewOrderPattern, Constants.Actions.customerViewOrderCommand);
            this.currentOrderLogs = currentOrderLogs;
        }

        private void printInfo(ArrayList<String[]> order) {
            String[] orderDetails = order.get(0);
            System.out.println("1. order ID: " + orderDetails[0]);
            System.out.println("2. customer username: " + orderDetails[1]);
            System.out.println("3. receiver name: " + orderDetails[2]);
            System.out.println("4. receiver phone: " + orderDetails[3]);
            System.out.println("5. receiver address: " + orderDetails[4]);
            System.out.println("6. date: " + orderDetails[5]);
            System.out.println("7. shipping status: " + orderDetails[6]);
            System.out.println("8. paid money: " + orderDetails[7]);
            System.out.println("9. total discount amount: " + orderDetails[8]);
            printSeparator();
            System.out.println("order products:");
            order.remove(0);
            printList(order);
        }


        @Override
        public void execute(String command) {
            int index = getIndex(command, currentOrderLogs);
            if (index != 0) {
                try {
                    printInfo(customerController.getOrderWithId(currentOrderLogs.get(index - 1)[0]));
                } catch (Exceptions.InvalidLogIdException | Exceptions.CustomerLoginException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class CustomerRateProduct extends Action {
        CustomerRateProduct(String name) {
            super(name, Constants.Actions.customerRateProductPattern, Constants.Actions.customerRateProductCommand);
        }

        @Override
        public void execute(String command) {
            String productID = getGroup(command, 1);
            int score = Integer.parseInt(getGroup(command, 2));
            try {
                customerController.rateProduct(productID, score);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            } catch (Exceptions.HaveNotBoughtException e) {
                System.out.println("you have not bought this product therefore you cant rate it.");
            }
            printSeparator();
        }
    }

    public static class Logout extends Action {
        Logout(String name) {
            super(name, Constants.Actions.logoutPattern, Constants.Actions.logoutCommand);
        }

        @Override
        public void execute(String command) {
            mainController.logout();
        }
    }
}
