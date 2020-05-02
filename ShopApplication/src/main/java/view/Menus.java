package view;

import java.util.ArrayList;


//Todo: addSubMenus, support back option for every field getter and menu. new all submenus or you'll get nullPtrExc.
//Todo: make all stupid public methods private :||||
//here we have all the blueprints and instances of all the menus we want.
public class Menus {
    //TODO: initSubActions havaset bashe bayad fargh kone. ye Action makhsos besaz.
    public static class AccountMenu extends Menu {
        private Menu previousMenu;
        private Menu nextMenu;

        AccountMenu(String name) {
            super(name, false, null, Constants.MenuCommandAndPattern.accountMenuPattern, Constants.MenuCommandAndPattern.accountMenuCommand);
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
//            subMenus.put(1, new AnonymousUserAccountMenu("Login/Register Menu", this));
//            subMenus.put(2, new AdminMenu("Admin Menu", this));
//            subMenus.put(3, new SellerMenu("Seller Menu", this));
//            subMenus.put(4, new CustomerMenu("Customer Menu", this));
        }

        @Override
        protected void initSubActions() {
            //no sub action available.
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
       //     subMenus.put(1, new SaleMenu("Sale menu", this));
            subMenus.put(2, new AllProductsMenu("products menu", this));
            subMenus.put(3, Menu.accountMenu);
        }

        @Override
        protected void initSubActions() {
            //no sub action available
        }
    }

    //both method and menu
    public static class AllProductsMenu extends Menu {
        private ArrayList<String> categoryTree;
        AllProductsMenu(String name, Menu parent) {
            super(name, true, parent, Constants.MenuCommandAndPattern.allProductsMenuPattern, Constants.MenuCommandAndPattern.allProductsMenuCommand);
            this.categoryTree = new ArrayList<>();
        }

        @Override
        protected void initSubMenus() {

        }

        @Override
        protected void initSubActions() {
            int index = subMenus.size();
            subActions.put(index + 1, new Actions.ShowProductsAction("show products", this.categoryTree));
            subActions.put(index + 2, new Actions.ShowCategories("show categories", this.categoryTree));
        }

