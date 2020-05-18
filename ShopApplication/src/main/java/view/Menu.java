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
    static ArrayList<Menu> allMenus;
    static private Stack<Menu> stackTrace;
    private String commandPattern;
    private String command;

    static {
        allMenus = new ArrayList<>();
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
        for (Integer key : subActions.keySet()) {
            System.out.println(subActions.get(key).getActionCommand());
        }
    }

    protected void run() {
        stackTrace.push(this);
        this.show();
        this.execute();
    }

    public static Stack<Menu> getStackTrace() {
        return stackTrace;
    }

    public void show() {
        System.out.println(this.name + ":");
        if (subMenus.size() != 0) {
            System.out.println("Sub Menus:");
        }
        int subMenuSize = subMenus.size();
        for (int index = 1; index <= subMenuSize; index++) {
            System.out.println(index + ". " + subMenus.get(index).command);
        }
        if (isAccountMenuAccessible) {
            System.out.println((subMenuSize + 1) + ". " + accountMenu.getName());
        }

        if (subActions.size() != 0) {
            System.out.println("Available Actions:");
        }
        int subActionSize = subActions.size();
        int modification = floatingMenusIndexModification();
        for (int index = subMenuSize + modification + 1; index <= subMenuSize + subActionSize + modification; index++) {
            System.out.println(subActions.get(index).getActionCommand());
        }

    }

    public void execute() {
        String command = View.getNextLineTrimmed();
        if (command.equalsIgnoreCase("help")) {
            showCommandList();
            this.run();
        }
        for (Integer menuIndex : subMenus.keySet()) {
            if (command.equals(Integer.toString(menuIndex)) || command.matches(subMenus.get(menuIndex).commandPattern)) {
                subMenus.get(menuIndex).run();
            }
        }
        if (command.equals(Integer.toString(subMenus.size() + 1)) || command.matches(accountMenu.getCommandPattern())) {
            accountMenu.run();
        }
        for (Integer actionIndex : subActions.keySet()) {
            if (command.matches(subActions.get(actionIndex).getActionPattern())) {
                try {
                    subActions.get(actionIndex).run(command);
                } catch (Exception actionException) {
                    System.out.println(actionException.getMessage());
                }
                this.run();
            }
        }
        //if the command is invalid.
        System.out.println("invalid entry.");
        this.execute();
    }

    protected static Menu getCallingMenu() {
        Menu temp = stackTrace.pop();
        Menu callingMenu = stackTrace.peek();
        stackTrace.push(temp);
        return callingMenu;
    }


    protected int floatingMenusIndexModification() {
        if (isAccountMenuAccessible) {
            return 1;
        } else {
            return 0;
        }
    }

    Actions.BackAction getBackAction() {
        return (Actions.BackAction) subActions.get(subMenus.size() + subActions.size() + floatingMenusIndexModification());
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
