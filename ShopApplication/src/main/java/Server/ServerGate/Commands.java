package Server.ServerGate;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Server.controller.Exceptions.*;

import static java.util.Map.entry;
import static Server.ServerGate.ServerRequestHandler.Session;

public class Commands {
    static Type stringType = new TypeToken<String>() {
    }.getType();
    static Type integerType = new TypeToken<Integer>() {
    }.getType();
    static Type doubleType = new TypeToken<Double>() {
    }.getType();
    static Type booleanType = new TypeToken<Boolean>() {
    }.getType();
    static Type stringListType = new TypeToken<ArrayList<String>>() {
    }.getType();
    static Type stringArrayListType = new TypeToken<ArrayList<String[]>>() {
    }.getType();
    static Type byteArrArr = new TypeToken<byte[][]>(){}.getType();
    static Type byteArrayType = new TypeToken<byte[]>(){}.getType();

    public static final String CommandRegex = "(^\\S+) (\\S+) (?:([^\\*]+)\\*)*([^\\*]+)?$";
    //Server.controller methods.
    public static final String authTokenRequest = "/main/authTokenRequest";
    public static final String usernameTypeValidation = "/main/usernameTypeValidation";
    public static final String createAccount = "/main/createAccount";
    public static final String isManager = "/main/isManager";
    public static final String doesManagerExist = "/main/doesManagerExist";
    public static final String login = "/main/login";
    public static final String logout = "/main/logout";
    public static final String getType = "/main/getType";
    public static final String getSubCategoriesOfThisCategory = "/main/getSubCategoriesOfThisCategory";
    public static final String getSubCategoriesOfACategory = "/main/getSubCategoriesOfACategory";
    public static final String getProductsOfThisCategory = "/main/getProductsOfThisCategory";
    public static final String getProductsInCategory = "/main/getProductsInCategory";
    public static final String sortFilterProducts = "/main/sortFilterProducts";
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
    public static final String adminGetOnlineAccounts = "/admin/getOnlineAccounts";
    public static final String adminManageAllFiles = "/admin/manageAllFiles";
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
    public static final String sellerEditAuction = "/seller/editAuction";
    public static final String sellerAddAuction = "/seller/addAuction";
    public static final String sellerRemoveFile = "/seller/removeFile";
    public static final String sellerViewAuctionWithId = "/seller/viewAuctionWithId";
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
    //supporterController methods
    public static final String supporterGetActiveChats = "/supporter/getActiveChats";
    public static final String supporterGetArchiveChats = "/supporter/getArchiveChats";
    public static final String supporterViewChat = "/supporter/viewChat";
    public static final String supporterSendMessage = "/supporter/sendMessage";
    public static final String supporterDeleteChat = "/supporter/deleteChat";

    public static final String supporterViewChatById = "/supporter/viewChatById";

