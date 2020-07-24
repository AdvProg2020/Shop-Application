package Client.view;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    public static final String supporterUserType = "Supporter";
    public static final String SUPER_CATEGORY_NAME = "SuperCategory";
    public static final String REQUEST_ACCEPT = "ACCEPTED";
    public static final String REQUEST_DECLINE = "DECLINED";
    public static final String REQUEST_PENDING = "PENDING";
    public static String base = new File("").getAbsolutePath();
    public static final DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

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
        public static final String adminProductManagingMenu =  "AdminSellableManagingMenu";
        public static final String adminRequestManagingMenu = "AdminRequestManagingMenu";
        public static final String adminManagingMenu = "AdminManagingMenu";
        public static final String adminBuyLogManagingMenu = "AdminBuyLogManagingMenu";
        public static final String adminBuyLogMangingPopup = "AdminBuyLogManagingPopup";
        public static final String purchaseMenu = "PurchaseMenu";
        public static final String productBox = "ProductBox";
        public static final String sellerManagingMenu = "SellerManagingMenu";
        public static final String sellerSellableManagingMenu = "SellerSellableManagingMenu";
        public static final String sellerSaleManagingMenu = "SellerSaleManagingMenu";
        public static final String sellerSaleManagingPopup = "SellerSaleManagingPopup";
        public static final String sellerSellLogsManagingMenu = "SellerSellLogsManagingMenu";
        public static final String sellerSellLogDetailsPopup = "SellerSellLogDetailsPopup";
        public static final String sellerAuctionManagingMenu = "SellerAuctionManagingMenu";
        public static final String sellerAuctionManagingPopup = "SellerAuctionManagingPopup";
        public static final String customerBuyLogMenu = "CustomerBuyLogMenu";
        public static final String customerBuyLogDetailsPopup = "CustomerBuyLogDetailsPopup";
        public static final String sellableDetailMenu = "ProductDetailMenu";
        public static final String categoriesBox = "CategoriesBox";
        public static final String editPersonalInfoPopup = "EditPersonalInfoPopup";
        public static final String editProductPopup = "EditProductPopup";
        public static final String addSellablePopup_Page1 = "AddSellablePopup_Page1";
        public static final String addProductPage2 = "AddProductPopup_Page2";
        public static final String addFilePage2 = "AddFilePopup_Page2";
        public static final String addProductRequestPopup = "AddProductRequestPopup";
        public static final String addAuctionRequestPopup = "AddAuctionRequestPopup";
        public static final String addFileRequestPopup = "AddFileRequestPopup";
        public static final String addSaleRequestPopup = "AddSaleRequestPopup";
        public static final String addReviewRequestPopup = "AddReviewRequestPopup";
        public static final String addSellerRequestPopup = "AddSellerRequestPopup";
        public static final String editRequestDetailsPopup = "EditRequestDetailsPopup";
        public static final String reviewBox = "ReviewBox";
        public static final String ratingBox = "RatingBox";
        public static final String categoryTreeBox = "CategoryTreeBox";
        public static final String purchaseConfirmation = "PurchaseConfirmation";
        public static final String addReviewPopup = "AddReviewPopup";
        public static final String compareMenu = "CompareMenu";
        public static final String supporterChatMenu = "SupporterChatMenu";
        public static final String supporterRegistrationPopup = "SupporterRegistrationPopup";
        public static final String auctionPopup = "AuctionPopup";
        public static final String messageBox = "MessageBox";
        public static final String chatPage = "ChatPage";
        public static final String customerChatPopup = "CustomerChatPopup";
        public static final String commissionManagingPopup = "CommissionManagingPopup";
    }


    public static class Commands {
        public static final String CommandRegex = "(^\\S+) (\\S+) (?:([^\\*]+)\\*)*([^\\*]+)$";
        //Server.controller methods.
        public static final String authTokenRequest = "/main/authTokenRequest";
        public static final String usernameTypeValidation = "/main/usernameTypeValidation";
        public static final String createAccount = "/main/createAccount";
        public static final String isManager = "/main/isManager";
        public static final String doesManagerExist = "/main/doesManagerExist";
        public static final String login = "/main/login";
        public static final String logout = "/main/logout";
        public static final String getType = "/main/getType";
        public static final String sortProducts = "/main/sortProducts";
        public static final String productToIdNameBrand = "/main/productToIdNameBrand";
        public static final String getSubCategoriesOfThisCategory = "/main/getSubCategoriesOfThisCategory";
        public static final String getSubCategoriesOfACategory = "/main/getSubCategoriesOfACategory";
        public static final String getProductsOfThisCategory = "/main/getProductsOfThisCategory";
        public static final String getProductsInCategory = "/main/getProductsInCategory";
        public static final String sortFilterProducts  = "/main/sortFilterProducts";
        public static final String showProduct = "/main/showProduct";
        public static final String digest = "/main/digest";
        public static final String getPropertyValuesOfAProduct = "/main/getPropertyValuesOfAProduct";
        public static final String getPropertiesOfCategory = "/main/getPropertiesOfCategory";
        public static final String subProductsOfAProduct = "/main/subProductsOfAProduct";
        public static final String getSubProductByID = "/main/getSubProductByID";
        public static final String reviewsOfProductWithId = "/main/reviewsOfProductWithId";
        public static final String checkAuthorityOverCart = "/main/checkAuthorityOverCart";
        public static final String addToCart = "/main/addToCart";
        public static final String getProductsInCart = "/main/getProductsInCart";
        public static final String increaseProductInCart = "/main/increaseProductInCart";
        public static final String decreaseProductInCart = "/main/decreaseProductInCart";
        public static final String getTotalPriceOfCart = "/main/getTotalPriceOfCart";
        public static final String addReview = "/main/addReview";
        public static final String viewPersonalInfo = "/main/viewPersonalInfo";
        public static final String viewPersonalInfoDef = "/main/viewPersonalInfoDef";
        public static final String editPersonalInfo = "/main/editPersonalInfo";
        public static final String clearCart = "/main/clearCart";
        public static final String removeSubProductFromCart = "/main/removeSubProductFromCart";
        public static final String getDefaultSubProductOfAProduct = "/main/getDefaultSubProductOfAProduct";
        public static final String getPropertyValuesInCategory = "/main/getPropertyValuesInCategory";
        public static final String getSubProductsForAdvertisements = "/main/getSubProductsForAdvertisements";
        public static final String getSubProductsInSale = "/main/getSubProductsInSale";
        public static final String getCategoryTreeOfAProduct = "/main/getCategoryTreeOfAProduct";
        public static final String getCategoryTreeOfACategory = "/main/getCategoryTreeOfACategory";
        public static final String getBuyersOfASubProduct = "/main/getBuyersOfASubProduct";

        public static final String getSubSellablesInAuction = "/main/getSubSellablesInAuction";
        public static final String getPropertyValuesOfAFile = "/main/getPropertyValuesOfAFile";
        public static final String getMessagesInAuctionChat = "/main/getMessagesInAuctionChat";
        public static final String getMessagesInChat = "/main/getMessagesInChat";

        public static final String sendMessage = "/main/sendMessage";
        public static final String loadFromDatabase = "/main/loadFromDatabase";
        //admin Server.controller methods.
        public static final String adminEditPersonalInfo = "/admin/editPersonalInfo";
        public static final String adminManageUsers = "/admin/manageUsers";
        public static final String adminViewUsername = "/admin/viewUsername";
        public static final String adminDeleteUsername = "/admin/deleteUsername";
        public static final String adminCreatAdminProfile = "/admin/creatAdminProfile";
        public static final String adminManageAllProducts = "/admin/manageAllProducts";
        public static final String adminRemoveProduct = "/admin/removeProduct";
        public static final String adminCreateDiscountCode = "/admin/createDiscountCode";
        public static final String adminViewActiveDiscountCodes = "/admin/viewActiveDiscountCodes";
        public static final String adminViewArchiveDiscountCodes = "/admin/viewArchiveDiscountCodes";
        public static final String adminViewDiscountCodeByCode = "/admin/viewDiscountCodeByCode";
        public static final String adminViewDiscountCodeById = "/admin/viewDiscountCodeById";
        public static final String adminPeopleWhoHaveThisDiscount = "/admin/peopleWhoHaveThisDiscount";
        public static final String adminEditDiscountCode = "/admin/editDiscountCode";
        public static final String adminRemoveDiscountCode = "/admin/removeDiscountCode";
        public static final String adminGetArchivedRequests = "/admin/getArchivedRequests";
        public static final String adminGetPendingRequests = "/admin/getPendingRequests";
        public static final String adminDetailsOfRequest = "/admin/detailsOfRequest";
        public static final String adminAcceptRequest = "/admin/acceptRequest";
        public static final String adminManageCategories = "/admin/manageCategories";
        public static final String adminEditCategory = "/admin/editCategory";
        public static final String adminAddCategory = "/admin/addCategory";
        public static final String adminGetCategory = "/admin/getCategory";
        public static final String adminRemoveCategory = "/admin/removeCategory";
        public static final String adminSetAccounts = "/admin/setAccounts";
        public static final String adminRemoveAccountsFromDiscount = "/admin/removeAccountsFromDiscount";
        public static final String adminGetPropertyValuesOfAProductInARequest = "/admin/getPropertyValuesOfAProductInARequest";
        public static final String adminGetProductsInSaleRequest = "/admin/getProductsInSaleRequest";
        public static final String adminAddPropertyToACategory = "/admin/addPropertyToACategory";
        public static final String adminRemovePropertyFromACategory = "/admin/removePropertyFromACategory";
        public static final String adminEditBrandOfProduct = "/admin/editBrandOfProduct";
        public static final String adminEditImageOfProduct = "/admin/editImageOfProduct";
        public static final String adminEditPropertyOfProduct = "/admin/editPropertyOfProduct";
        public static final String adminEditInfoTextOfProduct = "/admin/editInfoTextOfProduct";
        public static final String adminEditNameOfProduct = "/admin/editNameOfProduct";

        public static final String adminGetPropertyValuesOfAFileInRequest = "/admin/getPropertyValuesOfAFileInRequest";
        public static final String adminGetAllBuyLogs = "/admin/getAllBuyLogs";
        public static final String adminGetBuyLogById = "/admin/getBuyLogById";
        public static final String adminGetBuyLogItemsWithId = "/admin/getBuyLogItemsWithId";
        public static final String adminEditBuyLogStatus = "/admin/editBuyLogStatus";
        public static final String adminCreateSupporterProfile = "/admin/createSupporterProfile";
        public static final String adminSetCommission = "/admin/setCommission";
        public static final String adminSetWalletMin = "/admin/setWalletMin";
        //seller Server.controller methods.
        public static final String sellerEditPersonalInfo = "/seller/editPersonalInfo";
        public static final String sellerIsProductWithNameAndBrand = "/seller/isProductWithNameAndBrand";
        public static final String sellerIsNameAndBrandUsed = "/seller/isNameAndBrandUsed";
        public static final String sellerDoesSellerSellThisProduct = "/seller/doesSellerSellThisProduct";
        public static final String sellerGetAllSellLogs = "/seller/getAllSellLogs";
        public static final String sellerGetSellLogWithId = "/seller/getSellLogWithId";
        public static final String sellerManageProducts = "/seller/manageProducts";
        public static final String sellerViewProductBuyers = "/seller/viewProductBuyers";
        public static final String sellerEditProduct = "/seller/editProduct";
        public static final String sellerExist = "/seller/exist";
        public static final String sellerAddNewProduct = "/seller/addNewProduct";
        public static final String sellerAddNewSubProductToAnExistingProduct = "/seller/addNewSubProductToAnExistingProduct";
        public static final String sellerRemoveProduct = "/seller/removeProduct";
        public static final String sellerViewActiveSales = "/seller/viewActiveSales";
        public static final String sellerViewArchiveSales = "/seller/viewArchiveSales";
        public static final String sellerViewSaleWithId = "/seller/viewSaleWithId";
        public static final String sellerGetProductsInSale = "/seller/getProductsInSale";
        public static final String sellerEditSale = "/seller/editSale";
        public static final String sellerAddSale = "/seller/addSale";
        public static final String sellerAddProductsToSale = "/seller/addProductsToSale";
        public static final String sellerRemoveProductsFromSale = "/seller/removeProductsFromSale";
        public static final String sellerViewBalance = "/seller/viewBalance";
        public static final String sellerRemoveSale = "/seller/removeSale";
        public static final String sellerGetPendingRequests = "/seller/getPendingRequests";
        public static final String sellerGetAllCategories = "/seller/getAllCategories";
        public static final String sellerDoesSellSubProduct = "/seller/doesSellSubProduct";

        public static final String sellerIsFileWithNameAndExtension = "/seller/isFileWithNameAndExtension";
        public static final String sellerIsNameAndExtensionUsed = "/seller/isNameAndExtensionUsed";
        public static final String sellerDoesSellerSellThisFile = "/seller/doesSellerSellThisFile";
        public static final String sellerAddNewFile = "/seller/addNewFile";
        public static final String sellerAddNewSubFileToAnExistingFile = "/seller/addNewSubFileToAnExistingFile";
        public static final String sellerRemoveAuction = "/seller/removeAuction";
        public static final String sellerViewActiveAuctions = "/seller/viewActiveAuctions";
        public static final String sellerViewArchiveAuctions = "/seller/viewArchiveAuctions";
        public static final String sellerViewAuctionWithId = "/seller/viewAuctionWithId";
        public static final String sellerEditAuction = "/seller/editAuction";
        public static final String sellerAddAuction = "/seller/addAuction";
        public static final String sellerRemoveFile = "/seller/removeFile";
        //customer Server.controller methods.
        public static final String customerEditPersonalInfo = "/customer/editPersonalInfo";
        public static final String customerGetTotalPriceOfCartWithDiscount = "/customer/getTotalPriceOfCartWithDiscount";
        public static final String customerPurchaseTheCart = "/customer/purchaseTheCart";
        public static final String customerGetOrders = "/customer/getOrders";
        public static final String customerGetOrderWithId = "/customer/getOrderWithId";
        public static final String customerRateProduct = "/customer/rateProduct";
        public static final String customerViewBalance = "/customer/viewBalance";
        public static final String customerViewDiscountCodes = "/customer/viewDiscountCodes";
        public static final String customerGetTotalPriceOfCart = "/customer/getTotalPriceOfCart";
        public static final String customerHasBought = "/customer/hasBought";
        public static final String customerBid = "/customer/bid";

        public static final String customerGetSupportChatId = "/customer/getSupportChatId";
        public static final String customerGetAllSupporters = "/customer/getAllSupporters";
        public static final String customerCreateSupportChat = "/customer/createSupportChat";
        public static final String customerDownloadFile = "/customer/downloadFile";
        public static final String customerPurchaseTheFile = "/customer/purchaseTheFile";
        public static final String customerGetTotalPriceOfFileWithDiscount = "/customer/getTotalPriceOfFileWithDiscount";
        //supporterController methods.
        public static final String supporterGetActiveChats = "/supporter/getActiveChats";
        public static final String supporterGetArchiveChats = "/supporter/getArchiveChats";
        public static final String supporterViewChat = "/supporter/viewChat";
        public static final String supporterSendMessage = "/supporter/sendMessage";
        public static final String supporterDeleteChat = "/supporter/deleteChat";

        public static final String supporterViewChatById = "/supporter/viewChatById";
    }
}