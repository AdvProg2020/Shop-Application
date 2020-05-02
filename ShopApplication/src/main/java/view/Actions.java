package view;

import java.util.ArrayList;

public class Actions {
    public static class BackAction extends Action {
        private Menu parent;
        BackAction(String name, Menu parent) {
            super(name, Constants.ActionCommandAndPattern.backPattern, Constants.ActionCommandAndPattern.backCommand);
            this.parent = parent;
        }

        @Override
        public void execute(String command) {
            parent.run();
        }
    }

    public static class ExitAction extends Action {
        ExitAction(String name) {
            super(name, Constants.ActionCommandAndPattern.exitPattern, Constants.ActionCommandAndPattern.exitCommand);
        }

        @Override
        public void execute(String command) {
            System.exit(1);
        }
    }

    public static  class LoginAction extends Action {
        LoginAction(String name) {
            super(name, Constants.ActionCommandAndPattern.loginPattern, Constants.ActionCommandAndPattern.loginCommand);
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class RegisterAction extends Action {
        RegisterAction(String name) {
            super(name, Constants.ActionCommandAndPattern.registerPattern, Constants.ActionCommandAndPattern.registerCommand);
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowProductsAction extends Action {
        private ArrayList<String> categoryTree;
        ShowProductsAction(String name, ArrayList<String> categoryTree) {
            super(name, Constants.ActionCommandAndPattern.showProductsPattern, Constants.ActionCommandAndPattern.showProductsCommand);
            this.categoryTree = categoryTree;
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCategories extends Action {
        private ArrayList<String> categoryTree;
        ShowCategories(String name, ArrayList<String> categoryTree) {
            super(name, Constants.ActionCommandAndPattern.showCategoriesPattern, Constants.ActionCommandAndPattern.showCategoriesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowProductByID extends Action {
        ShowProductByID(String name) {
            super(name, Constants.ActionCommandAndPattern.showProductByIDPattern, Constants.ActionCommandAndPattern.showProductByIDCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ChooseCategoryAction extends Action {
        ChooseCategoryAction(String name) {
            super(name, Constants.ActionCommandAndPattern.chooseCategoryPattern, Constants.ActionCommandAndPattern.chooseCategoryCommand);
        }

        //TODO: imp
        @Override
        public void execute(String command) {

        }
    }

    public static class RevertCategoryAction extends Action {
        RevertCategoryAction(String name) {
            super(name, Constants.ActionCommandAndPattern.revertCategoryPattern, Constants.ActionCommandAndPattern.revertCategoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowAvailableSorts extends Action {
        private ArrayList<String> availableSorts;

        ShowAvailableSorts(String name, ArrayList<String> availableSorts) {
            super(name, Constants.ActionCommandAndPattern.showAvailableSortsPattern, Constants.ActionCommandAndPattern.showAvailableSortsCommand);
            this.availableSorts = availableSorts;
        }

        @Override
        public void execute(String command) {
            availableSorts.forEach((s) -> System.out.println(s));
        }
    }

    public static class SortAction extends Action {
        private StringBuilder currentSort;

        SortAction(String name, StringBuilder currentSort) {
            super(name, Constants.ActionCommandAndPattern.sortPattern, Constants.ActionCommandAndPattern.sortCommand);
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
            super(name, Constants.ActionCommandAndPattern.showCurrentSortPattern, Constants.ActionCommandAndPattern.showCurrentSortCommand);
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
            super(name, Constants.ActionCommandAndPattern.disableSortPattern, Constants.ActionCommandAndPattern.disableSortCommand);
            this.currentSort = currentSort;
        }

        @Override
        public void execute(String command) {
            currentSort.delete(0, currentSort.length());
            currentSort.trimToSize();
        }
    }

    public static class ShowAvailableFilters extends Action {
        private ArrayList<String> availableFilters;

        ShowAvailableFilters(String name, ArrayList<String> availableFilters) {
            super(name, Constants.ActionCommandAndPattern.showAvailableFiltersPattern, Constants.ActionCommandAndPattern.showAvailableFiltersCommand);
            this.availableFilters = availableFilters;
        }

        @Override
        public void execute(String command) {
            availableFilters.forEach((f) -> System.out.println(f));
        }
    }

    public static class FilterAction extends Action {
        private ArrayList<String> currentFilters;

        FilterAction(String name, ArrayList<String> currentFilters) {
            super(name, Constants.ActionCommandAndPattern.filterPattern, Constants.ActionCommandAndPattern.filterCommand);
            this.currentFilters = currentFilters;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCurrentFilters extends Action {
        private ArrayList<String> currentFilters;

        ShowCurrentFilters(String name, ArrayList<String> currentFilters) {
            super(name, Constants.ActionCommandAndPattern.showCurrentFiltersPattern, Constants.ActionCommandAndPattern.showCurrentFiltersCommand);
            this.currentFilters = currentFilters;
        }

        @Override
        public void execute(String command) {
            currentFilters.forEach((f) -> System.out.println(f));
        }
    }

    public static class DisableFilter extends Action {
        private ArrayList<String> currentFilters;

        DisableFilter(String name, ArrayList<String> currentFilters) {
            super(name, Constants.ActionCommandAndPattern.disableFilterPattern, Constants.ActionCommandAndPattern.disableFilterCommand);
            this.currentFilters = currentFilters;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }
}
