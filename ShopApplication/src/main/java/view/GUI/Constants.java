package view.GUI;

public final class Constants {
    public static final String caseInsensitiveMode = "(?i)";
    public static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    public static final String unsignedIntPattern = "\\+?(\\d+)";
    public static final String usernamePattern = "(\\w+)";
    public static final String doublePattern = "(\\d+(?:\\.\\d+)?)";
    public static final String percentagePattern = "%?" + doublePattern + "%?";
    public static final String datePattern = "\\d{2}[/-]\\d{2}[/-]\\d{2}";
    public static final String IRLNamePattern = "[a-zA-Z]+( [a-zA-Z]+)*";
    public static final String emailPattern = ".+@.+\\.com";
    public static final String phonePattern = "\\d+";
    public static final String anonymousUserType = "Anonymous";
    public static final String adminUserType = "Admin";
    public static final String customerUserType = "Customer";
    public static final String sellerUserType = "Seller";
    public static final String SUPER_CATEGORY_NAME = "SuperCategory";
    public static final String[] types = new String[]{anonymousUserType, customerUserType, sellerUserType, adminUserType};

    public static int getTypeByIndex(String type) {
        for (int i = 0; i < types.length; i++) {
            if (type.equalsIgnoreCase(types[i])) {
                return i;
            }
        }
        return -1;
    }

    public static class FXMLLocations {
        public static final String AdminAccountMenu = "src/main/resources/fxml/AdminAccountMenu.fxml";
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
    }

    public static class Menus {
        public static final String accountMenuCommand = "account menu";
        public static final String accountMenuPattern = caseInsensitiveMode + "^" + accountMenuCommand + "$";
        public static final String allProductsMenuCommand = "products";
        public static final String allProductsMenuPattern = caseInsensitiveMode + "^" + allProductsMenuCommand + "$";
        public static final String sortMenuCommand = "sorting";
        public static final String sortMenuPattern = caseInsensitiveMode + "^" + sortMenuCommand + "$";
        public static final String filterMenuCommand = "filtering";
        public static final String filterMenuPattern = caseInsensitiveMode + "^" + filterMenuCommand + "$";
        public static final String saleMenuCommand = "sales";
        public static final String saleMenuPattern = caseInsensitiveMode + "^" + saleMenuCommand + "$";
        public static final String viewPersonalInfoCommand = "view personal info";
        public static final String viewPersonalInfoPattern = caseInsensitiveMode + "^" + viewPersonalInfoCommand + "$";
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
        public static final String productReviewMenuCommand = "comments";
        public static final String productReviewMenuPattern = caseInsensitiveMode + "^" + productReviewMenuCommand + "$";
    }

