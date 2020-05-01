package view;

import java.lang.reflect.Array;
import java.util.*;

public abstract class Menu {
    protected String name;
    protected boolean isAccountMenuAccessible;
    protected static Menu accountMenu;
    protected Menu parent;
    protected Map<Integer, Menu> subMenus;
    protected Map<Integer, Action> subActions;
    static protected ArrayList<Menu>  allMenus;
    static protected Scanner sc;
    protected String commandPattern;
    protected String command;


    static {
        sc = new Scanner(System.in);
        allMenus = new ArrayList<Menu>();
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
        for (int index = 1; index <= subMenus.keySet().size(); index++) {
            System.out.println(subMenus.get(index).command + " or " + index);
        }
        for (int index = subMenus.keySet().size() + 1; index <= subMenus.keySet().size() + subActions.keySet().size(); index++) {
            System.out.println(subActions.get(index).getActionCommand() + " or " + index);
        }
    }

    static protected <T> void printArray(ArrayList<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    protected void run() {
        this.show();
        this.execute();
    }

    public void show() {
        if (subMenus.size() != 0) {
            System.out.println("Sub Menus:");
        }
        for (int index = 1; index <= subMenus.keySet().size(); index++) {
            System.out.println(index + ". " + subMenus.get(index).getName());
        }

        if (subActions.size() != 0) {
            System.out.println("Available Actions:");
        }
        for (int index = subMenus.keySet().size() + 1; index <= subActions.keySet().size() + subMenus.keySet().size(); index++) {
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
                subActions.get(actionIndex).execute(command);
            }
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
