package view;

import controller.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

//TODO: printSeparator();
//TODO: isInProducts --> int IndexByID
//TODO: getGroup()
//TODO: types start with capital.
//TODO: sout completion messages. ex: .... done successfully.
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
                    } catch (Exceptions.NotExistedUsernameException | Exceptions.WrongPasswordException e) {
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
                } catch (Exceptions.ExistedUsernameException | Exceptions.AdminRegisterException e) {
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
                } catch (Exceptions.ExistedUsernameException | Exceptions.AdminRegisterException e) {
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
                } catch (Exceptions.ExistedUsernameException | Exceptions.AdminRegisterException e) {
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
                } catch (Exceptions.ExistedUsernameException e) {
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
            int indicatorIndex = currentSort.lastIndexOf("creasing");
            indicatorIndex -= 3;
            return currentSort.substring(0, indicatorIndex);
        }

        private boolean isIncreasing() {
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
        ShowCategories(String name, ArrayList<String> categoryTree) {
            super(name, Constants.Actions.showCategoriesPattern, Constants.Actions.showCategoriesCommand);
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
                ArrayList<String[]> info = mainController.getSubCategoriesOfThisCategory(lastCategory);
                printList(info);
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class ChooseCategoryAction extends Action {
        private ArrayList<String> categoryTree;
        ChooseCategoryAction(String name, ArrayList<String> categoryTree) {
            super(name, Constants.Actions.chooseCategoryPattern, Constants.Actions.chooseCategoryCommand);
            this.categoryTree = categoryTree;
        }

        private boolean isCategoryNameValid(String categoryName) {
            try {
                ArrayList<String[]> subs;
                if (categoryTree.isEmpty()) {
                    subs = mainController.getSubCategoriesOfThisCategory("superCategory");
                } else {
                    subs = mainController.getSubCategoriesOfThisCategory(categoryTree.get(categoryTree.size() - 1));
                }
                for (String[] category : subs) {
                    if (category[1].equals(categoryName)) { return true;}
                }
                return false;
            } catch (Exceptions.InvalidCategoryException e) {
                //wont happen
                return false;
            }
        }

        @Override
        public void execute(String command) {
            String categoryName = getGroup(command, 1);
            if (isCategoryNameValid(categoryName)) {
                categoryTree.add(categoryName);
            } else {
                System.out.println(categoryName + " its not in the sub-categories of current category");
                return;
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
            System.out.println(currentSort);
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

        ShowCurrentFilters(String name, String[] currentFilters) {
            super(name, Constants.Actions.showCurrentFiltersPattern, Constants.Actions.showCurrentFiltersCommand);
            this.currentFilters = currentFilters;
        }

        @Override
        public void execute(String command) {
            Arrays.asList(currentFilters).forEach((f) -> System.out.println(f));
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
                    currentFilters[1] = "0.00";
                    break;
                case 3:
                    currentFilters[2] = "0.00";
                    break;
                case 4:
                    currentFilters[3] = null;
                    break;
                case 5:
                    currentFilters[4] = null;
                    break;
                case 6:
                    currentFilters[5] = null;
                    break;
                case 7:
                    currentFilters[6] = "0.00";
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

    //TODO: remember that filters and sorts are only for products.
    //TODO: holy guakamoly.
    public static class ShowOffs extends Action {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String[]> currentProducts;
        private ArrayList<String[]> currentOffs;

        public ShowOffs(String name, StringBuilder currentSort, String[] currentFilters, ArrayList<String[]> currentProducts, ArrayList<String[]> currentOffs) {
            super(name, Constants.Actions.showOffsPattern, Constants.Actions.showOffsCommand);
            this.currentSort = currentSort;
            this.currentFilters = currentFilters;
            this.currentProducts = currentProducts;
            this.currentOffs = currentOffs;
        }

        private ArrayList<String[]> appendSaleInfo(String[] sale, ArrayList<String[]> salesProducts) {
          ArrayList<String[]> result = new ArrayList<>();
            for (String[] salesProduct : salesProducts) {
                String[] extendedProduct = Arrays.copyOf(salesProduct, salesProduct.length + sale.length);
                System.arraycopy(sale, 0, extendedProduct, salesProduct.length, sale.length);
                result.add(extendedProduct);
            }
            return result;
        }

        private void sortProducts() {

        }

        //if adding filter and sort for sales then should add an additional currentOffs.clear() and currentOffs.getOffs(...) codes.
        @Override
        public void execute(String command) {
            currentProducts.clear();
            try {
                for (String[] off : currentOffs) {
                    ArrayList<String[]> offsProducts = mainController.getProductsInSale(off[0]);
                    currentProducts.addAll(appendSaleInfo(off, offsProducts));
                }
                sortProducts();
              //  showProducts();
            } catch (Exceptions.InvalidSaleIdException e) {
                System.out.println(e.getMessage());
            }
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
        ShowSellerSellHistory(String name) {
            super(name, Constants.Actions.showSellerSellHistoryPattern, Constants.Actions.showSellerSellHistoryCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("sell history:");
            printList(sellerController.getAllSellLogs());
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
        ProductDetailMenu(String name) {
            super(name, Constants.Actions.productDetailMenuPattern, Constants.Actions.productDetailMenuCommand);
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = this.getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                mainController.showProduct(productID);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }

            Menu.getProductDetailMenu().runByProductID(productID);
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

    //TODO: waiting for shayan
    public static class ShowCurrentSeller extends Action {
        private StringBuilder subProductID;
        ShowCurrentSeller(String name, StringBuilder subProductID) {
            super(name, Constants.Actions.showCurrentSellerPattern, Constants.Actions.showCurrentSellerCommand);
        }

        @Override
        public void execute(String command) {
            try {
                System.out.println("current seller is: " + mainController.getSubProductByIndex(subProductID)[1]);
            } catch (Exceptions.InvalidSubProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    //TODO: last one
    public static class CompareProductByID extends Action {
        private StringBuilder productID;
        CompareProductByID(String name, StringBuilder productID) {
            super(name, Constants.Actions.compareProductByIDPattern, Constants.Actions.compareProductByIDCommand);
            this.productID = productID;
        }


        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String otherProductID = commandMatcher.group(1);
            try {
                //what is the return type?
                mainController.compare(productID.toString(), otherProductID);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }
            //show infos.
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

    public static class AdminViewUser extends Action {
        AdminViewUser(String name) {
            super(name, Constants.Actions.adminViewUserPattern, Constants.Actions.adminViewUserCommand);
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
        public void execute(String username) {
            try {
                String[] info = adminController.viewUsername(username);
                showPersonalInfo(info);
                printSeparator();
            } catch (Exceptions.NotExistedUsernameException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static class AdminDeleteUser extends Action {
        AdminDeleteUser(String name) {
            super(name, Constants.Actions.adminDeleteUserPattern, Constants.Actions.adminDeleteUserCommand);
        }

        @Override
        public void execute(String username) {
            try {
                adminController.deleteUsername(username);
                System.out.println("user removed successfully");
            } catch (Exceptions.NotExistedUsernameException e) {
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
                    mainController.creatAccount(Constants.adminUserType, results[0], results[1], results[2], results[3], results[4],results[5], 0.00, null);
                }
            }  catch (Exceptions.ExistedUsernameException | Exceptions.AdminRegisterException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminRemoveProductByID extends Action {
        AdminRemoveProductByID(String name) {
            super(name, Constants.Actions.adminRemoveProductByIDPattern, Constants.Actions.adminRemoveProductByIDCommand);
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                adminController.removeProduct(productID);
                System.out.println("product removed successfully");
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
            if (discountCodeForm.takeInput() == 0) {
                String[] results = discountCodeForm.getResults();
                try {
                    adminController.createDiscountCode(results[0], Date.valueOf(results[1]), Date.valueOf(results[2]),
                            Double.valueOf(results[3]), Integer.parseInt(results[4]));
                    System.out.println("discount code created successfully");
                } catch (Exceptions.ExistingDiscountCodeException e) {
                    System.out.println(e.getMessage());
                }
            }
            printSeparator();
        }
    }

    public static class AdminViewDiscountCode extends Action {
        AdminViewDiscountCode(String name) {
            super(name, Constants.Actions.adminViewDiscountCodePattern, Constants.Actions.adminViewDiscountCodeCommand);
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
            Matcher commandMatcher = getMatcherReady(command);
            String discountCode = commandMatcher.group(1);
            try {
                String[] info = adminController.viewDiscountCode(discountCode);
                showDiscountCode(info);
            } catch (Exceptions.DiscountCodeException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminEditDiscountCode extends Action {
        private String[] editableFields;
        AdminEditDiscountCode(String name, String[] editableFields) {
            super(name, Constants.Actions.adminEditDiscountCodePattern, Constants.Actions.adminEditDiscountCodeCommand);
            this.editableFields = editableFields;
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
            Matcher commandMatcher = getMatcherReady(command);
            String discountCode = commandMatcher.group(1);
            while (true) {
                showEditableFields();
                System.out.println("enter the field to edit (index):");
                String response = View.getNextLineTrimmed();
                if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                    if (editField(Integer.parseInt(response), discountCode) == -1) {continue;}
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

    public static class AdminRemoveDiscountCode extends Action {
        AdminRemoveDiscountCode(String name) {
            super(name, Constants.Actions.adminRemoveDiscountCodePattern, Constants.Actions.adminRemoveDiscountCodeCommand);
        }

        @Override
        public void execute(String command) {
            try {
                String discountCodeID = getGroup(command, 1);
                adminController.removeDiscountCode(discountCodeID);
            } catch (Exceptions.DiscountCodeException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminShowRequests extends Action {
        AdminShowRequests(String name) {
            super(name, Constants.Actions.adminShowRequestsPattern, Constants.Actions.adminShowRequestsCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("request IDs:");
            adminController.manageRequests().forEach(ID -> System.out.println(ID));
            System.out.println("you can see a request's detail by: details [requestID]");
            printSeparator();
        }
    }

    public static class AdminViewRequestDetail extends Action {
        AdminViewRequestDetail(String name) {
            super(name, Constants.Actions.adminViewRequestDetailPattern, Constants.Actions.adminViewRequestDetailCommand);
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
            try {
                String requestID = getGroup(command, 1);
                printList(adminController.detailsOfRequest(requestID));
                acceptOrDeclineRequest(requestID);
            } catch (Exceptions.InvalidRequestIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminShowCategories extends Action {
        AdminShowCategories(String name) {
            super(name, Constants.Actions.adminShowCategoriesPattern, Constants.Actions.adminShowCategoriesCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("categories (category inheritance is not shown in this list): ");
            adminController.manageCategories().forEach(c -> System.out.println(c));
            printSeparator();
        }
    }

    public static class AdminEditCategory extends Action {
        private String[] editableFields;
        AdminEditCategory(String name, String[] editableFields) {
            super(name, Constants.Actions.adminEditCategoryPattern, Constants.Actions.adminEditCategoryCommand);
            this.editableFields = editableFields;
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
            Matcher commandMatcher = getMatcherReady(command);
            String categoryName = commandMatcher.group(1);
            while (true) {
                showEditableFields();
                System.out.println("enter the field to edit (index):");
                String response = View.getNextLineTrimmed();
                if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                    if (editField(Integer.parseInt(response), categoryName) == -1) {continue;}
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

    public static class AdminAddCategory extends Action {
        AdminAddCategory(String name) {
            super(name, Constants.Actions.adminAddCategoryPattern, Constants.Actions.adminAddCategoryCommand);
        }

        @Override
        public void execute(String command) {
            try {
                String categoryName = getGroup(command, 1);
                String[] fields = new String[] {"parent category (enter \"root\" for no parent)"};
                String[] regex = new String[] {".+"};
                Form categoryForm = new Form(fields, regex);
                categoryForm.setupArrayForm("special properties", ".+");
                if(categoryForm.takeInput() == 0) {
                    String[] results = categoryForm.getResults();
                    ArrayList<String> specialProperties = categoryForm.getListResult();
                    adminController.addCategory(categoryName, (results[0].equalsIgnoreCase("root")) ? "superCategory" : results[0], specialProperties);
                }
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class AdminRemoveCategory extends Action {
        AdminRemoveCategory(String name) {
            super(name, Constants.Actions.adminRemoveCategoryPattern, Constants.Actions.adminRemoveCategoryCommand);
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String categoryName = commandMatcher.group(1);
            try {
                adminController.removeCategory(categoryName);
            } catch (Exceptions.InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    //TODO: waiting for shayan
    public static class SellerShowSales extends Action {
        SellerShowSales(String name) {
            super(name, Constants.Actions.sellerShowSalesPattern, Constants.Actions.sellerShowSalesCommand);
        }

        //TODO: shayan will fix it.
        @Override
        public void execute(String command) {
            ArrayList<String[]> sales = sellerController.viewSales();
            printList(sales);
            printSeparator();
        }
    }

    public static class SellerViewSaleDetails extends Action {
        SellerViewSaleDetails(String name) {
            super(name, Constants.Actions.sellerViewSaleDetailsPattern, Constants.Actions.sellerViewSaleDetailsCommand);
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
            Matcher commandMatcher = getMatcherReady(command);
            String saleID = commandMatcher.group(1);
            try {
                String[] info = sellerController.viewSaleWithId(saleID);
                showSaleInfo(info);
            } catch (Exceptions.InvalidSaleIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerEditSale extends Action {
        private String[] editableFields;
        SellerEditSale(String name, String[] editableFields) {
            super(name, Constants.Actions.sellerEditSalePattern, Constants.Actions.sellerEditSaleCommand);
            this.editableFields = editableFields;
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
            Matcher commandMatcher = getMatcherReady(command);
            String saleID = commandMatcher.group(1);
            while (true) {
                showEditableFields();
                System.out.println("enter the field to edit (index):");
                String response = View.getNextLineTrimmed();
                if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                    if (editField(Integer.parseInt(response), saleID) == -1) {continue;}
                    else {break;}
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

    public static class SellerAddSale extends Action {
        SellerAddSale(String name) {
            super(name, Constants.Actions.sellerAddSalePattern, Constants.Actions.sellerAddSaleCommand);
        }

        @Override
        public void execute(String command) {
            String[] fields = new String[] {"start date", "end date", "percentage", "maximum price reduction"};
            String[] fieldRegex = new String[] {Constants.datePattern, Constants.datePattern, "^%?[0-99]\\.\\d+%?$", Constants.doublePattern};
            Form saleForm = new Form(fields, fieldRegex);
            saleForm.setupArrayForm("product ID", Constants.argumentPattern);
            if (saleForm.takeInput() == 0) {
                String[] results = saleForm.getResults();
                try {
                    sellerController.addSale(Date.valueOf(results[0]), Date.valueOf(results[1]),
                            Double.valueOf(results[2]), Double.valueOf(results[3]), saleForm.getListResult());
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

        @Override
        public void execute(String command) {
            if (sellerProducts.isEmpty()) {
                sellerProducts.addAll(sellerController.manageProducts());
            }
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


        private boolean isInProducts(String productID) {
            for (String[] product : sellerProducts) {
                if (product[0].equals(productID)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                if ( ! isInProducts(productID)) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                    mainController.showProduct(productID);
                    String[] info = sellerController.viewProduct(productID);
                    printInfo(info);
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

        private boolean isInProducts(String productID) {
            for (String[] product : sellerProducts) {
                if (product[0].equals(productID)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                if (!isInProducts(productID)) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                    ArrayList<String> buyers = sellerController.viewProductBuyers(productID);
                    System.out.println("buyers:");
                    buyers.forEach(b -> System.out.println(b));
                }
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class SellerEditProduct extends Action {
        private String[] editableFields;
        SellerEditProduct(String name, String[] editableFields) {
            super(name, Constants.Actions.sellerEditProductPattern, Constants.Actions.sellerEditProductCommand);
            this.editableFields = editableFields;
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
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            while (true) {
                showEditableFields();
                System.out.println("enter the field to edit (index):");
                String response = View.getNextLineTrimmed();
                if (response.matches("\\d+") && Integer.parseInt(response) <= editableFields.length) {
                    if (editField(Integer.parseInt(response), productID) == -1) {continue;}
                    else {break;}
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

    //TODO: wtf should we do?
    public static class SellerAddProduct extends Action {
        SellerAddProduct(String name) {
            super(name, Constants.Actions.sellerAddProductPattern, Constants.Actions.sellerAddProductCommand);
        }

        @Override
        public void execute(String command) {
            String[] fields = new String[] {"name", "brand"};
            String[] fieldRegex = new String[] {Constants.argumentPattern, Constants.argumentPattern};
            Form productFirstForm = new Form(fields, fieldRegex);
        }
    }

    public static class SellerRemoveProduct extends Action {
        private ArrayList<String[]> sellerProducts;
        SellerRemoveProduct(String name, ArrayList<String[]> sellerProducts) {
            super(name, Constants.Actions.sellerRemoveProductPattern, Constants.Actions.sellerRemoveProductCommand);
            this.sellerProducts = sellerProducts;
        }

        private boolean isInProducts(String productID) {
            for (String[] product : sellerProducts) {
                if (product[0].equals(productID)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                if (!isInProducts(productID)) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                    sellerController.removeProduct(productID);
                }
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CustomerCartShowProducts extends Action {
        private ArrayList<String[]> currentProducts;
        CustomerCartShowProducts(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.customerCartShowProductsPattern, Constants.Actions.customerCartShowProductsCommand);
            this.currentProducts = currentProducts;
        }

        private void refreshCurrentProducts() {
            currentProducts.clear();
            currentProducts.addAll(customerController.getProductsInCart());
        }

        @Override
        public void execute(String command) {
            refreshCurrentProducts();
            printList(currentProducts);
            printSeparator();
        }
    }

    public static class CustomerCartViewProduct extends Action {
        private ArrayList<String[]> currentProducts;
        CustomerCartViewProduct(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.customerCartViewProductPattern, Constants.Actions.customerCartViewProductCommand);
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

        //wtf is last field.
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
            Matcher commandMatcher = getMatcherReady(command);
            String productID = commandMatcher.group(1);
            try {
                int index = getIndexByID(productID);
                if (index == -1) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                    customerController.viewProductInCart(productID);
                    printInfo(index);
                }
            } catch (Exceptions.InvalidSubProductIdException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CustomerCartIncreaseProductCount extends Action {
        private ArrayList<String[]> currentProducts;
        CustomerCartIncreaseProductCount(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.customerCartIncreaseProductCountPattern, Constants.Actions.customerCartIncreaseProductCountCommand);
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
            String productID = commandMatcher.group(1);
            int count;
            if (commandMatcher.groupCount() == 2) {
                count = Integer.parseInt(commandMatcher.group(2));
            } else { count = 1; }
            try {
                int index = getIndexByID(productID);
                if (index == -1) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                   customerController.increaseProductInCart(productID, count);
                }
            } catch (Exceptions.InvalidSubProductIdException | Exceptions.NotSubProductIdInTheCartException e) {
                System.out.println(e.getMessage());
            } catch (Exceptions.UnavailableProductException e) {
                System.out.println("not enough product. try choosing another seller");
            }
            printSeparator();
        }
    }

    public static class CustomerCartDecreaseProductCount extends Action {
        private ArrayList<String[]> currentProducts;
        CustomerCartDecreaseProductCount(String name, ArrayList<String[]> currentProducts) {
            super(name, Constants.Actions.customerCartDecreaseProductCountPattern, Constants.Actions.customerCartDecreaseProductCountCommand);
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
            String productID = commandMatcher.group(1);
            int count;
            if (commandMatcher.groupCount() == 2) {
                count = Integer.parseInt(commandMatcher.group(2));
            } else { count = 1; }
            try {
                int index = getIndexByID(productID);
                if (index == -1) {
                    System.out.println("please enter a valid ID. you can see the list of available IDs with show \"products\"");
                } else {
                    customerController.decreaseProductInCart(productID, count);
                }
            } catch (Exceptions.InvalidSubProductIdException | Exceptions.NotSubProductIdInTheCartException e) {
                System.out.println(e.getMessage());
            }
            printSeparator();
        }
    }

    public static class CustomerCartShowTotalPrice extends Action {
        CustomerCartShowTotalPrice(String name) {
            super(name, Constants.Actions.customerCartShowTotalPricePattern, Constants.Actions.customerCartShowTotalPriceCommand);
        }

        @Override
        public void execute(String command) {
            System.out.println("Total price (ofcourse ghabel nadare :p):" + customerController.getTotalPriceOfCart());
            printSeparator();
        }
    }

    //TODO: what about anonymous
    public static class CustomerCartPurchase extends Action {
        CustomerCartPurchase(String name) {
            super(name, Constants.Actions.customerCartPurchasePattern, Constants.Actions.customerCartPurchaseCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerShowOrders extends Action {
        private ArrayList<String[]> currentOrderLogs;
        CustomerShowOrders(String name, ArrayList<String[]> currentOrderLogs) {
            super(name, Constants.Actions.customerShowOrdersPattern, Constants.Actions.customerShowOrdersCommand);
            this.currentOrderLogs = currentOrderLogs;
        }

        private void refreshCurrentOrderLogs() {
            currentOrderLogs.clear();
            currentOrderLogs.addAll(customerController.getProductsInCart());
        }

        @Override
        public void execute(String command) {
            refreshCurrentOrderLogs();
            printList(currentOrderLogs);
            printSeparator();
        }
    }

    public static class CustomerViewOrder extends Action {
        private ArrayList<String[]> currentOrderLogs;
        CustomerViewOrder(String name, ArrayList<String[]> currentOrderLogs) {
            super(name, Constants.Actions.customerViewOrderPattern, Constants.Actions.customerViewOrderCommand);
            this.currentOrderLogs = currentOrderLogs;
        }

        private int getIndexByID(String productID) {
            int size = currentOrderLogs.size();
            for (int i = 0; i < size; i++) {
                if (currentOrderLogs.get(i)[0].equals(productID)) {
                    return i;
                }
            }
            return -1;
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
            printList((ArrayList)order.subList(1, order.size()));
        }


        @Override
        public void execute(String command) {
            String orderID = getGroup(command, 1);
            try {
                int index = getIndexByID(orderID);
                if (index == -1) {
                    System.out.println("please enter a valid ID. you can see the list of valid IDs by \"show orders\".");
                } else {
                    ArrayList<String[]> info = customerController.getOrderWithId(orderID);
                    printInfo(info);
                }
            } catch (Exceptions.InvalidLogIdException e) {
                System.out.println(e.getMessage());
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
}