    public static class Actions {
        public static final String backCommand = "back";
        public static final String backPattern = caseInsensitiveMode + "^" + backCommand + "$";
        public static final String exitCommand = "exit";
        public static final String exitPattern = caseInsensitiveMode + "^" + exitCommand + "$";
        public static final String loginCommand = "login [username]";
        public static final String loginPattern = caseInsensitiveMode + "^login" + spacePattern + usernamePattern + "$";
        public static final String logoutCommand = "logout";
        public static final String logoutPattern = caseInsensitiveMode + "^" + logoutCommand + "$";
        public static final String registerCommand = "create account [type] [username]";
        public static final String registerPattern =
                caseInsensitiveMode + "^create account" + spacePattern + "(anonymous|customer|seller|admin)" + spacePattern + usernamePattern + "$";
        //AllProductsMenu actions.
        public static final String showProductsCommand = "show products [-all]";
        public static final String showProductsPattern = caseInsensitiveMode + "^show products(?:" + spacePattern + "(-all))?$";
        public static final String showCategoriesCommand = "view categories";
        public static final String showCategoriesPattern = caseInsensitiveMode + "^" + showCategoriesCommand + "$";
        public static final String chooseCategoryCommand = "choose category [index]";
        public static final String chooseCategoryPattern = caseInsensitiveMode + "^choose category" + spacePattern + unsignedIntPattern + "$";
        public static final String revertCategoryCommand = "revert category [-numberOfReverts]"; //you can set the number of times to go back. 1 by default.
        public static final String revertCategoryPattern = caseInsensitiveMode + "^revert category(?:" + spacePattern + unsignedIntPattern + ")?$";
        public static final String productDetailMenuCommand = "show product [productIndex]";
        public static final String productDetailMenuPattern = caseInsensitiveMode + "^show product" + spacePattern + unsignedIntPattern + "$";
        //SortMenu actions.
        public static final String sortCommand = "select sort";
        public static final String sortPattern = caseInsensitiveMode + "^" + sortCommand + "$";
        public static final String showCurrentSortCommand = "current sort";
        public static final String showCurrentSortPattern = caseInsensitiveMode + "^" + showCurrentSortCommand + "$";
        public static final String disableSortCommand = "disable sort";
        public static final String disableSortPattern = caseInsensitiveMode + "^" + disableSortCommand + "$";
        //FilterMenu actions.
        public static final String filterCommand = "choose filtering";
        public static final String filterPattern = caseInsensitiveMode + "^" + filterCommand + "$";
        public static final String showCurrentFiltersCommand = "current filters";
        public static final String showCurrentFiltersPattern = caseInsensitiveMode + "^" + showCurrentFiltersCommand + "$";
        public static final String disableFilterCommand = "disable filter [index]";
        public static final String disableFilterPattern = caseInsensitiveMode + "^disable filter" + spacePattern + unsignedIntPattern + "$";
        //SaleMenu actions.
        public static final String showOffsCommand = "show offs";
        public static final String showOffsPattern = caseInsensitiveMode + "^" + showOffsCommand + "$";
        public static final String showInSaleProductsCommand = "show in sale products";
        public static final String showInSaleProductsPattern = caseInsensitiveMode + "^" + showInSaleProductsCommand + "$";
        //account menus actions.
        public static final String viewPersonalInfoCommand = "view info";
        public static final String viewPersonalInfoPattern = caseInsensitiveMode + "^" + viewPersonalInfoCommand + "$";
        public static final String editFieldCommand = "edit field";
        public static final String editFieldPattern = caseInsensitiveMode + "^" + editFieldCommand + "$";
        public static final String showSellerCompanyInfoCommand = "view company information";
        public static final String showSellerCompanyInfoPattern = caseInsensitiveMode + "^" + showSellerCompanyInfoCommand + "$";
        public static final String showSellerSellHistoryCommand = "view sells history";
        public static final String showSellerSellHistoryPattern = caseInsensitiveMode + "^" + showSellerSellHistoryCommand + "$";
        public static final String showSingleSellLogCommand = "view sell history [sellHistoryIndex]";
        public static final String showSingleSellLogPattern = caseInsensitiveMode + "^view sell history" + spacePattern + unsignedIntPattern + "$";
        public static final String showSellerBalanceCommand = "view balance";
        public static final String showSellerBalancePattern = caseInsensitiveMode + "^" + showSellerBalanceCommand + "$";
        public static final String showCustomerBalanceCommand = "view balance";
        public static final String showCustomerBalancePattern = caseInsensitiveMode + "^" + showCustomerBalanceCommand + "$";
        public static final String showCustomerDiscountCodesCommand = "view discount codes";
        public static final String showCustomerDiscountCodesPattern = caseInsensitiveMode + "^" + showCustomerDiscountCodesCommand + "$";
        public static final String digestProductCommand = "digest";
        public static final String digestProductPattern = caseInsensitiveMode + "" + digestProductCommand + "$";
        public static final String showSubProductsCommand = "show sub products";
        public static final String showSubProductsPattern = caseInsensitiveMode + "^" + showSubProductsCommand + "$";
        public static final String addToCartCommand = "add to cart";
        public static final String addToCartPattern = caseInsensitiveMode + "^" + addToCartCommand + "$";
        public static final String productCurrentSellerCommand = "show current seller";
        public static final String productCurrentSellerPattern = caseInsensitiveMode + "^" + productCurrentSellerCommand + "$";
        //different form doc. cuz of index choosing.
        public static final String selectSellerCommand = "select seller [index]";
        public static final String selectSellerPattern = caseInsensitiveMode + "^select seller" + spacePattern + unsignedIntPattern + "$";
        public static final String showCurrentSellerCommand = "show current seller";
        public static final String showCurrentSellerPattern = caseInsensitiveMode + "^" + showCurrentSellerCommand + "$";
        public static final String compareProductByIDCommand = "compare [productID]";
        public static final String compareProductByIDPattern = caseInsensitiveMode + "^compare" + spacePattern + argumentPattern + "$";
        public static final String addReviewCommand = "add comment";
        public static final String addReviewPattern = caseInsensitiveMode + "^" + addReviewCommand + "$";
        public static final String showReviewsCommand = "show comments";
        public static final String showReviewsPattern = caseInsensitiveMode + "^" + showReviewsCommand + "$";
        //admin account menu actions
        public static final String adminViewAllUsersCommand = "show users";
        public static final String adminViewAllUsersPattern = caseInsensitiveMode + "^" + adminViewAllUsersCommand + "$";
        public static final String adminViewUserCommand = "view user [index]";
        public static final String adminViewUserPattern = caseInsensitiveMode + "^view user" + spacePattern + unsignedIntPattern + "$";
        public static final String adminDeleteUserCommand = "delete user [index]";
        public static final String adminDeleteUserPattern = caseInsensitiveMode + "^delete user" + spacePattern + unsignedIntPattern + "$";
        public static final String adminCreateAdminCommand = "create manager profile";
        public static final String adminCreateAdminPattern = caseInsensitiveMode + "^" + adminCreateAdminCommand + "$";
        public static final String adminShowProductsCommand = "show products";
        public static final String adminShowProductsPattern = caseInsensitiveMode + "^" + adminShowProductsCommand + "$";
        public static final String adminRemoveProductByIDCommand = "remove [index]";
        public static final String adminRemoveProductByIDPattern = caseInsensitiveMode + "^remove" + spacePattern + unsignedIntPattern + "$";
        public static final String adminCreateDiscountCodeCommand = "create discount";
        public static final String adminCreateDiscountCodePattern = caseInsensitiveMode + adminCreateDiscountCodeCommand + "$";
        public static final String adminShowDiscountCodesCommand = "show discount";
        public static final String adminShowDiscountCodesPattern = caseInsensitiveMode + "^" + adminShowDiscountCodesCommand + "$";
        public static final String adminViewDiscountCodeCommand = "view discount [index]";
        public static final String adminViewDiscountCodePattern = caseInsensitiveMode + "^view discount" + spacePattern + unsignedIntPattern + "$";
        public static final String adminEditDiscountCodeCommand = "edit discount [index]";
        public static final String adminEditDiscountCodePattern = caseInsensitiveMode + "^edit discount" + spacePattern + unsignedIntPattern + "$";
        public static final String adminRemoveDiscountCodeCommand = "remove discount [index]";
        public static final String adminRemoveDiscountCodePattern = caseInsensitiveMode + "^remove discount" + spacePattern + unsignedIntPattern + "$";
        //minor modification cuz of showing dilemma.
        public static final String adminViewRequestDetailCommand = "details [index]";
        public static final String adminViewRequestDetailPattern = caseInsensitiveMode + "^details" + spacePattern + unsignedIntPattern + "$";
        public static final String adminShowRequestsCommand = "view pending requests";
        public static final String adminShowRequestsPattern = caseInsensitiveMode + "^" + adminShowRequestsCommand + "$";
        public static final String adminShowArchiveRequestsCommand = "view archive requests";
        public static final String adminShowArchiveRequestsPattern = caseInsensitiveMode + "^" + adminShowArchiveRequestsCommand + "$";
        public static final String adminShowCategoriesCommand = "show categories";
        public static final String adminShowCategoriesPattern = caseInsensitiveMode + "^" + adminShowCategoriesCommand + "$";
        //minor modification cuz of showing dilemma.
        public static final String adminEditCategoryCommand = "edit [index]";
        public static final String adminEditCategoryPattern = caseInsensitiveMode + "^edit" + spacePattern + unsignedIntPattern + "$";
        public static final String adminAddCategoryCommand = "add [categoryName]";
        public static final String adminAddCategoryPattern = caseInsensitiveMode + "^add" + spacePattern + argumentPattern + "$";
        public static final String adminRemoveCategoryCommand = "remove [index]";
        public static final String adminRemoveCategoryPattern = caseInsensitiveMode + "^remove" + spacePattern + unsignedIntPattern + "$";
        //seller account menu actions.
        public static final String sellerShowSalesCommand = "view sales";
        public static final String sellerShowSalesPattern = caseInsensitiveMode + "^" + sellerShowSalesCommand + "$";
        public static final String sellerViewSaleDetailsCommand = "view [index]";
        public static final String sellerViewSaleDetailsPattern = caseInsensitiveMode + "^view" + spacePattern + unsignedIntPattern + "$";
        public static final String sellerEditSaleCommand = "edit [index]";
        public static final String sellerEditSalePattern = caseInsensitiveMode + "^edit" + spacePattern + unsignedIntPattern + "$";
        public static final String sellerAddSaleCommand = "add off";
        public static final String sellerAddSalePattern = caseInsensitiveMode + sellerAddSaleCommand + "$";
        public static final String sellerShowProductsCommand = "view products";
        public static final String sellerShowProductsPattern = caseInsensitiveMode + "^" + sellerShowProductsCommand + "$";
        public static final String sellerViewProductDetailsCommand = "view [index]";
        public static final String sellerViewProductDetailsPattern = caseInsensitiveMode + "^view" + spacePattern + unsignedIntPattern + "$";
        public static final String sellerViewProductBuyersCommand = "view buyers [index]";
        public static final String sellerViewProductBuyersPattern = caseInsensitiveMode + "^view buyers" + spacePattern + unsignedIntPattern + "$";
        public static final String sellerEditProductCommand = "edit [index]";
        public static final String sellerEditProductPattern = caseInsensitiveMode + "^edit" + spacePattern + unsignedIntPattern + "$";
        public static final String sellerAddProductCommand = "add product";
        public static final String sellerAddProductPattern = caseInsensitiveMode + "^" + sellerAddProductCommand + "$";
        public static final String sellerRemoveProductCommand = "remove product [index]";
        public static final String sellerRemoveProductPattern = caseInsensitiveMode + "^remove product" + spacePattern + unsignedIntPattern + "$";
        //customer account menu actions
        public static final String shoppingCartShowProductsCommand = "show products";
        public static final String shoppingCartShowProductsPattern = caseInsensitiveMode + "^" + shoppingCartShowProductsCommand + "$";
        public static final String shoppingCartViewProductCommand = "view [index]";
        public static final String shoppingCartViewProductPattern = caseInsensitiveMode + "^view" + spacePattern + unsignedIntPattern + "$";
        public static final String shoppingCartIncreaseProductCountCommand = "increase [index] [-count]";
        public static final String shoppingCartIncreaseProductCountPattern = caseInsensitiveMode +
                "^increase" + spacePattern + unsignedIntPattern + "(?:" + spacePattern + unsignedIntPattern + ")$";
        public static final String shoppingCartDecreaseProductCountCommand = "increase [index] [-count]";
        public static final String shoppingCartDecreaseProductCountPattern = caseInsensitiveMode +
                "^decrease" + spacePattern + unsignedIntPattern + "(?:" + spacePattern + unsignedIntPattern + ")$";
        public static final String shoppingCartShowTotalPriceCommand = "show total price";
        public static final String shoppingCartShowTotalPricePattern = caseInsensitiveMode + shoppingCartShowTotalPriceCommand + "$";
        public static final String shoppingCartPurchaseCommand = "purchase";
        public static final String shoppingCartPurchasePattern = caseInsensitiveMode + "^" + shoppingCartPurchaseCommand + "$";
        public static final String customerShowOrdersCommand = "show orders";
        public static final String customerShowOrdersPattern = caseInsensitiveMode + "^" + customerShowOrdersCommand + "$";
        public static final String customerViewOrderCommand = "show order [index]";
        public static final String customerViewOrderPattern = caseInsensitiveMode + "^show order" + spacePattern + unsignedIntPattern + "$";
        public static final String customerRateProductCommand = "rate [productID] [1-5]";
        public static final String customerRateProductPattern = caseInsensitiveMode + "^rate" + spacePattern + argumentPattern + "([1-5])$";
    }
}