        public void showCurrentCategories(){}
        public void showProductByID(){}
        public void chooseCategory(String category){}
        public void previousCategory(){}
    }

//    public static class ProductMenu extends Menu {
//        private String productID;
//        ProductMenu(String name, Menu parent){}
//        @Override
//        public void execute() {}
//        public void digest(){}
//        //should select seller before adding.
//        public void addToShoppingCart(){}
//        public void showAttributes(){}
//        public void compareByID(String otherProductID){}
//        public void showComments(){}
//        public void addComment(){}
//    }
//
//    public static class SortMenu extends Menu {
//        private String currentSort;
//        private ArrayList<String> availableSorts;
//        SortMenu(String name, Menu parent, ArrayList<String> availableSorts){}
//        @Override
//        public void execute() {}
//        public void viewAvailableSorts(){}
//        public void setCurrentSort(String availableSort){
//            this.currentSort = availableSort;
//        }
//        public String getCurrentSort(){
//            return currentSort;
//        }
//    }
//
//    //wtf is with filtering anyway
//    public static class FilterMenu extends Menu {
//        private String currentFilter;
//        private ArrayList<String> availableFilters;
//        FilterMenu(String name, Menu parent, ArrayList<String> availableFilters){}
//        @Override
//        public void execute() {}
//        public void viewAvailableFilters(){}
//        public void setCurrentFilter(){
//            this.currentFilter = currentFilter;
//        }
//        public String getCurrentFilter() {
//            return currentFilter;
//        }
//    }
//
//    //handles both method and menu
//    public static class SaleMenu extends Menu {
//        private String currentSort;
//        private String currentFilter;
//        SaleMenu(String name, Menu parent) {}
//        @Override
//        public void show(){}
//        @Override
//        public void execute() {}
//        public void showCurrentSales(){}
//        public void showProductByID(String ID){}
//    }
//
//    public static class AnonymousUserAccountMenu extends Menu {
//        AnonymousUserAccountMenu(String name, Menu parent) {
//            super(name, false, parent, null, null);
//        }
//
//        @Override
//        protected void initSubMenus() {
//            //no sub menu available.
//        }
//
//        @Override
//        protected void initSubActions() {
//            int index = subMenus.size();
//            subActions.put(index + 1, new Actions.LoginAction("Login"));
//            subActions.put(index + 2, new Actions.RegisterAction("Register"));
//        }
//    }
//
//    public static class LoginMenu extends Menu{
//        private String username;
//        private String password;
//        LoginMenu(String name, Menu parent){}
//        @Override
//        public void execute() {}
//    }
//
//    public static class RegisterMenu extends Menu {
//        private String username;
//        private String password;
//        private String phoneNumber;
//        private String firstName;
//        private String lastName;
//        private String emailAddress;
//        private String type;
//        RegisterMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        private void getCustomerInfo(){}
//        private void getSellerInfo(){}
//        private void getAdminInfo(){}
//    }
//
//    //Todo: in sub menus add custom personalInfoMenu
//    public static class AdminMenu extends Menu {
//        AdminMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//    }
//
//
//    public static abstract class PersonalInfoMenu extends Menu{
//        PersonalInfoMenu(String name, Menu parent);
//    }
//
//    public static class UserManagingMenu extends Menu{
//        UserManagingMenu(String name, Menu parent);
//        @Override
//        public void execute() {}
//        public void viewUser(String command){}
//        public void deleteUser(String command){}
//        public void createAdmin(){}
//    }
//
//    public static class ProductManagingMenu extends Menu{
//        ProductManagingMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public void removeProduct(String command);
//    }
//
//    //this one handles both creation and view discount codes. so supports to commands
//    public static class DiscountCodesMenu extends Menu{
//        DiscountCodesMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public void createDiscountCode(){}
//        public void viewDiscountCodes(){}
//        public void viewDiscountByCode(String code){}
//        public void editDiscountCodes(String code){}
//        public void removeDiscountCode(String code){}
//    }
//
//    public static class RequestManagingMenu extends Menu {
//        RequestManagingMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public void viewRequestDetailsByID(String ID){}
//        public void determineRequest(String ID, boolean isAccepted){}
//    }
//
//    public static class CategoryManagingMenu extends Menu {
//        CategoryManagingMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public boolean isCategoryValid(String category){}
//        public void addCategory(String category){}
//        public void editCategory(String category){}
//        public void removeCategory(String category){}
//    }
//
//    //Todo: in sub menus add custom personalInfoMenu and also show method should support few more methods methods.
//    public static class SellerMenu extends Menu {
//        SellerMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public void viewCompanyInfo(){}
//        public void viewSellHistory(){}
//        public void viewCategories(){}
//        public void viewBalance(){}
//    }
//
//    public static class SellerSalesMenu extends Menu {
//        SellerSalesMenu(String name, Menu parent){}
//        @Override
//        public void execute() {}
//        public void viewSales(){}
//        public void editSale(){}
//        public void addSale(){}
//    }
//
//    public static class SellerProductMenu extends Menu {
//        SellerProductMenu(String name, Menu parent){}
//        @Override
//        public void execute() {}
//        public void viewProductByID(String ID){}
//        public void viewProductBuyersByID(String ID){}
//        public void editProductByID(String ID){}
//        public void addProduct(){}
//        public void removeProduct(String ID){}
//    }
//
//    public abstract class TransactionLogMenu {
//        TransactionLogMenu(String name, Menu parent) {}
//    }
//
//    //Todo: in sub menus add custom personalInfoMenu
//    public static class CustomerMenu extends Menu {
//        CustomerMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//    }
//
//    //this is both used in product menu and in customer menu
//    //also should be able to handle both method call and menu call because of shopping cart
//    public static class ShoppingCartMenu extends Menu {
//        ShoppingCartMenu(String name, Menu parent){}
//        @Override
//        public void execute() {}
//        public void showProducts(){}
//        public void viewProductByID(String ID){}
//        public void increaseProductCount(String ID){}
//        public void decreaseProductCount(String ID){}
//        public void ShowTotalPrice(){}
//        public void purchase(){}
//        public void viewBalance(){}
//        public void viewDiscountCodes(){}
//    }
//
//    public static class CustomerOrderLogMenu extends Menu {
//        CustomerOrderLogMenu(String name, Menu parent) {}
//        @Override
//        public void execute() {}
//        public void showOrderByID(String ID){}
//        public void rateProductByID(String ID, int rate){}
//        private boolean hasBoughtProduct(String ID){}
//    }
}
