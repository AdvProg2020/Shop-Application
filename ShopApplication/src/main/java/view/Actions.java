package view;

import controller.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

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

        public void setParent(Menu newParent) {
            this.parent = newParent;
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
        private StringBuilder productID;
        DigestProduct(String name, StringBuilder productID) {
            super(name ,Constants.Actions.digestProductPattern, Constants.Actions.digestProductCommand);
            this.productID = productID;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {
            try {
                String[] productInfo = mainController.digest(productID.toString());
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }

            //if ID is valid, show the info.

        }
    }

    public static class AddToCart extends Action {
        private StringBuilder subProductID;
        AddToCart(String name, StringBuilder subProductID) {
            super(name, Constants.Actions.addToCartPattern, Constants.Actions.addToCartCommand);
            this.subProductID = subProductID;
        }

        @Override
        public void execute(String command) {
            try {
                mainController.addToCart(subProductID.toString());
            } catch (Exceptions.InvalidSubProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static class SelectSeller extends Action {
        private StringBuilder subProductID;
        SelectSeller(String name, StringBuilder subProductID) {
            super(name, Constants.Actions.selectSellerPattern, Constants.Actions.showSellerCompanyInfoCommand);
            this.subProductID = subProductID;
        }


        //TODO: imp. first show all the subProducts and then index choosing.
        @Override
        public void execute(String command) {

        }
    }

    public static class CompareProductByID extends Action {
        private StringBuilder productID;
        CompareProductByID(String name, StringBuilder productID) {
            super(name, Constants.Actions.compareProductByIDPattern, Constants.Actions.compareProductByIDCommand);
            this.productID = productID;
        }


        //TODO: imp.
        @Override
        public void execute(String command) {
            Matcher commandMatcher = getMatcherReady(command);
            String otherProductID = commandMatcher.group(1);
            try {
                //what is the return type?
                mainController.compare(productID.toString(), otherProductID);
            } catch (Exceptions.InvalidProductIdException e) {
                System.out.println(e.getMessage());
                return;
            }
            //show infos.
        }
    }

    public static class AddComment extends Action {
        private StringBuilder productID;
        AddComment(String name, StringBuilder productID) {
            super(name, Constants.Actions.addReviewPattern, Constants.Actions.addReviewCommand);
            this.productID = productID;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowReviews extends Action {
        private StringBuilder productID;
        ShowReviews(String name, StringBuilder productID) {
            super(name, Constants.Actions.showReviewsPattern, Constants.Actions.showReviewsCommand);
            this.productID = productID;
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminViewUser extends Action {
        AdminViewUser(String name) {
            super(name, Constants.Actions.adminViewUserPattern, Constants.Actions.adminViewUserCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String userID) {

        }
    }

    public static class AdminDeleteUser extends Action {
        AdminDeleteUser(String name) {
            super(name, Constants.Actions.adminDeleteUserPattern, Constants.Actions.adminDeleteUserCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String userID) {

        }
    }

    public static class AdminCreateAdmin extends Action {
        AdminCreateAdmin(String name) {
            super(name, Constants.Actions.adminCreateAdminPattern, Constants.Actions.adminCreateAdminCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminRemoveProductByID extends Action {
        AdminRemoveProductByID(String name) {
            super(name, Constants.Actions.adminRemoveProductByIDPattern, Constants.Actions.adminRemoveProductByIDCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminCreateDiscountCode extends Action {
        AdminCreateDiscountCode(String name) {
            super(name, Constants.Actions.adminCreateDiscountCodePattern, Constants.Actions.adminCreateDiscountCodeCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminViewDiscountCode extends Action {
        AdminViewDiscountCode(String name) {
            super(name, Constants.Actions.adminViewDiscountCodePattern, Constants.Actions.adminViewDiscountCodeCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminEditDiscountCode extends Action {
        AdminEditDiscountCode(String name) {
            super(name, Constants.Actions.adminEditDiscountCodePattern, Constants.Actions.adminEditDiscountCodeCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminRemoveDiscountCode extends Action {
        AdminRemoveDiscountCode(String name) {
            super(name, Constants.Actions.adminRemoveDiscountCodePattern, Constants.Actions.adminRemoveDiscountCodeCommand);
        }


        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminShowRequests extends Action {
        AdminShowRequests(String name) {
            super(name, Constants.Actions.adminShowRequestsPattern, Constants.Actions.adminShowRequestsCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminViewRequestDetail extends Action {
        AdminViewRequestDetail(String name) {
            super(name, Constants.Actions.adminViewRequestDetailPattern, Constants.Actions.adminViewRequestDetailCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminShowCategories extends Action {
        AdminShowCategories(String name) {
            super(name, Constants.Actions.adminShowCategoriesPattern, Constants.Actions.adminShowCategoriesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminEditCategory extends Action {
        AdminEditCategory(String name) {
            super(name, Constants.Actions.adminEditCategoryPattern, Constants.Actions.adminEditCategoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminAddCategory extends Action {
        AdminAddCategory(String name) {
            super(name, Constants.Actions.adminAddCategoryPattern, Constants.Actions.adminAddCategoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class AdminRemoveCategory extends Action {
        AdminRemoveCategory(String name) {
            super(name, Constants.Actions.adminRemoveCategoryPattern, Constants.Actions.adminRemoveCategoryCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }

    public static class SellerShowSales extends Action {
        SellerShowSales(String name) {
            super(name, Constants.Actions.sellerShowSalesPattern, Constants.Actions.sellerShowSalesCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerViewSaleDetails extends Action {
        SellerViewSaleDetails(String name) {
            super(name, Constants.Actions.sellerViewSaleDetailsPattern, Constants.Actions.sellerViewSaleDetailsCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerEditSale extends Action {
        SellerEditSale(String name) {
            super(name, Constants.Actions.sellerEditSalePattern, Constants.Actions.sellerEditSaleCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerAddSale extends Action {
        SellerAddSale(String name) {
            super(name, Constants.Actions.sellerAddSalePattern, Constants.Actions.sellerAddSaleCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerShowProducts extends Action {
        SellerShowProducts(String name) {
            super(name, Constants.Actions.sellerShowProductsPattern, Constants.Actions.sellerShowProductsCommand);
        }

        @Override
        public void execute(String command) {
        }
    }

    public static class SellerViewProductDetails extends Action {
        SellerViewProductDetails(String name) {
            super(name, Constants.Actions.sellerViewProductDetailsPattern, Constants.Actions.sellerViewProductDetailsCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerViewProductBuyers extends Action {
        SellerViewProductBuyers(String name) {
            super(name, Constants.Actions.sellerViewProductBuyersPattern, Constants.Actions.sellerViewProductBuyersCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerEditProduct extends Action {
        SellerEditProduct(String name) {
            super(name, Constants.Actions.sellerEditProductPattern, Constants.Actions.sellerEditProductCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerAddProduct extends Action {
        SellerAddProduct(String name) {
            super(name, Constants.Actions.sellerAddProductPattern, Constants.Actions.sellerAddProductCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class SellerRemoveProduct extends Action {
        SellerRemoveProduct(String name) {
            super(name, Constants.Actions.sellerRemoveProductPattern, Constants.Actions.sellerRemoveProductCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartShowProducts extends Action {
        CustomerCartShowProducts(String name) {
            super(name, Constants.Actions.customerCartShowProductsPattern, Constants.Actions.customerCartShowProductsCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartViewProduct extends Action {
        CustomerCartViewProduct(String name) {
            super(name, Constants.Actions.customerCartViewProductPattern, Constants.Actions.customerCartViewProductCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartIncreaseProductCount extends Action {
        CustomerCartIncreaseProductCount(String name) {
            super(name, Constants.Actions.customerCartIncreaseProductCountPattern, Constants.Actions.customerCartIncreaseProductCountCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartDecreaseProductCount extends Action {
        CustomerCartDecreaseProductCount(String name) {
            super(name, Constants.Actions.customerCartDecreaseProductCountPattern, Constants.Actions.customerCartDecreaseProductCountCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartShowTotalPrice extends Action {
        CustomerCartShowTotalPrice(String name) {
            super(name, Constants.Actions.customerCartShowTotalPricePattern, Constants.Actions.customerCartShowTotalPriceCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerCartPurchase extends Action {
        CustomerCartPurchase(String name) {
            super(name, Constants.Actions.customerCartPurchasePattern, Constants.Actions.customerCartPurchaseCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerShowOrders extends Action {
        CustomerShowOrders(String name) {
            super(name, Constants.Actions.customerShowOrdersPattern, Constants.Actions.customerShowOrdersCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerViewOrder extends Action {
        CustomerViewOrder(String name) {
            super(name, Constants.Actions.customerViewOrderPattern, Constants.Actions.customerViewOrderCommand);
        }

        @Override
        public void execute(String command) {

        }
    }

    public static class CustomerRateProduct extends Action {
        CustomerRateProduct(String name) {
            super(name, Constants.Actions.customerRateProductPattern, Constants.Actions.customerRateProductCommand);
        }

        @Override
        public void execute(String command) {

        }
    }
}
