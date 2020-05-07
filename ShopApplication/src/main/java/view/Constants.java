package view;

public class Constants {
    private static final String caseInsensitiveMode = "(?i)";
    private static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";

    public static class Menus {
        public static final String accountMenuCommand = "account menu";
        public static final String accountMenuPattern = caseInsensitiveMode + "^" + accountMenuCommand + "$";
        public static final String allProductsMenuCommand = "products";
        public static final String allProductsMenuPattern = caseInsensitiveMode + "^" + allProductsMenuCommand + "$";
        public static final String sortMenuCommand = "sorting";
        public static final String sortMenuPattern = caseInsensitiveMode + "^" + sortMenuCommand + "$";
        public static final String filterMenuCommand = "filtering";
        public static final String filterMenuPattern = caseInsensitiveMode + "^" + filterMenuCommand + "$";
        public static final String saleMenuCommand = "offs";
        public static final String saleMenuPattern = caseInsensitiveMode + "^" + saleMenuCommand + "$";
        public static final String productDetailMenuCommand = "show product [productID]";
        public static final String productDetailMenuPattern = caseInsensitiveMode + "^show product" + spacePattern + argumentPattern + "$";
        public static final String viewPersonalInfoCommand = "view personal info";
        public static final String viewPersonalInfoPattern = caseInsensitiveMode + "^"  + viewPersonalInfoCommand + "$";
        public static final String userManagingMenuCommand = "manage users";
        public static final String userManagingMenuPattern = caseInsensitiveMode + "^" + userManagingMenuCommand + "$";
        public static final String productManagingMenuCommand = "manage all products";
        public static final String productManagingMenuPattern = caseInsensitiveMode + "^" + productManagingMenuCommand + "$";
        public static final String discountCodesManagingMenuCommand = "view discount codes";
        public static final String discountCodesManagingMenuPattern = caseInsensitiveMode + "^" + discountCodesManagingMenuCommand + "$";
        public static final String requestManagingMenuCommand = "manage requests";
        public static final String requestManagingMenuPattern = caseInsensitiveMode + "^" + requestManagingMenuCommand + "$";
        public static final String categoryManagingMenuCommand = "manage categories";
        public static final String categoryManagingMenuPattern = caseInsensitiveMode + "^" + categoryManagingMenuCommand + "$";
        public static final String sellerProductManagingMenuCommand = "manage products";
        public static final String sellerProductManagingMenuPattern = caseInsensitiveMode + "^" + sellerProductManagingMenuCommand + "$";
        public static final String sellerSaleManagingMenuCommand = "view offs";
        public static final String sellerSaleManagingMenuPattern = caseInsensitiveMode + "^" + sellerSaleManagingMenuCommand + "$";
        public static final String shoppingCartMenuCommand = "view cart";
        public static final String shoppingCartMenuPattern = caseInsensitiveMode + "^" + shoppingCartMenuCommand + "$";
        public static final String customerOrderLogMenuCommand = "view orders";
        public static final String customerOrderLogMenuPattern = caseInsensitiveMode + "^" + customerOrderLogMenuCommand + "$";
//        public static final String
//        public static final String
//        public static final String
    }

    public static class Actions {
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
        //SaleMenu actions.
        public static final String showOffsCommand = "show offs";
        public static final String showOffsPattern = caseInsensitiveMode + "^" + showOffsCommand + "$";
        //account menus actions.
        public static final String editFieldCommand = "edit [field]";
        public static final String editFieldPattern = caseInsensitiveMode + "^edit " + argumentPattern + "$";
        public static final String showSellerCompanyInfoCommand = "view company information";
        public static final String showSellerCompanyInfoPattern = caseInsensitiveMode + "^" + showSellerCompanyInfoCommand + "$";
        public static final String showSellerSellHistoryCommand = "view sales history";
        public static final String showSellerSellHistoryPattern = caseInsensitiveMode + "^" + showSellerSellHistoryCommand + "$";
        public static final String showSellerCategoriesCommand = "show categories";
        public static final String showSellerCategoriesPattern = caseInsensitiveMode + "^" + showSellerCategoriesCommand + "$";
        public static final String showSellerBalanceCommand = "view balance";
        public static final String showSellerBalancePattern = caseInsensitiveMode + "^" + showSellerBalanceCommand + "$";
        public static final String showCustomerBalanceCommand = "view balance";
        public static final String showCustomerBalancePattern = caseInsensitiveMode + "^" + showCustomerBalanceCommand + "$";
        public static final String showCustomerDiscountCodesCommand = "view discount codes";
        public static final String showCustomerDiscountCodesPattern = caseInsensitiveMode + "^" + showCustomerDiscountCodesCommand +"$";
//        public static final String
    }
}
