package view;

import java.util.*;

public abstract class Menu {
    private String name;
    private boolean isAccountMenuAccessible;
    private static Menus.AccountMenu accountMenu;
    private static Menus.ProductDetailMenu productDetailMenu;
    protected Menu parent;
    protected Map<Integer, Menu> subMenus;
    protected Map<Integer, Action> subActions;
    static ArrayList<Menu>  allMenus;
    static private Scanner sc;
    static private Stack<Menu> stackTrace;
    private String commandPattern;
    private String command;



    static {
        sc = new Scanner(System.in);
        allMenus = new ArrayList<Menu>();
        stackTrace = new Stack<>();
    }

    public Menu(String name, boolean isAccountMenuAccessible, Menu parent, String commandPattern, String command) {
        this.name = name;
        this.isAccountMenuAccessible = isAccountMenuAccessible;
        this.parent = parent;
        this.commandPattern = commandPattern;
        this.command = command;
        allMenus.add(this);
        subMenus = new HashMap<>();
        subActions = new HashMap<>();
        initSubMenus();
        initSubActions();
    }

    protected abstract void initSubMenus();
    protected abstract void initSubActions();

    public static void setAccountMenu(Menus.AccountMenu accountMenu) {
        Menu.accountMenu = accountMenu;
    }

    public static void setProductDetailMenu(Menus.ProductDetailMenu productDetailMenu) {
        Menu.productDetailMenu = productDetailMenu;
    }

    public static Menus.AccountMenu getAccountMenu() {
        return accountMenu;
    }

    public static Menus.ProductDetailMenu getProductDetailMenu() {
        return productDetailMenu;
    }

    static protected String getNextLineTrimmed() {
        return sc.nextLine().trim();
    }

    public String getName() {
        return name;
    }

    public String getCommandPattern() {
        return commandPattern;
    }

    public void showCommandList() {
        for (int index = 1; index <= subMenus.size(); index++) {
            System.out.println(subMenus.get(index).command + " or " + index);
        }
        for (int index = subMenus.size() + 1; index <= subMenus.size() + subActions.size(); index++) {
            System.out.println(subActions.get(index).getActionCommand() + " or " + index);
        }
    }

    static protected <T> void printArray(ArrayList<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    protected void run() {
        stackTrace.push(this);
        this.show();
        this.execute();
    }

    public void show() {
        System.out.println(this.name + ":");
        if (subMenus.size() != 0) {
            System.out.println("Sub Menus:");
        }
        int subMenuSize = subMenus.size();
        for (int index = 1; index <= subMenuSize; index++) {
            System.out.println(index + ". " + subMenus.get(index).getName());
        }
        if (isAccountMenuAccessible) {
            System.out.println((subMenuSize + 1) + ". " + accountMenu.getName());
        }

        if (subActions.size() != 0) {
            System.out.println("Available Actions:");
        }
        int subActionSize = subActions.size();
        for (int index = subMenus.size() + 1; index <= subMenuSize + subActionSize; index++) {
            System.out.println(index + ". " + subActions.get(index).getName());
        }

    }

    public void execute() {
        String command = getNextLineTrimmed();
        for (Integer menuIndex : subMenus.keySet()) {
            if (command.equals(Integer.toString(menuIndex)) || command.matches(subMenus.get(menuIndex).commandPattern)) {
                subMenus.get(menuIndex).run();
            }
        }
        for (Integer actionIndex : subActions.keySet()) {
            if (command.equals(Integer.toString(actionIndex)) || command.matches(subActions.get(actionIndex).getActionPattern())) {
                try {
                    subActions.get(actionIndex).run(command);
                } catch (Exception actionException) {
                    System.out.println(actionException.getMessage());
                }
                this.run();
            }
        }
        //TODO: if invalid command entered.
    }

    protected static Menu getCallingMenu() {
        Menu temp = stackTrace.pop();
        Menu callingMenu = stackTrace.peek();
        stackTrace.push(temp);
        return callingMenu;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
