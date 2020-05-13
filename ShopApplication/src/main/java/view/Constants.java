package view;

import model.Category;

class Constants {
    private static final String caseInsensitiveMode = "(?i)";
    private static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    private static final String unsignedIntPattern = "(\\d+)";
    static final String anonymousUserType = "anonymous";
    static final String adminUserType = "admin";
    static final String customerUserType = "customer";
    static final String sellerUserType = "seller";

     static class Menus {
        static final String accountMenuCommand = "account menu";
        static final String accountMenuPattern = caseInsensitiveMode + "^" + accountMenuCommand + "$";
        static final String allProductsMenuCommand = "products";
        static final String allProductsMenuPattern = caseInsensitiveMode + "^" + allProductsMenuCommand + "$";
        static final String sortMenuCommand = "sorting";
        static final String sortMenuPattern = caseInsensitiveMode + "^" + sortMenuCommand + "$";
        static final String filterMenuCommand = "filtering";
        static final String filterMenuPattern = caseInsensitiveMode + "^" + filterMenuCommand + "$";
        static final String saleMenuCommand = "offs";
        static final String saleMenuPattern = caseInsensitiveMode + "^" + saleMenuCommand + "$";
        static final String viewPersonalInfoCommand = "view personal info";
        static final String viewPersonalInfoPattern = caseInsensitiveMode + "^"  + viewPersonalInfoCommand + "$";
        static final String userManagingMenuCommand = "manage users";
        static final String userManagingMenuPattern = caseInsensitiveMode + "^" + userManagingMenuCommand + "$";
        static final String productManagingMenuCommand = "manage all products";
        static final String productManagingMenuPattern = caseInsensitiveMode + "^" + productManagingMenuCommand + "$";
        static final String discountCodesManagingMenuCommand = "view discount codes";
        static final String discountCodesManagingMenuPattern = caseInsensitiveMode + "^" + discountCodesManagingMenuCommand + "$";
        static final String requestManagingMenuCommand = "manage requests";
        static final String requestManagingMenuPattern = caseInsensitiveMode + "^" + requestManagingMenuCommand + "$";
        static final String categoryManagingMenuCommand = "manage categories";
        static final String categoryManagingMenuPattern = caseInsensitiveMode + "^" + categoryManagingMenuCommand + "$";
        static final String sellerProductManagingMenuCommand = "manage products";
        static final String sellerProductManagingMenuPattern = caseInsensitiveMode + "^" + sellerProductManagingMenuCommand + "$";
        static final String sellerSaleManagingMenuCommand = "view offs";
        static final String sellerSaleManagingMenuPattern = caseInsensitiveMode + "^" + sellerSaleManagingMenuCommand + "$";
        static final String shoppingCartMenuCommand = "view cart";
        static final String shoppingCartMenuPattern = caseInsensitiveMode + "^" + shoppingCartMenuCommand + "$";
        static final String customerOrderLogMenuCommand = "view orders";
        static final String customerOrderLogMenuPattern = caseInsensitiveMode + "^" + customerOrderLogMenuCommand + "$";
        static final String productReviewMenuCommand = "comments";
        static final String productReviewMenuPattern = caseInsensitiveMode + "^" + productReviewMenuCommand + "$";
    }

