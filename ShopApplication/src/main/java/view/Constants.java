package view;

public class Constants {
    private static final String caseInsensitiveMode = "(?i)";
    private static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    private static final String
    private static final String

    public static class MenuCommandAndPattern {
        public static final String accountMenuCommand = "account menu";
        public static final String accountMenuPattern = caseInsensitiveMode + "^" + accountMenuCommand + "$";
        public static final String allProductsMenuCommand = "products";
        public static final String allProductsMenuPattern = caseInsensitiveMode + "^" + allProductsMenuCommand + "$";
        public static final String sortMenuCommand = "sorting";
        public static final String sortMenuPattern = caseInsensitiveMode + "^" + sortMenuCommand + "$";
        public static final String filterMenuCommand = "filtering";
        public static final String filterMenuPattern = caseInsensitiveMode + "^" + filterMenuCommand + "$";
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
    }

    public static class ActionCommandAndPattern {
        public static final String backCommand = "back";
        public static final String backPattern = caseInsensitiveMode + "^" + backCommand + "$";
        public static final String exitCommand = "exit";
        public static final String exitPattern = caseInsensitiveMode + "^" + exitCommand + "$";
        public static final String loginCommand = "login [username]";
        public static final String loginPattern = caseInsensitiveMode + "^login" + spacePattern + argumentPattern + "$";
        public static final String registerCommand = "create account [type] [username]";
        public static final String registerPattern =
                caseInsensitiveMode + "^create account" + spacePattern + argumentPattern + spacePattern + argumentPattern + "$";
        //AllProductsMenu actions.
        public static final String showProductsCommand = "show products [-all]";
        public static final String showProductsPattern = caseInsensitiveMode + "^show products" + spacePattern + "(-all)?$";
        public static final String showCategoriesCommand = "view categories [-all]";
        public static final String showCategoriesPattern = caseInsensitiveMode + "^view categories" + spacePattern + "(-all)?$";
        public static final String showProductByIDCommand = "show product [productID]";
        public static final String showProductByIDPattern = caseInsensitiveMode + "^show product" + spacePattern + argumentPattern + "$";
        public static final String chooseCategoryCommand = "choose category [availableCategory]";
        public static final String chooseCategoryPattern = caseInsensitiveMode + "^choose category" + spacePattern + argumentPattern + "$";
        public static final String revertCategoryCommand = "revert category [-numberOfReverts]"; //you can set the number of times to go back. 1 by default.
        public static final String revertCategoryPattern = caseInsensitiveMode + "^revert category" + spacePattern + argumentPattern + "?$";
        //SortMenu actions.
        public static final String showAvailableSortsCommand = "show available sorts";
        public static final String showAvailableSortsPattern = caseInsensitiveMode + "^" + showAvailableSortsCommand + "$";
        public static final String sortCommand = "sort [anAvailableSorts]";
        public static final String sortPattern = caseInsensitiveMode + "^sort" + spacePattern + argumentPattern + "$";
        public static final String showCurrentSortCommand = "current sort";
        public static final String showCurrentSortPattern = caseInsensitiveMode + "^" + showCurrentSortCommand + "$";
        public static final String disableSortCommand = "disable sort";
        public static final String disableSortPattern = caseInsensitiveMode + "^" + disableSortCommand + "$";
        //FilterMenu actions.
        public static final String showAvailableFiltersCommand = "show available filters";
        public static final String showAvailableFiltersPattern = caseInsensitiveMode + "^" + showAvailableFiltersCommand + "$";
        public static final String filterCommand = "filter [anAvailableFilter]";
        public static final String filterPattern = caseInsensitiveMode + "^filter" + spacePattern + argumentPattern + "$";
        public static final String showCurrentFiltersCommand = "current filters";
        public static final String showCurrentFiltersPattern = caseInsensitiveMode + "^" + showCurrentFiltersCommand + "$";
        public static final String disableFilterCommand = "disable filter [aFilter] ...";
        public static final String disableFilterPattern = caseInsensitiveMode + "^disable filter" + "(?:" + spacePattern + argumentPattern + ")+$";
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
    }
}
