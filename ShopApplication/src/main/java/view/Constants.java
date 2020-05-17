package view;

final class Constants {
    //TODO: space pattern ro bargardon.
    static final String caseInsensitiveMode = "(?i)";
    static final String argumentPattern = "(\\S+)";
    static final String spacePattern = "\\s+";
    static final String unsignedIntPattern = "+?(\\d+)";
    static final String doublePattern = "(\\d+\\.\\d+)";
    static final String datePattern = "\\d{2}[/-]\\d{2}[/-]\\d{2}";
    static final String anonymousUserType = "Anonymous";
    static final String adminUserType = "Admin";
    static final String customerUserType = "Customer";
    static final String sellerUserType = "Seller";
    static final String[] types = new String[] {anonymousUserType, customerUserType, sellerUserType, adminUserType};

    static int getTypeByIndex(String type) {
        for (int i = 0; i < types.length; i++) {
            if (type.equalsIgnoreCase(types[i])) {
                return i;
            }
        }
        return -1;
    }

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
        static final String logoutCommand = "logout";
        static final String logoutPattern = caseInsensitiveMode + "^" + loginCommand + "$";
        static final String registerCommand = "create account [type] [username]";
        static final String registerPattern =
                caseInsensitiveMode + "^create account" + spacePattern + argumentPattern + spacePattern + argumentPattern + "$";
        //AllProductsMenu actions.
        static final String showProductsCommand = "show products [-all]";
        static final String showProductsPattern = caseInsensitiveMode + "^show products(?:" + spacePattern + "(-all))?$";
        static final String showCategoriesCommand = "view categories";
        static final String showCategoriesPattern = caseInsensitiveMode + "^" + showCategoriesCommand + "$";
        static final String chooseCategoryCommand = "choose category [index]";
        static final String chooseCategoryPattern = caseInsensitiveMode + "^choose category" + spacePattern + unsignedIntPattern + "$";
        static final String revertCategoryCommand = "revert category [-numberOfReverts]"; //you can set the number of times to go back. 1 by default.
        static final String revertCategoryPattern = caseInsensitiveMode + "^revert category(?:" + spacePattern + unsignedIntPattern + ")?$";
        static final String productDetailMenuCommand = "show product [productIndex]";
        static final String productDetailMenuPattern = caseInsensitiveMode + "^show product" + spacePattern + unsignedIntPattern + "$";
        //SortMenu actions.
        static final String sortCommand = "select sort";
        static final String sortPattern = caseInsensitiveMode + "^" + sortCommand + "$";
        static final String showCurrentSortCommand = "current sort";
        static final String showCurrentSortPattern = caseInsensitiveMode + "^" + showCurrentSortCommand + "$";
        static final String disableSortCommand = "disable sort";
        static final String disableSortPattern = caseInsensitiveMode + "^" + disableSortCommand + "$";
        //FilterMenu actions.
        static final String filterCommand = "choose filtering";
        static final String filterPattern = caseInsensitiveMode + "^" + filterCommand + "$";
        static final String showCurrentFiltersCommand = "current filters";
        static final String showCurrentFiltersPattern = caseInsensitiveMode + "^" + showCurrentFiltersCommand + "$";
        static final String disableFilterCommand = "disable filter [index]";
        static final String disableFilterPattern = caseInsensitiveMode + "^disable filter" + spacePattern + unsignedIntPattern +"$";
        //SaleMenu actions.
        static final String showOffsCommand = "show offs";
        static final String showOffsPattern = caseInsensitiveMode + "^" + showOffsCommand + "$";
        static final String showInSaleProductsCommand = "show in sale products";
        static final String showInSaleProductsPattern = caseInsensitiveMode + "^" + showInSaleProductsCommand + "$";
        //account menus actions.
        static final String viewPersonalInfoCommand = "view info";
        static final String viewPersonalInfoPattern = caseInsensitiveMode + "^" + viewPersonalInfoCommand + "$";
        static final String editFieldCommand = "edit field";
        static final String editFieldPattern = caseInsensitiveMode + "^" + editFieldCommand + "$";
        static final String showSellerCompanyInfoCommand = "view company information";
        static final String showSellerCompanyInfoPattern = caseInsensitiveMode + "^" + showSellerCompanyInfoCommand + "$";
        static final String showSellerSellHistoryCommand = "view sales history";
        static final String showSellerSellHistoryPattern = caseInsensitiveMode + "^" + showSellerSellHistoryCommand + "$";
        static final String showSingleSellLogCommand = "view sell history [sellHistoryIndex]";
        static final String showSingleSellLogPattern = caseInsensitiveMode + "^view sell history" + spacePattern + unsignedIntPattern + "$";
        static final String showSellerBalanceCommand = "view balance";
        static final String showSellerBalancePattern = caseInsensitiveMode + "^" + showSellerBalanceCommand + "$";
        static final String showCustomerBalanceCommand = "view balance";
        static final String showCustomerBalancePattern = caseInsensitiveMode + "^" + showCustomerBalanceCommand + "$";
        static final String showCustomerDiscountCodesCommand = "view discount codes";
        static final String showCustomerDiscountCodesPattern = caseInsensitiveMode + "^" + showCustomerDiscountCodesCommand +"$";
        static final String digestProductCommand = "digest";
        static final String digestProductPattern = caseInsensitiveMode + "" + digestProductCommand + "$";
        static final String showSubProductsCommand = "show sub products";
        static final String showSubProductsPattern = caseInsensitiveMode + "^" + showSubProductsCommand + "$";
        static final String addToCartCommand = "add to cart";
        static final String addToCartPattern = caseInsensitiveMode + "^" + addToCartCommand + "$";
        static final String productCurrentSellerCommand = "show current seller";
        static final String productCurrentSellerPattern = caseInsensitiveMode + "^" + productCurrentSellerCommand + "$";
        //different form doc. cuz of index choosing.
        static final String selectSellerCommand = "select seller";
        static final String selectSellerPattern = caseInsensitiveMode + "^" + selectSellerCommand + "$";
        static final String showCurrentSellerCommand = "show current seller";
        static final String showCurrentSellerPattern = caseInsensitiveMode + "^" + showCurrentSellerCommand + "$";
        static final String compareProductByIDCommand = "compare [productID]";
        static final String compareProductByIDPattern = caseInsensitiveMode + "^compare" + spacePattern + argumentPattern + "$";
        static final String addReviewCommand = "add comment";
        static final String addReviewPattern = caseInsensitiveMode + "^" + addReviewCommand + "$";
        static final String showReviewsCommand = "show comments";
        static final String showReviewsPattern = caseInsensitiveMode + "^" + showReviewsCommand + "$";
        //admin account menu actions
        static final String adminViewAllUsersCommand = "show users";
        static final String adminViewAllUsersPattern = caseInsensitiveMode + "^" + adminViewAllUsersCommand + "$";
        static final String adminViewUserCommand = "view user [index]";
        static final String adminViewUserPattern = caseInsensitiveMode + "^view user" + spacePattern + unsignedIntPattern + "$";
        static final String adminDeleteUserCommand = "delete user [index]";
        static final String adminDeleteUserPattern = caseInsensitiveMode + "^delete user " + spacePattern + unsignedIntPattern + "$";
        static final String adminCreateAdminCommand = "create manager profile";
        static final String adminCreateAdminPattern = caseInsensitiveMode + "^" + adminCreateAdminCommand + "$";
        static final String adminShowProductsCommand = "show products";
        static final String adminShowProductsPattern = caseInsensitiveMode + "^" +  adminShowProductsCommand + "$";
        static final String adminRemoveProductByIDCommand = "remove [index]";
        static final String adminRemoveProductByIDPattern = caseInsensitiveMode + "^remove" + spacePattern + unsignedIntPattern + "$";
        static final String adminCreateDiscountCodeCommand = "create discount code";
        static final String adminCreateDiscountCodePattern = caseInsensitiveMode + adminCreateDiscountCodeCommand + "$";
        static final String adminShowDiscountCodesCommand = "show discount codes";
        static final String adminShowDiscountCodesPattern = caseInsensitiveMode + "^" + adminShowDiscountCodesCommand + "$";
        static final String adminViewDiscountCodeCommand = "view discount code [index]";
        static final String adminViewDiscountCodePattern = caseInsensitiveMode + "^view discount code" + spacePattern + unsignedIntPattern  + "$";
        static final String adminEditDiscountCodeCommand = "edit discount code [index]";
        static final String adminEditDiscountCodePattern = caseInsensitiveMode + "^edit discount code" + spacePattern + unsignedIntPattern + "$";
        static final String adminRemoveDiscountCodeCommand = "remove discount code [index]";
        static final String adminRemoveDiscountCodePattern = caseInsensitiveMode + "^remove discount code" + spacePattern + unsignedIntPattern + "$";
        //minor modification cuz of showing dilemma.
        static final String adminViewRequestDetailCommand = "details [index]";
        static final String adminViewRequestDetailPattern = caseInsensitiveMode + "^details" + spacePattern + argumentPattern + "$";
        static final String adminShowRequestsCommand = "view requests";
        static final String adminShowRequestsPattern = caseInsensitiveMode + "^" + adminShowRequestsCommand + "$";
        static final String adminShowCategoriesCommand = "show categories";
        static final String adminShowCategoriesPattern = caseInsensitiveMode + "^" + adminShowCategoriesCommand + "$";
        //minor modification cuz of showing dilemma.
        static final String adminEditCategoryCommand = "edit [index]";
        static final String adminEditCategoryPattern = caseInsensitiveMode + "^edit" + spacePattern + argumentPattern + "$";
        static final String adminAddCategoryCommand = "add [categoryName]";
        static final String adminAddCategoryPattern = caseInsensitiveMode + "^add" + spacePattern + argumentPattern + "$";
        static final String adminRemoveCategoryCommand = "remove [index]";
        static final String adminRemoveCategoryPattern = caseInsensitiveMode + "^remove" + spacePattern + argumentPattern +"$";
        //seller account menu actions.
        static final String sellerShowSalesCommand = "view sales";
        static final String sellerShowSalesPattern = caseInsensitiveMode + "^" + sellerShowSalesCommand + "$";
        static final String sellerViewSaleDetailsCommand = "view [index]";
        static final String sellerViewSaleDetailsPattern = caseInsensitiveMode + "^view" + spacePattern + unsignedIntPattern + "$";
        static final String sellerEditSaleCommand = "edit [index]";
        static final String sellerEditSalePattern = caseInsensitiveMode + "^edit" + spacePattern + argumentPattern + "$";
        static final String sellerAddSaleCommand = "add off";
        static final String sellerAddSalePattern = caseInsensitiveMode + sellerAddSaleCommand + "$";
        static final String sellerShowProductsCommand = "view products";
        static final String sellerShowProductsPattern = caseInsensitiveMode + "^" + sellerShowProductsCommand + "$";
        static final String sellerViewProductDetailsCommand = "view [index]";
        static final String sellerViewProductDetailsPattern = caseInsensitiveMode + "^view" + spacePattern + argumentPattern + "$";
        static final String sellerViewProductBuyersCommand = "view buyers [index]";
        static final String sellerViewProductBuyersPattern = caseInsensitiveMode + "^view buyers" + spacePattern + argumentPattern + "$";
        static final String sellerEditProductCommand = "edit [index]";
        static final String sellerEditProductPattern = caseInsensitiveMode + "^edit" + spacePattern + argumentPattern + "$";
        static final String sellerAddProductCommand = "add product";
        static final String sellerAddProductPattern = caseInsensitiveMode + "^" + sellerAddProductCommand + "$";
        static final String sellerRemoveProductCommand = "remove product [index]";
        static final String sellerRemoveProductPattern = caseInsensitiveMode + "^remove product" + spacePattern + argumentPattern + "$";
        //customer account menu actions
        static final String customerCartShowProductsCommand = "show products";
        static final String customerCartShowProductsPattern = caseInsensitiveMode + "^" + customerCartShowProductsCommand + "$";
        static final String customerCartViewProductCommand = "view [productID]";
        static final String customerCartViewProductPattern = caseInsensitiveMode + "^view " + argumentPattern + "$";
        static final String customerCartIncreaseProductCountCommand = "increase [productID] [-count]";
        static final String customerCartIncreaseProductCountPattern = caseInsensitiveMode +
                "^increase " + argumentPattern + "(?: " + unsignedIntPattern + ")$";
        static final String customerCartDecreaseProductCountCommand = "increase [productID] [-count]";
        static final String customerCartDecreaseProductCountPattern = caseInsensitiveMode +
                "^decrease " + argumentPattern + "(?: " + unsignedIntPattern + ")$";
        static final String customerCartShowTotalPriceCommand = "show total price";
        static final String customerCartShowTotalPricePattern = caseInsensitiveMode + customerCartShowTotalPriceCommand + "$";
        static final String customerCartPurchaseCommand = "purchase";
        static final String customerCartPurchasePattern = caseInsensitiveMode + "^" + customerCartPurchaseCommand + "$";
        static final String customerShowOrdersCommand = "show orders";
        static final String customerShowOrdersPattern = caseInsensitiveMode + "^" + customerShowOrdersCommand + "$";
        static final String customerViewOrderCommand = "show order [orderID]";
        static final String customerViewOrderPattern = caseInsensitiveMode + "^show order " + argumentPattern + "$";
        static final String customerRateProductCommand = "rate [productID] [1-5]";
        static final String customerRateProductPattern = caseInsensitiveMode + "^rate " + argumentPattern + "([1-5])$";
    }
}