    static Map<String, Task> allTasks = Map.ofEntries(
            entry(authTokenRequest, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    //TODO: add a method
                    return null;
                }
            }),
            entry(usernameTypeValidation, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().usernameTypeValidation(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetAllBuyLogs, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getAdminController().getAllBuyLogs();
                }
            }),
            entry(createAccount, new Task(stringType, stringType, stringType, stringType, stringType, stringType, stringType, doubleType, stringType, byteArrayType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().creatAccount(objectArgs[0] + "", objectArgs[1] + "",
                                objectArgs[2] + "", objectArgs[3] + "", objectArgs[4] + "", objectArgs[5] + "", objectArgs[6] + "",
                                ((double) objectArgs[7]), objectArgs[8] + "", ((byte[]) objectArgs[9]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(isManager, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getMainController().isManager();
                }
            }),
            entry(doesManagerExist, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getMainController().doesManagerExist();
                }
            }),
            entry(login, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().login(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(logout, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().logout();
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getType, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getMainController().getType();
                }
            }),
            entry(getSubCategoriesOfThisCategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getSubCategoriesOfThisCategory(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getSubCategoriesOfACategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getSubCategoriesOfACategory(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getProductsOfThisCategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getProductsOfThisCategory(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            /**
             * String categoryName, boolean inSale, boolean inAuction, String sortBy, boolean isIncreasing, boolean available, double minPrice, double maxPrice, String contains, String brand,
             *                                                   String extension, String storeName, double minRatingScore, HashMap<String, String> propertyFilters
             */
            entry(sortFilterProducts, new Task(stringType, booleanType, booleanType, stringType, booleanType, booleanType, doubleType, doubleType, stringType, stringType, stringType, stringType, doubleType, new TypeToken<HashMap<String, String>>() {
            }.getType()) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getMainController().sortFilterProducts(objectArgs[0] + "", ((boolean) objectArgs[1]), ((boolean) objectArgs[2]), objectArgs[3] + "", ((boolean) objectArgs[4]),
                            ((boolean) objectArgs[5]), ((double) objectArgs[6]), ((double) objectArgs[7]), objectArgs[8] + "", objectArgs[9] + "", objectArgs[10] + "", objectArgs[11] + "", ((double) objectArgs[12]), (HashMap<String, String>) objectArgs[13]);
                }
            }),
            entry(showProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().showProduct(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(digest, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().digest(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getPropertyValuesOfAProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getPropertyValuesOfAProduct(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getPropertiesOfCategory, new Task(stringType, booleanType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getPropertiesOfCategory(objectArgs[0] + "", ((boolean) objectArgs[1]));
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(subProductsOfAProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().subSellablesOfASellable(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getSubProductByID, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getSubProductByID(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(reviewsOfProductWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().reviewsOfProductWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(checkAuthorityOverCart, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().checkAuthorityOverCart();
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(addToCart, new Task(stringType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().addToCart(objectArgs[0] + "", ((int) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getProductsInCart, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getProductsInCart();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(increaseProductInCart, new Task(stringType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().increaseProductInCart(objectArgs[0] + "", ((int) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(decreaseProductInCart, new Task(stringType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().decreaseProductInCart(objectArgs[0] + "", ((int) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getTotalPriceOfCart, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getTotalPriceOfCart();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(addReview, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().addReview(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(viewPersonalInfo, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().viewPersonalInfo(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(viewPersonalInfoDef, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().viewPersonalInfo();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(editPersonalInfo, new Task(stringType, stringType, byteArrArr) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().editPersonalInfo(objectArgs[0] + "", objectArgs[1] + "", ((byte[][]) objectArgs[2]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(clearCart, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    currentSession.getMainController().clearCart();
                    return "";
                }
            }),
            entry(removeSubProductFromCart, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().removeSubProductFromCart(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getDefaultSubProductOfAProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getDefaultSubSellableOfASellable(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getPropertyValuesInCategory, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getPropertyValuesInCategory(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getSubProductsForAdvertisements, new Task(integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getSubSellablesForAdvertisements(((int) objectArgs[0]));
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getSubProductsInSale, new Task(integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getSubSellablesInSale(((int) objectArgs[0]));
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getCategoryTreeOfAProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getCategoryTreeOfAProduct(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getCategoryTreeOfACategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getCategoryTreeOfACategory(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getBuyersOfASubProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getBuyersOfASubProduct(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditPersonalInfo, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editPersonalInfo(objectArgs[0] + "", objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminManageUsers, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().manageUsers();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminViewUsername, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().viewUsername(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminDeleteUsername, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().deleteUsername(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminCreatAdminProfile, new Task(stringType, stringType, stringType, stringType, stringType, stringType, byteArrayType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().createAdminProfile(objectArgs[0] + "", objectArgs[1] + "",
                                objectArgs[2] + "", objectArgs[3] + "", objectArgs[4] + "", objectArgs[5] + "", ((byte[]) objectArgs[6]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminManageAllProducts, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().manageAllProducts();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminRemoveProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().removeProduct(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminCreateDiscountCode, new Task(stringType, stringType, stringType, doubleType, doubleType, stringArrayListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().createDiscountCode(objectArgs[0] + "", objectArgs[1] + "",
                                objectArgs[2] + "", ((double) objectArgs[3]), ((double) objectArgs[4]), (ArrayList<String[]>) objectArgs[5]);
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminViewActiveDiscountCodes, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().viewActiveDiscountCodes();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminViewArchiveDiscountCodes, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().viewArchiveDiscountCodes();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminViewDiscountCodeByCode, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().viewDiscountCodeByCode(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminViewDiscountCodeById, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().viewDiscountCodeById(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminPeopleWhoHaveThisDiscount, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().peopleWhoHaveThisDiscount(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditDiscountCode, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editDiscountCode(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminRemoveDiscountCode, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().removeDiscountCode(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetArchivedRequests, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getArchivedRequests();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetPendingRequests, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getPendingRequests();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminDetailsOfRequest, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().detailsOfRequest(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminAcceptRequest, new Task(stringType, booleanType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().acceptRequest(objectArgs[0] + "", ((boolean) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminManageCategories, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().manageCategories();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditCategory, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editCategory(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminAddCategory, new Task(stringType, stringType, stringListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().addCategory(objectArgs[0] + "", objectArgs[1] + "", ((ArrayList<String>) objectArgs[2]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetCategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getCategory(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminRemoveCategory, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().removeCategory(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminSetAccounts, new Task(stringType, stringArrayListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().setAccounts(objectArgs[0] + "", ((ArrayList<String[]>) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminRemoveAccountsFromDiscount, new Task(stringType, stringListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().removeAccountsFromDiscount(objectArgs[0] + "", ((ArrayList<String>) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetPropertyValuesOfAProductInARequest, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().getPropertyValuesOfAProductInARequest(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetProductsInSaleRequest, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().getProductsInSaleRequest(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminAddPropertyToACategory, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().addPropertyToACategory(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminRemovePropertyFromACategory, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().removePropertyFromACategory(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditBrandOfProduct, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editNameOfProduct(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditImageOfProduct, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editImageOfProduct(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditPropertyOfProduct, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editPropertyOfProduct(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditInfoTextOfProduct, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editInfoTextOfProduct(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditNameOfProduct, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editNameOfProduct(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerEditPersonalInfo, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().editPersonalInfo(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerIsProductWithNameAndBrand, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().isProductWithNameAndBrand(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerIsNameAndBrandUsed, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().isNameAndBrandUsed(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerDoesSellerSellThisProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().doesSellerSellThisProduct(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerGetAllSellLogs, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().getAllSellLogs();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerGetSellLogWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().getSellLogWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerManageProducts, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().manageProducts();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewProductBuyers, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewProductBuyers(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerEditProduct, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().editProduct(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerExist, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().exist(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddNewProduct, new Task(stringType, stringType, stringType, byteArrayType, stringType, new TypeToken<HashMap<String, String>>() {
            }.getType(), doubleType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addNewProduct(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "",
                                ((byte[]) objectArgs[3]), objectArgs[4] + "", ((HashMap<String, String>) objectArgs[5]), ((double) objectArgs[6]), ((int) objectArgs[7]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddNewSubProductToAnExistingProduct, new Task(stringType, doubleType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addNewSubProductToAnExistingProduct(objectArgs[0] + "", ((double) objectArgs[1]), ((int) objectArgs[2]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerRemoveProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().removeProduct(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewActiveSales, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewActiveSales();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewArchiveSales, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewArchiveSales();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewSaleWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewSaleWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerGetProductsInSale, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().getProductsInSale(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerEditSale, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().editSale(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddSale, new Task(stringType, stringType, doubleType, doubleType, stringListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addSale(objectArgs[0] + "", objectArgs[1] + "", ((double) objectArgs[2]), ((double) objectArgs[3]), ((ArrayList<String>) objectArgs[4]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddProductsToSale, new Task(stringType, stringListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addProductsToSale(objectArgs[0] + "", ((ArrayList<String>) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerRemoveProductsFromSale, new Task(stringType, stringListType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().removeProductsFromSale(objectArgs[0] + "", ((ArrayList<String>) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewBalance, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewBalance();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerRemoveSale, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().removeSale(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerGetPendingRequests, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().getPendingRequests();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerGetAllCategories, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().getAllCategories();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerDoesSellSubProduct, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().doesSellSubProduct(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerEditPersonalInfo, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getCustomerController().editPersonalInfo(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetTotalPriceOfCartWithDiscount, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getTotalPriceOfCartWithDiscount(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerPurchaseTheCart, new Task(stringType, stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getCustomerController().purchaseTheCart(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "", objectArgs[3] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetOrders, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getOrders();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetOrderWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getOrderWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerRateProduct, new Task(stringType, integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getCustomerController().rateProduct(objectArgs[0] + "", ((int) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerViewBalance, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().viewBalance();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerViewDiscountCodes, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().viewDiscountCodes();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetTotalPriceOfCart, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getTotalPriceOfCart();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerHasBought, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().hasBought(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetPropertyValuesOfAFileInRequest, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getPropertyValuesOfAFileInRequest(objectArgs[0] + "");
                    } catch (InvalidRequestIdException e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetBuyLogById, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getBuyLogItemsWithId(objectArgs[0] + "");
                    } catch (InvalidLogIdException e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getSubSellablesInAuction, new Task(integerType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return currentSession.getMainController().getSubSellablesInAuction(((int) objectArgs[0]));
                }
            }),
            entry(adminGetBuyLogItemsWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().getBuyLogItemsWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminEditBuyLogStatus, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().editBuyLogStatus(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerIsFileWithNameAndExtension, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().isFileWithNameAndExtension(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerIsNameAndExtensionUsed, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().isNameAndExtensionUsed(objectArgs[0] + "", objectArgs[1] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerDoesSellerSellThisFile, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().doesSellerSellThisFile(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getPropertyValuesOfAFile, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getPropertyValuesOfAFile(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddNewFile, new Task(stringType, stringType, stringType, byteArrayType, stringType, new TypeToken<HashMap<String, String>>(){}.getType(), doubleType, byteArrayType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addNewFile(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "",
                                ((byte[]) objectArgs[3]), objectArgs[4] + "", ((HashMap<String, String>) objectArgs[5]), ((double) objectArgs[6]), ((byte[]) objectArgs[7]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddNewSubFileToAnExistingFile, new Task(stringType, doubleType, byteArrayType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addNewSubFileToAnExistingFile(objectArgs[0] + "", ((double) objectArgs[1]), ((byte[]) objectArgs[2]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerRemoveAuction, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().removeAuction(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewActiveAuctions, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewActiveAuctions();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewArchiveAuctions, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewArchiveAuctions();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerEditAuction, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().editAuction(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerAddAuction, new Task(stringType, stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().addAuction(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminCreateSupporterProfile, new Task(stringType, stringType, stringType, stringType, stringType, stringType, byteArrayType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().createSupporterProfile(objectArgs[0] + "", objectArgs[1] + "", objectArgs[2] + "",
                                objectArgs[3] + "", objectArgs[4] + "", objectArgs[5] + "", ((byte[]) objectArgs[6]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getMessagesInAuctionChat, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getMessagesInAuctionChat(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(getMessagesInChat, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().getMessagesInChat(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerBid, new Task(stringType, doubleType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getCustomerController().bid(objectArgs[0] + "", ((double) objectArgs[1]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerRemoveFile, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSellerController().removeFile(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterGetActiveChats, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSupporterController().getActiveChats();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterGetArchiveChats, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSupporterController().getArchiveChats();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterViewChat, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSupporterController().viewChat(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterSendMessage, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSupporterController().sendMessage(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterDeleteChat, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getSupporterController().deleteChat(objectArgs[0] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sendMessage, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getMainController().sendMessage(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(supporterViewChatById, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSupporterController().viewChatById(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetSupportChatId, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getSupportChatId();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetAllSupporters, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getAllSupporters();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerCreateSupportChat, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().createSupportChat(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminSetCommission, new Task(doubleType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().setCommission(((double) objectArgs[0]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminSetWalletMin, new Task(doubleType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getAdminController().setWalletMin(((double) objectArgs[0]));
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(loadFromDatabase, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getMainController().loadFileFromDataBase(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerDownloadFile, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().downloadFile(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerPurchaseTheFile, new Task(stringType, stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        currentSession.getCustomerController().purchaseTheFile(objectArgs[0] + "", objectArgs[1] + "");
                        return "";
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(customerGetTotalPriceOfFileWithDiscount, new Task(stringType, doubleType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getCustomerController().getTotalPriceOfFileWithDiscount(objectArgs[0] + "", ((double) objectArgs[1]));
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(adminGetOnlineAccounts, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    return ServerRequestHandler.getOnlineAccounts();
                }
            }),
            entry(adminManageAllFiles, new Task() {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getAdminController().manageAllFiles();
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            }),
            entry(sellerViewAuctionWithId, new Task(stringType) {
                @Override
                public Object executeMethod(Session currentSession, Object[] objectArgs) {
                    try {
                        return currentSession.getSellerController().viewAuctionWithId(objectArgs[0] + "");
                    } catch (Exception e) {
                        return "exception:" + e.getClass().getSimpleName() + "\n" + e.getMessage();
                    }
                }
            })
    );
}
