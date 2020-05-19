package view;

import java.util.*;

public abstract class Menu {
    private String name;
    private boolean isAccountMenuAccessible;
    private static Menus.AccountMenu accountMenu;
    protected Menu parent;
    protected Map<Integer, Menu> subMenus;
    protected Map<Integer, Action> subActions;
    static ArrayList<Menu> allMenus;
    private String commandPattern;
    private String command;

    static {
        allMenus = new ArrayList<>();
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

    public static Menus.AccountMenu getAccountMenu() {
        return accountMenu;
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
        this.show();
        this.execute();
    }

    public void show() {
        System.out.println( "|" + this.name.toUpperCase() + "|");
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
            accountMenu.run(this);
        }
        for (Integer actionIndex : subActions.keySet()) {
            if (command.matches(subActions.get(actionIndex).getActionPattern())) {
                subActions.get(actionIndex).run(command);
                this.run();
            }
        }
        //if the command is invalid.
        System.out.println("invalid entry.");
        this.execute();
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
