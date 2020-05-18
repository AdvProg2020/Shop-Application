package view;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Menus {
    private static Controller mainController;
    private static AdminController adminController;
    private static SellerController sellerController;
    private static CustomerController customerController;

    public static void init() {
        mainController = View.mainController;
        adminController = View.adminController;
        sellerController = View.sellerController;
        customerController = View.customerController;
    }

    public static class AccountMenu extends Menu {
        private Menu previousMenu;

        AccountMenu(String name) {
            super(name, false, null, Constants.Menus.accountMenuPattern, Constants.Menus.accountMenuCommand);
            Menu.setAccountMenu(this);
            previousMenu = null;
            initSubMenus();
            initSubActions();
        }

        @Override
        public void execute() {
            String userType = mainController.getType();
            if (userType.equalsIgnoreCase(Constants.anonymousUserType)) {
                ((AnonymousUserAccountMenu)subMenus.get(1)).run(previousMenu);
            } else if (userType.equalsIgnoreCase(Constants.adminUserType)) {
                ((AdminMenu)subMenus.get(2)).run(previousMenu);
            } else if (userType.equalsIgnoreCase(Constants.sellerUserType)) {
                ((SellerMenu)subMenus.get(3)).run(previousMenu);
            }else if (userType.equalsIgnoreCase(Constants.customerUserType)) {
                ((CustomerMenu)subMenus.get(4)).run(previousMenu);
            }
        }

        public void run(Menu previousMenu) {
            this.previousMenu = previousMenu;
            super.run();
        }

        @Override
        public void show() {
            // no execution needed!
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new AnonymousUserAccountMenu("Login/Register Menu", this,  Constants.anonymousUserType));
            subMenus.put(2, new AdminMenu("Admin Menu",this,  Constants.adminUserType));
            subMenus.put(3, new SellerMenu("Seller Menu",this, Constants.sellerUserType));
            subMenus.put(4, new CustomerMenu("Customer Menu", this, Constants.customerUserType));
        }

        @Override
        protected void initSubActions() {
            //no actions available.
        }

        public void loginFirst(Menu ifBackMenu,Menu ifDoneMenu) {
            ((AnonymousUserAccountMenu)subMenus.get(1)).loginFirst(ifBackMenu, ifDoneMenu);
        }
    }

    public static class FirstMenu extends Menu {
        FirstMenu(String name) {
            super(name, true, null, null, null);
            initSubMenus();
            initSubActions();
        }

        @Override
        public void initSubMenus() {
            subMenus.put(1, new SaleMenu("Sale menu", this));
            subMenus.put(2, new AllProductsMenu("products menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ExitAction());
        }
    }

    public static class AllProductsMenu extends Menu {
        private ArrayList<String> categoryTree;
        private ArrayList<String[]> availableCategories;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String[]> currentProducts;
        private ArrayList<String> availableProperties;
        private Map<String, String> currentProperties;

        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.allProductsMenuPattern, Constants.Menus.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
            this.currentFilters = new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)};
            currentSort = new StringBuilder();
            currentProducts = new ArrayList<>();
            this.availableCategories = new ArrayList<>();
            availableProperties = new ArrayList<>();
            currentProperties = new HashMap<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("product sorting menu", this, getAvailableSorts(), currentSort));
            subMenus.put(2, new FilterMenu("product filtering menu", this, getAvailableFilters(), currentFilters,
                    availableProperties, currentProperties, categoryTree));
        }

        private String[] getAvailableSorts() {
            return mainController.getProductAvailableSorts();
        }

        private String[] getAvailableFilters() {
            return mainController.getProductAvailableFilters();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show products -all");
            super.show();
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowProductsAction(categoryTree, currentFilters, currentSort,
                    currentProducts, availableProperties, currentProperties));
            subActions.put(index + 2, new Actions.ShowCategories(categoryTree, availableCategories, availableProperties));
            subActions.put(index + 3, new Actions.ChooseCategoryAction(categoryTree, availableCategories));
            subActions.put(index + 4, new Actions.RevertCategoryAction(categoryTree));
            subActions.put(index + 5, new Actions.ProductDetailMenu(currentProducts));
            subActions.put(index + 6, new Actions.BackAction(parent));
        }
    }

    public static class ProductDetailMenu extends Menu {
        private Map<Integer, Action> subActionsAnonymousCustomer;
        private Map<Integer, Action> subActionsAdminSeller;
        private StringBuilder productID;
        private StringBuilder subProductID;
        private ArrayList<String[]> subProducts;

        ProductDetailMenu(String name) {
            super(name, false, null, null, null);
            Menu.setProductDetailMenu(this);
            productID = new StringBuilder();
            subProductID = new StringBuilder();
            subProducts = new ArrayList<>();
            subActionsAnonymousCustomer = new HashMap<>();
            subActionsAdminSeller = new HashMap<>();
            initSubMenus();
            initSubActions();
        }

        private void modifyActions() {
            String type = mainController.getType();
            if (type.equalsIgnoreCase(Constants.anonymousUserType) || type.equalsIgnoreCase(Constants.customerUserType)) {
                subActions = subActionsAnonymousCustomer;
            } else {
                subActions = subActionsAdminSeller;
            }
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new ProductReviewMenu("comments menu", this, productID));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActionsAnonymousCustomer.put(index + 1, new Actions.DigestProduct(productID));
            subActionsAnonymousCustomer.put(index + 2, new Actions.ShowSubProducts(subProducts, productID, subProductID));
            subActionsAnonymousCustomer.put(index + 3, new Actions.AddToCart(subProductID));
            subActionsAnonymousCustomer.put(index + 4, new Actions.SelectSeller(subProductID, subProducts));
            subActionsAnonymousCustomer.put(index + 5, new Actions.ShowCurrentSeller(subProductID));
            subActionsAnonymousCustomer.put(index + 6, new Actions.CompareProductByID(productID));
            subActionsAnonymousCustomer.put(index + 7, new Actions.BackAction(null));

            subActionsAdminSeller.put(index + 1, new Actions.DigestProduct(productID));
            subActionsAdminSeller.put(index + 2, new Actions.ShowSubProducts(subProducts, productID, subProductID));
            subActionsAdminSeller.put(index + 3, new Actions.CompareProductByID(productID));
            subActionsAdminSeller.put(index + 4, new Actions.BackAction(null));
        }

        @Override
        public void run() {
            modifyActions();
            super.run();
        }

        public void runByProductID(String productID) {
            this.productID.delete(0, productID.length());
            this.productID.append(productID);
            ((Actions.BackAction) subActions.get(subActions.size() - 1)).setParent(Menu.getCallingMenu());
            this.run();
        }
    }

    public static class ProductReviewMenu extends Menu {
        private StringBuilder productID;
        private Map<Integer, Action> subActionsAnonymousCustomer;
        private Map<Integer, Action> subActionsAdminSeller;

        ProductReviewMenu(String name, Menu parent, StringBuilder productID) {
            super(name, true, parent, Constants.Menus.productReviewMenuPattern, Constants.Menus.productReviewMenuCommand);
            this.productID = productID;
            subActionsAdminSeller = new HashMap<>();
            subActionsAnonymousCustomer = new HashMap<>();
            initSubMenus();
            initSubActions();
        }

        private void modifyActions() {
            String type = mainController.getType();
            if (type.equalsIgnoreCase(Constants.anonymousUserType) || type.equalsIgnoreCase(Constants.customerUserType)) {
                subActions = subActionsAnonymousCustomer;
            } else {
                subActions = subActionsAdminSeller;
            }
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show comments");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menus.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActionsAnonymousCustomer.put(index + 1, new Actions.ShowReviews(productID));
            subActionsAnonymousCustomer.put(index + 2, new Actions.AddComment(productID, this));
            subActionsAnonymousCustomer.put(index + 3, new Actions.BackAction(parent));

            subActionsAdminSeller.put(index + 1, new Actions.ShowReviews(productID));
            subActionsAdminSeller.put(index + 3, new Actions.BackAction(parent));
        }

        @Override
        protected void run() {
            modifyActions();
            super.run();
        }
    }

    public static class SortMenu extends Menu {
        private StringBuilder currentSort;
        private String[] availableSorts;

        SortMenu(String name, Menu parent, String[] availableSorts, StringBuilder currentSort) {
            super(name, true, parent, Constants.Menus.sortMenuPattern, Constants.Menus.sortMenuCommand);
            this.currentSort = currentSort;
            this.availableSorts = availableSorts.clone();
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseSorting(currentSort, availableSorts));
            subActions.put(index + 2, new Actions.ShowCurrentSort(currentSort));
            subActions.put(index + 3, new Actions.DisableSort(currentSort));
            subActions.put(index + 4, new Actions.BackAction(parent));
        }
    }

    public static class FilterMenu extends Menu {
        private String[] currentFilters;
        private String[] availableFilters;
        private ArrayList<String> availableProperties;
        private Map<String, String> currentProperties;
        private ArrayList<String> categoryTree;

        FilterMenu(String name, Menu parent, String[] availableFilters, String[] currentFilters,
        ArrayList<String> availableProperties, Map<String, String> currentProperties, ArrayList<String> categoryTree) {
            super(name, true, parent, Constants.Menus.filterMenuPattern, Constants.Menus.filterMenuCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = availableFilters.clone();
            initSubMenus();
            initSubActions();
            this.availableProperties = availableProperties;
            this.currentProperties = currentProperties;
            this.categoryTree = categoryTree;
        }

        @Override
        protected void initSubMenus() {
            //no available sub meu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseFiltering(currentFilters, availableFilters, availableProperties, currentProperties, categoryTree));
            subActions.put(index + 2, new Actions.ShowCurrentFilters(currentFilters, availableFilters));
            subActions.put(index + 3, new Actions.DisableFilter(currentFilters, availableFilters));
            subActions.put(index + 4, new Actions.BackAction(parent));
        }
    }

    public static class SaleMenu extends Menu {
        private StringBuilder currentSort;
        private String[] currentFilters;
        private ArrayList<String[]> currentProducts;
        private ArrayList<String[]> currentSales;

        SaleMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.saleMenuPattern, Constants.Menus.saleMenuCommand);
            this.currentSort = new StringBuilder();
            this.currentFilters = new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)};
            this.currentProducts = new ArrayList<>();
            this.currentSales = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show offs");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("sale sort menu", this, getAvailableSorts(), this.currentSort));
            subMenus.put(2, new FilterMenu("sale filter menu", this, getAvailableFilters(), this.currentFilters, null, null, null));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowSales(currentSales));
            subActions.put(index + 2, new Actions.ShowInSaleProducts(currentSort, currentFilters, currentProducts));
            subActions.put(index + 3, new Actions.ProductDetailMenu(currentProducts));
            subActions.put(index + 4, new Actions.BackAction(parent));
        }

        private String[] getAvailableSorts() {
            return mainController.getProductAvailableSorts();
        }

        private String[] getAvailableFilters() {
            return mainController.getProductAvailableFilters();
        }
    }

    public static class AnonymousUserAccountMenu extends Menu {
        private Map<Integer, Menu> primarySubMenus;
        private Map<Integer, Action> primarySubActions;
        private Map<Integer, Action> loginSubActions;

        AnonymousUserAccountMenu(String name , Menu parent, String userType) {
            super(name, false, parent, userType, null);
            primarySubMenus = new HashMap<>();
            primarySubActions = new HashMap<>();
            loginSubActions = new HashMap<>();
            subActions = primarySubActions;
            subMenus = primarySubMenus;
            initSubMenus();
            initSubActions();
        }

        public void run(Menu previousMenu) {
            this.getBackAction().setParent(previousMenu);
            if (mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                subMenus = primarySubMenus;
                subActions = primarySubActions;
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        public void run() {
            if (mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                subMenus = primarySubMenus;
                subActions = primarySubActions;
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        protected void initSubMenus() {
            primarySubMenus.put(1, new ShoppingCartMenu("anonymous user shopping cart menu", this));
            subMenus = primarySubMenus;
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            primarySubActions.put(index + 1, new Actions.LoginAction());
            primarySubActions.put(index + 2, new Actions.RegisterAction());
            primarySubActions.put(index + 3, new Actions.BackAction(null));

            loginSubActions.put( 1, new Actions.LoginAction());
            loginSubActions.put( 2, new Actions.RegisterAction());
            loginSubActions.put( 3, new Actions.BackAction(null));
        }


        private void loginRun(Menu ifDoneMenu) {
            if ( ! mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                ifDoneMenu.run();
            }
            show();
            loginExecute(ifDoneMenu);
        }

        private void loginExecute(Menu ifDoneMenu) {
            String command = View.getNextLineTrimmed();
            if (command.equalsIgnoreCase("help")) {
                showCommandList();
                this.loginRun(ifDoneMenu);
            }
            if (command.matches(Integer.toString(1))) {
                ((Actions.RegisterAction)subActions.get(1)).runNLogin(command);
            } else if (command.matches(Integer.toString(2))) {
                subActions.get(2).run(command);
            } else {
                //if the command is invalid.
                System.out.println("invalid entry.");
            }
            this.execute();
        }

        public void loginFirst(Menu ifBackMenu, Menu ifDoneMenu) {
            subMenus = new HashMap<>();
            subActions = loginSubActions;
            ((Actions.BackAction)subActions.get(3)).setParent(ifBackMenu);
            this.loginRun(ifDoneMenu);
        }
    }

    public static class AdminMenu extends Menu {

        AdminMenu(String name, Menu parent,  String userType) {
            super(name, false, parent, userType, null);
            initSubMenus();
            initSubActions();
        }

        public void run(Menu previousMenu) {
            this.getBackAction().setParent(previousMenu);
            if (mainController.getType().equalsIgnoreCase(Constants.adminUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        public void run() {
            if (mainController.getType().equalsIgnoreCase(Constants.adminUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("admin personal info", this) {
                @Override
                protected String[] getEditableFields() {
                    return adminController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new UserManagingMenu("user managing menu", this));
            subMenus.put(3, new ProductManagingMenu("product managing menu", this));
            subMenus.put(4, new DiscountCodesManagingMenu("discount code managing menu", this));
            subMenus.put(5, new RequestManagingMenu("request managing menu", this));
            subMenus.put(6, new CategoryManagingMenu("category managing menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.Logout());
            subActions.put(index + 2, new Actions.BackAction(null));
        }
    }

    public static abstract class PersonalInfoMenu extends Menu {
        PersonalInfoMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.viewPersonalInfoPattern, Constants.Menus.viewPersonalInfoCommand);
            initSubMenus();
            initSubActions();
        }

        @Override
        protected void initSubMenus() {
            //no available subMenus
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ViewPersonalInfo());
            subActions.put(index + 2, new Actions.EditField(getEditableFields()));
            subActions.put(index + 3, new Actions.BackAction(parent));
        }

        protected abstract String[] getEditableFields();
    }

    public static class UserManagingMenu extends Menu {
        private ArrayList<String[]> users;

        UserManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.userManagingMenuPattern, Constants.Menus.userManagingMenuCommand);
            users = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show users");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no sub menus available.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminViewAllUsers(users));
            subActions.put(index + 2, new Actions.AdminViewUser(users));
            subActions.put(index + 3, new Actions.AdminDeleteUser(users));
            subActions.put(index + 4, new Actions.AdminCreateAdmin());
            subActions.put(index + 5, new Actions.BackAction(parent));
        }
    }

    public static class ProductManagingMenu extends Menu {
        private ArrayList<String[]> currentProducts;

        ProductManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.productManagingMenuPattern, Constants.Menus.productManagingMenuCommand);
            this.currentProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show products");
            super.show();
        }


        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowProducts(currentProducts));
            subActions.put(index + 2, new Actions.AdminRemoveProductByID(currentProducts));
            subActions.put(index + 3, new Actions.BackAction(parent));
        }
    }

    public static class DiscountCodesManagingMenu extends Menu {
        private ArrayList<String> discountCodes;

        DiscountCodesManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.discountCodesManagingMenuPattern, Constants.Menus.discountCodesManagingMenuCommand);
            this.discountCodes = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(subMenus.size() + 1 + floatingMenusIndexModification()).run("show discount codes");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        private String[] getEditableFields() {
            return adminController.getDiscountEditableFields();
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowDiscountCodes(discountCodes));
            subActions.put(index + 2, new Actions.AdminCreateDiscountCode());
            subActions.put(index + 3, new Actions.AdminViewDiscountCode(discountCodes));
            subActions.put(index + 4, new Actions.AdminEditDiscountCode(discountCodes, getEditableFields()));
            subActions.put(index + 5, new Actions.AdminRemoveDiscountCode(discountCodes));
            subActions.put(index + 6, new Actions.BackAction(parent));
        }
    }

    public static class RequestManagingMenu extends Menu {
        private ArrayList<String[]> pendingRequests;

        RequestManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.requestManagingMenuPattern, Constants.Menus.requestManagingMenuCommand);
            pendingRequests = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show pending requests");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowPendingRequests(pendingRequests));
            subActions.put(index + 2, new Actions.AdminShowArchiveRequests());
            subActions.put(index + 3, new Actions.AdminViewRequestDetail(pendingRequests));
            subActions.put(index + 4, new Actions.BackAction(parent));
        }
    }

    public static class CategoryManagingMenu extends Menu {
        private ArrayList<String> currentCategories;

        CategoryManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.categoryManagingMenuPattern, Constants.Menus.categoryManagingMenuCommand);
            this.currentCategories = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        private String[] getEditableFields() {
            return adminController.getCategoryEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show categories");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowCategories(currentCategories));
            subActions.put(index + 2, new Actions.AdminEditCategory(getEditableFields(), currentCategories));
            subActions.put(index + 3, new Actions.AdminAddCategory());
            subActions.put(index + 4, new Actions.AdminRemoveCategory(currentCategories));
            subActions.put(index + 5, new Actions.BackAction(this));
        }
    }

    public static class SellerMenu extends Menu {
        private ArrayList<String[]> sellLogs;

        SellerMenu(String name, Menu parent, String userType) {
            super(name, false, parent, userType, null);
            this.sellLogs = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        public void run(Menu previousMenu) {
            this.getBackAction().setParent(previousMenu);
            if (mainController.getType().equalsIgnoreCase(Constants.sellerUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        public void run() {
            if (mainController.getType().equalsIgnoreCase(Constants.sellerUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("seller personal info menu", this) {
                @Override
                protected String[] getEditableFields() {
                    return sellerController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new SellerProductMenu("seller product menu", this));
            subMenus.put(3, new SellerSalesMenu("seller sales menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowSellerCompanyInfo());
            subActions.put(index + 2, new Actions.ShowSellerBalance());
            subActions.put(index + 3, new Actions.ShowSellerSellHistory(sellLogs));
            subActions.put(index + 4, new Actions.ViewSingleSellHistory(sellLogs));
            subActions.put(index + 5, new Actions.Logout());
            subActions.put(index + 6, new Actions.BackAction(null));
        }
    }

    public static class SellerSalesMenu extends Menu {
        private ArrayList<String[]> currentSales;

        SellerSalesMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.sellerSaleManagingMenuPattern, Constants.Menus.sellerSaleManagingMenuCommand);
            this.currentSales = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        private String[] getEditableFields() {
            return sellerController.getSaleEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show sales");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.SellerShowSales(currentSales));
            subActions.put(index + 2, new Actions.SellerViewSaleDetails(currentSales));
            subActions.put(index + 3, new Actions.SellerEditSale(getEditableFields(), currentSales));
            subActions.put(index + 4, new Actions.SellerAddSale());
            subActions.put(index + 5, new Actions.BackAction(parent));
        }
    }

    public static class SellerProductMenu extends Menu {
        private ArrayList<String[]> sellerProducts;

        SellerProductMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.sellerProductManagingMenuPattern, Constants.Menus.sellerProductManagingMenuCommand);
            this.sellerProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show seller products");
            super.show();
        }

        private String[] getEditableFields() {
            return sellerController.getProductEditableFields();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.SellerShowProducts(sellerProducts));
            subActions.put(index + 2, new Actions.SellerViewProductDetails(sellerProducts));
            subActions.put(index + 3, new Actions.SellerViewProductBuyers(sellerProducts));
            subActions.put(index + 4, new Actions.SellerEditProduct(getEditableFields(), sellerProducts));
            subActions.put(index + 5, new Actions.SellerAddProduct());
            subActions.put(index + 6, new Actions.SellerRemoveProduct(sellerProducts));
            subActions.put(index + 7, new Actions.BackAction(parent));
        }


    }

    public static class CustomerMenu extends Menu {

        CustomerMenu(String name, Menu parent, String userType) {
            super(name, false, parent, userType, null);
            initSubMenus();
            initSubActions();
        }

        public void run(Menu previousMenu) {
            this.getBackAction().setParent(previousMenu);
            if (mainController.getType().equalsIgnoreCase(Constants.customerUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        public void run() {
            if (mainController.getType().equalsIgnoreCase(Constants.customerUserType)) {
                super.run();
            } else {
                parent.execute();
            }
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("customer personal info menu", this) {
                @Override
                protected String[] getEditableFields() {
                    return customerController.getPersonalInfoEditableFields();
                }
            });
            subMenus.put(2, new ShoppingCartMenu("customer shopping cart", this));
            subMenus.put(3, new CustomerOrderLogMenu("customer order log menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowCustomerBalance());
            subActions.put(index + 2, new Actions.ShowCustomerDiscountCodes());
            subActions.put(index + 3, new Actions.Logout());
            subActions.put(index + 4, new Actions.BackAction(null));
        }
    }

    //add product detail menu
    public static class ShoppingCartMenu extends Menu {
        private ArrayList<String[]> currentProducts;

        ShoppingCartMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.shoppingCartMenuPattern, Constants.Menus.shoppingCartMenuCommand);
            this.currentProducts = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            String type = mainController.getType();
            if (type.equalsIgnoreCase(Constants.adminUserType) || type.equalsIgnoreCase(Constants.sellerUserType)) {
                System.out.println("you dont possess a shopping cart");
                parent.run();
            } else {
                super.show();
            }
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShoppingCartShowProducts(currentProducts));
            subActions.put(index + 2, new Actions.ProductDetailMenu(currentProducts));
            subActions.put(index + 3, new Actions.ShoppingCartIncreaseProductCount(currentProducts));
            subActions.put(index + 4, new Actions.ShoppingCartDecreaseProductCount(currentProducts));
            subActions.put(index + 5, new Actions.ShoppingCartShowTotalPrice());
            subActions.put(index + 6, new Actions.ShoppingCartPurchase(this));
            subActions.put(index + 7, new Actions.BackAction(parent));
        }
    }

    public static class CustomerOrderLogMenu extends Menu {
        private ArrayList<String[]> currentOrderLogs;

        CustomerOrderLogMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.customerOrderLogMenuPattern, Constants.Menus.customerOrderLogMenuCommand);
            this.currentOrderLogs = new ArrayList<>();
            initSubMenus();
            initSubActions();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show orders");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.CustomerShowOrders(currentOrderLogs));
            subActions.put(index + 2, new Actions.CustomerViewOrder(currentOrderLogs));
            subActions.put(index + 3, new Actions.CustomerRateProduct());
            subActions.put(index + 4, new Actions.BackAction(parent));
        }
    }
}
