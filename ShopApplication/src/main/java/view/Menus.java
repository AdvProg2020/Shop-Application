package view;

import java.util.ArrayList;


//Todo: addSubMenus, support back option for every field getter and menu. new all submenus or you'll get nullPtrExc.
//Todo: make all stupid public methods private :||||
public class Menus {
    //TODO: initSubActions havaset bashe bayad fargh kone. ye Action makhsos besaz.
    //TODO: execute khas. handle kardane back alan moshkel dre.
    public static class AccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;

        AccountMenu(String name) {
            super(name, false, null, Constants.Menus.accountMenuPattern, Constants.Menus.accountMenuCommand);
            Menu.accountMenu = this;
            previousMenu = null;
            nextMenu = null;
        }

        //TODO: imp.
        @Override
        public void execute() {

        }

        @Override
        public void show() {
            // no execution needed!
        }

        @Override
        protected void initSubMenus() {
            subMenus.put(1, new AnonymousUserAccountMenu("Login/Register Menu", this));
            subMenus.put(2, new AdminMenu("Admin Menu", this));
            subMenus.put(3, new SellerMenu("Seller Menu", this));
            subMenus.put(4, new CustomerMenu("Customer Menu", this));
        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.BackAction("account menu back", previousMenu));
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
        private ArrayList<String> currentFilters;
        private StringBuilder currentSort;
        private ArrayList<String> currentProducts;

        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.allProductsMenuPattern, Constants.Menus.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
            this.currentFilters = new ArrayList<>();
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
        private ArrayList<String> getAvailableSorts() {
            return null;
        }

        //TODO: imp.
        private ArrayList<String> getAvailableFilters() {
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

    public static class ProductMenu extends Menu {
        private String productID;
        ProductMenu(String name, Menu parent){}
        @Override
        public void execute() {}
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
        private ArrayList<String> availableSorts;

        SortMenu(String name, Menu parent, ArrayList<String> availableSorts, StringBuilder currentSort){
            super(name, true, parent, Constants.Menus.sortMenuPattern, Constants.Menus.sortMenuCommand);
            this.currentSort = currentSort;
            this.availableSorts = new ArrayList<>(availableSorts);
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
        private ArrayList<String> currentFilters;
        private ArrayList<String> availableFilters;

        FilterMenu(String name, Menu parent, ArrayList<String> availableFilters, ArrayList<String> currentFilters){
            super(name, true, parent, Constants.Menus.filterMenuPattern, Constants.Menus.filterMenuCommand);
            this.currentFilters = currentFilters;
            this.availableFilters = new ArrayList<>(availableFilters);
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
        private ArrayList<String> currentFilters;
        private ArrayList<String> currentProducts;
        private ArrayList<String> currentOffs;
        SaleMenu(String name, Menu parent) {
            super(name, true, parent, Constants.Menus.saleMenuPattern, Constants.Menus.saleMenuCommand);
            this.currentSort = new StringBuilder();
            this.currentFilters = new ArrayList<>();
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
        private ArrayList<String> getAvailableSorts() {
            return null;
        }

        //TODO: imp.
        private ArrayList<String> getAvailableFilters() {
            return null;
        }
    }

    public static class AnonymousUserAccountMenu extends Menu {
        AnonymousUserAccountMenu(String name, Menu parent) {
            super(name, false, parent, null, null);
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
        }
    }

    public static class LoginMenu extends Menu{
        private String username;
        private String password;
        LoginMenu(String name, Menu parent){}
        @Override
        public void execute() {}
    }

    public static class RegisterMenu extends Menu {
        private String username;
        private String password;
        private String phoneNumber;
        private String firstName;
        private String lastName;
        private String emailAddress;
        private String type;
        RegisterMenu(String name, Menu parent) {}
        @Override
        public void execute() {}
        private void getCustomerInfo(){}
        private void getSellerInfo(){}
        private void getAdminInfo(){}
    }

    //Todo: in sub menus add custom personalInfoMenu
    public static class AdminMenu extends Menu {
        AdminMenu(String name, Menu parent) {
            super(name, false, parent, null, null);
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
        public void viewUser(String command){}
        public void deleteUser(String command){}
        public void createAdmin(){}
    }

    public static class ProductManagingMenu extends Menu{
        ProductManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.productManagingMenuPattern, Constants.Menus.productManagingMenuCommand);
        }
        public void removeProduct(String command);
    }

    //this one handles both creation and view discount codes. so supports to commands
    public static class DiscountCodesManagingMenu extends Menu{
        DiscountCodesManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.discountCodesManagingMenuPattern, Constants.Menus.discountCodesManagingMenuCommand);
        }
        public void createDiscountCode(){}
        public void viewDiscountCodes(){}
        public void viewDiscountByCode(String code){}
        public void editDiscountCodes(String code){}
        public void removeDiscountCode(String code){}
    }

    public static class RequestManagingMenu extends Menu {
        RequestManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.requestManagingMenuPattern, Constants.Menus.requestManagingMenuCommand);
        }

        public void viewRequestDetailsByID(String ID){}
        public void determineRequest(String ID, boolean isAccepted){}
    }

    public static class CategoryManagingMenu extends Menu {
        CategoryManagingMenu(String name, Menu parent) {
            super(name, false, parent, Constants.Menus.categoryManagingMenuPattern, Constants.Menus.categoryManagingMenuCommand);
        }

        public boolean isCategoryValid(String category){}
        public void addCategory(String category){}
        public void editCategory(String category){}
        public void removeCategory(String category){}
    }

    //Todo: in sub menus add custom personalInfoMenu and also show method should support few more methods methods.
    public static class SellerMenu extends Menu {
        SellerMenu(String name, Menu parent) {
            super(name, )
        }
        @Override
        public void execute() {}
        public void viewCompanyInfo(){}
        public void viewSellHistory(){}
        public void viewCategories(){}
        public void viewBalance(){}
    }

    public static class SellerSalesMenu extends Menu {
        SellerSalesMenu(String name, Menu parent){}
        @Override
        public void execute() {}
        public void viewSales(){}
        public void editSale(){}
        public void addSale(){}
    }

    public static class SellerProductMenu extends Menu {
        SellerProductMenu(String name, Menu parent){}
        @Override
        public void execute() {}
        public void viewProductByID(String ID){}
        public void viewProductBuyersByID(String ID){}
        public void editProductByID(String ID){}
        public void addProduct(){}
        public void removeProduct(String ID){}
    }

    public abstract class TransactionLogMenu {
        TransactionLogMenu(String name, Menu parent) {}
    }

    //Todo: in sub menus add custom personalInfoMenu
    public static class CustomerMenu extends Menu {
        CustomerMenu(String name, Menu parent) {}
        @Override
        public void execute() {}
    }

    //this is both used in product menu and in customer menu
    //also should be able to handle both method call and menu call because of shopping cart
    public static class ShoppingCartMenu extends Menu {
        ShoppingCartMenu(String name, Menu parent){}
        @Override
        public void execute() {}
        public void showProducts(){}
        public void viewProductByID(String ID){}
        public void increaseProductCount(String ID){}
        public void decreaseProductCount(String ID){}
        public void ShowTotalPrice(){}
        public void purchase(){}
        public void viewBalance(){}
        public void viewDiscountCodes(){}
    }

    public static class CustomerOrderLogMenu extends Menu {
        CustomerOrderLogMenu(String name, Menu parent) {}
        @Override
        public void execute() {}
        public void showOrderByID(String ID){}
        public void rateProductByID(String ID, int rate){}
        private boolean hasBoughtProduct(String ID){}
    }
}
