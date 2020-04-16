package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class Menu {
    protected String name;
    protected boolean isAccountMenuAccessible;
    protected static Menu accountMenu;
    protected Menu parent;
    protected Map<Integer, Menu> subMenus;
    static protected ArrayList<Menu>  allMenus;
    static protected Scanner sc;
    protected String commandList;
    protected String commandPattern;

    static {
        sc = new Scanner(System.in);
        allMenus = new ArrayList<Menu>();
    }


    public Menu(String name, boolean isAccountMenuAccessible, Menu parent, String commandPattern, String commandList) {
        this.name = name;
        this.isAccountMenuAccessible = isAccountMenuAccessible;
        this.parent = parent;
        this.commandPattern = commandPattern;
        this.commandList = commandList;
        allMenus.add(this);
        subMenus = new HashMap<Integer, Menu>();

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

    public String getCommandList() {
        return commandList;
    }
    public void showCommandList() {
        System.out.println(commandList);
    }

    static protected void printArray(ArrayList<String> list) {
        for (String item : list) {
            System.out.println(item);
        }
    }

    protected void run() {
        this.show(null);
        this.execute(null);
    }

    protected void run(String command) {
        this.show(command);
        this.execute(command);
    }

    //Todo: implement
    static protected boolean isUserLoggedIn() {return false;}

    public void show(String command) {
        for (Integer menuIndex : subMenus.keySet()) {
            System.out.println(menuIndex + ". " + subMenus.get(menuIndex));
        }
        int index = subMenus.keySet().size();
        if (isAccountMenuAccessible) {
            System.out.println(++index  + ". " + accountMenu);
        }
        if (this.parent == null) {
            System.out.println(++index + ". exit");
        } else {
            System.out.println(++index + ". back");
        }
    }

    public boolean shouldGoBack(String command) {
        if (command.equalsIgnoreCase("back") || command.equalsIgnoreCase("exit")) {
            return true;
        } else {
            return false;
        }
    }

    public int getMenuIndex(Menu menu) throws Exception{
        for (Integer index : subMenus.keySet()) {
            if (subMenus.get(index).equals(menu)) {
                return index;
            }
        }
        throw new Exception("Not in the sub-menus");
    }

    public abstract void execute(String command);

    @Override
    public String toString() {
        return this.getName();
    }
}
