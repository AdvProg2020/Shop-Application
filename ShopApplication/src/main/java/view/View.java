package view;

import controller.*;
import com.sun.tools.javac.code.Directive;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Filter;

/**
 *  @author Dana
 *  This is the non-implemented sketch of the View
 **/

public class View {
    private Controller mainController;
    private CustomerController customerController;
    private AdminController adminController;
    private SellerController sellerController;


    //Todo: create Menus
    public View(Controller mainController, SellerController sellerController, AdminController adminController, CustomerController customerController) {
        this.mainController = mainController;
        this.sellerController = sellerController;
        this.customerController = customerController;
        this.adminController = adminController;
    }

    static private class Constants {

    }
    //Todo: addSubMenus, support back option for every field getter and menu. new all submenus or you'll get nullPtrExc.
    //Todo: make all stupid public methods private :||||
    //here we have all the blueprints and instances of all the menus we want.
    static private class Menus {
        public class AccountMenu extends Menu {
            private Menu previousMenu;
            private Menu nextMenu;

            AccountMenu(String name) {
                Menu.accountMenu = this;
                previousMenu = null;
                nextMenu = null;
            }
            public void run(Menu previousMenu, Menu nextMenu) {
                this.previousMenu = previousMenu;
                this.nextMenu = nextMenu;
                this.execute(null);
            }
            @Override
            public void execute(String command) {}
        }
        public static AccountMenu accountMenu;

        public class FirstMenu extends Menu {
            FirstMenu(String name) {
                subMenus.put(1, new SaleMenu("Sale menu", this));
                subMenus.put(2, new AllProductsMenu("products menu", this));
            }
            @Override
            public void execute(String command) {}
        }
        public static FirstMenu firstMenu;

        //both method and menu
        public class AllProductsMenu extends Menu {
            private ArrayList<String> categoryTree;
            AllProductsMenu(String name, Menu parent) {
            }
            @Override
            public void show(String command){}
            @Override
            public void execute(String command) {}
            public void showCurrentProducts(){}
            public void showCurrentCategories(){}
            public void showProductByID(){}
            public void chooseCategory(String category){}
            public void previousCategory(){}
        }

        public class ProductMenu extends Menu {
            private String productID;
            ProductMenu(String name, Menu parent){}
            @Override
            public void execute(String command) {}
            public void digest(){}
            //should select seller before adding.
            public void addToShoppingCart(){}
            public void showAttributes(){}
            public void compareByID(String otherProductID){}
            public void showComments(){}
            public void addComment(){}
        }

        public class SortMenu extends Menu {
            private String currentSort;
            private ArrayList<String> availableSorts;
            SortMenu(String name, Menu parent, ArrayList<String> availableSorts){}
            @Override
            public void execute(String command) {}
            public void viewAvailableSorts(){}
            public void setCurrentSort(String availableSort){
                this.currentSort = availableSort;
            }
            public String getCurrentSort(){
                return currentSort;
            }
        }

        //wtf is with filtering anyway
        public class FilterMenu extends Menu {
            private String currentFilter;
            private ArrayList<String> availableFilters;
            FilterMenu(String name, Menu parent, ArrayList<String> availableFilters){}
            @Override
            public void execute(String command) {}
            public void viewAvailableFilters(){}
            public void setCurrentFilter(){
                this.currentFilter = currentFilter;
            }
            public String getCurrentFilter() {
                return currentFilter;
            }
        }

        //handles both method and menu
        public class SaleMenu extends Menu {
            private String currentSort;
            private String currentFilter;
            SaleMenu(String name, Menu parent) {}
            public void show(){}
            @Override
            public void execute(String command) {}
            public void showCurrentSales(){}
            public void showProductByID(String ID){}
        }

        public class LoginMenu extends Menu{
            private String username;
            private String password;
            LoginMenu(String name, Menu parent){}
            @Override
            public void execute(String command) {}
        }

        public class RegisterMenu extends Menu {
            private String username;
            private String password;
            private String phoneNumber;
            private String firstName;
            private String lastName;
            private String emailAddress;
            private String type;
            RegisterMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            private void getCustomerInfo(){}
            private void getSellerInfo(){}
            private void getAdminInfo(){}
        }

        //Todo: in sub menus add custom personalInfoMenu
        public class AdminMenu extends Menu {
            AdminMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
        }


        public abstract class PersonalInfoMenu extends Menu{
            PersonalInfoMenu(String name, Menu parent) {}
        }

        public class UserManagingMenu extends Menu{
            UserManagingMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void viewUser(String command){}
            public void deleteUser(String command){}
            public void createAdmin(){}
        }

        public class ProductManagingMenu extends Menu{
            ProductManagingMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void removeProduct(String command) {}
        }

        //this one handles both creation and view discount codes. so supports to commands
        public class DiscountCodesMenu extends Menu{
            DiscountCodesMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void createDiscountCode(){}
            public void viewDiscountCodes(){}
            public void viewDiscountByCode(String code){}
            public void editDiscountCodes(String code){}
            public void removeDiscountCode(String code){}
        }

        public class RequestManagingMenu extends Menu {
            RequestManagingMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void viewRequestDetailsByID(String ID){}
            public void determineRequest(String ID, boolean isAccepted){}
        }

        public class CategoryManagingMenu extends Menu {
            CategoryManagingMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public boolean isCategoryValid(String category){
                return false;
            }
            public void addCategory(String category){}
            public void editCategory(String category){}
            public void removeCategory(String category){}
        }

        //Todo: in sub menus add custom personalInfoMenu and also show method should support few more methods methods.
        public class SellerMenu extends Menu {
            SellerMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void viewCompanyInfo(){}
            public void viewSellHistory(){}
            public void viewCategories(){}
            public void viewBalance(){}
        }

        public class SellerSalesMenu extends Menu {
            SellerSalesMenu(String name, Menu parent){}
            @Override
            public void execute(String command) {}
            public void viewSales(){}
            public void editSale(){}
            public void addSale(){}
        }

        public class SellerProductMenu extends Menu {
            SellerProductMenu(String name, Menu parent){}
            @Override
            public void execute(String command) {}
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
        public class CustomerMenu extends Menu {
            CustomerMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
        }

        //this is both used in product menu and in customer menu
        //also should be able to handle both method call and menu call because of shopping cart
        public class ShoppingCartMenu extends Menu {
            ShoppingCartMenu(String name, Menu parent){}
            @Override
            public void execute(String command) {}
            public void showProducts(){}
            public void viewProductByID(String ID){}
            public void increaseProductCount(String ID){}
            public void decreaseProductCount(String ID){}
            public void ShowTotalPrice(){}
            public void purchase(){}
            public void viewBalance(){}
            public void viewDiscountCodes(){}
        }

        public class CustomerOrderLogMenu extends Menu {
            CustomerOrderLogMenu(String name, Menu parent) {}
            @Override
            public void execute(String command) {}
            public void showOrderByID(String ID){}
            public void rateProductByID(String ID, int rate){}
            private boolean hasBoughtProduct(String ID){
                return false;
            }
        }
    }
}
