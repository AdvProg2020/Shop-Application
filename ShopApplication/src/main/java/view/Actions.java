package view;

import controller.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: be actions controller haro moarefi kon.
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

    public static class AccountMenuBackAction extends Action {
        private Menu previousMenu;
        AccountMenuBackAction(String name, Menu previousMenu) {
            super(name, Constants.Actions.backPattern, Constants.Actions.backCommand);
            this.previousMenu = previousMenu;
        }

        @Override
        public void execute(String command) {
            previousMenu.run();
        }
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

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class RegisterAction extends Action {
        RegisterAction(String name) {
            super(name, Constants.Actions.registerPattern, Constants.Actions.registerCommand);
        }

        //TODO: implement.
        @Override
        public void execute(String command) {
            String type ;
            String username;

        }
    }

    public static class ShowProductsAction extends Action {
        private ArrayList<String> categoryTree;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String> currentProducts;
        ShowProductsAction(String name, ArrayList<String> categoryTree, String[] currentFilters, StringBuilder currentSort, ArrayList<String> currentProducts) {
            super(name, Constants.Actions.showProductsPattern, Constants.Actions.showProductsCommand);
            this.categoryTree = categoryTree;
            this.currentFilters = currentFilters;
            this.currentSort = currentSort;
            this.currentProducts = currentProducts;
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCategories extends Action {
        private ArrayList<String> categoryTree;
        ShowCategories(String name, ArrayList<String> categoryTree) {
            super(name, Constants.Actions.showCategoriesPattern, Constants.Actions.showCategoriesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }


    public static class ChooseCategoryAction extends Action {
        private ArrayList<String> categoryTree;
        ChooseCategoryAction(String name, ArrayList<String> categoryTree) {
            super(name, Constants.Actions.chooseCategoryPattern, Constants.Actions.chooseCategoryCommand);
            this.categoryTree = categoryTree;
        }

        //TODO: imp
        @Override
        public void execute(String command) {

        }
    }

    public static class RevertCategoryAction extends Action {
        RevertCategoryAction(String name) {
            super(name, Constants.Actions.revertCategoryPattern, Constants.Actions.revertCategoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowAvailableSorts extends Action {
        private String[] availableSorts;

        ShowAvailableSorts(String name, String[] availableSorts) {
            super(name, Constants.Actions.showAvailableSortsPattern, Constants.Actions.showAvailableSortsCommand);
            this.availableSorts = availableSorts;
        }

        @Override
        public void execute(String command) {
            Arrays.asList(availableSorts).forEach((s) -> System.out.println(s));
        }
    }

    public static class SortAction extends Action {
        private StringBuilder currentSort;

        SortAction(String name, StringBuilder currentSort) {
            super(name, Constants.Actions.sortPattern, Constants.Actions.sortCommand);
            this.currentSort = currentSort;
        }

        //TODO: imp
        @Override
        public void execute(String command) {
            currentSort.delete(0, currentSort.length());
            currentSort.append("shit");
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
            currentSort.delete(0, currentSort.length());
            currentSort.trimToSize();
        }
    }

    public static class ShowAvailableFilters extends Action {
        private String[] availableFilters;

        ShowAvailableFilters(String name, String[] availableFilters) {
            super(name, Constants.Actions.showAvailableFiltersPattern, Constants.Actions.showAvailableFiltersCommand);
            this.availableFilters = availableFilters;
        }

        @Override
        public void execute(String command) {
            Arrays.asList(availableFilters).forEach((f) -> System.out.println(f));
        }
    }

    public static class FilterAction extends Action {
        private String[] currentFilters;

        FilterAction(String name, String[] currentFilters) {
            super(name, Constants.Actions.filterPattern, Constants.Actions.filterCommand);
            this.currentFilters = currentFilters;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

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

        DisableFilter(String name, String[] currentFilters) {
            super(name, Constants.Actions.disableFilterPattern, Constants.Actions.disableFilterCommand);
            this.currentFilters = currentFilters;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    //TODO: remember that filters and sorts are only for products.
    //TODO: filter and sort for sales.
    public static class ShowOffs extends Action {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String> currentProducts;
        private ArrayList<String> currentOffs;

        public ShowOffs(String name, StringBuilder currentSort, String[] currentFilters, ArrayList<String> currentProducts, ArrayList<String> currentOffs) {
            super(name, Constants.Actions.showOffsPattern, Constants.Actions.showOffsCommand);
            this.currentSort = currentSort;
            this.currentFilters = currentFilters;
            this.currentProducts = currentProducts;
            this.currentOffs = currentOffs;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class EditField extends Action {
        private ArrayList<String> editableFields;

        //TODO: check new info (for example for password)
        EditField(String name, ArrayList<String> editableFields) {
            super(name, Constants.Actions.editFieldPattern, Constants.Actions.editFieldCommand);
            this.editableFields = editableFields;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowSellerCategories extends Action {
        ShowSellerCategories(String name) {
            super(name, Constants.Actions.showSellerCategoriesPattern, Constants.Actions.showSellerCategoriesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowSellerCompanyInfo extends Action {
        ShowSellerCompanyInfo(String name) {
            super(name, Constants.Actions.showSellerCompanyInfoPattern, Constants.Actions.showSellerCompanyInfoCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowSellerSellHistory extends Action {
        ShowSellerSellHistory(String name) {
            super(name, Constants.Actions.showSellerSellHistoryPattern, Constants.Actions.showSellerSellHistoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowSellerBalance extends Action {
        ShowSellerBalance(String name) {
            super(name, Constants.Actions.showSellerBalancePattern, Constants.Actions.showSellerBalanceCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCustomerBalance extends Action {
        ShowCustomerBalance(String name) {
            super(name, Constants.Actions.showCustomerBalancePattern, Constants.Actions.showCustomerBalanceCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCustomerDiscountCodes extends Action {
        ShowCustomerDiscountCodes(String name) {
            super(name, Constants.Actions.showCustomerDiscountCodesPattern, Constants.Actions.showCustomerDiscountCodesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

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
            //checks if the ID is valid or not
            try {
                mainController.showProduct(productID);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }

            //if valid, runs productMenu with given productID.
            Menu.getProductDetailMenu().runByProductID(productID);
        }
    }

    public static class DigestProduct extends Action {
        DigestProduct(String name) {
            super(name ,Constants.Actions.digestProductPattern, Constants.Actions.digestProductCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {
            try {
                String[] productInfo = mainController.digest(command);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }

            //if ID is valid, show the info.
        }
    }
}
