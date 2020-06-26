package view;

import java.io.File;

public final class Constants {
    public static final String caseInsensitiveMode = "(?i)";
    public static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    public static final String unsignedIntPattern = "\\+?(\\d+)";
    public static final String usernamePattern = "(\\w+)";
    public static final String doublePattern = "(\\d+(?:\\.\\d+)?)";
    public static final String percentagePattern = "%?" + doublePattern + "%?";
    public static final String datePattern = "\\d{2}[/-]\\d{2}[/-]\\d{2, 4}";
    public static final String IRLNamePattern = "[a-zA-Z]+( [a-zA-Z]+)*";
    public static final String emailPattern = "^.+@.+\\..+$";
    public static final String phonePattern = "\\+?\\d{8,12}";
    public static final String anonymousUserType = "Anonymous";
    public static final String adminUserType = "Admin";
    public static final String customerUserType = "Customer";
    public static final String sellerUserType = "Seller";
    public static final String SUPER_CATEGORY_NAME = "SuperCategory";
    public static final String REQUEST_ACCEPT = "ACCEPTED";
    public static final String REQUEST_DECLINE = "DECLINED";
    public static final String REQUEST_PENDING = "PENDING";
    public static String base = new File("").getAbsolutePath();


    public static final String[] types = new String[]{anonymousUserType, customerUserType, sellerUserType, adminUserType};

    public static int getTypeByIndex(String type) {
        for (int i = 0; i < types.length; i++) {
            if (type.equalsIgnoreCase(types[i])) {
                return i;
            }
        }
        return -1;
    }

    public static class FXMLs {
        public static final String mainMenu = "MainMenu";
        public static final String base = "Base";
        public static final String shoppingCartMenu = "ShoppingCartMenu";
        public static final String loginPopup = "LoginPopup";
        public static final String registerPopup = "RegisterPopup";
        public static final String personalInfoMenu = "PersonalInfoMenu";
        public static final String productsMenu = "ProductsMenu";
        public static final String adminCategoryManagingMenu = "AdminCategoryManagingMenu";
        public static final String adminCategoryManagingPopup = "AdminCategoryManagingPopup";
        public static final String adminDiscountManagingMenu = "AdminDiscountManagingMenu";
        public static final String adminDiscountManagingPopup = "AdminDiscountManagingPopup";
        public static final String adminAccountManagingMenu = "AdminAccountManagingMenu";
        public static final String adminRegistrationPopup = "AdminRegistrationPopup";
        public static final String adminProductManagingMenu =  "AdminProductManagingMenu";
        public static final String adminRequestManagingMenu = "AdminRequestManagingMenu";
        public static final String adminManagingMenu = "AdminManagingMenu";
        public static final String purchaseMenu = "PurchaseMenu";
        public static final String productBox = "ProductBox";
        public static final String sellerManagingMenu = "SellerManagingMenu";
        public static final String sellerProductManagingMenu = "SellerProductManagingMenu";
        public static final String sellerSaleManagingMenu = "SellerSaleManagingMenu";
        public static final String sellerSaleManagingPopup = "SellerSaleManagingPopup";
        public static final String sellerSellLogsManagingMenu = "SellerSellLogsManagingMenu";
        public static final String sellerSellLogDetailsPopup = "SellerSellLogDetailsPopup";
        public static final String customerBuyLogMenu = "CustomerBuyLogMenu";
        public static final String customerBuyLogDetailsPopup = "CustomerBuyLogDetailsPopup";
        public static final String productDetailMenu = "ProductDetailMenu";
        public static final String categoriesBox = "CategoriesBox";
        public static final String editPersonalInfoPopup = "EditPersonalInfoPopup";
        public static final String editProductPopup = "EditProductPopup";
        public static final String addProductPage1 = "AddProductPopup_Page1";
        public static final String addProductPage2 = "AddProductPopup_Page2";
        public static final String addProductRequestPopup = "AddProductRequestPopup";
        public static final String addSaleRequestPopup = "AddSaleRequestPopup";
        public static final String addReviewRequestPopup = "AddReviewRequestPopup";
        public static final String addSellerRequestPopup = "AddSellerRequestPopup";
        public static final String editRequestDetailsPopup = "EditRequestDetailsPopup";
        public static final String reviewBox = "ReviewBox";
        public static final String ratingBox = "RatingBox";
        public static final String categoryTreeBox = "CategoryTreeBox";
        public static final String addReviewPopup = "AddReviewPopup";
    }
}