    static class Actions {
        static final String backCommand = "back";
        static final String backPattern = caseInsensitiveMode + "^" + backCommand + "$";
        static final String exitCommand = "exit";
        static final String exitPattern = caseInsensitiveMode + "^" + exitCommand + "$";
        static final String loginCommand = "login [username]";
        static final String loginPattern = caseInsensitiveMode + "^login" + spacePattern + argumentPattern + "$";
        static final String registerCommand = "create account [type] [username]";
        static final String registerPattern =
                caseInsensitiveMode + "^create account" + spacePattern + argumentPattern + spacePattern + argumentPattern + "$";
        //AllProductsMenu actions.
        static final String showProductsCommand = "show products [-all]";
        static final String showProductsPattern = caseInsensitiveMode + "^show products" + spacePattern + "(-all)?$";
        static final String showCategoriesCommand = "view categories";
        static final String showCategoriesPattern = caseInsensitiveMode + "^" + showCategoriesCommand + "$";
        static final String chooseCategoryCommand = "choose category [availableCategory]";
        static final String chooseCategoryPattern = caseInsensitiveMode + "^choose category" + spacePattern + argumentPattern + "$";
        static final String revertCategoryCommand = "revert category [-numberOfReverts]"; //you can set the number of times to go back. 1 by default.
        static final String revertCategoryPattern = caseInsensitiveMode + "^revert category" + spacePattern + argumentPattern + "?$";
        static final String productDetailMenuCommand = "show product [productID]";
        static final String productDetailMenuPattern = caseInsensitiveMode + "^show product" + spacePattern + argumentPattern + "$";
        //SortMenu actions.
        static final String showAvailableSortsCommand = "show available sorts";
        static final String showAvailableSortsPattern = caseInsensitiveMode + "^" + showAvailableSortsCommand + "$";
        static final String sortCommand = "sort [anAvailableSorts]";
        static final String sortPattern = caseInsensitiveMode + "^sort" + spacePattern + argumentPattern + "$";
        static final String showCurrentSortCommand = "current sort";
        static final String showCurrentSortPattern = caseInsensitiveMode + "^" + showCurrentSortCommand + "$";
        static final String disableSortCommand = "disable sort";
        static final String disableSortPattern = caseInsensitiveMode + "^" + disableSortCommand + "$";
        //FilterMenu actions.
        static final String showAvailableFiltersCommand = "show available filters";
        static final String showAvailableFiltersPattern = caseInsensitiveMode + "^" + showAvailableFiltersCommand + "$";
        static final String filterCommand = "filter [anAvailableFilter]";
        static final String filterPattern = caseInsensitiveMode + "^filter" + spacePattern + argumentPattern + "$";
        static final String showCurrentFiltersCommand = "current filters";
        static final String showCurrentFiltersPattern = caseInsensitiveMode + "^" + showCurrentFiltersCommand + "$";
        static final String disableFilterCommand = "disable filter [aFilter] ...";
        static final String disableFilterPattern = caseInsensitiveMode + "^disable filter" + "(?:" + spacePattern + argumentPattern + ")+$";
        //SaleMenu actions.
        static final String showOffsCommand = "show offs";
        static final String showOffsPattern = caseInsensitiveMode + "^" + showOffsCommand + "$";
        //account menus actions.
        static final String editFieldCommand = "edit [field]";
        static final String editFieldPattern = caseInsensitiveMode + "^edit " + argumentPattern + "$";
        static final String showSellerCompanyInfoCommand = "view company information";
        static final String showSellerCompanyInfoPattern = caseInsensitiveMode + "^" + showSellerCompanyInfoCommand + "$";
        static final String showSellerSellHistoryCommand = "view sales history";
        static final String showSellerSellHistoryPattern = caseInsensitiveMode + "^" + showSellerSellHistoryCommand + "$";
        static final String showSellerCategoriesCommand = "show categories";
        static final String showSellerCategoriesPattern = caseInsensitiveMode + "^" + showSellerCategoriesCommand + "$";
        static final String showSellerBalanceCommand = "view balance";
        static final String showSellerBalancePattern = caseInsensitiveMode + "^" + showSellerBalanceCommand + "$";
        static final String showCustomerBalanceCommand = "view balance";
        static final String showCustomerBalancePattern = caseInsensitiveMode + "^" + showCustomerBalanceCommand + "$";
        static final String showCustomerDiscountCodesCommand = "view discount codes";
        static final String showCustomerDiscountCodesPattern = caseInsensitiveMode + "^" + showCustomerDiscountCodesCommand +"$";
        static final String digestProductCommand = "digest";
        static final String digestProductPattern = caseInsensitiveMode + "" + digestProductCommand + "$";
        //TODO: wtf should we do with sub products
        static final String showSubProductsCommand = "show sub products";
        static final String showSubProductsPattern = caseInsensitiveMode + "^" + showSubProductsCommand + "$";
        static final String addToCartCommand = "add to cart";
        static final String addToCartPattern = caseInsensitiveMode + "^" + addToCartCommand + "$";
        static final String productCurrentSellerCommand = "show current seller";
        static final String productCurrentSellerPattern = caseInsensitiveMode + "^" + productCurrentSellerCommand + "$";
        //different form doc. cuz of index choosing.
        static final String selectSellerCommand = "select seller";
        static final String selectSellerPattern = caseInsensitiveMode + "^" + selectSellerCommand + "$";
        static final String compareProductByIDCommand = "compare [productID]";
        static final String compareProductByIDPattern = caseInsensitiveMode + "^compare " + argumentPattern + "$";
        static final String addReviewCommand = "add comment";
        static final String addReviewPattern = caseInsensitiveMode + "^" + addReviewCommand + "$";
        static final String showReviewsCommand = "show comments";
        static final String showReviewsPattern = caseInsensitiveMode + "^" + showReviewsCommand + "$";
        //admin account menu actions
        static final String adminViewUserCommand = "view user [userIndex]";
        static final String adminViewUserPattern = caseInsensitiveMode + "^view user " + unsignedIntPattern + "$";
        static final String adminDeleteUserCommand = "delete user [userIndex]";
        static final String adminDeleteUserPattern = caseInsensitiveMode + "delete user " + unsignedIntPattern + "$";
        static final String adminCreateAdminCommand = "create manager profile";
        static final String adminCreateAdminPattern = caseInsensitiveMode + "^" + adminCreateAdminCommand + "$";
        static final String adminRemoveProductByIDCommand = "remove [productID]";
        static final String adminRemoveProductByIDPattern = caseInsensitiveMode + "^remove " + argumentPattern + "$";
        static final String adminCreateDiscountCodeCommand = "create discount code";
        static final String adminCreateDiscountCodePattern = caseInsensitiveMode + adminCreateDiscountCodeCommand + "$";
        static final String adminViewDiscountCodeCommand = "view discount code [discountCode]";
        static final String adminViewDiscountCodePattern = caseInsensitiveMode + "^view discount code " + argumentPattern + "$";
        static final String adminEditDiscountCodeCommand = "edit discount code [discountCode]";
        static final String adminEditDiscountCodePattern = caseInsensitiveMode + "^edit discount code " + argumentPattern + "$";
        static final String adminRemoveDiscountCodeCommand = "remove discount code [discountCode]";
        static final String adminRemoveDiscountCodePattern = caseInsensitiveMode + "^remove discount code " + argumentPattern + "$";
        //minor modification cuz of showing dilemma.
        static final String adminViewRequestDetailCommand = "details [requestIndex]";
        static final String adminViewRequestDetailPattern = caseInsensitiveMode + "^details " + argumentPattern + "$";
        static final String adminShowRequestsCommand = "view requests";
        static final String adminShowRequestsPattern = caseInsensitiveMode + "^" + adminShowRequestsCommand + "$";
        static final String adminShowCategoriesCommand = "show categories";
        static final String adminShowCategoriesPattern = caseInsensitiveMode + "^" + adminShowCategoriesCommand + "$";
        //minor modification cuz of showing dilemma.
        static final String adminEditCategoryCommand = "edit [categoryIndex]";
        static final String adminEditCategoryPattern = caseInsensitiveMode + "^edit " + argumentPattern + "$";
        static final String adminAddCategoryCommand = "add [categoryName]";
        static final String adminAddCategoryPattern = caseInsensitiveMode + "^add " + argumentPattern + "$";
        static final String adminRemoveCategoryCommand = "remove [categoryIndex]";
        static final String adminRemoveCategoryPattern = caseInsensitiveMode + "^remove " + argumentPattern +"$";
        //seller account menu actions.
        static final String sellerShowSalesCommand = "view sales";
        static final String sellerShowSalesPattern = caseInsensitiveMode + "^" + sellerShowSalesCommand + "$";
        static final String sellerViewSaleDetailsCommand = "view [saleIndex]";
        static final String sellerViewSaleDetailsPattern = caseInsensitiveMode + "^view " + argumentPattern + "$";
        static final String sellerEditSaleCommand = "edit [saleIndex]";
        static final String sellerEditSalePattern = caseInsensitiveMode + "^edit " + argumentPattern + "$";
        static final String sellerAddSaleCommand = "add off";
        static final String sellerAddSalePattern = caseInsensitiveMode + sellerAddSaleCommand + "$";
        static final String sellerShowProductsCommand = "view products";
        static final String sellerShowProductsPattern = caseInsensitiveMode + "^" + sellerShowProductsCommand + "$";
        static final String sellerViewProductDetailsCommand = "view [productIndex]";
        static final String sellerViewProductDetailsPattern = caseInsensitiveMode + "^view " + argumentPattern + "$";
        static final String sellerViewProductBuyersCommand = "view buyers [productIndex]";
        static final String sellerViewProductBuyersPattern = caseInsensitiveMode + "^view buyers " + argumentPattern + "$";
        static final String sellerEditProductCommand = "edit [productIndex]";
        static final String sellerEditProductPattern = caseInsensitiveMode + "^edit " + argumentPattern + "$";
        static final String sellerAddProductCommand = "add product";
        static final String sellerAddProductPattern = caseInsensitiveMode + "^" + sellerAddProductCommand + "$";
        static final String sellerRemoveProductCommand = "remove product [productIndex]";
        static final String sellerRemoveProductPattern = caseInsensitiveMode + "^remove product " + argumentPattern + "$";
        //customer account menu actions
        static final String customerCartShowProductsCommand = "show products";
        static final String customerCartShowProductsPattern = caseInsensitiveMode + "^" + customerCartShowProductsCommand + "$";
        static final String customerCartViewProductCommand = "view [productIndex]";
        static final String customerCartViewProductPattern = caseInsensitiveMode + "^view " + argumentPattern + "$";
        static final String customerCartIncreaseProductCountCommand = "increase [productIndex] [-count]";
        static final String customerCartIncreaseProductCountPattern = caseInsensitiveMode +
                "^increase " + argumentPattern + "(?: " + unsignedIntPattern + ")$";
        static final String customerCartDecreaseProductCountCommand = "increase [productIndex] [-count]";
        static final String customerCartDecreaseProductCountPattern = caseInsensitiveMode +
                "^decrease " + argumentPattern + "(?: " + unsignedIntPattern + ")$";
        static final String customerCartShowTotalPriceCommand = "show total price";
        static final String customerCartShowTotalPricePattern = caseInsensitiveMode + customerCartShowTotalPriceCommand + "$";
        static final String customerCartPurchaseCommand = "purchase";
        static final String customerCartPurchasePattern = caseInsensitiveMode + "^" + customerCartPurchaseCommand + "$";
        static final String customerShowOrdersCommand = "show orders";
        static final String customerShowOrdersPattern = caseInsensitiveMode + "^" + customerShowOrdersCommand + "$";
        static final String customerViewOrderCommand = "show order [orderIndex]";
        static final String customerViewOrderPattern = caseInsensitiveMode + "^show order " + argumentPattern + "$";
        static final String customerRateProductCommand = "rate [productIndex] [1-5]";
        static final String customerRateProductPattern = caseInsensitiveMode + "^rate " + argumentPattern + "([1-5])$";
    }
}
