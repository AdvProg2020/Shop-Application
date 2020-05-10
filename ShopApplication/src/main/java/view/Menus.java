package view;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;

import java.util.ArrayList;


public class Menus {
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

    public static class AccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;

        AccountMenu(String name) {
            super(name, false, null, Constants.Menus.accountMenuPattern, Constants.Menus.accountMenuCommand);
            Menu.accountMenu = this;
            previousMenu = null;
            nextMenu = null;
        }

        @Override
        public void execute() {
            String userType = mainController.getType();
            for (Menu menu : subMenus.values()) {
                if (userType.matches(menu.getCommandPattern())) {
                    menu.run();
                }
            }
        }

        @Override
        public void show() {
            // no execution needed!
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new AnonymousUserAccountMenu("Login/Register Menu", this, this.previousMenu, this.nextMenu, Constants.anonymousUserType));
            subMenus.put(2, new AdminMenu("Admin Menu", this, this.previousMenu, this.nextMenu, Constants.adminUserType));
            subMenus.put(3, new SellerMenu("Seller Menu", this, this.previousMenu, this.nextMenu, Constants.sellerUserType));
            subMenus.put(4, new CustomerMenu("Customer Menu", this, this.previousMenu, this.nextMenu, Constants.customerUserType));
        }

        @Override
        protected void initSubActions() {
            //no actions available.
        }

        @Override
        public void run() {
            previousMenu = nextMenu = Menu.getCallingMenu();
            this.execute();
        }

        public void run(Menu previousMenu, Menu nextMenu) {
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            this.execute();
        }
    }

    public static class FirstMenu extends Menu {
        FirstMenu(String name) {
            super(name, true, null, null, null);
        }

        @Override
        public void initSubMenus() {
            subMenus.put(1, new SaleMenu("Sale menu", this));
            subMenus.put(2, new AllProductsMenu("products menu", this));
            subMenus.put(3, Menu.accountMenu);
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ExitAction("first menu exit"));
        }
    }


    //TODO: remove show products as an action and always do it as show method and kinda allow iteration through pages of the products. same for sale menu
    public static class AllProductsMenu extends Menu {
        private ArrayList<String> categoryTree;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String> currentProducts;

        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.allProductsMenuPattern, Constants.Menus.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
            this.currentFilters = new String[getAvailableFilters().length];
            currentSort = new StringBuilder();
            currentProducts = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("product sorting menu", this, getAvailableSorts(), currentSort));
            subMenus.put(2, new FilterMenu("product filtering menu", this, getAvailableFilters(), currentFilters));
            subMenus.put(3, new ProductMenu("product Menu", this));
        }

        //TODO: imp.
        private String[] getAvailableSorts() {
            return null;
        }

        //TODO: imp. waiting for shayan to add the method
        private String[] getAvailableFilters() {
            return null;
        }


        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowProductsAction("show products", this.categoryTree, this.currentFilters,this.currentSort,this.currentProducts));
            subActions.put(index + 2, new Actions.ShowCategories("show categories", this.categoryTree));
            subActions.put(index + 4, new Actions.ChooseCategoryAction("choose category", this.categoryTree));
            subActions.put(index + 5, new Actions.RevertCategoryAction("revert category"));
            subActions.put(index + 6, new Actions.BackAction("all product menu back", parent));
        }
    }

    //TODO: bayad in fact ke age taraf anonymous bd betone be sabad kharid az inja ezafe kone mahsoolo handle konim.
    public static class ProductMenu extends Menu {
        private String productID;
        ProductMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.productDetailMenuPattern, Constants.Menus.productDetailMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }

        public void digest(){}
        //should select seller before adding.
        public void addToShoppingCart(){}
        public void showAttributes(){}
        public void compareByID(String otherProductID){}
        public void showComments(){}
        public void addComment(){}
    }

    public static class SortMenu extends Menu {
        private StringBuilder currentSort;
        private String[] availableSorts;

        SortMenu(String name, Menu parent, String[] availableSorts, StringBuilder currentSort){
            super(name, true, parent, Constants.Menus.sortMenuPattern, Constants.Menus.sortMenuCommand);
            this.currentSort = currentSort;
            this.availableSorts = availableSorts.clone();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowAvailableSorts("product available sorts", availableSorts));
            subActions.put(index + 2, new Actions.SortAction("product sorter", currentSort));
            subActions.put(index + 3, new Actions.ShowCurrentSort("product current sort", currentSort));
            subActions.put(index + 4, new Actions.DisableSort("product sort remover", currentSort));
            subActions.put(index + 5, new Actions.BackAction("product sort back", parent));
        }
    }

    //wtf is with filtering anyway
    public static class FilterMenu extends Menu {
        private String[] currentFilters;
        private String[] availableFilters;

        FilterMenu(String name, Menu parent, String[] availableFilters, String[] currentFilters){
            super(name, true, parent, Constants.Menus.filterMenuPattern, Constants.Menus.filterMenuCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters.clone();
        }

        @Override
        protected void initSubMenus() {
            //no available sub meu.
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowAvailableFilters("product available filters", availableFilters));
            subActions.put(index + 2, new Actions.FilterAction("product sorter", currentFilters));
            subActions.put(index + 3, new Actions.ShowCurrentFilters("product current filters", currentFilters));
            subActions.put(index + 4, new Actions.DisableFilter("product filter remover", currentFilters));
            subActions.put(index + 5, new Actions.BackAction("product filter back", parent));
        }
    }

    //handles both method and menu
    public static class SaleMenu extends Menu {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String> currentProducts;
        private ArrayList<String> currentOffs;
        SaleMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.saleMenuPattern, Constants.Menus.saleMenuCommand);
            this.currentSort = new StringBuilder();
            this.currentFilters = new String[getAvailableFilters().length];
            this.currentProducts = new ArrayList<>();
            this.currentOffs = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("sale sort menu", this, getAvailableSorts(), this.currentSort));
            subMenus.put(2, new FilterMenu("sale filter menu", this, getAvailableFilters(), this.currentFilters));
            subMenus.put(3, new ProductMenu("sale menu prdduct menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowOffs("show products", this.currentSort, this.currentFilters, this.currentProducts, this.currentOffs));
            subActions.put(index + 2, new Actions.BackAction("sale menu back", parent));
        }

        //TODO: imp.
        private String[] getAvailableSorts() {
            return null;
        }

        //TODO: imp.
        private String[] getAvailableFilters() {
            return null;
        }
    }

    public static class AnonymousUserAccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        AnonymousUserAccountMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
        }

        @Override
        protected void initSubMenus() {
            //no sub menu available.
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.LoginAction("Login"));
            subActions.put(index + 2, new Actions.RegisterAction("Register"));
            subActions.put(index + 3, new Actions.AccountMenuBackAction("back", previousMenu));
        }
    }

    public static class AdminMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        AdminMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("admin personal info", this) {
                //TODO: imp.
                @Override
                protected ArrayList<String> getEditableFields() {
                    return null;
                }
            });
            subMenus.put(2, new UserManagingMenu("user managing menu", this));
            subMenus.put(3, new ProductManagingMenu("product managing menu", this));
            subMenus.put(4, new DiscountCodesManagingMenu( "discount code managing menu", this));
            subMenus.put(5, new RequestManagingMenu("request managing menu", this));
            subMenus.put(6, new CategoryManagingMenu("category managing menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.AccountMenuBackAction("another fuckin back", previousMenu));
        }
    }

    public static abstract class PersonalInfoMenu extends Menu {
        PersonalInfoMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.viewPersonalInfoPattern, Constants.Menus.viewPersonalInfoCommand);
        }

        @Override
        protected void initSubMenus() {
            //no available subMenus
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.EditField("edit field",getEditableFields()));
            subActions.put(index + 2, new Actions.BackAction("view personal info back", parent));
        }

        protected abstract ArrayList<String> getEditableFields();
    }

    public static class UserManagingMenu extends Menu{
        UserManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.userManagingMenuPattern, Constants.Menus.userManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class ProductManagingMenu extends Menu{
        ProductManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.productManagingMenuPattern, Constants.Menus.productManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    //this one handles both creation and view discount codes. so supports to commands
    public static class DiscountCodesManagingMenu extends Menu{
        DiscountCodesManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.discountCodesManagingMenuPattern, Constants.Menus.discountCodesManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class RequestManagingMenu extends Menu {
        RequestManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.requestManagingMenuPattern, Constants.Menus.requestManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class CategoryManagingMenu extends Menu {
        CategoryManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.categoryManagingMenuPattern, Constants.Menus.categoryManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class SellerMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        SellerMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("seller personal info menu", this) {
                //TODO: imp.
                @Override
                protected ArrayList<String> getEditableFields() {
                    return null;
                }
            });
            subMenus.put(2, new SellerProductMenu("seller product menu", this));
            subMenus.put(3, new SellerSalesMenu("seller sales menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowSellerCompanyInfo("seller company info"));
            subActions.put(index + 2, new Actions.ShowSellerCategories("seller categories"));
            subActions.put(index + 3, new Actions.ShowSellerBalance("seller balance"));
            subActions.put(index + 4, new Actions.ShowSellerSellHistory("seller sell history"));
            subActions.put(index + 5, new Actions.AccountMenuBackAction("seller menu back", previousMenu));
        }
    }

    public static class SellerSalesMenu extends Menu {
        SellerSalesMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerSaleManagingMenuPattern, Constants.Menus.sellerSaleManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class SellerProductMenu extends Menu {
        SellerProductMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerProductManagingMenuPattern, Constants.Menus.sellerProductManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }


    }

    //Todo: in sub menus add custom personalInfoMenu
    public static class CustomerMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        CustomerMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("customer personal info menu", this) {
                //TODO: imp
                @Override
                protected ArrayList<String> getEditableFields() {
                    return null;
                }
            });
            subMenus.put(2, new ShoppingCartMenu("customer shopping cart", this));
            subMenus.put(3, new CustomerOrderLogMenu("customer order log menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowCustomerBalance("show customer balance"));
            subActions.put(index + 2, new Actions.ShowCustomerDiscountCodes("show customer discount codes"));
            subActions.put(index + 3, new Actions.AccountMenuBackAction("back", previousMenu));
        }
    }

    //this is both used in product menu and in customer menu
    //also should be able to handle both method call and menu call because of shopping cart
    //TODO: isAccountMenuAccessible ino che gohi bokhorim :|
    public static class ShoppingCartMenu extends Menu {
        ShoppingCartMenu(String name, Menu parent){
            super(name, true, parent, Constants.Menus.shoppingCartMenuPattern, Constants.Menus.shoppingCartMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }

    public static class CustomerOrderLogMenu extends Menu {
        CustomerOrderLogMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.customerOrderLogMenuPattern, Constants.Menus.customerOrderLogMenuCommand);
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {

        }
    }
}
