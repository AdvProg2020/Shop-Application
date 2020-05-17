package view;

import controller.*;

import java.util.ArrayList;

class Menus {
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
            Menu.setAccountMenu(this);
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
            getStackTrace().push(this);
            previousMenu = Menu.getCallingMenu();
            nextMenu = null;
            this.execute();
        }

        void run(Menu previousMenu, Menu nextMenu) {
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
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ExitAction("exit"));
        }
    }
    
    public static class AllProductsMenu extends Menu {
        private ArrayList<String> categoryTree;
        private ArrayList<String[]> availableCategories;
        private String[] currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String[]> currentProducts;

        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.allProductsMenuPattern, Constants.Menus.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
            this.currentFilters = new String[]{"false", Double.toString(0.00), Double.toString(0.00), null, null, null, Double.toString(0.00)};
            currentSort = new StringBuilder();
            currentProducts = new ArrayList<>();
            this.availableCategories = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("product sorting menu", this, getAvailableSorts(), currentSort));
            subMenus.put(2, new FilterMenu("product filtering menu", this, getAvailableFilters(), currentFilters));
        }

        private String[] getAvailableSorts() {
            return mainController.getAvailableSorts();
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
            subActions.put(index + 1, new Actions.ShowProductsAction("show products", this.categoryTree, this.currentFilters,this.currentSort,this.currentProducts));
            subActions.put(index + 2, new Actions.ShowCategories("show categories", this.categoryTree, availableCategories));
            subActions.put(index + 4, new Actions.ChooseCategoryAction("choose category", this.categoryTree, availableCategories));
            subActions.put(index + 5, new Actions.RevertCategoryAction("revert category", categoryTree));
            subActions.put(index + 6, new Actions.ProductDetailMenu("productDetailMenu", currentProducts));
            subActions.put(index + 7, new Actions.BackAction("all product menu back", parent));
        }
    }


    //TODO: this motha fucka.
    public static class ProductDetailMenu extends Menu {
        private StringBuilder productID;
        private StringBuilder subProductID;
        ProductDetailMenu(String name){
            super(name, false, null, null, null);
            Menu.setProductDetailMenu(this);
           subProductID = new StringBuilder();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new ProductReviewMenu("comments menu", this, productID));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.DigestProduct("digest product", productID));
            subActions.put(index + 2, new Actions.AddToCart("add to cart", subProductID));
            subActions.put(index + 3, new Actions.SelectSeller("select seller", subProductID, productID));
            subActions.put(index + 4, new Actions.ShowCurrentSeller("show current seller", subProductID));
            subActions.put(index + 4, new Actions.CompareProductByID("compare products", productID));
            subActions.put(index + 5, new Actions.BackAction("back", null));
        }

        public void runByProductID(String productID) {
            this.productID.delete(0, productID.length());
            this.productID.append(productID);
            ((Actions.BackAction)subActions.get(subActions.size() - 1)).setParent(Menu.getCallingMenu());
            this.run();
        }
    }

    public static class ProductReviewMenu extends Menu {
        private StringBuilder productID;
        ProductReviewMenu(String name, Menu parent, StringBuilder productID) {
            super(name, true, parent, Constants.Menus.productReviewMenuPattern, Constants.Menus.productReviewMenuCommand);
            this.productID = productID;
        }

        @Override
        protected void initSubMenus() {
            //no available sub menus.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowReviews("show comments", productID));
            subActions.put(index + 2, new Actions.AddComment("add comment", productID));
            subActions.put(index + 3, new Actions.BackAction("back", parent));
        }
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
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseSorting("choose sort", currentSort, availableSorts));
            subActions.put(index + 2, new Actions.ShowCurrentSort("product current sort", currentSort));
            subActions.put(index + 3, new Actions.DisableSort("product sort remover", currentSort));
            subActions.put(index + 4, new Actions.BackAction("product sort back", parent));
        }
    }

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
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ChooseFiltering("product sorter", currentFilters, availableFilters));
            subActions.put(index + 2, new Actions.ShowCurrentFilters("product current filters", currentFilters, availableFilters));
            subActions.put(index + 3, new Actions.DisableFilter("product filter remover", currentFilters, availableFilters));
            subActions.put(index + 4, new Actions.BackAction("product filter back", parent));
        }
    }

    //TODO: show sales joda bayad bashe refresh ham beshe
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
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new SortMenu("sale sort menu", this, getAvailableSorts(), this.currentSort));
            subMenus.put(2, new FilterMenu("sale filter menu", this, getAvailableFilters(), this.currentFilters));
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ShowSales("show offs", currentSales));
            subActions.put(index + 2, new Actions.ShowInSaleProducts("show filtered products", currentSort, currentFilters, currentProducts));
            subActions.put(index + 3, new Actions.ProductDetailMenu("product detail menu", currentProducts));
            subActions.put(index + 4, new Actions.BackAction("sale menu back", parent));
        }

        private String[] getAvailableSorts() {
            return mainController.getAvailableSorts();
        }

        private String[] getAvailableFilters() {
            return mainController.getProductAvailableFilters();
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
            subMenus.put(1, new ShoppingCartMenu("anonymous user shopping cart menu", this));
        }

        @Override
        public void execute() {
            if (mainController.getType().equalsIgnoreCase(Constants.anonymousUserType)) {
                super.execute();
            } else {
                parent.run();
            }
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.LoginAction("Login"));
            subActions.put(index + 2, new Actions.RegisterAction("Register"));
            subActions.put(index + 3, new Actions.BackAction("back", previousMenu));
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
                @Override
                protected String[] getEditableFields() {
                    return adminController.getPersonalInfoEditableFields();
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
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.Logout("logout"));
            subActions.put(index + 2, new Actions.BackAction("back", previousMenu));
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
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.ViewPersonalInfo("show personal info"));
            subActions.put(index + 2, new Actions.EditField("edit personal info", getEditableFields()));
            subActions.put(index + 3, new Actions.BackAction("view personal info back", parent));
        }

        protected abstract String[] getEditableFields();
    }

    //TODO: executesh bayad fargh kone.
    public static class UserManagingMenu extends Menu{
        private ArrayList<String[]> users;
        UserManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.userManagingMenuPattern, Constants.Menus.userManagingMenuCommand);
            users = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            //no sub menus available.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminViewAllUsers("show users", users));
            subActions.put(index + 2, new Actions.AdminViewUser("view user", users));
            subActions.put(index + 3, new Actions.AdminDeleteUser("delete user", users));
            subActions.put(index + 4, new Actions.AdminCreateAdmin("create admin"));
            subActions.put(index + 5, new Actions.BackAction("back", parent));
        }
    }

    public static class ProductManagingMenu extends Menu{
        private ArrayList<String[]> currentProducts;
        ProductManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.productManagingMenuPattern, Constants.Menus.productManagingMenuCommand);
            this.currentProducts = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowProducts("show products", currentProducts));
            subActions.put(index + 2, new Actions.AdminRemoveProductByID("remove product", currentProducts));
            subActions.put(index + 3, new Actions.BackAction("back", parent));
        }
    }

    public static class DiscountCodesManagingMenu extends Menu {
        private ArrayList<String> discountCodes;
        DiscountCodesManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.discountCodesManagingMenuPattern, Constants.Menus.discountCodesManagingMenuCommand);
            this.discountCodes = new ArrayList<>();
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
            subActions.put(index + 1, new Actions.AdminShowDiscountCodes("show discount codes", discountCodes));
            subActions.put(index + 1, new Actions.AdminCreateDiscountCode("create discount code"));
            subActions.put(index + 2, new Actions.AdminViewDiscountCode("view discount code", discountCodes));
            subActions.put(index + 3, new Actions.AdminEditDiscountCode("edit discount codes", discountCodes, getEditableFields()));
            subActions.put(index + 4, new Actions.AdminRemoveDiscountCode("remove discount code", discountCodes));
            subActions.put(index + 5, new Actions.BackAction("back", parent));
        }
    }

    //TODO: show dilemma.
    public static class RequestManagingMenu extends Menu {
        RequestManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.requestManagingMenuPattern, Constants.Menus.requestManagingMenuCommand);
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1,  new Actions.AdminShowRequests("show requests"));
            subActions.put(index + 2, new Actions.AdminViewRequestDetail("view request detail"));
            subActions.put(index + 3, new Actions.BackAction("back", parent));
        }
    }

    public static class CategoryManagingMenu extends Menu {
        private ArrayList<String> currentCategories;
        CategoryManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.categoryManagingMenuPattern, Constants.Menus.categoryManagingMenuCommand);
            this.currentCategories = new ArrayList<>();
        }

        private String[] getEditableFields() {
            return adminController.getCategoryEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show and refresh");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.AdminShowCategories("show categories", currentCategories));
            subActions.put(index + 2, new Actions.AdminEditCategory("edit category", getEditableFields(), currentCategories));
            subActions.put(index + 3, new Actions.AdminAddCategory("add category"));
            subActions.put(index + 4, new Actions.AdminRemoveCategory("remove category", currentCategories));
        }
    }

    public static class SellerMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;
        private ArrayList<String[]> sellLogs;
        SellerMenu(String name, Menu parent, Menu previousMenu, Menu nextMenu, String userType) {
            super(name, false, parent, userType, null);
            this.previousMenu = previousMenu;
            this.nextMenu = nextMenu;
            this.sellLogs = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new PersonalInfoMenu("seller personal info menu",this) {
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
            subActions.put(index + 1, new Actions.ShowSellerCompanyInfo("seller company info"));
            subActions.put(index + 2, new Actions.ShowSellerBalance("seller balance"));
            subActions.put(index + 3, new Actions.ShowSellerSellHistory("seller sell history", sellLogs));
            subActions.put(index + 4, new Actions.ViewSingleSellHistory("show single sell history", sellLogs));
            subActions.put(index + 5, new Actions.Logout("logout"));
            subActions.put(index + 6, new Actions.BackAction("seller menu back", previousMenu));
        }
    }

    public static class SellerSalesMenu extends Menu {
        private ArrayList<String[]> currentSales;
        SellerSalesMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerSaleManagingMenuPattern, Constants.Menus.sellerSaleManagingMenuCommand);
            this.currentSales = new ArrayList<>();
        }

        private String[] getEditableFields() {
            return sellerController.getSaleEditableFields();
        }

        @Override
        public void show() {
            subActions.get(floatingMenusIndexModification() + subMenus.size() + 1).run("show and refresh");
            super.show();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.SellerShowSales("show sales", currentSales));
            subActions.put(index + 2, new Actions.SellerViewSaleDetails("view sale details", currentSales));
            subActions.put(index + 3, new Actions.SellerEditSale("edit sale", getEditableFields(), currentSales));
            subActions.put(index + 4, new Actions.SellerAddSale("add sale"));
            subActions.put(index + 5, new Actions.BackAction("back", parent));
        }
    }

    //TODO: show bayad koni avalesh.
    public static class SellerProductMenu extends Menu {
        private ArrayList<String[]> sellerProducts;
        SellerProductMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.sellerProductManagingMenuPattern, Constants.Menus.sellerProductManagingMenuCommand);
            this.sellerProducts = new ArrayList<>();
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
            subActions.put(index + 1, new Actions.SellerShowProducts("show seller products", sellerProducts));
            subActions.put(index + 2, new Actions.SellerViewProductDetails("view product details", sellerProducts));
            subActions.put(index + 3, new Actions.SellerViewProductBuyers("view product buyers", sellerProducts));
            subActions.put(index + 4, new Actions.SellerEditProduct("edit product", getEditableFields()));
            subActions.put(index + 4, new Actions.SellerAddProduct("add product"));
            subActions.put(index + 4, new Actions.SellerRemoveProduct("remove product", sellerProducts));
            subActions.put(index + 5, new Actions.BackAction("back", parent));
        }


    }
    
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
            subActions.put(index + 1, new Actions.ShowCustomerBalance("show customer balance"));
            subActions.put(index + 2, new Actions.ShowCustomerDiscountCodes("show customer discount codes"));
            subActions.put(index + 3, new Actions.Logout("logout"));
            subActions.put(index + 4, new Actions.BackAction("back", previousMenu));
        }
    }

    //this is both used in product menu and in customer menu
    public static class ShoppingCartMenu extends Menu {
        private ArrayList<String[]> currentProducts;
        ShoppingCartMenu(String name, Menu parent){
            super(name, false, parent, Constants.Menus.shoppingCartMenuPattern, Constants.Menus.shoppingCartMenuCommand);
            this.currentProducts = new ArrayList<>();
        }

        @Override
        public void show() {
            String type = mainController.getType();
            if (type.equalsIgnoreCase(Constants.adminUserType) || type.equalsIgnoreCase(Constants.sellerUserType)) {
                System.out.println("you dont possess a shopping cart");
                return;
            }
            else {
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
            subActions.put(index + 1, new Actions.CustomerCartShowProducts("show products", currentProducts));
            subActions.put(index + 2, new Actions.CustomerCartViewProduct("view product", currentProducts));
            subActions.put(index + 3, new Actions.CustomerCartIncreaseProductCount("increase count", currentProducts));
            subActions.put(index + 4, new Actions.CustomerCartDecreaseProductCount("decrease count", currentProducts));
            subActions.put(index + 5, new Actions.CustomerCartShowTotalPrice("show total price"));
            subActions.put(index + 6, new Actions.CustomerCartPurchase("purchase products", this));
            subActions.put(index + 7, new Actions.BackAction("back", parent));
        }
    }

    public static class CustomerOrderLogMenu extends Menu {
        private ArrayList<String[]> currentOrderLogs;
        CustomerOrderLogMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.customerOrderLogMenuPattern, Constants.Menus.customerOrderLogMenuCommand);
            this.currentOrderLogs = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {
            //no available sub menu.
        }

        @Override
        protected void initSubActions() {
            int index = floatingMenusIndexModification() + subMenus.size();
            subActions.put(index + 1, new Actions.CustomerShowOrders("show orders", currentOrderLogs));
            subActions.put(index + 2, new Actions.CustomerViewOrder("view order", currentOrderLogs));
            subActions.put(index + 3, new Actions.CustomerRateProduct("rate product"));
            subActions.put(index + 4, new Actions.BackAction("back", parent));
        }
    }
}
